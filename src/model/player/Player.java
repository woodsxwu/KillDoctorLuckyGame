package model.player;

import java.util.List;

import model.item.Item;
import model.space.Space;

/**
 * The Player interface represents a player in the game.
 */
public interface Player {
  /**
   * Gets the name of the player.
   *
   * @return the player's name
   */
  String getPlayerName();

  /**
   * Gets the current space index of the player.
   *
   * @return the current space index
   */
  int getCurrentSpaceIndex();

  /**
   * Sets the current space index of the player.
   *
   * @param spaceIndex the new space index
   */
  void setCurrentSpaceIndex(int spaceIndex);

  /**
   * Gets the list of items the player is carrying.
   *
   * @return a list of items
   */
  List<Item> getItems();

  /**
   * Attempts to add an item to the player's inventory.
   *
   * @param item the item to add
   * @return true if the item was added successfully, false otherwise
   */
  boolean addItem(Item item);

  /**
   * Allows the player to look around their current space.
   *
   * @return a description of the player's surroundings
   */
  String lookAround(List<Space> spaces);

  /**
   * Gets a description of the player.
   *
   * @return a string describing the player
   */
  String getDescription();
  
  /**
   * Moves the player to the given index.
   * @param index the index to move to
   */
  void move(int index);
  
  /**
   * Allows the player to take a turn.
   * 
   * @param spaces the list of spaces in the game
   */
  void takeTurn(List<Space> spaces);
}
