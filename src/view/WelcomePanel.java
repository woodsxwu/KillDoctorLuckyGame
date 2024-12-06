package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class represents the welcome panel of the game.
 */
public class WelcomePanel extends JPanel {
  private static final long serialVersionUID = -4146809552957440896L;
  private JLabel welcomeText;
  private JLabel creatorInfo;
  private JLabel resourceInfo;

  /**
   * Constructor for the welcome panel.
   */
  public WelcomePanel() {
    setLayout(new GridBagLayout());
    setupComponents();
    setMinimumSize(new Dimension(300, 300));
  }

  /**
   * Set up the components in the welcome panel.
   */
  private void setupComponents() {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.CENTER; // This ensures center alignment
    gbc.insets = new Insets(10, 10, 10, 10);

    welcomeText = new JLabel("<html><h1>Welcome to Kill Doctor Lucky!</h1></html>");
    creatorInfo = new JLabel("<html><p>Created by Zhixiao Wu</html>");
    resourceInfo = new JLabel("<html>external resources:<br>"
        + "https://en.wikipedia.org/wiki/Depth-first_search<br>"
        + "https://www.codejava.net/ides/eclipse/how-to-create-jar-file-in-eclipse<br>"
        + "https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html<br>"
        + "https://www.geeksforgeeks.org/variable-arguments-varargs-in-java<br>"
        + "https://www.baeldung.com/java-command-line-arguments<br>"
        + "https://docs.github.com/en/desktop/managing-commits/managing-tags-in-github-desktop</html>");

    gbc.gridy = 0;
    add(welcomeText, gbc);

    gbc.gridy = 1;
    add(creatorInfo, gbc);

    gbc.gridy = 2;
    add(resourceInfo, gbc);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }
}
