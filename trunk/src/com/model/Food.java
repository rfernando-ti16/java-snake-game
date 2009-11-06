package com.model;

import org.newdawn.slick.Graphics;

public class Food extends GameObject{

    public Food(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g, int[] extraSize) {
        g.drawOval((x * 10) + extraSize[1], (y * 10) + extraSize[0], 10, 10);
    }

}
