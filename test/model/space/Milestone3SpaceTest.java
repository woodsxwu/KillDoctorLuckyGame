package model.space;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.item.Item;
import model.item.ItemImpl;
import model.pet.Pet;
import model.pet.PetImpl;
import model.player.HumanPlayer;
import model.player.Player;
import model.target.TargetCharacter;
import model.target.TargetCharacterImpl;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for Space implementation.
 */
public class Milestone3SpaceTest {
  private Space testSpace;
  private List<Item> items;
  private List<Integer> neighbors;
  private List<Space> spaces;
  private List<Player> players;
  private TargetCharacter target;
  private Pet pet;

  /**
   * Sets up the test data before each test.
   */
  @Before
  public void setUp() {
    // Initialize test data
    items = new ArrayList<>();
    items.add(new ItemImpl("Sword", 5, 0));
    items.add(new ItemImpl("Shield", 2, 0));

    neighbors = new ArrayList<>(Arrays.asList(1, 2));

    // Create the test space
    testSpace = new SpaceImpl(0, "TestRoom", 0, 0, 2, 2, items, neighbors);

    // Create a list of spaces for testing
    spaces = new ArrayList<>();
    spaces.add(testSpace);
    spaces.add(new SpaceImpl(1, "Neighbor1", 0, 3, 2, 5, new ArrayList<>(), new ArrayList<>()));
    spaces.add(new SpaceImpl(2, "Neighbor2", 3, 0, 5, 2, new ArrayList<>(), new ArrayList<>()));

    // Create test players
    players = new ArrayList<>();
    players.add(new HumanPlayer("Player1", 0, 3));
    players.add(new HumanPlayer("Player2", 1, 3));

    // Create test target character
    target = new TargetCharacterImpl("Target", 10);
    target.setCurrentSpaceIndex(0);

    // Create test pet
    pet = new PetImpl("TestPet", 1);
  }

  @Test
  public void testIsSpaceVisibleWhenPetInSameSpace() {
    // When pet is in the same space, the space should not be visible
    assertFalse(testSpace.isSpaceVisible(0));
  }

  @Test
  public void testIsSpaceVisibleWhenPetInDifferentSpace() {
    // When pet is in a different space, the space should be visible
    assertTrue(testSpace.isSpaceVisible(1));
  }

  @Test
  public void testGetSpaceInfoWithCompleteInfo() {
    String info = testSpace.getSpaceInfo(spaces, players, target, pet);
    
    // Check if info contains space name
    assertTrue(info.contains("TestRoom"));
    
    // Check if info contains item information
    assertTrue(info.contains("Sword"));
    assertTrue(info.contains("Shield"));
    
    // Check if info contains player information
    assertTrue(info.contains("Player1"));
    
    // Check if info contains target character information
    assertTrue(info.contains("Target"));
    
    // Check if info contains neighbor information
    assertTrue(info.contains("Neighbor1"));
    assertTrue(info.contains("Neighbor2"));
  }

  @Test
  public void testGetSpaceInfoWithNoItems() {
    Space emptySpace = new SpaceImpl(3, "EmptyRoom", 6, 0, 8, 2, 
        new ArrayList<>(), new ArrayList<>());
    String info = emptySpace.getSpaceInfo(spaces, players, target, pet);
    
    assertTrue(info.contains("No items in this space"));
  }

  @Test
  public void testGetSpaceInfoWithNoPlayers() {
    String info = testSpace.getSpaceInfo(spaces, new ArrayList<>(), target, pet);
    
    assertTrue(info.contains("There are no players in this space"));
  }

  @Test
  public void testGetSpaceInfoWithPetPresent() {
    pet.setSpaceIndex(0); // Set pet to be in the test space
    String info = testSpace.getSpaceInfo(spaces, players, target, pet);
    
    assertTrue(info.contains("TestPet is in this space"));
  }

  @Test
  public void testGetSpaceInfoWithPetAbsent() {
    pet.setSpaceIndex(1); // Set pet to be in a different space
    String info = testSpace.getSpaceInfo(spaces, players, target, pet);
    
    assertFalse(info.contains("TestPet is in this space"));
  }

  @Test
  public void testIsSpaceVisibleWithNegativeIndex() {
    // Should handle negative pet space index appropriately
    assertTrue(testSpace.isSpaceVisible(-1));
  }

  @Test
  public void testIsSpaceVisibleWithLargeIndex() {
    // Should handle large pet space index appropriately
    assertTrue(testSpace.isSpaceVisible(999));
  }
}