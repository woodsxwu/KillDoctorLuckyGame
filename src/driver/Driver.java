package driver;

import control.WorldController;
import control.WorldControllerImpl;
import facade.GameFacade;
import facade.GameFacadeImpl;
import model.viewmodel.ViewModel;
import model.world.World;
import model.world.WorldFactory;
import view.GameView;
import view.GameViewImpl;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Driver class for the game. Handles command-line arguments and initializes the game.
 */
public class Driver {
  /**
   * Main method to run the game.
   *
   * @param args Command-line arguments. Expects two or three arguments:
   *             1. Path to the world specification file
   *             2. Maximum number of turns
   *             3. Optional: "gui" to run in GUI mode (default is text mode)
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

    try {
      // Create the model, facade, and viewmodel layers
      World world = createWorld(worldFilePath);
      GameFacade facade = new GameFacadeImpl(world);
      ViewModel viewModel = (ViewModel) world; // WorldImpl implements ViewModel

      // Create controller based on mode
      WorldController controller;
      if (useGui) {
        GameView view = new GameViewImpl(viewModel);
        controller = new WorldControllerImpl(
          facade, 
          new InputStreamReader(System.in), 
          new PrintStream(System.out), 
          view,
          viewModel  // Pass the viewModel to controller
        );
      } else {
        controller = new WorldControllerImpl(
          facade, 
          new InputStreamReader(System.in), 
          new PrintStream(System.out), 
          null,
          viewModel  // Pass the viewModel even in text mode
        );
      }

      // Start the game
      controller.startGame(maxTurns);

    } catch (FileNotFoundException e) {
      System.out.println("Error: World file not found: " + worldFilePath);
      System.exit(1);
    }
  }

  private static World createWorld(String worldFilePath) throws FileNotFoundException {
    WorldFactory factory = new WorldFactory();
    return factory.createWorld(new InputStreamReader(new FileInputStream(worldFilePath)));
  }
}