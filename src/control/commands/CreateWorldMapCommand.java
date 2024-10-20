package control.commands;

import java.io.IOException;

import facade.GameFacade;

/**
 * A command to create the world map.
 */
public class CreateWorldMapCommand implements GameCommand {
  @Override
  public String execute(GameFacade facade) {
    try {
      facade.createWorldMap();
    } catch (IOException e) {
      return "Failed to create world map: " + e.getMessage();
    }
    return "World map created successfully.";
  }

  @Override
  public GameCommand create(String[] args) {
      if (args.length != 0) {
          throw new IllegalArgumentException("No arguments required");
      }
      return new CreateWorldMapCommand();
  }
}