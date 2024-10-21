package control.commands;

import facade.GameFacade;

/**
 * AddComputerPlayerCommand is a class that represents a command for adding a
 * computer player to the game.
 */
public class AddComputerPlayerCommand extends AbstractAddPlayerCommand {

  public AddComputerPlayerCommand(String playerName, String startingSpaceName, int maxItems) {
    super(playerName, startingSpaceName, maxItems);
  }

  @Override
  public String execute(GameFacade facade) {
    try {
      facade.addComputerPlayer(playerName, startingSpaceName, maxItems);
      return String.format("Computer player %s added successfully", playerName);
    } catch (IllegalArgumentException e) {
      return "Failed to add computer player: " + e.getMessage();
    }
  }
  
  @Override
  public GameCommand create(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    try {
      return new AddComputerPlayerCommand(args[0], args[1], Integer.parseInt(args[2]));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Item capacity must be an integer");
    }
  }
}
