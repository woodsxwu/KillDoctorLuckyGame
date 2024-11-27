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
   * Constructs a new Keyboard Listener.
   */
  public KeyboardListener() {
    this.keyboardActions = null;
  }
  
  /**
   * Sets the keyboard actions for this listener.
   * 
   * @param keyboardActions The keyboard actions
   */
  public void setKeyPressedMap(Map<Integer, Runnable> keyboardActions) {
    this.keyboardActions = keyboardActions;
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }

}
