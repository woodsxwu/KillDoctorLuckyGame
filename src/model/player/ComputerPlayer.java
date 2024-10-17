package model.player;

import java.util.List;

import model.item.Item;
import model.space.Space;

/**
 * ComputerPlayer represents a computer-controlled player in the game.
 */
public class ComputerPlayer extends AbstractPlayer {
  private RandomGenerator randomGenerator;

  /**
   * Constructs a ComputerPlayer with the given parameters and a RandomGenerator.
   *
   * @param name the name of the player
   * @param currentSpaceIndex the starting space index for the player
   * @param maxItems the maximum number of items the player can carry
   */
  public ComputerPlayer(String name, int currentSpaceIndex, int maxItems) {
    super(name, currentSpaceIndex, maxItems);
    this.randomGenerator = new RandomGenerator();
  }
  
  @Override
  public void takeTurn(List<Space> spaces) {
    int action = randomGenerator.nextInt(3); // 0: move, 1: look around, 2: pick up item

    switch (action) {
      case 0:
        moveRandomly(spaces);
        break;
      case 1:
        lookAround(spaces);
        break;
      case 2:
        pickUpRandomItem(spaces);
        break;
      default:
        break;
    }
  }

  private void moveRandomly(List<Space> spaces) {
    List<Integer> neighbors = spaces.get(currentSpaceIndex).getNeighborIndices();
    if (!neighbors.isEmpty()) {
      int randomNeighborIndex = randomGenerator.nextInt(neighbors.size());
      int destinationIndex = neighbors.get(randomNeighborIndex);
      move(destinationIndex);
    }
  }

  private void pickUpRandomItem(List<Space> spaces) {
    Space currentSpace = spaces.get(currentSpaceIndex);
    List<Item> itemsInSpace = currentSpace.getItems();

    if (!itemsInSpace.isEmpty()) {
      Item randomItem = itemsInSpace.get(randomGenerator.nextInt(itemsInSpace.size()));
      if (addItem(randomItem)) {
        currentSpace.removeItem(randomItem);
      }
    }
  }
}