package driver;

import control.WorldController;
import control.WorldControllerImpl;
import view.GameViewImpl;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Driver class for the game. Handles command-line arguments and initializes the
 * game.
 */
public class Driver {
  /**
   * Main method to run the game.
   *
   * @param args Command-line arguments. Expects two or three arguments: 1. Path
   *             to the world specification file 2. Maximum number of turns 3.
   *             Optional: "gui" to run in GUI mode (default is text mode)
   */
  public static void main(String[] args) {
    if (args.length < 2 || args.length > 3) {
      System.out.println("Usage: java Driver <world_file_path> <max_turns> [gui]");
      System.exit(1);
    }

    String worldFilePath = args[0];
    int maxTurns;
    boolean useGui = args.length == 3 && "gui".equalsIgnoreCase(args[2]);

    try {
      maxTurns = Integer.parseInt(args[1]);
      if (maxTurns <= 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      System.out.println("Error: Max turns must be a positive integer.");
      return;
    }

    // Create controller based on mode
    WorldController controller = new WorldControllerImpl(new InputStreamReader(System.in),
        new PrintStream(System.out), useGui ? new GameViewImpl() : null,
        worldFilePath);
    controller.startGame(maxTurns);
  }
}