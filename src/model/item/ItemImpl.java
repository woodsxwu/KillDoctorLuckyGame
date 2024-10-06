package model.item;

import java.util.Objects;

/**
 * This class represents an item with a name, damage value, and its position in a space.
 */
public class ItemImpl implements Item {

  private final String itemName;
  private final int damage;
  private int spaceIndex;
  
  /**
   * Constructs a new ItemImpl instance.
   *
   * @param itemName  the name of the item
   * @param damage    the damage value of the item
   * @param spaceIndex the index of the space where the item is located
   * @throws IllegalArgumentException if itemName is null or empty, 
   *         if damage is negative, or if spaceIndex is negative
   */
  public ItemImpl(String itemName, int damage, int spaceIndex) {
    if (itemName == null || itemName.trim().isEmpty()) {
      throw new IllegalArgumentException("Item name cannot be null or empty.");
    }
    if (damage < 0) {
      throw new IllegalArgumentException("Damage cannot be negative.");
    }
    if (spaceIndex < 0) {
      throw new IllegalArgumentException("Space index cannot be negative.");
    }
    this.itemName = itemName;
    this.damage = damage;
    this.spaceIndex = spaceIndex;
  }
  
  @Override
  public String getItemName() {
    return itemName;
  }

  @Override
  public int getDamage() {
    return damage;
  }

  @Override
  public int getSpaceIndex() {
    return spaceIndex;
  }

  @Override
  public Item copy() {
    return new ItemImpl(itemName, damage, spaceIndex);
  }
  

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ItemImpl)) {
      return false;
    }
    ItemImpl other = (ItemImpl) obj;
    return damage == other.damage 
        && spaceIndex == other.spaceIndex 
        && Objects.equals(itemName, other.itemName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemName, damage, spaceIndex);
  }

  @Override
  public String toString() {
    return String.format("Item{name='%s', damage=%d, spaceIndex=%d}", itemName, damage, spaceIndex);
  }
}