package edu.cmu.cs.cs214.hw4.core;

import javafx.util.Pair;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Board {

    private Map<Coordinate, Tile> board;
    private Set<Coordinate> availablePos;
    private Map<Direction, Coordinate> neighbors;

    private static final int ROAD_SCORE = 1;
    private static final int CITY_SCORE = 2;
    private static final int SHIELD_SCORE = 2;
    private static final int MONASTERY_SCORE = 9;

    /**
     * a simple constructor
     */
    public Board() {
        board = new HashMap<>();
        availablePos = new HashSet<>();
        neighbors  = new HashMap<>();
    }


    /**
     * gets the available position to place a tile on the board
     * @return a set of coordinates
     */
    public Set<Coordinate> getAvailablePos() {
        return availablePos;
    }

    /**
     * place a tile on the given coordinate
     * @param coordinate coordinate to place the tile
     * @param tile the tile to be placed
     * @return true if the tile is placed successfully
     */
    public boolean addTile(Coordinate coordinate, Tile tile) {
        if(board.containsKey(coordinate) || !checkTilePosition(coordinate, tile)) {
            return false;
        }
        board.put(coordinate, tile);
        // update available positions
        coordinate.updateNeighbors();
        availablePos.remove(coordinate);
        for(Coordinate n: coordinate.getNeighbors().values()) {
            if(!board.containsKey(n)) {
                availablePos.add(n);
            }
        }
        return true;
    }

    private boolean checkTilePosition(Coordinate coordinate, Tile curTile) {
        // direction to the current tile
        Map<Direction, Coordinate> surrounding = coordinate.getNeighbors();
        boolean result = true;
        neighbors.clear();
        for(Direction dir: surrounding.keySet()) {
            // the neighbor is not empty
            if(board.containsKey(surrounding.get(dir))) {
                Tile neighbor = board.get(surrounding.get(dir));
                neighbors.put(dir, surrounding.get(dir));
                switch (dir) {
                    case LEFT:
                        result = neighbor.getRight().equals(curTile.getLeft());
                        break;
                    case RIGHT:
                        result = neighbor.getLeft().equals(curTile.getRight());
                        break;
                    case UP:
                        result = neighbor.getDown().equals(curTile.getUp());
                        break;
                    case DOWN:
                        result = neighbor.getUp().equals(curTile.getDown());
                        break;
                    default:
                        return false;
                }
                if(!result)
                    return false;
            }
        }
        return true;
    }

    /**
     * place a meeple on a tile
     * @param tile the tile that contains the meeple
     * @param meeple the meeple to be placed
     * @param dir a certain direction on a tile
     * @return true if the meeple is placed successfully
     */
    public boolean addMeeple(Tile tile, Meeple meeple, Direction dir) {
        Construction tileCenter = tile.getCenter().getConstruction();
        Construction cons = tile.getComponent(dir).getConstruction();
        // only need to check one direction
        if(tileCenter == Construction.INTERSECTION || tileCenter == Construction.VILLAGE
                || (tileCenter == null && cons == Construction.CITY)) {
            if(checkMeeplePosition(tile, dir)) {
                tile.putMeeple(dir, meeple);
                return true;
            }
            return false;
        }
        // check multiple directions
        Set<Direction> dirs = tile.containConstruction(cons);
        for(Direction d: dirs) {
            if(!checkMeeplePosition(tile, d))
                return false;
        }
        for(Direction d: dirs) {
            tile.putMeeple(d, meeple);
        }
        return true;
    }

    private boolean checkMeeplePosition(Tile tile, Direction dir) {
        // the construction which the user wants to put meeple on
        Construction cons = tile.getComponent(dir).getConstruction();
        if(cons == null || cons == Construction.VILLAGE || cons == Construction.INTERSECTION)
            return false;

        // if it is monastery, return true
        if(cons == Construction.MONASTERY)
            return true;

        if(dir == Direction.CENTER) {
            return true;
        }

        // if is Road or City, check if it is already occupied
        Queue<Pair<Direction, Coordinate>> queue = new LinkedList<>();
        Set<Coordinate> visited = new HashSet<>();

        if(!neighbors.containsKey(dir))
            return true;
        queue.add(new Pair<>(dir, neighbors.get(dir)));
        while (!queue.isEmpty()) {
            // neighbor's direction to the current tile and its coordinate
            Pair<Direction, Coordinate> p = queue.poll();
            p.getValue().updateNeighbors();
            visited.add(p.getValue());
            // get neighbor tile
            Tile t = board.get(p.getValue());
            if(t == null) continue;
            // the construction position in the tile
            Set<Direction> dirsConstruction = t.containConstruction(cons);
            if(dirsConstruction.size() == 0)
                continue;
            // if the neighbor is in the left of the tile, check the right edge of the neighbor
            // check the reverse direction of the edges
            Direction consPosition = p.getKey().reverse();
            if(dirsConstruction.contains(consPosition)) {
                // if the construction is already be occupied
                if (t.getComponent(consPosition).getMeeple() != null)
                    return false;
                // decide if the tile should be added to queue to continue to check
                if(dirsConstruction.size() > 1) {
                    if (cons == Construction.ROAD) {
                        if (t.getCenter().getConstruction() != Construction.VILLAGE
                                && t.getCenter().getConstruction() != Construction.INTERSECTION) {
                            for (Direction extendDir : dirsConstruction) {
                                if (extendDir != consPosition) {
                                    Coordinate newNeighbor = p.getValue().getNeighbor(extendDir);
                                    if (visited.contains(newNeighbor))
                                        continue;
                                    queue.add(new Pair<>(extendDir, newNeighbor));
                                }
                            }
                        }
                    }
                    else if (cons == Construction.CITY) {
                        if (t.getCenter().getConstruction() == Construction.CITY) {
                           for(Direction extendDir: dirsConstruction) {
                               if(extendDir != consPosition) {
                                   Coordinate newNeighbor = p.getValue().getNeighbor(extendDir);
                                   if (visited.contains(newNeighbor))
                                       continue;
                                   queue.add(new Pair<>(extendDir, newNeighbor));
                               }
                           }
                        }
                    }
                }
            }

        }
        return true;
    }

    /**
     * count the score for each player occurring in the board
     * @param coordinate coordinate of the start tile to be counted
     * @return a pair contains the scores gained for each player
     * and a list of coordinates that needs to remove the meeples on them
     */
    public Pair<Map<Player, Integer>, List<Coordinate>> countScore(Coordinate coordinate) {
        Map<Player, Integer> counter = new HashMap<>();
        List<Coordinate> tilesToMove = new ArrayList<>();
        // count each construction one by one
        // Road and City can be started from current tile
        // monastery could be in the neighbor tiles

        Tile tile = board.get(coordinate);
        // if there is no tile, end the method
        if(tile == null)
            return null;

        // Road
        Set<Direction> roadSet = tile.containConstruction(Construction.ROAD);
        if (roadSet.size() > 0) {
            for(Direction roadDir: roadSet) {
                Pair<Map<Player, Integer>, List<Coordinate>> scores = countRoadLength(coordinate, roadDir, false);
                // road is completed
                settle(counter, scores);
                if(scores != null)
                    tilesToMove.addAll(scores.getValue());
            }
        }

        // City
        Set<Direction> citySet = tile.containConstruction(Construction.CITY);
        if(citySet.size() > 0) {
            // cities connect in the center
            if(tile.getCenter().getConstruction() == Construction.CITY) {
                Pair<Map<Player, Integer>, List<Coordinate>> scores = countCityLength(coordinate, null, false);
                settle(counter, scores);
                if(scores != null)
                    tilesToMove.addAll(scores.getValue());
            } else { // two cities, count separately
                for(Direction dir: citySet) {
                    Pair<Map<Player, Integer>, List<Coordinate>> scores = countCityLength(coordinate, dir, false);
                    settle(counter, scores);
                    if(scores != null)
                        tilesToMove.addAll(scores.getValue());
                }
            }
        }

        // Monastery
        if(tile.getCenter().getConstruction() == Construction.MONASTERY) {
            if (isMonasteryComplete(coordinate)) {
                Meeple meeple = tile.getCenter().getMeeple();
                if (meeple != null) {
                    Player p = meeple.getOwner();
                    p.withdrawMeeple();
                    counter.put(p, counter.getOrDefault(p, 0) + MONASTERY_SCORE);
                    tile.withdrawMeeple();
                    tilesToMove.add(coordinate);
                }
            }
        }
        // check monastery in surrounding tiles
        for(Coordinate surr: coordinate.getSurrounding()) {
            if(board.containsKey(surr)
                    && board.get(surr).getCenter().getConstruction() == Construction.MONASTERY
                    && isMonasteryComplete(surr)) {
                Tile t = board.get(surr);
                Meeple meeple = t.getCenter().getMeeple();
                if(meeple != null) {
                    Player p = meeple.getOwner();
                    p.withdrawMeeple();
                    counter.put(p, counter.getOrDefault(p, 0) + MONASTERY_SCORE);
                    t.withdrawMeeple();
                    tilesToMove.add(surr);
                }
            }
        }


        return new Pair<>(counter, tilesToMove);

    }

    private void settle(Map<Player, Integer> counter, Pair<Map<Player, Integer>, List<Coordinate>> scores) {
        if(scores == null)
            return;
        for (Player p : scores.getKey().keySet()) {
            p.withdrawMeeple();
            counter.put(p, counter.getOrDefault(p, 0) + scores.getKey().get(p));
        }
        // remove meeples from the board
        for(Coordinate t: scores.getValue()) {
            board.get(t).withdrawMeeple();
        }
    }

    private Pair<Map<Player, Integer>, List<Coordinate>> countRoadLength(Coordinate coor, Direction dir, boolean finalCount) {
        Map<Player, Integer> players = new HashMap<>();
        int maxNum = 0;
        List<Coordinate> meeples = new ArrayList<>();
        //int count = 1;
        Queue<Pair<Direction, Coordinate>> queue = new LinkedList<>();
        queue.add(new Pair<>(dir, coor));
        Set<Coordinate> visited = new HashSet<>();

        while(!queue.isEmpty()) {
            Pair<Direction, Coordinate> p = queue.poll();
            if(visited.contains(p.getValue())) {  // find a loop
                Tile t = board.get(p.getValue());
                if(t.getCenter().getConstruction() == Construction.INTERSECTION
                        || t.getCenter().getConstruction() == Construction.VILLAGE) {
                    Meeple m = t.getComponent(p.getKey().reverse()).getMeeple();
                    if(m != null) {
                        players.put(m.getOwner(), players.getOrDefault(m.getOwner(), 0) + 1);
                        maxNum = Math.max(maxNum, players.get(m.getOwner()));
                        meeples.add(p.getValue());
                    }
                }
                break;
            }
            Coordinate coordinate = p.getValue();
            Tile t = board.get(coordinate);
            // road is incomplete
            if(t == null) {
                if(finalCount)
                    continue;
                return null;
            }
            p.getValue().updateNeighbors();
            visited.add(p.getValue());
            // check meeple's owner
            Meeple m;
            if(visited.size() == 1)
                m = t.getComponent(p.getKey()).getMeeple();
            else
                m = t.getComponent(p.getKey().reverse()).getMeeple();
            if(m != null) {
                players.put(m.getOwner(), players.getOrDefault(m.getOwner(), 0) + 1);
                maxNum = Math.max(maxNum, players.get(m.getOwner()));
                meeples.add(coordinate);
            }

            Set<Direction> roadDirs = t.containConstruction(Construction.ROAD);
            // find one end
            if(roadDirs.size() == 1
                    || t.getCenter().getConstruction() == Construction.INTERSECTION
                    || t.getCenter().getConstruction() == Construction.VILLAGE) {
                //count++;
                Direction d = p.getKey();
                Coordinate neighbor = p.getValue().getNeighbor(d);
                if(visited.size() == 1)  // if this tile is a start tile of the road
                    queue.add(new Pair<>(d, neighbor));
                continue;
            }
            for(Direction d: roadDirs) {
                if(visited.size() == 1) {
                    if (d != Direction.CENTER && !visited.contains(p.getValue().getNeighbor(d))) {
                        queue.add(new Pair<>(d, p.getValue().getNeighbor(d)));
                    }
                } else {
                    if (d != Direction.CENTER && d != p.getKey().reverse()) {
                        queue.add(new Pair<>(d, p.getValue().getNeighbor(d)));
                    }
                }
            }
            //count++;
        }
        Map<Player, Integer> map = new HashMap<>();
        for(Player p: players.keySet()) {
            // only add the player(s) with the maximum number
            if(players.get(p) == maxNum)
                map.put(p, visited.size() * ROAD_SCORE);
        }
        return new Pair<>(map, meeples);
    }

    private Pair<Map<Player, Integer>, List<Coordinate>> countCityLength(Coordinate coor, Direction dir, boolean finalCount) {
        Map<Player, Integer> playerCounter = new HashMap<>();
        int maxNum = 0;
        int shieldNum = 0;
        List<Coordinate> meeplelist = new ArrayList<>();
        Set<Coordinate> visited = new HashSet<>();
        visited.add(coor);
        Queue<Pair<Direction, Coordinate>> queue = new LinkedList<>(); // the neighbor's direction to current tile

        Tile t = board.get(coor);
        if(t.isShield())
            shieldNum++;
        if(dir == null) {  // count multiple directions because city connects in the center
            Set<Direction> citySet = t.containConstruction(Construction.CITY);
            for(Direction d: citySet) {
                // add neighbors to the queue
                queue.add(new Pair<>(d, coor.getNeighbor(d)));
            }
            Meeple meeple = t.getComponent(citySet.iterator().next()).getMeeple();
            if(meeple != null) {
                Player p = meeple.getOwner();
                playerCounter.put(p, playerCounter.getOrDefault(p, 0) + 1);
                maxNum = Math.max(maxNum, playerCounter.get(p));
                meeplelist.add(coor);
            }
        } else {
            queue.add(new Pair<>(dir, coor.getNeighbor(dir)));
            Meeple meeple = t.getComponent(dir).getMeeple();  // juts check one direction
            if(meeple != null) {
                Player p = meeple.getOwner();
                playerCounter.put(p, playerCounter.getOrDefault(p, 0) + 1);
                maxNum = Math.max(maxNum, playerCounter.get(p));
                meeplelist.add(coor);
            }

        }

        while(!queue.isEmpty()) {
            Pair<Direction, Coordinate> pair = queue.poll();
            if(visited.contains(pair.getValue())) {
                continue;
            }
            Tile neighbor = board.get(pair.getValue());
            if(neighbor == null) { // city is incomplete
                if(finalCount)
                    continue;
                return null;
            }
            pair.getValue().updateNeighbors();
            visited.add(pair.getValue());
            if(neighbor.isShield())
                shieldNum++;
            if(neighbor.getCenter().getConstruction() != Construction.CITY) { // find one end
                Meeple meeple = neighbor.getComponent(pair.getKey().reverse()).getMeeple();
                if(meeple != null) {
                    Player p = meeple.getOwner();
                    playerCounter.put(p, playerCounter.getOrDefault(p, 0) + 1);
                    maxNum = Math.max(maxNum, playerCounter.get(p));
                    meeplelist.add(pair.getValue());
                }
            } else {
                Meeple meeple = neighbor.getComponent(pair.getKey().reverse()).getMeeple();
                if(meeple != null) {
                    Player p = meeple.getOwner();
                    playerCounter.put(p, playerCounter.getOrDefault(p, 0) + 1);
                    maxNum = Math.max(maxNum, playerCounter.get(p));
                    meeplelist.add(pair.getValue());
                }
                Set<Direction> extensionSet = neighbor.containConstruction(Construction.CITY);
                for(Direction d: extensionSet) {
                    if(d != pair.getKey().reverse() && d != Direction.CENTER)
                        queue.add(new Pair<>(d, pair.getValue().getNeighbor(d)));
                }
            }
        }
        Map<Player, Integer> map = new HashMap<>();
        for(Player p: playerCounter.keySet()) {
            // only add the player(s) with the maximum number
            if(playerCounter.get(p) == maxNum) {
                if(finalCount)
                    map.put(p, visited.size() * CITY_SCORE/2 + shieldNum * SHIELD_SCORE/2);
                else
                    map.put(p, visited.size() * CITY_SCORE + shieldNum * SHIELD_SCORE);
            }

        }
        return new Pair<>(map, meeplelist);
    }

    /**
     * check if a monastery feature is completed
     * @param coor the coordinate of the tile to be checked
     * @return true if the monastery is completed
     */
    private boolean isMonasteryComplete(Coordinate coor) {
        coor.updateNeighbors();
        Set<Coordinate> surrounding = coor.getSurrounding();
        for(Coordinate c: surrounding) {
            if(!board.containsKey(c))
                return false;
        }
        return true;
    }

    /**
     * Final scoring for the monastery
     * @param coor the coordinate of the tile
     * @return the score of the current tile about monastery
     */
    private int finalCountMonastery(Coordinate coor) {
        coor.updateNeighbors();
        Set<Coordinate> surrounding = coor.getSurrounding();
        int count = 1;
        for(Coordinate c: surrounding) {
            if(board.containsKey(c))
                count++;
        }
        return count;
    }

    /**
     * Final scoring
     * @return the players and corresponding score
     */
    public Map<Player, Integer> finalCount() {
        Map<Player, Integer> counter = new HashMap<>();
        for(Coordinate coordinate: board.keySet()) {
            Tile tile = board.get(coordinate);
            // if tile has no meeple, skip to the next tile
            if(!tile.hasMeeple())
                continue;

            // Road
            Set<Direction> roadSet = tile.containConstruction(Construction.ROAD);

            for(Direction roadDir: roadSet) {
                Pair<Map<Player, Integer>, List<Coordinate>> temp = countRoadLength(coordinate, roadDir, true);
                // road is completed
                settle(counter, temp);
            }

            // City
            Set<Direction> citySet = tile.containConstruction(Construction.CITY);
            if(citySet.size() > 0) {
                // city connect in the center
                if(tile.getCenter().getConstruction() == Construction.CITY) {
                    Pair<Map<Player, Integer>, List<Coordinate>> scores = countCityLength(coordinate, null, true);
                    settle(counter, scores);
                } else { // one or two cities, count separately
                    for(Direction dir: citySet) {
                        Pair<Map<Player, Integer>, List<Coordinate>> scores = countCityLength(coordinate, dir, true);
                        settle(counter, scores);
                    }
                }
            }

            // Monastery
            if(tile.getCenter().getConstruction() == Construction.MONASTERY
                    && tile.getCenter().getMeeple() != null) {
                int score = finalCountMonastery(coordinate);
                Meeple meeple = tile.getCenter().getMeeple();
                if(meeple != null) {
                    Player p = meeple.getOwner();
                    p.withdrawMeeple();
                    counter.put(p, counter.getOrDefault(p, 0) + score);
                    tile.getCenter().setMeeple(null);
                }
            }

        }
        return counter;
    }

}
