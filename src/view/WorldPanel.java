package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import model.pet.Pet;
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.viewmodel.ViewModel;
import model.world.WorldPainter;

/**
 * Panel responsible for displaying the game world and all its dynamic elements.
 */
public class WorldPanel extends JPanel {
  private static final int PADDING = 10;
  
  private final ViewModel viewModel;
  private BufferedImage worldImage;
  private Point offset;
  private Point lastClickPoint;

  /**
   * Constructs a WorldPanel with the given view model.
   *
   * @param viewModel the view model containing game state
   */
  public WorldPanel(ViewModel viewModel) {
    if (viewModel == null) {
      throw new IllegalArgumentException("ViewModel cannot be null");
    }
    this.viewModel = viewModel;
    this.offset = new Point(PADDING, PADDING);
    setPreferredSize(new Dimension(500, 500));
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    setBackground(Color.WHITE);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    // Draw background
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    
    if (worldImage != null) {
      // Draw the world map image which already contains spaces and their names
      g.drawImage(worldImage, offset.x, offset.y, null);
      
      // Draw dynamic game elements on top of the map
      drawTarget(g);
      drawPlayers(g);
      drawPet(g);
    }
  }

  /**
   * Draws the target character on the map.
   *
   * @param g the graphics context
   */
  private void drawTarget(Graphics g) {
    g.setColor(Color.RED);
    TargetCharacter target = viewModel.getTargetCopy();
    Point targetPos = getSpaceCenter(target.getCurrentSpaceIndex());
    if (targetPos != null) {
      g.fillOval(targetPos.x - 10, targetPos.y - 10, 20, 20);
      String health = String.valueOf(target.getHealth());
      g.setColor(Color.WHITE);
      g.drawString(health, targetPos.x - 4, targetPos.y + 4);
    }
  }

  /**
   * Draws all players on the map.
   *
   * @param g the graphics context
   */
  private void drawPlayers(Graphics g) {
    List<Player> players = viewModel.getPlayerCopies();
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      Point playerPos = getSpaceCenter(player.getCurrentSpaceIndex());
      if (playerPos != null) {
        g.setColor(getPlayerColor(i));
        g.fillRect(playerPos.x - 8, playerPos.y - 8, 16, 16);
        
        g.setColor(Color.BLACK);
        String name = player.getPlayerName();
        FontMetrics fm = g.getFontMetrics();
        g.drawString(name, playerPos.x - fm.stringWidth(name)/2, playerPos.y - 12);
      }
    }
  }

  /**
   * Draws the pet on the map.
   *
   * @param g the graphics context
   */
  private void drawPet(Graphics g) {
    Pet pet = viewModel.getPetCopy();
    Point petPos = getSpaceCenter(pet.getCurrentSpaceIndex());
    if (petPos != null) {
      g.setColor(Color.GREEN);
      int[] xPoints = {petPos.x, petPos.x - 8, petPos.x + 8};
      int[] yPoints = {petPos.y - 8, petPos.y + 8, petPos.y + 8};
      g.fillPolygon(xPoints, yPoints, 3);
      
      String name = pet.getPetName();
      FontMetrics fm = g.getFontMetrics();
      g.setColor(Color.BLACK);
      g.drawString(name, petPos.x - fm.stringWidth(name)/2, petPos.y + 20);
    }
  }

  /**
   * Gets the center point of a space given its index.
   *
   * @param spaceIndex the index of the space
   * @return the center point of the space, or null if invalid index
   */
  private Point getSpaceCenter(int spaceIndex) {
    if (spaceIndex < 0) {
      return null;
    }
    
    List<Space> spaces = viewModel.getSpaceCopies();
    if (spaceIndex >= spaces.size()) {
      return null;
    }
    
    Space space = spaces.get(spaceIndex);
    int cellSize = 50;
    int x = (space.getUpperLeftColumn() + (space.getLowerRightColumn() - space.getUpperLeftColumn()) / 2) 
            * cellSize + offset.x;
    int y = (space.getUpperLeftRow() + (space.getLowerRightRow() - space.getUpperLeftRow()) / 2) 
            * cellSize + offset.y;
    return new Point(x, y);
  }

  /**
   * Gets a color for a player based on their index.
   *
   * @param playerIndex the index of the player
   * @return a unique color for the player
   */
  private Color getPlayerColor(int playerIndex) {
    Color[] colors = {
      new Color(0, 120, 215),  // Blue
      new Color(216, 0, 115),  // Magenta
      new Color(255, 140, 0),  // Orange
      new Color(0, 183, 195),  // Cyan
      new Color(147, 0, 211),  // Purple
      new Color(0, 204, 106),  // Green
      new Color(232, 17, 35),  // Red
      new Color(136, 136, 136) // Gray
    };
    return colors[playerIndex % colors.length];
  }

  /**
   * Sets the world map image to be displayed.
   *
   * @param image the buffered image of the world map
   */
  public void setWorldImage(BufferedImage image) {
    this.worldImage = image;
    if (image != null) {
      setPreferredSize(new Dimension(
          image.getWidth() + 2 * PADDING,
          image.getHeight() + 2 * PADDING
      ));
      revalidate();
    }
    repaint();
  }

  /**
   * Gets the space name at the given point.
   *
   * @param point the point to check
   * @return the name of the space at the point, or null if none
   */
  public String getSpaceAtPoint(Point point) {
    if (point == null || worldImage == null) {
      return null;
    }
    
    int cellSize = 50;
    List<Space> spaces = viewModel.getSpaceCopies();
    for (Space space : spaces) {
      int x1 = space.getUpperLeftColumn() * cellSize + offset.x;
      int y1 = space.getUpperLeftRow() * cellSize + offset.y;
      int x2 = (space.getLowerRightColumn() + 1) * cellSize + offset.x;
      int y2 = (space.getLowerRightRow() + 1) * cellSize + offset.y;
      
      if (point.x >= x1 && point.x <= x2 && point.y >= y1 && point.y <= y2) {
        return space.getSpaceName();
      }
    }
    return null;
  }

  /**
   * Sets the last clicked point on the panel.
   *
   * @param point the point that was clicked
   */
  public void setLastClickPoint(Point point) {
    this.lastClickPoint = point;
  }

  /**
   * Gets the last clicked point on the panel.
   *
   * @return the last clicked point
   */
  public Point getLastClickPoint() {
    return lastClickPoint;
  }
}