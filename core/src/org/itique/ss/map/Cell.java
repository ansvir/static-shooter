package org.itique.ss.map;

import org.itique.ss.map.object.AbstractObject;

public class Cell {

    private int x;
    private int y;
    private AbstractObject object;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell(int x, int y, AbstractObject object) {
        this.x = x;
        this.y = y;
        this.object = object;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public AbstractObject getObject() {
        return object;
    }

    public void setObject(AbstractObject object) {
        this.object = object;
    }
}
