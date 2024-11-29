package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Listener for keyboard input in the GUI.
 */
public class KeyboardListener implements KeyListener {
  private Map<Integer, Runnable> keyboardActions;

  public KeyboardListener() {
      this.keyboardActions = new HashMap<>();
  }

  public void setKeyPressedMap(Map<Integer, Runnable> keyboardActions) {
      this.keyboardActions = keyboardActions;
  }

  @Override
  public void keyPressed(KeyEvent e) {
      if (keyboardActions != null && keyboardActions.containsKey(e.getKeyCode())) {
          keyboardActions.get(e.getKeyCode()).run();
      }
  }

  @Override
  public void keyTyped(KeyEvent e) {
      // Not used
  }

  @Override
  public void keyReleased(KeyEvent e) {
      // Not used
  }
}
