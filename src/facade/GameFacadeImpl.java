package facade;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import model.item.Item;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.RandomGenerator;
import model.space.Space;
import model.target.TargetCharacter;
import model.world.World;

/**
 * Implementation of the GameFacade interface. This class serves as a facade for
 * the game model, providing simplified methods for interacting with the game
 * state.
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
    return space.getSpaceInfo(world.getSpaces(), world.getPlayers(), world.getTargetCharacter(), world.getPet());
  }

  @Override
  public BufferedImage createWorldMap() throws IOException {
    return world.createWorldMap();
  }

  @Override
  public void addHumanPlayer(String name, String startingSpace, int maxItems) {
    Player player = createPlayer(name, startingSpace, maxItems, true);
    world.addPlayer(player);
  }

  @Override
  public void addComputerPlayer(String name, String startingSpace, int maxItems) {
    Player player = createPlayer(name, startingSpace, maxItems, false);
    world.addPlayer(player);
  }

  private Player createPlayer(String name, String startingSpace, int maxItems, boolean isHuman) {
    Space space = findSpaceByName(startingSpace);
    if (isNameTaken(name)) {
      throw new IllegalArgumentException("Player name is already taken: " + name);
    }

    int effectiveMaxItems = maxItems > 0 ? maxItems : -1;

    if (isHuman) {
      return new HumanPlayer(name, space.getSpaceIndex(), effectiveMaxItems);
    } else {
      return new ComputerPlayer(name, space.getSpaceIndex(), effectiveMaxItems,
          new RandomGenerator());
    }
  }

  private boolean isNameTaken(String name) {
    return world.getPlayers().stream().anyMatch(player -> player.getPlayerName().equals(name));
  }

  @Override
  public String movePlayer(String spaceName) {
    Player player = world.getCurrentPlayer();
    Space space = findSpaceByName(spaceName);
    if (validMove(space.getSpaceIndex())) {
      player.setCurrentSpaceIndex(space.getSpaceIndex());
      moveTargetCharacter();
      petAutoMove();
      nextTurn();
      return String.format("%s moved to %s", player.getPlayerName(), spaceName);
    } else {
      throw new IllegalArgumentException("Invalid move: " + spaceName);
    }
  }

  @Override
  public String playerPickUpItem(String itemName) {
    Player player = world.getCurrentPlayer();
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
      moveTargetCharacter();
      petAutoMove();
      nextTurn();
      return String.format("%s picked up %s", player.getPlayerName(), itemName);
    } else {
      throw new IllegalStateException("Player cannot carry more items");
    }
  }

  @Override
  public String playerLookAround() {
    Player player = world.getCurrentPlayer();
    String description = player.lookAround(world.getSpaces(), world.getPlayers(), world.getTargetCharacter(), world.getPet());
    moveTargetCharacter();
    petAutoMove();
    nextTurn();
    return description;
  }

  @Override
  public String getPlayerInfo(String playerName) {
    Player player = findPlayerByName(playerName);
    if (player == null) {
      return "Player not found";
    }
    return player.getDescription(world.getSpaces());
  }

  @Override
  public void nextTurn() {
    world.nextTurn();
  }

  @Override
  public boolean isGameEnded() {
    // Implement game end condition. For example:
    return world.getWinner() != null || world.getCurrentTurn() > world.getMaxTurns();
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
    throw new IllegalArgumentException("Space not found: " + spaceName);
  }

  /**
   * Finds a player in the world by its name.
   * 
   * @param playerName the name of the player to find
   * @return the Player object if found, null otherwise
   */
  private Player findPlayerByName(String playerName) {
    for (Player player : world.getPlayers()) {
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

  @Override
  public String getCurrentPlayerName() {
    return world.getCurrentPlayer().getPlayerName();
  }

  @Override
  public void moveTargetCharacter() {
    world.getTargetCharacter().move(world.getSpaces().size());
  }
  
  @Override
  public void petAutoMove() {
    world.getPet().moveFollowingDFS(world.getSpaces());
  }

  /**
   * Gets the index of the space the current player is on.
   * 
   * @return the index of the current player's space
   */
  private int getCurrenPlayerSpaceIndex() {
    return world.getCurrentPlayer().getCurrentSpaceIndex();
  }

  /**
   * Checks if the player can move to the given space.
   * 
   * @param spaceIndex the index of the space to move to
   * @return true if the move is valid, false otherwise
   */
  private boolean validMove(int spaceIndex) {
    if (world.getSpaceByIndex(getCurrenPlayerSpaceIndex()).hasNeighbor(spaceIndex)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean computerPlayerTurn() {
    return world.getCurrentPlayer() instanceof ComputerPlayer;
  }

  @Override
  public String computerPlayerTakeTurn() {
    ComputerPlayer computerPlayer = (ComputerPlayer) world.getCurrentPlayer();
    String result = computerPlayer.takeTurn(world.getSpaces(), world.getPlayers(), world.getTargetCharacter(), world.getPet());
    murderSucceeded(result);
    return result;
  }

  @Override
  public int getPlayerCount() {
    return world.getPlayerCount();
  }

  @Override
  public String getWinner() {
    if (!isGameEnded()) {
      throw new IllegalStateException("Game is not over yet");
    }
    return world.getWinner() != null ? "Winner is " + world.getWinner() : "Target escaped! No winner.";
  }

  @Override
  public String attackTargetCharacter(String itemName) {
    Player player = world.getCurrentPlayer();
    TargetCharacter target = world.getTargetCharacter();
    String description = "";
    if (target.getCurrentSpaceIndex() != player.getCurrentSpaceIndex()) {
      description =  "Attack failed! Target is not in the same space";
      return description;
    }
    if (playerCanBeeSeen(player.getCurrentSpaceIndex())) {
      description = "Attack failed! Your attack is seen by another player.";
      return description;
    }
    description =  player.attack(itemName, target);
    murderSucceeded(description);
    return description;
  }
  
  private void murderSucceeded(String description) {
    if (world.getTargetCharacter().getHealth() == 0) {
      world.setWinner(world.getCurrentPlayer().getPlayerName());
      description += "Target character is defeated!";
    } else {
      moveTargetCharacter();
      petAutoMove();
      nextTurn();
    }
  }

  @Override
  public boolean playerCanBeeSeen(int spaceIndex) {
    if (!isSpaceVisible(spaceIndex)) {
      return false;
    }
    if (world.getSpaceByIndex(spaceIndex).playerCount(world.getPlayers()) > 1) {
      return true;
    }
    List<Integer> neighbors = world.getSpaceByIndex(spaceIndex).getNeighborIndices();
    for (int neighbor : neighbors) {
      if (world.getSpaceByIndex(neighbor).playerCount(world.getPlayers()) > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String limitedInfo() {
    return world.getCurrentPlayer().limitedInfo(world.getSpaces());
  }

  @Override
  public boolean isSpaceVisible(int spaceIndex) {
    if (world.getSpaceByIndex(spaceIndex).isSpaceVisible(world.getPet().getCurrentSpaceIndex())) {
      return true;
    }
    return false;
  }

  @Override
  public String movePet(String spaceName) {
    if (world.getCurrentPlayer().getCurrentSpaceIndex() != world.getPet().getCurrentSpaceIndex()) {
      return "Pet cannot be moved from another space";
    }
    Space space = findSpaceByName(spaceName);
    world.getPet().setSpaceIndex(space.getSpaceIndex());
    String description = String.format("%s moved pet to %s.", world.getCurrentPlayer().getPlayerName(), space.getSpaceName());
    moveTargetCharacter();
    //if pet was moved, this turn pet do not move again.
    nextTurn();
    return description;
  }

  @Override
  public String getTargetInfo() {
    return world.getTargetCharacter().getTargetDescription(world.getSpaces());
  }

}
