package view;

import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.viewmodel.ViewModel;
import model.pet.Pet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;

public class WorldPanel extends JPanel {
  private static final int CELL_SIZE = 50;
  private static final int PADDING = 25;
  private static final float SCALE_FACTOR = 0.8f;
  private static final int PLAYER_SIZE = 16;
  private static final int PLAYER_SPACING = 20;
  private static final double DISTRIBUTION_RADIUS = 25.0;

  private final ViewModel viewModel;
  private BufferedImage worldImage;
  private Point offset;
  private Point lastClickPoint;
  private int scale = 30;
  private Map<Rectangle, Player> playerBounds;

  public WorldPanel(ViewModel viewModel) {
    this.viewModel = viewModel;
    this.offset = new Point(PADDING, PADDING);
    this.playerBounds = new HashMap<>();
    setPreferredSize(new Dimension(800, 600));
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    setBackground(Color.WHITE);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    playerBounds.clear(); // Clear previous player bounds

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());

    if (worldImage != null) {
      int scaledWidth = (int) (worldImage.getWidth() * SCALE_FACTOR);
      int scaledHeight = (int) (worldImage.getHeight() * SCALE_FACTOR);

      int x = Math.max(PADDING, (getWidth() - scaledWidth) / 2);
      int y = Math.max(PADDING, (getHeight() - scaledHeight) / 2);

      offset = new Point(x + 25, y + 25);

      g.drawImage(worldImage, x, y, scaledWidth, scaledHeight, null);

      scale = (int) (30 * SCALE_FACTOR);

      drawGameElements(g);
    }
  }

  private void drawGameElements(Graphics g) {
    drawTarget(g);
    drawPlayersInSpaces(g);
  }

  private Rectangle getSpaceBounds(Space space) {
    int x = (int) (space.getUpperLeftColumn() * scale) + offset.x;
    int y = (int) (space.getUpperLeftRow() * scale) + offset.y;
    int width = (int) ((space.getLowerRightColumn() - space.getUpperLeftColumn() + 1) * scale);
    int height = (int) ((space.getLowerRightRow() - space.getUpperLeftRow() + 1) * scale);
    return new Rectangle(x, y, width, height);
  }

  private Point getSpaceCenter(int spaceIndex) {
    List<Space> spaces = viewModel.getSpaceCopies();
    if (spaceIndex >= 0 && spaceIndex < spaces.size()) {
      Space space = spaces.get(spaceIndex);
      Rectangle bounds = getSpaceBounds(space);
      return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
    }
    return null;
  }

  private void drawTarget(Graphics g) {
    TargetCharacter target = viewModel.getTargetCopy();
    Point pos = getSpaceCenter(target.getCurrentSpaceIndex());
    if (pos != null) {
      g.setColor(Color.RED);
      int size = (int) (20 * SCALE_FACTOR);
      g.fillOval(pos.x - size / 2, pos.y - size / 2, size, size);

      String health = String.valueOf(target.getHealth());
      g.setColor(Color.WHITE);
      FontMetrics fm = g.getFontMetrics();
      g.drawString(health, pos.x - fm.stringWidth(health) / 2, pos.y + fm.getAscent() / 2);
    }
  }

  private void drawPlayersInSpaces(Graphics g) {
    List<Player> players = viewModel.getPlayerCopies();
    Map<Integer, List<Player>> playersInSpaces = new HashMap<>();

    // Group players by space
    for (Player player : players) {
      int spaceIndex = player.getCurrentSpaceIndex();
      playersInSpaces.computeIfAbsent(spaceIndex, k -> new ArrayList<>()).add(player);
    }

    // Draw players in each space
    for (Map.Entry<Integer, List<Player>> entry : playersInSpaces.entrySet()) {
      int spaceIndex = entry.getKey();
      List<Player> playersInSpace = entry.getValue();
      Point center = getSpaceCenter(spaceIndex);

      if (center != null && !playersInSpace.isEmpty()) {
        if (playersInSpace.size() == 1) {
          // Single player - draw in center
          drawPlayer(g, playersInSpace.get(0), center, 0);
        } else {
          // Multiple players - distribute in a circle
          double angleStep = 2 * Math.PI / playersInSpace.size();
          for (int i = 0; i < playersInSpace.size(); i++) {
            double angle = i * angleStep;
            int x = (int) (center.x + DISTRIBUTION_RADIUS * Math.cos(angle));
            int y = (int) (center.y + DISTRIBUTION_RADIUS * Math.sin(angle));
            drawPlayer(g, playersInSpace.get(i), new Point(x, y), i);
          }
        }
      }
    }
  }

  private void drawPlayer(Graphics g, Player player, Point position, int playerIndex) {
    int scaledSize = (int) (PLAYER_SIZE * SCALE_FACTOR);

    // Calculate the bounds for the player's clickable area
    Rectangle playerRect = new Rectangle(position.x - scaledSize / 2, position.y - scaledSize / 2,
        scaledSize, scaledSize);

    // Store the player and their bounds for click detection
    playerBounds.put(playerRect, player);

    // Draw the player
    g.setColor(getPlayerColor(playerIndex));
    g.fillRect(playerRect.x, playerRect.y, playerRect.width, playerRect.height);

    // Draw player name
    String name = player.getPlayerName();
    g.setColor(Color.BLACK);
    FontMetrics fm = g.getFontMetrics();
    g.drawString(name, position.x - fm.stringWidth(name) / 2, position.y - scaledSize);
  }

  private Color getPlayerColor(int playerIndex) {
    Color[] colors = { new Color(0, 120, 215), // Blue
        new Color(216, 0, 115), // Magenta
        new Color(255, 140, 0), // Orange
        new Color(0, 183, 195), // Cyan
        new Color(147, 0, 211), // Purple
        new Color(0, 204, 106), // Green
        new Color(232, 17, 35), // Red
        new Color(136, 136, 136) // Gray
    };
    return colors[playerIndex % colors.length];
  }

  public void setWorldImage(BufferedImage image) {
    this.worldImage = image;
    if (image != null) {
      int width = (int) (image.getWidth() * SCALE_FACTOR) + (2 * PADDING);
      int height = (int) (image.getHeight() * SCALE_FACTOR) + (2 * PADDING);
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

  public Player getPlayerAtPoint(Point point) {
    if (point == null) {
      return null;
    }

    for (Map.Entry<Rectangle, Player> entry : playerBounds.entrySet()) {
      if (entry.getKey().contains(point)) {
        return entry.getValue();
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