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
}