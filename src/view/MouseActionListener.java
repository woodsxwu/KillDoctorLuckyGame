package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;

public class MouseActionListener implements MouseListener {
  private final Map<String, Runnable> mouseActions;

  public MouseActionListener(Map<String, Runnable> mouseActions) {
    if (mouseActions == null) {
      throw new IllegalArgumentException("Mouse actions cannot be null");
    }
    this.mouseActions = mouseActions;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    System.out.println("Mouse clicked");
    if (mouseActions.containsKey("click")) {
      mouseActions.get("click").run();
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // Not used currently
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // Not used currently
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // Not used currently
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // Not used currently
  }
}
