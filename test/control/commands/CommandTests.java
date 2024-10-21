package control.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import facade.GameFacade;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * CommandTests class that tests the various Command classes in the
 * control.commands package. The tests cover the following commands: -
 * AddHumanPlayerCommand - AddComputerPlayerCommand - CreateWorldMapCommand -
 * DisplayPlayerInfoCommand - DisplaySpaceInfoCommand - HelpCommand -
 * LookAroundCommand - MoveCommand - PickUpItemCommand
 */
public class CommandTests {

  private GameFacade mockFacade;

  @Before
  public void setUp() {
    mockFacade = mock(GameFacade.class);
  }

  // AddHumanPlayerCommand Tests
  @Test
  public void testAddHumanPlayerCommand() {
    AddHumanPlayerCommand command = new AddHumanPlayerCommand("Alice", "LivingRoom", 5);
    String result = command.execute(mockFacade);
    verify(mockFacade).addHumanPlayer("Alice", "LivingRoom", 5);
    assertEquals("Human player Alice added successfully", result);
  }

  @Test
  public void testAddHumanPlayerCommandFailure() {
    AddHumanPlayerCommand command = new AddHumanPlayerCommand("Bob", "Kitchen", 3);
    doThrow(new IllegalArgumentException("Invalid space")).when(mockFacade).addHumanPlayer("Bob",
        "Kitchen", 3);
    String result = command.execute(mockFacade);
    assertEquals("Failed to add human player: Invalid space", result);
  }

