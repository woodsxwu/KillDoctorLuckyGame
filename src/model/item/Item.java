package model.item;


/**
 * Represents an item in the game.
 * This interface defines the basic functionalities that any item should implement.
 */
public interface Item {
  /**
   * Retrieves the name of the item.
   *
   * @return the name of the item as a {@link String}.
   */
  String getItemName();
  
  /**
   * Retrieves the damage value associated with the item.
   *
   * @return an integer representing the damage dealt by the item.
   */
  int getDamage();
  
  /**
   * Retrieves the index of the space where the item is located.
   *
   * @return an integer representing the index of the space.
   */
  int getSpaceIndex();
  
  /**
   * Creates a copy of the current item.
   *
   * @return a new instance of the item that is a copy of this item.
   */
  Item copy();
}
