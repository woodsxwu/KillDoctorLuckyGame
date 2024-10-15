package model.player;

import java.util.ArrayList;
import java.util.List;

import model.item.Item;

/**
 * AbstractPlayer provides a base implementation for the Player interface.
 */
public abstract class AbstractPlayer implements Player {
  protected String name;
  protected int currentSpaceIndex;
  protected List<Item> items;
  protected boolean hasMaxItems;
  protected int maxItems;

  /**
   * Constructs an AbstractPlayer with the given parameters.
   *
   * @param name the name of the player
   * @param currentSpaceIndex the starting space index for the player
   * @param maxItems the maximum number of items the player can carry
   */
  protected AbstractPlayer(String name, int currentSpaceIndex, int maxItems) {
    this.name = name;
    this.currentSpaceIndex = currentSpaceIndex;
    this.items = new ArrayList<>();
    this.hasMaxItems = maxItems != Integer.MAX_VALUE;
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
    return new ArrayList<>(items);
  }

  @Override
  public boolean addItem(Item item) {
    if (items.size() < maxItems) {
      items.add(item);
      return true;
    }
    return false;
  }

  @Override
  public String lookAround() {
    // This method should be implemented in the World class and called from here
    // For now, we'll return a placeholder message
    return "You are in space " + currentSpaceIndex + ".";
  }

  @Override
  public String getDescription() {
    StringBuilder description = new StringBuilder();
    description.append("Name: ").append(name).append("\n");
    description.append("Current Space: ").append(currentSpaceIndex).append("\n");
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
}
