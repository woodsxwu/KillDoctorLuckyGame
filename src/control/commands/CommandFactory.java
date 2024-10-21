package control.commands;

/**
 * Factory for creating commands.
 */
public interface CommandFactory {
  GameCommand create(String[] args);
}
