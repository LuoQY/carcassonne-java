package edu.cmu.cs.cs214.hw4.core;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Objects;

public class Coordinate {
    private final int x;
    private final int y;
    private final Map<Direction, Coordinate> neighbors;
    private final Set<Coordinate> surrounding;

    /**
     * Constructor with two parameters
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        neighbors = new HashMap<>();
        surrounding = new HashSet<>();
    }

    /**
     * update the surrounding coordinates around this coordinate
     */
    public void updateNeighbors() {
        Coordinate left = new Coordinate(x-1, y);
        Coordinate right = new Coordinate(x+1, y);
        Coordinate up = new Coordinate(x, y+1);
        Coordinate down = new Coordinate(x, y-1);
        neighbors.put(Direction.LEFT, left);
        neighbors.put(Direction.RIGHT, right);
        neighbors.put(Direction.UP, up);
        neighbors.put(Direction.DOWN, down);
        neighbors.put(Direction.CENTER, this);
        surrounding.add(left);
        surrounding.add(right);
        surrounding.add(up);
        surrounding.add(down);
        surrounding.add(new Coordinate(x-1, y+1));
        surrounding.add(new Coordinate(x+1, y+1));
        surrounding.add(new Coordinate(x+1, y-1));
        surrounding.add(new Coordinate(x-1, y-1));
    }

    /**
     * get the neighbor coordinates and their directions to this coordinate
     * @return a hash map with neighbor coordinates and directions
     */
    public Map<Direction, Coordinate> getNeighbors() {
        return neighbors;

    }

    /**
     * gets a neighbor coordinate given a certain direction
     * @param dir the direction of the neighbor
     * @return the neighbor coordinate
     */
    public Coordinate getNeighbor(Direction dir) {
        return neighbors.get(dir);
    }

    /**
     * gets all the coordinates surrounding this coordinate
     * @return a hash set of surrounding coordinates
     */
    public Set<Coordinate> getSurrounding() {
        return surrounding;
    }

    /**
     * gets value of x-coordinate
     * @return value of x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * gets value of y-coordinate
     * @return value of y-coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Coordinate) {
            return x == ((Coordinate) object).x && y == ((Coordinate) object).y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
