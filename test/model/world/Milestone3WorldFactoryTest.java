package model.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import model.pet.Pet;

public class Milestone3WorldFactoryTest {
  private WorldFactory factory;
  
  @Before
  public void setUp() {
    factory = new WorldFactory();
  }

  @Test
  public void testCreateWorldWithValidPet() {
    // Valid world specification with pet
    String input = String.join("\n",
        "3 4 Test World",           // rows, cols, world name
        "20 Dr Lucky",              // target health, name
        "Fortune the Cat",          // pet name
        "2",                        // number of spaces
        "0 0 1 1 Living Room",      // space 1
        "2 2 3 3 Kitchen",          // space 2
        "0"                         // number of items
    );
    
    World world = factory.createWorld(new StringReader(input));
    
    Pet pet = world.getPet();
    assertNotNull("Pet should not be null", pet);
    assertEquals("Pet name should match", "Fortune the Cat", pet.getPetName());
    assertEquals("Pet should start at space 0", 0, pet.getCurrentSpaceIndex());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWorldWithEmptyPetName() {
    String input = String.join("\n",
        "3 4 Test World",
        "20 Dr Lucky",
        "",                         // empty pet name
        "2",
        "0 0 1 1 Living Room",
        "2 2 3 3 Kitchen",
        "0"
    );
    
    factory.createWorld(new StringReader(input));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWorldWithWhitespacePetName() {
    String input = String.join("\n",
        "3 4 Test World",
        "20 Dr Lucky",
        "   ",                      // whitespace pet name
        "2",
        "0 0 1 1 Living Room",
        "2 2 3 3 Kitchen",
        "0"
    );
    
    factory.createWorld(new StringReader(input));
  }

  @Test
  public void testCreateWorldWithPetNameHavingSpaces() {
    String input = String.join("\n",
        "3 4 Test World",
        "20 Dr Lucky",
        "Mr Whiskers Jr",           // pet name with spaces
        "2",
        "0 0 1 1 Living Room",
        "2 2 3 3 Kitchen",
        "0"
    );
    
    World world = factory.createWorld(new StringReader(input));
    
    Pet pet = world.getPet();
    assertNotNull("Pet should not be null", pet);
    assertEquals("Pet name with spaces should be preserved", "Mr Whiskers Jr", pet.getPetName());
  }

  @Test
  public void testPetStartsAtFirstSpace() {
    String input = String.join("\n",
        "3 4 Test World",
        "20 Dr Lucky",
        "Fortune",
        "3",
        "0 0 1 1 Living Room",
        "2 2 3 3 Kitchen",
        "4 4 5 5 Garden",
        "0"
    );
    
    World world = factory.createWorld(new StringReader(input));
    
    Pet pet = world.getPet();
    assertEquals("Pet should start at space index 0", 0, pet.getCurrentSpaceIndex());
  }

  @Test
  public void testPetCorrectlyLinkedToWorld() {
    String input = String.join("\n",
        "3 4 Test World",
        "20 Dr Lucky",
        "Fortune",
        "2",
        "0 0 1 1 Living Room",
        "2 2 3 3 Kitchen",
        "0"
    );
    
    World world = factory.createWorld(new StringReader(input));
    
    // Verify that the pet in the world is the same one we expect
    Pet pet = world.getPet();
    assertNotNull("World should have a pet", pet);
    assertEquals("Fortune", pet.getPetName());
    
    // Test that the pet can move through the world spaces
    pet.moveFollowingDFS(world.getSpaces());
    assertTrue("Pet should be able to move to a new space", 
        pet.getCurrentSpaceIndex() < world.getTotalSpace());
  }

  @Test
  public void testCompleteWorldCreationWithPet() {
    String input = String.join("\n",
        "3 4 Mansion",              // world specs
        "50 Dr Lucky",              // target
        "Fortune the Cat",          // pet
        "2",                        // spaces
        "0 0 1 1 Living Room",
        "2 2 3 3 Kitchen",
        "2",                        // items
        "0 5 Sword",
        "1 3 Plate"
    );
    
    World world = factory.createWorld(new StringReader(input));
    
    // Verify all components including pet
    assertEquals("Mansion", world.getWorldName());
    assertNotNull(world.getTargetCharacter());
    assertNotNull(world.getPet());
    assertEquals(2, world.getTotalSpace());
    assertEquals("Fortune the Cat", world.getPet().getPetName());
  }
}