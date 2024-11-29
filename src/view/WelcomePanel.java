package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomePanel extends JPanel {
  private JLabel welcomeText;
  private JLabel creatorInfo;

  public WelcomePanel() {
      setLayout(new GridBagLayout());
      setupComponents();
      setMinimumSize(new Dimension(300, 300));
  }

  private void setupComponents() {
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.insets = new Insets(10, 10, 10, 10);

      welcomeText = new JLabel("<html><h1>Welcome to Kill Doctor Lucky!</h1></html>");
      creatorInfo = new JLabel("<html><p>Created by [Your Name]<br><br>" +
              "Controls:<br>" +
              "- Click spaces to move<br>" +
              "- Press 'P' to pick up items<br>" +
              "- Press 'L' to look around<br>" +
              "- Press 'A' to attack<br>" +
              "- Press 'M' to move pet</p></html>");

      add(welcomeText, gbc);
      gbc.gridy = 1;
      add(creatorInfo, gbc);
  }

  @Override
  protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      // Add any custom background painting if needed
  }
}
