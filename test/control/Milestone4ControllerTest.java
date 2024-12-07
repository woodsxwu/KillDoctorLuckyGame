package control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.StringReader;
import mocks.MockGameFacade;
import mocks.MockGameView;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for WorldControllerImpl that verifies Milestone 4 functionality
 * through listener interactions.
 */
public class Milestone4ControllerTest {
  private StringBuilder log;
  private MockGameView view;
  private MockGameFacade facade;
  private WorldController controller;

  /**
   * Sets up the test environment before each test.
   */
  @Before
  public void setUp() {
    log = new StringBuilder();
    view = new MockGameView(log);
    facade = new MockGameFacade(log);
    controller = new WorldControllerImpl(new StringReader(""), log, view, "res/mansion.txt");

    // Initialize game which sets up listeners
    controller.startGame(10);

    // Simulate button press for NewGameCurrentWorld to initialize listeners
    view.getButtonListener().actionPerformed(new ActionEvent(view.getDummyComponent(),
        ActionEvent.ACTION_PERFORMED, "NewGameCurrentWorld"));

    // Verify listeners were added
    assertTrue("No keyboard listener registered", view.getKeyListener() != null);
    assertTrue("No mouse listener registered", view.getMouseListener() != null);
    assertTrue("No action listener registered", view.getButtonListener() != null);

    // Clear log before each test
    log.setLength(0);
  }

  @Test
  public void testPetMovementUsingKeyAndMouse() {
    // Setup game state
    facade.setComputerTurn(false);

    // Simulate pressing 'M' key
    KeyEvent mKeyPress = new KeyEvent(view.getDummyComponent(), KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_M, 'M');
    view.getKeyListener().keyPressed(mKeyPress);

    // Verify the expected log entries for pet movement
    String logStr = log.toString();
    assertTrue("Pet movement mode not activated", logStr.contains("Pet movement mode activated"));

    // Simulate mouse click on space (100, 100)
    Point clickPoint = new Point(100, 100);
    view.setLastClickPoint(clickPoint);
    view.getMouseListener().mouseClicked(null);

    // Verify that space checking, pet movement, and world refresh happen
    logStr = log.toString();
    assertTrue("Space not checked", logStr.contains("getSpaceAtPoint called"));
    assertTrue("Pet not moved", logStr.contains("movePet called with space: Kitchen"));
    assertTrue("World not refreshed", logStr.contains("refreshWorld called"));
  }

  @Test
  public void testItemPickupUsingKeyboard() {
    // Setup game state
    facade.setComputerTurn(false);

    // Simulate pressing 'P' key
    KeyEvent pKeyPress = new KeyEvent(view.getDummyComponent(), KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_P, 'P');
    view.getKeyListener().keyPressed(pKeyPress);

    // Verify the expected method calls for item pickup
    String logStr = log.toString();
    assertTrue("Item picker not shown", logStr.contains("showItemPickerDialog called"));
    assertTrue("Item not picked up", logStr.contains("playerPickUpItem called with item: Knife"));
    assertTrue("Status not updated", logStr.contains("updateStatusDisplay called"));
  }

  @Test
  public void testPlayerMovementUsingMouse() {
    // Setup game state
    facade.setComputerTurn(false);

    // Simulate mouse click on space (100, 100)
    Point clickPoint = new Point(100, 100);
    view.setLastClickPoint(clickPoint);
    view.getMouseListener().mouseClicked(null);

    // Verify the expected method calls for player movement
    String logStr = log.toString();
    assertTrue("Space not checked", logStr.contains("getSpaceAtPoint called"));
    assertTrue("Player not moved", logStr.contains("movePlayer called with space: Kitchen"));
    assertTrue("World not refreshed", logStr.contains("refreshWorld called"));
  }

