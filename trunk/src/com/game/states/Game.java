package com.game.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.game.GamePlay;

/**
 * Representa o estado do jogo em si
 * 
 * @author Erick Zanardo
 *
 */
public class Game extends BasicGameState{

    public static int GAME_WIDTH = 800;
    public static int GAME_HEIGHT = 600;

    private GamePlay gamePlay;
    private int stateId;

    public Game(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer arg0, StateBasedGame arg1)
            throws SlickException {
        gamePlay = new GamePlay("Map1.map");
        gamePlay.loadResources();
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame sbg, Graphics g)
            throws SlickException {
        gamePlay.renderGamePlay(g);
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        gamePlay.updateGame(gc.getInput(), delta, sbg);
        
    }
}

