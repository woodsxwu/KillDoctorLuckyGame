package facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;
import model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;


/**
 * Test class for GameFacadeImpl.
 */
public class GameFacadeImplTest {

  private GameFacadeImpl facade;
  private World mockWorld;
  private Space mockSpace;
  private Player mockPlayer;
  private TargetCharacter mockTarget;
  private Item mockItem;

  /**
   * Sets up the test fixture.
   */
  @Before
  public void setUp() {
    mockWorld = mock(World.class);
    mockSpace = mock(Space.class);
    mockPlayer = mock(Player.class);
    mockTarget = mock(TargetCharacter.class);
    mockItem = mock(Item.class);

    when(mockWorld.getSpaceByIndex(anyInt())).thenReturn(mockSpace);
    when(mockWorld.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockWorld.getTargetCharacter()).thenReturn(mockTarget);
    when(mockWorld.getTotalSpace()).thenReturn(5);

    facade = new GameFacadeImpl(mockWorld);
  }

  @Test
  public void testGetWorldName() {
    when(mockWorld.getWorldName()).thenReturn("Test World");
    assertEquals("Test World", facade.getWorldName());
  }

  @Test
  public void testGetSpaceInfo() {
    when(mockSpace.getSpaceName()).thenReturn("Living Room");
    when(mockSpace.getItemsInfo()).thenReturn("Items: Book, Lamp");
    when(mockSpace.getPlayersInfo(any())).thenReturn("Players: Alice, Bob");
    when(mockSpace.getNeighborInfo(any())).thenReturn("Neighbors: Kitchen, Bedroom");
    
    String info = facade.getSpaceInfo("Living Room");

    assertTrue(info.contains("Living Room"));
    assertTrue(info.contains("Items: Book, Lamp"));
    assertTrue(info.contains("Players: Alice, Bob"));
    assertTrue(info.contains("Neighbors: Kitchen, Bedroom"));
  }

  @Test
  public void testCreateWorldMap() throws IOException {
    BufferedImage mockImage = mock(BufferedImage.class);
    when(mockWorld.createWorldMap()).thenReturn(mockImage);

    assertEquals(mockImage, facade.createWorldMap());
  }

  @Test
  public void testAddHumanPlayer() {

    when(mockSpace.getSpaceName()).thenReturn("Living Room");
    when(mockSpace.getSpaceIndex()).thenReturn(0);

    // Perform the action
    facade.addHumanPlayer("Alice", "Living Room", 5);

    // Verify that a player was added to the world
    ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
    verify(mockWorld).addPlayer(playerCaptor.capture());

    // Check the properties of the added player
    Player addedPlayer = playerCaptor.getValue();
    assertTrue(addedPlayer instanceof HumanPlayer);
    assertEquals("Alice", addedPlayer.getPlayerName());
    assertEquals(0, addedPlayer.getCurrentSpaceIndex());
  }

  @Test
  public void testAddComputerPlayer() {
    when(mockSpace.getSpaceName()).thenReturn("Kitchen");

    facade.addComputerPlayer("Bot", "Kitchen", 3);

    ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
    verify(mockWorld).addPlayer(playerCaptor.capture());

    Player addedPlayer = playerCaptor.getValue();
    assertTrue(addedPlayer instanceof ComputerPlayer);
    assertEquals("Bot", addedPlayer.getPlayerName());
  }

