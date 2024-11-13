package control.commands;

import facade.GameFacade;

/**
 * Command to display information about a space.
 */
public class DisplaySpaceInfoCommand implements GameCommand {
  private final String spaceName;

  /**
   * Constructor for the DisplaySpaceInfoCommand.
   * 
   * @param spaceName the name of the space to display information
   */
  public DisplaySpaceInfoCommand(String spaceName) {
    this.spaceName = spaceName;
  }

  @Override
  public String execute(GameFacade facade) {
    try {
      return facade.getSpaceInfo(spaceName);
    } catch (IllegalArgumentException e) {
      return "Failed to get space info: " + e.getMessage();
    }
  }

  @Override
  public GameCommand create(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    return new DisplaySpaceInfoCommand(args[0]);
  }
}