package control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.io.StringReader;
import mocks.MockGameFacade;
import mocks.MockGameView;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the controller's interaction with the view and facade.
 */
public class Milestone4ControllerTest {
  private static final String TEST_FILE = "res/my_mansion.txt";
  private StringBuilder mockViewLog;
  private StringBuilder mockFacadeLog;
  private MockGameView mockView;
  private MockGameFacade mockFacade;
  private WorldControllerImpl controller;

  /**
   * Sets up the test fixture before each test method.
   */
  @Before
  public void setUp() {
    mockViewLog = new StringBuilder();
    mockFacadeLog = new StringBuilder();
    mockView = new MockGameView(mockViewLog);
    mockFacade = new MockGameFacade(mockFacadeLog);

    // Create the controller
    controller = new WorldControllerImpl(new StringReader(""), new StringBuilder(), mockView,
        TEST_FILE);

    // Use reflection to set the mock facade in the controller
    try {
      java.lang.reflect.Field facadeField = WorldControllerImpl.class.getDeclaredField("facade");
      facadeField.setAccessible(true);
      facadeField.set(controller, mockFacade);
    } catch (NoSuchFieldException e) {
      mockViewLog.append("Failed to set facade field");
    } catch (SecurityException e) {
      mockViewLog.append("Failed to set facade field");
    } catch (IllegalArgumentException e) {
      mockViewLog.append("Failed to set facade field");
    } catch (IllegalAccessException e) {
      mockViewLog.append("Failed to set facade field");
    }
  }

  /**
   * Helper method to initialize game state for gameplay tests.
   */
  private void initializeGameState() {
    mockFacade.setComputerTurn(false);
    mockFacade.setPlayerCount(1);

    try {
      java.lang.reflect.Field gameSetupField = WorldControllerImpl.class
          .getDeclaredField("isGameSetup");
      gameSetupField.setAccessible(true);
      gameSetupField.set(controller, true);
    } catch (NoSuchFieldException e) {
      mockViewLog.append("Failed to set game setup field");
    } catch (SecurityException e) {
      mockViewLog.append("Failed to set game setup field");
    } catch (IllegalArgumentException e) {
      mockViewLog.append("Failed to set game setup field");
    } catch (IllegalAccessException e) {
      mockViewLog.append("Failed to set game setup field");
    }
  }

  @Test
  public void testAddHumanPlayer() {
    mockFacade.setPlayerCount(0);

    controller.handleAddPlayer(true);

    String viewLog = mockViewLog.toString();
    assertTrue("Name prompt should be shown",
        viewLog.contains("showInputDialog called with: Enter player name"));
    assertTrue("Space picker should be shown", viewLog.contains("showSpacePickerDialog called"));
    assertTrue("Capacity prompt should be shown",
        viewLog.contains("showInputDialog called with: Enter item carrying capacity"));
    String facadeLog = mockFacadeLog.toString();
    assertTrue("Player should be added to facade", facadeLog.contains("addHumanPlayer called"));
    assertTrue("Player should be added to view list", viewLog.contains("addPlayerToList called"));
  }

  @Test
  public void testAddComputerPlayer() {
    mockFacade.setPlayerCount(0);

    controller.handleAddPlayer(false);
    String viewLog = mockViewLog.toString();
    String facadeLog = mockFacadeLog.toString();

    assertTrue("Computer player should be added to facade",
        facadeLog.contains("addComputerPlayer called"));
    assertTrue("Computer player should be added to view list",
        viewLog.contains("addPlayerToList called"));
  }

  @Test
  public void testGameStartValidation() {
    mockFacade.setPlayerCount(0);

    // Test start with no players
    controller.handleGameStart();
    assertTrue("Error should be shown for no players",
        mockViewLog.toString().contains("showError called with: Add at least one player"));

    // Clear logs
    mockViewLog.setLength(0);
    mockFacadeLog.setLength(0);

    // Set player count to 1 and test start
    mockFacade.setPlayerCount(1);
    controller.handleGameStart();

    String log = mockViewLog.toString();
    assertTrue("Game screen should be shown", log.contains("showGameScreen called"));
    assertTrue("Turn display should be updated", log.contains("updateTurnDisplay called"));
  }

