package control.commands;

public abstract class AbstractAddPlayerCommand implements GameCommand{
  protected final String playerName;
  protected final String startingSpaceName;
  protected final int maxItems;
  
  protected AbstractAddPlayerCommand(String playerName, String startingSpaceName, int maxItems) {
    this.playerName = playerName;
    this.startingSpaceName = startingSpaceName;
    this.maxItems = maxItems;
  }
}
