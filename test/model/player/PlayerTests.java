package model.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.item.ItemImpl;
import model.space.Space;
import model.space.SpaceImpl;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for the Player interface and its implementations.
 */
public class PlayerTests {
  private HumanPlayer humanPlayer;
  private ComputerPlayer computerPlayer;
  private List<Space> spaces;
  private Item item1;
  private Item item2;
  private Space space1;
  private Space space2;

  /**
   * RandomGenerator implementation that returns a sequence of numbers.
   */
  @Before
  public void setUp() {
    humanPlayer = new HumanPlayer("Alice", 0, 5);
    computerPlayer = new ComputerPlayer("Bot", 1, 3, new RandomGenerator(0, 1, 2));
    spaces = new ArrayList<>();
    space1 = new SpaceImpl(0, "Living Room", 0, 0, 1, 1, new ArrayList<>(), new ArrayList<>());
    space2 = new SpaceImpl(1, "Kitchen", 1, 1, 2, 2, new ArrayList<>(), new ArrayList<>());
    spaces.add(space1);
    spaces.add(space2);
    item1 = new ItemImpl("Book", 1, 0);
    item2 = new ItemImpl("Knife", 2, 1);

    space1.setNeighbors(List.of(1));
    space2.setNeighbors(List.of(0));
    space1.addItem(item1);
    space2.addItem(item2);
  }

  // AbstractPlayer Tests

  @Test
  public void testValidPlayerCreation() {
    assertEquals("Alice", humanPlayer.getPlayerName());
    assertEquals(0, humanPlayer.getCurrentSpaceIndex());
    assertEquals("Bot", computerPlayer.getPlayerName());
    assertEquals(1, computerPlayer.getCurrentSpaceIndex());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPlayerNameNull() {
    new HumanPlayer(null, 0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPlayerNameEmpty() {
    new HumanPlayer("", 0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartingSpaceIndex() {
    new HumanPlayer("Charlie", -1, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMaxItems() {
    new HumanPlayer("David", 0, -2);
  }

  @Test
  public void testSetCurrentSpaceIndex() {
    humanPlayer.setCurrentSpaceIndex(1);
    assertEquals(1, humanPlayer.getCurrentSpaceIndex());
  }

  @Test
  public void testAddItemWithinLimit() {
    assertTrue(humanPlayer.addItem(item1));
    assertEquals(1, humanPlayer.getItems().size());
  }

  @Test
  public void testAddItemBeyondLimit() {
    for (int i = 0; i < 5; i++) {
      assertTrue(humanPlayer.addItem(item1));
    }
    assertFalse(humanPlayer.addItem(item2));
    assertEquals(5, humanPlayer.getItems().size());
  }

  @Test
  public void testAddItemNoLimit() {
    HumanPlayer unlimitedPlayer = new HumanPlayer("Eve", 0, -1);
    for (int i = 0; i < 100; i++) {
      assertTrue(unlimitedPlayer.addItem(item1));
    }
    assertEquals(100, unlimitedPlayer.getItems().size());
  }

  @Test
  public void testLookAround() {
    String result = humanPlayer.lookAround(spaces);
    assertTrue(result.contains("Alice is currently in: Living Room"));
    assertTrue(result.contains("Neighbors: \n" + " - Kitchen"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLookAroundNullSpaces() {
    humanPlayer.lookAround(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testLookAroundEmptySpaces() {
    humanPlayer.lookAround(new ArrayList<>());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testLookAroundInvalidSpaceIndex() {
    HumanPlayer invalidPlayer = new HumanPlayer("Frank", 5, 5);
    invalidPlayer.lookAround(spaces);
  }

  @Test
  public void testGetDescription() {
    humanPlayer.addItem(item1);
    String description = humanPlayer.getDescription(spaces);
    assertTrue(description.contains("Name: Alice"));
    assertTrue(description.contains("Current Space: Living Room"));
    assertTrue(description.contains("Items: Book"));
  }

  @Test
  public void testGetDescriptionNoItems() {
    String description = humanPlayer.getDescription(spaces);
    assertTrue(description.contains("Items: None"));
  }

  @Test
  public void testGetDescriptionEmptyWorld() {
    String description = humanPlayer.getDescription(new ArrayList<>());
    assertTrue(description.contains("Current Space: Unknown (The world is empty)"));
  }

  @Test
  public void testGetDescriptionPlayerLost() {
    HumanPlayer lostPlayer = new HumanPlayer("Grace", 5, 5);
    String description = lostPlayer.getDescription(spaces);
    assertTrue(description.contains("Current Space: Unknown (Player is lost)"));
  }

  @Test
  public void testMove() {
    humanPlayer.move(1);
    assertEquals(1, humanPlayer.getCurrentSpaceIndex());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToInvalidSpace() {
    humanPlayer.move(-1);
  }

  // HumanPlayer Specific Tests

  @Test
  public void testHumanPlayerTakeTurn() {
    // HumanPlayer's takeTurn method is empty, so we just ensure it doesn't throw an
    // exception
    humanPlayer.takeTurn(spaces);
  }

  // ComputerPlayer Specific Tests

  @Test
  public void testComputerPlayerTakeTurn() {
    computerPlayer.takeTurn(spaces);
    // The RandomGenerator is set to return 0, 1, 2 in sequence
    // 0 corresponds to move action
    assertEquals(0, computerPlayer.getCurrentSpaceIndex());
  }

  @Test
  public void testComputerPlayerLookAround() {
    ComputerPlayer lookingBot = new ComputerPlayer("LookBot", 0, 3, new RandomGenerator(1));
    lookingBot.takeTurn(spaces);
    // No state change, just ensuring no exception is thrown
  }

  @Test
  public void testComputerPlayerPickUpItem() {
    ComputerPlayer pickingBot = new ComputerPlayer("PickBot", 0, 3, new RandomGenerator(2));
    pickingBot.takeTurn(spaces);
    assertEquals(1, pickingBot.getItems().size());
    assertEquals("Book", pickingBot.getItems().get(0).getItemName());
  }

  @Test
  public void testComputerPlayerPickUpItemWhenFull() {
    ComputerPlayer fullBot = new ComputerPlayer("FullBot", 0, 1, new RandomGenerator(2));
    fullBot.addItem(item2);
    fullBot.takeTurn(spaces);
    assertEquals(1, fullBot.getItems().size());
    assertEquals("Knife", fullBot.getItems().get(0).getItemName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComputerPlayerTakeTurnInvalidSpaces() {
    computerPlayer.takeTurn(null);
  }

  @Test
  public void testComputerPlayerMoveToEmptyNeighbor() {
    Space isolatedSpace = new SpaceImpl(2, "Isolated Room", 2, 2, 3, 3, new ArrayList<>(),
        new ArrayList<>());
    spaces.add(isolatedSpace);
    ComputerPlayer isolatedBot = new ComputerPlayer("IsolatedBot", 2, 3, new RandomGenerator(0));
    isolatedBot.takeTurn(spaces);
    // Bot should not move as there are no neighbors
    assertEquals(2, isolatedBot.getCurrentSpaceIndex());
  }
}