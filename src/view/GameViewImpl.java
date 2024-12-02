package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;
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
  private final ViewModel viewModel;
  private final WorldPanel worldPanel;
  private final JFileChooser fileChooser;
  private final GameMenu gameMenu;
  private final HelpMenu helpMenu;
  private GameSetupPanel setupPanel;
  private String currentCard;

  public GameViewImpl(ViewModel viewModel) {
    if (viewModel == null) {
      throw new IllegalArgumentException("ViewModel cannot be null");
    }
    this.viewModel = viewModel;
    this.frame = new JFrame("Kill Doctor Lucky");
    this.cardLayout = new CardLayout();
    this.mainPanel = new JPanel(cardLayout);
    this.welcomePanel = new WelcomePanel();
    this.gamePanel = new JPanel(new BorderLayout());
    this.statusArea = createStatusArea();
    this.worldPanel = new WorldPanel(viewModel);
    this.fileChooser = new JFileChooser();
    this.gameMenu = new GameMenu();
    this.helpMenu = new HelpMenu();
    this.setupPanel = new GameSetupPanel();
    this.currentCard = WELCOME_PANEL;
    
    setupFrame();
  }

  private JTextArea createStatusArea() {
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
    
    setupGamePanel();
    frame.add(mainPanel);
  }

  private void setupGamePanel() {
    gamePanel.setLayout(new BorderLayout());
    
    // Create world panel with scroll pane
    JScrollPane worldScrollPane = new JScrollPane(worldPanel);
    worldScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    worldScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
    // Create right status panel
    JPanel rightPanel = createStatusPanel();
    
    // Add components to game panel
    gamePanel.add(worldScrollPane, BorderLayout.CENTER);
    gamePanel.add(rightPanel, BorderLayout.EAST);
  }

  private JPanel createStatusPanel() {
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setPreferredSize(new Dimension(STATUS_PANEL_WIDTH, 0));
    rightPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
    
    // Add status panel title
    JLabel statusTitle = new JLabel("Game Status", SwingConstants.CENTER);
    statusTitle.setFont(new Font("Arial", Font.BOLD, 14));
    statusTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    
    // Create scrollable status area
    JScrollPane statusScrollPane = new JScrollPane(statusArea);
    statusScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
    rightPanel.add(statusTitle, BorderLayout.NORTH);
    rightPanel.add(statusScrollPane, BorderLayout.CENTER);
    
    return rightPanel;
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
    SwingUtilities.invokeLater(() -> {
      statusArea.append(message + "\n");
      statusArea.setCaretPosition(statusArea.getDocument().getLength());
      
      // Handle player addition in setup screen
      if (message.contains("successfully")) {
        setupPanel.addPlayerToList(message.split(" ")[2], message.contains("Human"));
      }
    });
  }

  @Override
  public void showPlayerInfo(String playerName) {
    String info = null;
    for (var player : viewModel.getPlayerCopies()) {
      if (player.getPlayerName().equals(playerName)) {
        info = player.getDescription(viewModel.getSpaceCopies());
        break;
      }
    }
    if (info != null) {
      JOptionPane.showMessageDialog(frame, info, "Player Information", 
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  @Override
  public void showSpaceInfo(String spaceName) {
    String info = null;
    for (var space : viewModel.getSpaceCopies()) {
      if (space.getSpaceName().equals(spaceName)) {
        info = space.getSpaceInfo(viewModel.getSpaceCopies(), 
            viewModel.getPlayerCopies(), 
            viewModel.getTargetCopy(),
            viewModel.getPetCopy());
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
    try {
      worldPanel.repaint();
    } catch (Exception e) {
      displayMessage("Error refreshing world: " + e.getMessage());
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
      String filePath = fileChooser.getSelectedFile().getPath();
      if (filePath.toLowerCase().endsWith(".txt")) {
        return filePath;
      } else {
        displayMessage("Please select a valid .txt file");
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
    JOptionPane.showMessageDialog(frame, winner, "Game Over", 
        JOptionPane.INFORMATION_MESSAGE);
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
    worldPanel.addMouseListener(listener);
  }

  @Override
  public void updateTurnDisplay(String playerName, int turnNumber) {
    SwingUtilities.invokeLater(() -> {
      statusArea.append("\n" + String.format("Turn %d: %s's turn", turnNumber, playerName) + "\n");
      statusArea.setCaretPosition(statusArea.getDocument().getLength());
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
}