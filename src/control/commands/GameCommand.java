package control.commands;

import facade.GameFacade;

/**
 * The GameCommand interface represents a command in the game.
 */
public interface GameCommand extends CommandFactory {

  /**
   * Executes the command using the provided GameFacade.
   *
   * @param facade the GameFacade to execute the command on
   * @return a String representing the result of the command execution
   */
  String execute(GameFacade facade);
}
