package control.commands;

import facade.GameFacade;

/**
 * Command to display help information.
 */
public class HelpCommand implements GameCommand {
  private final boolean isSetup;

  /**
   * Constructor for the HelpCommand.
   * 
   * @param isSetup true if the game is in the setup phase, false otherwise
   */
  public HelpCommand(boolean isSetup) {
    this.isSetup = isSetup;
  }

  @Override
  public String execute(GameFacade facade) {
    if (isSetup) {
      return getSetupHelp();
    } else {
      return getGameplayHelp();
    }
  }

  private String getSetupHelp() {
    StringBuilder help = new StringBuilder("Setup Phase Commands:\n\n");
    help.append("  add-human <player-name> <starting-space> <item-carrying-capacity>\n");
    help.append("    Adds a human player to the game. \n");
    help.append("    (If no carrying capacity limitation, please use -1.)\n");
    help.append("    Example: add-human Alice \"Living Room\" 5\n\n");
    help.append("  add-computer <player-name> <starting-space> <item-carrying-capacity>\n");
    help.append("    Adds a computer-controlled player to the game.\n");
    help.append("    Example: add-computer Bob Kitchen 3\n\n");
    help.append("  map\n");
    help.append("    Creates the world map.\n\n");
    help.append("  help\n");
    help.append("    Displays this help message.\n\n");
    help.append("  start\n");
    help.append("    Start the game (only available after adding players)\n");
    return help.toString();
  }

  private String getGameplayHelp() {
    StringBuilder help = new StringBuilder("Gameplay Commands:\n");
    help.append("  move <space-name>\n");
    help.append("    Moves the current player to the specified space.\n");
    help.append("    Example: move DiningRoom\n\n");
    help.append("  pick <item-name>\n");
    help.append("    Attempts to pick up the specified item in the current space.\n");
    help.append("    Example: pick Knife\n\n");
    help.append("  look\n");
    help.append("    Displays information about the current space and neighboring spaces.\n\n");
    help.append("  attack <item-name>\n");
    help.append("    Attempt to attack the target character with the chosen item.\n\n");
    help.append("  move-pet <space-name>\n");
    help.append("    Move the pet to to a specified space\n\n");
    help.append("  space <space-name>\n");
    help.append("    Displays detailed information about the specified space.\n");
    help.append("    Example: space Kitchen\n\n");
    help.append("  player-info <player-name>\n");
    help.append("    Displays information about the specified player.\n");
    help.append("    Example: player-info Alice\n\n");
    help.append("  help\n");
    help.append("    Displays this help message.\n\n");
    help.append("  quit\n");
    help.append("    Ends the game.\n");
    return help.toString();
  }

  @Override
  public GameCommand create(String[] args) {
    if (args.length != 0) {
      throw new IllegalArgumentException("No arguments required");
    }
    return new HelpCommand(this.isSetup);
  }
}
