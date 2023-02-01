package org.itique.ss.map.object;

public class BackgroundObject extends AbstractObject {

    public BackgroundObject(int x, int y, int degreePx) {
        super(x, y, ObjectType.WALL, degreePx);
    }

    public BackgroundObject(int x, int y) {
        super(x, y, ObjectType.WALL);
    }

}
