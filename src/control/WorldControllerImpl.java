package control;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import control.commands.AddComputerPlayerCommand;
import control.commands.AddHumanPlayerCommand;
import control.commands.AttackCommand;
import control.commands.CommandFactory;
import control.commands.CreateWorldMapCommand;
import control.commands.DisplayPlayerInfoCommand;
import control.commands.DisplaySpaceInfoCommand;
import control.commands.GameCommand;
import control.commands.HelpCommand;
import control.commands.LookAroundCommand;
import control.commands.MoveCommand;
import control.commands.MovePetCommand;
import control.commands.PickUpItemCommand;
import facade.GameFacade;
import facade.GameFacadeImpl;
import model.player.Player;
import model.viewmodel.ViewModel;
import model.world.World;
import model.world.WorldFactory;
import view.ButtonListener;
import view.GameView;
import view.KeyboardListener;
import view.MouseActionListener;

/**
 * Implementation of the WorldController interface. This class is responsible
 * for managing the game state and user input/output.
 */
public class WorldControllerImpl implements WorldController {

  private static final int MAX_PLAYERS = 10;
  private final Scanner scanner;
  private final Appendable output;
  private final GameView view;
  private final Map<String, CommandFactory> setupCommands;
  private final Map<String, CommandFactory> gameplayCommands;
  private final Map<Integer, Runnable> keyActions;
  private final Map<String, Consumer<MouseEvent>> mouseActions;
  private final Map<String, Runnable> buttonActions;
  private final boolean isGuiMode;
  private boolean isGameSetup;
  private boolean isGameQuit;
  private int maxTurns;
  private boolean isPetMoveMode;

  private GameFacade facade;
  private ViewModel viewModel;
  private String currentWorldFile;

  /**
   * Constructs a new WorldControllerImpl with the given facade, input, and output
   * streams.
   */
  public WorldControllerImpl(Readable input, Appendable output, GameView view, String worldFile) {
    if (input == null || output == null) {
      throw new IllegalArgumentException("input, and output must not be null");
    }
    this.scanner = new Scanner(input);
    this.output = output;
    this.view = view;
    this.isGuiMode = (view != null);
    this.setupCommands = new HashMap<>();
    this.gameplayCommands = new HashMap<>();
    this.keyActions = new HashMap<>();
    this.mouseActions = new HashMap<>();
    this.buttonActions = new HashMap<>();
    this.isGameSetup = false;
    this.isGameQuit = false;
    this.currentWorldFile = worldFile;
    this.isPetMoveMode = false;

    this.facade = null;
    this.viewModel = null;

    initializeCommands();
    if (isGuiMode) {
      initializeActions();
      configureListeners();
    }
  }

  private void initializeCommands() {
    // Setup commands
    setupCommands.put("add-human", new AddHumanPlayerCommand(null, null, 0));
    setupCommands.put("add-computer", new AddComputerPlayerCommand(null, null, 0));
    setupCommands.put("map", new CreateWorldMapCommand());
    setupCommands.put("help", new HelpCommand(true));

    // Gameplay commands
    gameplayCommands.put("move", new MoveCommand(null));
    gameplayCommands.put("pick", new PickUpItemCommand(null));
    gameplayCommands.put("look", new LookAroundCommand());
    gameplayCommands.put("space", new DisplaySpaceInfoCommand(null));
    gameplayCommands.put("player-info", new DisplayPlayerInfoCommand(null));
    gameplayCommands.put("help", new HelpCommand(false));
    gameplayCommands.put("attack", new AttackCommand(null));
    gameplayCommands.put("move-pet", new MovePetCommand(null));
  }

  private void initializeActions() {
    // Initialize button actions
    buttonActions.put("NewGame", this::handleNewGame);
    buttonActions.put("NewGameCurrentWorld", this::handleNewGameCurrentWorld);
    buttonActions.put("Quit", () -> System.exit(0));
    buttonActions.put("Add Human Player", () -> handleAddPlayer(true));
    buttonActions.put("Add Computer Player", () -> handleAddPlayer(false));
    buttonActions.put("Start Game", this::handleGameStart);

    // Initialize key actions
    keyActions.put(KeyEvent.VK_P, () -> handlePickUpItem());
    keyActions.put(KeyEvent.VK_L, () -> executeCommand("look"));
    keyActions.put(KeyEvent.VK_A, () -> handleAttackCommand());
    keyActions.put(KeyEvent.VK_I, () -> executeCommand("player-info"));
    keyActions.put(KeyEvent.VK_M, () -> {
      if (!isGameSetup || facade.computerPlayerTurn()) {
        return;
      }
      isPetMoveMode = true;
      view.updateStatusDisplay("Pet movement mode activated. Click a space to move the pet.");
    });

    // Initialize mouse actions
    mouseActions.put("click", e -> {
      view.setLastClickPoint(e.getPoint());
      if (isPetMoveMode) {
        handlePetMove();
      } else {
        handleSpaceClick();
      }
    });
  }

