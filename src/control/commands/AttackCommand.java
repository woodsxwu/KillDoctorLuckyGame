package control.commands;

import facade.GameFacade;

/**
 * Command to attack a target character with a specific item.
 */
public class AttackCommand implements GameCommand {

  private String itemName;
  
  /**
   * Constructor for the AttackCommand.
   * 
   * @param itemName the name of the item to attack with
   */
  public AttackCommand(String itemName) {
    this.itemName = itemName;
  }
  
  @Override
  public GameCommand create(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Wrong number of arguments");
    }
    return new AttackCommand(args[0]);
  }

  @Override
  public String execute(GameFacade facade) {
    try {
      return facade.attackTargetCharacter(itemName);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return "Failed to attack target character: " + e.getMessage();
    }
  }

}
