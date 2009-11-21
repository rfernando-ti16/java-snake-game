package com.game.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.i18n.Messages;

/**
 * Representa o estado do Game Over
 * 
 * @author Erick Zanardo
 *
 */
public class GameOver extends BasicGameState {

    private Image backGround;
    private int stateId;

    public GameOver (int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    public void init(GameContainer arg0, StateBasedGame arg1)
            throws SlickException {
        backGround = new Image("img/gameOver.png");
    }

    public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
            throws SlickException {
        backGround.draw(( Game.GAME_WIDTH - backGround.getWidth()) / 2, 50);
        g.drawString(Messages.getString("GameOver.precissioneEscParaVoltar"), 250, backGround.getHeight() + 100); 
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            sbg.enterState(Main.MENU);
        }
    }

}
