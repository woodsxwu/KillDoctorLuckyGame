package control.commands;

import facade.GameFacade;

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
}
