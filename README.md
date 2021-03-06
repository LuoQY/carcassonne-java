# Carcassonne in Java

This is a course project for 17-214. It is a Java implementation of the game Carcassonne. The rules of this game can be found in the rules.pdf. The program is built in Gradle so you can run it by importing to IntelliJ with Gradle.

### Here are the steps when playing the game:

1.	When running the program, a start window should show up asking users to enter the number of players, so the first step is to input a number between 2 to 5. If users give illegal input, there would be an error message.
2.	Enter the game board window. Click “start game!” button to start the game. On the left side, users can see the order of players, the colors assigned to each player and the number of meeples owned by each player.
3.	A start tile will be put in the center of the board. On the right side, it shows the next tile to be placed with a “rotate” button and “discard” button. Users can click “rotate” button to rotate the tile. If there is no place to put the tile, users can click “discard” to get a new tile. The old tile will be discarded.
4.	Put the tile on the board by click one “+” button. The “+” buttons represent the available positions for tile placement. If the pattern/construction on the tile can’t adjoin the neighbor tiles on the board, then the new tile would not be added to the board. If users put it successfully, a new tile will show up int the position on the board and the next tile picture on the right side will change.
5.	After placing a tile, users have to choose a position to put a meeple or choose to give up placing a meeple. The choice panel is on the right side. If users make no choice, a warning will pop up when users try to place a new tile. If users have no more meeple, a notification will pop up when they try to place a meeple. In this situation, users have to choose “do nothing”. If users choose an invalid position on a tile, like the Fields or Village, a notification will pop up as well. After a meeple is placed successfully, a rectangle filled with the players’ color will appear in the specific position of the tile. 
6.	The current player’s color shows on the top and changes in each round.
7.	The scoreboard shows on the bottom and also changes when scores have any updates.
8.	When there is no more tile in the deck, the game will end automatically, and a final scoreboard will pop up.
 
