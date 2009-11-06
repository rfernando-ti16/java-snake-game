package com.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

    public static int GAME_WIDTH = 800;
    public static int GAME_HEIGHT = 600;

    private GamePlay gamePlay;

    public Game(String title) {
        super(title);
    }

    public void init(GameContainer gc) throws SlickException {
        gamePlay = new GamePlay();
        gamePlay.loadResources();
    }

    public void update(GameContainer gc, int delta) throws SlickException {
        gamePlay.updateGame(gc.getInput(), delta);
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        gamePlay.renderGamePlay(g);
    }
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game("Snake"));
        app.setDisplayMode(GAME_WIDTH, GAME_HEIGHT, false);
        app.setShowFPS(false);
        app.start();
    }
}

