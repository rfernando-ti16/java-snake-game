package com.model;

import org.newdawn.slick.Graphics;

/**
 * Representa um pedaço da cobra
 * 
 * @author Erick Zanardo
 *
 */
public class SnakePiece extends GameObject{
    public SnakePiece(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g, int[] extraSize) {
        g.drawRect((x * 10) + extraSize[1], (y * 10) + extraSize[0], 10, 10);
    }
}
