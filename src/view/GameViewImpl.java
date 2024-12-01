package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
    this.welcomePanel = createWelcomePanel();
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
    
    // Create setup panel first
    setupPanel = new GameSetupPanel();
    
    // Add panels to card layout
    mainPanel.add(welcomePanel, WELCOME_PANEL);
    mainPanel.add(setupPanel, "Setup");
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
    gamePanel.add(infoPanel, BorderLayout.EAST);
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(gameMenu);
    return menuBar;
  }

  private JPanel createWelcomePanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    
    JLabel titleLabel = new JLabel("Welcome to Kill Doctor Lucky");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    
    JLabel creditsLabel = new JLabel(
        "<html><center>Created by [Your Name]<br><br>"
        + "Controls:<br>"
        + "Mouse Click - Move to space<br>"
        + "P - Pick up item<br>"
        + "L - Look around<br>"
        + "A - Attack<br>"
        + "M - Move pet<br>"
        + "I - Player info"
        + "</center></html>");
    
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 10, 20, 10);
    panel.add(titleLabel, gbc);
    
    gbc.gridy = 1;
    panel.add(creditsLabel, gbc);
    
    return panel;
  }

  @Override
  public void initialize() {
    frame.pack();
    frame.setLocationRelativeTo(null);
    showWelcomeScreen();
  }

  @Override
  public void showWelcomeScreen() {
    cardLayout.show(mainPanel, SETUP_PANEL); // Always go to setup after welcome
    currentCard = SETUP_PANEL;
  }

  @Override
  public void showGameScreen() {
    // Only show game screen if coming from setup panel
    if (SETUP_PANEL.equals(currentCard)) {
      cardLayout.show(mainPanel, GAME_PANEL);
      currentCard = GAME_PANEL;
      refreshWorld();
    }
  }

  @Override
  public void displayMessage(String message) {
    statusLabel.setText(message);
  }

  @Override
  public void showPlayerInfo(String playerName) {
    // Find player in viewModel
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
    // Find space in viewModel
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
      worldPanel.setWorldImage(viewModel.createWorldMap());
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
    if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile().getPath();
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
    // Add listener to menu items
    for (int i = 0; i < frame.getJMenuBar().getMenuCount(); i++) {
      JMenu menu = frame.getJMenuBar().getMenu(i);
      for (int j = 0; j < menu.getItemCount(); j++) {
        if (menu.getItem(j) != null) {
          menu.getItem(j).addActionListener(listener);
        }
      }
    }

    // Add listener to setup panel
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
}