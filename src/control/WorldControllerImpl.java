package control;

import facade.GameFacade;
import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import control.commands.*;

/**
 * Implementation of the WorldController interface. This class is responsible
 * for managing the game state and user input/output.
 */
public class WorldControllerImpl implements WorldController {

  private final GameFacade facade;
  private final Scanner scanner;
  private final Appendable output;
  private final Map<String, CommandFactory> setupCommands;
  private final Map<String, CommandFactory> gameplayCommands;
  private boolean isGameSetup = false;
  private boolean isGameQuit = false;

  /**
   * Constructs a new WorldControllerImpl with the given facade, input, and output
   * streams.
   * 
   * @param facade The facade to use for game state
   * @param input  The input stream to read user input from
   * @param output The output stream to write game output to
   */
  public WorldControllerImpl(GameFacade facade, Readable input, Appendable output) {
    if (facade == null || input == null || output == null) {
      throw new IllegalArgumentException("Facade, input, and output must not be null");
    }
    this.facade = facade;
    this.scanner = new Scanner(input);
    this.output = output;
    this.setupCommands = new HashMap<>();
    this.gameplayCommands = new HashMap<>();
    initializeCommands();
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
  }

  @Override
  public void startGame(int maxTurns) {
    try {
      setupGame();
      playGame(maxTurns);
    } catch (IOException e) {
      System.err.println("An I/O error occurred: " + e.getMessage());
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

  private void playGame(int maxTurns) throws IOException {
    if (isGameQuit) {
      return;
    }
    facade.setMaxTurns(maxTurns);
    output.append(String.format("Starting game with %d turns\n", maxTurns));

    while (!facade.isGameEnded()) {
      output.append(String.format("Turn %d, Current player: %s\n", facade.getCurrentTurn(),
          facade.getCurrentPlayerName()));

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
}