package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.gui.GameBoardPanel;
import org.junit.Before;
import org.junit.Test;


public class MainTest {
    private GameBoardPanel gameboard;
    private Carcassonne game;

    @Before
    public void testWriteOperator() {
        game = new CarcassonneImp(3);
        gameboard = new GameBoardPanel(game);
    }

    @Test
    public void getNewTileTest() {
        Tile testTile = new Tile(new Component(), new Component(Construction.CITY),
                new Component(Construction.ROAD), new Component(Construction.ROAD),
                new Component(Construction.ROAD), false);
        gameboard.switchPendingImage(testTile);
    }

    @Test
    public void rotateImageTest() {
        getNewTileTest();
        gameboard.rotateImage();
    }

    @Test
    public void updateMeepleTest() {
        gameboard.updateMeepleNum();
    }

}
