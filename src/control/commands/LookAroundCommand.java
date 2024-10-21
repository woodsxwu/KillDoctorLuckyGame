package control.commands;

import facade.GameFacade;

/**
 * A class that represents the command to look around the room.
 */
public class LookAroundCommand implements GameCommand {
  @Override
  public String execute(GameFacade facade) {
    return facade.playerLookAround();
  }
  
  @Override
  public GameCommand create(String[] args) {
    if (args.length != 0) {
      throw new IllegalArgumentException("No arguments required");
    }
    return new LookAroundCommand();
  }
}
