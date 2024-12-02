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
  private static final Dimension PREFERRED_SIZE = new Dimension(800, 600);

  private final JFrame frame;
  private final CardLayout cardLayout;
  private final JPanel mainPanel;
  private final JPanel welcomePanel;
  private final JPanel gamePanel;
  private final JLabel statusLabel;
  private final ViewModel viewModel;
  private final WorldPanel worldPanel;
  private final JFileChooser fileChooser;
  private final GameMenu gameMenu;
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
    this.statusLabel = new JLabel("Welcome to the game!", SwingConstants.CENTER);
    this.worldPanel = new WorldPanel(viewModel);
    this.fileChooser = new JFileChooser();
    this.gameMenu = new GameMenu();
    this.setupPanel = new GameSetupPanel();
    this.currentCard = WELCOME_PANEL;
    
    setupFrame();
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
    
    JScrollPane scrollPane = new JScrollPane(worldPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    infoPanel.add(statusLabel);
    
    gamePanel.add(scrollPane, BorderLayout.CENTER);
    gamePanel.add(infoPanel, BorderLayout.SOUTH);
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(gameMenu);
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
    statusLabel.setText(message);
    if (message.contains("successfully")) {
      setupPanel.addPlayerToList(message.split(" ")[2], message.contains("Human")); // Extract player name
    }
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
    String turnInfo = String.format("Turn %d: %s's turn", turnNumber, playerName);
    statusLabel.setText(turnInfo);
  }

  @Override
  public void updateStatusDisplay(String status) {
    statusLabel.setText(status);
  }

  @Override
  public void enableStartButton(boolean enabled) {
    setupPanel.enableStartButton(enabled);
  }
}