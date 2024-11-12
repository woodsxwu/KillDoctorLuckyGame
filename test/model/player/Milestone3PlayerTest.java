package model.player;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.item.Item;
import model.item.ItemImpl;
import model.pet.Pet;
import model.pet.PetImpl;
import model.space.Space;
import model.space.SpaceImpl;
import model.target.TargetCharacter;
import model.target.TargetCharacterImpl;

/**
 * Test class for Milestone 3 player-related functionality.
 * Tests various aspects of player behavior including:
 * - Look around functionality
 * - Attack mechanics
 * - Pet interaction
 * - Computer player decision making
 * - Limited information display
 */
public class Milestone3PlayerTest {
  private Player humanPlayer;
  private ComputerPlayer computerPlayer;
  private List<Space> spaces;
  private List<Player> players;
  private TargetCharacter target;
  private Pet pet;
  private Space space1;
  private Space space2;
  private Space space3;
  private Item weapon1;
  private Item weapon2;
  private Item weapon3;
  private RandomGenerator randomGenerator;

  @Before
  public void setUp() {
    // Create spaces with a triangular layout
    spaces = new ArrayList<>();
    List<Item> items1 = new ArrayList<>();
    List<Item> items2 = new ArrayList<>();
    List<Item> items3 = new ArrayList<>();
    List<Integer> neighbors1 = new ArrayList<>(Arrays.asList(1, 2));
    List<Integer> neighbors2 = new ArrayList<>(Arrays.asList(0, 2));
    List<Integer> neighbors3 = new ArrayList<>(Arrays.asList(0, 1));

    space1 = new SpaceImpl(0, "Living Room", 0, 0, 1, 1, items1, neighbors1);
    space2 = new SpaceImpl(1, "Kitchen", 0, 2, 1, 3, items2, neighbors2);
    space3 = new SpaceImpl(2, "Dining Room", 2, 0, 3, 3, items3, neighbors3);
    spaces.add(space1);
    spaces.add(space2);
    spaces.add(space3);

    // Create weapons
    weapon1 = new ItemImpl("Knife", 3, 0);
    weapon2 = new ItemImpl("Gun", 5, 0);
    weapon3 = new ItemImpl("Poison", 4, 0);

    // Initialize players
    randomGenerator = new RandomGenerator(0, 1, 2, 3);
    humanPlayer = new HumanPlayer("Alice", 0, 5);
    computerPlayer = new ComputerPlayer("Bob", 0, 6, randomGenerator);
    Player thirdPlayer = new HumanPlayer("Charlie", 1, 7);
    players = new ArrayList<>();
    players.add(thirdPlayer);
    players.add(humanPlayer);
    players.add(computerPlayer);

    // Create target and pet
    target = new TargetCharacterImpl("Dr. Lucky", 10);
    pet = new PetImpl("Fortune", 0);
  }

  // Basic Look Around Tests
  @Test
  public void testLookAroundWithPet() {
    String result = humanPlayer.lookAround(spaces, players, target, pet);
    assertTrue(result.contains("Fortune is in this space"));
    assertTrue(result.contains("Living Room"));
    assertTrue(result.contains("Kitchen"));
  }

  @Test
  public void testLookAroundWithMultiplePlayers() {
    players.get(1).setCurrentSpaceIndex(0); // Same space
    players.get(2).setCurrentSpaceIndex(1); // Neighboring space
    Player newPlayer = new HumanPlayer("Nick", 0, 7);
    players.add(newPlayer);
    
    String result = humanPlayer.lookAround(spaces, players, target, pet);
    assertTrue(result.contains("Nick is in the same space"));
    assertTrue(result.contains("Charlie"));
  }

  @Test
  public void testLookAroundWithPetInNeighboringSpace() {
    pet.setSpaceIndex(1);
    String result = humanPlayer.lookAround(spaces, players, target, pet);
    assertTrue(result.contains("Fortune is in Kitchen"));
    assertTrue(result.contains("you can't take your eyes off it"));
  }

