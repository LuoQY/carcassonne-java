package edu.cmu.cs.cs214.hw4.core;

import java.awt.Color;
import java.util.Set;

public interface Carcassonne {
    Color[] COLORS = {Color.BLACK, Color.PINK, Color.YELLOW, Color.ORANGE, Color.BLUE};

    /**
     * Register a game change listener to be notified of game change events.
     *
     * @param listener The listener to be notified of game change events.
     */
    void addGameChangeListener(GameChangeListener listener);

    /**
     * Start the game
     */
    void start();

    /**
     * get current player
     * @return current player
     */
    Player getCurrentPlayer();

    /**
     * get a new tile from the deck
     */
    void getNewTile();

    /**
     * put a tile on the board
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the tile is put successfully
     */
    boolean putTile(int x, int y);


    /**
     * place a meeple on certain direction of a tile
     * @param dir direction
     * @return true if meeple is placed successfully
     */
    boolean putMeeple(Direction dir);

    /**
     * get the available position on the board
     * @return a set of positions
     */
    Set<Coordinate> getAvailablePos();

    /**
     * get scoreboard
     * @return score board
     */
    String getScoreBoard();

    /**
     * a round of the game ends
     */
    void endRound();

    /**
     * game over
     */
    void endGame();

    /**
     * rotate a tile
     */
    void rotateTile();

    /**
     * get the order of playing
     * @return a string that contains the order
     */
    String getPlayerOrder();

    /**
     * check if the game ends or not
     */
    void checkGameEnd();

    /**
     * get the number of meeples owned by each player
     * @return a string that contains the numbers and player's color
     */
    String getMeepleNum();
}
