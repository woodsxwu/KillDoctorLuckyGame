package control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import facade.GameFacade;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Integration tests for the WorldController implementation focusing on 
 * Milestone 3 features (MovePet and Attack commands).
 */
@RunWith(MockitoJUnitRunner.class)
public class Milestone3ControllerTest {
  
  @Mock
  private GameFacade mockFacade;
  private StringWriter output;
  private WorldController controller;
  
  @Before
  public void setUp() {
    output = new StringWriter();
    
    // Setup basic game state responses
    when(mockFacade.getPlayerCount()).thenReturn(2);
    when(mockFacade.getCurrentPlayerName()).thenReturn("TestPlayer");
    when(mockFacade.getCurrentTurn()).thenReturn(1);
    when(mockFacade.limitedInfo()).thenReturn("Player is in Kitchen");
    when(mockFacade.getTargetInfo()).thenReturn("Target is nearby");
    when(mockFacade.computerPlayerTurn()).thenReturn(false);
  }

  /**
   * Helper method to create controller with specific input.
   */
  private void createControllerWithInput(String input) {
    Readable readable = new StringReader(input);
    controller = new WorldControllerImpl(mockFacade, readable, output);
  }

  @Test
  public void testMovePetCommand() {
    // Setup mock responses
    when(mockFacade.movePet("Kitchen"))
        .thenReturn("TestPlayer moved pet to Kitchen");
    
    // Simulate game setup and move-pet command
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "move-pet Kitchen\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("TestPlayer moved pet to Kitchen"));
  }

  @Test
  public void testMovePetCommandFromWrongSpace() {
    // Setup mock response for attempting to move pet from wrong space
    when(mockFacade.movePet("Kitchen"))
        .thenReturn("Pet cannot be moved from another space");
    
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "move-pet Kitchen\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Pet cannot be moved from another space"));
  }

  @Test
  public void testAttackCommand() {
    // Setup mock response for successful attack
    when(mockFacade.attackTargetCharacter("Knife"))
        .thenReturn("TestPlayer attacked with Knife, caused 5 damage");
    
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "attack Knife\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("TestPlayer attacked with Knife, caused 5 damage"));
  }

  @Test
  public void testAttackCommandTargetNotInSpace() {
    // Setup mock response for attack when target is not in same space
    when(mockFacade.attackTargetCharacter("Knife"))
        .thenReturn("Attack failed! Target is not in the same space");
    
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "attack Knife\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Attack failed! Target is not in the same space"));
  }

  @Test
  public void testAttackCommandPlayerVisible() {
    // Setup mock response for attack when player is visible
    when(mockFacade.attackTargetCharacter("Knife"))
        .thenReturn("Attack failed! Your attack is seen by another player.");
    
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "attack Knife\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Attack failed! Your attack is seen by another player."));
  }

  @Test
  public void testInvalidCommands() {
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "move-pet\n"  // Missing argument
        + "attack\n"    // Missing argument
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Error: Wrong number of arguments"));
  }

  @Test
  public void testMultipleCommandsInSequence() {
    // Setup mock responses for sequence of commands
    when(mockFacade.movePet("Kitchen"))
        .thenReturn("TestPlayer moved pet to Kitchen");
    when(mockFacade.attackTargetCharacter("Knife"))
        .thenReturn("TestPlayer attacked with Knife, caused 5 damage");
    
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "move-pet Kitchen\n"
        + "attack Knife\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("TestPlayer moved pet to Kitchen"));
    assertTrue(outputStr.contains("TestPlayer attacked with Knife, caused 5 damage"));
  }

  @Test
  public void testAttackWithPokeCommand() {
    // Setup mock response for poke attack
    when(mockFacade.attackTargetCharacter("poke"))
        .thenReturn("TestPlayer poked the target in the eye, caused 1 damage, Ouch!");
    
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "attack poke\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("TestPlayer poked the target in the eye"));
    assertTrue(outputStr.contains("caused 1 damage"));
  }

  @Test
  public void testMovePetToInvalidSpace() {
    // Setup mock response for invalid space
    when(mockFacade.movePet("InvalidRoom"))
        .thenThrow(new IllegalArgumentException("Invalid space name"));
    
    String input = "add-human Alice Kitchen 3\n"
        + "map\n"
        + "start\n"
        + "move-pet InvalidRoom\n"
        + "quit\n";
    
    createControllerWithInput(input);
    controller.startGame(10);
    
    String outputStr = output.toString();
    assertTrue(outputStr.contains("Failed to move pet: Invalid space name"));
  }
}