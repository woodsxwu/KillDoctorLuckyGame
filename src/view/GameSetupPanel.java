package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameSetupPanel extends JPanel {
  private final JPanel playerListPanel;
  private final JButton addHumanButton;
  private final JButton addComputerButton;
  private final JButton startGameButton;
  private final JLabel playerCountLabel;
  private final JLabel titleLabel;

  public GameSetupPanel() {
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Create header panel
    JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
    titleLabel = new JLabel("Game Setup", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    playerCountLabel = new JLabel("Players (0)", SwingConstants.CENTER);
    playerCountLabel.setFont(new Font("Arial", Font.PLAIN, 16));

    headerPanel.add(titleLabel, BorderLayout.NORTH);
    headerPanel.add(playerCountLabel, BorderLayout.CENTER);
    add(headerPanel, BorderLayout.NORTH);

    // Create player list panel with scroll pane
    playerListPanel = new JPanel();
    playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(playerListPanel);
    scrollPane.setPreferredSize(new Dimension(400, 200));
    add(scrollPane, BorderLayout.CENTER);

    // Create buttons panel
    JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

    addHumanButton = new JButton("Add Human Player");
    addComputerButton = new JButton("Add Computer Player");
    startGameButton = new JButton("Start Game");

    // Style buttons
    addHumanButton.setPreferredSize(new Dimension(200, 40));
    addComputerButton.setPreferredSize(new Dimension(200, 40));
    startGameButton.setPreferredSize(new Dimension(200, 40));

    startGameButton.setEnabled(false);

    buttonsPanel.add(addHumanButton);
    buttonsPanel.add(addComputerButton);
    buttonsPanel.add(startGameButton);

    add(buttonsPanel, BorderLayout.SOUTH);
  }

  public void addPlayerToList(String playerName, boolean isHuman) {
    JPanel playerPanel = new JPanel(new BorderLayout(10, 0));
    playerPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createLineBorder(Color.GRAY)));

    String playerType = isHuman ? "Human" : "Computer";
    JLabel playerLabel = new JLabel(playerName + " (" + playerType + ")");
    playerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    playerPanel.add(playerLabel, BorderLayout.CENTER);

    playerListPanel.add(playerPanel);
    playerListPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add spacing

    updatePlayerCount();
  }

  private void updatePlayerCount() {
    int playerCount = playerListPanel.getComponentCount() / 2; // Divide by 2 because of spacing
                                                               // components
    playerCountLabel.setText("Players (" + playerCount + ")");
    startGameButton.setEnabled(playerCount > 0);
    revalidate();
    repaint();
  }

  public void addActionListener(ActionListener listener) {
    addHumanButton.addActionListener(listener);
    addComputerButton.addActionListener(listener);
    startGameButton.addActionListener(listener);

    // Set action commands
    addHumanButton.setActionCommand("Add Human Player");
    addComputerButton.setActionCommand("Add Computer Player");
    startGameButton.setActionCommand("Start Game");
  }
}