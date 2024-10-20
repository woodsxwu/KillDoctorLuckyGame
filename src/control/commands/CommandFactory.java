package control.commands;

public interface CommandFactory {
  GameCommand create(String[] args);
}
