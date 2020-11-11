package gui;

import board.Game;
import board.Tile;

import javax.swing.*;
import java.awt.*;

public class Board{
        // COMPUTER PANEL
        private static Dimension COMPUTER_PANEL_DIMENSION = new Dimension(400, 20);
        // BOARD PANEL
        private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
        private static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
        private static Color WHITE_TILE_COLOR = new Color(255, 255, 255);
        private static Color BLACK_TILE_COLOR = new Color(100, 73, 38);
        // HUMAN PANEL
        private static Dimension HUMAN_PANEL_DIMENSION = new Dimension(400, 30);
        private Game game;
        private JFrame mainFrame;
        private BoardPanel boardPanel;

        private JPanel computerPanel;
        private JPanel humanPanel;


        private JLabel playerName;
        private JLabel computerName;

    public Board() throws HeadlessException {
        this.game = new Game ("SOSY");
        this.mainFrame = new JFrame("LUCKY CHESS");
        this.mainFrame.setSize(600,600);
        this.mainFrame.setLayout(new BorderLayout());

        //this.mainFrame.setBackground(new Color(200, 76, 3));
        //this.mainFrame.setEnabled(true);

        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.boardPanel = new BoardPanel();
        this.mainFrame.add(boardPanel, BorderLayout.CENTER);
        this.mainFrame.setVisible(true);
    }

    public Game getGame() {
        return game;
    }



    private class ComputerPanel extends JPanel{

    }

    private class BoardPanel extends JPanel{
        TilePanel[][] tileBoard = new TilePanel[8][8];

        BoardPanel() {
            super(new GridLayout(8,8));
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    TilePanel tilePanel = new TilePanel(this, i, j); // aici porbabil voi face legutura cu controlleru cum ar vnei
                    tileBoard[i][j] = tilePanel;
                    this.add(tilePanel); // ?
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }

    private class TilePanel extends JPanel{
        private int coordX;
        private int coordY;

        TilePanel(BoardPanel boardPanel, int coordX, int coordY){
            super(new GridBagLayout());
            this.coordX = coordX;
            this.coordY = coordY;

            setPreferredSize(TILE_PANEL_DIMENSION);
            setTileColor(); // am facut asta in Tile
            validate();
        }

        private void setTileColor() {
            if (coordX % 2 == 0){ // row Par
                if (coordY % 2 == 0) {// col Par
                    setBackground(WHITE_TILE_COLOR);
                }
                else { // col imPar
                    setBackground(BLACK_TILE_COLOR);
                }
            }
            else { // row imPar
                if (coordY % 2 == 0) {// col Par
                    setBackground(BLACK_TILE_COLOR);
                }
                else { // col imPar
                    setBackground(WHITE_TILE_COLOR);
                }
            }
        }

    }
}
