package control.commands;

/**
 * Abstract class for commands that add a player to the game.
 * 
 * @param playerName        the name of the player to add
 * @param startingSpaceName the name of the space the player starts on
 * @param maxItems          the maximum number of items the player can carry
 */
public abstract class AbstractAddPlayerCommand implements GameCommand {
  protected final String playerName;
  protected final String startingSpaceName;
  protected final int maxItems;
  
  /**
   * Creates a new AbstractAddPlayerCommand.
   * 
   * @param playerName        the name of the player to add
   * @param startingSpaceName the name of the space the player starts on
   * @param maxItems          the maximum number of items the player can carry
   */
  protected AbstractAddPlayerCommand(String playerName, String startingSpaceName, int maxItems) {
    this.playerName = playerName;
    this.startingSpaceName = startingSpaceName;
    this.maxItems = maxItems;
  }
}
