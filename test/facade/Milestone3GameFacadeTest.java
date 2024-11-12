package facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.item.Item;
import model.item.ItemImpl;
import model.pet.Pet;
import model.pet.PetImpl;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.RandomGenerator;
import model.space.Space;
import model.space.SpaceImpl;
import model.target.TargetCharacter;
import model.target.TargetCharacterImpl;
import model.world.World;
import model.world.WorldImpl;

public class Milestone3GameFacadeTest {
  private GameFacade facade;
  private World world;
  private List<Space> spaces;
  private Player humanPlayer;
  private Player computerPlayer;
  private TargetCharacter target;
  private Pet pet;

  @Before
  public void setUp() {
    // Create spaces
    spaces = new ArrayList<>();
    spaces.add(createSpace(0, "Kitchen", 0, 0, 1, 1));
    spaces.add(createSpace(1, "Living Room", 0, 2, 1, 3));
    spaces.add(createSpace(2, "Dining Room", 2, 0, 3, 1));
    spaces.add(createSpace(3, "Study", 2, 2, 3, 3));
    
    // Set up neighbors manually since we're not using the world factory
    spaces.get(0).setNeighbors(List.of(1, 2)); // Kitchen connects to Living Room and Dining Room
    spaces.get(1).setNeighbors(List.of(0, 3)); // Living Room connects to Kitchen and Study
    spaces.get(2).setNeighbors(List.of(0, 3)); // Dining Room connects to Kitchen and Study
    spaces.get(3).setNeighbors(List.of(1, 2)); // Study connects to Living Room and Dining Room
    
    // Create items and add them to spaces
    spaces.get(0).addItem(new ItemImpl("Knife", 3, 0));
    spaces.get(1).addItem(new ItemImpl("Candlestick", 2, 1));
    spaces.get(2).addItem(new ItemImpl("Rope", 1, 2));
    spaces.get(3).addItem(new ItemImpl("Revolver", 4, 3));
    
    // Create players (starting in different rooms)
    humanPlayer = new HumanPlayer("Alice", 0, 2); // Alice starts in Kitchen with 2 item capacity
    computerPlayer = new ComputerPlayer("Bob", 1, 2, new RandomGenerator(1, 2, 3)); // Bob starts in Living Room
    
    // Create target character
    target = new TargetCharacterImpl("Dr. Lucky", 10); // 10 health points
    
    // Create pet
    pet = new PetImpl("Fortune", 0); // Pet starts in Kitchen
    
    // Create world
    world = new WorldImpl(
        "Dr. Lucky's Mansion",
        4, // rows
        4, // columns
        spaces,
        target,
        4, // total spaces
        4, // total items
        pet
    );
    
    // Add players to world
    world.addPlayer(humanPlayer);
    world.addPlayer(computerPlayer);
    
    // Create facade
    facade = new GameFacadeImpl(world);
    facade.setMaxTurns(20);
  }
  
  private Space createSpace(int index, String name, int ulRow, int ulCol, int lrRow, int lrCol) {
    return new SpaceImpl(
        index,
        name,
        ulRow,
        ulCol,
        lrRow,
        lrCol,
        new ArrayList<>(),
        new ArrayList<>()
    );
  }

  // Constructor Tests
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullWorld() {
    new GameFacadeImpl(null);
  }

  // Basic Space Information Tests
  @Test
  public void testPlayerCanSeeSpaceName() {
    String result = facade.limitedInfo();
    assertTrue(result.contains("Kitchen"));
    assertTrue(result.contains("Alice"));
  }

  // Space Info Tests
  @Test(expected = IllegalArgumentException.class)
  public void testGetSpaceInfoWithNonexistentSpace() {
    facade.getSpaceInfo("NonexistentSpace");
  }

  @Test
  public void testGetSpaceInfoSuccess() {
    String result = facade.getSpaceInfo("Kitchen");
    assertTrue(result.contains("Kitchen"));
    assertTrue(result.contains("Knife"));
  }

  // Player Movement Tests
  @Test(expected = IllegalArgumentException.class)
  public void testMovePlayerToNonexistentSpace() {
    facade.movePlayer("NonexistentSpace");
  }

  @Test
  public void testMovePlayerSuccess() {
    String result = facade.movePlayer("Living Room");
    assertTrue(result.contains("moved to Living Room"));
  }

