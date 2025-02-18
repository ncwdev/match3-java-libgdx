package com.ncwdev.match3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardInitializer {
    public static Color[][] generateValidBoard(int width, int height) {
        Color[][] board;
        do {
            board = createInitialBoard(width, height);
        } while (hasAnyMatches(board) || !hasPossibleMoves(board));

        return board;
    }

    private static Color[][] createInitialBoard(int width, int height) {
        Color[][] board = new Color[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[x][y] = getValidColor(board, x, y);
            }
        }
        return board;
    }

    private static Color getValidColor(Color[][] board, int x, int y) {
        List<Color> forbidden = new ArrayList<>();

        // Prevent horizontal matches
        if (x >= 2 && board[x-1][y] == board[x-2][y]) {
            forbidden.add(board[x-1][y]);
        }

        // Prevent vertical matches
        if (y >= 2 && board[x][y-1] == board[x][y-2]) {
            forbidden.add(board[x][y-1]);
        }

        List<Color> available = new ArrayList<>(Arrays.asList(GameConfig.COLORS));
        available.removeAll(forbidden);

        return available.isEmpty() ? 
            getRandomColor() : 
            available.get(MathUtils.random(available.size() - 1));
    }

    private static boolean hasAnyMatches(Color[][] board) {
        // Horizontal check
        for (int y = 0; y < board[0].length; y++) {
            for (int x = 0; x < board.length - 2; x++) {
                if (board[x][y] == board[x+1][y] && board[x][y] == board[x+2][y]) {
                    return true;
                }
            }
        }
        // Vertical check
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length - 2; y++) {
                if (board[x][y] == board[x][y+1] && board[x][y] == board[x][y+2]) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasPossibleMoves(Color[][] board) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                // Check right neighbor
                if (x < board.length - 1) {
                    if (checkSwap(board, x, y, x+1, y)) return true;
                }
                // Check bottom neighbor
                if (y < board[0].length - 1) {
                    if (checkSwap(board, x, y, x, y+1)) return true;
                }
            }
        }
        return false;
    }

    private static boolean checkSwap(Color[][] board, int x1, int y1, int x2, int y2) {
        // Swap temporarily
        Color temp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = temp;

        boolean hasMatch = checkLocalMatches(board, x1, y1) || checkLocalMatches(board, x2, y2);

        // Swap back
        board[x2][y2] = board[x1][y1];
        board[x1][y1] = temp;

        return hasMatch;
    }

    private static boolean checkLocalMatches(Color[][] board, int x, int y) {
        // Horizontal check
        int left = Math.max(x - 2, 0);
        int right = Math.min(x + 2, board.length - 1);
        for (int i = left; i <= right - 2; i++) {
            if (board[i][y] == board[i+1][y] && board[i][y] == board[i+2][y]) {
                return true;
            }
        }
        // Vertical check
        int bottom = Math.max(y - 2, 0);
        int top = Math.min(y + 2, board[0].length - 1);
        for (int j = bottom; j <= top - 2; j++) {
            if (board[x][j] == board[x][j+1] && board[x][j] == board[x][j+2]) {
                return true;
            }
        }
        return false;
    }

    public static Color getRandomColor() {
        return GameConfig.COLORS[MathUtils.random(GameConfig.COLORS.length - 1)];
    }
}
