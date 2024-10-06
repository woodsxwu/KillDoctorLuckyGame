package model.target;

/**
 * Represents a target character in the game or application.
 * 
 * This interface defines the basic functionalities that any target character should implement,
 * including methods for managing health, movement, and duplication.
 */
public interface TargetCharacter {

  /**
   * Retrieves the name of the target character.
   *
   * @return the name of the target character as a {@link String}.
   */
  String getTargetName();
  
  /**
   * Retrieves the current health of the target character.
   *
   * @return an integer representing the target character's health.
   */
  int getHealth();
  
  /**
   * Retrieves the current space index of the target character.
   *
   * @return an integer representing the index of the space where the character is located.
   */
  int getCurrentSpaceIndex();
  
  /**
   * Moves the target character for 1 step.
   *
   * @param totalSpaces the number of spaces in the world.
   */
  void move(int totalSpaces);
  
  /**
   * Applies damage to the target character, reducing its health.
   *
   * @param damage an integer representing the amount of damage to be inflicted.
   */
  void takeDamage(int damage);
  
  /**
   * Creates a copy of the current target character.
   *
   * @return a new instance of the target character that is a copy of this character.
   */
  TargetCharacter copy();
  
  /**
   * Sets the current space index of the target character.
   *
   * @param index an integer representing the new index of the space.
   */
  void setCurrentSpaceIndex(int index);
}