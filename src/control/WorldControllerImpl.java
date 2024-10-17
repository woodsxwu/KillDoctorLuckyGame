package control;

import facade.GameFacade;
import java.util.Scanner;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

import control.commands.AddComputerPlayerCommand;
import control.commands.AddHumanPlayerCommand;
import control.commands.CreateWorldMapCommand;
import control.commands.DisplayPlayerInfoCommand;
import control.commands.DisplaySpaceInfoCommand;
import control.commands.GameCommand;
import control.commands.LookAroundCommand;
import control.commands.MoveCommand;
import control.commands.PickUpItemCommand;

/**
 * Implementation of the WorldController interface. This class manages the game
 * flow and user interactions.
 */
public class WorldControllerImpl implements WorldController {

  private final GameFacade facade;
  private final Scanner scanner;
  private final Appendable output;
  private final Map<String, Function<Scanner, GameCommand>> knownCommands;

  /**
   * Constructs a new WorldControllerImpl.
   *
   * @param facade the game facade
   * @param input  the input source
   * @param output the output destination
   */
  public WorldControllerImpl(GameFacade facade, Readable input, Appendable output) {
    if (facade == null || input == null || output == null) {
      throw new IllegalArgumentException("Facade, input, and output must not be null");
    }
    this.facade = facade;
    this.scanner = new Scanner(input);
    this.output = output;
    this.knownCommands = new HashMap<>();
    initializeCommands();
  }

  private void initializeCommands() {
    knownCommands.put("move", s -> new MoveCommand(s.next()));
    knownCommands.put("pick", s -> new PickUpItemCommand(s.next()));
    knownCommands.put("look", s -> new LookAroundCommand());
    knownCommands.put("map", s -> new CreateWorldMapCommand());
    knownCommands.put("space", s -> new DisplaySpaceInfoCommand(s.next()));
    knownCommands.put("add-human", s -> new AddHumanPlayerCommand(s.next(), s.next(), s.nextInt()));
    knownCommands.put("add-computer",
        s -> new AddComputerPlayerCommand(s.next(), s.next(), s.nextInt()));
    knownCommands.put("player-info", s -> new DisplayPlayerInfoCommand(s.next()));
    // TODO: ADD HELP CoMMAND
  }

  @Override
  public void startGame(int maxTurns) {
    try {
      output.append(String.format("Starting game with %d turns\n", maxTurns));
      facade.setMaxTurns(maxTurns);

      while (!facade.isGameEnded()) {
        output.append(String.format("Turn %d, Current player: %s\n", facade.getCurrentTurn(),
            facade.getCurrentPlayerName()));

        if (facade.computerPlayerTurn()) {
          output.append("Computer player turn\n");
          facade.computerPlayerTakeTurn();
        } else {
          output.append("Enter command: ");

          String command = scanner.next();
          if (knownCommands.containsKey(command)) {
            GameCommand gameCommand = knownCommands.get(command).apply(scanner);
            String result = gameCommand.execute(facade);
            output.append(result).append("\n");
          } else if ("quit".equalsIgnoreCase(command)) {
            break;
          } else {
            output.append("Unknown command. Try again.\n");
          }
        }
      }

      output.append("Game over!\n");
    } catch (IOException e) {
      System.err.println("An I/O error occurred while saving the file: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println("Invalid argument: " + e.getMessage());
    } finally {
      scanner.close();
    }
  }
}