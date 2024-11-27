package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Listener for button clicks in the GUI.
 */
public class ButtonListener implements ActionListener {
  private Map<String, Runnable> buttonClickActions;

  /**
   * Constructs a new ButtonListener.
   */
  public ButtonListener(Map<String, Runnable> buttonClickActions) {
    buttonClickActions = null;
  }

  /**
   * Sets the button click actions for this listener.
   * 
   * @param buttonClickActions The button click actions
   */
  public void setButtonClickActions(Map<String, Runnable> buttonClickActions) {
    this.buttonClickActions = buttonClickActions;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (buttonClickActions.containsKey(e.getActionCommand())) {
      buttonClickActions.get(e.getActionCommand()).run();
    }
  }
}
