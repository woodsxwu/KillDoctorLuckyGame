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
}
