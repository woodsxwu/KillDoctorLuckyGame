# Kill Doctor Lucky Milestone3

author: Zhixiao Wu<br>
mail: wu.zhixia@northeastern.edu<br><br>

#####How to run JAR file:
1. Download and unzip the project.<br>
2. Go to killDoctorLucky/res and open a terminal in the folder<br>
3. Type in following command "java -jar kill_doctor_lucky.jar fewerRoomsEasyTest.txt 100", you can replace the txt file or the max turn number.<br>
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
The primary assumption is that the pet's presence in a room allows for undetected attacks, regardless of other players present. players must share a space with the target to attack. Players must be in the same space as the pet to move it, and cannot command it to move to their current location. After a manual pet movement, it skips its automatic movement for that turn. computer players only attack when it is possible and only move pet when it is possible, without false attempts.

#####limitations:
The pet follows a predictable DFS movement pattern without true randomness.
The computer player AI employs purely random decision-making without any strategic evaluation or adaptation. 
There's no save/load functionality, no pause system.
space number must be bigger than 2, since pet must be moved to another space.
There is no limit for how many players or items can exist in one space.
The target moves after every player's turn finished, and pet also moves.

#####Design Changes:
One difference is how the pet and visibility mechanics were implemented. Pre design only shows a basic pet system, the actual code features a pet that moves using depth-first search (DFS) and creates "blind spots" in the game, with methods to check if players can be seen or if spaces are visible.<br>
The pre design only shows basic player movement and actions, but the implementation includes detailed information control (what players can see), strategic pet movement, and more sophisticated computer player behavior. The computer players make smarter decisions about movement, item collection, and combat than suggested in the design.<br>

#####example run explanations:
in these tests, I use a simplified map to make attacking and moving pet more likely to happen.
#####1. res/example_runs/example_run1: This run is the valid run which human player wins
1) tells the position of the target character in each turn, so that the user has some way of knowing where the target player is in the world. 42-43 <br>
2) updated look around (multiple player situation will be shown in another run) 45-75<br>
3) the space information is blocked by the pet. 58<br>
4) limited information about where player is in every turn. 39<br>
5) space description is updated with pet included. 142-151<br>
6) human player poke target in the eye and delt 1 damage. 161-162<br>
7) human player attack target with picked item. 403-404<br>
8) human player successfully moved the pet to another space. 619-620<br>
9) attack caused death of target, game over, the human player gave the lethal strike won. 639-643<br>


#####2. res/example_runs/example_run2: This run is the valid run which computer player wins
1) a computer player moving a pet. 73, 991<br>
2) a computer player attacked target, it used the item doing the highest damage. 1011<br>
3) attack caused death of target, game over, the computer player gave the lethal strike won.1013-1015<br>

#####3. res/example_runs/example_run3 : is the valid run which the target character escaping with his life and the game ending
after the attacks, the target remained 1 health and escaped. 139-145<br>

#####4. res/example_runs/example_run4 : is the run shows the effect of pet and being seen, and shows what will happen if try to move pet or attack target from a different space
1) look around can see other players.55-86<br>
2) last turn the pet was in study, and this turn it moves to Kitchen, where alice is in.<br>
Though Bob is in the neighbor space, because the pet blocked the view, Alice's attack succeeded. 97-98<br>
3) without a pet, the attack could be seen by neighbor, and was automatically stopped. 174-175<br>
4) look around can see players in the same space. 197<br>
5) moving the pet from another space fails. 236-237<br>
6) Alice and Bob are in the same space, so the attack would be stopped. 288-289<br>
7) attacking the target from another space fails. 308-309 <br>


#####5. res/example_runs/example_run5 : is the valid run shows the evidence of extra credit
the simplified map is in res/fewerRoomsEasyTest.png<br>
1) the pet enters the game in the same space as the target character. 42, 58<br>
2) you can see that the pet wander from the beginning space to the end space, traverse through the map. 38-246<br>
3) after reaching a space where there is no unvisited space, it trace back to the former space, trying to find another unvisited neighbor space. Sadly it has already visited all spaces, so it behaves like traversing back to the beginning point. 207-418<br>
4) after traced back to the starting point, its track of visited spaces is now empty, so it start a new traverse loop. 422-642<br>
5) the player moved it to another space, its memory cleared so that it can start a new traverse unaffectedly.646-end of the file<br>

#####References:
https://en.wikipedia.org/wiki/Depth-first_search<br>
https://www.codejava.net/ides/eclipse/how-to-create-jar-file-in-eclipse<br>
https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html<br>
https://www.geeksforgeeks.org/variable-arguments-varargs-in-java<br>
https://www.baeldung.com/java-command-line-arguments<br>
https://docs.github.com/en/desktop/managing-commits/managing-tags-in-github-desktop