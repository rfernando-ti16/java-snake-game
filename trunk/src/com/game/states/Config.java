package com.game.states;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.config.ConfigManager;
import com.field.FieldLoader;
import com.i18n.Messages;
import com.model.Field;
import com.model.GameObject;

/**
 * Estado para configurar o jogo
 * 
 * @author Erick Zanardo
 * 
 */
public class Config extends BasicGameState {
    private int stateId;
    private List<MapItem> mapas;
    private int selectedMap;
    private int actualPage[];
    private Image block;

    private int nivel;

    private String idioma;

    private int[] mapPreviewExtraSize;

    private ConfigManager manager;

    public Config(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {
        // Inicializa as opções
        manager = ConfigManager.getInstance();
        idioma = manager.getIdioma();
        nivel = manager.getLevel();

        // Inicializa o pré visuazador de mapas e a lista de mapas
        block = new Image("img/miniBlock.png");
        mapPreviewExtraSize = new int[2];
        mapPreviewExtraSize[0] = 90;
        mapPreviewExtraSize[1] = 500;
        if (mapas == null) {
            mapas = new ArrayList<MapItem>();
            File mapsDir = new File("maps");
            File[] arquivos = mapsDir.listFiles();
            MapItem item;
            int index = 0;
            for (File file : arquivos) {
                if (!file.isDirectory()) {
                    if (file.getName().equals(manager.getMaze())) {
                        selectedMap = index;
                    }
                    index++;
                    item = new MapItem();
                    item.setField(FieldLoader.loadField(file.getName(), block));
                    item.setFile(file);
                    mapas.add(item);
                }
            }
        }
        actualPage = new int[2];
        actualPage[0] = 0;
        if (mapas.size() > 10) {
            actualPage[1] = 10;
        } else {
            actualPage[1] = mapas.size();
        }
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {
        g.drawRect(3, 9, 790, 17);
        g.drawRect(3, 26, 790, 205);
        int y = 0;
        g.drawString(Messages.getString("Config.selecioneOLabirinto"), 5, y += 10);

        for (int i = actualPage[0]; i < actualPage[1]; i++) {
            if (i == selectedMap) {
                g.setColor(Color.gray);
            }
            g.drawString(mapas.get(i).getFile().getName(), 5, y += 20);
            g.setColor(Color.white);
        }

        for (int i = 0; i < mapas.get(selectedMap).getField().getHeight(); i++) {
            for (int j = 0; j < mapas.get(selectedMap).getField().getWidth(); j++) {
                if (mapas.get(selectedMap).getField().getFieldMap()[i][j] != null) {
                    ((GameObject)mapas.get(selectedMap).getField().getFieldMap()[i][j]).draw(g, mapPreviewExtraSize);
                }
            }
        }

        g.drawRect(3, 240, 790, 17);
        g.drawString(Messages.getString("Config.nivel") + nivel, 5, 240); 

        g.drawRect(3, 270, 790, 17);
        g.drawString(Messages.getString("Config.Idioma") + (idioma.equals("pt") ? "Português" : "English"), 5, 270);

        g.drawString(Messages.getString("Config.instrucoes"), 3, 300); 
        g.drawString(Messages.getString("Config.cimaBaixo"), 3, 330); 
        g.drawString(Messages.getString("Config.esquerdaDireita"), 3, 360); 
        g.drawString(Messages.getString("Config.mudaOIdioma"), 3, 390); 
        g.drawString(Messages.getString("Config.enterSalvarAsMudancas"), 3, 420); 
        g.drawString(Messages.getString("Config.escCancelaEVolta"), 3, 450); 
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        Input input = gc.getInput();
        // Baixo e cima controlam a lista de labirintos
        if (input.isKeyPressed(Input.KEY_UP)) {
            selectedMap--;
            if (selectedMap < 0) {
                actualPage[1] = mapas.size();
                if (mapas.size() > 10) {
                    actualPage[0] = mapas.size() - 10;
                } else {
                    actualPage[0] = 0;
                }
                selectedMap = actualPage[1] - 1;
            } else {
                if (actualPage[0] > 0) {
                    actualPage[0]--;
                }
                if (actualPage[1] >= 10 || actualPage[1] >= mapas.size()) {
                    actualPage[1]--;
                }
            }
            // Move para baixo
        } else if (input.isKeyPressed(Input.KEY_DOWN)) {
            selectedMap++;

            if (selectedMap > mapas.size() - 1) {
                actualPage[0] = 0;
                if (mapas.size() > 10) {
                    actualPage[1] = 10;
                } else {
                    actualPage[1] = mapas.size();
                }
                selectedMap = actualPage[0];
            } else {
                if (mapas.size() > 10) {
                    if (actualPage[0] < mapas.size() - 10) {
                        actualPage[0]++;
                    }
                }
                if (actualPage[1] < mapas.size()) {
                    actualPage[1]++;
                }
            }
        // Para esquerda e direita controlam os níveis
        } else if (input.isKeyPressed(Input.KEY_LEFT)) {
            if (nivel > 0) {
                nivel--;
            }
        } else if (input.isKeyPressed(Input.KEY_RIGHT)) {
           nivel++;
        // O I controla o idioma
        } else if (input.isKeyPressed(Input.KEY_I)) {
            if (idioma.equals("pt")) {
                idioma = "en"; 
            } else {
                idioma = "pt"; 
            }
        // Enter e ESC Controlam o salvamento e o cancelamento das mudanças
        } else if (input.isKeyPressed(Input.KEY_ENTER)) {
            manager.setIdioma(idioma);
            manager.setLevel(nivel);
            manager.setMaze(mapas.get(selectedMap).getFile().getName());
            manager.saveFile();
            sbg.enterState(Main.MENU);
        } else if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            sbg.enterState(Main.MENU);
        }
    }
}

class MapItem {
    private File file;
    private Field field;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
