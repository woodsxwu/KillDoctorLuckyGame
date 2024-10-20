package model.player;

import java.util.ArrayList;
import java.util.List;

import model.item.Item;
import model.space.Space;

/**
 * AbstractPlayer provides a base implementation for the Player interface.
 */
public abstract class AbstractPlayer implements Player {
  protected String name;
  protected int currentSpaceIndex;
  protected List<Item> items;
  protected int maxItems;

  /**
   * Constructs an AbstractPlayer with the given parameters.
   *
   * @param name              the name of the player
   * @param currentSpaceIndex the starting space index for the player
   * @param maxItems          the maximum number of items the player can carry, if
   *                          maxItems is negative, then the player has no limit
   *                          on the number of items
   */
  protected AbstractPlayer(String name, int currentSpaceIndex, int maxItems) {
    this.name = name;
    this.currentSpaceIndex = currentSpaceIndex;
    this.items = new ArrayList<>();
    this.maxItems = maxItems;
  }

  @Override
  public String getPlayerName() {
    return name;
  }

  @Override
  public int getCurrentSpaceIndex() {
    return currentSpaceIndex;
  }

  @Override
  public void setCurrentSpaceIndex(int spaceIndex) {
    this.currentSpaceIndex = spaceIndex;
  }

  @Override
  public List<Item> getItems() {
    List<Item> itemsCopy = new ArrayList<>();
    for (Item item : this.items) {
      itemsCopy.add(item.copy());
    }
    return itemsCopy;
  }

  @Override
  public boolean addItem(Item item) {
    if (maxItems < 0 || items.size() < maxItems) {
      items.add(item);
      return true;
    }
    return false;
  }

  @Override
  public String lookAround(List<Space> spaces) {
    StringBuilder description = new StringBuilder();
    description.append(name).append(" is currently in: ")
        .append(spaces.get(currentSpaceIndex).getSpaceName()).append("\n");
    description.append(spaces.get(currentSpaceIndex).getNeighborInfo(spaces));
    return description.toString();
  }

  @Override
  public String getDescription(List<Space> spaces) {
    StringBuilder description = new StringBuilder();
    description.append("Name: ").append(name).append("\n");
    description.append("Current Space: ").append(spaces.get(currentSpaceIndex).getSpaceName()).append("\n");
    description.append("Items: ");
    if (items.isEmpty()) {
      description.append("None");
    } else {
      for (Item item : items) {
        description.append(item.getItemName()).append(", ");
      }
      description.setLength(description.length() - 2); // Remove last ", "
    }
    return description.toString();
  }

  @Override
  public void move(int spaceIndex) {
    this.currentSpaceIndex = spaceIndex;
  }
}
