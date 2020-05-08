package edu.cmu.cs.cs214.hw4.core;

import java.awt.Color;

public class Player {
    private int numOfMeeples;
    private Color color;
    private static final int NUMBER_OF_MEEPLES = 7;

    /**
     * constructor with a parameter
     * @param color the color representation of this player
     */
    Player(Color color) {
        numOfMeeples = NUMBER_OF_MEEPLES;
        this.color = color;
    }

    /**
     * gets the color of the player
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * the player places a meeple on the game board.
     */
    public void sendMeeple() {
        numOfMeeples--;
    }

    /**
     * the player withdraw a meeple from the board
     */
    public void withdrawMeeple() {
        numOfMeeples++;
    }

    /**
     * whether the player has meeple left or not
     * @return true or false
     */
    public boolean hasMeeple() {
        return numOfMeeples > 0;
    }

    /**
     * gets the number of meeples left
     * @return the number of meeples left
     */
    public int getNumOfMeeples() {
        return numOfMeeples;
    }

    @Override
    public String toString() {
        if (Color.BLACK.equals(color)) {
            return "Black";
        } else if (Color.PINK.equals(color)) {
            return "Pink";
        } else if(Color.YELLOW.equals(color)) {
            return "Yellow";
        } else if(Color.ORANGE.equals(color)) {
            return "Orange";
        } else if(Color.BLUE.equals(color)) {
            return "Blue";
        }
        return null;
    }
}
