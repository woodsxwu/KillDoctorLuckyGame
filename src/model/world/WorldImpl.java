package model.world;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.pet.Pet;
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;

/**
 * This class represents a game world, containing multiple spaces and a target character.
 * It provides functionalities for managing spaces, characters, and generating maps.
 */
public class WorldImpl implements World {
  private final String worldName;
  private final int rows;
  private final int columns;
  private final List<Space> spaces;
  private final TargetCharacter targetCharacter;
  private final int totalSpaces;
  private final int totalItems;
  private final List<Player> players;
  private int currentPlayerIndex;
  private int currentTurn;
  private int maxTurns;
  private String winner;
  private Pet pet;

  /**
   * Constructs a new WorldImpl instance.
   *
   * @param worldName      the name of the world
   * @param rows           the number of rows in the world
   * @param columns        the number of columns in the world
   * @param spaces         the list of spaces in the world
   * @param targetCharacter the target character in the world
   * @param totalSpaces    the total number of spaces
   * @param totalItems     the total number of items
   * @throws IllegalArgumentException if worldName is null or empty, 
   *         if rows or columns are non-positive, 
   *         if spaces is null or empty,
   *         if targetCharacter is null, 
   *         or if totalSpaces is non-positive, 
   *         or if totalItems is negative
   */
  public WorldImpl(String worldName, int rows, int columns,
      List<Space> spaces, TargetCharacter targetCharacter, 
      int totalSpaces, int totalItems, Pet pet) {
    if (worldName == null || worldName.trim().isEmpty()) {
      throw new IllegalArgumentException("World name cannot be null or empty.");
    }
    if (rows <= 0) {
      throw new IllegalArgumentException("Number of rows must be positive.");
    }
    if (columns <= 0) {
      throw new IllegalArgumentException("Number of columns must be positive.");
    }
    if (spaces == null || spaces.isEmpty()) {
      throw new IllegalArgumentException("Spaces cannot be null or empty.");
    }
    if (targetCharacter == null) {
      throw new IllegalArgumentException("Target character cannot be null.");
    }
    if (totalSpaces <= 0) {
      throw new IllegalArgumentException("Total spaces must be positive.");
    }
    if (totalItems < 0) {
      throw new IllegalArgumentException("Total items cannot be negative.");
    }
    this.worldName = worldName;
    this.rows = rows;
    this.columns = columns;
    this.spaces = spaces;
    this.targetCharacter = targetCharacter;
    this.totalSpaces = totalSpaces;
    this.totalItems = totalItems;
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.currentTurn = 1;
    this.maxTurns = 0;
    this.winner = null;
    this.pet = pet;
    findNeighbors();
  }

  @Override
  public String getWorldName() {
    return worldName;
  }
  
  @Override
  public int getRows() {
    return rows;
  }
  
  @Override
  public int getColumns() {
    return columns;
  }
  
  @Override
  public int getTotalSpace() {
    return totalSpaces;
  }
  
  @Override
  public int getTotalItems() {
    return totalItems;
  }

  /**
   * Determines if two spaces are neighbors based on their coordinates.
   *
   * @param s1 the first space
   * @param s2 the second space
   * @return true if the spaces are neighbors, false otherwise
   */
  private boolean areNeighbors(Space s1, Space s2) {
    boolean adjacentRows = 
        (s1.getLowerRightRow() == s2.getUpperLeftRow() - 1 
        || s2.getLowerRightRow() == s1.getUpperLeftRow() - 1) 
        && (s1.getUpperLeftColumn() <= s2.getLowerRightColumn() 
        && s1.getLowerRightColumn() >= s2.getUpperLeftColumn());
    boolean adjacentColumns = 
        (s1.getUpperLeftColumn() == s2.getLowerRightColumn() + 1 
        || s2.getUpperLeftColumn() == s1.getLowerRightColumn() + 1) 
        && (s1.getUpperLeftRow() <= s2.getLowerRightRow() 
        && s1.getLowerRightRow() >= s2.getUpperLeftRow());

    return adjacentRows || adjacentColumns;
  }

  @Override
  public void findNeighbors() {
    List<Integer> neighborsIndices = new ArrayList<Integer>();
    for (Space space : spaces) {
      for (Space potentialNeighbor : spaces) {
        if (space != potentialNeighbor && areNeighbors(potentialNeighbor, space)) {
          neighborsIndices.add(potentialNeighbor.getSpaceIndex());
        }
      }
      space.setNeighbors(neighborsIndices);
      neighborsIndices.clear();
    }
  }

  @Override
  public BufferedImage createWorldMap() throws IOException {
    WorldPainter wp = new WorldPainter(spaces, columns, rows);
    int scale = 30;
    int padding = 100;
    return wp.createImage(scale, padding);
  }
  
  
  @Override
  public TargetCharacter getTargetCharacter() {
    return targetCharacter.copy();
  }
  
  @Override
  public void addPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    players.add(player);
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  @Override
  public Player getCurrentPlayer() {
    if (players.isEmpty()) {
      throw new IllegalStateException("No players in the game");
    }
    Player player = players.get(currentPlayerIndex);
    if (player == null) {
      throw new IllegalStateException("Current player not found");
    }
    return player;
  }

  @Override
  public void nextTurn() {
    if (players.isEmpty()) {
      throw new IllegalStateException("No players in the game");
    }
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    currentTurn++;
  }

  @Override
  public int getPlayerCount() {
    return players.size();
  }

  @Override
  public void setMaxTurns(int maxTurns) {
    if (maxTurns <= 0) {
      throw new IllegalArgumentException("Maximum turns must be positive");
    }
    this.maxTurns = maxTurns;
  }

  @Override
  public int getCurrentTurn() {
    return currentTurn;
  }

  @Override
  public int getMaxTurns() {
    return maxTurns;
  }

  @Override
  public Space getSpaceByIndex(int index) {
    if (index < 0 || index >= spaces.size()) {
      throw new IllegalArgumentException("Invalid space index: " + index);
    }
    return spaces.get(index).copy();
  }

  @Override
  public List<Space> getSpaces() {
    List<Space> copySpaces = new ArrayList<>();
    for (Space space : spaces) {
      copySpaces.add(space.copy());
    }
    return copySpaces;
  }

  @Override
  public String getWinner() {
    return winner;
  }

  @Override
  public Pet getPet() {
    return pet.copy();
  }
}