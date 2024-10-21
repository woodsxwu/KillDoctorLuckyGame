# Kill Doctor Lucky Milestone2

author: Zhixiao Wu<br>
mail: wu.zhixia@northeastern.edu<br><br>

#####How to use JAR file:
1. Download and unzip the project.<br>
2. Go to killDoctorLucky/res and open a terminal in the folder<br>
3. Type in following command "java -jar kill_doctor_lucky.jar my_mansion.txt 10", you can replace the number with the max turn number you want.<br>
4. Now you can interact with the program, type "help" for instructions.<br>

#####Setup Phase Commands:

  add-human <player-name> <starting-space> <item-carrying-capacity>--
    Adds a human player to the game. 
    (If no carrying capacity limitation, please use -1.)
    Example: add-human Alice "Living Room" 5

  add-computer <player-name> <starting-space> <item-carrying-capacity>--
    Adds a computer-controlled player to the game.
    Example: add-computer Bob Kitchen 3

  map--
    Creates the world map.

  help--
    Displays this help message.

  start--
    Start the game (only available after adding players)<br>
    
#####Gameplay Commands:

  move <space-name>--
    Moves the current player to the specified space.
    Example: move DiningRoom

  pick <item-name>--
    Attempts to pick up the specified item in the current space.
    Example: pick Knife

  look--
    Displays information about the current space and neighboring spaces.

  space <space-name>--
    Displays detailed information about the specified space.
    Example: space Kitchen

  player-info <player-name>--
    Displays information about the specified player.
    Example: player-info Alice

  help--
    Displays this help message.

  quit--
    Ends the game.
<br><br>


#####example runs:
#####1. res/example_runs/example_run1: This run is the valid run
adding a human-controlled player to the world: 25-26 <br>
adding a computer-controlled player to the world: 27-28 <br>
the player moving around the world: 80-84<br>
the player picking up an item: 100-101 <br>
the player looking around: 64-74<br>
taking turns between multiple players: you can see that turn switches between Alice and Bob<br>
displaying the description of a specific player: 112-115<br>
displaying information about a specific space in the world: 86-97<br>
creating and saving a graphical representation of the world map to the current directory: 29-31<br>
demonstrates the game ending after reaching the maximum number of turns: 151<br>

#####2. res/example_runs/example_run2: This run shows the results of invalid commands
using a nonexist command: 4-5<br>
number of command arguments less than the requirement: 27-28<br>
names(for player, space or item) not wraped with "" leads to number of command arguments exceeded the requirement: 29-30<br>
give arguments to commands requring no arguments: 40-41<br>


#####3. res/example_runs/example_tun3 : This run shows the results of invalid values or restricted inputs
max capacity not an integer: 25-26<br>
space not exist while creating player: 27-28<br>
add a human player whose name already exist: 31-32<br>
add a computer player whose name already exist: 33-34<br>
trying to pick up a item doesn't exist in current space: 88-89<br>
trying to pick up more items than this player's capacity: 91-92<br>
move to a nonexist space: 94-95<br>
move to current space: 97-98<br>
trying to get the information of a nonexist space: 103-104<br>
trying to show the description of a nonexist player: 106-107<br>

#####References:
https://www.codejava.net/ides/eclipse/how-to-create-jar-file-in-eclipse
https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html
https://www.geeksforgeeks.org/variable-arguments-varargs-in-java/
https://www.baeldung.com/java-command-line-arguments
https://docs.github.com/en/desktop/managing-commits/managing-tags-in-github-desktop