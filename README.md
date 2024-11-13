# Kill Doctor Lucky Milestone2

author: Zhixiao Wu<br>
mail: wu.zhixia@northeastern.edu<br><br>

#####How to run JAR file:
1. Download and unzip the project.<br>
2. Go to killDoctorLucky/res and open a terminal in the folder<br>
3. Type in following command "java -jar kill_doctor_lucky.jar fewerRoomsEasyTest.txt 100", you can replace the number with the max turn number you want.<br>
4. Now you can interact with the program, type "help" for instructions.<br>

#####How to use JAR file:
######Setup Phase Commands:

  add-human player-name starting-space item-carrying-capacity--
    Adds a human player to the game. 
    (If no carrying capacity limitation, please use -1.)
    Example: add-human Alice "Living Room" 5

  add-computer player-name starting-space item-carrying-capacity--
    Adds a computer-controlled player to the game.
    Example: add-computer Bob Kitchen 3

  map--
    Creates the world map.

  help--
    Displays this help message.

  start--
    Start the game (only available after adding players)<br>
    
######Gameplay Commands:

  move space-name--
    Moves the current player to the specified space.
    Example: move DiningRoom

  pick item-name--
    Attempts to pick up the specified item in the current space.
    Example: pick Knife

  look--
    Displays information about the current space and neighboring spaces.

  <span style="color:red">attack item-name--
    Attempt to attack the target character with the chosen item.</span><br><br>

  <span style="color:red">move-pet space-name--
    Move the pet to to a specified space.</span>

  space space-name--
    Displays detailed information about the specified space.
    Example: space Kitchen

  player-info player-name--
    Displays information about the specified player.
    Example: player-info Alice

  help--
    Displays this help message.

  quit--
    Ends the game.
<br><br>

#####assumptions:

#####limitations:

#####example run explanations:
in these tests, I use a simplified map to make attacking and moving pet more likely to happen.
#####1. res/example_runs/example_run1: This run is the valid run which human player wins
1) tells the position of the target character in each turn, so that the user has some way of knowing where the target player is in the world. 42-43 <br>
2) updated look around (multiple player situation will be shown in another run) 45-75<br>
3) the space information is blocked by the pet. 58<br>
4) limited information about where player is in every turn. 39<br>
5) space description is updated with pet included. 142-151<br>
6) player poke target in the eye and delt 1 damage. 161-162<br>
7) player attack target with picked item. 403-404<br>
8) player successfully moved the pet to another space. 619-620<br>
9) attack caused death of target, game over, the player gave the lethal strike won. 639-643<br>


#####2. res/example_runs/example_run2: This run is the valid run which computer player wins


#####3. res/example_runs/example_run3 : is the valid run which the target character escaping with his life and the game ending


#####4. res/example_runs/example_run4 : is the run shows the fail attempts to attack and move pet


#####5. res/example_runs/example_run5 : is the valid run shows the evidence of extra credit


#####References:
https://www.codejava.net/ides/eclipse/how-to-create-jar-file-in-eclipse
https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html
https://www.geeksforgeeks.org/variable-arguments-varargs-in-java/
https://www.baeldung.com/java-command-line-arguments
https://docs.github.com/en/desktop/managing-commits/managing-tags-in-github-desktop