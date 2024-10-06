package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import model.world.World;
import model.world.WorldFactory;

/**
 * This is a driver.
 */
public class Driver {
  /**
   * main function.
   * @param args arguments
   */
  public static void main(String[] args) {

    try {
      WorldFactory wf = new WorldFactory();
      System.out.println("Please input file path:");
      Scanner scanner = new Scanner(System.in);
      String filePath = scanner.nextLine();
      FileReader fileReader = new FileReader(filePath);
      World world = wf.createWorld(fileReader);
      
      world.createWorldMap();
      
      // Display the world name and total spaces
      System.out.println("World Name: " + world.getWorldName());
      System.out.println("Total Spaces: " + world.getTotalSpace());
      System.out.println("\n");
      
      // Move the target character around the world
      for (int i = 0; i < world.getTotalSpace(); i++) {
        int currentSpaceIndex = world.getTargetCharacter().getCurrentSpaceIndex();
        System.out.println(currentSpaceIndex);
        // Display information about the current space
        System.out.println(world.getSpaceInfoByIndex(currentSpaceIndex));
        world.moveTargetCharacter();
      }
      scanner.close();
    } catch (IOException e) {
      System.err.println("Error reading the world file: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println("An error occurred: " + e.getMessage());
    }
  }
}

