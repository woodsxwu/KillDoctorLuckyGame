package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import model.pet.Pet;
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.viewmodel.ViewModel;

public class WorldPanel extends JPanel {
  private static final int PADDING = 25; // Increased padding
  private static final float SCALE_FACTOR = 0.8f; // Scale down factor for better fit
  
  private final ViewModel viewModel;
  private BufferedImage worldImage;
  private Point offset;
  private Point lastClickPoint;
  private int scale = 30; // Base scale from WorldPainter

  public WorldPanel(ViewModel viewModel) {
    if (viewModel == null) {
      throw new IllegalArgumentException("ViewModel cannot be null");
    }
    this.viewModel = viewModel;
    this.offset = new Point(PADDING, PADDING);
    setPreferredSize(new Dimension(800, 600)); // Larger default size
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    setBackground(Color.WHITE);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    
    if (worldImage != null) {
      // Calculate scaled dimensions
      int scaledWidth = (int)(worldImage.getWidth() * SCALE_FACTOR);
      int scaledHeight = (int)(worldImage.getHeight() * SCALE_FACTOR);
      
      // Center the image if panel is larger
      int x = Math.max(PADDING, (getWidth() - scaledWidth) / 2);
      int y = Math.max(PADDING, (getHeight() - scaledHeight) / 2);
      
      // Update offset for other elements
      offset = new Point(x + 25, y + 25);
      
      // Draw scaled image
      g.drawImage(worldImage, x, y, scaledWidth, scaledHeight, null);
      
      // Adjust scale for game elements
      scale = (int)(30 * SCALE_FACTOR);
      
      drawGameElements(g);
    }
  }

  private void drawGameElements(Graphics g) {
    List<Space> spaces = viewModel.getSpaceCopies();
    
    // Draw game elements
    drawTarget(g);
    drawPlayers(g);
  }

  private Rectangle getSpaceBounds(Space space) {
    int x = (int)(space.getUpperLeftColumn() * scale) + offset.x;
    int y = (int)(space.getUpperLeftRow() * scale) + offset.y;
    int width = (int)((space.getLowerRightColumn() - space.getUpperLeftColumn() + 1) * scale);
    int height = (int)((space.getLowerRightRow() - space.getUpperLeftRow() + 1) * scale);
    return new Rectangle(x, y, width, height);
  }

  private Point getSpaceCenter(int spaceIndex) {
    List<Space> spaces = viewModel.getSpaceCopies();
    if (spaceIndex < 0 || spaceIndex >= spaces.size()) {
      return null;
    }
    
    Space space = spaces.get(spaceIndex);
    Rectangle bounds = getSpaceBounds(space);
    return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
  }

  private void drawTarget(Graphics g) {
    TargetCharacter target = viewModel.getTargetCopy();
    Point pos = getSpaceCenter(target.getCurrentSpaceIndex());
    if (pos != null) {
      g.setColor(Color.RED);
      int size = (int)(20 * SCALE_FACTOR);
      g.fillOval(pos.x - size/2, pos.y - size/2, size, size);
      
      String health = String.valueOf(target.getHealth());
      g.setColor(Color.WHITE);
      FontMetrics fm = g.getFontMetrics();
      g.drawString(health, 
          pos.x - fm.stringWidth(health) / 2, 
          pos.y + fm.getAscent() / 2);
    }
  }

  private void drawPlayers(Graphics g) {
    List<Player> players = viewModel.getPlayerCopies();
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      Point pos = getSpaceCenter(player.getCurrentSpaceIndex());
      if (pos != null) {
        int size = (int)(16 * SCALE_FACTOR);
        g.setColor(getPlayerColor(i));
        g.fillRect(pos.x - size/2, pos.y - size/2, size, size);
        
        String name = player.getPlayerName();
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(name, 
            pos.x - fm.stringWidth(name) / 2, 
            pos.y - size);
      }
    }
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
      int width = (int)(image.getWidth() * SCALE_FACTOR) + (2 * PADDING);
      int height = (int)(image.getHeight() * SCALE_FACTOR) + (2 * PADDING);
      setPreferredSize(new Dimension(width, height));
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