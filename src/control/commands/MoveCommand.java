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
      facade.movePlayer(spaceName);
      return String.format("%s moved to %s", facade.getCurrentPlayerName(), spaceName);
    } catch (IllegalArgumentException e) {
      return "Invalid move: " + e.getMessage();
    }
  }
}