package model.pet;

public class PetImpl implements Pet {

  private String petName;
  
  private int currentSpaceIndex;
  
  /**
   * Constructs a new PetImpl instance.
   *
   * @param name the name of the pet
   * @param currentSpaceIndex the starting space index for the pet
   * @throws IllegalArgumentException if name is null or empty, or if currentSpaceIndex is negative
   */
  public PetImpl(String name, int currentSpaceIndex) {
      if (name == null || name.trim().isEmpty()) {
          throw new IllegalArgumentException("Pet name cannot be null or empty");
      }
      if (currentSpaceIndex < 0) {
          throw new IllegalArgumentException("Space index cannot be negative");
      }
      this.petName = name;
      this.currentSpaceIndex = currentSpaceIndex;
  }
  
  @Override
  public int getCurrentSpaceIndex() {
    return this.currentSpaceIndex;
  }

  @Override
  public String getPetName() {
    return this.petName;
  }

  @Override
  public Pet copy() {
    return new PetImpl(this.petName, this.currentSpaceIndex);
  }

  @Override
  public void setSpaceIndex(int spaceIndex) {
    if (spaceIndex < 0) {
      throw new IllegalArgumentException("Space number cannot be negative");
    }
    this.currentSpaceIndex = spaceIndex;
  }

  @Override
  public String getPetDescription() {
    String description = "Pet: " + this.petName + "\n";
    return description;
  }

}
