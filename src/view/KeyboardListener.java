package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Listener for keyboard input in the GUI.
 */
public class KeyboardListener implements KeyListener {
  private Map<Integer, Runnable> keyboardActions;

  /**
   * Constructor for KeyboardListener.
   * 
   * @param keyboardActions the actions to be taken when a key is pressed
   */
  public KeyboardListener(Map<Integer, Runnable> keyboardActions) {
    if (keyboardActions == null) {
      throw new IllegalArgumentException("Keyboard actions cannot be null");
    }
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
