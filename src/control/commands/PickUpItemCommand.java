package control.commands;

import facade.GameFacade;

/**
 * A command to pick up an item.
 */
public class PickUpItemCommand implements GameCommand {
  private final String itemName;

  /**
   * Constructor for the PickUpItemCommand.
   * 
   * @param itemName the name of the item to pick up
   */
  public PickUpItemCommand(String itemName) {
    this.itemName = itemName;
  }

  @Override
  public String execute(GameFacade facade) {
    try {
      return facade.playerPickUpItem(itemName);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return "Failed to pick up item: " + e.getMessage();
    }
  }

  @Override
  public GameCommand create(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    return new PickUpItemCommand(args[0]);
  }
}