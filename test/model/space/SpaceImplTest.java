package model.space;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.item.ItemImpl;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SpaceImpl.
 */
public class SpaceImplTest {

  private SpaceImpl space;
  private List<Item> items;
  private List<Integer> neighbors;

  /**
   * Sets up the test fixture.
   */
  @Before
  public void setUp() {
    items = new ArrayList<>();
    Item item = new ItemImpl("Guitar", 3, 0);
    items.add(item);
    neighbors = new ArrayList<>();
    neighbors.add(1);
    space = new SpaceImpl(0, "Living Room", 0, 0, 2, 2, items, neighbors);
  }

  @Test
  public void testValidInput() {
    assertEquals(0, space.getSpaceIndex());
    assertEquals("Living Room", space.getSpaceName());
    assertEquals(0, space.getUpperLeftRow());
    assertEquals(0, space.getUpperLeftColumn());
    assertEquals(2, space.getLowerRightRow());
    assertEquals(2, space.getLowerRightColumn());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeSpaceIndex() {
    new SpaceImpl(-1, "Room", 0, 0, 1, 1, new ArrayList<>(), new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSpaceName() {
    new SpaceImpl(0, null, 0, 0, 1, 1, new ArrayList<>(), new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptySpaceName() {
    new SpaceImpl(0, "", 0, 0, 1, 1, new ArrayList<>(), new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCoordinates() {
    new SpaceImpl(0, "Room", -1, 0, 1, 1, new ArrayList<>(), new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCoordinates() {
    new SpaceImpl(0, "Room", 0, 0, 0, 0, new ArrayList<>(), new ArrayList<>());
  }

  @Test
  public void testGetItems() {
    List<Item> gotItems = space.getItems();
    assertEquals(items, gotItems);
  }

  @Test
  public void testAddItem() {
    Item item = new ItemImpl("Sword", 10, 0);
    space.addItem(item);
    assertEquals(2, space.getItems().size());
    assertEquals(item, space.getItems().get(1));
  }

  @Test
  public void testRemoveItem() {
    Item item = new ItemImpl("Sword", 10, 0);
    space.addItem(item);
    space.removeItem(item);
    assertEquals(items, space.getItems());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveItemNotInSpace() {
    Item item = new ItemImpl("Sword", 10, 0);
    space.removeItem(item);
  }

  @Test
  public void testGetNeighborIndices() {
    List<Integer> neighborList = space.getNeighborIndices();
    assertEquals(neighbors, neighborList);
  }

  @Test
  public void testSetNeighbors() {
    List<Integer> newNeighbors = new ArrayList<>();
    newNeighbors.add(2);
    newNeighbors.add(3);
    space.setNeighbors(newNeighbors);
    assertEquals(newNeighbors, space.getNeighborIndices());
  }

  @Test
  public void testCopy() {
    Space copiedSpace = space.copy();
    assertEquals(space.getSpaceIndex(), copiedSpace.getSpaceIndex());
    assertEquals(space.getSpaceName(), copiedSpace.getSpaceName());
    assertEquals(space.getUpperLeftRow(), copiedSpace.getUpperLeftRow());
    assertEquals(space.getUpperLeftColumn(), copiedSpace.getUpperLeftColumn());
    assertEquals(space.getLowerRightRow(), copiedSpace.getLowerRightRow());
    assertEquals(space.getLowerRightColumn(), copiedSpace.getLowerRightColumn());
    assertEquals(space.getItems(), copiedSpace.getItems());
    assertEquals(space.getNeighborIndices(), copiedSpace.getNeighborIndices());
  }

  @Test
  public void testGetNeighborInfo() {
    List<Space> allSpaces = new ArrayList<>();
    allSpaces.add(space);
    allSpaces.add(new SpaceImpl(1, "Kitchen", 3, 0, 5, 2, new ArrayList<>(), new ArrayList<>()));
    String neighborInfo = space.getNeighborInfo(allSpaces);
    assertTrue(neighborInfo.contains("Neighbors:"));
    assertTrue(neighborInfo.contains("Kitchen"));
  }

  @Test
  public void testGetItemsInfo() {
    String itemsInfo = space.getItemsInfo();
    assertTrue(itemsInfo.contains("Items:"));
    assertTrue(itemsInfo.contains("Guitar"));
  }

  @Test
  public void testGetItemsInfoNoItems() {
    Space emptySpace = new SpaceImpl(1, "Empty Room", 0, 0, 1, 1, new ArrayList<>(),
        new ArrayList<>());
    String itemsInfo = emptySpace.getItemsInfo();
    assertTrue(itemsInfo.contains("No items in this space!"));
  }

  @Test
  public void testHasNeighbor() {
    assertTrue(space.hasNeighbor(1));
    assertFalse(space.hasNeighbor(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHasNeighborNegativeIndex() {
    space.hasNeighbor(-1);
  }

  @Test
  public void testGetPlayersInfo() {
    List<Player> players = new ArrayList<>();
    players.add(new HumanPlayer("Alice", 0, 5));
    players.add(new HumanPlayer("Bob", 1, 5));
    String playersInfo = space.getPlayersInfo(players);
    assertTrue(playersInfo.contains("Players in this space:"));
    assertTrue(playersInfo.contains("Alice"));
    assertFalse(playersInfo.contains("Bob"));
  }

  @Test
  public void testGetPlayersInfoNoPlayers() {
    List<Player> players = new ArrayList<>();
    String playersInfo = space.getPlayersInfo(players);
    assertTrue(playersInfo.contains("There are no players in this space."));
  }

  @Test
  public void testEquals() {
    SpaceImpl anotherSpace = new SpaceImpl(0, "Living Room", 0, 0, 2, 2, items, neighbors);
    assertTrue(space.equals(anotherSpace));
    assertFalse(space.equals(null));
    assertFalse(space.equals(new Object()));
  }

  @Test
  public void testHashCode() {
    SpaceImpl anotherSpace = new SpaceImpl(0, "Living Room", 0, 0, 2, 2, new ArrayList<>(),
        new ArrayList<>());
    assertEquals(space.hashCode(), anotherSpace.hashCode());
  }

  @Test
  public void testToString() {
    String expectedString = "Space{name='Living Room', "
        + "index=0, upperLeft=(0, 0), lowerRight=(2, 2)}";
    assertEquals(expectedString, space.toString());
  }
}

