package gui;

import board.*;
import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board{
        // COMPUTER PANEL
        private static Dimension COMPUTER_PANEL_DIMENSION = new Dimension(400, 40);
        // BOARD PANEL
        private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
        private static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
        private static Color WHITE_TILE_COLOR = new Color(255, 255, 255);
        private static Color BLACK_TILE_COLOR = new Color(100, 73, 38);
        // HUMAN PANEL
        private static Dimension HUMAN_PANEL_DIMENSION = new Dimension(400, 40);

        private JFrame mainFrame;
        private BoardPanel boardPanel;

        private JPanel computerPanel;
        private JPanel humanPanel;
        private String humanPlayerName;

        private ArrayList<ImageIcon> diceIcons = new ArrayList<>(7); //= new ImageIcon("images/dice1.png");

    private void loadIcons (){
        diceIcons.add(new ImageIcon("images/dice1.png")); // il punema ici de forma ca altfel loam eroare de index mai jos
        // LOAD ICONS
        for (int i = 1; i <= 6; i++){
            String fileName = "images/dice" + i + ".png";
            ImageIcon imageIcon = new ImageIcon(fileName);
            Image image = imageIcon.getImage();
            Image resizedImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(resizedImage);
            diceIcons.add(i, imageIcon);
        }
    }

    public Board(String humanPlayerName, Game game) throws HeadlessException {
        loadIcons();

        this.mainFrame = new JFrame("LUCKY CHESS");
        this.mainFrame.setSize(700,700); // [600,600]
        this.mainFrame.setLayout(new BorderLayout());
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // COMPUTER PANEL
        this.computerPanel = new ComputerPanel();
        this.mainFrame.add(computerPanel, BorderLayout.NORTH);
        // BOARD PANEL
        this.boardPanel = new BoardPanel(game);
        this.mainFrame.add(boardPanel, BorderLayout.CENTER);
        // HUMAN PANEL
        this.humanPanel = new HumanPanel(humanPlayerName);
        this.mainFrame.add(humanPanel, BorderLayout.SOUTH);

        this.mainFrame.setVisible(true);

        this.humanPlayerName = humanPlayerName;

        loadPieceIcons();


    }


    private void loadPieceIcons (){
        // set the pieces (icons)

        /*
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                String pieceType = "";
                /// TO DO => LOAD ICONS OF PIECES
                this.boardPanel.tileBoard[i][j].pieceIcon = new ImageIcon(iconLocation + pieceType + ".png");
                //this.boardPanel.tileBoard[i][j].pieceIcon =
                //        new ImageIcon("images/dice1.png"
            }

        }
        */
    }

    public void setDices(Dice dice, Player player) {
        int firstDice = dice.getFirstDice();
        int secondDice = dice.getSecondDice();

        if (player.isHuman() == true){
            HumanPanel humanPanel = (HumanPanel) this.humanPanel;
            humanPanel.getFirstDiceLabel().setIcon(diceIcons.get(firstDice));
            humanPanel.getSecondDiceLabel().setIcon(diceIcons.get(secondDice));
            humanPanel.getRuleDescriptionLabel().setText(dice.getRule());
        }
        else {
            ComputerPanel computerPanel = (ComputerPanel) this.computerPanel;
            // dices
            computerPanel.getFirstDiceLabel().setIcon(diceIcons.get(firstDice));
            computerPanel.getSecondDiceLabel().setIcon(diceIcons.get(secondDice));
            // rule
            computerPanel.getRuleDescriptionLabel().setText(dice.getRule());
        }

    }


    private class HumanPanel extends JPanel{
        JLabel nameLabel = new JLabel("<insert-name>");
        JLabel ruleDescriptionLabel = new JLabel("");
        JLabel firstDiceLabel = new JLabel("");
        JLabel secondDiceLabel = new JLabel("");
        JLabel magicPointsLabel = new JLabel("MAGIC POINTS : 0");
        JButton boomButton = new JButton("BOOM");

        HumanPanel (String name){
            super(new FlowLayout());

            nameLabel.setFont(new Font(name, Font.BOLD, 20));
            this.add(ruleDescriptionLabel);
            this.add(firstDiceLabel);
            this.add(secondDiceLabel);
            this.add(magicPointsLabel);
            this.add(boomButton);

            this.add(nameLabel);
            setPreferredSize(HUMAN_PANEL_DIMENSION);
            validate();
        }

        public JLabel getRuleDescriptionLabel() {
            return ruleDescriptionLabel;
        }

        public JLabel getFirstDiceLabel() {
            return firstDiceLabel;
        }

        public JLabel getSecondDiceLabel() {
            return secondDiceLabel;
        }
    }

    private class ComputerPanel extends JPanel{
        JLabel nameLabel = new JLabel("LORD INATEUR");
        JLabel ruleDescriptionLabel = new JLabel("");
        JLabel firstDiceLabel = new JLabel("");
        JLabel secondDiceLabel = new JLabel("");
        JLabel magicPointsLabel = new JLabel("MAGIC POINTS : 0");
        JButton boomButton = new JButton("BOOM");

        ComputerPanel (){
            super(new FlowLayout());

            nameLabel.setFont(new Font("LORD INATEUR", Font.BOLD, 20));

            this.add(ruleDescriptionLabel);
            this.add(firstDiceLabel);
            this.add(secondDiceLabel);
            this.add(magicPointsLabel);
            this.add(boomButton);

            this.add(nameLabel);
            setPreferredSize(COMPUTER_PANEL_DIMENSION);
            validate();
        }

        public JLabel getFirstDiceLabel() {
            return firstDiceLabel;
        }

        public JLabel getSecondDiceLabel() {
            return secondDiceLabel;
        }

        public JLabel getRuleDescriptionLabel() {
            return ruleDescriptionLabel;
        }
    }

    private class BoardPanel extends JPanel{
        TilePanel[][] tileBoard = new TilePanel[8][8];

        BoardPanel(Game game) {
            super(new GridLayout(8,8));
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    TilePanel tilePanel = new TilePanel(this, i, j, game); // aici porbabil voi face legutura cu controlleru cum ar vnei
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

        TilePanel(BoardPanel boardPanel, int coordX, int coordY, Game game){
            super(new GridBagLayout());
            this.coordX = coordX;
            this.coordY = coordY;

            setPreferredSize(TILE_PANEL_DIMENSION);
            setTileColor(); // am facut asta in Tile
            setPieceIcon(game);
            validate();
        }

        private void setPieceIcon(Game game){
            this.removeAll();
            Tile[][] chessboard = game.getChessboard().getBoard();
            if (chessboard[coordX][coordY].isTileOccupied()){
                Piece piece = chessboard[coordX][coordY].getPiece();
                String fileName = "images/" + piece.getType();
                if (piece.isWhite()) fileName += "W";
                else fileName += "B";
                fileName += ".png";

                ImageIcon imageIcon = new ImageIcon(fileName);
                Image image = imageIcon.getImage();
                Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(resizedImage);
                add(new JLabel(imageIcon));

            }


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
