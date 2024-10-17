package model.world;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;

/**
 * Represents a game world containing spaces and target characters.
 * 
 * This interface defines the basic functionalities that any world should implement,
 * including methods for retrieving world properties, managing characters, and generating maps.
 */
public interface World {

  /**
   * Retrieves the name of the world.
   *
   * @return the name of the world as a {@link String}.
   */
  String getWorldName();

  /**
   * Retrieves the number of rows in the world.
   *
   * @return an integer representing the number of rows.
   */
  int getRows();

  /**
   * Retrieves the number of columns in the world.
   *
   * @return an integer representing the number of columns.
   */
  int getColumns();

  /**
   * Retrieves the total number of spaces in the world.
   *
   * @return an integer representing the total space count.
   */
  int getTotalSpace();

  /**
   * Retrieves the total number of items in the world.
   *
   * @return an integer representing the total item count.
   */
  int getTotalItems();

  /**
   * Finds and sets the neighboring spaces for all spaces in the world.
   */
  void findNeighbors();

  /**
   * Creates a visual representation of the world as a map.
   *
   * @return a {@link BufferedImage} representing the world map.
   * @throws IOException if an error occurs while creating the map.
   */
  BufferedImage createWorldMap() throws IOException;

  /**
   * Moves the target character within the world for 1 step.
   */
  void moveTargetCharacter();

  /**
   * Retrieves the current target character in the world.
   *
   * @return the copy of {@link TargetCharacter} currently in the world.
   */
  TargetCharacter getTargetCharacter();
  
  /**
   * Retrieves information about a specific space by its index.
   *
   * @param index the index of the space
   * @return a {@link String} containing information about the specified space.
   */
  String getSpaceInfoByIndex(int index);
  
  /**
   * Adds a player to the game world.
   *
   * @param player the player to be added
   */
  void addPlayer(Player player);

  /**
   * Retrieves a list of all players in the game world.
   *
   * @return a list of all players
   */
  List<Player> getPlayers();

  /**
   * Gets the current active player.
   *
   * @return the current player
   */
  Player getCurrentPlayer();

  /**
   * Advances the game to the next turn, updating the current player.
   */
  void nextTurn();

  /**
   * Gets the total number of players in the game.
   *
   * @return the number of players
   */
  int getPlayerCount();

  /**
   * Sets the maximum number of turns for the game.
   *
   * @param maxTurns the maximum number of turns
   */
  void setMaxTurns(int maxTurns);

  /**
   * Gets the current turn number.
   *
   * @return the current turn number
   */
  int getCurrentTurn();

  /**
   * Gets the maximum number of turns for the game.
   *
   * @return the maximum number of turns
   */
  int getMaxTurns();
  
  /**
   * Retrieves a space by its index.
   *
   * @param index the index of the space
   * @return the space at the given index
   */
  Space getSpaceByIndex(int index);
  
  /**
   * Retrieves a list of all spaces in the world.
   *
   * @return a list of all spaces
   */
  List<Space> getSpaces();
}

