package com.game;

import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.field.FieldLoader;
import com.model.Block;
import com.model.Direction;
import com.model.Field;
import com.model.Food;
import com.model.GameObject;
import com.model.Snake;
import com.model.SnakePiece;

/**
 * Controla o jogo
 * 
 * @author Erick Zanardo
 *
 */
public class GamePlay {
    private Image blockImage;
    private Field field;
    private Snake snakeOnField;
    private Food foodOnField;
    /** Qual  level de dificuldade que estamos */
    private int level;
    /** Para controlar a passegem de level */
    private int levelStep;
    /** Qual a pontuação do jogador */
    private int score;
    /** Qual tempo já passou desde a última atualização */
    private int timePassed;
    /** para pausar o jogo */
    private boolean paused;

    /** Quantos milisegundos de um segundo */
    private static int MILI = 1000;

    private Random random;

    /** Para saber qual o tamanho de folga qu foi utilizado para centralizar o campo */
    private int extraSize[];

    public void loadResources() throws SlickException{
        blockImage = new Image("img/block.png");

        // Carrega o campo
        field = FieldLoader.loadField("Map1.map", blockImage);

        random = new Random();
        createSnakeOnField();
        level = 1;

        extraSize = new int[2];
        // Y
        extraSize[0] = (Game.GAME_HEIGHT - (field.getHeight() * 10)) / 2; 
        // X
        extraSize[1] = (Game.GAME_WIDTH - (field.getWidth() * 10)) / 2;
    }

    public void updateGame(Input input, int delta) {
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            paused = !paused;
        }
        if (!paused) {
            if (foodOnField == null) {
                createFoodOnField();
            }
            if (input.isKeyPressed(Input.KEY_UP)) {
                if (snakeOnField.getDirection() != Direction.DOWN) {
                    snakeOnField.setDirection(Direction.UP);
                }
            } else if (input.isKeyPressed(Input.KEY_DOWN)) {
                if (snakeOnField.getDirection() != Direction.UP) {
                    snakeOnField.setDirection(Direction.DOWN);
                }
            } else if (input.isKeyPressed(Input.KEY_LEFT)) {
                if (snakeOnField.getDirection() != Direction.RIGHT) {
                    snakeOnField.setDirection(Direction.LEFT);
                }
            } else if (input.isKeyPressed(Input.KEY_RIGHT)) {
                if (snakeOnField.getDirection() != Direction.LEFT) {
                    snakeOnField.setDirection(Direction.RIGHT);
                }
            }

            int speedUp = 1;
            if (input.isKeyDown(Input.KEY_SPACE)) {
                speedUp = 4;
            }

            timePassed += delta * level * speedUp;
            int x;
            int y;

            if (timePassed >= MILI) {
                x = snakeOnField.getSnakePieces().get(0).getX();
                y = snakeOnField.getSnakePieces().get(0).getY();

                switch (snakeOnField.getDirection()) {
                case RIGHT:
                    x++;
                    break;
                case LEFT:
                    x--;
                    break;
                case UP:
                    y--;
                    break;
                case DOWN:
                    y++;
                    break;
                }
                Object o =  field.getObjectOnField(x, y);
                if (o instanceof Block || o instanceof SnakePiece) {
                    System.out.println("Perdeu preiboy");
                    // TODO Colocar tela de game over
                } else {
                    if (o instanceof Food) {
                        // Remove a comida atual para criar outra
                        field.setObjectOnField(null, foodOnField.getX(), foodOnField.getY());
                        createFoodOnField();
                        score += level * 1;
                        addSnakePieceOnSnake();

                        // Cálcula a passegem de nível
                        levelStep++;
                        if (levelStep >= (level * 10)) {
                            level++;
                        }
                    }

                    // Remove a última peça do campo para que não ficar peças invisíveis(peças que já passaram por 
                    // aquela posição mas não foram removidas do campo) 
                    field.setObjectOnField(null, snakeOnField.getSnakePieces().get(snakeOnField.getSnakePieces().size() - 1).getX(), 
                                                 snakeOnField.getSnakePieces().get(snakeOnField.getSnakePieces().size() - 1).getY());
                    SnakePiece piece;
                    SnakePiece nextPiece;
                    for(int i = snakeOnField.getSnakePieces().size() - 1; i > 0; i--) {
                        piece = snakeOnField.getSnakePieces().get(i);
                        nextPiece = snakeOnField.getSnakePieces().get(i - 1);
                        piece.setX(nextPiece.getX());
                        piece.setY(nextPiece.getY());
                        field.setObjectOnField(piece, piece.getX(), piece.getY());
                    }

                    snakeOnField.getSnakePieces().get(0).setX(x);
                    snakeOnField.getSnakePieces().get(0).setY(y);
                    field.setObjectOnField(snakeOnField.getSnakePieces().get(0), x, y);
                    timePassed = 0;
                }
            }
        }
    }
    
    public void renderGamePlay(Graphics g) {
        g.drawString(" Pontos: " + score + " | Nível: " + level, 5 , 5 );
        for (int i = 0; i < field.getHeight(); i++) {
            for (int j = 0; j < field.getWidth(); j++) {
                if (field.getFieldMap()[i][j] != null) {
                    ((GameObject)field.getFieldMap()[i][j]).draw(g, extraSize);
                }
            }
        }
    }

    private void createSnakeOnField() {
        int[] start = field.getStartPoint();
        snakeOnField = new Snake();
        snakeOnField.setDirection(Direction.RIGHT);
        SnakePiece sp;
        for (int i = 0; i < 3; i++) {
            sp = new SnakePiece(start[1] - i, start[0]);
            snakeOnField.addSnakePiece(sp);
            field.setObjectOnField(sp, sp.getX(), sp.getY());
        }
    }

    private void createFoodOnField() {
        foodOnField = new Food(random.nextInt(field.getWidth() - 1), random.nextInt(field.getHeight() - 1));
        while (!validPositionForFood()) {
            foodOnField.setX(random.nextInt(field.getWidth() - 1));
            foodOnField.setY(random.nextInt(field.getHeight() - 1));
        }
        field.setObjectOnField(foodOnField, foodOnField.getX(), foodOnField.getY());
    }

    private boolean validPositionForFood() {
        return field.getObjectOnField(foodOnField.getX(), foodOnField.getY()) == null;
    }
    
    private void addSnakePieceOnSnake() {
        SnakePiece lastPiece = snakeOnField.getSnakePieces().get(snakeOnField.getSnakePieces().size() - 1);
        SnakePiece newPiece = new SnakePiece(lastPiece.getX() - 1, lastPiece.getY());
        snakeOnField.addSnakePiece(newPiece);
    }
}
