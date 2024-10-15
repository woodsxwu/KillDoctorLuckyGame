package model.player;


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
   * @param randomGenerator the random number generator to use
   */
  public ComputerPlayer(String name, int currentSpaceIndex, int maxItems, RandomGenerator randomGenerator) {
    super(name, currentSpaceIndex, maxItems);
    this.randomGenerator = randomGenerator;
  }

  /**
   * Performs a random action for the computer player.
   */
  public void randomAction() {
    int action = randomGenerator.nextInt(3); // 0: move, 1: pick up item, 2: look around
    switch (action) {
      case 0:
        randomMove();
        break;
      case 1:
        randomAddItem();
        break;
      case 2:
        lookAround();
        break;
    }
  }

  private void randomMove() {
    // This method should interact with the World to move the player randomly
    // For now, we'll just print a placeholder message
    int randomSpace = randomGenerator.nextInt(/* total number of spaces */);
    System.out.println(name + " moves to space " + randomSpace);
    // In actual implementation, you would call setCurrentSpaceIndex(randomSpace)
  }

  private void randomAddItem() {
    // This method should interact with the World to add a random item
    // For now, we'll just print a placeholder message
    System.out.println(name + " attempts to pick up a random item.");
    // In actual implementation, you would check if there are items in the current space
    // and try to add a random one to the player's inventory
  }
}