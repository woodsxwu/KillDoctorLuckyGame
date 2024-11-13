package control.commands;

/**
 * Factory for creating commands.
 */
public interface CommandFactory {
  /**
   * Creates a new command from the given arguments.
   * 
   * @param args the arguments to create the command from
   * @return the new command
   */
  GameCommand create(String[] args);
}
