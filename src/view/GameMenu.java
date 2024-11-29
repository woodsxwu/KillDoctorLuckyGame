package view;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Game menu providing options for game control.
 */
public class GameMenu extends JMenu {
  private JMenuItem newGameItem;
  private JMenuItem newGameCurrentWorldItem;
  private JMenuItem quitItem;
  private JFileChooser customFileChooser;
  
  public GameMenu() {
    super("Game");
    customFileChooser = new JFileChooser();
    setupMenuItems();
    createFileMenu();
  }

  private void setupMenuItems() {
    newGameItem = new JMenuItem("New Game");
    newGameItem.setToolTipText("Start a new game with a new world specification");
    
    newGameCurrentWorldItem = new JMenuItem("New Game (Current World)");
    newGameCurrentWorldItem.setToolTipText("Start a new game with the current world specification");
    
    quitItem = new JMenuItem("Quit");
    quitItem.setToolTipText("Exit the game");

    add(newGameItem);
    add(newGameCurrentWorldItem);
    addSeparator();
    add(quitItem);
  }

  private void createFileMenu() {
    customFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    customFileChooser.setDialogTitle("Select World Specification File");
  }

  public void handleMenuSelection(String command) {
    switch (command) {
      case "New Game":
        if (customFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Handle new game with selected file
        }
        break;
      case "New Game (Current World)":
        // Handle new game with current world
        break;
      case "Quit":
        System.exit(0);
        break;
    }
  }

  public JFileChooser getFileChooser() {
    return customFileChooser;
  }
}