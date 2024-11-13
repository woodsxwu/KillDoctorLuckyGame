package model.pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.space.Space;
import model.space.SpaceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Pet implementation.
 */
public class PetTest {
  private Pet pet;
  private List<Space> spaces;
  
  @Before
  public void setUp() {
    pet = new PetImpl("Fortune", 0);
    
    // Create a list of test spaces with connections:
    // Space 0 connects to 1,2
    // Space 1 connects to 0,2,3
    // Space 2 connects to 0,1,3
    // Space 3 connects to 1,2
    spaces = new ArrayList<>();
    spaces.add(new SpaceImpl(0, "Room0", 0, 0, 1, 1, new ArrayList<>(), Arrays.asList(1, 2)));
    spaces.add(new SpaceImpl(1, "Room1", 0, 2, 1, 3, new ArrayList<>(), Arrays.asList(0, 2, 3)));
    spaces.add(new SpaceImpl(2, "Room2", 2, 0, 3, 1, new ArrayList<>(), Arrays.asList(0, 1, 3)));
    spaces.add(new SpaceImpl(3, "Room3", 2, 2, 3, 3, new ArrayList<>(), Arrays.asList(1, 2)));
  }

  @Test
  public void testValidConstruction() {
    Pet testPet = new PetImpl("TestPet", 1);
    assertEquals("TestPet", testPet.getPetName());
    assertEquals(1, testPet.getCurrentSpaceIndex());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionWithNullName() {
    new PetImpl(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionWithEmptyName() {
    new PetImpl("", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionWithBlankName() {
    new PetImpl("   ", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionWithNegativeSpaceIndex() {
    new PetImpl("TestPet", -1);
  }

  @Test
  public void testGetPetName() {
    assertEquals("Fortune", pet.getPetName());
  }

  @Test
  public void testGetCurrentSpaceIndex() {
    assertEquals(0, pet.getCurrentSpaceIndex());
  }

  @Test
  public void testCopy() {
    Pet copiedPet = pet.copy();
    assertEquals(pet.getPetName(), copiedPet.getPetName());
    assertEquals(pet.getCurrentSpaceIndex(), copiedPet.getCurrentSpaceIndex());
    assertNotSame(pet, copiedPet);
  }

  @Test
  public void testSetSpaceIndex() {
    pet.setSpaceIndex(2);
    assertEquals(2, pet.getCurrentSpaceIndex());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNegativeSpaceIndex() {
    pet.setSpaceIndex(-1);
  }

  @Test
  public void testGetPetDescription() {
    String expected = "Pet: Fortune is currently at space 0";
    assertEquals(expected, pet.getPetDescription());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFollowingDfsWithNullSpaces() {
    pet.moveFollowingDfs(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFollowingDfsWithEmptySpaces() {
    pet.moveFollowingDfs(new ArrayList<>());
  }

  @Test
  public void testDfsMovementPattern() {
    // Pet starts in Room 0
    assertEquals(0, pet.getCurrentSpaceIndex());

    // First move: Will go to Room 1 (first unvisited neighbor of Room 0)
    int move = pet.moveFollowingDfs(spaces);
    assertEquals(1, move);

    // Second move: Will go to Room 2 (unvisited neighbor of Room 1)
    move = pet.moveFollowingDfs(spaces);
    assertEquals(2, move);

    // Third move: Will go to Room 3 (unvisited neighbor of Room 2)
    move = pet.moveFollowingDfs(spaces);
    assertEquals(3, move);

    // Fourth move: Will backtrack to Room 2 (all neighbors of Room 3 are visited)
    move = pet.moveFollowingDfs(spaces);
    assertEquals(2, move);

    // Fifth move: Will backtrack to Room 1 (all neighbors of Room 2 are visited)
    move = pet.moveFollowingDfs(spaces);
    assertEquals(1, move);

    // Sixth move: Will backtrack to Room 0 (all neighbors of Room 1 are visited)
    move = pet.moveFollowingDfs(spaces);
    assertEquals(0, move);

    // Seventh move: Pattern restarts from Room 0
    move = pet.moveFollowingDfs(spaces);
    assertEquals(1, move); // Starts the pattern over
  }

  @Test
  public void testMultipleFullTraversals() {
    // Test that multiple complete traversals follow the same pattern
    List<Integer> firstTraversal = new ArrayList<>();
    List<Integer> secondTraversal = new ArrayList<>();

    // Record first complete traversal
    for (int i = 0; i < 7; i++) {
      firstTraversal.add(pet.moveFollowingDfs(spaces));
    }

    // Record second complete traversal
    for (int i = 0; i < 7; i++) {
      secondTraversal.add(pet.moveFollowingDfs(spaces));
    }

    // Both traversals should be identical since Dfs follows deterministic pattern
    assertEquals(firstTraversal, secondTraversal);
  }

  @Test
  public void testDfsResetsAfterManualMove() {
    // Start normal Dfs
    assertEquals(1, pet.moveFollowingDfs(spaces)); // Moves to Room 1
    assertEquals(2, pet.moveFollowingDfs(spaces)); // Moves to Room 2

    // Manually move the pet to Room 0
    pet.setSpaceIndex(0);

    // Dfs should restart from new position
    assertEquals(1, pet.moveFollowingDfs(spaces)); // Should start fresh Dfs from Room 0
  }

  @Test
  public void testDfsFromEachStartingPosition() {
    // Test Dfs pattern starting from each room
    
    // Start from Room 0
    pet.setSpaceIndex(0);
    assertEquals(1, pet.moveFollowingDfs(spaces)); // Goes to Room 1
    assertEquals(2, pet.moveFollowingDfs(spaces)); // Goes to Room 2
    
    // Start from Room 1
    pet.setSpaceIndex(1);
    assertEquals(0, pet.moveFollowingDfs(spaces)); // Goes to Room 0
    assertEquals(2, pet.moveFollowingDfs(spaces)); // Goes to Room 2
    
    // Start from Room 2
    pet.setSpaceIndex(2);
    assertEquals(0, pet.moveFollowingDfs(spaces)); // Goes to Room 0
    assertEquals(1, pet.moveFollowingDfs(spaces)); // Goes to Room 1
    
    // Start from Room 3
    pet.setSpaceIndex(3);
    assertEquals(1, pet.moveFollowingDfs(spaces)); // Goes to Room 1
    assertEquals(0, pet.moveFollowingDfs(spaces)); // Goes to Room 0
  }
}