package control;

/**
 * The WorldController interface defines the contract for controlling the game world.
 */
public interface WorldController {

  /**
   * Starts the game with the specified maximum number of turns.
   *
   * @param maxTurns the maximum number of turns for the game
   */
  void startGame(int maxTurns);

  /**
   * Handles the event of a player moving in the game world of GUI.
   */
  void handlePetMove();

  /**
   * Handles the event of a player picking up an item in the game world of GUI.
   */
  void handlePickUpItem();

  /**
   * Handles starting a new game with user specified file.
   */
  void handleNewGame();

  /**
   * Handles starting a new game with the current world.
   */
  void handleNewGameCurrentWorld();

  /**
   * Handles adding a player to the game.
   * @param isHuman true if the player is human, false if computer
   */
  void handleAddPlayer(boolean isHuman);

  /**
   * Handles the event of starting the game.
   */
  void handleGameStart();

  /**
   * Handles the event of a player clicking a space in the game world of GUI.
   */
  void handleSpaceClick();

  /**
   * Handles the ending of the game
   */
  void handleGameEnd();

  /**
   * Handles the change of turn in the game.
   */
  void handleTurnChange();

  /**
   * Handles the event of a player attacking in the game world of GUI.
   */
  void handleAttackCommand();

  /**
   * Handles the event of a player looking around in the game world of GUI.
   */
  void handleLookAround();

  /**
   * Handles the event of a player move the pet in the game world of GUI.
   */
  void handleMovePet();
}