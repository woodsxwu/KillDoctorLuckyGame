package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Panel for setting up a new game, including adding players and starting the
 * game.
 */
public class GameSetupPanel extends JPanel implements SetupPanel {
  private static final long serialVersionUID = 5770974747847142546L;
  private final JPanel playerListPanel;
  private final JButton addHumanButton;
  private final JButton addComputerButton;
  private final JButton startGameButton;
  private final JLabel playerCountLabel;
  private int playerCount = 0;

  /**
   * Create a new GameSetupPanel with the default layout and components.
   */
  public GameSetupPanel() {
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    // Create header panel with title and player count
    JLabel titleLabel = new JLabel("Game Setup", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
    playerCountLabel = new JLabel("Players (0)", SwingConstants.CENTER);
    playerCountLabel.setFont(new Font("Arial", Font.PLAIN, 16));

    headerPanel.add(titleLabel, BorderLayout.NORTH);
    headerPanel.add(playerCountLabel, BorderLayout.CENTER);
    add(headerPanel, BorderLayout.NORTH);

    // Create player list panel with vertical BoxLayout
    playerListPanel = new JPanel();
    playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));

    // Create scroll pane with custom settings
    JScrollPane scrollPane = new JScrollPane(playerListPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getViewport().setBackground(Color.WHITE);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

    // Set preferred size for the scroll pane
    scrollPane.setPreferredSize(new Dimension(400, 300));
    add(scrollPane, BorderLayout.CENTER);

    // Create buttons panel
    JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

    addHumanButton = createStyledButton("Add Human Player");
    addComputerButton = createStyledButton("Add Computer Player");
    startGameButton = createStyledButton("Start Game");
    startGameButton.setEnabled(false); // Initially disabled

    buttonsPanel.add(addHumanButton);
    buttonsPanel.add(addComputerButton);
    buttonsPanel.add(startGameButton);

    add(buttonsPanel, BorderLayout.SOUTH);
  }

  /**
   * Create a new styled button with the given text.
   *
   * @param text the text to display on the button
   * @return the new button
   */
  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setPreferredSize(new Dimension(200, 40));
    button.setFont(new Font("Arial", Font.PLAIN, 14));
    button.setFocusPainted(false);
    return button;
  }

  @Override
  public void addPlayerToList(String playerName, String startingSpace, int capacity,
      boolean isHuman) {
    // Create a fixed-height panel for each player
    JPanel playerPanel = new JPanel();
    playerPanel.setLayout(new BorderLayout());
    playerPanel.setPreferredSize(new Dimension(playerListPanel.getWidth(), 50));
    playerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    playerPanel.setMinimumSize(new Dimension(100, 50));

    // Add gradient background and border
    playerPanel.setBackground(new Color(250, 250, 250));
    playerPanel
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
            BorderFactory.createLineBorder(new Color(200, 200, 200))));

    // Create content panel for player information
    JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    contentPanel.setOpaque(false);

    // Create player info with HTML formatting
    String playerType = isHuman ? "Human" : "Computer";
    String capacityText = capacity < 0 ? "unlimited" : String.valueOf(capacity);
    String htmlText = String.format(
        "<html><div style='margin: 5px'>" + "<b>%s</b> (%s)<br>"
            + "<font color='#666666'>Location: %s | Capacity: %s</font></div></html>",
        playerName, playerType, startingSpace, capacityText);

    JLabel playerLabel = new JLabel(htmlText);
    playerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    contentPanel.add(playerLabel);

    playerPanel.add(contentPanel, BorderLayout.CENTER);

    // Add the player panel to the list
    playerListPanel.add(playerPanel);
    playerListPanel.add(Box.createRigidArea(new Dimension(0, 2))); // Gap between players

    playerCount++;
    updatePlayerCount();
    enableStartButton(playerCount > 0);

    // Revalidate and repaint
    playerListPanel.revalidate();
    playerListPanel.repaint();

    // Auto-scroll to bottom
    SwingUtilities.invokeLater(() -> {
      JScrollPane scrollPane = (JScrollPane) playerListPanel.getParent().getParent();
      JScrollBar vertical = scrollPane.getVerticalScrollBar();
      vertical.setValue(vertical.getMaximum());
    });
  }

  @Override
  public void reset() {
    playerListPanel.removeAll();
    playerCount = 0;
    updatePlayerCount();
    enableStartButton(false);
    revalidate();
    repaint();
  }

  /**
   * Update the player count label with the current player count.
   */
  private void updatePlayerCount() {
    playerCountLabel.setText(String.format("Players (%d)", playerCount));
  }

  @Override
  public void enableStartButton(boolean enabled) {
    startGameButton.setEnabled(enabled);
  }

  @Override
  public void addActionListener(ActionListener listener) {
    addHumanButton.addActionListener(listener);
    addComputerButton.addActionListener(listener);
    startGameButton.addActionListener(listener);

    addHumanButton.setActionCommand("Add Human Player");
    addComputerButton.setActionCommand("Add Computer Player");
    startGameButton.setActionCommand("Start Game");
  }
}