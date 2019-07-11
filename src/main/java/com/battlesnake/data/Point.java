package com.battlesnake.data;

public class Point {
    public int x;
    public int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point(int[] c) {
        this.x = c[0];
        this.y = c[1];
    }

    public boolean theSame(int[] target) {
        return x == target[0] && y == target[1];
    }

    public int[] leftOf() {
        return new int[] {x-1, y};
    }
    public int[] rightOf() {
        return new int[] {x+1, y};
    }
    public int[] upOf() {
        return new int[] {x, y-1};
    }
    public int[] downOf() {
        return new int[] {x, y+1};
    }
    public Point move(Move selectedMove) {
        switch (selectedMove) {
        case DOWN:
            return new Point(x, y + 1);
        case UP:
            return new Point(x, y - 1);
        case LEFT:
            return new Point(x - 1, y);
        case RIGHT:
            return new Point(x + 1, y);
        }
        return this;
    }
    public int[] get() {
        return new int[] {x, y};
    }

}
