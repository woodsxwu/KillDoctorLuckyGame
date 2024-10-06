package model.space;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.item.ItemImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for SpaceImpl.
 */
public class SpaceImplTest {

  private SpaceImpl space;
  private List<Item> items;
  private List<Integer> neighbors;
  
  /**
   * Create objects before each test.
   */
  @Before
  public void setUp() {
    items = new ArrayList<>();
    Item item = new ItemImpl("guitar", 3, 0);
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
    List<Integer> sampleIntegers = new ArrayList<Integer>();
    sampleIntegers.add(1);
    assertEquals(sampleIntegers, neighborList);
  }
  
  @Test
  public void testSetNeighbors() {
    List<Integer> newNeighbors = new ArrayList<>();
    newNeighbors.add(1);
    space.setNeighbors(newNeighbors);
    assertEquals(1, space.getNeighborIndices().size());
    assertEquals(Integer.valueOf(1), space.getNeighborIndices().get(0));
  }
  
  @Test
  public void testEquals() {
    SpaceImpl anotherSpace = new SpaceImpl(0, "Living Room", 0, 0, 2, 2, items, neighbors);
    assertTrue(space.equals(anotherSpace));
  }
  
  @Test
  public void testHashCode() {
    SpaceImpl anotherSpace = new SpaceImpl(0, "Living Room", 
        0, 0, 2, 2, new ArrayList<>(), new ArrayList<>());
    assertEquals(space.hashCode(), anotherSpace.hashCode());
  }
  
  @Test
  public void testToString() {
    String expectedString = "Space{name='Living Room', "
        + "index=0, upperLeft=(0, 0), lowerRight=(2, 2)}";
    assertEquals(expectedString, space.toString());
  }
}

