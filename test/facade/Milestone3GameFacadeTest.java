package facade;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.pet.Pet;
import model.player.ComputerPlayer;
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.world.World;
import org.junit.Before;
import org.junit.Test;

public class Milestone3GameFacadeTest {
  private GameFacade facade;
  private World mockWorld;
  private List<Space> mockSpaces;
  private Player mockPlayer;
  private TargetCharacter mockTarget;
  private Pet mockPet;
  private List<Player> players;

  @Before
  public void setUp() {
    // Create mocks
    mockWorld = mock(World.class);
    mockPlayer = mock(Player.class);
    mockTarget = mock(TargetCharacter.class);
    mockPet = mock(Pet.class);
    
    // Create a list of mock spaces
    mockSpaces = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Space mockSpace = mock(Space.class);
      when(mockSpace.getSpaceIndex()).thenReturn(i);
      when(mockSpace.getSpaceName()).thenReturn("Space" + i);
      when(mockSpace.isSpaceVisible(anyInt())).thenReturn(true);
      mockSpaces.add(mockSpace);
    }
    
    players = new ArrayList<>(Arrays.asList(mockPlayer));
    
    // Set up basic mock behaviors
    when(mockWorld.getSpaces()).thenReturn(mockSpaces);
    when(mockWorld.getPlayers()).thenReturn(players);
    when(mockWorld.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockWorld.getTargetCharacter()).thenReturn(mockTarget);
    when(mockWorld.getPet()).thenReturn(mockPet);
    when(mockWorld.getTotalSpace()).thenReturn(mockSpaces.size());
    
    // Set up getSpaceByIndex
    for (int i = 0; i < mockSpaces.size(); i++) {
      when(mockWorld.getSpaceByIndex(i)).thenReturn(mockSpaces.get(i));
    }
    
    when(mockPlayer.getPlayerName()).thenReturn("TestPlayer");
    
