package view;

import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.viewmodel.ViewModel;
import model.pet.Pet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;

public class WorldPanel extends JPanel {
  private static final int CELL_SIZE = 50;
  private static final int PADDING = 10;
  
  private final ViewModel viewModel;
  private BufferedImage worldImage;
  private Dimension preferredSize;
  private Point offset;
  private Point lastClickPoint;

  public WorldPanel(ViewModel viewModel) {
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
      // Draw world map
      g.drawImage(worldImage, offset.x, offset.y, null);
      
      // Draw grid lines
      drawGrid(g);
      
      // Draw spaces
      drawSpaces(g);
      
      // Draw target character
      drawTarget(g);
      
      // Draw players
      drawPlayers(g);
      
      // Draw pet
      drawPet(g);
    }
  }

  private void drawGrid(Graphics g) {
    g.setColor(Color.LIGHT_GRAY);
    
    // Draw vertical lines
    for (int x = offset.x; x <= worldImage.getWidth() + offset.x; x += CELL_SIZE) {
      g.drawLine(x, offset.y, x, worldImage.getHeight() + offset.y);
    }
    
    // Draw horizontal lines
    for (int y = offset.y; y <= worldImage.getHeight() + offset.y; y += CELL_SIZE) {
      g.drawLine(offset.x, y, worldImage.getWidth() + offset.x, y);
    }
  }

  private void drawSpaces(Graphics g) {
    List<Space> spaces = viewModel.getSpaceCopies();
    for (Space space : spaces) {
      // Draw space boundaries
      g.setColor(Color.ORANGE);
      Rectangle bounds = getSpaceBounds(space);
      g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
      
      // Draw space name
      g.setColor(Color.BLACK);
      String name = space.getSpaceName();
      FontMetrics fm = g.getFontMetrics();
      int nameWidth = fm.stringWidth(name);
      int nameHeight = fm.getHeight();
      g.drawString(name, 
          bounds.x + (bounds.width - nameWidth) / 2,
          bounds.y + (bounds.height + nameHeight) / 2);
    }
  }

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

  private Point getSpaceCenter(int spaceIndex) {
    List<Space> spaces = viewModel.getSpaceCopies();
    if (spaceIndex >= 0 && spaceIndex < spaces.size()) {
      Space space = spaces.get(spaceIndex);
      Rectangle bounds = getSpaceBounds(space);
      return new Point(bounds.x + bounds.width/2, bounds.y + bounds.height/2);
    }
    return null;
  }

  private Rectangle getSpaceBounds(Space space) {
    int x = space.getUpperLeftColumn() * CELL_SIZE + offset.x;
    int y = space.getUpperLeftRow() * CELL_SIZE + offset.y;
    int width = (space.getLowerRightColumn() - space.getUpperLeftColumn() + 1) * CELL_SIZE;
    int height = (space.getLowerRightRow() - space.getUpperLeftRow() + 1) * CELL_SIZE;
    return new Rectangle(x, y, width, height);
  }

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

  public void setWorldImage(BufferedImage image) {
    this.worldImage = image;
    if (image != null) {
      preferredSize = new Dimension(
          image.getWidth() + 2 * PADDING,
          image.getHeight() + 2 * PADDING
      );
      setPreferredSize(preferredSize);
      revalidate();
    }
    repaint();
  }

  public String getSpaceAtPoint(Point point) {
    if (point == null || worldImage == null) {
      return null;
    }
    
    List<Space> spaces = viewModel.getSpaceCopies();
    for (Space space : spaces) {
      Rectangle bounds = getSpaceBounds(space);
      if (bounds.contains(point)) {
        return space.getSpaceName();
      }
    }
    return null;
  }

  public void setLastClickPoint(Point point) {
    this.lastClickPoint = point;
  }

  public Point getLastClickPoint() {
    return lastClickPoint;
  }
}