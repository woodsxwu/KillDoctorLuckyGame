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

  @Override
  public GameCommand create(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    return new DisplayPlayerInfoCommand(args[0]);
  }
}