package facade;

import java.awt.image.BufferedImage;

/**
 * The GameFacade interface provides a simplified interface for interacting with the game model.
 * It encapsulates the complexity of the underlying game logic and provides methods
 * for common game operations.
 */
public interface GameFacade {

  /**
   * Retrieves the name of the game world.
   *
   * @return the name of the world as a String
   */
  String getWorldName();

  /**
   * Retrieves information about a specific space in the world.
   *
   * @param spaceName the name of the space to get information about
   * @return a String containing information about the specified space
   */
  String getSpaceInfo(String spaceName);

  /**
   * Creates and returns a visual representation of the world map.
   *
   * @return a BufferedImage representing the world map
   */
  BufferedImage createWorldMap();

  /**
   * Adds a new player to the game.
   *
   * @param name the name of the player
   * @param startingSpace the name of the space where the player starts
   * @param hasMaxItems whether the player has a maximum item limit
   * @param maxItems the maximum number of items the player can carry (if hasMaxItems is true)
   */
  void addPlayer(String name, String startingSpace, boolean hasMaxItems, int maxItems);

  /**
   * Moves a player to a new space.
   *
   * @param playerName the name of the player to move
   * @param spaceName the name of the space to move the player to
   */
  void movePlayer(String playerName, String spaceName);

  /**
   * Allows a player to pick up an item.
   *
   * @param playerName the name of the player picking up the item
   * @param itemName the name of the item to pick up
   */
  void playerPickUpItem(String playerName, String itemName);

  /**
   * Retrieves information about the surroundings of a player.
   *
   * @param playerName the name of the player
   * @return a String containing information about the player's surroundings
   */
  String playerLookAround(String playerName);

  /**
   * Retrieves information about a specific player.
   *
   * @param playerName the name of the player
   * @return a String containing information about the specified player
   */
  String getPlayerInfo(String playerName);

  /**
   * Retrieves the name of the current player.
   *
   * @return the name of the current player as a String
   */
  String getCurrentPlayerName();

  /**
   * Advances the game to the next turn.
   */
  void nextTurn();

  /**
   * Checks if the game has ended.
   *
   * @return true if the game has ended, false otherwise
   */
  boolean isGameEnded();
}