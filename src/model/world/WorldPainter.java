package model.world;

import constants.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import model.space.Space;

public class WorldPainter {

  private final List<Space> gameSpaces;
  private final int totalColumns;
  private final int totalRows;
  private static final int NAME_PADDING = 5; // Padding for space names

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

  private void saveImageToFile(BufferedImage bufferedImage) throws IOException {
    File outputFile = new File(Constants.SAVE_PATH + "gameWorldMap.png");
    ImageIO.write(bufferedImage, "png", outputFile);
    System.out.println("Image saved to: " + outputFile.getPath());
  }
}