package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * A custom ActionListener that maps button clicks to actions.
 */
public class ButtonListener implements ActionListener {
  private final Map<String, Runnable> buttonClickActions;

  /**
   * Creates a new ButtonListener.
   *
   * @param buttonClickActions the map of button click actions
   */
  public ButtonListener(Map<String, Runnable> buttonClickActions) {
    if (buttonClickActions == null) {
      throw new IllegalArgumentException("Button actions cannot be null");
    }
    this.buttonClickActions = buttonClickActions;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (buttonClickActions.containsKey(e.getActionCommand())) {
      buttonClickActions.get(e.getActionCommand()).run();
    }
  }
}