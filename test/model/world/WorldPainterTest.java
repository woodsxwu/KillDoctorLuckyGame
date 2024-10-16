package model.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.item.Item;
import model.space.Space;
import model.space.SpaceImpl;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit tests for WorldPainter.
 */
public class WorldPainterTest {

  private List<Space> spaces;
  private WorldPainter worldPainter;

  /**
   * Create objects before each test.
   */
  @Before
  public void setUp() {
    spaces = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    List<Integer> neighbors = new ArrayList<>();
    spaces.add(new SpaceImpl(0, "Space1", 0, 0, 2, 2, items, neighbors));
    spaces.add(new SpaceImpl(1, "Space2", 3, 3, 5, 5, items, neighbors));

    worldPainter = new WorldPainter(spaces, 10, 10);
  }

  @Test
  public void testValidInput() {
    new WorldPainter(spaces, 10, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSpaces() {
    new WorldPainter(null, 10, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeDimensions() {
    new WorldPainter(spaces, -1, -1);
  }

  @Test
  public void testCreateImage() throws IOException {
    BufferedImage image = worldPainter.createImage(30, 100);

    // Check if the generated image is non-null
    assertNotNull("Image should not be null", image);
    
    // Check if image dimensions are as expected
    assertEquals("Image width should be correct", 400, image.getWidth());
    assertEquals("Image height should be correct", 400, image.getHeight());
  }
}


