package model.space;

import java.util.List;
import model.item.Item;

/**
 * Represents a space in the game or application.
 * 
 * This interface defines the basic functionalities that any space should implement,
 * including methods for managing items and retrieving space properties.
 */
public interface Space {

  /**
   * Retrieves the name of the space.
   *
   * @return the name of the space as a {@link String}.
   */
  String getSpaceName();

  /**
   * Retrieves the upper-left row coordinate of the space.
   *
   * @return the row index of the upper-left corner.
   */
  int getUpperLeftRow();

  /**
   * Retrieves the upper-left column coordinate of the space.
   *
   * @return the column index of the upper-left corner.
   */
  int getUpperLeftColumn();

  /**
   * Retrieves the lower-right row coordinate of the space.
   *
   * @return the row index of the lower-right corner.
   */
  int getLowerRightRow();

  /**
   * Retrieves the lower-right column coordinate of the space.
   *
   * @return the column index of the lower-right corner.
   */
  int getLowerRightColumn();

  /**
   * Retrieves the list of items currently located in the space.
   *
   * @return a {@link List} of {@link Item} objects present in the space.
   */
  List<Item> getItems();

  /**
   * Retrieves the indices of neighboring spaces.
   *
   * @return a {@link List} of integers representing the indices of neighboring spaces.
   */
  List<Integer> getNeighborIndices();

  /**
   * Sets the neighboring space indices for this space.
   *
   * @param neighbors a {@link List} of integers representing the indices of neighboring spaces.
   */
  void setNeighbors(List<Integer> neighbors);

  /**
   * Adds an item to the space.
   *
   * @param item the {@link Item} to be added to the space.
   */
  void addItem(Item item);

  /**
   * Removes an item from the space.
   *
   * @param item the {@link Item} to be removed from the space.
   */
  void removeItem(Item item);

  /**
   * Retrieves the index of the space.
   *
   * @return an integer representing the index of the space.
   */
  int getSpaceIndex();
  
  /**
   * Makes a copy of the space.
   */
  Space copy();
  
  /**
   * Retrieves information about the neighboring spaces.
   * 
   * @param spaces the list of spaces in the world
   * @return
   */
  String getNeighborInfo(List<Space> spaces);
  
  /**
   * Retrieves information about the items in the space.
   * 
   * @return a string containing information about the items in the space
   */
  String getItemsInfo();
}
