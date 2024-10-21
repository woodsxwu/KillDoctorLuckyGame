package control.commands;

import facade.GameFacade;

/**
 * MoveCommand is a class that represents the command to move the player to a new space.
 */
public class MoveCommand implements GameCommand {
  private final String spaceName;

  public MoveCommand(String spaceName) {
    this.spaceName = spaceName;
  }

  @Override
  public String execute(GameFacade facade) {
    try {
      return facade.movePlayer(spaceName);
    } catch (IllegalArgumentException e) {
      return e.getMessage();
    }
  }

  @Override
  public GameCommand create(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    return new MoveCommand(args[0]);
  }
}