  @Test
  public void testMovePlayerToNonNeighborFails() {
    assertThrows(IllegalArgumentException.class, () -> facade.movePlayer("Study"));
  }

  // Attack Tests
  @Test
  public void testAttackWithNoWeapon() {
    assertThrows(IllegalArgumentException.class, () -> 
        facade.attackTargetCharacter("NonexistentWeapon"));
  }

  @Test
  public void testAttackWithZeroHealthTarget() {
    // Setup: Move target to kitchen and reduce health to 1
    target.setCurrentSpaceIndex(0);
    target.takeDamage(9); // Reduce to 1 health
    
    // Add item directly to player's inventory to avoid turn change
    Item knife = new ItemImpl("Knife", 3, 0);
    humanPlayer.addItem(knife);
    
    String result = facade.attackTargetCharacter("Knife");
    assertTrue(result.contains("attacked"));
    assertEquals(0, target.getHealth());
    assertTrue(facade.getWinner().contains("Alice")); // Alice should be the winner
    assertTrue(facade.isGameEnded());
  }

  @Test
  public void testAttackBlockedByNeighborWithoutPet() {
    target.setCurrentSpaceIndex(3);
    computerPlayer.setCurrentSpaceIndex(1);
    humanPlayer.setCurrentSpaceIndex(3);
    
    // Add item directly to player's inventory
    Item knife = new ItemImpl("Knife", 3, 0);
    humanPlayer.addItem(knife);
    
    String result = facade.attackTargetCharacter("Knife");
    assertEquals("Attack failed! Your attack is seen by another player.", result);
    assertEquals(10, target.getHealth()); // Health should not change
  }

  @Test
  public void testAttackSucceedsWhenPetBlocksView() {
    // Setup: Move target to kitchen where human player is
    target.setCurrentSpaceIndex(0);
    
    // Move computer player to Living Room (neighbor space)
    computerPlayer.setCurrentSpaceIndex(1);
    
    // First try attack without pet - should be seen by neighbor
    Item knife1 = new ItemImpl("Knife", 3, 0);
    humanPlayer.addItem(knife1);
    
    // Put pet in Kitchen (same space as attacker)
    // This makes the attacker's space invisible to others
    pet.setSpaceIndex(0);
    
    String result = facade.attackTargetCharacter("Knife");
    assertEquals("Alice attacked the target with Knife, caused 3 damage.\n", result);
    assertEquals(7, target.getHealth());
  }
  
  @Test
  public void testAttackTriggersMovement() {
    target.setCurrentSpaceIndex(0);
    
    // Add item directly to player's inventory
    Item knife = new ItemImpl("Knife", 3, 0);
    humanPlayer.addItem(knife);
    
    int originalTargetSpace = target.getCurrentSpaceIndex();
    int originPetSpace = pet.getCurrentSpaceIndex();
    String result = facade.attackTargetCharacter("Knife");
    
    assertTrue(result.contains("attacked"));
    // Target should move after attack if not killed
    assertNotEquals(originalTargetSpace, target.getCurrentSpaceIndex());
    assertNotEquals(originPetSpace, pet.getCurrentSpaceIndex());
  }

  @Test
  public void testAttackFailsWhenTargetInDifferentSpace() {
    // Setup: Target in different room
    target.setCurrentSpaceIndex(2); // Dining Room
    
    // Add item directly to player's inventory
    Item knife = new ItemImpl("Knife", 3, 0);
    humanPlayer.addItem(knife);
    
    String result = facade.attackTargetCharacter("Knife");
    assertEquals("Attack failed! Target is not in the same space", result);
    assertEquals(10, target.getHealth()); // Health should not change
  }

  // Pet Movement Tests
  @Test(expected = IllegalArgumentException.class)
  public void testMovePetToInvalidSpace() {
    facade.movePet("InvalidSpace");
  }

  @Test
  public void testMovePetFromWrongSpace() {
    pet.setSpaceIndex(1); 
    String result = facade.movePet("Dining Room");
    assertEquals("Pet cannot be moved from another space", result);
  }

  @Test
  public void testMovePetSuccess() {
    assertEquals(0, pet.getCurrentSpaceIndex());
    String result = facade.movePet("Living Room");
    assertTrue(result.contains("moved pet to Living Room"));
    assertEquals(1, pet.getCurrentSpaceIndex());
  }

