package com.ncwdev.match3.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ncwdev.ecs.Entity;
import com.ncwdev.ecs.System;
import com.ncwdev.ecs.World;
import com.ncwdev.match3.GameConfig;
import com.ncwdev.match3.Grid;
import com.ncwdev.match3.components.Position;
import com.ncwdev.match3.components.Tile;
import com.ncwdev.match3.events.MatchesDetectedEvent;
import com.ncwdev.match3.events.SpecialMatchEvent;
import com.ncwdev.match3.events.SwapCompletedEvent;
import com.ncwdev.match3.events.TilesLandedEvent;

public class MatchCheckSystem extends System {
    private static final int MIN_MATCH = 3;
    private final Array<Entity> matchedTiles = new Array<>();

    public MatchCheckSystem(World world) {
        // Listen for swap completion
        world.getEventManager().addListener(SwapCompletedEvent.class, event -> {
            Gdx.app.log("MatchCheckSystem", "Swap completed");

            checkAllMatches();
        });
        world.getEventManager().addListener(TilesLandedEvent.class, event -> {
            checkAllMatches();
        });
    }

    @Override
    public void update(float delta) {
    }

    private void checkAllMatches() {
        matchedTiles.clear();
        Array<Entity> tiles = getTilesWithPosition();
        Grid grid = buildGridFromEntities(tiles);

        // Detect matches
        findHorizontalMatches(grid, tiles);
        findVerticalMatches(grid, tiles);
        // detectSpecialMatches(grid, tiles);

        Gdx.app.log("MatchCheckSystem", "Matched tiles: " + matchedTiles.size);

        // Gdx.app.log("MatchCheckSystem", "Field state:\n" + grid);

        if (matchedTiles.size > 0) {
            world.getEventManager().postEvent(new MatchesDetectedEvent(matchedTiles));
        }
    }

    private Array<Entity> getTilesWithPosition() {
        Array<Entity> tiles = new Array<>();
        for (Entity entity : world.getEntities()) {
            if (entity.has(Position.class) && entity.has(Tile.class)) {
                tiles.add(entity);
            }
        }
        return tiles;
    }

    private Grid buildGridFromEntities(Array<Entity> tiles) {
        Grid grid = new Grid(GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT);
        for (Entity tile : tiles) {
            Position pos = tile.get(Position.class);
            grid.set(pos.x, pos.y, tile);
        }
        return grid;
    }

    // private void findHorizontalMatches(Grid grid, Array<Entity> tiles) {
    //     for (int y = 0; y < grid.height; ++y) {
    //         int currentRun = 1;
    //         Entity previous = null;
    //         for (int x = 0; x < grid.width; ++x) {
    //             Entity current = grid.get(x, y);
    //             if (y < 2) {
    //                 Gdx.app.log("MatchCheckSystem", "Checking tile: " + x + "," + y);
    //             }
    //             if (current != null && colorsMatch(current, previous)) {
    //                 ++currentRun;
    //                 Gdx.app.log("MatchCheckSystem", "Found horizontal match: " + currentRun);
    //                 Position pos1 = previous.get(Position.class);
    //                 Position pos2 = current.get(Position.class);
    //                 Gdx.app.log("MatchCheckSystem", "Previous: " + pos1.x + "," + pos1.y);
    //                 Gdx.app.log("MatchCheckSystem", "Current: " + pos2.x + "," + pos2.y);
    //             } else {
    //                 if (currentRun >= MIN_MATCH) {
    //                     addMatches(grid, x - currentRun, x - 1, y, true);
    //                 }
    //                 currentRun = 1;
    //             }
    //             previous = current;
    //         }
    //     }
    // }

    private void findHorizontalMatches(Grid grid, Array<Entity> tiles) {
        for (int y = 0; y < grid.height; y++) {
            int currentRun = 1;
            Entity previous = null;
    
            for (int x = 0; x < grid.width; x++) {
                Entity current = grid.get(x, y);
                
                if (colorsMatch(current, previous)) {
                    currentRun++;
                } else {
                    checkAndAddMatch(grid, currentRun, x, y, true);
                    currentRun = 1;
                }
                
                previous = current;
            }
            
            // Check for matches at the end of the row
            checkAndAddMatch(grid, currentRun, grid.width, y, true);
        }
    }
    
