package control.commands;

import facade.GameFacade;

/**
 * A class that represents the command to display player information.
 */
public class DisplayPlayerInfoCommand implements GameCommand {

  private final String playerName;
  
  public DisplayPlayerInfoCommand(String playerName) {
    this.playerName = playerName;
  }
  
  @Override
  public String execute(GameFacade facade) {
    return facade.getPlayerInfo(playerName);
  }
}