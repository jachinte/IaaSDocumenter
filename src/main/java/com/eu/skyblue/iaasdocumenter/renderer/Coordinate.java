package com.eu.skyblue.iaasdocumenter.renderer;

/**
 * Represents a coordinate for an artefact on a diagram.
 * Based on http://stackoverflow.com/questions/13434681/how-to-search-an-array-of-coordinates-in-java
 */
public class Coordinate {
    private int x;
    private int y;

    /**
     * Constructs a new <code>Coordinate</code> object.
     *
     * @param x   X-Coordinate
     * @param y   Y-Coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Indicate whether this object is equal to the specified object.
     *
     * @param o  The object to be compared.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate point = (Coordinate) o;

        if (x != point.x) return false;
        if (y != point.y) return false;

        return true;
    }

    /**
     * Returns the hashcode for this object.
     */
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * Returns the X-Coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y-Coordinate
     */
    public int getY() {
        return y;
    }
}
