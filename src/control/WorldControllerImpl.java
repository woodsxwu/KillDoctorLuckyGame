package control;

import control.commands.*;
import facade.GameFacade;
import model.space.Space;
import model.viewmodel.ViewModel;
import view.GameView;
import view.KeyboardListener;
import view.MouseActionListener;
import view.ButtonListener;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implementation of the WorldController interface. This class is responsible
 * for managing the game state and user input/output.
 */
public class WorldControllerImpl implements WorldController {

  private final GameFacade facade;
  private final Scanner scanner;
  private final Appendable output;
  private final GameView view;
  private final Map<String, CommandFactory> setupCommands;
  private final Map<String, CommandFactory> gameplayCommands;
  private final Map<Integer, Runnable> keyActions;
  private final Map<String, Runnable> mouseActions;
  private final Map<String, Runnable> buttonActions;
  private boolean isGameSetup;
  private boolean isGameQuit;
  private final boolean isGuiMode;
  private final ViewModel viewModel;

  /**
   * Constructs a new WorldControllerImpl with the given facade, input, and output streams.
   */
  public WorldControllerImpl(GameFacade facade, Readable input, Appendable output, GameView view, ViewModel viewModel) {
    if (facade == null || input == null || output == null) {
      throw new IllegalArgumentException("Facade, input, and output must not be null");
    }
    this.facade = facade;
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
    this.viewModel = viewModel;
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
    buttonActions.put("New Game", this::handleNewGame);
    buttonActions.put("New Game Current World", this::handleNewGameCurrentWorld);
    buttonActions.put("Quit", () -> System.exit(0));
    buttonActions.put("Add Human Player", () -> handleAddPlayer(true));
    buttonActions.put("Add Computer Player", () -> handleAddPlayer(false));
    buttonActions.put("Start Game", this::handleGameStart);

    // Initialize key actions
    keyActions.put(KeyEvent.VK_P, () -> executeCommand("pick"));
    keyActions.put(KeyEvent.VK_L, () -> executeCommand("look"));
    keyActions.put(KeyEvent.VK_A, () -> executeCommand("attack"));
    keyActions.put(KeyEvent.VK_M, () -> executeCommand("move-pet"));
    keyActions.put(KeyEvent.VK_I, () -> executeCommand("player-info"));

    // Initialize mouse actions
    mouseActions.put("click", () -> handleSpaceClick());
  }

  private void configureListeners() {
  // Remove map setters and pass maps directly in constructor
  view.addActionListener(new ButtonListener(buttonActions));

  KeyboardListener keyboardListener = new KeyboardListener();
  keyboardListener.setKeyPressedMap(keyActions);
  view.addKeyListener(keyboardListener);

  // Remove setMouseActions call and pass map in constructor
  view.addMouseListener(new MouseActionListener(mouseActions));
}
  @Override
  public void startGame(int maxTurns) {
    facade.setMaxTurns(maxTurns);
    
    if (isGuiMode) {
      startGuiGame();
    } else {
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
    view.showFileChooser();
    view.showGameScreen();
  }

  private void handleNewGameCurrentWorld() {
    view.showGameScreen();
  }

  private void handleAddPlayer(boolean isHuman) {
    String name = view.showInputDialog("Enter player name:");
    String space = view.showInputDialog("Enter starting space:");
    String itemLimit = view.showInputDialog("Enter item carrying capacity (-1 for unlimited):");
    
    try {
      int capacity = Integer.parseInt(itemLimit);
      if (isHuman) {
        facade.addHumanPlayer(name, space, capacity);
      } else {
        facade.addComputerPlayer(name, space, capacity);
      }
      view.displayMessage("Player " + name + " added successfully");
      view.refreshWorld();
    } catch (Exception e) {
      view.displayMessage("Error adding player: " + e.getMessage());
    }
  }

  private void handleGameStart() {
    if (facade.getPlayerCount() == 0) {
      view.displayMessage("Add at least one player before starting");
      return;
    }
    isGameSetup = true;
    view.refreshWorld();
    view.displayMessage("Game started!");
  }

  private void handleSpaceClick() {
    if (!isGameSetup || facade.computerPlayerTurn()) {
        return;
    }
    
    Point clickPoint = view.getLastClickPoint();
    String spaceName = view.getSpaceAtPoint(clickPoint);
    if (spaceName != null) {
      // Add validation using ViewModel
      List<Space> spaces = viewModel.getSpaceCopies();
      Space currentSpace = null;
      Space targetSpace = null;
      
      // Find current and target spaces
      for (Space space : spaces) {
        if (space.getSpaceIndex() == facade.getCurrentPlayer().getCurrentSpaceIndex()) {
          currentSpace = space;
        }
        if (space.getSpaceName().equals(spaceName)) {
          targetSpace = space;
        }
      }
      
      // Validate move before executing command
      if (currentSpace != null && targetSpace != null && 
        currentSpace.hasNeighbor(targetSpace.getSpaceIndex())) {
        executeCommand("move", spaceName);
      } else {
        view.displayMessage("Invalid move: Space is not accessible");
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
      displayResult(result);
      
      if (facade.isGameEnded()) {
        handleGameEnd();
      }
    } catch (Exception e) {
      displayError("Error executing command: " + e.getMessage());
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
  
  private void displayResult(String result) {
    if (isGuiMode) {
      view.displayMessage(result);
      view.refreshWorld();
    } else {
      try {
        output.append(result).append("\n");
      } catch (IOException e) {
        System.err.println("Error displaying result: " + e.getMessage());
      }
    }
  }

  private void displayError(String error) {
    if (isGuiMode) {
      view.displayMessage("Error: " + error);
    } else {
      try {
        output.append("Error: ").append(error).append("\n");
      } catch (IOException e) {
        System.err.println("Error displaying error: " + e.getMessage());
      }
    }
  }
}