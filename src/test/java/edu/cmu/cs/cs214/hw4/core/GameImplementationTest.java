package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.*;

public class GameImplementationTest {
    Carcassonne game;

    @Before
    public void setUp() {
        game = new CarcassonneImp(3);
        game.start();
    }

    @Test
    public void playerTest() {
        assertEquals(game.getCurrentPlayer().getColor(), Color.black);
    }
    @Test
    public void putTileTest() {
        assertFalse(game.putTile(0, 0));
        assertEquals(game.getAvailablePos().size(), 4);
        game.getNewTile();
        if(game.putTile(1, 0))
            assertEquals(game.getAvailablePos().size(), 6);
        else
            assertEquals(game.getAvailablePos().size(), 4);
    }

    @Test
    public void putMeepleTest() {
        assertTrue(game.putMeeple(null));
    }

}