  @Test
  public void testAddHumanPlayerCommandCreate() {
    AddHumanPlayerCommand command = new AddHumanPlayerCommand(null, null, 0);
    GameCommand createdCommand = command.create(new String[] { "Charlie", "Bedroom", "4" });
    assertTrue(createdCommand instanceof AddHumanPlayerCommand);
    String result = createdCommand.execute(mockFacade);
    assertEquals("Human player Charlie added successfully", result);
    verify(mockFacade).addHumanPlayer("Charlie", "Bedroom", 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddHumanPlayerCommandCreateInvalidArgs() {
    AddHumanPlayerCommand command = new AddHumanPlayerCommand(null, null, 0);
    command.create(new String[] { "David", "Garden" }); // Missing capacity
  }

  // AddComputerPlayerCommand Tests
  @Test
  public void testAddComputerPlayerCommand() {
    AddComputerPlayerCommand command = new AddComputerPlayerCommand("Bot1", "Lobby", 2);
    String result = command.execute(mockFacade);
    verify(mockFacade).addComputerPlayer("Bot1", "Lobby", 2);
    assertEquals("Computer player Bot1 added successfully", result);
  }

  @Test
  public void testAddComputerPlayerCommandFailure() {
    AddComputerPlayerCommand command = new AddComputerPlayerCommand("Bot2", "InvalidRoom", 3);
    doThrow(new IllegalArgumentException("Invalid space")).when(mockFacade)
        .addComputerPlayer("Bot2", "InvalidRoom", 3);
    String result = command.execute(mockFacade);
    assertEquals("Failed to add computer player: Invalid space", result);
  }

  @Test
  public void testAddComputerPlayerCommandCreate() {
    AddComputerPlayerCommand command = new AddComputerPlayerCommand(null, null, 0);
    GameCommand createdCommand = command.create(new String[] { "Bot3", "Library", "5" });
    assertTrue(createdCommand instanceof AddComputerPlayerCommand);
    String result = createdCommand.execute(mockFacade);
    assertEquals("Computer player Bot3 added successfully", result);
    verify(mockFacade).addComputerPlayer("Bot3", "Library", 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddComputerPlayerCommandCreateInvalidArgs() {
    AddComputerPlayerCommand command = new AddComputerPlayerCommand(null, null, 0);
    command.create(new String[] { "Bot4" }); // Missing space and capacity
  }

  // CreateWorldMapCommand Tests
  @Test
  public void testCreateWorldMapCommand() throws IOException {
    CreateWorldMapCommand command = new CreateWorldMapCommand();
    when(mockFacade.createWorldMap()).thenReturn(mock(BufferedImage.class));
    String result = command.execute(mockFacade);
    verify(mockFacade).createWorldMap();
    assertEquals("World map created successfully.", result);
  }

  @Test
  public void testCreateWorldMapCommandFailure() throws IOException {
    CreateWorldMapCommand command = new CreateWorldMapCommand();
    doThrow(new IOException("File error")).when(mockFacade).createWorldMap();
    String result = command.execute(mockFacade);
    assertEquals("Failed to create world map: File error", result);
  }

  @Test
  public void testCreateWorldMapCommandCreate() {
    CreateWorldMapCommand command = new CreateWorldMapCommand();
    GameCommand createdCommand = command.create(new String[] {});
    assertTrue(createdCommand instanceof CreateWorldMapCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWorldMapCommandCreateWithArgs() {
    CreateWorldMapCommand command = new CreateWorldMapCommand();
    command.create(new String[] { "unexpected" }); // Should not accept any arguments
  }

  // DisplayPlayerInfoCommand Tests
  @Test
  public void testDisplayPlayerInfoCommand() {
    DisplayPlayerInfoCommand command = new DisplayPlayerInfoCommand("Alice");
    when(mockFacade.getPlayerInfo("Alice")).thenReturn("Alice's info");
    String result = command.execute(mockFacade);
    assertEquals("Alice's info", result);
  }

  @Test
  public void testDisplayPlayerInfoCommandNonexistentPlayer() {
    DisplayPlayerInfoCommand command = new DisplayPlayerInfoCommand("NonexistentPlayer");
    when(mockFacade.getPlayerInfo("NonexistentPlayer")).thenReturn("Player not found");
    String result = command.execute(mockFacade);
    assertEquals("Player not found", result);
  }

  @Test
  public void testDisplayPlayerInfoCommandCreate() {
    DisplayPlayerInfoCommand command = new DisplayPlayerInfoCommand(null);
    GameCommand createdCommand = command.create(new String[] { "Bob" });
    assertTrue(createdCommand instanceof DisplayPlayerInfoCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDisplayPlayerInfoCommandCreateNoArgs() {
    DisplayPlayerInfoCommand command = new DisplayPlayerInfoCommand(null);
    command.create(new String[] {}); // Missing player name
  }

  // DisplaySpaceInfoCommand Tests
  @Test
  public void testDisplaySpaceInfoCommand() {
    DisplaySpaceInfoCommand command = new DisplaySpaceInfoCommand("LivingRoom");
    when(mockFacade.getSpaceInfo("LivingRoom")).thenReturn("LivingRoom info");
    String result = command.execute(mockFacade);
    assertEquals("LivingRoom info", result);
  }

  @Test
  public void testDisplaySpaceInfoCommandNonexistentSpace() {
    DisplaySpaceInfoCommand command = new DisplaySpaceInfoCommand("NonexistentRoom");
    when(mockFacade.getSpaceInfo("NonexistentRoom"))
        .thenThrow(new IllegalArgumentException("Space not found"));
    String result = command.execute(mockFacade);
    assertEquals("Failed to get space info: Space not found", result);
  }

  @Test
  public void testDisplaySpaceInfoCommandCreate() {
    DisplaySpaceInfoCommand command = new DisplaySpaceInfoCommand(null);
    GameCommand createdCommand = command.create(new String[] { "Kitchen" });
    assertTrue(createdCommand instanceof DisplaySpaceInfoCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDisplaySpaceInfoCommandCreateNoArgs() {
    DisplaySpaceInfoCommand command = new DisplaySpaceInfoCommand(null);
    command.create(new String[] {}); // Missing space name
  }

  // HelpCommand Tests
  @Test
  public void testHelpCommandSetup() {
    HelpCommand command = new HelpCommand(true);
    String result = command.execute(mockFacade);
    assertTrue(result.contains("Setup Phase Commands:"));
    assertTrue(result.contains("add-human"));
    assertTrue(result.contains("add-computer"));
  }

  @Test
  public void testHelpCommandGameplay() {
    HelpCommand command = new HelpCommand(false);
    String result = command.execute(mockFacade);
    assertTrue(result.contains("Gameplay Commands:"));
    assertTrue(result.contains("move"));
    assertTrue(result.contains("pick"));
  }

  @Test
  public void testHelpCommandCreate() {
    HelpCommand command = new HelpCommand(true);
    GameCommand createdCommand = command.create(new String[] {});
    assertTrue(createdCommand instanceof HelpCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHelpCommandCreateWithArgs() {
    HelpCommand command = new HelpCommand(true);
    command.create(new String[] { "unexpected" }); // Should not accept any arguments
  }

  // LookAroundCommand Tests
  @Test
  public void testLookAroundCommand() {
    LookAroundCommand command = new LookAroundCommand();
    when(mockFacade.playerLookAround()).thenReturn("You see a table and a chair.");
    String result = command.execute(mockFacade);
    assertEquals("You see a table and a chair.", result);
  }

  @Test
  public void testLookAroundCommandCreate() {
    LookAroundCommand command = new LookAroundCommand();
    GameCommand createdCommand = command.create(new String[] {});
    assertTrue(createdCommand instanceof LookAroundCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLookAroundCommandCreateWithArgs() {
    LookAroundCommand command = new LookAroundCommand();
    command.create(new String[] { "unexpected" }); // Should not accept any arguments
  }

  // MoveCommand Tests
  @Test
  public void testMoveCommand() {
    MoveCommand command = new MoveCommand("Kitchen");
    when(mockFacade.getCurrentPlayerName()).thenReturn("Alice");
    when(mockFacade.movePlayer("Kitchen")).thenReturn("Alice moved to Kitchen");
    String result = command.execute(mockFacade);
    verify(mockFacade).movePlayer("Kitchen");
    assertEquals("Alice moved to Kitchen", result);
  }

  @Test
  public void testMoveCommandInvalidMove() {
    MoveCommand command = new MoveCommand("InvalidRoom");
    when(mockFacade.getCurrentPlayerName()).thenReturn("Bob");
    doThrow(new IllegalArgumentException("Invalid move")).when(mockFacade)
        .movePlayer("InvalidRoom");
    String result = command.execute(mockFacade);
    assertEquals("Invalid move", result);
  }

  @Test
  public void testMoveCommandCreate() {
    MoveCommand command = new MoveCommand(null);
    GameCommand createdCommand = command.create(new String[] { "Bedroom" });
    assertTrue(createdCommand instanceof MoveCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveCommandCreateNoArgs() {
    MoveCommand command = new MoveCommand(null);
    command.create(new String[] {}); // Missing space name
  }

  // PickUpItemCommand Tests
  @Test
  public void testPickUpItemCommand() {
    PickUpItemCommand command = new PickUpItemCommand("Book");
    when(mockFacade.getCurrentPlayerName()).thenReturn("Charlie");
    when(mockFacade.playerPickUpItem("Book")).thenReturn("Charlie picked up Book");
    String result = command.execute(mockFacade);
    verify(mockFacade).playerPickUpItem("Book");
    assertEquals("Charlie picked up Book", result);
  }

  @Test
  public void testPickUpItemCommandFailure() {
    PickUpItemCommand command = new PickUpItemCommand("HeavyObject");
    when(mockFacade.getCurrentPlayerName()).thenReturn("David");
    doThrow(new IllegalStateException("Item too heavy")).when(mockFacade)
        .playerPickUpItem("HeavyObject");
    String result = command.execute(mockFacade);
    assertEquals("Failed to pick up item: Item too heavy", result);
  }

  @Test
  public void testPickUpItemCommandCreate() {
    PickUpItemCommand command = new PickUpItemCommand(null);
    GameCommand createdCommand = command.create(new String[] { "Lamp" });
    assertTrue(createdCommand instanceof PickUpItemCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickUpItemCommandCreateNoArgs() {
    PickUpItemCommand command = new PickUpItemCommand(null);
    command.create(new String[] {}); // Missing item name
  }
}