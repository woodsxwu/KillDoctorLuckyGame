package mocks;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import model.player.Player;
import model.viewmodel.ViewModel;
import view.GameView;
import view.KeyboardListener;
import view.MouseActionListener;

/**
 * A mock implementation of the GameView interface for testing purposes.
 */
public class MockGameView implements GameView {
  private final StringBuilder log;
  private Point lastClickPoint;

  public MockGameView(StringBuilder log) {
    this.log = log;
  }


  @Override
  public void initialize() {
    log.append("initialize called\n");
  }

  @Override
  public void showWelcomeScreen() {
    log.append("showWelcomeScreen called\n");
  }

  @Override
  public void showGameScreen() {
    log.append("showGameScreen called\n");
  }

  @Override
  public void showSpaceInfo(String spaceName) {
    log.append(String.format("showSpaceInfo called with: %s\n", spaceName));
  }

  @Override
  public void refreshWorld() {
    log.append("refreshWorld called\n");
  }

  @Override
  public void makeVisible() {
    log.append("makeVisible called\n");
  }

  @Override
  public String showFileChooser() {
    log.append("showFileChooser called\n");
    return "res/my_mansion.txt";
  }

  @Override
  public String showInputDialog(String prompt) {
    log.append(String.format("showInputDialog called with: %s\n", prompt));
    if (prompt.contains("name")) {
      return "TestPlayer";
    } else if (prompt.contains("capacity")) {
      return "5";
    }
    return null;
  }

  @Override
  public void showGameEndDialog(String winner) {
    log.append(String.format("showGameEndDialog called with: %s\n", winner));
  }

  @Override
  public String getSpaceAtPoint(Point point) {
    log.append("getSpaceAtPoint called\n");
    return point != null ? "Kitchen" : null;
  }

  @Override
  public void setLastClickPoint(Point point) {
    lastClickPoint = point;
    log.append("setLastClickPoint called\n");
  }

  @Override
  public Point getLastClickPoint() {
    log.append("getLastClickPoint called\n");
    return lastClickPoint;
  }

  @Override
  public void setWorldImage(BufferedImage image) {
    log.append("setWorldImage called\n");
  }

  @Override
  public void addActionListener(ActionListener listener) {
    log.append("addActionListener called\n");
  }

  @Override
  public void addKeyListener(KeyboardListener listener) {
    log.append("addKeyListener called\n");
  }

  @Override
  public void addMouseListener(MouseActionListener listener) {
    log.append("addMouseListener called\n");
  }

  @Override
  public void updateTurnDisplay(String playerName, int turnNumber) {
    log.append(String.format("updateTurnDisplay called with player: %s, turn: %d\n", playerName,
        turnNumber));
  }

  @Override
  public void updateStatusDisplay(String status) {
    log.append(String.format("updateStatusDisplay called with: %s\n", status));
  }

  @Override
  public void enableStartButton(boolean enabled) {
    log.append(String.format("enableStartButton called with: %b\n", enabled));
  }

  @Override
  public void showSetupScreen() {
    log.append("showSetupScreen called\n");
  }

  @Override
  public void setViewModel(ViewModel viewModel) {
    log.append("setViewModel called\n");
  }

  @Override
  public void showMessage(String message, int type) {
    log.append(String.format("showMessage called with: %s, type: %d\n", message, type));
  }

  @Override
  public void showError(String message) {
    log.append(String.format("showError called with: %s\n", message));
  }

  @Override
  public void addPlayerToList(String name, String space, int capacity, boolean isHuman) {
    log.append(String.format("addPlayerToList called with: %s, %s, %d, %b\n", name, space, capacity,
        isHuman));
  }

  @Override
  public Player getPlayerAtPoint(Point point) {
    log.append("getPlayerAtPoint called\n");
    return null;
  }

  @Override
  public void updateGameInfo(String info) {
    log.append(String.format("updateGameInfo called with: %s\n", info));
  }

  @Override
  public String showItemPickerDialog() {
    log.append("showItemPickerDialog called\n");
    return "Knife";
  }

  @Override
  public String showAttackItemDialog() {
    log.append("showAttackItemDialog called\n");
    return "Knife";
  }

  @Override
  public String showSpacePickerDialog() {
    log.append("showSpacePickerDialog called\n");
    return "Kitchen";
  }
}