    private void checkAndAddMatch(Grid grid, int currentRun, int currentX, int fixedY, boolean horizontal) {
        if (currentRun >= MIN_MATCH) {
            int start = currentX - currentRun;
            int end = currentX - 1;
            addMatches(grid, start, end, fixedY, horizontal);
        }
    }

    private void findVerticalMatches(Grid grid, Array<Entity> tiles) {
        for (int x = 0; x < grid.width; x++) {
            int currentRun = 1;
            Entity previous = null;
    
            for (int y = 0; y < grid.height; y++) {
                Entity current = grid.get(x, y);
                
                if (colorsMatch(current, previous)) {
                    currentRun++;
                } else {
                    checkAndAddMatch(grid, currentRun, y, x, false);
                    currentRun = 1;
                }
                
                previous = current;
            }
            
            // Check for matches at the top of the column
            checkAndAddMatch(grid, currentRun, grid.height, x, false);
        }
    }

    private boolean colorsMatch(Entity a, Entity b) {
        if (a == null || b == null) return false;
        // return a.get(Tile.class).color.equals(b.get(Tile.class).color);
        return a.get(Tile.class).typeId == b.get(Tile.class).typeId;
    }

    private void addMatches(Grid grid, int start, int end, int fixedCoord, boolean horizontal) {
        for (int i = start; i <= end; ++i) {
            Entity tile = horizontal ? grid.get(i, fixedCoord) : grid.get(fixedCoord, i);
            if (!matchedTiles.contains(tile, true)) {
                matchedTiles.add(tile);
                tile.get(Tile.class).matched = true;

                Position pos = tile.get(Position.class);
                Gdx.app.log("MatchCheckSystem", "Added match: " + pos.x + "," + pos.y);
            }
        }
    }

    private void detectSpecialMatches(Grid grid, Array<Entity> tiles) {
        // Detect cross-shaped matches (vertical + horizontal intersection)
        for (Entity tile : tiles) {
            Position pos = tile.get(Position.class);
            if (checkCrossMatch(grid, pos.x, pos.y)) {
                Array<Entity> crossMatches = getCrossMatches(grid, pos.x, pos.y);
                world.getEventManager().postEvent(new SpecialMatchEvent(crossMatches, SpecialMatchEvent.MatchType.CROSS));
            }
        }
    }

    private boolean checkCrossMatch(Grid grid, int x, int y) {
        // Check if this tile is part of both vertical and horizontal matches
        return hasHorizontalMatch(grid, x, y) && hasVerticalMatch(grid, x, y);
    }

    private boolean hasVerticalMatch(Grid grid, int x, int y) {
        int top = Math.min(y + 2, grid.height - 1);
        int bottom = Math.max(y - 2, 0);

        for (int i = bottom; i <= top; i++) {
            if (i == y) {
                continue;
            }

            if (!colorsMatch(grid.get(x, i), grid.get(x, y))) {
                return false;
            }
        }

        return true;
    }

    private boolean hasHorizontalMatch(Grid grid, int x, int y) {
        int left = Math.max(x - 2, 0);
        int right = Math.min(x + 2, grid.width - 1);

        for (int i = left; i <= right; i++) {
            if (i == x) {
                continue;
            }

            if (!colorsMatch(grid.get(i, y), grid.get(x, y))) {
                return false;
            }
        }

        return true;
    }

    private Array<Entity> getCrossMatches(Grid grid, int x, int y) {
        Array<Entity> matches = new Array<>();

        // Horizontal line
        for (int dx = x - 2; dx <= x + 2; dx++) {
            if (grid.isValid(dx, y) && colorsMatch(grid.get(dx, y), grid.get(x, y))) {
                matches.add(grid.get(dx, y));
            }
        }
        // Vertical line
        for (int dy = y - 2; dy <= y + 2; dy++) {
            if (grid.isValid(x, dy) && colorsMatch(grid.get(x, dy), grid.get(x, y))) {
                matches.add(grid.get(x, dy));
            }
        }
        return matches;
    }
}
