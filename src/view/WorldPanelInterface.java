package view;

import java.awt.Point;
import java.awt.image.BufferedImage;
import model.player.Player;

/**
 * Interface defining the contract for world panel components that render the
 * game world.
 */
public interface WorldPanelInterface {
  /**
   * Sets the world image to be displayed.
   *
   * @param image the BufferedImage to display
   */
  void setWorldImage(BufferedImage image);

  /**
   * Gets the space name at the given point.
   *
   * @param point the point to check
   * @return the name of the space at the point, or null if none
   */
  String getSpaceAtPoint(Point point);

  /**
   * Gets the player at the given point.
   *
   * @param point the point to check
   * @return the Player at the point, or null if none
   */
  Player getPlayerAtPoint(Point point);

  /**
   * Sets the last clicked point.
   *
   * @param point the point that was clicked
   */
  void setLastClickPoint(Point point);

  /**
   * Gets the last clicked point.
   *
   * @return the last clicked point
   */
  Point getLastClickPoint();

  /**
   * Revalidate the panel to update the layout.
   */
  void revalidate();

  /**
   * Repaint the panel.
   */
  void repaint();

  /**
   * Set the panel to be focusable.
   * 
   * @param b whether the panel should be focusable
   */
  void setFocusable(boolean b);
}