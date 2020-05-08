package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileDeck {
    private List<Tile> deck;
    private int tileNumber = NUMBER_OF_TILES - 2;
    private static final int NUMBER_OF_TILES = 72;
    static final Tile START_TILE = new Tile(new Component(), new Component(Construction.CITY),
            new Component(Construction.ROAD), new Component(Construction.ROAD),
            new Component(Construction.ROAD), false);

    /**
     * a constructor to construct the whole deck of tiles
     */
    public TileDeck() {
        deck = new ArrayList<>(NUMBER_OF_TILES - 1);


        for(int i = 0; i < 1; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), true));
        }
        for(int i = 0; i < 3; i++) {
            deck.add(new Tile(new Component(), new Component(Construction.CITY),
                    new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.ROAD), false));
        }
        for(int i = 0; i < 2; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(), new Component(), new Component(Construction.CITY), true));
        }
        for(int i = 0; i < 1; i++) {
            deck.add(new Tile(new Component(), new Component(), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.CITY), false));
        }
        for(int i = 0; i < 3; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(), new Component(), new Component(), false));
        }
        for(int i = 0; i < 2; i++) {
            deck.add(new Tile(new Component(), new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), new Component(), false));
        }
        for(int i = 0; i < 3; i++) {
            deck.add(new Tile(new Component(), new Component(Construction.ROAD),
                    new Component(Construction.CITY), new Component(Construction.ROAD), new Component(), false));
        }
        for(int i = 0; i < 3; i++) {
            deck.add(new Tile(new Component(Construction.ROAD), new Component(Construction.CITY),
                    new Component(Construction.ROAD), new Component(), new Component(), false));
        }
        for(int i = 0; i < 3; i++) {
            deck.add(new Tile(new Component(Construction.ROAD), new Component(Construction.CITY),
                    new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.INTERSECTION), false));
        }
        for(int i = 0; i < 2; i++) {
            deck.add( new Tile(new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), true));
        }
        for(int i = 0; i < 3; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), false));
        }
        for(int i = 0; i < 2; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), true));
        }
        for(int i = 0; i < 3; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), false));
        }
        for(int i = 0; i < 1; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), true));
        }
        for(int i = 0; i < 3; i++) {
            deck.add( new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), false));
        }
        for(int i = 0; i < 2; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), true));
        }
        for(int i = 0; i < 1; i++) {
            deck.add(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), false));
        }
        for(int i = 0; i < 8; i++) {
            deck.add(new Tile(new Component(), new Component(), new Component(Construction.ROAD),
                    new Component(Construction.ROAD), new Component(Construction.ROAD), false));
        }

        for(int i = 0; i < 4; i++) {
            deck.add(new Tile(new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(), new Component(Construction.ROAD),
                    new Component(Construction.VILLAGE), false));
        }
        for(int i = 0; i < 1; i++) {
            deck.add(new Tile(new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.VILLAGE), false));
        }

        for(int i = 0; i < 9; i++) {
            deck.add(new Tile(new Component(Construction.ROAD), new Component(), new Component(),
                    new Component(Construction.ROAD), new Component(), false));
        }
        for(int i = 0; i < 5; i++) {
            deck.add(new Tile(new Component(), new Component(), new Component(Construction.CITY),
                    new Component(), new Component(), false));
        }

        for(int i = 0; i < 4; i++) {
            deck.add(new Tile(new Component(), new Component(), new Component(),
                    new Component(), new Component(Construction.MONASTERY), false));
        }
        for(int i = 0; i < 2; i++) {
            deck.add(new Tile(new Component(), new Component(), new Component(),
                    new Component(Construction.ROAD), new Component(Construction.MONASTERY), false));
        }
    }

    /**
     * shuffle the tile deck.
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * get a new tile from the deck
     * @return a new tile
     */
    public Tile getNewTile() {
        System.out.println("deck has # of tiles:" + (tileNumber+1));
        return deck.get(tileNumber--);
    }

    /**
     * determine if the deck has any more tile
     * @return true or false
     */
    public boolean hasTile() {
        return tileNumber >= 0;
    }
}
