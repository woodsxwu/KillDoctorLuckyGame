package control.commands;

import facade.GameFacade;

/**
 * Command to move the pet to a new space.
 */
public class MovePetCommand implements GameCommand {

  private String spaceName;
  
  public MovePetCommand(String spaceName) {
    this.spaceName = spaceName;
  }
  
  @Override
  public GameCommand create(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    return new MovePetCommand(args[0]);
  }

  @Override
  public String execute(GameFacade facade) {
    try {
      return facade.movePet(spaceName);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return "Failed to move pet: " + e.getMessage();
    }
  }

}
