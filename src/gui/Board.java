package gui;

import board.*;
import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

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

        private Tile startTile;
        private Tile endTile;
        private ArrayList<Tile> validEndTiles = new ArrayList<>();
        private ArrayList<Move> validMoves = new ArrayList<>();


    private Piece humanMovedPiece;

        private Game game;
        private Dice dice = new Dice();

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

    public Board(String humanPlayerName) throws HeadlessException {

        loadIcons();
        game = new Game(humanPlayerName);

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

    public Game getGame() {
        return game;
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

    public void setDices(Player player) {
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
        JLabel nameLabel = new JLabel("sosy");
        JLabel ruleDescriptionLabel = new JLabel("");
        JLabel firstDiceLabel = new JLabel("");
        JLabel secondDiceLabel = new JLabel("");
        JLabel magicPointsLabel = new JLabel("MAGIC POINTS : 0");
        JButton boomButton = new JButton("BOOM");

        HumanPanel (String name){
            super(new FlowLayout());

            nameLabel.setFont(new Font(name, Font.BOLD, 20));
            nameLabel.setText(name);
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
        private Color tileColor;

        TilePanel(BoardPanel boardPanel, int coordX, int coordY, Game game){
            super(new GridBagLayout());
            this.coordX = coordX;
            this.coordY = coordY;

            setPreferredSize(TILE_PANEL_DIMENSION);
            setTileColor(); // am facut asta in Tile
            setPieceIcon(game);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isLeftMouseButton(e)){ // select a move
                        if (startTile == null){ // first click => startTile
                            System.out.println("start");
                            startTile = game.getChessboard().getBoard()[coordX][coordY];
                            humanMovedPiece = startTile.getPiece();
                            if (humanMovedPiece == null){ // daca o selectat o patratica care nu i buna
                                startTile = null; // resetam patratica aleasa
                            }
                            else { // afisam mutarile bune posibile
                                validMoves = game.generateValidMoves(startTile);
                                for (Move move : validMoves){
                                    validEndTiles.add(move.getEnd());
                                }
                                highlightTiles(validEndTiles);
                            }
                        } else { // second click => endTile
                            System.out.println("end");
                            System.out.println(validEndTiles);
                            endTile = game.getChessboard().getBoard()[coordX][coordY];

                            System.out.println(endTile);

                            if (itWasClickedAnEndTile(endTile)){
                                for (Move validMove : validMoves){
                                    if (endTile == validMove.getEnd()){
                                        System.out.println("make move");
                                        game.makeMove(game.getHumanPlayer(), validMove);
                                        System.out.println(game.getChessboard());
                                        setPieceIcon(game);
                                    }
                                }
                            }
                            else endTile = null;



                            /*
                            endTile = game.getChessboard().getBoard()[coordX][coordY];
                            Piece pieceFromEndTile = endTile.getPiece();
                            if (pieceFromEndTile == null){

                            }
                            if (pieceFromEndTile.isWhite() == humanMovedPiece.isWhite()){ // daca piesele au aceeasi culoare, nu se poate
                                endTile = null;
                            }
                            else {

                            }

                             */
                            //MoveTransition moveTransition = game.getPlayerToMove().makeMove(move);

                        }


                    } else if (isRightMouseButton(e)){ // deselect
                        //unHighlightTiles();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });


            validate();
        }

        private boolean itWasClickedAnEndTile (Tile clickedTile){
            for (Tile validTile : validEndTiles){
                if (clickedTile == validTile){
                    System.out.println("it was");
                    return true;
                }
            }
            return false;
        }

        private void setPieceIcon(Game game){
            this.removeAll();
            Tile[][] chessboard = game.getChessboard().getBoard();
            if (chessboard[coordX][coordY].isTileOccupied()){
                Piece piece = chessboard[coordX][coordY].getPiece();

                System.out.println(coordX + " " + coordY + " " + piece.getType());

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
                    tileColor = WHITE_TILE_COLOR;
                     //       setBackground();
                }
                else { // col imPar
                    tileColor = BLACK_TILE_COLOR;
                    //setBackground(BLACK_TILE_COLOR);
                }
            }
            else { // row imPar
                if (coordY % 2 == 0) {// col Par
                    tileColor = BLACK_TILE_COLOR;
                    //setBackground(BLACK_TILE_COLOR);
                }
                else { // col imPar
                    tileColor = WHITE_TILE_COLOR;
                    //setBackground(WHITE_TILE_COLOR);
                }
            }
            setBackground(tileColor);
        }

        private void highlight (){
            setBackground(tileColor.darker());
        }

        private void unHighlight (){
            setBackground(tileColor);
        }


    }

    private void unHighlightTiles (){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                //unHighlight();
            }
        }
    }

    public void highlightTiles (ArrayList<Tile> tiles){
        for (Tile tile : tiles){
            int tileX = tile.getCoordX();
            int tileY = tile.getCoordY();
            boardPanel.tileBoard[tileX][tileY].highlight();
        }
    }



    public void play (){
        System.out.println(game.getChessboard().toString());
        Scanner scanner = new Scanner(System.in);

        int rowCounter = 0;
        game.setStatus(Game.Status.PLAY);

        while (rowCounter < 10 && game.getStatus() == Game.Status.PLAY ||  // cat timp inca se poate juca
                game.getStatus() == Game.Status.WHITE_IN_CHECK || game.getStatus() == Game.Status.BLACK_IN_CHECK){


            if (game.getStatus() == Game.Status.WHITE_IN_CHECK || game.getStatus() == Game.Status.BLACK_IN_CHECK){ // daca jucatorul curent e in sah
                if (isCheckMate(game.getPlayerToMove())){ // in cazu asta playerToMove o sa fie jucatorul cu alb
                    if (game.getPlayerToMove() == game.getHumanPlayer()){ // daca omu si o luat sah mat
                        game.setWinner(game.getComputerPlayer());// = computerPlayer;
                    }
                    else { // daca calculatorul si a luat sah mat
                        game.setWinner(game.getHumanPlayer());
                    }
                    game.setStatus(Game.Status.WIN);//status = Game.Status.WIN;
                    break; // daca e sah mat se iese din while (play) si se constata castigatoruu
                }

            }


            if (rowCounter % 2 == 0) game.setPlayerToMove(game.getHumanPlayer());
            else game.setPlayerToMove(game.getComputerPlayer());

            // TO DO => PLAY MODE
            // mai intai dam cu zaru
            dice.rollDice();
            System.out.println(dice);
            this.setDices(game.getPlayerToMove());


            //    boardGUI.setDices(dice, playerToMove);
            //game.setStatus(Game.Status.WIN);
            // status = Game.Status.WIN; // ca sa nu intre in bucla infinita

            System.out.println("Tura : " + rowCounter + " muta " + game.getPlayerToMove().getName());




            rowCounter++;
            /*
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             */
        }


        //System.out.println(rowCounter);
        //System.out.println("finnal");
        // TO DO => anunta castigatorul

        /*
        System.out.println("here");
        if (game == null){
            System.out.println("game pl");
        }
        if (boardGUI == null){
            System.out.println("boardgui pl");
        }
        */
    }
    private boolean isCheckMate(Player playerToMove) {
        return false;
    }

}
