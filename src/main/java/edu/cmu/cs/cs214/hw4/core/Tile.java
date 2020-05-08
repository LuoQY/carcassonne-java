package edu.cmu.cs.cs214.hw4.core;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Tile {
    private Component left;
    private Component right;
    private Component up;
    private Component down;
    private Component center;
    private boolean shield;
    private boolean hasMeeple;

    /**
     * a simple constructor.
     * @param left the components in the left side
     * @param right the components in the right side
     * @param up the components in the top side
     * @param down the components in the bottom side
     * @param center the components in the center
     * @param shield does the tile have a shield or not
     */
    public Tile(Component left, Component right, Component up, Component down, Component center, boolean shield) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.center = center;
        this.shield = shield;
        hasMeeple = false;
    }

    /**
     * whether the tile already has a meeple on it or not.
     * @return true or false
     */
    public boolean hasMeeple() {
        return hasMeeple;
    }

    /**
     * Returns all the directions that the construction extends to.
     * @param cons construction
     * @return a set of directions
     */
    public Set<Direction> containConstruction(Construction cons) {
        Set<Direction> dirs = new HashSet<>();
        if(left.getConstruction() == cons)
            dirs.add(Direction.LEFT);
        if(right.getConstruction() == cons)
            dirs.add(Direction.RIGHT);
        if(up.getConstruction() == cons)
            dirs.add(Direction.UP);
        if(down.getConstruction() == cons)
            dirs.add(Direction.DOWN);
        if(center.getConstruction() == cons)
            dirs.add(Direction.CENTER);
        return dirs;
    }

    /**
     * rotate this tile.
     */
    public void rotate() {
        Component temp = left;
        left = down;
        down = right;
        right = up;
        up = temp;
    }

    /**
     * gets the construction and meeple on the tile given a certain direction
     * @param dir direction
     * @return the component in the direction
     */
    public Component getComponent(Direction dir) {
        switch (dir) {
            case LEFT:
                return left;
            case RIGHT:
                return right;
            case UP:
                return up;
            case DOWN:
                return down;
            case CENTER:
                return center;
            default:
                return null;
        }
    }

    /**
     * get the component in the left side.
     * @return component
     */
    public Component getLeft() {
        return left;
    }

    /**
     * get the component in the right side.
     * @return component
     */
    public Component getRight() {
        return right;
    }

    /**
     * get the component in the up side.
     * @return component
     */
    public Component getUp() {
        return up;
    }

    /**
     * get the component in the down side.
     * @return component
     */
    public Component getDown() {
        return down;
    }

    /**
     * get the component in the center.
     * @return component
     */
    public Component getCenter() {
        return center;
    }

    /**
     * does the tile contain a shield or not
     * @return true or false
     */
    public boolean isShield() {
        return shield;
    }

    /**
     * place a meeple on the tile in certain direction
     * @param dir direction
     * @param meeple meeple to be placed
     * @return true if meeple is placed successfully
     */
    public boolean putMeeple(Direction dir, Meeple meeple) {
        boolean result = true;
        switch (dir) {
           case LEFT:
               if (left.getMeeple() == null && left.getConstruction() != null)
                   left.setMeeple(meeple);
               else
                   result = false;
               break;
           case RIGHT:
               if (right.getMeeple() == null && right.getConstruction() != null)
                   right.setMeeple(meeple);
               else
                   result = false;
               break;
           case UP:
               if (up.getMeeple() == null && up.getConstruction() != null)
                   up.setMeeple(meeple);
               else
                   result = false;
               break;
           case DOWN:
               if (down.getMeeple() == null && down.getConstruction() != null)
                   down.setMeeple(meeple);
               else
                   result = false;
               break;
           case CENTER:
               if (center.getMeeple() == null && center.getConstruction() != null)
                   center.setMeeple(meeple);
               else
                   result = false;
               break;
            default:
                result = false;
       }
       if(result) {
           hasMeeple = true;
       }
       return result;
   }

    /**
     * remove a meeple from the tile.
     */
    public void withdrawMeeple() {
        left.setMeeple(null);
        right.setMeeple(null);
        up.setMeeple(null);
        down.setMeeple(null);
        center.setMeeple(null);
        hasMeeple = false;
    }

   @Override
    public boolean equals(Object o) {
        if(o instanceof Tile) {
            Tile t = (Tile) o;
            return (left == t.left || left.equals(t.left)) && (right == t.right || right.equals(t.right))
                    && (up == t.up || up.equals(t.up)) && (down == t.down || down.equals(t.down))
                    && (center == t.center || center.equals(t.center)) && shield == t.shield;
        }
        return false;
   }

   @Override
    public int hashCode() {
       return Objects.hash(left, right, up, down, center, shield);
   }

}
