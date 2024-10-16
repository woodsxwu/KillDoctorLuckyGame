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
 * A class responsible for painting the world based on the given spaces and target character.
 *
 * This class generates a visual representation of the game world as a PNG image,
 * drawing each space with its name and saving the result to a specified file.
 */
public class WorldPainter {

  private final List<Space> gameSpaces;
  private final int totalColumns;
  private final int totalRows;

  /**
   * Constructs a WorldPainter instance.
   *
   * @param gameSpaces the list of spaces in the world
   * @param totalColumns the number of columns in the world
   * @param totalRows the number of rows in the world
   * @throws IllegalArgumentException if any parameter is invalid
   */
  public WorldPainter(List<Space> gameSpaces, 
      int totalColumns, int totalRows) {
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
   * Creates a BufferedImage representing the world and saves it as a PNG file.
   *
   * @param scaleFactor the scale factor for drawing the spaces
   * @param borderPadding the padding to add around the image
   * @return the created BufferedImage
   * @throws IOException  if an error occurs while creating the image
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
   * Draws the spaces on the provided Graphics context.
   *
   * @param graphicsContext the Graphics context to draw on
   * @param scaleFactor the scale factor for drawing the spaces
   * @param borderPadding the padding to add around the image
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
      
      // Center the space name
      graphicsContext.setColor(Color.BLACK);
      String spaceName = space.getSpaceName();
      
      // Get the width and height of the text
      int textWidth = graphicsContext.getFontMetrics().stringWidth(spaceName);
      int textHeight = graphicsContext.getFontMetrics().getHeight();
      
      // Draw the space name
      int centerX = x + (width - textWidth) / 2;
      int centerY = y + (height + textHeight) / 2;
      graphicsContext.drawString(spaceName, centerX, centerY);
    }
  }

  /**
   * Saves the given BufferedImage to a file.
   *
   * @param bufferedImage the BufferedImage to save
   * @throws IOException if an error occurs while saving the image
   */
  private void saveImageToFile(BufferedImage bufferedImage) throws IOException {
    File outputFile = new File(Constants.SAVE_PATH + "gameWorldMap.png");
    ImageIO.write(bufferedImage, "png", outputFile);
    System.out.println("Image saved to: " + outputFile.getPath());
  }
}