  @Test
  public void testSpaceClickDuringPlayerTurn() {
    initializeGameState();
    mockView.setLastClickPoint(new Point(100, 100));
    controller.handleSpaceClick();

    String viewLog = mockViewLog.toString();
    assertTrue("Space click should be processed", viewLog.contains("getSpaceAtPoint called"));
    assertTrue("World should be refreshed", viewLog.contains("refreshWorld called"));
    assertTrue("Status should be updated", viewLog.contains("updateStatusDisplay called"));
  }

  @Test
  public void testSpaceClickDuringComputerTurn() {
    initializeGameState();
    mockFacade.setComputerTurn(true);
    mockView.setLastClickPoint(new Point(100, 100));
    controller.handleSpaceClick();

    String viewLog = mockViewLog.toString();
    assertFalse("Space click should not be processed", viewLog.contains("getSpaceAtPoint called"));
  }

  @Test
  public void testPickUpItemInteraction() {
    initializeGameState();
    controller.handlePickUpItem();

    String viewLog = mockViewLog.toString();
    String facadeLog = mockFacadeLog.toString();
    assertTrue("Item picker should be shown", viewLog.contains("showItemPickerDialog called"));
    assertTrue("Pickup action should be executed", facadeLog.contains("playerPickUpItem called"));
    assertTrue("Status should be updated", viewLog.contains("updateStatusDisplay called"));
  }

  @Test
  public void testAttackMechanism() {
    initializeGameState();
    controller.handleAttackCommand();

    String viewLog = mockViewLog.toString();
    String facadeLog = mockFacadeLog.toString();
    assertTrue("Attack dialog should be shown", viewLog.contains("showAttackItemDialog called"));
    assertTrue("Attack should be executed", facadeLog.contains("attackTargetCharacter called"));
    assertTrue("Status should be updated", viewLog.contains("updateStatusDisplay called"));
  }

  @Test
  public void testPetMovementSystem() {
    initializeGameState();

    // Test entering pet move mode
    controller.handleMovePet();
    assertTrue("Pet movement mode should be activated", mockViewLog.toString()
        .contains("updateStatusDisplay called with: Pet movement mode activated"));

    // Clear logs for next test
    mockViewLog.setLength(0);
    mockFacadeLog.setLength(0);

    // Test actual pet movement
    mockView.setLastClickPoint(new Point(100, 100));
    controller.handlePetMove();

    String viewLog = mockViewLog.toString();
    String facadeLog = mockFacadeLog.toString();
    assertTrue("Pet should be moved", facadeLog.contains("movePet called"));
    assertTrue("World should be refreshed", viewLog.contains("refreshWorld called"));
  }

  @Test
  public void testGameEndConditions() {
    initializeGameState();

    // Test win condition
    mockFacade.setGameEnded(true);
    mockFacade.setWinner("TestPlayer");
    controller.handleGameEnd();

    String viewLog = mockViewLog.toString();
    assertTrue("Win dialog should be shown", viewLog.contains("showGameEndDialog called"));

    // Clear logs
    mockViewLog.setLength(0);
    mockFacadeLog.setLength(0);

    // Test escape condition
    mockFacade.setGameEnded(true); // Make sure game is still ended
    mockFacade.setWinner(null);
    controller.handleGameEnd();
    viewLog = mockViewLog.toString();
    assertTrue("Escape message should be shown",
        viewLog.contains("showGameEndDialog called with: Target escaped! No winner."));
  }

  @Test
  public void testLookAroundMechanism() {
    initializeGameState();
    controller.handleLookAround();

    String viewLog = mockViewLog.toString();
    String facadeLog = mockFacadeLog.toString();
    assertTrue("Look around should be executed", facadeLog.contains("playerLookAround called"));
    assertTrue("Status should be updated", viewLog.contains("updateStatusDisplay called"));
  }

