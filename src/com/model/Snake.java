package com.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a cobra controlada
 * 
 * @author Erick Zanardo
 *
 */
public class Snake {
    private List<SnakePiece> snakePieces;
    private Direction direction;
    
    public Snake() {
        snakePieces = new ArrayList<SnakePiece>();
    }

    public List<SnakePiece> getSnakePieces() {
        return snakePieces;
    }

    public void setSnakePieces(List<SnakePiece> snakePieces) {
        this.snakePieces = snakePieces;
    }
    
    public void addSnakePiece(SnakePiece sp) {
        snakePieces.add(sp);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
