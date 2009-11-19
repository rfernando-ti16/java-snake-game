package com.game.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Estado para configurar o jogo
 * 
 * @author Erick Zanardo
 *
 */
public class Config extends BasicGameState {
    private int stateId;

    public Config(int stateId) {
     this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        
    }

}