    facade = new GameFacadeImpl(mockWorld);
  }

  @Test
  public void testGetSpaceInfo() {
    when(mockSpaces.get(0).getSpaceInfo(mockSpaces, players, mockTarget, mockPet))
        .thenReturn("Space Info");
    
    String result = facade.getSpaceInfo("Space0");
    assertEquals("Space Info", result);
  }

  @Test
  public void testPlayerLookAround() {
    when(mockPlayer.lookAround(mockSpaces, players, mockTarget, mockPet))
        .thenReturn("Look Around Info");
    
    String result = facade.playerLookAround();
    assertEquals("Look Around Info", result);
    verify(mockWorld).nextTurn();
  }

  @Test
  public void testIsGameEndedWithWinner() {
    when(mockWorld.getWinner()).thenReturn("Player1");
    assertTrue(facade.isGameEnded());
  }

  @Test
  public void testIsGameEndedWithMaxTurns() {
    when(mockWorld.getCurrentTurn()).thenReturn(11);
    when(mockWorld.getMaxTurns()).thenReturn(10);
    assertTrue(facade.isGameEnded());
  }

  @Test
  public void testMoveTargetCharacterWithPet() {
    facade.moveTargetCharacter();
    verify(mockTarget).move(anyInt());
    verify(mockPet).moveFollowingDFS(mockSpaces);
  }

  @Test
  public void testComputerPlayerTakeTurn() {
    ComputerPlayer mockCompPlayer = mock(ComputerPlayer.class);
    when(mockWorld.getCurrentPlayer()).thenReturn(mockCompPlayer);
    when(mockCompPlayer.takeTurn(mockSpaces, players, mockTarget, mockPet))
        .thenReturn("Computer Move");
    
    String result = facade.computerPlayerTakeTurn();
    assertEquals("Computer Move", result);
  }

  @Test
  public void testGetWinnerGameNotEnded() {
    when(mockWorld.getWinner()).thenReturn(null);
    when(mockWorld.getCurrentTurn()).thenReturn(5);
    when(mockWorld.getMaxTurns()).thenReturn(10);
    
    assertThrows(IllegalStateException.class, () -> facade.getWinner());
  }

  @Test
  public void testGetWinnerWithSuccess() {
    when(mockWorld.getWinner()).thenReturn("Player1");
    when(mockWorld.getCurrentTurn()).thenReturn(10);
    when(mockWorld.getMaxTurns()).thenReturn(10);
    
    String result = facade.getWinner();
    assertEquals("Winner is Player1", result);
  }

  @Test
  public void testGetWinnerWithEscape() {
    when(mockWorld.getWinner()).thenReturn(null);
    when(mockWorld.getCurrentTurn()).thenReturn(11);  // Game ended due to max turns
    when(mockWorld.getMaxTurns()).thenReturn(10);
    
    String result = facade.getWinner();
    assertEquals("Target escaped! No winner.", result);
  }

  @Test
  public void testAttackTargetCharacterSuccess() {
    int spaceIndex = 0;
    when(mockTarget.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    
    Space currentSpace = mockSpaces.get(spaceIndex);
    when(currentSpace.playerCount(players)).thenReturn(1);
    when(currentSpace.getNeighborIndices()).thenReturn(new ArrayList<>());
    when(currentSpace.isSpaceVisible(anyInt())).thenReturn(false);
    
    when(mockPlayer.attack("Sword", mockTarget)).thenReturn("Attack successful");
    
    String result = facade.attackTargetCharacter("Sword");
    assertEquals("Attack successful", result);
  }

  @Test
  public void testAttackTargetCharacterDifferentSpace() {
    when(mockTarget.getCurrentSpaceIndex()).thenReturn(1);
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(0);
    
    String result = facade.attackTargetCharacter("Sword");
    assertEquals("Attack failed! Target is not in the same space", result);
  }

  @Test
  public void testAttackTargetCharacterSeen() {
    int spaceIndex = 0;
    when(mockTarget.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    
    Space currentSpace = mockSpaces.get(spaceIndex);
    when(currentSpace.playerCount(players)).thenReturn(2);
    when(currentSpace.getNeighborIndices()).thenReturn(new ArrayList<>());
    when(currentSpace.isSpaceVisible(anyInt())).thenReturn(true);
    
    String result = facade.attackTargetCharacter("Sword");
    assertEquals("Attack failed! Your attack is seen by another player.", result);
  }

  @Test
  public void testAttackTargetCharacterKill() {
    int spaceIndex = 0;
    when(mockTarget.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    
    Space currentSpace = mockSpaces.get(spaceIndex);
    when(currentSpace.playerCount(players)).thenReturn(1);
    when(currentSpace.getNeighborIndices()).thenReturn(new ArrayList<>());
    when(currentSpace.isSpaceVisible(anyInt())).thenReturn(false);
    
    when(mockPlayer.attack("Sword", mockTarget)).thenReturn("Fatal attack");
    when(mockTarget.getHealth()).thenReturn(0);
    
    String result = facade.attackTargetCharacter("Sword");
    
    verify(mockWorld).setWinner("TestPlayer");
    assertTrue(result.contains("Fatal attack"));
  }

  @Test
  public void testPlayerCanBeSeenAlone() {
    int spaceIndex = 0;
    Space currentSpace = mockSpaces.get(spaceIndex);
    when(currentSpace.playerCount(players)).thenReturn(1);
    when(currentSpace.getNeighborIndices()).thenReturn(new ArrayList<>());
    when(currentSpace.isSpaceVisible(anyInt())).thenReturn(true);
    
    assertFalse(facade.playerCanBeeSeen(spaceIndex));
  }

  @Test
  public void testPlayerCanBeSeenWithOthers() {
    int spaceIndex = 0;
    Space currentSpace = mockSpaces.get(spaceIndex);
    when(currentSpace.playerCount(players)).thenReturn(2);
    when(currentSpace.getNeighborIndices()).thenReturn(new ArrayList<>());
    when(currentSpace.isSpaceVisible(anyInt())).thenReturn(true);
    
    assertTrue(facade.playerCanBeeSeen(spaceIndex));
  }

  @Test
  public void testPlayerCanBeSeenFromNeighbor() {
    int spaceIndex = 0;
    Space currentSpace = mockSpaces.get(spaceIndex);
    Space neighborSpace = mockSpaces.get(1);
    
    when(currentSpace.playerCount(players)).thenReturn(1);
    when(neighborSpace.playerCount(players)).thenReturn(1);
    when(currentSpace.getNeighborIndices()).thenReturn(Arrays.asList(1));
    when(currentSpace.isSpaceVisible(anyInt())).thenReturn(true);
    when(neighborSpace.isSpaceVisible(anyInt())).thenReturn(true);
    
    assertTrue(facade.playerCanBeeSeen(spaceIndex));
  }

  @Test
  public void testLimitedInfo() {
    when(mockPlayer.limitedInfo(mockSpaces)).thenReturn("Limited Info");
    
    String result = facade.limitedInfo();
    assertEquals("Limited Info", result);
  }

  @Test
  public void testIsSpaceVisible() {
    int spaceIndex = 0;
    when(mockPet.getCurrentSpaceIndex()).thenReturn(5);
    Space currentSpace = mockSpaces.get(spaceIndex);
    when(currentSpace.isSpaceVisible(5)).thenReturn(true);
    
    assertTrue(facade.isSpaceVisible(spaceIndex));
    verify(currentSpace).isSpaceVisible(5);
  }

  @Test
  public void testMovePetSuccess() {
    int spaceIndex = 0;
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    when(mockPet.getCurrentSpaceIndex()).thenReturn(spaceIndex);
    
    String result = facade.movePet("Space1");
    verify(mockPet).setSpaceIndex(1);
    verify(mockWorld).nextTurn();
    assertEquals("TestPlayer moved pet to Space1.", result);
  }

  @Test
  public void testMovePetDifferentSpace() {
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(0);
    when(mockPet.getCurrentSpaceIndex()).thenReturn(1);
    
    String result = facade.movePet("Space2");
    assertEquals("Pet cannot be moved from another space", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePetInvalidSpace() {
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(0);
    when(mockPet.getCurrentSpaceIndex()).thenReturn(0);
    facade.movePet("NonexistentSpace");
  }

  @Test
  public void testComputerPlayerTakeTurnKill() {
    ComputerPlayer mockCompPlayer = mock(ComputerPlayer.class);
    when(mockWorld.getCurrentPlayer()).thenReturn(mockCompPlayer);
    when(mockCompPlayer.takeTurn(mockSpaces, players, mockTarget, mockPet))
        .thenReturn("Computer attack");
    when(mockTarget.getHealth()).thenReturn(0);
    when(mockCompPlayer.getPlayerName()).thenReturn("Computer");
    
    String result = facade.computerPlayerTakeTurn();
    verify(mockWorld).setWinner("Computer");
    assertTrue(result.contains("Computer attack"));
  }

  @Test
  public void testGetTargetInfo() {
    when(mockTarget.getTargetDescription(mockSpaces)).thenReturn("Target Info");
    
    String result = facade.getTargetInfo();
    assertEquals("Target Info", result);
  }
}