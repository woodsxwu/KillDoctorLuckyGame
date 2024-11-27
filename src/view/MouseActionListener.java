package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;

public class MouseActionListener implements MouseListener {
  private Map<String, Runnable> mouseActions;

  /**
   * Constructs a new MouseClickListener.
   */
  public MouseActionListener() {
    mouseActions = null;
  }
  
  /**
   * Sets the mouse actions for this listener.
   * 
   * @param mouseActions The mouse actions
   */
  public void setMouseActions(Map<String, Runnable> mouseActions) {
    this.mouseActions = mouseActions;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

}
