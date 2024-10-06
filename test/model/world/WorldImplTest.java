package model.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.item.ItemImpl;
import model.space.Space;
import model.space.SpaceImpl;
import model.target.TargetCharacter;
import model.target.TargetCharacterImpl;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit tests for WorldImpl.
 */
public class WorldImplTest {

  private WorldImpl world;
  private List<Space> spaces;
  private TargetCharacter targetCharacter;
  
  /**
   * Create objects before each test.
   */
  @Before
  public void setUp() {
    // Set up sample spaces
    spaces = new ArrayList<>();
    spaces.add(new SpaceImpl(0, "Space 1", 0, 0, 2, 2, new ArrayList<>(), new ArrayList<>()));
    spaces.add(new SpaceImpl(1, "Space 2", 3, 0, 5, 2, new ArrayList<>(), new ArrayList<>()));
    spaces.add(new SpaceImpl(2, "Space 3", 0, 3, 2, 5, new ArrayList<>(), new ArrayList<>()));
    
    // Create a target character
    targetCharacter = new TargetCharacterImpl("Target Character", 100);

    // Create a world
    world = new WorldImpl("Test World", 6, 6, spaces, targetCharacter, 3, 0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidWorldInitialization() {
    new WorldImpl("", 6, 6, spaces, targetCharacter, 3, 0);
  }
  
  @Test
  public void testFindNeighbors() {
    world.findNeighbors();
    
    // Verify neighbors
    Space space1 = spaces.get(0);
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    assertEquals(list, space1.getNeighborIndices());
  }
  
  @Test
  public void testAddItemToSpace() {
    Item item = new ItemImpl("Item 1", 10, 0);
    spaces.get(0).addItem(item);
    
    assertEquals(1, spaces.get(0).getItems().size());
    assertEquals("Item 1", spaces.get(0).getItems().get(0).getItemName());
  }
  
  @Test
  public void testGetSpaceInfoByIndex() {
    world.findNeighbors();
    String spaceInfo = world.getSpaceInfoByIndex(0);
    System.out.println(spaceInfo);
    StringBuilder info = new StringBuilder();
    info.append(String.format("Space: Space 1%n"));
    info.append("There are no items in the space.\n");
    info.append("Visible neighboring spaces:\n");
    info.append(String.format(" - Space 2%n")).append(String.format(" - Space 3%n"));
    String s = info.toString();
    assertTrue(s.equals(spaceInfo));
  }
  
  @Test
  public void testMoveTargetCharacter() {
    int initialPosition = targetCharacter.getCurrentSpaceIndex();
    world.moveTargetCharacter();
    assertNotEquals(initialPosition, targetCharacter.getCurrentSpaceIndex());
  }
  
  @Test
  public void testTargetCharacterInfo() {
    TargetCharacter copy = world.getTargetCharacter();
    assertEquals("Target Character", copy.getTargetName());
    assertEquals(100, copy.getHealth());
  }
}


