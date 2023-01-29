package org.itique.ss.map;

import java.util.stream.IntStream;

public class DefaultMap {

    private static final int MAP_WIDTH = 20;
    private static final int MAP_HEIGHT = 20;

    public static Cell[][] getMap() {
        Cell[][] cells = new Cell[MAP_WIDTH][MAP_HEIGHT];
        IntStream.range(0, MAP_HEIGHT).boxed()
                .forEach(i -> IntStream.range(0, MAP_WIDTH).boxed()
                        .forEach(j -> cells[i][j] = new Cell(i ,j)));
        return cells;
    }

}
