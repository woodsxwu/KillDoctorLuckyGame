package control.commands;

import facade.GameFacade;

public class AttackCommand implements GameCommand {

  private String itemName;
  
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
