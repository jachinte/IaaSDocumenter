package com.eu.skyblue.iaasdocumenter.renderer;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 03/07/15
 * Time: 01:15
 * To change this template use File | Settings | File Templates.
 */

// Based on http://stackoverflow.com/questions/13434681/how-to-search-an-array-of-coordinates-in-java
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate point = (Coordinate) o;

        if (x != point.x) return false;
        if (y != point.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
