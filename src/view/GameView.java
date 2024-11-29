package view;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * The view interface for the game, supporting both GUI components and game state display.
 */
public interface GameView {
  /**
   * Initializes the view components.
   */
  void initialize();
  
  /**
   * Shows the welcome/about screen.
   */
  void showWelcomeScreen();
  
  /**
   * Shows the main game screen.
   */
  void showGameScreen();
  
  /**
   * Displays a message to the user.
   */
  void displayMessage(String message);
  
  /**
   * Shows detailed information about a player.
   */
  void showPlayerInfo(String playerName);
  
  /**
   * Shows detailed information about a space.
   */
  void showSpaceInfo(String spaceName);
  
  /**
   * Refreshes the world display.
   */
  void refreshWorld();
  
  /**
   * Makes the view visible.
   */
  void makeVisible();
  
  /**
   * Shows a file chooser dialog for selecting world files.
   * @return selected file path or null if cancelled
   */
  String showFileChooser();
  
  /**
   * Shows an input dialog with the given prompt.
   */
  String showInputDialog(String prompt);
  
  /**
   * Shows a game end dialog with the winner information.
   */
  void showGameEndDialog(String winner);
  
  /**
   * Gets the space name at the given screen coordinates.
   */
  String getSpaceAtPoint(Point point);
  
  /**
   * Gets the last clicked point.
   */
  Point getLastClickPoint();
  
  /**
   * Sets the world image to display.
   */
  void setWorldImage(BufferedImage image);
  
  /**
   * Adds an action listener for buttons and menu items.
   */
  void addActionListener(ActionListener listener);
  
  /**
   * Adds a keyboard listener.
   */
  void addKeyListener(KeyboardListener listener);
  
  /**
   * Adds a mouse listener.
   */
  void addMouseListener(MouseActionListener listener);
  
  /**
   * Updates the current player turn display.
   */
  void updateTurnDisplay(String playerName, int turnNumber);
  
  /**
   * Updates the game status/info display.
   */
  void updateStatusDisplay(String status);
}
