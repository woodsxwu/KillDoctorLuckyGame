package model.world;

import constants.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import model.space.Space;

/**
 * Paints the game world to an image file.
 */
public class WorldPainter {
  private static final int NAME_PADDING = 5; // Padding for space names
  private final List<Space> gameSpaces;
  private final int totalColumns;
  private final int totalRows;

  /**
   * Creates a new WorldPainter.
   *
   * @param gameSpaces   The list of spaces in the game.
   * @param totalColumns The total number of columns in the game.
   * @param totalRows    The total number of rows in the game.
   */
  public WorldPainter(List<Space> gameSpaces, int totalColumns, int totalRows) {
    if (gameSpaces == null) {
      throw new IllegalArgumentException("Game spaces and player character cannot be null.");
    }
    if (totalColumns <= 0 || totalRows <= 0) {
      throw new IllegalArgumentException("Total columns and rows must be positive integers.");
    }
    this.gameSpaces = gameSpaces;
    this.totalColumns = totalColumns;
    this.totalRows = totalRows;
  }

  /**
   * Creates an image of the game world map.
   * 
   * @param scaleFactor  The scale factor for the image.
   * @param borderPadding The padding around the border of the image.
   * @return The BufferedImage of the game world map.
   * @throws IOException  If an error occurs while creating the image.
   */
  public BufferedImage createImage(int scaleFactor, int borderPadding) throws IOException {
    int imageWidth = totalColumns * scaleFactor + borderPadding;
    int imageHeight = totalRows * scaleFactor + borderPadding;
    
    BufferedImage worldImage = new BufferedImage(imageWidth, 
        imageHeight, BufferedImage.TYPE_INT_RGB);
    Graphics graphicsContext = worldImage.getGraphics();
    
    // Set up background
    graphicsContext.setColor(Color.WHITE);
    graphicsContext.fillRect(0, 0, imageWidth, imageHeight);
    
    // Draw spaces
    drawSpaces(graphicsContext, scaleFactor, borderPadding);
    
    graphicsContext.dispose();
    
    // Save the BufferedImage to a file
    saveImageToFile(worldImage);
    
    return worldImage;
  }

  /**
   * Draws the spaces on the game world map.
   *
   * @param graphicsContext The graphics context to draw on.
   * @param scaleFactor     The scale factor for drawing the spaces.
   * @param borderPadding   The padding around the border of the image.
   */
  private void drawSpaces(Graphics graphicsContext, int scaleFactor, int borderPadding) {
    for (Space space : gameSpaces) {
      int x = space.getUpperLeftColumn() * scaleFactor + borderPadding / 4;
      int y = space.getUpperLeftRow() * scaleFactor + borderPadding / 4;   
      int height = (space.getLowerRightRow() - space.getUpperLeftRow() + 1) * scaleFactor;
      int width = (space.getLowerRightColumn() - space.getUpperLeftColumn() + 1) * scaleFactor;
        
      // Draw the space rectangle outline
      graphicsContext.setColor(Color.ORANGE);
      graphicsContext.drawRect(x, y, width, height);
      
      // Draw the space name in top-left corner with padding
      graphicsContext.setColor(Color.BLACK);
      String spaceName = space.getSpaceName();
      
      // Draw the space name in the top-left corner with padding
      int textX = x + NAME_PADDING;
      int textY = y + graphicsContext.getFontMetrics().getHeight() + NAME_PADDING;
      graphicsContext.drawString(spaceName, textX, textY);
    }
  }

  /**
   * Saves the image to a file.
   * 
   * @param bufferedImage The image to save.
   * @throws IOException If an error occurs while saving the image.
   */
  private void saveImageToFile(BufferedImage bufferedImage) throws IOException {
    // Create res directory if it doesn't exist
    File resDirectory = new File(Constants.SAVE_PATH);
    if (!resDirectory.exists()) {
      boolean created = resDirectory.mkdirs();
      if (!created) {
        throw new IOException("Failed to create directory: " + Constants.SAVE_PATH);
      }
    }

    // Create and save the file
    File outputFile = new File(Constants.SAVE_PATH + "gameWorldMap.png");
    if (!ImageIO.write(bufferedImage, "png", outputFile)) {
      throw new IOException("Failed to save image: No appropriate writer found");
    }
    System.out.println("Image saved to: " + outputFile.getAbsolutePath());
  }
}