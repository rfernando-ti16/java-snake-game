package com.mapeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * Editor de mapas para o jogo
 * 
 * @author Erick Zanardo
 *
 */
public class MapEditor extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -250713205949659413L;
    private JDesktopPane desktop;
    public MapEditor () {
        super("Snake - Map Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        desktop = new JDesktopPane();
        desktop.setLayout(null);
        setContentPane(desktop);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Arquivo");
        menuBar.add(menu);

        JMenuItem item = new JMenuItem("Novo");
        menu.add(item);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                MapDesign map = new MapDesign();
                map.setModified(true);
                desktop.add(map);
                map.setVisible(true);
                
            }
        });

        item = new JMenuItem("Abrir");
        menu.add(item);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileFilter() {
                
                    @Override
                    public String getDescription() {
                        return "Apenas arquivos .map";
                    }
                
                    @Override
                    public boolean accept(File f) {
                        boolean retorno = false;
                        if (!f.isDirectory()) {
                            retorno = getExtension(f).equals("map");
                        }
                        return retorno;
                    }
                });

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                    // Faz um parse do arquivo para descobrir as dimensões do mapa
                    try {

                        FileReader fReader = new FileReader(chooser.getSelectedFile());
                        BufferedReader bReader = new BufferedReader(fReader);
                        String linha;
                        int h = 0;
                        int w = -1;
                        List<String> linhas = new ArrayList<String>();
                        while ((linha = bReader.readLine()) != null) {
                            linhas.add(linha);
                            h++;
                            if (w == -1) {
                                w = linha.length();
                            }

                            // todas as linhas tem que ter o mesmo tamanho
                            if (linha.length() != w) {
                                throw new IllegalArgumentException();
                            }
                        }

                        MapDesign map = new MapDesign();
                        map.setFileName(chooser.getSelectedFile().getName());
                        map.setFile(chooser.getSelectedFile());
                        map.setMapHeight(h);
                        map.setMapWidth(w);
                        map.initMatriz();

                        int i, j;
                        for (i = 0; i < linhas.size(); i++) {
                            for (j = 0; j < linhas.get(i).length(); j++) {
                                map.getMatrizMapa()[i][j] = linhas.get(i).substring(j, j + 1).charAt(0);
                            }
                        }

                        if (getWidth() < w * 10 + MapDesign.EXTRA_X + 100) {
                            setSize(w * 10 + MapDesign.EXTRA_X + 100, getHeight());
                        }

                        if (getHeight() < h * 10 + MapDesign.EXTRA_Y + 100) {
                            setSize(getWidth(), h * 10 + MapDesign.EXTRA_Y + 100);
                        }

                        map.setSize(w * 10 + MapDesign.EXTRA_X + 50, h * 10 + MapDesign.EXTRA_Y + 50);
                        repaint();
                        desktop.add(map);
                        map.setVisible(true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Arquivo vazio", "Erro", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, "Arquivo malformado", "Erro", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                    
                }
            }
        });

        item = new JMenuItem("Sair");
        menu.add(item);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });

        setSize(800, 600);
        setVisible(true);
    }

    public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }


    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new MapEditor();
    }
}
