package model.player;

import java.util.List;
import model.item.Item;
import model.space.Space;
import model.target.TargetCharacter;

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
   * @param randomGenerator the RandomGenerator to use for random actions
   */
  public ComputerPlayer(String name, int currentSpaceIndex, int maxItems,
      RandomGenerator randomGenerator) {
    super(name, currentSpaceIndex, maxItems);
    this.randomGenerator = randomGenerator;
  }
  
  @Override
  public String takeTurn(List<Space> spaces, List<Player> players, TargetCharacter target, int petSpaceIndex) {
    int action = randomGenerator.nextInt(3); // 0: move, 1: look around, 2: pick up item

    switch (action) {
      case 0:
        return moveRandomly(spaces);
      case 1:
        return lookAround(spaces, players, target, petSpaceIndex);
      case 2:
        return pickUpRandomItem(spaces);
      default:
        return "Computer player did nothing.";
    }
  }

  private String moveRandomly(List<Space> spaces) {
    if (spaces == null || currentSpaceIndex >= spaces.size()) {
      throw new IllegalArgumentException("Invalid spaces");
    }
    List<Integer> neighbors = spaces.get(currentSpaceIndex).getNeighborIndices();
    if (!neighbors.isEmpty()) {
      int randomNeighborIndex = randomGenerator.nextInt(neighbors.size());
      int destinationIndex = neighbors.get(randomNeighborIndex);
      move(destinationIndex);
      return String.format("%s moved to %s.", name, spaces.get(destinationIndex).getSpaceName());
    }
    return String.format("%s couldn't move (no neighboring spaces).", name);
  }

  private String pickUpRandomItem(List<Space> spaces) {
    if (spaces == null || currentSpaceIndex >= spaces.size()) {
      throw new IllegalArgumentException("Invalid spaces");
    }
    Space currentSpace = spaces.get(currentSpaceIndex);
    List<Item> itemsInSpace = currentSpace.getItems();

    if (!itemsInSpace.isEmpty()) {
      Item randomItem = itemsInSpace.get(randomGenerator.nextInt(itemsInSpace.size()));
      if (addItem(randomItem)) {
        currentSpace.removeItem(randomItem);
        return String.format("%s picked up %s.", name, randomItem.getItemName());
      }
      return String.format("%s tried to pick up %s, but couldn't carry more items.", name,
          randomItem.getItemName());
    }
    return String.format("%s looked for items, but found none.", name);
  }
}