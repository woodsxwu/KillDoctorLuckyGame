package model.pet;

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
}
