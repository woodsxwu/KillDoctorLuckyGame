package view;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import model.player.Player;
import model.viewmodel.ViewModel;

/**
 * The view interface for the game, supporting both GUI components and game
 * state display.
 */
public interface GameView {
  /**
   * Initializes the view components.
   */
  void initialize();

  /**
   * Shows the setup panel for adding players.
   */
  void showWelcomeScreen();

  /**
   * Shows the main game screen.
   */
  void showGameScreen();

  /**
   * Displays a message to the user.
   * 
   * @param message the message to display
   */
  void displayMessage(String message);

  /**
   * Shows detailed information about a space.
   * 
   * @param spaceName the name of the space
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
   * 
   * @return selected file path or null if cancelled
   */
  String showFileChooser();

  /**
   * Shows an input dialog with the given prompt.
   * 
   * @param prompt the prompt to display
   * @return the user's input
   */
  String showInputDialog(String prompt);

  /**
   * Shows a game end dialog with the winner information.
   * 
   * @param winner the winner information to display
   */
  void showGameEndDialog(String winner);

  /**
   * Gets the space name at the given screen coordinates.
   * 
   * @param point the screen coordinates
   * @return the name of the space at the given point
   */
  String getSpaceAtPoint(Point point);

  /**
   * Gets the last clicked point.
   * 
   * @return the last clicked point
   */
  Point getLastClickPoint();

  /**
   * Sets the world image to display.
   * 
   * @param image the world image to display
   */
  void setWorldImage(BufferedImage image);

  /**
   * Adds an action listener for buttons and menu items.
   * 
   * @param listener the action listener to add
   */
  void addActionListener(ActionListener listener);

  /**
   * Adds a keyboard listener.
   * 
   * @param listener the keyboard listener to add
   */
  void addKeyListener(KeyboardListener listener);

  /**
   * Adds a mouse listener.
   * 
   * @param listener the mouse listener to add
   */
  void addMouseListener(MouseActionListener listener);

  /**
   * Updates the current player turn display.
   * 
   * @param playerName the name of the current player
   * @param turnNumber the current turn number
   */
  void updateTurnDisplay(String playerName, int turnNumber);

  /**
   * Updates the game status/info display.
   * 
   * @param status the status to display
   */
  void updateStatusDisplay(String status);

  /**
   * Enables or disables the Start Game button in the setup panel.
   * 
   * @param enabled true to enable the button, false to disable
   */
  void enableStartButton(boolean enabled);

  /**
   * Shows the setup screen for adding players.
   */
  void showSetupScreen();

  /**
   * Sets the view model for the view.
   * 
   * @param viewModel the view model to set
   */
  void setViewModel(ViewModel viewModel);

  /**
   * Shows an message dialog.
   * 
   * @param message the message to display
   * @param type    the message type (ERROR_MESSAGE, WARNING_MESSAGE, or
   *                INFORMATION_MESSAGE)
   */
  void showMessage(String message, int type);

  /**
   * Shows an error message dialog.
   * 
   * @param message the message to display
   */
  void showError(String message);

  /**
   * add player to the list of players in setup panel.
   * 
   * @param playerName    the name of the player
   * @param startingSpace the name of the starting space
   * @param capacity      the maximum number of items the player can carry
   * @param isHuman       true if the player is human, false if computer
   */
  void addPlayerToList(String playerName, String startingSpace, int capacity, boolean isHuman);

  /**
   * Gets the name of the player at the given point, if any.
   * 
   * @param point the point to check
   * @return player at the point
   */
  Player getPlayerAtPoint(Point point);

  /**
   * Updates the game info panel with specific player information.
   * 
   * @param playerInfo the player information to display
   */
  void updateGameInfo(String playerInfo);
}