  @Test
  public void testMovePlayer() {
    when(mockSpace.getSpaceName()).thenReturn("Living Room");
    when(mockPlayer.getCurrentSpaceIndex()).thenReturn(0);
    when(mockSpace.hasNeighbor(1)).thenReturn(true);
    
    Space mockKitchenSpace = mock(Space.class);
    when(mockKitchenSpace.getSpaceName()).thenReturn("Kitchen");
    when(mockKitchenSpace.getSpaceIndex()).thenReturn(1);
    
    when(mockWorld.getSpaceByIndex(1)).thenReturn(mockKitchenSpace);

    facade.movePlayer("Kitchen");

    verify(mockPlayer).setCurrentSpaceIndex(1);
    verify(mockWorld).nextTurn();
    verify(mockTarget).move(anyInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePlayerToInvalidSpace() {
    when(mockSpace.hasNeighbor(anyInt())).thenReturn(false);
    when(mockSpace.getSpaceName()).thenReturn("Living Room");
    facade.movePlayer("Invalid Room");
  }

  @Test
  public void testPlayerPickUpItem() {
    when(mockSpace.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getItemName()).thenReturn("Book");
    when(mockPlayer.addItem(any())).thenReturn(true);

    facade.playerPickUpItem("Book");

    verify(mockPlayer).addItem(mockItem);
    verify(mockSpace).removeItem(mockItem);
    verify(mockWorld).nextTurn();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerPickUpNonexistentItem() {
    when(mockSpace.getItems()).thenReturn(new ArrayList<>());
    facade.playerPickUpItem("Nonexistent Item");
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayerPickUpItemWhenInventoryFull() {
    when(mockSpace.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getItemName()).thenReturn("Book");
    when(mockPlayer.addItem(any())).thenReturn(false);

    facade.playerPickUpItem("Book");
  }

  @Test
  public void testPlayerLookAround() {
    when(mockPlayer.lookAround(any(), null, mockTarget, null)).thenReturn("Player's surroundings");

    String result = facade.playerLookAround();

    assertEquals("Player's surroundings", result);
    verify(mockWorld).nextTurn();
  }

  @Test
  public void testGetPlayerInfo() {
    when(mockWorld.getPlayers()).thenReturn(List.of(mockPlayer));
    when(mockPlayer.getPlayerName()).thenReturn("Alice");
    when(mockPlayer.getDescription(any())).thenReturn("Alice's description");

    String info = facade.getPlayerInfo("Alice");

    assertEquals("Alice's description", info);
  }

  @Test
  public void testGetCurrentPlayerName() {
    when(mockPlayer.getPlayerName()).thenReturn("Bob");
    assertEquals("Bob", facade.getCurrentPlayerName());
  }

  @Test
  public void testNextTurn() {
    facade.nextTurn();
    verify(mockWorld).nextTurn();
  }

  @Test
  public void testIsGameEndedTrue() {
    when(mockWorld.getCurrentTurn()).thenReturn(11);
    when(mockWorld.getMaxTurns()).thenReturn(10);
    assertTrue(facade.isGameEnded());
  }

  @Test
  public void testIsGameEndedFalse() {
    when(mockWorld.getCurrentTurn()).thenReturn(5);
    when(mockWorld.getMaxTurns()).thenReturn(10);
    assertFalse(facade.isGameEnded());
  }

  @Test
  public void testSetMaxTurns() {
    facade.setMaxTurns(20);
    verify(mockWorld).setMaxTurns(20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetMaxTurnsWithNegativeValue() {
    facade.setMaxTurns(-5);
  }

  @Test
  public void testGetCurrentTurn() {
    when(mockWorld.getCurrentTurn()).thenReturn(7);
    assertEquals(7, facade.getCurrentTurn());
  }

  @Test
  public void testMoveTargetCharacter() {
    facade.moveTargetCharacter();
    verify(mockTarget).move(anyInt());
  }

  @Test
  public void testComputerPlayerTurnTrue() {
    Player mockComputerPlayer = mock(ComputerPlayer.class);
    when(mockWorld.getCurrentPlayer()).thenReturn(mockComputerPlayer);
    assertTrue(facade.computerPlayerTurn());
  }

  @Test
  public void testComputerPlayerTurnFalse() {
    Player mockHumanPlayer = mock(HumanPlayer.class);
    when(mockWorld.getCurrentPlayer()).thenReturn(mockHumanPlayer);
    assertFalse(facade.computerPlayerTurn());
  }

  @Test
  public void testComputerPlayerTakeTurn() {
    Player mockComputerPlayer = mock(ComputerPlayer.class);
    when(mockWorld.getCurrentPlayer()).thenReturn(mockComputerPlayer);

    facade.computerPlayerTakeTurn();

    verify(mockComputerPlayer).takeTurn(any(), null, mockTarget, null);
    verify(mockTarget).move(anyInt());
  }

  @Test
  public void testGetPlayerCount() {
    when(mockWorld.getPlayerCount()).thenReturn(3);
    assertEquals(3, facade.getPlayerCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullWorld() {
    new GameFacadeImpl(null);
  }

  @Test
  public void testGameWithMaxTurnsReached() {
    when(mockWorld.getCurrentTurn()).thenReturn(6);
    when(mockWorld.getMaxTurns()).thenReturn(5);

    assertTrue(facade.isGameEnded());
  }
}
