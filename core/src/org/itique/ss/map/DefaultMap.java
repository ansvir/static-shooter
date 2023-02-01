package org.itique.ss.map;

import com.badlogic.gdx.graphics.Texture;
import org.itique.ss.map.object.BackgroundObject;
import org.itique.ss.map.object.FloorObject;

import java.util.stream.IntStream;

public class DefaultMap {

    private static final int MAP_WIDTH = 800;
    private static final int MAP_HEIGHT = 800;

    public static Cell[][] getMap() {
        Cell[][] cells = new Cell[MAP_WIDTH][MAP_HEIGHT];
        IntStream.range(0, MAP_WIDTH).boxed()
                .forEach(i -> IntStream.range(0, MAP_HEIGHT).boxed()
                        .forEach(j ->
                            cells[i][j] = j == 0
                                    ? new Cell(i, j, new BackgroundObject(i, j))
                                    : i == 0 ? new Cell(i, j, new BackgroundObject(i, j))
                                    : i == MAP_WIDTH - 1 ? new Cell(i, j, new BackgroundObject(i, j))
                                    : j == MAP_HEIGHT - 1 ? new Cell(i, j, new BackgroundObject(i, j))
                                    : new Cell(i, j, new FloorObject(i, j))));
        return cells;
    }

}
