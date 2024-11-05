package model.pet;

public interface Pet {
  int getCurrentSpaceIndex();

  String getPetName();
  
  Pet copy();
  
  void setSpaceIndex(int spaceIndex);
  
  String getPetDescription();
}
