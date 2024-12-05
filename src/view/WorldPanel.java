package view;

import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.viewmodel.ViewModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WorldPanel extends JPanel {
  private static final long serialVersionUID = -791006074172367539L;
  private static final int PADDING = 25;
  private static final float SCALE_FACTOR = 0.8f;
  private static final int PLAYER_SIZE = 16;
  private static final double DISTRIBUTION_RADIUS = 25.0;

  private final ViewModel viewModel;
  private BufferedImage worldImage;
  private Point offset;
  private Point lastClickPoint;
  private int scale = 30;
  private Map<Rectangle, Player> playerBounds;
  private Map<String, Color> playerColors;

  private static final Color[] PLAYER_COLOR_PALETTE = { new Color(0, 120, 215), // Blue
      new Color(216, 0, 115), // Magenta
      new Color(255, 140, 0), // Orange
      new Color(0, 183, 195), // Cyan
      new Color(147, 0, 211), // Purple
      new Color(0, 204, 106), // Green
      new Color(232, 17, 35), // Red
      new Color(136, 136, 136) // Gray
  };

  public WorldPanel(ViewModel viewModel) {
    this.viewModel = viewModel;
    this.offset = new Point(PADDING, PADDING);
    this.playerBounds = new HashMap<>();
    this.playerColors = new HashMap<>();
    setPreferredSize(new Dimension(800, 600));
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    setBackground(Color.WHITE);
  }

  private Color getPlayerColor(Player player) {
    // If player doesn't have a color yet, assign one
    if (!playerColors.containsKey(player.getPlayerName())) {
      int playerIndex = playerColors.size(); // Get next available color index
      playerColors.put(player.getPlayerName(),
          PLAYER_COLOR_PALETTE[playerIndex % PLAYER_COLOR_PALETTE.length]);
    }
    return playerColors.get(player.getPlayerName());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    playerBounds.clear();

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
          drawPlayer(g, playersInSpace.get(0), center);
        } else {
          double angleStep = 2 * Math.PI / playersInSpace.size();
          for (int i = 0; i < playersInSpace.size(); i++) {
            double angle = i * angleStep;
            int x = (int) (center.x + DISTRIBUTION_RADIUS * Math.cos(angle));
            int y = (int) (center.y + DISTRIBUTION_RADIUS * Math.sin(angle));
            drawPlayer(g, playersInSpace.get(i), new Point(x, y));
          }
        }
      }
    }
  }

  private void drawPlayer(Graphics g, Player player, Point position) {
    int scaledSize = (int) (PLAYER_SIZE * SCALE_FACTOR);

    Rectangle playerRect = new Rectangle(position.x - scaledSize / 2, position.y - scaledSize / 2,
        scaledSize, scaledSize);

    playerBounds.put(playerRect, player);

    // Get the player's color (will assign a new color if needed)
    Color playerColor = getPlayerColor(player);
    g.setColor(playerColor);
    g.fillRect(playerRect.x, playerRect.y, playerRect.width, playerRect.height);

    String name = player.getPlayerName();
    g.setColor(Color.BLACK);
    FontMetrics fm = g.getFontMetrics();
    g.drawString(name, position.x - fm.stringWidth(name) / 2, position.y - scaledSize);
  }

  public void setWorldImage(BufferedImage image) {
    this.worldImage = image;
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

    final int CLICK_TOLERANCE = 2;
    for (Map.Entry<Rectangle, Player> entry : playerBounds.entrySet()) {
      Rectangle bounds = entry.getKey();
      Rectangle expandedBounds = new Rectangle(bounds.x - CLICK_TOLERANCE,
          bounds.y - CLICK_TOLERANCE, bounds.width + (2 * CLICK_TOLERANCE),
          bounds.height + (2 * CLICK_TOLERANCE));

      if (expandedBounds.contains(point)) {
        return entry.getValue().copy();
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