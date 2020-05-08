package edu.cmu.cs.cs214.hw4.core;

import javafx.util.Pair;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class CarcassonneImp implements Carcassonne{

    private Player[] players;
    private TileDeck tiledeck;
    private Board board;
    private int playerNum;
    private Tile curTile;
    private Tile oldTile;
    private Map<Player, Integer> scoreBoard;
    private Coordinate curCoordinate;
    private final List<GameChangeListener> gameChangeListeners  = new ArrayList<>();


    /**
     * a constructor with a parameter
     * @param numOfPlayer the number of player
     */
    public CarcassonneImp(int numOfPlayer) {
        players = new Player[numOfPlayer];
        for(int i = 0; i < numOfPlayer; i++) {
            players[i] = new Player(COLORS[i]);
        }
        tiledeck = new TileDeck();
        board = new Board();
        scoreBoard = new HashMap<>();
    }

    @Override
    public void addGameChangeListener(GameChangeListener listener) {
        gameChangeListeners.add(listener);
    }

    @Override
    public void start() {
        tiledeck.shuffle();
        // put the start tile
        Coordinate ini = new Coordinate(0, 0);
        board.addTile(ini, TileDeck.START_TILE);
        playerNum = 0;
        for(Player p: players) {
            scoreBoard.put(p, 0);
        }
        for (GameChangeListener listener : gameChangeListeners) {
            listener.updateScoreboard();
        }
        notifyPlayerChanged();
    }

    /**
     * switch the current player
     */
    private void switchPlayer() {
        playerNum++;
        playerNum = playerNum % players.length;
        notifyPlayerChanged();
    }

    @Override
    public Player getCurrentPlayer() {
        return players[playerNum];
    }

    @Override
    public void getNewTile() {
        if(tiledeck.hasTile()) {
            oldTile = curTile;
            curTile = tiledeck.getNewTile();
        } else {
            System.out.println("deck has no tile");
            curTile = null;
        }
        for (GameChangeListener listener : gameChangeListeners) {
            listener.switchPendingImage(curTile);
        }

    }

    @Override
    public boolean putTile(int x, int y) {
        curCoordinate = new Coordinate(x, y);
        curCoordinate.updateNeighbors();
        if(curTile != null)
            return board.addTile(curCoordinate, curTile);
        return false;
    }

    @Override
    public boolean putMeeple(Direction direction) {
        if(direction == null) {  // player decides not to put meeple
            oldTile = curTile;
            endRound();
            checkGameEnd();
            switchPlayer();
            return true;
        }
        if (oldTile.hasMeeple())
            return false;
        Construction cons = oldTile.getComponent(direction).getConstruction();
        if(cons == null || cons == Construction.INTERSECTION || cons == Construction.VILLAGE) {
            for (GameChangeListener listener : gameChangeListeners) {
                listener.notificationDialog("The area picked has no construction to be occupied.");
            }
            return false;
        }
        Meeple meeple = new Meeple(getCurrentPlayer());
        if(!getCurrentPlayer().hasMeeple()) {
            for (GameChangeListener listener : gameChangeListeners) {
                listener.notificationDialog("Player " + getCurrentPlayer() + " has no more meeples left.");
            }
            return false;
        }
        // check player side
        if(oldTile != null) {
            if(board.addMeeple(oldTile, meeple, direction)) { // successfully place meeple
                oldTile = curTile;
                getCurrentPlayer().sendMeeple();
                for (GameChangeListener listener : gameChangeListeners) {
                    listener.drawMeeple(direction);
                    listener.updateMeepleNum();
                }
                endRound();
                checkGameEnd();
                switchPlayer();
                return true;
            } else {
                for (GameChangeListener listener : gameChangeListeners) {
                    listener.notificationDialog("The construction has already been occupied.");
                }
            }
        }
        return false;
    }

    @Override
    public Set<Coordinate> getAvailablePos() {
        return board.getAvailablePos();
    }

    @Override
    public String getScoreBoard() {
        List<Player> players = new ArrayList<>(scoreBoard.keySet());
        // sort in descending order
        players.sort((p1, p2) -> scoreBoard.get(p2) - scoreBoard.get(p1));
        StringBuilder res = new StringBuilder("<html>");
        for(Player player: players) {
            res.append(player).append(": ").append(scoreBoard.get(player)).append("<br/>");
        }
        return res.append("</html>").toString();
    }

    @Override
    public void endRound() {
        // update scoreboard
        Pair<Map<Player, Integer>, List<Coordinate>> updates = board.countScore(curCoordinate);
        if(updates == null)
            return;
        Map<Player, Integer> scores = updates.getKey();
        if(scores.size() != 0) {
            for (Player p : scores.keySet()) {
                scoreBoard.put(p, scoreBoard.get(p) + scores.get(p));
            }
        }
        for (GameChangeListener listener : gameChangeListeners) {
            listener.updateScoreboard();
            listener.updateMeepleNum();
            listener.roundEnded(updates.getValue());
        }
    }

    @Override
    public void endGame() {
        Map<Player, Integer> scores = board.finalCount();
        for(Player p: scores.keySet()) {
            scoreBoard.put(p, scoreBoard.get(p) + scores.get(p));
        }
        for (GameChangeListener listener : gameChangeListeners) {
            listener.gameEnded(scoreBoard);
        }
    }

    private void notifyPlayerChanged() {
        for (GameChangeListener listener : gameChangeListeners) {
            listener.currentPlayerChanged(getCurrentPlayer());
        }
    }

    @Override
    public void checkGameEnd() {
        if(curTile == null) {
            endGame();
        }
    }

    @Override
    public void rotateTile() {
        if(curTile == null)
            return;
        curTile.rotate();
        for (GameChangeListener listener : gameChangeListeners) {
            listener.rotateImage();
        }
    }


    @Override
    public String getPlayerOrder() {
        StringBuilder order = new StringBuilder("<html>Order of playing:<br/> ");
        for(int i = 0; i < players.length; i++) {
            order.append(i+1).append(" - ").append(players[i]).append("<br/>");
        }
        order.append("<br/></html>");
        return order.toString();
    }

    @Override
    public String getMeepleNum() {
        StringBuilder order = new StringBuilder("<html>Number of Meeples:<br/> ");
        for(int i = 0; i < players.length; i++) {
            order.append(players[i]).append(": ").append(players[i].getNumOfMeeples()).append("<br/>");
        }
        order.append("</html>");
        return order.toString();
    }


}
