package com.ncwdev.match3;

import com.ncwdev.ecs.Entity;

public class Grid {
    public final int width, height;
    private final Entity[][] cells;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Entity[width][height];
    }

    public Entity get(int x, int y) {
        return isValid(x, y) ? cells[x][y] : null;
    }

    public void set(int x, int y, Entity entity) {
        if (isValid(x, y)) {
            cells[x][y] = entity;
        }
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Entity e = get(x, y);
                if (e == null) {
                    sb.append("nil");
                } else {
                    sb.append(String.format("%3s ", e.getClass().getSimpleName()));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
