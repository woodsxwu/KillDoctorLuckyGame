package view;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import model.player.Player;
import model.space.Space;

/**
 * The view of the game.
 */
public interface GameView {
  
  /**
   * Initializes the view.
   */
  void initialize();
  
  /**
   * Shows the welcome screen.
   */
  void showWelcomeScreen();
  
  /**
   * Shows the game screen.
   */
  void showGameScreen();
  
  /**
   * Updates the game screen.
   */
  void updateGameScreen();
  
  /**
   * Shows the end screen.
   */
  void displayMessage(String message);
  
  /**
   * Shows the player's information.
   * 
   * @param player the player
   */
  void showPlayerInfo(Player player);
  
  /**
   * Shows the space's information.
   * 
   * @param space the space
   */
  void showSpaceInfo(Space space);
  
  /**
   * Refreshes the world.
   */
  void refreshWorld();
  
  /**
   * Adds an action listener.
   * 
   * @param listener the action listener
   */
  void addActionListener(ActionListener listener);
  
  /**
   * Adds a mouse listener.
   * 
   * @param listener the mouse listener
   */
  void addMouseListener(MouseListener listener);
  
  /**
   * Makes the view visible.
   */
  void makeVisible();
}
