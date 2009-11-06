package com.field;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.Image;

import com.model.Block;
import com.model.Field;

public class FieldLoader {
    public static Field loadField(String mapFile, Image blockImage) {
        Field field = null;

        
            File file = new File("maps/" + mapFile);
            FileReader fileReader = null;
            try {
                // Le o arquivo e joga em uma String
                fileReader = new FileReader(file);
                char[] chars = new char[(int)file.length()];
                fileReader.read(chars);

                String[] linhas = new String(chars).replaceAll("\r", "").split("\n");
                // Impossivel criar um campo com dimensões 0
                if (linhas.length > 0 && linhas[0].length() > 0) {
                    field = new Field(linhas[0].length(), linhas.length);
                    // Para armazenar um setor do campo
                    String sector;
                    for (int i = 0; i < linhas.length; i++) {
                        if (linhas[i] != null) {
                            for (int j = 0; j < linhas[i].length(); j++) {
                                sector = linhas[i].substring(j, j + 1);
                                // Sharp = Block
                                if (sector.equals("#")) {
                                    field.setObjectOnField(new Block(j, i, blockImage), j, i);
                                // S = Start Point
                                } else if (sector.equals("S")) {
                                    field.setStartPoint(new int[]{i, j});
                                }
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("FieldLoader.loadField - Arquivo não encontrado");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("FieldLoader.loadField - Erro ao ler o arquivo");
                e.printStackTrace();
            } finally {
                try {
                    if (fileReader != null) {
                        fileReader.close();
                    }
                } catch (IOException e) {
                    System.out.println("FieldLoader.loadField - Erro ao fechar o arquivo");
                    e.printStackTrace();
                }
            }

        return field;
    }
}
