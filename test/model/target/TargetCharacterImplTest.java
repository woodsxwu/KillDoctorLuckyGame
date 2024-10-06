package model.target;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


/**
 * JUnit tests for TargetCharacter.
 */
public class TargetCharacterImplTest {

  private TargetCharacterImpl targetCharacter;
  
  @Before
  public void setUp() {
    targetCharacter = new TargetCharacterImpl("Hero", 100);
  }
  
  @Test
  public void testValidInput() {
    assertEquals("Hero", targetCharacter.getTargetName());
    assertEquals(100, targetCharacter.getHealth());
    assertEquals(0, targetCharacter.getCurrentSpaceIndex());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullTargetName() {
    new TargetCharacterImpl(null, 100);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyTargetName() {
    new TargetCharacterImpl("", 100);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeHealth() {
    new TargetCharacterImpl("Hero", -1);
  }
  
  @Test
  public void testMove() {
    targetCharacter.move(5); // Should move to space index 1
    assertEquals(1, targetCharacter.getCurrentSpaceIndex());
    targetCharacter.move(5); // Should move to space index 2
    assertEquals(2, targetCharacter.getCurrentSpaceIndex());
    targetCharacter.move(5); // Should move to index 3
    assertEquals(3, targetCharacter.getCurrentSpaceIndex());
  }
  
  @Test
  public void testTakeDamage() {
    targetCharacter.takeDamage(30);
    assertEquals(70, targetCharacter.getHealth());
  }
  
  @Test
  public void testTakeDamageToZeroHealth() {
    targetCharacter.takeDamage(100);
    assertEquals(0, targetCharacter.getHealth());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTakeDamage_negativeDamage() {
    targetCharacter.takeDamage(-10);
  }
  
  @Test
  public void testCopy() {
    TargetCharacter copy = targetCharacter.copy();
    assertEquals(targetCharacter.getTargetName(), copy.getTargetName());
    assertEquals(targetCharacter.getHealth(), copy.getHealth());
    assertEquals(targetCharacter.getCurrentSpaceIndex(), copy.getCurrentSpaceIndex());

    assertNotSame(targetCharacter, copy);
  }
  
  @Test
  public void testSetCurrentSpaceIndex() {
    targetCharacter.setCurrentSpaceIndex(3);
    assertEquals(3, targetCharacter.getCurrentSpaceIndex());
  }
  
  @Test
  public void testEquals() {
    TargetCharacterImpl anotherCharacter = new TargetCharacterImpl("Hero", 100);
    assertTrue(targetCharacter.equals(anotherCharacter));
  }
  
  @Test
  public void testHashCode() {
    TargetCharacterImpl anotherCharacter = new TargetCharacterImpl("Hero", 100);
    assertEquals(targetCharacter.hashCode(), anotherCharacter.hashCode());
  }
  
  @Test
  public void testToString() {
    String expectedString = "TargetCharacter{name='Hero', health=100, currentSpaceIndex=0}";
    assertEquals(expectedString, targetCharacter.toString());
  }
}

