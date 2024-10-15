package model.player;


import java.util.Random;

/**
 * A custom random number generator that can be used for both gameplay and testing.
 * It can either generate truly random numbers or return a predefined sequence of numbers.
 */
public class RandomGenerator {
  private Random random;
  private int[] predefinedNumbers;
  private int currentIndex;

  /**
   * Default constructor that uses Java's Random class for truly random numbers.
   */
  public RandomGenerator() {
    this.random = new Random();
    this.predefinedNumbers = null;
  }

  /**
   * Constructor that accepts a set of predefined numbers to be returned in sequence.
   * 
   * @param numbers the predefined numbers to be returned
   */
  public RandomGenerator(int... numbers) {
    this.predefinedNumbers = numbers;
    this.currentIndex = 0;
  }

  /**
   * Generates the next random or predefined number.
   * 
   * @return the next integer in the sequence or a random integer
   */
  public int nextInt() {
    if (predefinedNumbers != null) {
      int result = predefinedNumbers[currentIndex];
      currentIndex = (currentIndex + 1) % predefinedNumbers.length;
      return result;
    } else {
      return random.nextInt();
    }
  }

  /**
   * Generates the next random or predefined number within a specified range.
   * 
   * @param bound the upper bound (exclusive) for the random number
   * @return the next integer in the sequence or a random integer within the specified range
   */
  public int nextInt(int bound) {
    if (predefinedNumbers != null) {
      int result = predefinedNumbers[currentIndex] % bound;
      currentIndex = (currentIndex + 1) % predefinedNumbers.length;
      return Math.abs(result);
    } else {
      return random.nextInt(bound);
    }
  }
}
