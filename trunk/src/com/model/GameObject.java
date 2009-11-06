package com.model;

import org.newdawn.slick.Graphics;

/**
 * 
 * @author Erick Zanardo
 *
 */
public abstract class GameObject {
    protected int x;
    protected int y;
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics g, int[] extraSize);
}
