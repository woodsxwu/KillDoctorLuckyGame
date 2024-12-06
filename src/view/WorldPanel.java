package view;

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
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.viewmodel.ViewModel;

/**
 * A panel that displays the game world and its elements.
 */
public class WorldPanel extends JPanel implements WorldPanelInterface {
  private static final long serialVersionUID = -791006074172367539L;
  private static final int PADDING = 25;
  private static final int PLAYER_SIZE = 20;
  private static final double DISTRIBUTION_RADIUS = 30.0;
  private static final int IMAGE_PADDING = 100; // Match the padding used in WorldPainter
  private static final Color[] PLAYER_COLOR_PALETTE = { new Color(0, 120, 215), // Blue
      new Color(216, 0, 115), // Magenta
      new Color(255, 140, 0), // Orange
      new Color(0, 183, 195), // Cyan
      new Color(147, 0, 211), // Purple
      new Color(0, 204, 106), // Green
      new Color(232, 17, 35), // Red
      new Color(136, 136, 136) // Gray
  };

  private final ViewModel viewModel;
  private BufferedImage worldImage;
  private Point offset;
  private Point lastClickPoint;
  private double scale = 1.0;
  private Map<Rectangle, Player> playerBounds;
  private Map<String, Color> playerColors;

  /**
   * Creates a new WorldPanel with the given ViewModel.
   *
   * @param viewModel the ViewModel to use
   */
  public WorldPanel(ViewModel viewModel) {
    this.viewModel = viewModel;
    this.offset = new Point(PADDING, PADDING);
    this.playerBounds = new HashMap<>();
    this.playerColors = new HashMap<>();
    setPreferredSize(new Dimension(1000, 800));
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    setBackground(Color.WHITE);
  }

  /**
   * Gets the color associated with a player. If the player does not have a color
   * assigned yet, a new color is generated and stored for future use.
   *
   * @param player the player to get the color for
   * @return the color associated with the player
   */
  private Color getPlayerColor(Player player) {
    if (!playerColors.containsKey(player.getPlayerName())) {
      int playerIndex = playerColors.size();
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
      // Calculate scale based on the original image size
      double scaleX = (double) (getWidth() - 2 * PADDING) / worldImage.getWidth();
      double scaleY = (double) (getHeight() - 2 * PADDING) / worldImage.getHeight();
      scale = Math.min(scaleX, scaleY);

      // Calculate dimensions for the scaled image
      int scaledWidth = (int) (worldImage.getWidth() * scale);
      int scaledHeight = (int) (worldImage.getHeight() * scale);

      // Center the map in the panel
      int x = (getWidth() - scaledWidth) / 2;
      int y = (getHeight() - scaledHeight) / 2;
      offset = new Point(x, y);

      // Draw the scaled world image
      g.drawImage(worldImage, x, y, scaledWidth, scaledHeight, null);

      // Draw game elements on top
      drawGameElements(g);
    }
  }

  /**
   * Draws the target and players on the game world.
   *
   * @param g the graphics context to draw on
   */
  private void drawGameElements(Graphics g) {
    drawTarget(g);
    drawPlayersInSpaces(g);
  }

  /**
   * Gets the bounds of a space in the scaled image.
   *
   * @param space the space to get the bounds for
   * @return the bounds of the space in the scaled image coordinates
   */
  private Rectangle getSpaceBounds(Space space) {
    // Account for both the image padding and the scale
    int imageOffsetX = (int) (IMAGE_PADDING / 4 * scale); // Quarter of padding as in WorldPainter
    int imageOffsetY = (int) (IMAGE_PADDING / 4 * scale);

    int x = (int) (space.getUpperLeftColumn() * 30 * scale) + offset.x + imageOffsetX;
    int y = (int) (space.getUpperLeftRow() * 30 * scale) + offset.y + imageOffsetY;
    int width = (int) ((space.getLowerRightColumn() - space.getUpperLeftColumn() + 1) * 30 * scale);
    int height = (int) ((space.getLowerRightRow() - space.getUpperLeftRow() + 1) * 30 * scale);

    return new Rectangle(x, y, width, height);
  }

