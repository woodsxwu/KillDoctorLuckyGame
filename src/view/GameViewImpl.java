package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import model.item.Item;
import model.player.Player;
import model.space.Space;
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
  private int currentTurn;

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
    this.currentTurn = 0;

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
    // Clear existing components first
    gamePanel.removeAll();

    // Create new world panel with current view model
    this.worldPanel = new WorldPanel(viewModel);

    // Set the world image immediately after creating the panel
    try {
      BufferedImage worldImage = viewModel.createWorldMap();
      worldPanel.setWorldImage(worldImage);
    } catch (IOException e) {
      showError("Error creating world map: " + e.getMessage());
    }

    gamePanel.setLayout(new BorderLayout());

    // Create scrollable world panel
    JScrollPane worldScrollPane = new JScrollPane(worldPanel);
    worldScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    worldScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    gamePanel.add(worldScrollPane, BorderLayout.CENTER);

    // Create right status panel
    JPanel rightPanel = createStatusPanel();
    gamePanel.add(rightPanel, BorderLayout.EAST);

    // Clear text areas
    statusArea.setText("");
    gameInfoArea.setText("");
    limitedInfoArea.setText("");

    // Reset current turn
    currentTurn = 0;

    // Ensure the panel is properly refreshed
    gamePanel.revalidate();
    gamePanel.repaint();
  }

  private JPanel createStatusPanel() {
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setPreferredSize(new Dimension(STATUS_PANEL_WIDTH, 0));
    rightPanel.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

    // Limited Info Section - Small, just 2-3 lines
    JPanel limitedInfoPanel = createSectionPanel("Current Turn", limitedInfoArea, 75);
    rightPanel.add(limitedInfoPanel);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // Game Info Section - Moderate size, with default message
    JPanel gameInfoPanel = createSectionPanel("Player Information", gameInfoArea, 100);
    rightPanel.add(gameInfoPanel);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // Turn Results Section - Gets the remaining space
    JPanel turnResultsPanel = new JPanel(new BorderLayout());
    JLabel turnResultsLabel = new JLabel("Turn Results");
    turnResultsLabel.setFont(new Font("Arial", Font.BOLD, 14));
    turnResultsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    turnResultsPanel.add(turnResultsLabel, BorderLayout.NORTH);

    JScrollPane scrollPane = new JScrollPane(statusArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    int remainingHeight = frame.getHeight() - limitedInfoPanel.getPreferredSize().height
        - gameInfoPanel.getPreferredSize().height - 60;

    scrollPane.setPreferredSize(new Dimension(STATUS_PANEL_WIDTH - 20, remainingHeight));
    turnResultsPanel.add(scrollPane, BorderLayout.CENTER);

    rightPanel.add(turnResultsPanel);
    rightPanel.add(Box.createVerticalGlue());

    return rightPanel;
  }

  private JPanel createSectionPanel(String title, JTextArea textArea, int preferredHeight) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight + 25)); // Add space for
                                                                                  // title

    // Create title label
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Create scrollable text area with fixed height
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(STATUS_PANEL_WIDTH - 20, preferredHeight));
    scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight));
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
  }

  @Override
  public void showWelcomeScreen() {
    cardLayout.show(mainPanel, WELCOME_PANEL);
  }

  @Override
  public void showSetupScreen() {
    cardLayout.show(mainPanel, SETUP_PANEL);
    setupPanel.reset();
  }

  @Override
  public void showGameScreen() {
    if (gamePanel != null) {
      cardLayout.show(mainPanel, GAME_PANEL);
      refreshWorld();
      gamePanel.requestFocusInWindow(); // Ensure keyboard focus
    }
  }

  @Override
  public void addPlayerToList(String playerName, String startingSpace, int capacity,
      boolean isHuman) {
    setupPanel.addPlayerToList(playerName, startingSpace, capacity, isHuman);
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

  @Override
  public void refreshWorld() {
    if (worldPanel != null) {
      try {
        worldPanel.revalidate();
        worldPanel.repaint();
      } catch (Exception e) {
        showError("Error refreshing world: " + e.getMessage());
      }
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
    if (worldPanel != null) {
      worldPanel.setWorldImage(image);
      worldPanel.revalidate();
      worldPanel.repaint();

      // Also update the game panel
      gamePanel.revalidate();
      gamePanel.repaint();
    }
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
    worldPanel.addMouseListener(listener);
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
      if (currentTurn != viewModel.getCurrentTurn()) {
        currentTurn = viewModel.getCurrentTurn();
        updateTurnDisplay(viewModel.getCurrentPlayerCopy().getPlayerName(), currentTurn);
      }
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
  public void updateGameInfo(String info) {
    if (gameInfoArea != null) {
      SwingUtilities.invokeLater(() -> {
        gameInfoArea.setForeground(Color.BLACK); // Reset to black when showing actual info
        gameInfoArea.setText(info);
        gameInfoArea.setCaretPosition(0);
      });
    }
  }

  @Override
  public void setLastClickPoint(Point point) {
    if (worldPanel != null) {
      worldPanel.setLastClickPoint(point);
    }
  }

  @Override
  public String showItemPickerDialog() {
    // Get current player and items in their space
    Player currentPlayer = viewModel.getCurrentPlayerCopy();
    Space currentSpace = viewModel.getSpaceCopies().get(currentPlayer.getCurrentSpaceIndex());
    List<Item> items = currentSpace.getItems();

    if (items.isEmpty()) {
      showMessage("No items available in this space.", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }

    ItemPickerDialog dialog = new ItemPickerDialog(frame, items);
    return dialog.showDialog();
  }
}