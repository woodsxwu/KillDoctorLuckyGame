package model.viewmodel;

import model.pet.Pet;
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * The ViewModel interface defines a contract for retrieving 
 * view-specific data from the model layer.
 * It provides methods for accessing game state information
 * needed to render the view while maintaining separation of concerns.
 */
public interface ViewModel {
  /**
   * Gets the name of the world.
   *
   * @return the name of the world
   */
  String getWorldName();

  /**
   * Gets the number of rows in the world grid.
   *
   * @return the number of rows
   */
  int getRows();

  /**
   * Gets the number of columns in the world grid.
   *
   * @return the number of columns
   */
  int getColumns();

  /**
   * Gets copies of all players in the game to prevent direct modification.
   *
   * @return list of player copies
   */
  List<Player> getPlayerCopies();

  /**
   * Gets copies of all spaces in the world to prevent direct modification.
   *
   * @return list of space copies 
   */
  List<Space> getSpaceCopies();

  /**
   * Gets a copy of the target character to prevent direct modification.
   *
   * @return copy of target character
   */
  TargetCharacter getTargetCopy();

  /**
   * Gets a copy of the pet to prevent direct modification.
   *
   * @return copy of pet
   */
  Pet getPetCopy();

  /**
   * Creates and returns the visual representation of the world map.
   *
   * @return buffered image of the world map
   * @throws IOException if there is an error creating the map
   */
  BufferedImage createWorldMap() throws IOException;
  
  /**
   * Gets a copy of the current player.
   *
   * @return copy of the current player
   */
  Player getCurrentPlayerCopy();
} 