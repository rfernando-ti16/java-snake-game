package com.model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * 
 * @author Erick Za
 *
 */
public class Block extends GameObject{

    /** Imagem do bloco */
    private Image block;

    public Block(int x, int y, Image image) {
        super(x, y);
        this.block = image;
    }

    public void draw(Graphics g, int[] extraSize) {
        block.draw((x * block.getWidth()) + extraSize[1], (y * block.getHeight()) + extraSize[0]);
    }

}
