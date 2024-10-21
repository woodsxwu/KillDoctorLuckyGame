package model.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.item.ItemImpl;
import model.player.HumanPlayer;
import model.player.Player;
import model.space.Space;
import model.space.SpaceImpl;
import model.target.TargetCharacter;
import model.target.TargetCharacterImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for WorldImpl.
 */
public class WorldImplTest {

  private WorldImpl world;
  private List<Space> spaces;
  private TargetCharacter targetCharacter;

  /**
   * Set up the test environment.
   */
  @Before
  public void setUp() {
    spaces = new ArrayList<>();
    spaces.add(new SpaceImpl(0, "Living Room", 0, 0, 2, 2, new ArrayList<>(), new ArrayList<>()));
    spaces.add(new SpaceImpl(1, "Kitchen", 3, 0, 5, 2, new ArrayList<>(), new ArrayList<>()));
    spaces.add(new SpaceImpl(2, "Bedroom", 0, 3, 2, 5, new ArrayList<>(), new ArrayList<>()));

    targetCharacter = new TargetCharacterImpl("Dr. Lucky", 50);

    world = new WorldImpl("Lucky Mansion", 6, 6, spaces, targetCharacter, 3, 0);
  }

  @Test
  public void testValidWorldInitialization() {
    assertEquals("Lucky Mansion", world.getWorldName());
    assertEquals(6, world.getRows());
    assertEquals(6, world.getColumns());
    assertEquals(3, world.getTotalSpace());
    assertEquals(0, world.getTotalItems());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidWorldName() {
    new WorldImpl("", 6, 6, spaces, targetCharacter, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRows() {
    new WorldImpl("Test World", 0, 6, spaces, targetCharacter, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColumns() {
    new WorldImpl("Test World", 6, -1, spaces, targetCharacter, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSpaces() {
    new WorldImpl("Test World", 6, 6, null, targetCharacter, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptySpaces() {
    new WorldImpl("Test World", 6, 6, new ArrayList<>(), targetCharacter, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullTargetCharacter() {
    new WorldImpl("Test World", 6, 6, spaces, null, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTotalSpaces() {
    new WorldImpl("Test World", 6, 6, spaces, targetCharacter, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeTotalItems() {
    new WorldImpl("Test World", 6, 6, spaces, targetCharacter, 3, -1);
  }

  @Test
  public void testFindNeighbors() {
    world.findNeighbors();

    Space livingRoom = spaces.get(0);
    Space kitchen = spaces.get(1);

    assertTrue(livingRoom.getNeighborIndices().contains(1));
    assertTrue(livingRoom.getNeighborIndices().contains(2));
    assertTrue(kitchen.getNeighborIndices().contains(0));
    Space bedroom = spaces.get(2);
    assertTrue(bedroom.getNeighborIndices().contains(0));
  }

  @Test
  public void testCreateWorldMap() throws IOException {
    BufferedImage result = world.createWorldMap();
    assertNotNull("Created world map should not be null", result);
    assertTrue("Image width should be positive", result.getWidth() > 0);
    assertTrue("Image height should be positive", result.getHeight() > 0);
  }

  @Test
  public void testGetTargetCharacter() {
    TargetCharacter result = world.getTargetCharacter();
    assertEquals("Dr. Lucky", result.getTargetName());
    assertEquals(50, result.getHealth());
    assertEquals(0, result.getCurrentSpaceIndex());
  }

  @Test
  public void testAddPlayer() {
    Player player1 = new HumanPlayer("Alice", 0, 5);
    world.addPlayer(player1);
    assertEquals(1, world.getPlayerCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNullPlayer() {
    world.addPlayer(null);
  }

  @Test
  public void testGetPlayers() {
    Player player1 = new HumanPlayer("Alice", 0, 5);
    Player player2 = new HumanPlayer("Bob", 1, 5);
    world.addPlayer(player1);
    world.addPlayer(player2);
    List<Player> players = world.getPlayers();
    assertEquals(2, players.size());
    assertTrue(players.contains(player1));
    assertTrue(players.contains(player2));
  }

  @Test
  public void testGetCurrentPlayer() {
    Player player1 = new HumanPlayer("Alice", 0, 5);
    Player player2 = new HumanPlayer("Bob", 1, 5);
    world.addPlayer(player1);
    world.addPlayer(player2);
    assertEquals(player1, world.getCurrentPlayer());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrentPlayerNoPlayers() {
    world.getCurrentPlayer();
  }

  @Test
  public void testNextTurn() {
    Player player1 = new HumanPlayer("Alice", 0, 5);
    Player player2 = new HumanPlayer("Bob", 1, 5);
    world.addPlayer(player1);
    world.addPlayer(player2);
    world.setMaxTurns(10);

    assertEquals(player1, world.getCurrentPlayer());
    world.nextTurn();
    assertEquals(player2, world.getCurrentPlayer());
    world.nextTurn();
    assertEquals(player1, world.getCurrentPlayer());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextTurnNoPlayers() {
    world.nextTurn();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextTurnExceedMaxTurns() {
    Player player1 = new HumanPlayer("Alice", 0, 5);
    world.addPlayer(player1);
    world.setMaxTurns(1);
    world.nextTurn();
    world.nextTurn(); // This should throw an exception
  }

  @Test
  public void testSetMaxTurns() {
    world.setMaxTurns(20);
    assertEquals(20, world.getMaxTurns());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidMaxTurns() {
    world.setMaxTurns(0);
  }

  @Test
  public void testGetCurrentTurn() {
    Player player1 = new HumanPlayer("Alice", 0, 5);
    world.addPlayer(player1);
    world.setMaxTurns(10);
    assertEquals(0, world.getCurrentTurn());
    world.nextTurn();
    assertEquals(1, world.getCurrentTurn());
  }

  @Test
  public void testGetSpaceByIndex() {
    assertEquals("Living Room", world.getSpaceByIndex(0).getSpaceName());
    assertEquals("Kitchen", world.getSpaceByIndex(1).getSpaceName());
    assertEquals("Bedroom", world.getSpaceByIndex(2).getSpaceName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSpaceByInvalidIndex() {
    world.getSpaceByIndex(3);
  }

  @Test
  public void testGetSpaces() {
    List<Space> retrievedSpaces = world.getSpaces();
    assertEquals(3, retrievedSpaces.size());
    assertEquals("Living Room", retrievedSpaces.get(0).getSpaceName());
    assertEquals("Kitchen", retrievedSpaces.get(1).getSpaceName());
    assertEquals("Bedroom", retrievedSpaces.get(2).getSpaceName());
  }

  @Test
  public void testMoveTargetCharacter() {
    TargetCharacter target = world.getTargetCharacter();
    int initialPosition = target.getCurrentSpaceIndex();
    target.move(world.getTotalSpace());
    assertNotEquals(initialPosition, target.getCurrentSpaceIndex());
  }

  @Test
  public void testAddItemToSpace() {
    Item item = new ItemImpl("Knife", 3, 0);
    spaces.get(0).addItem(item);
    assertEquals(1, spaces.get(0).getItems().size());
    assertEquals("Knife", spaces.get(0).getItems().get(0).getItemName());
  }
}