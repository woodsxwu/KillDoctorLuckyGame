package model.item;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for ItemImpl.
 */
public class ItemImplTest {
  
  private ItemImpl item;
  
  @Before
  public void setUp() {
    item = new ItemImpl("Sword", 10, 1);
  }
  
  @Test
  public void testValidInput() {
    assertEquals("Sword", item.getItemName());
    assertEquals(10, item.getDamage());
    assertEquals(1, item.getSpaceIndex());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullItemName() {
    new ItemImpl(null, 10, 1);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyItemName() {
    new ItemImpl("", 10, 1);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeDamage() {
    new ItemImpl("Sword", -1, 1);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeSpaceIndex() {
    new ItemImpl("Sword", 10, -1);
  }
  
  @Test
  public void testCopy() {
    Item copiedItem = item.copy();
    assertEquals(item.getItemName(), copiedItem.getItemName());
    assertEquals(item.getDamage(), copiedItem.getDamage());
    assertEquals(item.getSpaceIndex(), copiedItem.getSpaceIndex());
    assertEquals(false, copiedItem == item);
  }
  
  @Test
  public void testEquals() {
    ItemImpl anotherItem = new ItemImpl("Sword", 10, 1);
    anotherItem.equals(item);
  }
  
  @Test
  public void testHashCode() {
    ItemImpl anotherItem = new ItemImpl("Sword", 10, 1);
    assertEquals(item.hashCode(), anotherItem.hashCode());
  }
  
  @Test
  public void testToString() {
    String expectedString = "Item{name='Sword', damage=10, spaceIndex=1}";
    assertEquals(expectedString, item.toString());
  }
}