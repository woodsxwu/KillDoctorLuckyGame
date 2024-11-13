package model.pet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import model.space.Space;

/**
 * This class represents a pet in the game, including its name and current
 * position.
 */
public class PetImpl implements Pet {

  private String petName;
  private Stack<Integer> pathStack;
  private Set<Integer> visitedSpaces;
  
  private int currentSpaceIndex;
  
  /**
   * Constructs a new PetImpl instance.
   *
   * @param name the name of the pet
   * @param currentSpaceIndex the starting space index for the pet
   * @throws IllegalArgumentException if name is null or empty, or if currentSpaceIndex is negative
   */
  public PetImpl(String name, int currentSpaceIndex) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Pet name cannot be null or empty");
    }
    if (currentSpaceIndex < 0) {
      throw new IllegalArgumentException("Space index cannot be negative");
    }
    this.petName = name;
    this.currentSpaceIndex = currentSpaceIndex;
    this.pathStack = new Stack<>();
    this.visitedSpaces = new HashSet<>();
    this.pathStack.push(currentSpaceIndex);
    this.visitedSpaces.add(currentSpaceIndex);
  }
  
  @Override
  public int getCurrentSpaceIndex() {
    return this.currentSpaceIndex;
  }

  @Override
  public String getPetName() {
    return this.petName;
  }

  @Override
  public Pet copy() {
    return new PetImpl(this.petName, this.currentSpaceIndex);
  }

  @Override
  public void setSpaceIndex(int spaceIndex) {
    if (spaceIndex < 0) {
      throw new IllegalArgumentException("Space number cannot be negative");
    }
    this.currentSpaceIndex = spaceIndex;
    // When manually moved, clear the DFS state and start fresh from new position
    resetDfs();
  }

  @Override
  public String getPetDescription() {
    String description = "Pet: " + this.petName + " is currently at space " 
        + this.currentSpaceIndex;
    return description;
  }

  @Override
  public int moveFollowingDfs(List<Space> spaces) {
    if (spaces == null || spaces.isEmpty()) {
      throw new IllegalArgumentException("Spaces list cannot be null or empty");
    }
    
    // Get current space and its neighbors
    Space currentSpace = spaces.get(currentSpaceIndex);
    List<Integer> neighbors = currentSpace.getNeighborIndices();
    
    // Try to find an unvisited neighbor
    Integer nextSpace = null;
    for (Integer neighborIndex : neighbors) {
      if (!visitedSpaces.contains(neighborIndex)) {
        nextSpace = neighborIndex;
        break;
      }
    }
    
    if (nextSpace != null) {
      // Found an unvisited neighbor - move forward
      currentSpaceIndex = nextSpace;
      pathStack.push(nextSpace);
      visitedSpaces.add(nextSpace);
    } else if (!pathStack.isEmpty()) {
      // No unvisited neighbors - backtrack
      pathStack.pop(); // Remove current space
      if (!pathStack.isEmpty()) {
        currentSpaceIndex = pathStack.peek();
      } else {
        // If stack is empty after pop, we've explored everything - restart DFS
        resetDfs();
      }
    } else {
      // This should not happen in normal operation
      resetDfs();
    }
    return currentSpaceIndex;
  }
  
  /**
   * Resets the DFS traversal state, starting fresh from current position.
   */
  private void resetDfs() {
    pathStack.clear();
    visitedSpaces.clear();
    pathStack.push(currentSpaceIndex);
    visitedSpaces.add(currentSpaceIndex);
  }
}
