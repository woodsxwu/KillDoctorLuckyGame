package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import javax.swing.*;

import model.player.Player;
import model.viewmodel.ViewModel;

public class GameViewImpl implements GameView {
  private static final String WELCOME_PANEL = "Welcome";
  private static final String SETUP_PANEL = "Setup";
  private static final String GAME_PANEL = "Game";
  private static final Dimension MIN_SIZE = new Dimension(300, 300);
  private static final Dimension PREFERRED_SIZE = new Dimension(1000, 600);
  private static final int STATUS_PANEL_WIDTH = 250;

  private final JFrame frame;
  private final CardLayout cardLayout;
  private final JPanel mainPanel;
  private final JPanel welcomePanel;
  private final JPanel gamePanel;
  private final JTextArea statusArea;
  private final JTextArea gameInfoArea;
  private final JTextArea limitedInfoArea;
  private ViewModel viewModel;
  private WorldPanel worldPanel;
  private final JFileChooser fileChooser;
  private final GameMenu gameMenu;
  private final HelpMenu helpMenu;
  private GameSetupPanel setupPanel;
  private String currentCard;

  public GameViewImpl() {
    this.viewModel = null;
    this.frame = new JFrame("Kill Doctor Lucky");
    this.cardLayout = new CardLayout();
    this.mainPanel = new JPanel(cardLayout);
    this.welcomePanel = new WelcomePanel();
    this.gamePanel = new JPanel(new BorderLayout());
    this.statusArea = createTextArea();
    this.gameInfoArea = createTextArea();
    this.limitedInfoArea = createTextArea();
    this.worldPanel = new WorldPanel(viewModel);
    this.fileChooser = new JFileChooser();
    this.gameMenu = new GameMenu();
    this.helpMenu = new HelpMenu();
    this.setupPanel = new GameSetupPanel();
    this.currentCard = WELCOME_PANEL;

    setupFrame();
  }

  private JTextArea createTextArea() {
    JTextArea area = new JTextArea();
    area.setEditable(false);
    area.setWrapStyleWord(true);
    area.setLineWrap(true);
    area.setFont(new Font("SansSerif", Font.PLAIN, 12));
    area.setBackground(new Color(245, 245, 245));
    area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    return area;
  }

  private void setupFrame() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(MIN_SIZE);
    frame.setPreferredSize(PREFERRED_SIZE);
    frame.setJMenuBar(createMenuBar());

    mainPanel.add(welcomePanel, WELCOME_PANEL);
    mainPanel.add(setupPanel, SETUP_PANEL);
    mainPanel.add(gamePanel, GAME_PANEL);

