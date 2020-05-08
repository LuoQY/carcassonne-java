package edu.cmu.cs.cs214.hw4.core;

public enum Construction {
    CITY("CITY"),
    ROAD("ROAD"),
    MONASTERY("MONASTERY"),
    INTERSECTION("INTERSECTION"),
    VILLAGE("VILLAGE");

    Construction(String s) {
        symbol = s;
    }

    private final String symbol;

    @Override
    public String toString() {
        return symbol;
    }

}
