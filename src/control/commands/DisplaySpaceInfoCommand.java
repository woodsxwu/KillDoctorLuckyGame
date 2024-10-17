package control.commands;

import facade.GameFacade;

/**
 * Command to display information about a space.
 */
public class DisplaySpaceInfoCommand implements GameCommand {
  private final String spaceName;

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
}