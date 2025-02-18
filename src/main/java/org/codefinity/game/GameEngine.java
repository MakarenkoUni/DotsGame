package org.codefinity.game;
import java.util.*;

public class GameEngine {
    public Grid grid;
    private Deque<Dot> currentChain;
    private int points=0;
    private int movesLeft = 30;
    private int level = 6; // Start with 6x6 grid
    private final int MAX_SIZE = 10; // Max grid size before stopping
    public boolean isANeighbour(int x, int y) {
        try{
        if (currentChain.isEmpty()) return false; // ✅ Nothing selected yet

        Dot lastDot = currentChain.peek();
        int lastX = lastDot.getX();
        int lastY = lastDot.getY();

        // ✅ Check direct neighbors ONLY (no diagonals)
        if (x == lastX && y == lastY + 1) return true; // Below
        else if (x == lastX && y == lastY - 1) return true; // Above
        else if (y == lastY && x == lastX + 1) return true; // Right
        else if (y == lastY && x == lastX - 1) return true; // Left
        else return false;
    } catch (NullPointerException e) {
        System.out.println("❌ Neighbor check failed: NullPointerException");
        return false;
    }
    }


    public GameEngine() {
        grid = new Grid(level);
        currentChain = new ArrayDeque<>();
    }

    // Select a dot (adds to the chain if it's the same color)
    public boolean selectDot(int x, int y) {
        Dot selected = grid.getDot(x, y);

        if (selected == null) {
            System.out.println("❌ ERROR: selectDot() failed - No dot found at " + x + ", " + y);
            return false;
        }

        System.out.println("🔎 `selectDot()` selecting: " + selected.getX() + ", " + selected.getY());

        if (currentChain.isEmpty()) {
            currentChain.push(selected);
            System.out.println("✅ First dot selected in backend: " + selected.getX() + ", " + selected.getY());
            return true;
        }

        Dot lastDot = currentChain.peek();
        System.out.println("🔍 Last selected in backend: " + lastDot.getX() + ", " + lastDot.getY());

        if (isANeighbour(x, y) && lastDot.getColor().equals(selected.getColor())) {
            currentChain.push(selected);
            System.out.println("✅ Dot added to chain in backend: " + selected.getX() + ", " + selected.getY());
            return true;
        } else {
            System.out.println("❌ Dot is not a neighbor or wrong color!");
        }

        currentChain.clear();  // Reset if invalid
        return false;
    }


    int pointsGained(int n) {
        if (n <= 0) return 0; // ✅ Base case to stop recursion
        return 2 * (n + pointsGained(n - 1));
    }

    // Finish move (remove chain & check for level progression)
    public void finishMove() {
        if (currentChain.size() > 1) {
            points += pointsGained(currentChain.size());
            grid.removeDots(currentChain);
            movesLeft--;

            // 🔄 NEW: Ensure dots update positions after falling
            grid.updateDotPositions();
        }
        currentChain.clear();  // Reset the chain

        // Check if it's time for the next level
        if (movesLeft <= 0 && level < MAX_SIZE) {
            levelUp();
        }
    }


    // Advance to the next level
    private void levelUp() {
        level++;  // Increase grid size
        grid = new Grid(level);  // Create a new grid with a larger size
        movesLeft = 30;  // Reset move count
        System.out.println("🎉 Level Up! Now playing on a " + level + "x" + level + " grid.");
    }

    // Check if the game is over (reached max size)
    public boolean isGameOver() {
        return movesLeft <= 0 && level >= MAX_SIZE;
    }

    // Print the grid
    public void displayBoard() {
        grid.printGrid();
    }
    public void clearSelection() {
        currentChain.clear(); // ✅ Reset internal tracking of selected dots
    }

    public int getPoints() {
        return points;
    }
}
