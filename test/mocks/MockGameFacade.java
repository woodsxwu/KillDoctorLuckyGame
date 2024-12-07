package mocks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import model.player.Player;
import facade.GameFacade;

/**
 * This class provides a mock implementation of the GameFacade interface for
 * testing purposes. It logs all method calls and allows for controlling the
 * behavior of the facade for testing.
 */
public class MockGameFacade implements GameFacade {
  private final StringBuilder log;
  private boolean isGameEnded;
  private boolean isComputerTurn;
  private int playerCount;
  private int currentTurn;
  private int maxTurns;
  private String currentPlayerName;
  private String winner;
  private Player currentPlayer;

  /**
   * Constructs a new MockGameFacade instance.
   *
   * @param log the StringBuilder to log method calls
   */
  public MockGameFacade(StringBuilder log) {
    this.log = log;
    this.isGameEnded = false;
    this.isComputerTurn = false;
    this.playerCount = 0;
    this.currentTurn = 1;
    this.maxTurns = 10;
    this.currentPlayerName = "TestPlayer";
    this.winner = null;
  }

  // Utility methods to control mock behavior
  public void setGameEnded(boolean ended) {
    this.isGameEnded = ended;
  }

  public void setComputerTurn(boolean computerTurn) {
    this.isComputerTurn = computerTurn;
  }

  public void setCurrentTurn(int turn) {
    this.currentTurn = turn;
  }

  public void setWinner(String winner) {
    this.winner = winner;
  }

  @Override
  public String getWorldName() {
    log.append("getWorldName called\n");
    return "Test World";
  }

  @Override
  public String getSpaceInfo(String spaceName) {
    log.append(String.format("getSpaceInfo called with space: %s\n", spaceName));
    return "Space Info for " + spaceName;
  }

  @Override
  public BufferedImage createWorldMap() throws IOException {
    log.append("createWorldMap called\n");
    return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
  }

  @Override
  public void addHumanPlayer(String name, String startingSpace, int maxItems) {
    playerCount++;
    log.append(String.format("addHumanPlayer called with: %s, %s, %d\n", 
        name, startingSpace, maxItems));
  }

  @Override
  public void addComputerPlayer(String name, String startingSpace, int maxItems) {
    playerCount++;
    log.append(String.format("addComputerPlayer called with: %s, %s, %d\n", 
        name, startingSpace, maxItems));
  }

  @Override
  public String movePlayer(String spaceName) {
    log.append(String.format("movePlayer called with space: %s\n", spaceName));
    return "Player moved to " + spaceName;
  }

  @Override
  public String playerPickUpItem(String itemName) {
    log.append(String.format("playerPickUpItem called with item: %s\n", itemName));
    return "Picked up " + itemName;
  }

  @Override
  public String playerLookAround() {
    log.append("playerLookAround called\n");
    return "Player looked around";
  }

  @Override
  public String getPlayerInfo(String playerName) {
    log.append(String.format("getPlayerInfo called with player: %s\n", playerName));
    return "Info for player " + playerName;
  }

  @Override
  public String getCurrentPlayerName() {
    log.append("getCurrentPlayerName called\n");
    return currentPlayerName;
  }

  @Override
  public void nextTurn() {
    currentTurn++;
    log.append("nextTurn called\n");
  }

  @Override
  public boolean isGameEnded() {
    log.append("isGameEnded called\n");
    return isGameEnded;
  }

  @Override
  public void setMaxTurns(int maxTurns) {
    this.maxTurns = maxTurns;
    log.append(String.format("setMaxTurns called with: %d\n", maxTurns));
  }

  @Override
  public int getCurrentTurn() {
    log.append("getCurrentTurn called\n");
    return currentTurn;
  }

  @Override
  public void moveTargetCharacter() {
    log.append("moveTargetCharacter called\n");
  }

  @Override
  public boolean computerPlayerTurn() {
    log.append("computerPlayerTurn called\n");
    return isComputerTurn;
  }

  @Override
  public String computerPlayerTakeTurn() {
    log.append("computerPlayerTakeTurn called\n");
    return "Computer player took turn";
  }

  @Override
  public int getPlayerCount() {
    log.append("getPlayerCount called\n");
    return playerCount;
  }

  @Override
  public String getWinner() {
    log.append("getWinner called\n");
    return winner != null ? winner : "Target escaped! No winner.";
  }

  @Override
  public String attackTargetCharacter(String itemName) {
    log.append(String.format("attackTargetCharacter called with item: %s\n", itemName));
    return "Attacked with " + itemName;
  }

  @Override
  public boolean playerCanBeeSeen(int spaceIndex) {
    log.append(String.format("playerCanBeeSeen called for space: %d\n", spaceIndex));
    return false;
  }

  @Override
  public String limitedInfo() {
    log.append("limitedInfo called\n");
    return "Limited player info";
  }

  @Override
  public boolean isSpaceVisible(int spaceIndex) {
    log.append(String.format("isSpaceVisible called for space: %d\n", spaceIndex));
    return true;
  }

  @Override
  public String movePet(String spaceName) {
    log.append(String.format("movePet called with space: %s\n", spaceName));
    return "Pet moved to " + spaceName;
  }

  @Override
  public String getTargetInfo() {
    log.append("getTargetInfo called\n");
    return "Target info";
  }

  @Override
  public void petAutoMove() {
    log.append("petAutoMove called\n");
  }

  @Override
  public int getMaxTurns() {
    log.append("getMaxTurns called\n");
    return maxTurns;
  }

  @Override
  public Player getCurrentPlayer() {
    log.append("getCurrentPlayer called\n");
    return currentPlayer;
  }
}