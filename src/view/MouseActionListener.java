package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.function.Consumer;

public class MouseActionListener implements MouseListener {
  private final Map<String, Consumer<MouseEvent>> mouseActions;

  public MouseActionListener(Map<String, Consumer<MouseEvent>> mouseActions) {
    if (mouseActions == null) {
      throw new IllegalArgumentException("Mouse actions cannot be null");
    }
    this.mouseActions = mouseActions;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (mouseActions.containsKey("click")) {
      mouseActions.get("click").accept(e);
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