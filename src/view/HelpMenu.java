package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

/**
 * Help menu providing detailed game instructions and controls.
 */
public class HelpMenu extends JMenu {
  private static final long serialVersionUID = -6977278992338014890L;
  private JMenuItem gameInstructionsItem;
  private JMenuItem controlsItem;

  /**
   * Constructs a new HelpMenu.
   */
  public HelpMenu() {
    super("Help");
    setupMenuItems();
  }

  /**
   * Displays a dialog with detailed game instructions.
   */
  private void setupMenuItems() {
    gameInstructionsItem = new JMenuItem("Game Instructions");
    gameInstructionsItem.setActionCommand("GameInstructions");
    gameInstructionsItem.setToolTipText("Learn how to play the game");
    gameInstructionsItem.addActionListener(e -> showGameInstructions());

    controlsItem = new JMenuItem("Controls");
    controlsItem.setActionCommand("Controls");
    controlsItem.setToolTipText("View game controls");
    controlsItem.addActionListener(e -> showControls());

    add(gameInstructionsItem);
    addSeparator();
    add(controlsItem);
  }

  /**
   * Displays a dialog with detailed game instructions.
   */
  private void showGameInstructions() {
    StringBuilder instructions = new StringBuilder();
    instructions.append("<html><body style='width: 400px; padding: 10px;'>");
    instructions.append("<h2>How to Play</h2>");
    instructions.append("<p><b>Game Objective:</b><br>");
    instructions.append(
        "Your goal is to eliminate Dr. Lucky before he escapes or other players stop you!</p>");

    instructions.append("<h3>Game Setup:</h3>");
    instructions.append("<ul>");
    instructions.append("<li>Add human and computer players to the game</li>");
    instructions.append("<li>Each player starts in their chosen space</li>");
    instructions.append("<li>Players can carry a limited number of items</li>");
    instructions.append("</ul>");

    instructions.append("<h3>Gameplay Rules:</h3>");
    instructions.append("<ul>");
    instructions.append("<li>Players take turns moving, looking around, and collecting items</li>");
    instructions.append("<li>You can only attack Dr. Lucky when no other players can see you</li>");
    instructions.append("<li>Items increase your attack damage</li>");
    instructions.append("<li>The pet can be used to create distractions</li>");
    instructions.append("</ul>");

    instructions.append("<h3>Winning the Game:</h3>");
    instructions.append("<ul>");
    instructions.append("<li>Successfully eliminate Dr. Lucky to win</li>");
    instructions.append("<li>The game ends if Dr. Lucky escapes or maximum turns are reached</li>");
    instructions.append("</ul>");
    instructions.append("</body></html>");

    showFormattedDialog("Game Instructions", instructions.toString());
  }

  /**
   * Displays a dialog with detailed game controls.
   */
  private void showControls() {
    StringBuilder controls = new StringBuilder();
    controls.append("<html><body style='width: 400px; padding: 10px;'>");
    controls.append("<h2>Game Controls</h2>");

    controls.append("<h3>Mouse Controls:</h3>");
    controls.append("<ul>");
    controls.append("<li><b>Click on a space:</b> Move to that space</li>");
    controls.append("<li><b>Click on a player:</b> View player information</li>");
    controls.append("</ul>");

    controls.append("<h3>Keyboard Controls:</h3>");
    controls.append("<ul>");
    controls.append("<li><b>P key:</b> Pick up item in current space</li>");
    controls.append("<li><b>L key:</b> Look around current space</li>");
    controls.append("<li><b>A key:</b> Attempt to attack Dr. Lucky</li>");
    controls.append("<li><b>M key:</b> Move the pet</li>");
    controls.append("<li><b>I key:</b> View player information</li>");
    controls.append("</ul>");

    controls.append("<h3>Menu Options:</h3>");
    controls.append("<ul>");
    controls.append("<li><b>Game Menu:</b> Start new game, load world, quit</li>");
    controls.append("<li><b>Help Menu:</b> View these instructions and controls</li>");
    controls.append("</ul>");
    controls.append("</body></html>");

    showFormattedDialog("Game Controls", controls.toString());
  }

  /**
   * Displays a dialog with formatted content.
   * 
   * @param title   the title of the dialog
   * @param content the content to display
   */
  private void showFormattedDialog(String title, String content) {
    JEditorPane editorPane = new JEditorPane("text/html", content);
    editorPane.setEditable(false);
    editorPane.setBackground(new Color(250, 250, 250));

    JScrollPane scrollPane = new JScrollPane(editorPane);
    scrollPane.setPreferredSize(new Dimension(450, 500));
    
    JDialog dialog = new JDialog((Frame) null, title, true);

    dialog.add(scrollPane);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
  }
}