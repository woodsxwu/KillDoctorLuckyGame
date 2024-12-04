package model.player;

import java.util.List;
import model.item.Item;
import model.pet.Pet;
import model.space.Space;
import model.target.TargetCharacter;

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
   * @param spaces the list of spaces in the game
   * @param players the list of players in the game
   * @param target the target character
   * @param pet the pet in the game
   * @return a description of the player's surroundings
   */
  String lookAround(List<Space> spaces, List<Player> players, TargetCharacter target, Pet pet);

  /**
   * Gets a description of the player.
   *
   * @param spaces the list of spaces in the game
   * @return a string describing the player
   */
  String getDescription(List<Space> spaces);
  
  /**
   * Moves the player to the given index.
   * @param index the index to move to
   */
  void move(int index);
  
  /**
   * Allows the player to take a turn.
   * 
   * @param spaces the list of spaces in the game
   * @param players the list of players in the game
   * @param target the target character
   * @param pet the pet in the game
   * @param canAttack whether the player can attack
   * @return a string representing the player's action
   */
  String takeTurn(List<Space> spaces, List<Player> players, 
      TargetCharacter target, Pet pet, Boolean canAttack);
  
  /**
   * Allows the player to attack a target.
   * 
   * @param itemName the name of the item to attack with
   * @param target the target to attack
   * @return a string representing the result of the attack
   * @throws IllegalArgumentException if the item is not found in the player's inventory
   */
  String attack(String itemName, TargetCharacter target);
  
  /**
   * Retrieves some limited information about where they are in the world.
   * 
   * @param spaces the list of spaces
   * @return a string containing limited information about the player position
   */
  String limitedInfo(List<Space> spaces);
  
  /**
   * Creates a copy of the current player.
   *
   * @return a new instance of the player that is a copy of this player
   */
  Player copy();
  
  /**
   * Checks if the player is a computer player.
   * 
   * @return true if the player is a computer player, false otherwise
   */
  Boolean isComputer();
}
