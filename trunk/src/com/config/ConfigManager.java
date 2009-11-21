package com.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Cuida do gerenciamento do arquivo de configuração do jogo
 * 
 * @author Erick Zanardo
 *
 */
public class ConfigManager {
    private static ConfigManager instance;
    private int level;
    private String maze;
    private String idioma;
    private Locale locale;

    private ConfigManager(){
        loadFile();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadFile() {
        File f = new File("config/Config.snk");
        try {
            FileReader reader = new FileReader(f);
            BufferedReader bReader = new BufferedReader(reader);
            String linha;
            String opcao[];
            while((linha = bReader.readLine()) != null) {
                 opcao = linha.split("=");
                 if (opcao.length == 2) {
                     if (opcao[0].equals("level")) {
                         setLevel(Integer.parseInt(opcao[1]));
                     } else if (opcao[0].equals("maze")) {
                         setMaze(opcao[1]);
                     } else if (opcao[0].equals("idioma")) {
                         setIdioma(opcao[1]);
                     }
                 }
            }
            bReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de configuração não encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Arquivo de configuração não encontrado");
            e.printStackTrace();
        }
    }

    public void saveFile() {
        File f = new File("config/Config.snk");
        try {
            FileWriter writer = new FileWriter(f);
            PrintWriter out = new PrintWriter(writer);
            out.println("level=" + level);
            out.println("maze=" + maze);
            out.println("idioma=" + idioma);
            out.close();
            writer.close();
        } catch (IOException e) {
            System.out.println(" Arquivo não encontrado ");
            e.printStackTrace();
        }
    }

    public int getLevel() {
        if (level == 0) {
            return 1;
        }
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMaze() {
        return maze;
    }

    public void setMaze(String maze) {
        this.maze = maze;
    }

    public String getIdioma() {
        if (idioma == null) {
            return "pt";
        }
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.locale = new Locale(idioma);
        this.idioma = idioma;
    }

    public Locale getLocale() {
        return locale;
    }
}
