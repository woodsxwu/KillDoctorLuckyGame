package model.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.item.Item;
import model.item.ItemImpl;
import model.pet.Pet;
import model.pet.PetImpl;
import model.space.Space;
import model.space.SpaceImpl;
import model.target.TargetCharacter;
import model.target.TargetCharacterImpl;

/**
 * A factory class that reads from a specified text file 
 * and creates a game world based on its content.
 * 
 * This class is responsible for parsing the input file, extracting key values, and instantiating
 * the target character, spaces, and items within the world.
 */
public class WorldFactory {

  private String worldName;
  private int rows;
  private int columns;
  private List<Space> spaces;
  private TargetCharacter targetCharacter;
  private int totalSpaces;
  private int totalItems;
  private Pet pet;
  
  /**
   * Initializes a new instance of the WorldFactory.
   * This constructor sets up the list of spaces.
   */
  public WorldFactory() {
    rows = 0;
    columns = 0;
    spaces = new ArrayList<>();
    totalSpaces = 0;
    totalItems = 0;
  }

  /**
   * Reads key values from the provided input and initializes the fields of the factory.
   * It sets up the target character, spaces, and items based on the input.
   *
   * @param readable the source of input (can be a file or other readable source)
   */
  public void readValuesFromFile(Readable readable) {
    Scanner scanner = new Scanner(readable);
    try {
      readWorld(scanner);
      createTargetCharacter(scanner);
      createPet(scanner);
      createSpaces(scanner);
      createItems(scanner);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Error reading values from file: " + e.getMessage());
    } finally {
      scanner.close();
    }
  }

  /**
   * Reads the world configuration from the input.
   *
   * @param scanner the scanner used to parse the input
   * @throws IllegalArgumentException if rows and columns read are negative or zero,
   *          or the world name read is empty
   */
  private void readWorld(Scanner scanner) {
    int rowsRead = Integer.parseInt(scanner.next());
    int columnsRead = Integer.parseInt(scanner.next());
    String worldNameRead = scanner.nextLine().trim();
    if (rowsRead <= 0 || columnsRead <= 0) {
      throw new IllegalArgumentException("Rows and columns must be positive integers.");
    }
    if (worldNameRead.isEmpty()) {
      throw new IllegalArgumentException("World name cannot be empty.");
    }
    rows = rowsRead;
    columns = columnsRead;
    worldName = worldNameRead;
  }

  /**
   * Creates the target character based on the input values.
   *
   * @param scanner the scanner used to parse the input
   * @throws IllegalArgumentException if health is not positive, 
   *          character name is empty or failed to create target character
   */
  private void createTargetCharacter(Scanner scanner) {
    int health = Integer.parseInt(scanner.next());
    String targetName = scanner.nextLine().trim();
    
    if (health <= 0) {
      throw new IllegalArgumentException("Health must be a positive integer.");
    }
    if (targetName.isEmpty()) {
      throw new IllegalArgumentException("Target character name cannot be empty.");
    }
    
    try {
      targetCharacter = new TargetCharacterImpl(targetName, health);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Failed to create target character: " + e.getMessage());
    }
  }

  private void createPet(Scanner scanner) {
    String petName = scanner.nextLine().trim();
    if (petName.isEmpty()) {
      throw new IllegalArgumentException("Pet name cannot be empty.");
    }
    try {
      pet = new PetImpl(petName, 0);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Failed to create pet: " + e.getMessage());
    }
  }
  
  /**
   * Creates spaces in the world based on the input values.
   *
   * @param scanner the scanner used to parse the input
   * @throws IllegalArgumentException if number of spaces is not positive.
   */
  private void createSpaces(Scanner scanner) {
    int totalSpacesRead = Integer.parseInt(scanner.next());
    if (totalSpacesRead <= 0) {
      throw new IllegalArgumentException("Total spaces must be a positive integer.");
    }
    totalSpaces = totalSpacesRead;
    for (int i = 0; i < totalSpaces; i++) {
      try {
        spaces.add(createSpace(i, scanner));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Error creating space at index " 
      + i + ": " + e.getMessage());
      }
    }
  }

  /**
   * Creates a single space based on the input values.
   *
   * @param index the index of the space being created
   * @param scanner the scanner used to parse the input
   * @return a newly created Space object
   * @throws IllegalArgumentException if invalid coordinates for spaces or space name is empty
   */
  private Space createSpace(int index, Scanner scanner) {
    int upperLeftRow = Integer.parseInt(scanner.next());
    int upperLeftCol = Integer.parseInt(scanner.next());
    int lowerRightRow = Integer.parseInt(scanner.next());
    int lowerRightCol = Integer.parseInt(scanner.next());
    String spaceName = scanner.nextLine().trim();
  
    if (upperLeftRow < 0 || upperLeftCol < 0 
        || lowerRightRow < upperLeftRow || lowerRightCol < upperLeftCol) {
      throw new IllegalArgumentException("Invalid coordinates for space: "
            + "(" + upperLeftRow + ", " + upperLeftCol + ") to (" 
            + lowerRightRow + ", " + lowerRightCol + ").");
    }
    if (spaceName.isEmpty()) {
      throw new IllegalArgumentException("Space name cannot be empty.");
    }
    
    try {
      return new SpaceImpl(index, spaceName, upperLeftRow, upperLeftCol, lowerRightRow, 
          lowerRightCol, new ArrayList<Item>(), new ArrayList<Integer>());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Failed to create space: " + e.getMessage());
    }
  }
  
  /**
   * Creates items in the world based on the input values.
   *
   * @param scanner the scanner used to parse the input
   * @throws IllegalArgumentException if number of items is negative
   */
  private void createItems(Scanner scanner) {
    int totalItemsRead = Integer.parseInt(scanner.next());
    if (totalItemsRead < 0) {
      throw new IllegalArgumentException("Total items cannot be negative.");
    }
    totalItems = totalItemsRead;
    for (int i = 0; i < totalItems; i++) {
      try {
        Item item = createItem(scanner);
        spaces.get(item.getSpaceIndex()).addItem(item);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Error creating item at index "
      + i + ": " + e.getMessage());
      }
    }
  }
  
  /**
   * Creates a single item based on the input values.
   *
   * @param scanner the scanner used to parse the input
   * @return a newly created Item object
   * @throws IllegalArgumentException if space index out of range
   *          , damage be negative, item name be empty, or failed to create item
   */
  private Item createItem(Scanner scanner) {
    int spaceIndex = Integer.parseInt(scanner.next());
    int damage = Integer.parseInt(scanner.next());
    String itemName = scanner.nextLine().trim();
    
    if (spaceIndex < 0 || spaceIndex >= totalSpaces) {
      throw new IllegalArgumentException("Space index must be valid (0 to "
    + (totalSpaces - 1) + ").");
    }
    if (damage < 0) {
      throw new IllegalArgumentException("Damage cannot be negative.");
    }
    if (itemName.isEmpty()) {
      throw new IllegalArgumentException("Item name cannot be empty.");
    }
    
    try {
      return new ItemImpl(itemName, damage, spaceIndex);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Failed to create item: " + e.getMessage());
    }
  }
  
  /**
   * Initializes the fields from the input and creates and returns a WorldImpl instance.
   *
   * @param readable the source of input (can be a file or other readable source)
   * @return a newly created WorldImpl object
   */
  public WorldImpl createWorld(Readable readable) {
    readValuesFromFile(readable);
    try {
      return new WorldImpl(worldName, rows, columns, spaces, targetCharacter, 
          totalSpaces, totalItems, pet);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Error creating world: " + e.getMessage());
    }
  }
}