  /**
   * Gets the center of a space in the scaled image.
   *
   * @param spaceIndex the index of the space to get the center for
   * @return the center of the space in the scaled image coordinates
   */
  private Point getSpaceCenter(int spaceIndex) {
    List<Space> spaces = viewModel.getSpaceCopies();
    if (spaceIndex >= 0 && spaceIndex < spaces.size()) {
      Space space = spaces.get(spaceIndex);
      Rectangle bounds = getSpaceBounds(space);
      return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
    }
    return null;
  }

  /**
   * Draws the target on the game world.
   *
   * @param g the graphics context to draw on
   */
  private void drawTarget(Graphics g) {
    TargetCharacter target = viewModel.getTargetCopy();
    Point pos = getSpaceCenter(target.getCurrentSpaceIndex());
    if (pos != null) {
      int size = (int) (PLAYER_SIZE * 1.2 * scale);
      g.setColor(Color.RED);
      g.fillOval(pos.x - size / 2, pos.y - size / 2, size, size);

      String health = String.valueOf(target.getHealth());
      g.setColor(Color.WHITE);
      FontMetrics fm = g.getFontMetrics();
      g.drawString(health, pos.x - fm.stringWidth(health) / 2,
          pos.y + fm.getAscent() / 2 - fm.getDescent() / 2);
    }
  }

  /**
   * Draws the players on the game world.
   *
   * @param g the graphics context to draw on
   */
  private void drawPlayersInSpaces(Graphics g) {
    List<Player> players = viewModel.getPlayerCopies();
    Map<Integer, List<Player>> playersInSpaces = new HashMap<>();

    // Group players by space
    for (Player player : players) {
      playersInSpaces.computeIfAbsent(player.getCurrentSpaceIndex(), k -> new ArrayList<>())
          .add(player);
    }

    // Draw players in each space
    for (Map.Entry<Integer, List<Player>> entry : playersInSpaces.entrySet()) {
      Point center = getSpaceCenter(entry.getKey());
      List<Player> playersInSpace = entry.getValue();

      if (center != null && !playersInSpace.isEmpty()) {
        if (playersInSpace.size() == 1) {
          drawPlayer(g, playersInSpace.get(0), center);
        } else {
          // Distribute multiple players in a circle
          double angleStep = 2 * Math.PI / playersInSpace.size();
          for (int i = 0; i < playersInSpace.size(); i++) {
            double angle = i * angleStep;
            int x = (int) (center.x + DISTRIBUTION_RADIUS * scale * Math.cos(angle));
            int y = (int) (center.y + DISTRIBUTION_RADIUS * scale * Math.sin(angle));
            drawPlayer(g, playersInSpace.get(i), new Point(x, y));
          }
        }
      }
    }
  }

  /**
   * Draws a player at the given position on the game world.
   *
   * @param g        the graphics context to draw on
   * @param player   the player to draw
   * @param position the position to draw the player at
   */
  private void drawPlayer(Graphics g, Player player, Point position) {
    int size = (int) (PLAYER_SIZE * scale);
    Rectangle playerRect = new Rectangle(position.x - size / 2, position.y - size / 2, size, size);
    playerBounds.put(playerRect, player);

    g.setColor(getPlayerColor(player));
    g.fillRect(playerRect.x, playerRect.y, playerRect.width, playerRect.height);

    String name = player.getPlayerName();
    g.setColor(Color.BLACK);
    FontMetrics fm = g.getFontMetrics();
    g.drawString(name, position.x - fm.stringWidth(name) / 2, position.y - size);
  }

  @Override
  public void setWorldImage(BufferedImage image) {
    this.worldImage = image;
  }

  @Override
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

  @Override
  public Player getPlayerAtPoint(Point point) {
    if (point == null) {
      return null;
    }

    int clickTolerance = 2;
    for (Map.Entry<Rectangle, Player> entry : playerBounds.entrySet()) {
      Rectangle bounds = entry.getKey();
      Rectangle expandedBounds = new Rectangle(bounds.x - clickTolerance,
          bounds.y - clickTolerance, bounds.width + (2 * clickTolerance),
          bounds.height + (2 * clickTolerance));

      if (expandedBounds.contains(point)) {
        return entry.getValue().copy();
      }
    }
    return null;
  }

  @Override
  public void setLastClickPoint(Point point) {
    this.lastClickPoint = point;
  }

  @Override
  public Point getLastClickPoint() {
    return lastClickPoint;
  }
}