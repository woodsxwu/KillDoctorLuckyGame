package control;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import facade.GameFacade;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for WorldControllerImpl.
 */
public class WorldControllerImplTest {

  private GameFacade mockFacade;

  private StringWriter output;
  private WorldControllerImpl controller;

  @Before
  public void setUp() {
    mockFacade = mock(GameFacade.class);
    output = new StringWriter();
  }

  private void createController(String input) {
    controller = new WorldControllerImpl(mockFacade, new StringReader(input), output);
  }

  @Test
  public void testStartGameNormalFlow() throws IOException {
    createController("add-human Alice Kitchen 5\nstart\nmove \"Living Room\"\nquit\n");
    when(mockFacade.getPlayerCount()).thenReturn(1);
    when(mockFacade.getCurrentPlayerName()).thenReturn("Alice");
    when(mockFacade.isGameEnded()).thenReturn(false, true);
    when(mockFacade.movePlayer("Living Room")).thenReturn("Alice moved to Living Room");

    controller.startGame(10);

    verify(mockFacade).addHumanPlayer("Alice", "Kitchen", 5);
    verify(mockFacade).movePlayer("Living Room");

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Human player Alice added successfully"));
    assertTrue(outputStr.contains("Game setup complete. Starting the game..."));
    assertTrue(outputStr.contains("Alice moved to Living Room"));
    assertTrue(outputStr.contains("Game over!"));
  }

  @Test
  public void testStartGameNoPlayers() throws IOException {
    createController("start\nadd-human Bob Bedroom 3\nstart\nquit\n");
    when(mockFacade.getPlayerCount()).thenReturn(0, 1);
    when(mockFacade.isGameEnded()).thenReturn(false, true);

    controller.startGame(10);

    verify(mockFacade).addHumanPlayer("Bob", "Bedroom", 3);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Please add at least one player before starting the game."));
    assertTrue(outputStr.contains("Human player Bob added successfully"));
    assertTrue(outputStr.contains("Game setup complete. Starting the game..."));
  }

  @Test
  public void testInvalidCommand() throws IOException {
    createController("invalid-command\nquit\n");
    controller.startGame(10);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Unknown command. Type 'help' for available commands."));
  }

  @Test
  public void testAddPlayerInvalidArguments() throws IOException {
    createController("add-human Alice\nquit\n");
    controller.startGame(10);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Error: Wrong number of arguments"));
  }

  @Test
  public void testMoveToInvalidSpace() throws IOException {
    createController("add-human Alice Kitchen 5\nstart\nmove InvalidRoom\nquit\n");
    when(mockFacade.getPlayerCount()).thenReturn(1);
    when(mockFacade.getCurrentPlayerName()).thenReturn("Alice");
    when(mockFacade.isGameEnded()).thenReturn(false, false, true);
    doThrow(new IllegalArgumentException("Invalid move: InvalidRoom")).when(mockFacade)
        .movePlayer("InvalidRoom");

    controller.startGame(10);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Invalid move: InvalidRoom"));
  }

  @Test
  public void testPickUpNonexistentItem() throws IOException {
    createController("add-human Alice Kitchen 5\nstart\npick NonexistentItem\nquit\n");
    when(mockFacade.getPlayerCount()).thenReturn(1);
    when(mockFacade.getCurrentPlayerName()).thenReturn("Alice");
    when(mockFacade.isGameEnded()).thenReturn(false, false, true);
    doThrow(new IllegalArgumentException("Item not found: NonexistentItem")).when(mockFacade)
        .playerPickUpItem("NonexistentItem");

    controller.startGame(10);

    String outputStr = output.toString();

    assertTrue(outputStr.contains("Failed to pick up item: Item not found: NonexistentItem"));
  }

  @Test
  public void testComputerPlayerTurn() throws IOException {
    createController("add-computer Bot Lounge 3\nstart\nquit\n");
    when(mockFacade.getPlayerCount()).thenReturn(1);
    when(mockFacade.getCurrentPlayerName()).thenReturn("Bot");
    when(mockFacade.isGameEnded()).thenReturn(false, true);
    when(mockFacade.computerPlayerTurn()).thenReturn(true);

    controller.startGame(10);

    verify(mockFacade).computerPlayerTakeTurn();

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Computer player turn"));
  }

  @Test
  public void testGameEndCondition() throws IOException {
    createController("add-human Alice Kitchen 5\nstart\nmove LivingRoom\n");
    when(mockFacade.getPlayerCount()).thenReturn(1);
    when(mockFacade.getCurrentPlayerName()).thenReturn("Alice");
    when(mockFacade.isGameEnded()).thenReturn(false, true);

    controller.startGame(1);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Game over!"));
  }

  @Test
  public void testHelpCommand() throws IOException {
    createController("help\nquit\n");
    controller.startGame(10);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Setup Phase Commands:"));
    assertTrue(
        outputStr.contains("add-human <player-name> <starting-space> <item-carrying-capacity>"));
    assertTrue(
        outputStr.contains("add-computer <player-name> <starting-space> <item-carrying-capacity>"));
  }

  @Test
  public void testCreateWorldMap() throws IOException {
    createController("map\nquit\n");
    when(mockFacade.createWorldMap()).thenReturn(null);

    controller.startGame(10);

    verify(mockFacade).createWorldMap();

    String outputStr = output.toString();
    assertTrue(outputStr.contains("World map created successfully."));
  }

  @Test
  public void testLookAroundCommand() throws IOException {
    createController("add-human Alice Kitchen 5\nstart\nlook\nquit\n");
    when(mockFacade.getPlayerCount()).thenReturn(1);
    when(mockFacade.getCurrentPlayerName()).thenReturn("Alice");
    when(mockFacade.isGameEnded()).thenReturn(false, false, true);
    when(mockFacade.playerLookAround()).thenReturn("You are in the Kitchen. You see a Knife.");

    controller.startGame(10);

    verify(mockFacade).playerLookAround();

    String outputStr = output.toString();
    assertTrue(outputStr.contains("You are in the Kitchen. You see a Knife."));
  }

  @Test
  public void testDisplayPlayerInfo() throws IOException {
    createController("add-human Alice Kitchen 5\nstart\nplayer-info Alice\nquit\n");
    when(mockFacade.getPlayerCount()).thenReturn(1);
    when(mockFacade.getCurrentPlayerName()).thenReturn("Alice");
    when(mockFacade.isGameEnded()).thenReturn(false, false, true);
    when(mockFacade.getPlayerInfo("Alice")).thenReturn("Alice is in the Kitchen. She has a Knife.");

    controller.startGame(10);

    verify(mockFacade).getPlayerInfo("Alice");

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Alice is in the Kitchen. She has a Knife."));
  }
}