  private void handlePetMove() {
    if (!isGameSetup || facade.computerPlayerTurn()) {
      return;
    }

    Point clickPoint = view.getLastClickPoint();
    String spaceName = view.getSpaceAtPoint(clickPoint);

    if (spaceName != null) {
      try {
        String result = facade.movePet(spaceName);
        view.updateStatusDisplay(result);
        view.refreshWorld();
        isPetMoveMode = false; // Exit pet move mode after successful move

        if (facade.isGameEnded()) {
          handleGameEnd();
        }
      } catch (IllegalArgumentException e) {
        view.showError("Invalid pet movement: " + e.getMessage());
      }
    }
  }

  private void handlePickUpItem() {
    if (!isGameSetup || facade.computerPlayerTurn()) {
      return;
    }

    String selectedItem = view.showItemPickerDialog();
    if (selectedItem != null) {
      executeCommand("pick", selectedItem);
    }
  }

  private void initializeGame(String filePath, int maxTurns) {
    try {
      // Reset game state
      isGameSetup = false;
      isGameQuit = false;

      WorldFactory worldFactory = new WorldFactory();

      // Create new world from selected file
      World newWorld = worldFactory
          .createWorld(new InputStreamReader(new FileInputStream(filePath)));

      // Initialize game components
      this.facade = new GameFacadeImpl(newWorld);
      this.viewModel = (ViewModel) newWorld;

      // Important: Set the view model before creating the map
      this.view.setViewModel(viewModel);

      // Create world map image and set it
      BufferedImage worldImage = facade.createWorldMap();

      // This is crucial - explicitly tell the view to update with new image
      view.setWorldImage(worldImage);
      view.refreshWorld();

      // add mouse listener
      view.addMouseListener(new MouseActionListener(mouseActions));
      this.currentWorldFile = filePath;
      facade.setMaxTurns(maxTurns);
    } catch (FileNotFoundException e) {
      view.showError("Error loading world file: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      view.showError(e.getMessage());
    } catch (IOException e) {
      view.showError(e.getMessage());
    }
  }

  private void configureListeners() {
    view.addActionListener(new ButtonListener(buttonActions));

    KeyboardListener keyboardListener = new KeyboardListener();
    keyboardListener.setKeyPressedMap(keyActions);
    view.addKeyListener(keyboardListener);
  }

  @Override
  public void startGame(int maxTurns) {
    this.maxTurns = maxTurns;

    if (isGuiMode) {
      startGuiGame();
    } else {
      initializeGame(currentWorldFile, maxTurns);
      facade.setMaxTurns(maxTurns);
      startTextGame();
    }
  }

  private void startGuiGame() {
    view.initialize();
    view.makeVisible();
  }

  private void startTextGame() {
    try {
      setupGame();
      playGame();
    } catch (IOException e) {
      System.err.println("An I/O error occurred: " + e.getMessage());
    }
  }

  // GUI Action Handlers
  private void handleNewGame() {
    String filePath = view.showFileChooser();
    if (filePath != null) {
      try {
        initializeGame(filePath, maxTurns);
        if (viewModel != null) { // Only proceed if initialization was successful
          view.showSetupScreen();
        } else {
          view.showError("Failed to initialize game");
        }
      } catch (Exception e) {
        view.showError("Error initializing game: " + e.getMessage());
      }
    } else {
      view.showError("No file selected");
    }
  }

  private void handleNewGameCurrentWorld() {
    initializeGame(currentWorldFile, maxTurns);
    view.showSetupScreen();
  }

  private void handleAddPlayer(boolean isHuman) {
    // First check if we've reached the player limit
    if (facade.getPlayerCount() >= MAX_PLAYERS) {
      view.showError(String.format("Cannot add more players. Maximum limit of %d players reached.",
          MAX_PLAYERS));
      return;
    }

    String name = view.showInputDialog("Enter player name:");
    if (name == null || name.trim().isEmpty()) {
      return; // User cancelled or entered empty name
    }

    // Use the new space picker dialog instead of text input
    String space = view.showSpacePickerDialog();
    if (space == null) {
      return; // User cancelled space selection
    }

    String itemLimit = view.showInputDialog("Enter item carrying capacity (-1 for unlimited):");
    if (itemLimit == null || itemLimit.trim().isEmpty()) {
      return; // User cancelled or entered empty capacity
    }

    try {
      int capacity = Integer.parseInt(itemLimit);
      GameCommand command = isHuman ? new AddHumanPlayerCommand(name, space, capacity)
          : new AddComputerPlayerCommand(name, space, capacity);

      String result = command.execute(facade);
      view.showMessage(result, JOptionPane.INFORMATION_MESSAGE);
      if (result.contains("successfully")) {
        view.addPlayerToList(name, space, capacity, isHuman);
        view.refreshWorld();

        // Check if we've reached max players after successful addition
        if (facade.getPlayerCount() >= MAX_PLAYERS) {
          view.showMessage("Maximum number of players (" + MAX_PLAYERS
              + ") reached. No more players can be added.", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    } catch (NumberFormatException e) {
      view.showError("Invalid capacity value: " + e.getMessage());
    } catch (IllegalStateException | IllegalArgumentException e) {
      view.showError(e.getMessage());
    }
  }

  private void handleGameStart() {
    if (facade.getPlayerCount() == 0) {
      view.showError("Add at least one player before starting");
      return;
    }

    // Set game state
    isGameSetup = true;

    // Update turn display
    view.updateTurnDisplay(facade.getCurrentPlayerName(), facade.getCurrentTurn());

    view.refreshWorld();
    view.showGameScreen();

    handleTurnChange();
  }

  private void handleSpaceClick() {
    if (!isGameSetup || facade.computerPlayerTurn()) {
      return;
    }

    Point clickPoint = view.getLastClickPoint();

    // First check if a player was clicked
    Player clickedPlayer = view.getPlayerAtPoint(clickPoint);
    if (clickedPlayer != null) {
      String playerInfo = clickedPlayer.getDescription(viewModel.getSpaceCopies());
      view.updateGameInfo(playerInfo);
      return;
    }

    // If no player was clicked, handle space click as before
    String spaceName = view.getSpaceAtPoint(clickPoint);
    if (spaceName != null) {
      GameCommand moveCommand = new MoveCommand(spaceName);
      String result = moveCommand.execute(facade);
      view.refreshWorld();
      view.updateStatusDisplay(result);
      handleTurnChange();

      if (facade.isGameEnded()) {
        handleGameEnd();
      }
    }
  }

  private void executeCommand(String command, String... args) {
    try {
      GameCommand gameCommand;
      if (setupCommands.containsKey(command)) {
        gameCommand = setupCommands.get(command).create(args);
      } else if (gameplayCommands.containsKey(command)) {
        gameCommand = gameplayCommands.get(command).create(args);
      } else {
        throw new IllegalArgumentException("Unknown command: " + command);
      }

      String result = gameCommand.execute(facade);

      // Update game display with command results
      if (isGuiMode) {
        // All command results including look should go to status display (turn results)
        view.updateStatusDisplay(result);
        view.refreshWorld();
        handleTurnChange();
      }

      if (facade.isGameEnded()) {
        handleGameEnd();
      }
    } catch (IllegalArgumentException e) {
      view.showError("Error executing command: " + e.getMessage());
    }
  }

  private void handleGameEnd() {
    String winner = facade.getWinner();
    if (isGuiMode) {
      view.showGameEndDialog(winner);
    } else {
      try {
        output.append(winner).append("\nGame over!\n");
      } catch (IOException e) {
        System.err.println("Error displaying game end: " + e.getMessage());
      }
    }
  }

  private void setupGame() throws IOException {
    output.append("Welcome to the game! Please add players before starting.\n");
    output.append("Use \"\" to wrap names with multiple words.\n");
    output.append("Type 'help' for available commands.\n");

    while (!isGameSetup) {
      String[] commandAndArgs = getNextCommand();
      String command = commandAndArgs[0];
      String[] args = new String[commandAndArgs.length - 1];
      System.arraycopy(commandAndArgs, 1, args, 0, args.length);

      output.append("--------------------------------------\n");
      if ("start".equals(command)) {
        if (facade.getPlayerCount() > 0) {
          isGameSetup = true;
          output.append("Game setup complete. Starting the game...\n");
        } else {
          output.append("Please add at least one player before starting the game.\n");
        }
      } else if (setupCommands.containsKey(command)) {
        try {
          GameCommand gameCommand = setupCommands.get(command).create(args);
          String result = gameCommand.execute(facade);
          output.append(result).append("\n");
        } catch (IllegalArgumentException e) {
          output.append("Error: ").append(e.getMessage()).append("\n");
        }
      } else if ("quit".equals(command)) {
        output.append("Setup aborted. Exiting game.\n");
        isGameQuit = true;
        return;
      } else {
        output.append("Unknown command. Type 'help' for available commands.\n");
      }
    }
  }

  private void playGame() throws IOException {
    if (isGameQuit) {
      return;
    }
    output.append(String.format("Starting game with %d turns\n", facade.getMaxTurns()));

    while (!facade.isGameEnded()) {
      output.append("--------------------------------------\n");
      output.append(String.format("Turn %d, Current player: %s\n", facade.getCurrentTurn(),
          facade.getCurrentPlayerName()));
      output.append(facade.limitedInfo()).append("\n");
      output.append(facade.getTargetInfo()).append("\n");

      if (facade.computerPlayerTurn()) {
        output.append("Computer player turn\n");
        try {
          output.append(facade.computerPlayerTakeTurn()).append("\n");
        } catch (IllegalArgumentException e) {
          output.append("Error: ").append(e.getMessage()).append("\n");
        }
      } else {
        String[] commandAndArgs = getNextCommand();
        String command = commandAndArgs[0];
        String[] args = new String[commandAndArgs.length - 1];
        System.arraycopy(commandAndArgs, 1, args, 0, args.length);

        if (gameplayCommands.containsKey(command)) {
          try {
            GameCommand gameCommand = gameplayCommands.get(command).create(args);
            String result = gameCommand.execute(facade);
            output.append(result).append("\n");
          } catch (IllegalArgumentException e) {
            output.append("Error: ").append(e.getMessage()).append("\n");
          }
        } else if ("quit".equals(command)) {
          break;
        } else {
          output.append("Unknown command. Type 'help' for available commands.\n");
        }
      }
    }
    try {
      output.append(facade.getWinner()).append("\n");
    } catch (IllegalStateException e) {
      output.append("User quit game.").append("\n");
    }
    output.append("Game over!\n");
  }

  private String[] getNextCommand() throws IOException {
    output.append("Enter command: ");
    String fullCommand = scanner.nextLine().trim();
    return parseCommand(fullCommand);
  }

  /**
   * Parses a command string into an array of strings, splitting on spaces and
   * preserving quoted strings.
   * 
   * @param input The input string to parse
   * @return An array of strings representing the parsed command
   */
  private String[] parseCommand(String input) {
    List<String> parts = new ArrayList<>();
    StringBuilder currentPart = new StringBuilder();
    boolean inQuotes = false;
    for (char c : input.toCharArray()) {
      if (c == '"') {
        inQuotes = !inQuotes;
      } else if (c == ' ' && !inQuotes) {
        if (currentPart.length() > 0) {
          parts.add(currentPart.toString());
          currentPart = new StringBuilder();
        }
      } else {
        currentPart.append(c);
      }
    }
    if (currentPart.length() > 0) {
      parts.add(currentPart.toString());
    }
    return parts.toArray(new String[0]);
  }

  private void handleTurnChange() {
    // Only proceed if game is setup and not ended
    if (!isGameSetup || facade.isGameEnded()) {
      return;
    }

    // Update the view
    view.updateTurnDisplay(facade.getCurrentPlayerName(), facade.getCurrentTurn());

    // Keep processing computer turns until either:
    // 1. We reach a human player's turn
    // 2. The game ends
    while (facade.computerPlayerTurn() && !facade.isGameEnded()) {
      try {
        String result = facade.computerPlayerTakeTurn();
        view.updateStatusDisplay(result);
        view.refreshWorld();

        // If the game hasn't ended, update the display for the next player
        if (!facade.isGameEnded()) {
          view.updateTurnDisplay(facade.getCurrentPlayerName(), facade.getCurrentTurn());
        }
      } catch (Exception e) {
        view.showError("Error during computer turn: " + e.getMessage());
        break; // Exit the loop if an error occurs
      }
    }

    // Check if the game has ended after computer players' turns
    if (facade.isGameEnded()) {
      handleGameEnd();
    }
  }

  private void handleAttackCommand() {
    if (!isGameSetup || facade.computerPlayerTurn()) {
      return;
    }

    String selectedItem = view.showAttackItemDialog();
    if (selectedItem != null) {
      executeCommand("attack", selectedItem);
    }
  }
}