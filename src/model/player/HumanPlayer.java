package model.player;

import java.util.List;
import model.space.Space;

/**
 * HumanPlayer represents a human-controlled player in the game.
 */
public class HumanPlayer extends AbstractPlayer {
  /**
   * Constructs a HumanPlayer with the given parameters.
   *
   * @param name              the name of the player
   * @param currentSpaceIndex the starting space index for the player
   * @param maxItems          the maximum number of items the player can carry
   */
  public HumanPlayer(String name, int currentSpaceIndex, int maxItems) {
    super(name, currentSpaceIndex, maxItems);
  }

  @Override
  public String takeTurn(List<Space> spaces, List<Player> players) {
    throw new UnsupportedOperationException("Human player cannot use takeTurn method");
  }
}