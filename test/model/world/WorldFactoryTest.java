package model.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import constants.Constants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

/**
 * JUnit tests for WorldFactory.
 */
public class WorldFactoryTest {

  @Test
  public void testCreateWorldWithValidInput() throws IOException {
    WorldFactory wf = new WorldFactory();
    FileReader fileReader = new FileReader(Constants.FILE_PATH);
    World world = wf.createWorld(fileReader);

    assertNotNull(world);
    assertEquals("Doctor Lucky's Mansion", world.getWorldName());
    assertEquals(36, world.getRows());
    assertEquals(30, world.getColumns());
  }

  @Test(expected = FileNotFoundException.class)
  public void testFileNotFound() throws IOException {
    File inputFile = new File("res/nonExistentFile.txt");
    WorldFactory factory = new WorldFactory();
    factory.createWorld(new FileReader(inputFile));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidWorldData() throws IOException {
    File inputFile = new File("res/invalidInput.txt");
    WorldFactory factory = new WorldFactory();
    factory.createWorld(new FileReader(inputFile));
  }
}
