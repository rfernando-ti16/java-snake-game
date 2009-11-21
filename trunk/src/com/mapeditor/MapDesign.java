package com.mapeditor;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.i18n.Messages;

/**
 * Representa a janela de edição de cada mapa
 * 
 * @author Erick Zanardo
 *
 */
public class MapDesign extends JInternalFrame implements MouseListener, MouseMotionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 7815598920004822729L;
    private String fileName;
    private boolean modified;
    private File file;
    private int mapWidth;
    private int mapHeight;

    private JTextField mapWidthBox;
    private JTextField mapHeightBox;

    private char[][] matrizMapa;
    private int eventAreaX;
    private int eventAreaY;

    // Contantes criadas para calcular um espaçamento do mapa dos inputs
    static int EXTRA_X = 10;
    static int EXTRA_Y = 150;

    // Para saber se o mouse ésta precionado ou não
    private boolean mouseButtonPressed;
    private int lastMouseButton;

    private boolean setStartPoint;

    private static int MAP_CREATION = 0;

    public MapDesign() {
        super("Novo_Map" + MAP_CREATION++ + ".map*", true, true); 
        setFileName(getTitle());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setLayout(null);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu(Messages.getString("MapDesign.arquivo"));
        menuBar.add(menu);

        JMenuItem item = new JMenuItem(Messages.getString("MapDesign.setarPontoDePartida"));
        menu.add(item);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setStartPoint = true;
            }
        });

        item = new JMenuItem(Messages.getString("MapDesign.salvar"));
        menu.add(item);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (file == null) {
                    createFile();
                }
                saveExistingFile();
            }
        });

        item = new JMenuItem(Messages.getString("MapDesign.salvarComo")); 
        menu.add(item);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                createFile();
                saveExistingFile();
            }
        });

        // Para informar o tamanho do mapa
        JLabel lblWidth = new JLabel(Messages.getString("MapDesign.largura"));
        lblWidth.setBounds(5, 5, 100, 20);

        mapWidthBox = new JTextField();
        mapWidthBox.setBounds(5, 25, 100, 20);
        mapWidthBox.setText(String.valueOf(getMapWidth()));

        JLabel lblHeight = new JLabel(Messages.getString("MapDesign.altura"));
        lblHeight.setBounds(120, 5, 100, 20);

        mapHeightBox = new JTextField();
        mapHeightBox.setBounds(120, 25, 100, 20);
        mapHeightBox.setText(String.valueOf(getMapHeight()));

        JButton btnCriarNovoMap = new JButton(Messages.getString("MapDesign.gerarNovoMapa"));
        btnCriarNovoMap.setBounds(240, 5, 300, 40);
        btnCriarNovoMap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event) {
                try {
                    int w = Integer.parseInt(mapWidthBox.getText());
                    int h = Integer.parseInt(mapHeightBox.getText());
                    if (w <= 70 && h <= 50) {
                        setMapWidth(w);
                        setMapHeight(h);

                        initMatriz();
                        // Preenche com espaçoes em branco
                        for (int i = 0; i < matrizMapa.length; i++) {
                            for (int j = 0; j < matrizMapa[i].length; j++) {
                                matrizMapa[i][j] = ' ';
                            }
                        }

                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, Messages.getString("MapDesign.oMapaPodeTerNoMaximo"), Messages.getString("MapDesign.erro"), JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, Messages.getString("MapDesign.numeroInvalido"), Messages.getString("MapDesign.erro"), JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        // Adicionando os objetos no Frame
        add(btnCriarNovoMap);
        add(lblHeight);
        add(mapHeightBox);
        add(lblWidth);
        add(mapWidthBox);

        addMouseListener(this);
        addMouseMotionListener(this);
        
        addInternalFrameListener(new InternalFrameListener(){
            public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
                if (isModified()) {
                    int resposta = JOptionPane.showConfirmDialog(null,Messages.getString("MapDesign.oMapaFoiModificado"),
                                                                      Messages.getString("MapDesign.fechar"),
                                                                      JOptionPane.YES_NO_OPTION);
                    if (resposta == JOptionPane.YES_OPTION) {
                        internalFrameEvent.getInternalFrame().dispose();
                    }
                } else {
                    internalFrameEvent.getInternalFrame().dispose();
                }
            }

            public void internalFrameActivated(InternalFrameEvent arg0) { }

            public void internalFrameClosed(InternalFrameEvent arg0) {}

            public void internalFrameDeactivated(InternalFrameEvent arg0) {}

            public void internalFrameDeiconified(InternalFrameEvent arg0) {}

            public void internalFrameIconified(InternalFrameEvent arg0) {}

            public void internalFrameOpened(InternalFrameEvent arg0) {}
        });

        setSize(700, 500);
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (matrizMapa != null) {
            for (int i = 0; i < matrizMapa.length; i++) {
                for(int j = 0; j < matrizMapa[i].length; j++) {
                    g.drawRect(j * 10 + EXTRA_X, i * 10 + EXTRA_Y, 10, 10);

                    if (matrizMapa[i][j] == '#') {
                        g.fillRect(j * 10 + EXTRA_X, i * 10 + EXTRA_Y, 10, 10);
                    } else if (matrizMapa[i][j] == 'S') {
                        g.drawString("S", j * 10 + EXTRA_X, i * 10 + EXTRA_Y);
                    }
                }
            }
        }
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        setTitle(fileName + (isModified() ? "*" : ""));
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        setTitle(fileName + (isModified() ? "*" : ""));
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int width) {
        this.mapWidth = width;
        mapWidthBox.setText(String.valueOf(width));
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int height) {
        this.mapHeight = height;
        mapHeightBox.setText(String.valueOf(height));
    }

    public char[][] getMatrizMapa() {
        return matrizMapa;
    }

    private void createFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            setFileName(file.getName());
        }
    }

    private void saveExistingFile() {
        try {
            FileWriter writer = new FileWriter(file);
            PrintWriter out = new PrintWriter(writer);

            String aux = ""; //$NON-NLS-1$
            for (char[] charsY : matrizMapa) {
                aux = "";
                for (char c : charsY) {
                    aux += c;
                }
                out.println(aux);
            }
            out.close();
            writer.close();
            setModified(false);
        } catch (IOException e) {
            System.out.println(" Arquivo não encontrado em disco ");
            e.printStackTrace();
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {}

    public void mouseExited(MouseEvent mouseEvent) {}

    public void mousePressed(MouseEvent mouseEvent) {
        mouseButtonPressed = true;
        lastMouseButton = mouseEvent.getButton();
        controlaEventoMouse(mouseEvent);
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        lastMouseButton = mouseEvent.getButton();
        mouseButtonPressed = false;
    }
    
    public void mouseDragged(MouseEvent mouseEvent) {
        if (mouseButtonPressed) {
            controlaEventoMouse(mouseEvent);
        }
    }

    public void mouseMoved(MouseEvent mouseEvent) {}

    /**
     * Pega as coordenad
     * 
     * @param event
     */
    private void controlaEventoMouse(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();

        // Só executa se o evento foi executado dentro da área do mapa
        if ((x >= EXTRA_X && x <= eventAreaX) &&
            (y >= EXTRA_Y && y <= eventAreaY)) {

            // Remove o tamanho extra
            int mapIndexX = x - EXTRA_X;
            int mapIndexY = y - EXTRA_Y;

            // Pega o indice de acordo com a sua dezena ex (1 = 0, 10 = 1, 20 = 2)
            while (mapIndexX % 10 != 0) {
                mapIndexX--;
            }
            mapIndexX = mapIndexX / 10;

            while (mapIndexY % 10 != 0) {
                mapIndexY--;
            }
            mapIndexY = mapIndexY / 10;

            if (setStartPoint) {
                matrizMapa[mapIndexY][mapIndexX] = 'S';
                setStartPoint = false;
            } else {
                if (lastMouseButton == MouseEvent.BUTTON1) {
                    matrizMapa[mapIndexY][mapIndexX] = '#';
                } else  if (lastMouseButton == MouseEvent.BUTTON3 && mouseButtonPressed) {
                    matrizMapa[mapIndexY][mapIndexX] = ' ';
                } 
            }
            repaint();
            setModified(true);
        }
    }
    
    public void initMatriz(){
        matrizMapa = new char[mapHeight][mapWidth];
        eventAreaX = mapWidth * 10 + EXTRA_X;
        eventAreaY = mapHeight * 10 + EXTRA_Y;
    }
}