    frame.add(mainPanel);
  }

  private void setupGamePanel() {
    gamePanel.setLayout(new BorderLayout());

    // Create scrollable world panel
    JScrollPane worldScrollPane = new JScrollPane(worldPanel);
    worldScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    worldScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    gamePanel.add(worldScrollPane, BorderLayout.CENTER);

    // Create right status panel
    JPanel rightPanel = createStatusPanel();
    gamePanel.add(rightPanel, BorderLayout.EAST);
  }

  private JPanel createStatusPanel() {
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setPreferredSize(new Dimension(STATUS_PANEL_WIDTH, 0));
    rightPanel.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

    // Limited Info Section
    JPanel limitedInfoPanel = createSectionPanel("Current Turn", limitedInfoArea, 80);
    rightPanel.add(limitedInfoPanel);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // Game Info Section
    JPanel gameInfoPanel = createSectionPanel("Game Information", gameInfoArea, 150);
    rightPanel.add(gameInfoPanel);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // Turn Results Section
    JPanel turnResultsPanel = createSectionPanel("Turn Results", statusArea, 0);
    rightPanel.add(turnResultsPanel);

    return rightPanel;
  }

  private JPanel createSectionPanel(String title, JTextArea textArea, int preferredHeight) {
    JPanel panel = new JPanel(new BorderLayout());

    // Create title label
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Create scrollable text area
    JScrollPane scrollPane = new JScrollPane(textArea);
    if (preferredHeight > 0) {
      scrollPane.setPreferredSize(new Dimension(STATUS_PANEL_WIDTH - 20, preferredHeight));
    }
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(gameMenu);
    menuBar.add(helpMenu);
    return menuBar;
  }

  @Override
  public void initialize() {
    frame.pack();
    frame.setLocationRelativeTo(null);
    cardLayout.show(mainPanel, WELCOME_PANEL);
    currentCard = WELCOME_PANEL;
  }

  @Override
  public void showWelcomeScreen() {
    cardLayout.show(mainPanel, WELCOME_PANEL);
    currentCard = WELCOME_PANEL;
  }

  @Override
  public void showSetupScreen() {
    cardLayout.show(mainPanel, SETUP_PANEL);
    currentCard = SETUP_PANEL;
    setupPanel.reset();
  }

  @Override
  public void showGameScreen() {
    cardLayout.show(mainPanel, GAME_PANEL);
    currentCard = GAME_PANEL;
    refreshWorld();
  }

  @Override
  public void displayMessage(String message) {
    updateStatusDisplay(message);
  }

  @Override
  public void addPlayerToList(String playerName, String startingSpace, int capacity,
      boolean isHuman) {
    setupPanel.addPlayerToList(playerName, startingSpace, capacity, isHuman);
  }

  @Override
  public void showPlayerInfo(String playerName) {
    if (viewModel != null) {
      for (Player player : viewModel.getPlayerCopies()) {
        if (player.getPlayerName().equals(playerName)) {
          updateGameInfo(player.getDescription(viewModel.getSpaceCopies()));
          break;
        }
      }
    }
  }

  @Override
  public void showSpaceInfo(String spaceName) {
    String info = null;
    for (var space : viewModel.getSpaceCopies()) {
      if (space.getSpaceName().equals(spaceName)) {
        info = space.getSpaceInfo(viewModel.getSpaceCopies(), viewModel.getPlayerCopies(),
            viewModel.getTargetCopy(), viewModel.getPetCopy());
        break;
      }
    }
    if (info != null) {
      JOptionPane.showMessageDialog(frame, info, "Space Information",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void updateGameInfo() {
    if (viewModel != null) {
      StringBuilder info = new StringBuilder();
      info.append("Players in game:\n");
      for (Player player : viewModel.getPlayerCopies()) {
        info.append(String.format("- %s in %s\n", player.getPlayerName(),
            viewModel.getSpaceCopies().get(player.getCurrentSpaceIndex()).getSpaceName()));

        // Add inventory information
        if (!player.getItems().isEmpty()) {
          info.append("  Carrying: ");
          player.getItems().forEach(item -> info.append(item.getItemName()).append(", "));
          info.setLength(info.length() - 2); // Remove last comma and space
          info.append("\n");
        }
      }

      // Add target character information
      info.append("\nDr. Lucky:\n");
      info.append(String.format("- Location: %s\n", viewModel.getSpaceCopies()
          .get(viewModel.getTargetCopy().getCurrentSpaceIndex()).getSpaceName()));
      info.append(String.format("- Health: %d\n", viewModel.getTargetCopy().getHealth()));

      gameInfoArea.setText(info.toString());
    }
  }

  @Override
  public void refreshWorld() {
    try {
      worldPanel.repaint();
      updateGameInfo();
    } catch (Exception e) {
      showError("Error refreshing world: " + e.getMessage());
    }
  }

  @Override
  public void makeVisible() {
    frame.setVisible(true);
  }

  @Override
  public String showFileChooser() {
    fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
      @Override
      public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
      }

      @Override
      public String getDescription() {
        return "Text Files (*.txt)";
      }
    });

    if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
      String filePath = fileChooser.getSelectedFile().getPath().replace('\\', '/');
      String currentDir = System.getProperty("user.dir").replace('\\', '/');
      if (filePath.startsWith(currentDir)) {
        filePath = filePath.substring(currentDir.length() + 1);
      }

      if (filePath.toLowerCase().endsWith(".txt")) {
        return filePath;
      } else {
        showError("Please select a valid .txt file");
        return null;
      }
    }
    return null;
  }

  @Override
  public String showInputDialog(String prompt) {
    return JOptionPane.showInputDialog(frame, prompt);
  }

  @Override
  public void showGameEndDialog(String winner) {
    JOptionPane.showMessageDialog(frame, winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    showWelcomeScreen();
  }

  @Override
  public String getSpaceAtPoint(Point point) {
    return worldPanel.getSpaceAtPoint(point);
  }

  @Override
  public Point getLastClickPoint() {
    return worldPanel.getLastClickPoint();
  }

  @Override
  public void setWorldImage(BufferedImage image) {
    worldPanel.setWorldImage(image);
  }

  @Override
  public void addActionListener(ActionListener listener) {
    for (int i = 0; i < frame.getJMenuBar().getMenuCount(); i++) {
      JMenu menu = frame.getJMenuBar().getMenu(i);
      for (int j = 0; j < menu.getItemCount(); j++) {
        if (menu.getItem(j) != null) {
          menu.getItem(j).addActionListener(listener);
        }
      }
    }
    setupPanel.addActionListener(listener);
  }

  @Override
  public void addKeyListener(KeyboardListener listener) {
    frame.addKeyListener(listener);
    frame.setFocusable(true);
    frame.requestFocus();
  }

  @Override
  public void addMouseListener(MouseActionListener listener) {
    frame.addMouseListener(listener);
  }

  @Override
  public void updateTurnDisplay(String playerName, int turnNumber) {
    SwingUtilities.invokeLater(() -> {
      if (viewModel != null) {
        Player currentPlayer = viewModel.getCurrentPlayerCopy();
        String limitedInfo = String.format("Turn %d | Current Player: %s\n%s", turnNumber,
            playerName, currentPlayer.limitedInfo(viewModel.getSpaceCopies()));
        limitedInfoArea.setText(limitedInfo);
      }
    });
  }

  @Override
  public void updateStatusDisplay(String status) {
    SwingUtilities.invokeLater(() -> {
      statusArea.append(status + "\n");
      statusArea.setCaretPosition(statusArea.getDocument().getLength());
    });
  }

  @Override
  public void enableStartButton(boolean enabled) {
    setupPanel.enableStartButton(enabled);
  }

  @Override
  public void setViewModel(ViewModel viewModel) {
    if (viewModel == null) {
      throw new IllegalArgumentException("View model cannot be null");
    }
    this.viewModel = viewModel;
    this.worldPanel = new WorldPanel(viewModel);
    setupGamePanel();
  }

  @Override
  public void showMessage(String message, int type) {
    SwingUtilities.invokeLater(() -> {
      CustomDialog dialog = new CustomDialog(frame, "", message, type);
      dialog.setVisible(true);
    });
  }

  @Override
  public void showError(String message) {
    showMessage(message, JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public Player getPlayerAtPoint(Point point) {
    if (point == null) {
      return null;
    }
    return worldPanel.getPlayerAtPoint(point);
  }

  @Override
  public void updateGameInfo(String playerInfo) {
    if (gameInfoArea != null) {
      SwingUtilities.invokeLater(() -> {
        gameInfoArea.setText(playerInfo);
        gameInfoArea.setCaretPosition(0);
      });
    }
  }
}