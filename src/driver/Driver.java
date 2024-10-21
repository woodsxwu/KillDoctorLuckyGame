package driver;

import control.WorldController;
import control.WorldControllerImpl;
import facade.GameFacade;
import facade.GameFacadeImpl;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import model.world.World;
import model.world.WorldFactory;

/**
 * Driver class for the game. Handles command-line arguments and initializes the game.
 */
public class Driver {

  /**
   * Main method to run the game.
   *
   * @param args Command-line arguments. Expects two arguments:
   *             1. Path to the world specification file
   *             2. Maximum number of turns
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: java Driver <world_file_path> <max_turns>");
      System.exit(1);
    }

    String worldFilePath = args[0];
    int maxTurns;

    try {
      maxTurns = Integer.parseInt(args[1]);
      if (maxTurns <= 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      System.out.println("Error: Max turns must be a positive integer.");
      return;
    }

    try {
      // Create the world
      WorldFactory factory = new WorldFactory();
      World world = factory.createWorld(new InputStreamReader(new FileInputStream(worldFilePath)));

      // Create the game facade
      GameFacade facade = new GameFacadeImpl(world);

      // Create the controller with Readable input and Appendable output
      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      WorldController controller = new WorldControllerImpl(facade, input, output);

      // Start the game
      controller.startGame(maxTurns);

    } catch (FileNotFoundException e) {
      System.out.println("Error: World file not found: " + worldFilePath);
      System.exit(1);
    }
  }
}
