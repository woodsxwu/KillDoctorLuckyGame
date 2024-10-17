package facade;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import model.item.Item;
import model.player.HumanPlayer;
import model.player.Player;
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
   * @throws IllegalArgumentException if the world is null
   */
  public GameFacadeImpl(World world) {
    if (world == null) {
      throw new IllegalArgumentException("World cannot be null");
    }
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
  public BufferedImage createWorldMap() throws IOException {
    return world.createWorldMap();
  }

  @Override
  public void addPlayer(String name, String startingSpace, int maxItems) {
    Space space = findSpaceByName(startingSpace);
    if (space == null) {
      throw new IllegalArgumentException("Starting space not found: " + startingSpace);
    }
    Player player;
    //TODO: name can't be duplicated
    if (maxItems > 0) {
      player = new HumanPlayer(name, space.getSpaceIndex(), maxItems);
    } else {
      player = new HumanPlayer(name, space.getSpaceIndex(), -1);
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
    return player.lookAround(world.getSpaces());
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

  /**
   * Finds a space in the world by its name.
   * 
   * @param spaceName the name of the space to find
   * @return the Space object if found, null otherwise
   */
  private Space findSpaceByName(String spaceName) {
    for (int i = 0; i < world.getTotalSpace(); i++) {
      Space space = world.getSpaceByIndex(i);
      if (space.getSpaceName().equals(spaceName)) {
        return space;
      }
    }
    return null;
  }

  /**
   * Finds a player in the world by their name.
   * 
   * @param playerName the name of the player to find
   * @return the Player object if found, null otherwise
   */
  private Player findPlayerByName(String playerName) {
    List<Player> players = world.getPlayers();
    for (Player player : players) {
      if (player.getPlayerName().equals(playerName)) {
        return player;
      }
    }
    return null;
  }
  
  @Override
  public void setMaxTurns(int maxTurns) {
    if (maxTurns <= 0) {
      throw new IllegalArgumentException("Maximum turns must be positive");
    }
    world.setMaxTurns(maxTurns);
  }

  @Override
  public int getCurrentTurn() {
    return world.getCurrentTurn();
  }
}
