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

}
