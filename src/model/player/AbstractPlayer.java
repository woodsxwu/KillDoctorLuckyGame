package model.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.item.Item;
import model.pet.Pet;
import model.space.Space;
import model.target.TargetCharacter;

/**
 * AbstractPlayer provides a base implementation for the Player interface.
 */
public abstract class AbstractPlayer implements Player {
  protected String name;
  protected int currentSpaceIndex;
  protected List<Item> items;
  protected int maxItems;

  /**
   * Constructs an AbstractPlayer with the given parameters.
   *
   * @param name              the name of the player
   * @param currentSpaceIndex the starting space index for the player
   * @param maxItems          the maximum number of items the player can carry, if
   *                          maxItems is negative, then the player has no limit
   *                          on the number of items
   */
  protected AbstractPlayer(String name, int currentSpaceIndex, int maxItems) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }
    if (currentSpaceIndex < 0) {
      throw new IllegalArgumentException("Invalid starting space index");
    }
    if (maxItems < -1) {
      throw new IllegalArgumentException(
          "Max items must be -1 (unlimited) or a non-negative number");
    }
    this.name = name;
    this.currentSpaceIndex = currentSpaceIndex;
    this.maxItems = maxItems;
    this.items = new LinkedList<>();
  }

  @Override
  public String getPlayerName() {
    return name;
  }

  @Override
  public int getCurrentSpaceIndex() {
    return currentSpaceIndex;
  }

  @Override
  public void setCurrentSpaceIndex(int spaceIndex) {
    this.currentSpaceIndex = spaceIndex;
  }

  @Override
  public List<Item> getItems() {
    List<Item> itemsCopy = new ArrayList<>();
    for (Item item : this.items) {
      itemsCopy.add(item.copy());
    }
    return itemsCopy;
  }

  @Override
  public boolean addItem(Item item) {
    if (maxItems < 0 || items.size() < maxItems) {
      items.add(item);
      return true;
    }
    return false;
  }

  @Override
  public String lookAround(List<Space> spaces, List<Player> players, 
      TargetCharacter target, Pet pet) {
    if (spaces == null) {
      throw new IllegalArgumentException("Space list cannot be null");
    }
    if (spaces.isEmpty()) {
      throw new IllegalStateException("Space list is empty");
    }
    if (currentSpaceIndex < 0 || currentSpaceIndex >= spaces.size()) {
      throw new IndexOutOfBoundsException("Invalid current space index");
    }

    StringBuilder description = new StringBuilder();
    description.append(name).append(" looked around:\n");
    description.append("Current player is in ").append(spaces.get(currentSpaceIndex).getSpaceName())
        .append("\n");
    List<Player> playersInSpace = spaces.get(currentSpaceIndex).getPlayersInSpace(players);
    if (target.getCurrentSpaceIndex() == currentSpaceIndex) {
      description.append(
          String.format("The target character %s is in this space.%n", target.getTargetName()));
    }
    if (playersInSpace.size() > 1) {
      for (Player player : playersInSpace) {
        if (!player.getPlayerName().equals(name)) {
          description.append(player.getPlayerName()).append(" is in the same space\n");
        }
      }
    }
    if (pet.getCurrentSpaceIndex() == currentSpaceIndex) {
      description.append(pet.getPetName()).append(" is in this space\n");
    }
    description.append(spaces.get(currentSpaceIndex).getItemsInfo()).append("\n");
    description.append(spaces.get(currentSpaceIndex).getNeighborInfo(spaces)).append("\n");
    List<Integer> neighborIndices = spaces.get(currentSpaceIndex).getNeighborIndices();
    for (int neighborIndex : neighborIndices) {
      Space neighbor = spaces.get(neighborIndex);
      if (neighborIndex == pet.getCurrentSpaceIndex()) {
        description.append("Space: ").append(neighbor.getSpaceName()).append(":\n");
        description.append(pet.getPetName()).append(" is in ").append(neighbor.getSpaceName())
        .append(", you can't take your eyes off it.").append("\n\n");
      } else {
        description.append(String.format("Space: %s%n", neighbor.getSpaceName()));
        description.append(neighbor.getItemsInfo());
        if (target.getCurrentSpaceIndex() == neighbor.getSpaceIndex()) {
          description.append(
              String.format("The target character %s is in this space.%n", target.getTargetName()));
        }
        description.append(neighbor.getPlayersInfo(players)).append("\n");
      }
    }
    return description.toString();
  }

  @Override
  public String getDescription(List<Space> spaces) {
    StringBuilder description = new StringBuilder();
    description.append("Name: ").append(name).append("\n");

    if (spaces == null || spaces.isEmpty()) {
      description.append("Current Space: Unknown (The world is empty)\n");
    } else if (currentSpaceIndex >= spaces.size()) {
      description.append("Current Space: Unknown (Player is lost)\n");
    } else {
      description.append("Current Space: ").append(spaces.get(currentSpaceIndex).getSpaceName())
          .append("\n");
    }

    description.append("Items: ");
    if (items.isEmpty()) {
      description.append("None");
    } else {
      for (Item item : items) {
        description.append(item.getItemName()).append(", ");
      }
      description.setLength(description.length() - 2); // Remove last ", "
    }
    return description.toString();
  }

  @Override
  public void move(int spaceIndex) {
    if (spaceIndex < 0) {
      throw new IllegalArgumentException("Invalid space index");
    }
    this.currentSpaceIndex = spaceIndex;
  }
  
  @Override
  public String attack(String itemName, TargetCharacter target) {
    if (itemName == null || itemName.trim().isEmpty()) {
      throw new IllegalArgumentException("Item name cannot be null or empty");
    }
    if (target == null) {
      throw new IllegalArgumentException("Target cannot be null");
    }
    if ("poke".equals(itemName)) {
      pokeEye(target);
      return getPlayerName() + " poked the target in the eye, caused 1 damage, Ouch!";
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
    } else {
      target.takeDamage(attackItem.getDamage());
      StringBuilder description = new StringBuilder();
      description.append(getPlayerName()).append(" attacked the target with ").append(itemName)
          .append(", caused ").append(attackItem.getDamage()).append(" damage.").append("\n");
      return description.toString();
    }
  }
  
  /**
   * Poke the target in the eye.
   *
   * @param target the target to poke
   */
  private void pokeEye(TargetCharacter target) {
    target.takeDamage(1);
  }
  
  @Override
  public String limitedInfo(List<Space> spaces) {
    StringBuilder description = new StringBuilder();
    description.append(name).append(" is currently in: ")
    .append(spaces.get(currentSpaceIndex).getSpaceName()).append("\n")
    .append("space index: ").append(this.currentSpaceIndex).append("\n");
    return description.toString();
  }
  
  @Override
  public Player copy() {
    Player copy = createCopy(); // Let subclasses create their specific type
    // Copy items
    for (Item item : this.items) {
      copy.addItem(item.copy());
    }
    return copy;
  }

  // Abstract method for subclasses to implement their specific copy creation
  protected abstract Player createCopy();
}