  @Test
  public void testAttackUsingKeyboard() {
    // Setup game state
    facade.setComputerTurn(false);

    // Simulate pressing 'A' key
    KeyEvent aKeyPress = new KeyEvent(view.getDummyComponent(), KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
    view.getKeyListener().keyPressed(aKeyPress);

    // Verify the expected method calls for attack
    String logStr = log.toString();
    assertTrue("Attack dialog not shown", logStr.contains("showAttackItemDialog called"));
    assertTrue("Attack not executed",logStr.contains("attackTargetCharacter called with item: Knife"));
    assertTrue("Status not updated", logStr.contains("updateStatusDisplay called"));
  }

  @Test
  public void testLookAroundUsingKeyboard() {
    // Setup game state
    facade.setComputerTurn(false);

    // Simulate pressing 'L' key
    KeyEvent lKeyPress = new KeyEvent(view.getDummyComponent(), KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_L, 'L');
    view.getKeyListener().keyPressed(lKeyPress);

    // Verify the expected method calls for look around
    String logStr = log.toString();
    assertTrue("Look around not executed", logStr.contains("playerLookAround called"));
    assertTrue("Status not updated", logStr.contains("updateStatusDisplay called"));
  }

  @Test
  public void testGameSetupAddPlayers() {
    // Simulate button press to add a human player
    view.getButtonListener().actionPerformed(new ActionEvent(view.getDummyComponent(),
        ActionEvent.ACTION_PERFORMED, "Add Human Player"));

    // Verify that player setup actions are invoked
    String logStr = log.toString();
    assertTrue("Name dialog not shown", logStr.contains("showInputDialog called"));
    assertTrue("Space picker not shown", logStr.contains("showSpacePickerDialog called"));
    assertTrue("Player not added", logStr.contains("addHumanPlayer called"));
    assertTrue("Player list not updated", logStr.contains("addPlayerToList called"));
  }

  @Test
  public void testGameStartWithPlayers() {
    // Add a human player
    view.getButtonListener().actionPerformed(new ActionEvent(view.getDummyComponent(),
        ActionEvent.ACTION_PERFORMED, "Add Human Player"));
    log.setLength(0); // Clear log after setup

    // Simulate starting the game
    view.getButtonListener().actionPerformed(
        new ActionEvent(view.getDummyComponent(), ActionEvent.ACTION_PERFORMED, "Start Game"));

    // Verify game start actions
    String logStr = log.toString();
    assertTrue("Player name not fetched", logStr.contains("getCurrentPlayerName called"));
    assertTrue("Turn display not updated", logStr.contains("updateTurnDisplay called"));
    assertTrue("Game screen not shown", logStr.contains("showGameScreen called"));
  }

  @Test
  public void testComputerPlayerTurnAutomaticExecution() {
    // Setup game state
    facade.setComputerTurn(true);
    facade.setGameEnded(false);

    // Simulate starting the game
    view.getButtonListener().actionPerformed(
        new ActionEvent(view.getDummyComponent(), ActionEvent.ACTION_PERFORMED, "Start Game"));

    // Verify computer player's actions
    String logStr = log.toString();
    assertTrue("Computer turn not checked", logStr.contains("computerPlayerTurn called"));
    assertTrue("Computer turn not executed", logStr.contains("computerPlayerTakeTurn called"));
    assertTrue("World not refreshed", logStr.contains("refreshWorld called"));
  }

  @Test
  public void testGameEndWhenTargetDefeated() {
    // Setup game state
    facade.setGameEnded(true);
    facade.setWinner("TestPlayer");

    // Simulate an action that would check game end
    KeyEvent aKeyPress = new KeyEvent(view.getDummyComponent(), KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
    view.getKeyListener().keyPressed(aKeyPress);

    // Verify the expected method calls when game ends
    String logStr = log.toString();
    assertTrue("Winner not checked", logStr.contains("getWinner called"));
    assertTrue("Game end dialog not shown", logStr.contains("showGameEndDialog called"));
    assertTrue("Welcome screen not shown", logStr.contains("showWelcomeScreen called"));
  }

  @Test
  public void testNoActionsOnComputerTurn() {
    // Setup game state for computer turn
    facade.setComputerTurn(true);

    // Simulate pressing 'P' key during computer's turn
    KeyEvent pKeyPress = new KeyEvent(view.getDummyComponent(), KeyEvent.KEY_PRESSED,
        System.currentTimeMillis(), 0, KeyEvent.VK_P, 'P');
    view.getKeyListener().keyPressed(pKeyPress);

    // Verify no player actions are executed
    String logStr = log.toString();
    assertFalse("Item pickup shouldn't be allowed", logStr.contains("showItemPickerDialog called"));
  }
}
