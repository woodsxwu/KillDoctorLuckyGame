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
  private int playerCount = 0;

  public GameSetupPanel() {
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Create header panel with title and player count
    JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
    JLabel titleLabel = new JLabel("Game Setup", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    playerCountLabel = new JLabel("Players (0)", SwingConstants.CENTER);
    playerCountLabel.setFont(new Font("Arial", Font.PLAIN, 16));

    headerPanel.add(titleLabel, BorderLayout.NORTH);
    headerPanel.add(playerCountLabel, BorderLayout.CENTER);
    add(headerPanel, BorderLayout.NORTH);

    // Create scrollable player list panel
    playerListPanel = new JPanel();
    playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(playerListPanel);
    scrollPane.setPreferredSize(new Dimension(400, 200));
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    add(scrollPane, BorderLayout.CENTER);

    // Create buttons panel
    JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

    addHumanButton = createStyledButton("Add Human Player");
    addComputerButton = createStyledButton("Add Computer Player");
    startGameButton = createStyledButton("Start Game");
    startGameButton.setEnabled(false);  // Initially disabled

    buttonsPanel.add(addHumanButton);
    buttonsPanel.add(addComputerButton);
    buttonsPanel.add(startGameButton);

    add(buttonsPanel, BorderLayout.SOUTH);
  }

  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setPreferredSize(new Dimension(200, 40));
    button.setFont(new Font("Arial", Font.PLAIN, 14));
    button.setFocusPainted(false);
    return button;
  }

  public void addPlayerToList(String playerName, boolean isHuman) {
    JPanel playerPanel = new JPanel(new BorderLayout(10, 0));
    playerPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(5, 5, 5, 5),
        BorderFactory.createLineBorder(Color.GRAY)));

    String playerType = isHuman ? "Human" : "Computer";
    JLabel playerLabel = new JLabel(String.format("%s (%s)", playerName, playerType));
    playerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    playerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    playerPanel.add(playerLabel, BorderLayout.CENTER);

    playerListPanel.add(playerPanel);
    playerListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    playerCount++;
    updatePlayerCount();
    enableStartButton(playerCount > 0);
    
    // Ensure the new player is visible in the scroll pane
    playerPanel.scrollRectToVisible(playerPanel.getBounds());
    
    revalidate();
    repaint();
}

  public void reset() {
      playerListPanel.removeAll();
      playerCount = 0;
      updatePlayerCount();
      enableStartButton(false);
      revalidate();
      repaint();
  }

  private void updatePlayerCount() {
    playerCountLabel.setText(String.format("Players (%d)", playerCount));
}

  public void enableStartButton(boolean enabled) {
    startGameButton.setEnabled(enabled);
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