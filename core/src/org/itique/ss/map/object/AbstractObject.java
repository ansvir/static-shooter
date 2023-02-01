package org.itique.ss.map.object;

public abstract class AbstractObject {

    private int x;
    private int y;
    private ObjectType type;
    private int degreePx;

    public AbstractObject(int x, int y, ObjectType type, int degreePx) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.degreePx = degreePx;
    }

    public AbstractObject(int x, int y, ObjectType type) {
        this.x = x;
        this.y = y;
        this.type = type;
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

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public int getDegreePx() {
        return degreePx;
    }

    public void setDegreePx(int degreePx) {
        this.degreePx = degreePx;
    }

}
