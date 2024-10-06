package model.target;

import java.util.Objects;

/**
 * This class represents a target character in the game, including its name, health,
 * and current position within a space.
 */
public class TargetCharacterImpl implements TargetCharacter {

  private final String targetName;
  private int health;
  private int currentSpaceIndex;
  
  /**
   * Constructs a new TargetCharacterImpl instance.
   *
   * @param targetName the name of the target character
   * @param health     the initial health of the target character
   * @throws IllegalArgumentException if targetName is null or empty,
   *         or if health is negative
   */
  public TargetCharacterImpl(String targetName, int health) {
    if (targetName == null || targetName.trim().isEmpty()) {
      throw new IllegalArgumentException("Target name cannot be null or empty.");
    }
    if (health < 0) {
      throw new IllegalArgumentException("Health cannot be negative.");
    }
    this.targetName = targetName;
    this.health = health;
    //The character starts in space 0.
    currentSpaceIndex = 0;
  }
  
  @Override
  public String getTargetName() {
    return targetName;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public void move(int totalSpaces) {
    currentSpaceIndex = (currentSpaceIndex + 1) % totalSpaces;
  }

  @Override
  public int getCurrentSpaceIndex() {
    return currentSpaceIndex;
  }

  @Override
  public void takeDamage(int damage) {
    if (damage < 0) {
      throw new IllegalArgumentException("Dammage can't be negative!");
    }
    
    health -= damage;
    if (health < 0) {
      health = 0;
    }
  }

  @Override
  public TargetCharacter copy() {
    TargetCharacter copyCharacter = new TargetCharacterImpl(targetName, health);
    copyCharacter.setCurrentSpaceIndex(currentSpaceIndex);
    return copyCharacter;
  }

  @Override
  public void setCurrentSpaceIndex(int index) {
    currentSpaceIndex = index;
  }
  

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TargetCharacterImpl)) {
      return false;
    }
    TargetCharacterImpl other = (TargetCharacterImpl) obj;
    return health == other.health 
        && currentSpaceIndex == other.currentSpaceIndex 
           && Objects.equals(targetName, other.targetName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetName, health, currentSpaceIndex);
  }

  @Override
  public String toString() {
    return String.format("TargetCharacter{name='%s', health=%d, currentSpaceIndex=%d}", 
                         targetName, health, currentSpaceIndex);
  }
}