  // Computer Player Tests
  @Test
  public void testComputerPlayerTurnSuccess() {
    // First player ends turn
    facade.movePlayer("Living Room");
    
    // Now it's computer's turn
    String result = facade.computerPlayerTakeTurn();
    assertNotNull(result);
    assertTrue(result.length() > 0);
  }

  // Game End Conditions Tests
  @Test
  public void testGameEndByMaxTurns() {
    facade.setMaxTurns(2);
    facade.movePlayer("Living Room"); // Turn 1
    facade.computerPlayerTakeTurn(); // Turn 2
    assertTrue(facade.isGameEnded());
  }

  @Test
  public void testGameEndByTargetDeath() {
    target.setCurrentSpaceIndex(0); // Move target to kitchen
    Item knife = new ItemImpl("Knife", 3, 0);
    humanPlayer.addItem(knife);
    target.takeDamage(9); // Leave 1 health
    facade.attackTargetCharacter("Knife");
    assertTrue(facade.isGameEnded());
  }

  // Visibility Tests
  @Test
  public void testSpaceVisibilityWithPet() {
    pet.setSpaceIndex(1); // Move pet to Living Room
    assertFalse(facade.isSpaceVisible(1)); // Space with pet should not be visible
    assertTrue(facade.isSpaceVisible(0)); // Current space should be visible
  }

  @Test
  public void testPlayerCannotBeSeenInInvisibleSpace() {
    pet.setSpaceIndex(1); // Pet makes space 1 invisible
    assertFalse(facade.playerCanBeeSeen(1));
  }

  @Test
  public void testPlayerCanSeeItemsInCurrentSpace() {
    String lookResult = facade.playerLookAround();
    assertTrue(lookResult.contains("Knife"));
  }

  @Test
  public void testPlayerCanSeeNeighborSpaces() {
    String lookResult = facade.playerLookAround();
    assertTrue(lookResult.contains("Living Room"));
    assertTrue(lookResult.contains("Dining Room"));
  }

  @Test
  public void testPlayerCanSeeTargetInSameRoom() {
    target.setCurrentSpaceIndex(0);
    String result = facade.playerLookAround();
    assertTrue(result.contains("Dr. Lucky"));
  }

  @Test
  public void testPlayerCanSeePetInSameRoom() {
    String result = facade.playerLookAround();
    assertTrue(result.contains("Fortune"));
  }

  @Test
  public void testPlayerVisibilityInMultipleNeighborSpaces() {
    // Move players to different neighbor spaces
    humanPlayer.setCurrentSpaceIndex(0);  // Kitchen
    computerPlayer.setCurrentSpaceIndex(1); // Living Room
    Player thirdPlayer = new HumanPlayer("Charlie", 2, 2); // Dining Room
    world.addPlayer(thirdPlayer);
    
    String result = facade.playerLookAround();
    assertTrue(result.contains("Bob"));
    assertTrue(result.contains("Charlie"));
  }

  @Test
  public void testPetBlocksVisibilityCheck() {
    // Test using isSpaceVisible method directly
    pet.setSpaceIndex(1); // Put pet in Living Room
    assertFalse("Space with pet should not be visible", 
        facade.isSpaceVisible(1));
  }

  @Test
  public void testPetAffectsPlayerVisibility() {
    // Test using playerCanBeeSeen method
    pet.setSpaceIndex(1); // Put pet in Living Room
    computerPlayer.setCurrentSpaceIndex(1); // Put player in same room as pet
    assertFalse("Player in room with pet should not be visible", 
        facade.playerCanBeeSeen(1));
  }

  @Test
  public void testPetVisibilityInLookAround() {
    // Initial setup - move pet to Living Room
    pet.setSpaceIndex(1);
    
    String result = facade.playerLookAround();
    
    // When looking around from Kitchen (space 0):
    assertTrue("Should see the pet name", 
        result.contains("Fortune"));
    assertTrue("Should mention the pet's location", 
        result.contains("Fortune is in Living Room"));
    assertTrue("Should have the can't take eyes off message", 
        result.contains("you can't take your eyes off it"));
  }

  @Test
  public void testVisibilityWithMultiplePlayersAndPet() {
    // Setup multiple players in neighboring spaces
    computerPlayer.setCurrentSpaceIndex(1);
    Player thirdPlayer = new HumanPlayer("Charlie", 1, 2);
    world.addPlayer(thirdPlayer);
    
    // Without pet - should be visible
    assertTrue(facade.playerCanBeeSeen(1));
    
    // With pet blocking visibility
    pet.setSpaceIndex(1);
    assertFalse(facade.playerCanBeeSeen(1));
  }

