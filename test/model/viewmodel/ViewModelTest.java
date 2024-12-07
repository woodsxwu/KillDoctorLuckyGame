package model.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.pet.Pet;
import model.pet.PetImpl;
import model.player.HumanPlayer;
import model.player.Player;
import model.space.Space;
import model.space.SpaceImpl;
import model.target.TargetCharacter;
import model.target.TargetCharacterImpl;
import model.world.WorldImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the ViewModel interface.
 */
public class ViewModelTest {
  private ViewModel viewModel;

  /**
   * Sets up the test fixture.
   */
  @Before
  public void setUp() {
    // Create a test WorldImpl instance to use as the view model
    List<Space> spaces;
    spaces = new ArrayList<>();
    spaces.add(new SpaceImpl(0, "Living Room", 0, 0, 2, 2, new ArrayList<>(), new ArrayList<>()));
    spaces.add(new SpaceImpl(1, "Kitchen", 3, 0, 5, 2, new ArrayList<>(), new ArrayList<>()));
    spaces.add(new SpaceImpl(2, "Bedroom", 0, 3, 2, 5, new ArrayList<>(), new ArrayList<>()));
    TargetCharacter target = new TargetCharacterImpl("Dr. Lucky", 50);
    int totalSpaces = 9;
    int totalItems = 5;
    Pet pet = new PetImpl("Dog", 0);
    String worldName = "Test World";
    int rows = 3;
    int columns = 3; 
    WorldImpl world = new WorldImpl(worldName, rows, columns, spaces, target, 
                              totalSpaces, totalItems, pet);
    world.addPlayer(new HumanPlayer("Alice", 0, 0));
    world.addPlayer(new HumanPlayer("Bob", 2, 2));
    viewModel = world;
  }

  @Test
  public void testGetWorldName() {
    assertEquals("Test World", viewModel.getWorldName());
  }

  @Test
  public void testGetRows() {
    assertEquals(3, viewModel.getRows());
  }

  @Test
  public void testGetColumns() {  
    assertEquals(3, viewModel.getColumns());
  }

  @Test
  public void testGetPlayerCopies() {
    List<Player> playerCopies = viewModel.getPlayerCopies();
    assertEquals(2, playerCopies.size());
    assertNotSame(viewModel.getPlayerCopies().get(0), playerCopies.get(0)); // Ensure copy
  }

  @Test  
  public void testGetSpaceCopies() {
    List<Space> spaceCopies = viewModel.getSpaceCopies();
    assertEquals(3, spaceCopies.size()); 
    assertNotSame(((WorldImpl) viewModel).getSpaces().get(0), spaceCopies.get(0)); // Ensure copy
  }

  @Test
  public void testGetTargetCopy() {
    TargetCharacter targetCopy = viewModel.getTargetCopy();
    assertNotNull(targetCopy);
    assertNotSame(((WorldImpl) viewModel).getTargetCharacter(), targetCopy); // Ensure copy
  }

  @Test
  public void testGetPetCopy() {  
    Pet petCopy = viewModel.getPetCopy();
    assertNotNull(petCopy);
    assertNotSame(((WorldImpl) viewModel).getPet(), petCopy); // Ensure copy
  }
  
  @Test
  public void testCreateWorldMap() throws IOException {
    BufferedImage worldMap = viewModel.createWorldMap();
    assertNotNull(worldMap);
    assertTrue(worldMap.getWidth() > 0);
    assertTrue(worldMap.getHeight() > 0);
  }

  @Test
  public void testGetCurrentPlayerCopy() {
    Player playerCopy = viewModel.getCurrentPlayerCopy();
    assertNotNull(playerCopy);
    assertNotSame(viewModel.getCurrentPlayerCopy(), playerCopy); // Ensure copy
  }

  @Test
  public void testGetCurrentTurn() {
    assertEquals(1, viewModel.getCurrentTurn()); // Default turn
  }

}