  @Test
  public void testLookAroundWithTargetAndPetInSameSpace() {
    target.setCurrentSpaceIndex(0);
    pet.setSpaceIndex(0);
    String result = humanPlayer.lookAround(spaces, players, target, pet);
    assertTrue(result.contains("Fortune is in this space"));
    assertTrue(result.contains("Dr. Lucky is in this space"));
  }

  // Attack Tests
  @Test
  public void testAttackWithItem() {
    humanPlayer.addItem(weapon1);
    String result = humanPlayer.attack("Knife", target);
    assertTrue(result.contains("attacked the target with Knife"));
    assertTrue(result.contains("3 damage"));
    assertEquals(7, target.getHealth());
  }

  @Test
  public void testAttackWithPoke() {
    String result = humanPlayer.attack("poke", target);
    assertTrue(result.contains("poked the target in the eye"));
    assertTrue(result.contains("1 damage"));
    assertEquals(9, target.getHealth());
  }

  @Test
  public void testAttackSequenceWithMultipleItems() {
    humanPlayer.addItem(weapon1);
    humanPlayer.addItem(weapon2);
    humanPlayer.addItem(weapon3);
    
    // First attack with strongest weapon
    String result1 = humanPlayer.attack("Gun", target);
    assertTrue(result1.contains("5 damage"));
    assertEquals(5, target.getHealth());
    
    // Second attack with medium weapon
    String result2 = humanPlayer.attack("Poison", target);
    assertTrue(result2.contains("4 damage"));
    assertEquals(1, target.getHealth());
    
    // Final attack with poke
    String result3 = humanPlayer.attack("poke", target);
    assertTrue(result3.contains("1 damage"));
    assertEquals(0, target.getHealth());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAttackWithNonExistentItem() {
    humanPlayer.attack("Sword", target);
  }

  // Limited Info Tests
  @Test
  public void testLimitedInfo() {
    String info = humanPlayer.limitedInfo(spaces);
    assertTrue(info.contains("Alice"));
    assertTrue(info.contains("Living Room"));
    assertTrue(info.contains("space index: 0"));
  }

  // Computer Player Tests

  @Test
  public void testBotMaxAttackAlwaysChoosesHighestDamage() {
    // Add multiple items in random order
    computerPlayer = new ComputerPlayer("Bob", 0, 6, new RandomGenerator(3, 3, 3)); // Force attacks
    computerPlayer.addItem(new ItemImpl("Knife", 3, 0));
    computerPlayer.addItem(new ItemImpl("Gun", 5, 0));
    computerPlayer.addItem(new ItemImpl("Poison", 4, 0));
    computerPlayer.addItem(new ItemImpl("Stick", 2, 0));
    target.setCurrentSpaceIndex(0); // Same space as computer
    
    // Make the bot attack multiple times to verify it always chooses the highest damage
    String result1 = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use Gun (5 damage) first", result1.contains("Gun"));
    assertEquals(5, target.getHealth());
    
    String result2 = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use Poison (4 damage) second", result2.contains("Poison"));
    assertEquals(1, target.getHealth());
    
    String result3 = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use Knife (3 damage) third", result3.contains("Knife"));
    assertEquals(0, target.getHealth());
  }
  
  @Test
  public void testBotMaxAttackWithEqualDamageItems() {
    // Add multiple items with the same damage
    computerPlayer = new ComputerPlayer("Bob", 0, 6, new RandomGenerator(3)); // Force attack
    computerPlayer.addItem(new ItemImpl("Knife", 3, 0));
    computerPlayer.addItem(new ItemImpl("Dagger", 3, 0));
    computerPlayer.addItem(new ItemImpl("Sword", 3, 0));
    target.setCurrentSpaceIndex(0);
    
    String result = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue(result.contains("3 damage"));
    assertEquals(7, target.getHealth());
  }

  @Test
  public void testBotMaxAttackWithSingleItem() {
    computerPlayer = new ComputerPlayer("Bob", 0, 6, new RandomGenerator(3)); // Force attack
    computerPlayer.addItem(new ItemImpl("Knife", 3, 0));
    target.setCurrentSpaceIndex(0);
    
    String result = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use only available item", result.contains("Knife"));
    assertTrue("Should deal correct damage", result.contains("3 damage"));
    assertEquals(7, target.getHealth());
  }

  @Test
  public void testBotMaxAttackTransitionToPoke() {
    computerPlayer = new ComputerPlayer("Bob", 0, 6, new RandomGenerator(3, 3)); // Force attacks
    computerPlayer.addItem(new ItemImpl("Knife", 3, 0));
    target.setCurrentSpaceIndex(0);
    
    // First attack should use the knife
    String result1 = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use Knife first", result1.contains("Knife"));
    assertEquals(7, target.getHealth());
    
    // Second attack should use poke as no items remain
    String result2 = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use poke when no items remain", result2.contains("poked"));
    assertEquals(6, target.getHealth());
  }

  @Test
  public void testBotMaxAttackWithItemAddedLater() {
    computerPlayer = new ComputerPlayer("Bob", 0, 6, new RandomGenerator(3, 3)); // Force attacks
    computerPlayer.addItem(new ItemImpl("Stick", 2, 0));
    target.setCurrentSpaceIndex(0);
    
    // First attack with lower damage item
    String result1 = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use Stick first", result1.contains("Stick"));
    assertEquals(8, target.getHealth());
    
    // Add higher damage item
    computerPlayer.addItem(new ItemImpl("Gun", 5, 0));
    
    // Should use new higher damage item
    String result2 = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue("Should use Gun when available", result2.contains("Gun"));
    assertEquals(3, target.getHealth());
  }

  @Test
  public void testBotPokeAttackWhenNoItems() {
    computerPlayer = new ComputerPlayer("Bob", 0, 6, new RandomGenerator(3)); // Force attack
    target.setCurrentSpaceIndex(0);
    String result = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue(result.contains("poked the target in the eye"));
    assertTrue(result.contains("1 damage"));
    assertEquals(9, target.getHealth());
  }

  @Test
  public void testMovePetRandomly() {
    computerPlayer.setCurrentSpaceIndex(0);
    pet.setSpaceIndex(0);
    
    RandomGenerator forcePetMove = new RandomGenerator(4);
    ComputerPlayer player = new ComputerPlayer("Bob", 0, 2, forcePetMove);
    
    String result = player.takeTurn(spaces, players, target, pet);
    assertTrue(result.contains("moved pet to"));
    assertNotEquals(0, pet.getCurrentSpaceIndex());
  }

  @Test
  public void testCannotMovePetFromDifferentSpace() {
    computerPlayer.setCurrentSpaceIndex(0);
    pet.setSpaceIndex(1);
    
    RandomGenerator forcePetMove = new RandomGenerator(4);
    ComputerPlayer player = new ComputerPlayer("Bob", 0, 2, forcePetMove);
    
    String result = player.takeTurn(spaces, players, target, pet);
    assertFalse(result.contains("moved pet to"));
    assertEquals(1, pet.getCurrentSpaceIndex());
  }

  @Test
  public void testNoAttackWhenVisible() {
    players.get(1).setCurrentSpaceIndex(0);
    computerPlayer.addItem(weapon2);
    
    String result = computerPlayer.takeTurn(spaces, players, target, pet);
    assertFalse(result.contains("attacked"));
    assertEquals(10, target.getHealth());
  }

  @Test
  public void testVisibilityWithPet() {
    computerPlayer = new ComputerPlayer("Bob", 0, 6, new RandomGenerator(3)); // Force attack
    computerPlayer.setCurrentSpaceIndex(0);
    players.get(1).setCurrentSpaceIndex(0);
    pet.setSpaceIndex(0);
    target.setCurrentSpaceIndex(0);

    String result = computerPlayer.takeTurn(spaces, players, target, pet);
    assertTrue(result.contains("poked") || result.contains("attacked"));
  }
}