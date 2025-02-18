package org.codefinity.game;

import java.util.*;

public class Grid {
    private int size;

    private Dot[][] board;

    public Grid(int size) {
        this.size = size;
        board = new Dot[size][size];

        // Fill the grid with random dots
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                board[x][y] = new Dot(x, y);
            }
        }
    }

    // Display the board in text form
    public void printGrid() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                System.out.print(board[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Get a dot at position (x, y)
    public Dot getDot(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return board[x][y];
        }
        return null;
    }

    // Remove dots in a chain and shift others down
    public void removeDots(Deque<Dot> chain) {
        // Step 1: Remove selected dots
        for (Dot dot : chain) {
            board[dot.getX()][dot.getY()] = null;
        }

        // Step 2: Apply gravity - Shift dots down
        for (int x = 0; x < size; x++) {
            List<Dot> columnDots = new ArrayList<>();

            // Collect all existing dots in this column
            for (int y = size - 1; y >= 0; y--) {
                if (board[x][y] != null) {
                    columnDots.add(board[x][y]);
                }
            }

            // Step 3: Refill column from bottom-up, maintaining order
            for (int y = size - 1; y >= 0; y--) {
                if (!columnDots.isEmpty()) {
                    board[x][y] = columnDots.remove(0);
                } else {
                    board[x][y] = new Dot(x, y); // Generate new dots at the top
                }
            }
        }

        // Step 4: Ensure all dots have correct X, Y positions after falling
        updateDotPositions();
    }

    public void updateDotPositions() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board[x][y] != null) {
                    board[x][y].setX(x);
                    board[x][y].setY(y);
                }
            }
        }
        System.out.println("🔄 Dots updated their coordinates!");
    }

    public int getSize() {
        return size;
    }
}