  // Test Menu Operations
  @Test
  public void testNewGameWithNewWorld() {
    // Clear logs before test to ensure clean state
    mockViewLog.setLength(0);
    mockFacadeLog.setLength(0);

    // Execute the action
    controller.handleNewGame();

    // Get the logs for analysis
    String viewLog = mockViewLog.toString();

    assertTrue("File chooser should be shown", viewLog.contains("showFileChooser called"));
  }

  @Test
  public void testNewGameWithCurrentWorld() {
    controller.handleNewGameCurrentWorld();
    String viewLog = mockViewLog.toString();

    assertTrue("Setup screen should be shown", viewLog.contains("showSetupScreen called"));
    assertFalse("File chooser should not be shown", viewLog.contains("showFileChooser called"));
  }

  // Test Player Limit
  @Test
  public void testPlayerLimitEnforcement() {
    // Add maximum number of players
    for (int i = 0; i < 10; i++) {
      mockFacade.setPlayerCount(i);
      controller.handleAddPlayer(true);
    }

    // Clear logs
    mockViewLog.setLength(0);
    mockFacadeLog.setLength(0);

    // Try to add one more player
    mockFacade.setPlayerCount(10);
    controller.handleAddPlayer(true);

    assertTrue("Error message about player limit should be shown",
        mockViewLog.toString().contains("showError called with: Cannot add more players"));
  }

  // Test Invalid Actions
  @Test
  public void testInvalidSpaceClick() {
    initializeGameState();
    mockView.setLastClickPoint(null);
    controller.handleSpaceClick();

    assertFalse("Should not process null click point",
        mockFacadeLog.toString().contains("movePlayer called"));
  }

  @Test
  public void testInvalidPetMovement() {
    initializeGameState();
    mockView.setLastClickPoint(null);
    controller.handlePetMove();

    assertFalse("Should not process invalid pet movement",
        mockFacadeLog.toString().contains("movePet called"));
  }

  // Test Player Information Display
  @Test
  public void testPlayerInformationDisplay() {
    initializeGameState();
    mockView.setLastClickPoint(new Point(100, 100));

    // Simulate clicking on a player
    controller.handleSpaceClick();
    assertTrue("Should show player information when clicked",
        mockViewLog.toString().contains("getPlayerAtPoint called"));
  }

  // Test Game State Transitions
  @Test
  public void testTransitionFromSetupToGameplay() {
    mockFacade.setPlayerCount(1);
    controller.handleGameStart();

    String viewLog = mockViewLog.toString();
    assertTrue("Should show game screen", viewLog.contains("showGameScreen called"));
    assertTrue("Should initialize turn display", viewLog.contains("updateTurnDisplay called"));
    assertTrue("Should refresh world display", viewLog.contains("refreshWorld called"));
  }

  @Test
  public void testPlayerMovement() {
    // Initialize game state
    initializeGameState();
    mockView.setLastClickPoint(new Point(100, 100));

    // Clear logs before testing
    mockViewLog.setLength(0);
    mockFacadeLog.setLength(0);

    // Simulate player movement click
    controller.handleSpaceClick();

    String viewLog = mockViewLog.toString();
    String facadeLog = mockFacadeLog.toString();

    // Verify movement sequence
    assertTrue("Should check space at click point", viewLog.contains("getSpaceAtPoint called"));
    assertTrue("Should attempt to move player",
        facadeLog.contains("movePlayer called with space: Kitchen"));
    assertTrue("Should update status display with movement result",
        viewLog.contains("updateStatusDisplay called"));
    assertTrue("Should refresh world after movement", viewLog.contains("refreshWorld called"));
  }

  @Test
  public void testPlayerInfoDisplay() {
    initializeGameState();
    mockViewLog.setLength(0);
    mockFacadeLog.setLength(0);

    // Set up click on player
    mockView.setLastClickPoint(new Point(100, 100));

    // Test clicking on a player
    controller.handleSpaceClick();
    String viewLog = mockViewLog.toString();

    // Verify player info retrieval sequence
    assertTrue("Should check for player at click point",
        viewLog.contains("getPlayerAtPoint called"));
  }
}