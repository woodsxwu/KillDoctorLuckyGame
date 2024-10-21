package control.commands;

import facade.GameFacade;

/**
 * Command to add a human player to the game.
 */
public class AddHumanPlayerCommand extends AbstractAddPlayerCommand {

  public AddHumanPlayerCommand(String playerName, String startingSpaceName, int maxItems) {
    super(playerName, startingSpaceName, maxItems);
  }

  @Override
  public String execute(GameFacade facade) {
    try {
      facade.addHumanPlayer(playerName, startingSpaceName, maxItems);
      return String.format("Human player %s added successfully", playerName);
    } catch (IllegalArgumentException e) {
      return "Failed to add human player: " + e.getMessage();
    }
  }
  
  @Override
  public GameCommand create(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    try {
      return new AddHumanPlayerCommand(args[0], args[1], Integer.parseInt(args[2]));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Item capacity must be an integer");
    }
  }
}