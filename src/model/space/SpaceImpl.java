package model.space;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.item.Item;

/**
 * This class represents a space in the game or application, including its dimensions,
 * items contained within it, and its neighboring spaces.
 */
public class SpaceImpl implements Space {

  private final int spaceIndex;
  private final String spaceName;
  private final int upperLeftRow;
  private final int upperLeftColumn;
  private final int lowerRightRow;
  private final int lowerRightColumn;
  private List<Item> items;
  private final List<Integer> neighborIndices;
  
  /**
   * Constructs a new SpaceImpl instance.
   *
   * @param spaceIndex        the index of the space
   * @param spaceName         the name of the space
   * @param upperLeftRow      the upper-left row coordinate of the space
   * @param upperLeftColumn   the upper-left column coordinate of the space
   * @param lowerRightRow     the lower-right row coordinate of the space
   * @param lowerRightColumn  the lower-right column coordinate of the space
   * @param items             the list of items in the space
   * @param neighborIndices    the indices of neighboring spaces
   * @throws IllegalArgumentException if spaceName is null or empty, 
   *         if any coordinate is negative, or if the lower right coordinates
   *          are less than the upper left coordinates
   */
  public SpaceImpl(int spaceIndex, String spaceName, int upperLeftRow, int upperLeftColumn, 
      int lowerRightRow, int lowerRightColumn, List<Item> items, List<Integer> neighborIndices) {
    if (spaceIndex < 0) {
      throw new IllegalArgumentException("Space index cannot be negative.");
    }
    if (spaceName == null || spaceName.trim().isEmpty()) {
      throw new IllegalArgumentException("Space name cannot be null or empty.");
    }
    if (upperLeftRow < 0 || upperLeftColumn < 0 || lowerRightRow < 0 || lowerRightColumn < 0) {
      throw new IllegalArgumentException("Coordinates cannot be negative.");
    }
    if (lowerRightRow <= upperLeftRow || lowerRightColumn <= upperLeftColumn) {
      throw new IllegalArgumentException("Lower right coordinates must be greater"
          + " than upper left coordinates.");
    }
    this.spaceIndex = spaceIndex;
    this.spaceName = spaceName;
    this.upperLeftRow = upperLeftRow;
    this.upperLeftColumn = upperLeftColumn;
    this.lowerRightRow = lowerRightRow;
    this.lowerRightColumn = lowerRightColumn;
    this.items = items;
    this.neighborIndices = neighborIndices;
  }
  
  @Override
  public String getSpaceName() {
    return spaceName;
  }
  
  @Override
  public int getUpperLeftRow() {
    return upperLeftRow;
  }
  
  @Override
  public int getUpperLeftColumn() {
    return upperLeftColumn;
  }
  
  @Override
  public int getLowerRightRow() {
    return lowerRightRow;
  }
  
  @Override
  public int getLowerRightColumn() {
    return lowerRightColumn;
  }
  
  @Override
  public List<Item> getItems() {
    List<Item> copyItems = new ArrayList<Item>();
    for (Item item : items) {
      copyItems.add(item.copy());
    }
    return copyItems;
  }
  
  @Override
  public void addItem(Item item) {
    items.add(item);
  }
  
  @Override
  public void removeItem(Item item) {
    if (!items.contains(item)) {
      throw new IllegalArgumentException("No such item!");
    }
    items.remove(item);
  }
  
  @Override
  public int getSpaceIndex() {
    return spaceIndex;
  }
  
  @Override
  public List<Integer> getNeighborIndices() {
    List<Integer> copiedList = new ArrayList<>();
    copiedList.addAll(neighborIndices);
    return copiedList;
  }

  @Override
  public void setNeighbors(List<Integer> neighbors) {
    neighborIndices.clear();
    neighborIndices.addAll(neighbors);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SpaceImpl)) {
      return false;
    }
    SpaceImpl other = (SpaceImpl) obj;
    return spaceIndex == other.spaceIndex && upperLeftRow == other.upperLeftRow 
        && upperLeftColumn == other.upperLeftColumn
        && lowerRightRow == other.lowerRightRow
        && lowerRightColumn == other.lowerRightColumn
        && Objects.equals(spaceName, other.spaceName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spaceIndex, spaceName, upperLeftRow, 
        upperLeftColumn, lowerRightRow, lowerRightColumn);
  }

  @Override
  public String toString() {
    return String.format("Space{name='%s', index=%d, upperLeft=(%d, %d), lowerRight=(%d, %d)}", 
                         spaceName, spaceIndex, upperLeftRow, upperLeftColumn, 
                         lowerRightRow, lowerRightColumn);
  }
}
