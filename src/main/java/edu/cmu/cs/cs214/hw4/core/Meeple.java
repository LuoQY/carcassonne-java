package edu.cmu.cs.cs214.hw4.core;

public class Meeple {
    private Player owner;

    /**
     * constructor with one parameter
     * @param player the owner of the meeple
     */
    public Meeple(Player player) {
        owner = player;
    }

    /**
     * gets the owner of the meeple
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }
}
