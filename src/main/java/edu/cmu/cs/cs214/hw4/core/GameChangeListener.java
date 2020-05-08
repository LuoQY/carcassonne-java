package edu.cmu.cs.cs214.hw4.core;

import java.util.List;
import java.util.Map;

public interface GameChangeListener {

    /**
     * Change current player.
     * @param player the next player to play
     */
    void currentPlayerChanged(Player player);

    /**
     * End the game and print the final scores.
     * @param scoreboard the scoreboard to be printed
     */
    void gameEnded(Map<Player, Integer> scoreboard);

    /**
     * rotate a tile image by 90 degree.
     */
    void rotateImage();

    /**
     * draw a rectangle(meeple) on a tile image in certain direction
     * @param dir the direction
     */
    void drawMeeple(Direction dir);

    /**
     * updates scoreboard
     */
    void updateScoreboard();

    /**
     * changes the next tile image
     * @param tile the next tile to be placed
     */
    void switchPendingImage(Tile tile);

    /**
     * ends a round and removes the meeples that already got credits
     * @param list list of meeple to be removed
     */
    void roundEnded(List<Coordinate> list);

    /**
     * pops up a notification dialogue box
     * @param str the string to be shown to users
     */
    void notificationDialog(String str);

    /**
     * updates the number of meeples owned by each player
     */
    void updateMeepleNum();

}
