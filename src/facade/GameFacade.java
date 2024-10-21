package facade;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
   * @throws IOException if an error occurs while creating the world map
   */
  BufferedImage createWorldMap() throws IOException;

  /**
   * Adds a human player to the game.
   *
   * @param name          the name of the player
   * @param startingSpace the name of the space where the player should start
   * @param maxItems      the maximum number of items the player can carry
   */
  void addHumanPlayer(String name, String startingSpace, int maxItems);

  /**
   * Adds a computer player to the game.
   * 
   * @param name the name of the computer player
   * @param startingSpace the name of the space where the computer player should start
   * @param maxItems the maximum number of items the computer player can carry
   */
  void addComputerPlayer(String name, String startingSpace, int maxItems);
  
  /**
   * Moves a player to a new space.
   *
   * @param spaceName the name of the space to move the player to
   * @return a String containing the result of the move
   */
  String movePlayer(String spaceName);

  /**
   * Allows a player to pick up an item.
   *
   * @param itemName the name of the item to pick up
   * @return a String containing the result of the pick up operation
   */
  String playerPickUpItem(String itemName);

  /**
   * Retrieves information about the surroundings of a player.
   *
   * @return a String containing information about the player's surroundings
   */
  String playerLookAround();

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
  
  /**
   * Sets the maximum number of turns for the game.
   *
   * @param maxTurns the maximum number of turns
   * @throws IllegalArgumentException if maxTurns is not positive
   */
  void setMaxTurns(int maxTurns);

  /**
   * Gets the current turn number.
   *
   * @return the current turn number
   */
  int getCurrentTurn();
  
  /**
   * moves the target character in the game.
   */
  void moveTargetCharacter();
  
  /**
   * Check if it is the computer player's turn.
   * 
   * @return true if it is the computer player's turn, false otherwise
   */
  boolean computerPlayerTurn();
  
  /**
   * Allows the computer player to take a turn.
   * 
   * @return the result of the computer player's turn
   */
  String computerPlayerTakeTurn();
  
  /**
   * Retrieves the number of players in the game.
   *
   * @return the number of players in the game
   */
  int getPlayerCount();
}