package control.commands;

import facade.GameFacade;

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