package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TileDeckTest {
    TileDeck deck;

    @Before
    public void setUp() {
        deck = new TileDeck();
    }

    @Test
    public void startTileTest() {
        assertEquals(TileDeck.START_TILE, new Tile(new Component(), new Component(Construction.CITY),
                new Component(Construction.ROAD), new Component(Construction.ROAD),
                new Component(Construction.ROAD), false));
    }


}
