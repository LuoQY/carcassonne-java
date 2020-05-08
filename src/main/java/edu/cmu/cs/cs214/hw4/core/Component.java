package edu.cmu.cs.cs214.hw4.core;

import java.util.Objects;

public class Component {
    private Construction construction;
    private Meeple meeple;

    /**
     * a simple constructor
     */
    public Component() {
        construction = null;
        meeple = null;
    }

    /**
     * a constructor with a construction parameter
     * @param cons construction
     */
    public Component(Construction cons) {
        construction = cons;
        meeple = null;
    }

    /**
     * set the meeple parameter
     * @param meeple meeple
     */
    public void setMeeple(Meeple meeple) {
        this.meeple = meeple;
    }

    /**
     * get the construction parameter
     * @return construction
     */
    public Construction getConstruction() {
        return construction;
    }

    /**
     * get the meeple parameter
     * @return meeple
     */
    public Meeple getMeeple() {
        return meeple;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Component) {
            Component com = (Component) o;
            return construction == com.construction;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(construction);
    }

}