  // Item Interaction Tests
  @Test
  public void testPickUpItem() {
    String result = facade.playerPickUpItem("Knife");
    assertTrue(result.contains("picked up Knife"));
    assertEquals(1, humanPlayer.getItems().size());
  }
  
  @Test
  public void testAttackWithPoke() {
    // Setup: Move target to same space as player
    target.setCurrentSpaceIndex(0);
    
    String result = facade.attackTargetCharacter("poke");
    assertTrue(result.contains("poked"));
    assertEquals(9, target.getHealth()); // Poke does 1 damage
  }

  @Test
  public void testGetWinnerBeforeGameEnd() {
    // Game hasn't ended yet
    assertThrows(IllegalStateException.class, () -> facade.getWinner());
  }

  @Test
  public void testGetWinnerAfterTargetKilled() {
    // Setup: Move target to player's space and kill it
    target.setCurrentSpaceIndex(0);
    Item knife = new ItemImpl("Knife", 10, 0); // Weapon that can kill in one hit
    humanPlayer.addItem(knife);
    
    facade.attackTargetCharacter("Knife");
    String winner = facade.getWinner();
    assertTrue(winner.contains("Alice")); // Alice should be winner
  }

  @Test
  public void testGetWinnerAfterMaxTurns() {
    facade.setMaxTurns(1);
    facade.movePlayer("Living Room"); // Take one turn
    
    String winner = facade.getWinner();
    assertTrue(winner.contains("Target escaped")); // No winner when turns run out
  }

  @Test
  public void testPetMovesEveryTurn() {
    // Record initial pet position
    int initialPetPosition = pet.getCurrentSpaceIndex();
    
    // Take a turn
    facade.movePlayer("Living Room");
    
    // Pet should have moved
    assertNotEquals(initialPetPosition, pet.getCurrentSpaceIndex());
    
    // Record position after first turn
    int secondPetPosition = pet.getCurrentSpaceIndex();
    
    // Take another turn
    facade.computerPlayerTakeTurn();
    
    // Pet should move again
    assertNotEquals(secondPetPosition, pet.getCurrentSpaceIndex());
  }

  @Test
  public void testPetAndTargetStartInSameSpace() {
    // Both should start in space 0 (Kitchen in our setup)
    assertEquals(0, pet.getCurrentSpaceIndex());
    assertEquals(0, target.getCurrentSpaceIndex());
    
    // Verify through lookAround that both are visible in starting space
    String lookResult = facade.playerLookAround();
    assertTrue(lookResult.contains("Fortune")); // Pet name
    assertTrue(lookResult.contains("Dr. Lucky")); // Target name
  }
  
  @Test
  public void testItemsUsedInAttackAreRemovedFromPlay() {
    World singlePlayerWorld = new WorldImpl(
        "Dr. Lucky's Mansion",
        4, // rows
        4, // columns
        spaces,
        target,
        4, // total spaces
        4, // total items
        pet
    );
    singlePlayerWorld.addPlayer(humanPlayer);
    GameFacade singlePlayerFacade = new GameFacadeImpl(singlePlayerWorld);
    
    // Initially verify Alice is in Kitchen where there's a Knife
    assertEquals(0, humanPlayer.getCurrentSpaceIndex());

    // Have Alice pick up the Knife
    singlePlayerFacade.playerPickUpItem("Knife");

    // Verify Alice has the Knife
    assertTrue(humanPlayer.getItems().stream()
        .anyMatch(item -> item.getItemName().equals("Knife")));
        
    // Move target to same space as Alice for the attack
    target.setCurrentSpaceIndex(0);

    // Alice attacks with Knife
    String attackResult = singlePlayerFacade.attackTargetCharacter("Knife");

    // Verify attack happened
    assertTrue(attackResult.contains("attacked"));

    // Verify Knife is no longer in Alice's inventory
    assertFalse(humanPlayer.getItems().stream()
        .anyMatch(item -> item.getItemName().equals("Knife")));

    // Verify Knife is not in any space
    for (Space space : spaces) {
        assertFalse(space.getItems().stream()
            .anyMatch(item -> item.getItemName().equals("Knife")));
    }
  }
}