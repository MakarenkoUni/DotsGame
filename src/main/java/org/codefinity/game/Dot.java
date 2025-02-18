package org.codefinity.game;

import java.util.Random;

public class Dot {
    private int x, y;  // Grid position
    private String color;  // Dot color

    private static final String[] COLORS = {"RED", "BLUE", "GREEN", "YELLOW", "PURPLE"};

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = getRandomColor();
    }

    private String getRandomColor() {
        Random rand = new Random();
        return COLORS[rand.nextInt(COLORS.length)];
    }

    public String getColor() { return color; }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return color.substring(0, 1);  // Display only first letter (e.g., R for RED)
    }
}
