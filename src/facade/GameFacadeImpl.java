package facade;

import java.awt.image.BufferedImage;
import java.util.List;

import model.space.Space;
import model.world.World;

/**
 * Implementation of the GameFacade interface. This class serves as a facade
 * for the game model, providing simplified methods for interacting with the game state.
 */
public class GameFacadeImpl implements GameFacade {
  private final World world;

  /**
   * Constructs a new GameFacadeImpl with the given World object.
   *
   * @param world the World object representing the game world
   */
  public GameFacadeImpl(World world) {
    this.world = world;
  }

  @Override
  public String getWorldName() {
    return world.getWorldName();
  }

  @Override
  public String getSpaceInfo(String spaceName) {
    Space space = findSpaceByName(spaceName);
    if (space == null) {
      throw new IllegalArgumentException("Space not found: " + spaceName);
    }
    return world.getSpaceInfoByIndex(space.getSpaceIndex());
  }

  @Override
  public BufferedImage createWorldMap() {
    return world.createWorldMap();
  }

  @Override
  public void addPlayer(String name, String startingSpace, boolean hasMaxItems, int maxItems) {
    Space space = findSpaceByName(startingSpace);
    if (space == null) {
      throw new IllegalArgumentException("Starting space not found: " + startingSpace);
    }
    Player player;
    if (hasMaxItems) {
      player = new HumanPlayer(name, space.getSpaceIndex(), maxItems);
    } else {
      player = new HumanPlayer(name, space.getSpaceIndex(), Integer.MAX_VALUE);
    }
    world.addPlayer(player);
  }

  @Override
  public void movePlayer(String playerName, String spaceName) {
    Player player = findPlayerByName(playerName);
    Space space = findSpaceByName(spaceName);
    if (player == null) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    if (space == null) {
      throw new IllegalArgumentException("Space not found: " + spaceName);
    }
    player.setCurrentSpaceIndex(space.getSpaceIndex());
  }

  @Override
  public void playerPickUpItem(String playerName, String itemName) {
    Player player = findPlayerByName(playerName);
    if (player == null) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Space currentSpace = world.getSpaceByIndex(player.getCurrentSpaceIndex());
    Item itemToPickUp = null;
    for (Item item : currentSpace.getItems()) {
      if (item.getItemName().equals(itemName)) {
        itemToPickUp = item;
        break;
      }
    }
    if (itemToPickUp == null) {
      throw new IllegalArgumentException("Item not found in current space: " + itemName);
    }
    if (player.addItem(itemToPickUp)) {
      currentSpace.removeItem(itemToPickUp);
    } else {
      throw new IllegalStateException("Player cannot carry more items");
    }
  }

  @Override
  public String playerLookAround(String playerName) {
    Player player = findPlayerByName(playerName);
    if (player == null) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    return player.lookAround();
  }

  @Override
  public String getPlayerInfo(String playerName) {
    Player player = findPlayerByName(playerName);
    if (player == null) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    return player.getDescription();
  }

  @Override
  public String getCurrentPlayerName() {
    return world.getCurrentPlayer().getPlayerName();
  }

  @Override
  public void nextTurn() {
    world.nextTurn();
  }

  @Override
  public boolean isGameEnded() {
    // Implement game end condition. For example:
    return world.getCurrentTurn() >= world.getMaxTurns();
  }

  private Space findSpaceByName(String spaceName) {
    for (int i = 0; i < world.getTotalSpace(); i++) {
      Space space = world.getSpaceByIndex(i);
      if (space.getSpaceName().equals(spaceName)) {
        return space;
      }
    }
    return null;
  }

  private Player findPlayerByName(String playerName) {
    List<Player> players = world.getPlayers();
    for (Player player : players) {
      if (player.getPlayerName().equals(playerName)) {
        return player;
      }
    }
    return null;
  }
}
