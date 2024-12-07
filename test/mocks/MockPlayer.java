package mocks;

import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.pet.Pet;
import model.player.Player;
import model.space.Space;
import model.target.TargetCharacter;

/**
 * Mock implementation of Player interface for testing.
 */
public class MockPlayer implements Player {
  private final StringBuilder log;
  private final String name;
  private int currentSpaceIndex;
  private final List<Item> items;
  private final int maxItems;
  private final boolean isComputer;

  /**
   * Constructs a new MockPlayer.
   *
   * @param log               the StringBuilder to log actions to
   * @param name              the player's name
   * @param currentSpaceIndex the starting space index
   * @param maxItems          maximum number of items player can carry
   * @param isComputer        whether this is a computer player
   */
  public MockPlayer(StringBuilder log, String name, int currentSpaceIndex, int maxItems,
      boolean isComputer) {
    this.log = log;
    this.name = name;
    this.currentSpaceIndex = currentSpaceIndex;
    this.items = new ArrayList<>();
    this.maxItems = maxItems;
    this.isComputer = isComputer;
  }

  @Override
  public String getPlayerName() {
    log.append(String.format("getPlayerName called for: %s\n", name));
    return name;
  }

  @Override
  public int getCurrentSpaceIndex() {
    log.append(String.format("getCurrentSpaceIndex called for: %s\n", name));
    return currentSpaceIndex;
  }

  @Override
  public void setCurrentSpaceIndex(int spaceIndex) {
    log.append(String.format("setCurrentSpaceIndex called with: %d for: %s\n", spaceIndex, name));
    this.currentSpaceIndex = spaceIndex;
  }

  @Override
  public List<Item> getItems() {
    log.append(String.format("getItems called for: %s\n", name));
    return new ArrayList<>(items);
  }

  @Override
  public boolean addItem(Item item) {
    log.append(String.format("addItem called with: %s for: %s\n", item.getItemName(), name));
    if (maxItems < 0 || items.size() < maxItems) {
      items.add(item);
      return true;
    }
    return false;
  }

  @Override
  public String lookAround(List<Space> spaces, List<Player> players, TargetCharacter target,
      Pet pet) {
    log.append(String.format("lookAround called for: %s\n", name));
    return String.format("%s looked around current space", name);
  }

  @Override
  public String getDescription(List<Space> spaces) {
    log.append(String.format("getDescription called for: %s\n", name));
    return String.format("Mock player %s in space %d with %d items", name, currentSpaceIndex,
        items.size());
  }

  @Override
  public void move(int index) {
    log.append(String.format("move called with index: %d for: %s\n", index, name));
    this.currentSpaceIndex = index;
  }

  @Override
  public String takeTurn(List<Space> spaces, List<Player> players, TargetCharacter target, Pet pet,
      Boolean canAttack) {
    log.append(String.format("takeTurn called for: %s\n", name));
    if (isComputer) {
      return "Computer player took turn";
    }
    throw new UnsupportedOperationException("Human player cannot use takeTurn method");
  }

  @Override
  public String attack(String itemName, TargetCharacter target) {
    log.append(String.format("attack called with item: %s for: %s\n", itemName, name));
    if ("poke".equals(itemName)) {
      target.takeDamage(1);
      return String.format("%s poked target, caused 1 damage", name);
    }

    Item attackItem = null;
    for (Item item : items) {
      if (item.getItemName().equals(itemName)) {
        attackItem = item;
        items.remove(item);
        break;
      }
    }

    if (attackItem == null) {
      throw new IllegalArgumentException("Item not found in player's inventory");
    }

    target.takeDamage(attackItem.getDamage());
    return String.format("%s attacked with %s, caused %d damage", name, itemName,
        attackItem.getDamage());
  }

  @Override
  public String limitedInfo(List<Space> spaces) {
    log.append(String.format("limitedInfo called for: %s\n", name));
    return String.format("%s is in space %d", name, currentSpaceIndex);
  }

  @Override
  public Player copy() {
    log.append(String.format("copy called for: %s\n", name));
    MockPlayer copy = new MockPlayer(log, name, currentSpaceIndex, maxItems, isComputer);
    for (Item item : items) {
      copy.addItem(item.copy());
    }
    return copy;
  }

  @Override
  public Boolean isComputer() {
    log.append(String.format("isComputer called for: %s\n", name));
    return isComputer;
  }

  /**
   * Gets the log of all actions performed by this mock player.
   *
   * @return the action log as a String
   */
  public String getLog() {
    return log.toString();
  }
}