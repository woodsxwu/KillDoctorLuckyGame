package view;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Game menu providing options for game control.
 */
public class GameMenu extends JMenu {
  private JMenuItem newGameItem;
  private JMenuItem newGameCurrentWorldItem;
  private JMenuItem quitItem;
  
  public GameMenu() {
    super("Game");
    setupMenuItems();
  }

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