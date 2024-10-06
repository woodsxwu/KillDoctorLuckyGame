package model.world;

import java.awt.image.BufferedImage;
import model.target.TargetCharacter;

/**
 * Represents a game world containing spaces and target characters.
 * 
 * This interface defines the basic functionalities that any world should implement,
 * including methods for retrieving world properties, managing characters, and generating maps.
 */
public interface World {

  /**
   * Retrieves the name of the world.
   *
   * @return the name of the world as a {@link String}.
   */
  String getWorldName();

  /**
   * Retrieves the number of rows in the world.
   *
   * @return an integer representing the number of rows.
   */
  int getRows();

  /**
   * Retrieves the number of columns in the world.
   *
   * @return an integer representing the number of columns.
   */
  int getColumns();

  /**
   * Retrieves the total number of spaces in the world.
   *
   * @return an integer representing the total space count.
   */
  int getTotalSpace();

  /**
   * Retrieves the total number of items in the world.
   *
   * @return an integer representing the total item count.
   */
  int getTotalItems();

  /**
   * Finds and sets the neighboring spaces for all spaces in the world.
   */
  void findNeighbors();

  /**
   * Creates a visual representation of the world as a map.
   *
   * @return a {@link BufferedImage} representing the world map.
   */
  BufferedImage createWorldMap();

  /**
   * Moves the target character within the world for 1 step.
   */
  void moveTargetCharacter();

  /**
   * Retrieves the current target character in the world.
   *
   * @return the copy of {@link TargetCharacter} currently in the world.
   */
  TargetCharacter getTargetCharacter();
  
  /**
   * Retrieves information about a specific space by its index.
   *
   * @param index the index of the space
   * @return a {@link String} containing information about the specified space.
   */
  String getSpaceInfoByIndex(int index);
}

