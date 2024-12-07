# Kill Doctor Lucky Milestone3

author: Zhixiao Wu<br>
mail: wu.zhixia@northeastern.edu<br><br>

#####How to run JAR:
1. Download and unzip the project.<br>
2. Go to killDoctorLucky/res and open a terminal in the folder<br>
3. Type in following command "java -jar kill_doctor_lucky.jar my_mansion.txt 100 gui", if you want to run the gui version of game. Or you can type in "java -jar kill_doctor_lucky.jar my_mansion.txt 100 gui"if you want to use the text based version. you can replace the txt file or the max turn number.<br>
4. Now you can interact with the program, click "help" menu for instructions.<br>

#####How to use JAR:
######Mouse Controls:
Click on a space: Move to that space<br>
Click on a player: View player information<br>
######Keyboard Controls:
P key: Pick up item in current space<br>
L key: Look around current space<br>
A key: Attempt to attack Dr. Lucky<br>
M key: Move the pet<br>
I key: View player information<br>
#######Menu Options:
Game Menu: Start new game, load world, quit<br>
Help Menu: View these instructions and controls<br>
Quit: quit game<br>
<br><br>

#####assumptions:
Using view model to retrieve the copies of models instead of there references to avoid potential mutations. The game can only have up to 10 players. If a user chose a file to play, that file become the current world and would be opened in the next default game.

#####limitations:
The game ui is not neat enough. Results are shown in a text panel in a consequence, might be difficult to track them. Once the game is over, user cannot review the game result details.

#####Design Changes:
Created gui for gameplay. Updated controller to support gui behaviors. Assigned listeners to the gui. Created a new interface called ViewModel and made WorldImpl to implement it, so that the view can use this interface to get copies of models. 

#####References:
https://en.wikipedia.org/wiki/Depth-first_search<br>
https://www.codejava.net/ides/eclipse/how-to-create-jar-file-in-eclipse<br>
https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html<br>
https://www.geeksforgeeks.org/variable-arguments-varargs-in-java<br>
https://www.baeldung.com/java-command-line-arguments<br>
https://docs.github.com/en/desktop/managing-commits/managing-tags-in-github-desktop