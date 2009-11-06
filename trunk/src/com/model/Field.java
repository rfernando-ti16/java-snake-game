package com.model;

/**
 * Representa o campo onde a cobra joga
 * 
 * @author Erick Zanardo
 *
 */
public class Field {
    private int width;
    private int height;
    private Object[][] fieldMap; 
    private int[] startPoint; 

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        fieldMap = new Object[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Object[][] getFieldMap() {
        return fieldMap;
    }

    public void setObjectOnField(Object o, int x, int y) {
        fieldMap[y][x] = o;
    }
    
    public Object getObjectOnField(int x, int y) {
        return fieldMap[y][x];
    }

    public int[] getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int[] startPoint) {
        this.startPoint = startPoint;
    }
}
