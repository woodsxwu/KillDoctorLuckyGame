package model.pet;

import java.util.List;

import model.space.Space;

public interface Pet {
  /**
   * Retrieves the current space index of the pet.
   *
   * @return the current space index
   */
  int getCurrentSpaceIndex();

  /**
   * Retrieves the name of the pet.
   *
   * @return the name of the pet
   */
  String getPetName();
  
  /**
   * Creates a copy of the pet.
   *
   * @return a new Pet instance with the same name and current space index
   */
  Pet copy();
  
  /**
   * Sets the space index of the pet.
   *
   * @param spaceIndex the new space
   */
  void setSpaceIndex(int spaceIndex);
  
  /**
   * Retrieves a description of the pet.
   *
   * @return a string describing the pet
   */
  String getPetDescription();

  /**
   * Moves the pet to the next space following a depth-first search algorithm.
   *
   * @param spaces the list of spaces in the game
   * @return the index of the space the pet moved to
   */
  int moveFollowingDFS(List<Space> spaces);
}
