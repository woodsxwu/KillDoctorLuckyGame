package view;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Game menu providing options for game control.
 */
public class GameMenu extends JMenu {
  private static final long serialVersionUID = 3927735036660508962L;
  private JMenuItem newGameItem;
  private JMenuItem newGameCurrentWorldItem;
  private JMenuItem quitItem;
  
  /**
   * Creates a new GameMenu.
   */
  public GameMenu() {
    super("Game");
    setupMenuItems();
  }

  /**
   * Sets up the menu items.
   */
  private void setupMenuItems() {
    newGameItem = new JMenuItem("New Game");
    newGameItem.setActionCommand("NewGame");
    newGameItem.setToolTipText("Start a new game with a new world specification");
    
    newGameCurrentWorldItem = new JMenuItem("New Game (Current World)");
    newGameCurrentWorldItem.setActionCommand("NewGameCurrentWorld");
    newGameCurrentWorldItem.setToolTipText("Start a new game with the current world specification");
    
    quitItem = new JMenuItem("Quit");
    quitItem.setActionCommand("Quit");
    quitItem.setToolTipText("Exit the game");

    add(newGameItem);
    add(newGameCurrentWorldItem);
    addSeparator();
    add(quitItem);
  }
}