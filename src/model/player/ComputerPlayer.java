package model.player;

import java.util.List;
import model.item.Item;
import model.pet.Pet;
import model.space.Space;
import model.target.TargetCharacter;

/**
 * Represents a computer player in the game. Computer players take turns
 * automatically, choosing actions randomly.
 */
public class ComputerPlayer extends AbstractPlayer {
  private static final int[] NON_ATTACK_ACTIONS = {0, 1, 2, 4}; // move, look, pickup, move pet
  private RandomGenerator randomGenerator;

  /**
   * Creates a new computer player.
   *
   * @param name              The name of the player.
   * @param currentSpaceIndex The index of the space the player is currently on.
   * @param maxItems          The maximum number of items the player can carry.
   * @param randomGenerator   The random generator to use for choosing actions.
   */
  public ComputerPlayer(String name, int currentSpaceIndex, int maxItems,
      RandomGenerator randomGenerator) {
    super(name, currentSpaceIndex, maxItems);
    this.randomGenerator = randomGenerator;
  }
  
  @Override
  public String takeTurn(List<Space> spaces, List<Player> players, 
      TargetCharacter target, Pet pet, Boolean canAttack) {
    
    // If we can attack, always attempt to attack
    if (canAttack) {
      return botMaxAttack(target);
    }
    
    // If we can't attack, randomly choose between other actions
    int actionIndex = randomGenerator.nextInt(NON_ATTACK_ACTIONS.length);
    int action = NON_ATTACK_ACTIONS[actionIndex];
    
    switch (action) {
      case 0:
        return moveRandomly(spaces);
      case 1:
        return lookAround(spaces, players, target, pet);
      case 2:
        return pickUpRandomItem(spaces);
      case 4:
        return movePetRandomly(spaces, pet);
      default:
        return "Computer player did nothing.";
    }
  }
  
  /**
   * Attempts to attack the target character with the item with the highest
   * damage. If no items are available, the player will attempt to attack with
   * "poke".
   *
   * @param target The target character to attack.
   * @return A string describing the attack attempt.
   */
  private String botMaxAttack(TargetCharacter target) {
    // Find the item with max damage
    Item maxDamageItem = null;
    int maxDamage = 0;
    
    for (Item item : items) {
      if (item.getDamage() > maxDamage) {
        maxDamage = item.getDamage();
        maxDamageItem = item;
      }
    }

    // If no items found, use "poke", otherwise use the max damage item
    String itemToUse = (maxDamageItem != null) ? maxDamageItem.getItemName() : "poke";
    return attack(itemToUse, target);
  }

  /**
   * Moves the pet to a random neighboring space.
   *
   * @param spaces The list of spaces in the world.
   * @param pet    The pet to move.
   * @return A string describing the action taken.
   */
  private String movePetRandomly(List<Space> spaces, Pet pet) {
    int currentPetSpace = pet.getCurrentSpaceIndex();
    int moveTo = (currentPetSpace + 1 + randomGenerator.nextInt(spaces.size() - 1)) % spaces.size();
    
    pet.setSpaceIndex(moveTo);
    return String.format("%s moved pet to %s.", name, spaces.get(moveTo).getSpaceName());
  }
  
  /**
   * Moves the player to a random neighboring space.
   * 
   * @param spaces The list of spaces in the world.
   * @return A string describing the action taken.
   */
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

  /**
   * Looks around the current space, picking up a random item if available.
   *
   * @param spaces  The list of spaces in the world.
   * @param players The list of players in the world.
   * @param target  The target character to attack.
   * @param pet     The pet to move.
   * @return A string describing the action taken.
   */
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
  
  @Override
  protected Player createCopy() {
    return new ComputerPlayer(
      this.getPlayerName(),
      this.getCurrentSpaceIndex(),
      this.maxItems,
      new RandomGenerator() // New random generator for copy
    );
  }

  @Override
  public Boolean isComputer() {
    return true;
  }
}