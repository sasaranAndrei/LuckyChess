package gui;

import board.*;
import console.Command;
import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

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

        ///////////this.mainFrame.setVisible(false);
        this.mainFrame.setVisible(false);

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

                                        game.makeMove(validMove);

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

                //System.out.println(coordX + " " + coordY + " " + piece.getType());

                String fileName = "images/" + piece.getType();
                if (piece.isWhite()) fileName += "W";
                else fileName += "B";
                fileName += ".png";

                //System.out.println(fileName);

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


    int rowCounter = 0;
    //int switchSides = 0;

    public void play (){
        Scanner scanner = new Scanner(System.in);
        //System.out.println(game.getChessboard().toString());

        Move lastHumanMoveMade = null;
        Move lastComputerMoveMade = null;
        game.setStatus(Game.Status.PLAY);

        while (rowCounter < 20 && game.getStatus() == Game.Status.PLAY ||  // cat timp inca se poate juca
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
            //this.setDices(game.getPlayerToMove());


            //    boardGUI.setDices(dice, playerToMove);
            //game.setStatus(Game.Status.WIN);
            // status = Game.Status.WIN; // ca sa nu intre in bucla infinita

            System.out.println("Tura : " + rowCounter + " muta " + game.getPlayerToMove().getName());
            if (game.getPlayerToMove().isHuman()){
                // citim comanda utilizatorului
                boolean makeMoveFlag = false;
                System.out.println(game.getChessboard().toString());
                while (makeMoveFlag == false){
                    Command command = new Command(scanner);
                    ArrayList<Move> validMoves = null;
                    if (command.isShowCommand()){
                        int showX = command.startX;
                        int showY = command.endX;
                        //System.out.println("SHOW " + showX + " " + showY);
                        Tile showTile = game.getChessboard().getBoard()[showX][showY];

                        validMoves = game.showValidMoves(showTile);
                        if (validMoves != null){
                            System.out.println("COPY and PASTE one of this:");
                            for (Move validMove : validMoves){
                                System.out.println("move " + validMove.getStart().getCoordX() + " " + validMove.getStart().getCoordY() + " " + validMove.getEnd().getCoordX() + " " + validMove.getEnd().getCoordY());
                            }
                        }


                    }
                    else { // ar trebui command, dar testam
                        if (command.isMoveCommand()){
                            int sourceX = command.startX;
                            int sourceY = command.endX;
                            int destX = command.startY;
                            int destY = command.endY;
                            Tile sourceTile = game.getChessboard().getBoard()[sourceX][sourceY];
                            Tile destinationTile = game.getChessboard().getBoard()[destX][destY];
                            //System.out.println("MOVE " + sourceX + " " + sourceY + " " + destX + " " + destY);
                            // make move cum zice baiatu
                            Move moveToMake = null;
                            // daca nu o facut baiatu show inainte
                            validMoves = game.generateValidMoves(sourceTile);
                            for (Move validMove : validMoves){
                                if (validMove.getEnd() == destinationTile){
                                    moveToMake = validMove;
                                    break;
                                }
                            }
                            for (Move validMove : validMoves){
                                Tile markedTile = validMove.getEnd(); // let s unMark the valid move on the chessboard
                                int tileCoordX = markedTile.getCoordX();
                                int tileCoordY = markedTile.getCoordY();
                                game.getChessboard().getBoard()[tileCoordX][tileCoordY].setForValidMove(false);
                            }
                            game.makeMove(moveToMake);
                            makeMoveFlag = true;
                            lastHumanMoveMade = moveToMake;

                        }
                    }
                }


            }
            else { // daca nu i uman
                System.out.println(game.getChessboard().toString());

                ArrayList<Piece> computerPieces = null;

                //if (switchSides % 2 == 0){ // computer = black
                    computerPieces = game.getChessboard().storePieces(false);
                //}
                //else {
                //    computerPieces = game.getChessboard().storePieces(true);
                //}
                ArrayList<Move> validMoves = game.generateAllValidMoves(computerPieces);
                Move moveToMake = newPickTheBestMove(validMoves, lastHumanMoveMade);
                System.out.println("move to be made");
                System.out.println(moveToMake);
                game.makeMove(moveToMake);

                lastComputerMoveMade = moveToMake;
            }







            rowCounter++;

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

    private Move newPickTheBestMove (ArrayList<Move> validMoves, Move lastHumanMoveMade){
        HashSet<Piece> vulnerablePiecesSet = new HashSet<>();
        HashSet<Tile> vulnerableTiles = this.findVulnerableTiles(lastHumanMoveMade);
        for (Tile vulnerableTile : vulnerableTiles){
            if (vulnerableTile.getPiece() != null && !vulnerableTile.getPiece().white){
                vulnerablePiecesSet.add(vulnerableTile.getPiece());
            }
        }

        Move bestPick = null;
        ArrayList<Move.BasicMove> basicMoves = new ArrayList<>();
        ArrayList<Move.AttackMove> attackMoves = new ArrayList<>();
        for (Move validMove : validMoves){
            if (validMove instanceof Move.AttackMove){
                attackMoves.add((Move.AttackMove) validMove);
            }
            else {
                basicMoves.add((Move.BasicMove) validMove);
            }
        }

        // find best attack move
        Move bestAttackMove = null;
        int bestValueOfAttackMove =  -1;
        if (attackMoves.size() != 0){
            for (Move.AttackMove attackMove : attackMoves){
                Piece pieceThatAttack = attackMove.getStart().getPiece();
                Piece attackedPiece = attackMove.attackedPiece;

                if (attackedPiece.getValue() >= pieceThatAttack.getValue()){ // trade sau mai bine
                    int currentValueOfAttackMove = attackedPiece.getValue() - pieceThatAttack.getValue();
                    if (currentValueOfAttackMove > bestValueOfAttackMove){
                        bestValueOfAttackMove = currentValueOfAttackMove;
                        bestAttackMove = attackMove;
                    }
                }
                else { // vedem daca piesa adv e neaparata si daca da mutam acolo

                    boolean canMoveThere = findIfCanMove(attackMove, vulnerableTiles);

                    if (canMoveThere){
                        System.out.println("vom lua o piesa neaparata");
                        // daca am ajuns inseamna ca nu e neaparata
                        int currentValueOfAttackMove = attackedPiece.getValue(); // nu mai facem diferenta pt ca nu tre sa facem trade
                        if (currentValueOfAttackMove > bestValueOfAttackMove){
                            bestValueOfAttackMove = currentValueOfAttackMove;
                            bestAttackMove = attackMove;
                        }
                    }

                }
            }
        }


        // find best basic move
        int bestValueOfBasicMove = -1;
        Move bestBasicMove = null;
        List<Piece> vulnerablePieces = new ArrayList<>(vulnerablePiecesSet);
        Collections.sort(vulnerablePieces);

        if (vulnerablePieces.size() != 0) {
            Piece mostVulnerablePieceNow = vulnerablePieces.get(0);
            bestValueOfBasicMove = mostVulnerablePieceNow.getValue();

            Tile tileOfIt = game.findTheTile(game.getChessboard(), mostVulnerablePieceNow);
            ArrayList<Move> possibleMovesForIt = game.generateValidMoves(tileOfIt);
            for (Move mustMove : possibleMovesForIt){
                boolean canMoveThere = findIfCanMove(mustMove, vulnerableTiles);
                if (canMoveThere){
                    bestBasicMove = mustMove;
                    break;
                }
            }
            //bestValueOfBasicMove = -10; // daca am ajuns aici inseamna ca piesa aia ii rip deci alegem bestAttackMove
        }
        /*
        else { // daca nu avem piese vulnerabile atacam
            bestPick = bestAttackMove;
        }
         */
        System.out.println("Best Attack Move : " + bestValueOfAttackMove + " " + bestAttackMove);
        System.out.println("Best Basic Move : " + bestValueOfBasicMove + " " + bestBasicMove);

        if (bestValueOfAttackMove >= bestValueOfBasicMove){
            bestPick = bestAttackMove;
        }
        else {
            bestPick = bestBasicMove;
        }

        if (bestPick == null){
            ArrayList<Move> safeMoves = new ArrayList<>();
            for (Move possibleSafeMove : basicMoves){
                boolean isSafeTile = true;
                for (Tile vulnerableTile : vulnerableTiles){
                    if (possibleSafeMove.getEnd() == vulnerableTile){
                        isSafeTile = false;
                        break;
                    }
                }

                if (isSafeTile){
                    safeMoves.add(possibleSafeMove);
                }
            }
            System.out.println("save moves:");
            System.out.println(safeMoves);
            int size = safeMoves.size();
            Random random = new Random();
            bestPick = safeMoves.get(random.nextInt(size));
        }

        System.out.println("vulnerable tiles:");
        for (Tile vulnerableTile : vulnerableTiles){
            System.out.println(vulnerableTile.getCoordinates());
        }


        return bestPick;
    }

    private boolean findIfCanMove (Move move, HashSet<Tile> vulnerableTiles){
        for (Tile vulnerableTile : vulnerableTiles){
            if (move.getEnd() == vulnerableTile){
                return false;
            }
        }
        return true;
    }

    private HashSet<Tile> findVulnerableTiles (Move lastHumanMoveMade){
        HashSet<Tile> vulnerableTiles = new HashSet<>();

        ArrayList<Piece> opponentPieces = game.getChessboard().storePieces(true); // piesele omului
        //// tre sa impartim intre pawn si notPawn pentru ca
        //// inafara de pawn, toate piesele captureaza la fel ca si cum muta

        /*
        ArrayList<Tile> pawnsTiles = new ArrayList<>();
        ArrayList<Piece> nonPawns = new ArrayList<>();

        for (Piece piece : opponentPieces){
            if (piece instanceof Pawn){
                pawnsTiles.add(game.findTheTile(game.getChessboard(), piece));
            }
            else {
                nonPawns.add(piece);
            }
        }

         */

        ArrayList<Move> validOpponentMoves = game.generateAllValidMoves(opponentPieces);
        for (Move opponentMove : validOpponentMoves){
            vulnerableTiles.add(opponentMove.getEnd());  // adaugam pozitile unde poate muta tura asta
            // si pozitiile unde poate muta tura viitoare
            //nonPawnsVulnerableTilesSet.addAll(opponentMove.getStart().getPiece().getPossibleVulnerableTiles(game, opponentMove.getStart()));
        }

        System.out.println("AFISAM DE LA getVULNERABLETILES");
        Piece lastHumanMovedPiece = lastHumanMoveMade.getEnd().getPiece();
        if (lastHumanMovedPiece == null){
            System.out.println("bug");
        }
        else {
            System.out.println("Last moved piece is " + lastHumanMovedPiece.getType() + " from " +
                    lastHumanMoveMade.getStart().getCoordinates() + "  to  " + lastHumanMoveMade.getEnd().getCoordinates());
            for (Piece piece : opponentPieces){
                if (piece != lastHumanMovedPiece){
                    System.out.println("piesa  " + piece.getType() + " genereaza vulnerable tiles urmatoare:");
                    ArrayList<Tile> nextVulnerableTiles = piece.getPossibleVulnerableTiles(game, game.findTheTile(game.getChessboard(), piece));
                    if (nextVulnerableTiles.size() > 0){
                        for (Tile tile : nextVulnerableTiles){
                            System.out.println(tile.getCoordinates());
                        }
                        vulnerableTiles.addAll(nextVulnerableTiles);
                    }
                }
            }
        }



        //// add more moves : mutarile pe care le pot face aceste piese
        // peste piesele de aceeasi culoare care nu s mutate
        // pentru ca daca computerul va lua acea piesa ea devina vulnerabila
        // fara ca acest caz sa fie acoperit mai sus


        /*
        // pawns
        HashSet<Tile> pawnsVulnerableTilesSet = new HashSet<>();
        for (Tile pawnTile : pawnsTiles){
            int coordX = pawnTile.getCoordX() - 1;
            int firstCoordY  = pawnTile.getCoordY() - 1;
            int secondCoordY = pawnTile.getCoordY() + 1;

            if (! game.getChessboard().indexOutOfBound(coordX, firstCoordY)){ // daca pionul ramane pe tabla
                pawnsVulnerableTilesSet.add(game.getChessboard().getBoard()[coordX][firstCoordY]);
            }

            if (! game.getChessboard().indexOutOfBound(coordX, secondCoordY)){
                pawnsVulnerableTilesSet.add(game.getChessboard().getBoard()[coordX][secondCoordY]);
            }

        }
        vulnerableTiles.addAll(pawnsVulnerableTilesSet);
        */

        return vulnerableTiles;
    }

    private Move pickTheBestMove(ArrayList<Move> validMoves) {
        Move bestAttackPick = null;
        int maxValue = 0;
        ArrayList<Move> nullMoves = new ArrayList<>(); // mutarile pe poztii libere
        int pieceToBeMovedValue; //= validMoves.get(0).getStart().getPiece().getValue(); // valoarea propriei piese

        /// TRY TO SET BEST ATTACK PICK
        for (Move move : validMoves){ // verificam toate mutarile posibile
            pieceToBeMovedValue = validMoves.get(0).getStart().getPiece().getValue(); // valoarea propriei piese

            if (move.getEnd().getPiece() == null){ // daca e pozitie libera
                nullMoves.add(move); // adaugam la multimea respectiva descrisa mai sus
            }
            // daca exista o piesa pe aceea patratica, ii comparam valoarea
            // pt a o captura pe cea mai buna.
            else{
                int candidateTakePieceValue = move.getEnd().getPiece().getValue();
                //System.out.println("ptbMove : " + pieceToBeMovedValue + "  \\ cand" + candidateTakePieceValue + "  \\ maxValue" + maxValue);
                // trebuie sa verificam insa daca e un schimb profitabil / echitabil
                if (candidateTakePieceValue > maxValue){// && c{
                    if (candidateTakePieceValue >= pieceToBeMovedValue) {
                        maxValue = move.getEnd().getPiece().getValue();
                        bestAttackPick = move;
                        //System.out.println("here ?");
                    }
                    else { // verificam daca patratul unde poate ajunge adv e safe
                        ArrayList<Piece> opponentPieces = game.getChessboard().storePieces(true); // piesele omului
                        ArrayList<Move> validOpponentMoves = game.generateAllValidMoves(opponentPieces);
                        boolean weCan = true;
                        for (Move opponentMove : validOpponentMoves){
                            if (opponentMove.getEnd() == move.getEnd()){ // daca unde ajungem noi daca facem aceasta mutare
                                // poate ajunge el tura viitoare NU MUTAM ACOLO
                                weCan = false;
                                break;
                            }
                        }
                        if (weCan){
                            maxValue = move.getEnd().getPiece().getValue();
                            bestAttackPick = move;
                        }
                    }


                }

            }
        }


        /// TRY TO SET BEST DEFENCE PICK
        int valueOfMyMostImportantVulnerablePiece = 0;
        Tile tileOfMyMostImportantVulnerablePiece = null;
        Move bestDefencePick = null;
        ArrayList<Piece> opponentPieces = game.getChessboard().storePieces(true); // piesele omului
        ArrayList<Move> validOpponentMoves = game.generateAllValidMoves(opponentPieces);


        System.out.println("comp valid moves : ");
        System.out.println(validMoves);
        System.out.println("human valid moves: ");
        System.out.println(validOpponentMoves);


        ArrayList<Tile> vulnerableTiles = new ArrayList<>();
        for (Move opponentMove : validOpponentMoves){
            vulnerableTiles.add(opponentMove.getEnd()); // adaugam pozitia unde poate ajunge adversarul la lista pozitiilor vulnerabile

            Piece candidateMyPiece = opponentMove.getEnd().getPiece();
            if (candidateMyPiece != null){ // daca avem o piesa pe o pozitie vulnerabila
                int valueOfVulnerablePiece = candidateMyPiece.getValue();
                if (valueOfVulnerablePiece > valueOfMyMostImportantVulnerablePiece){
                    valueOfMyMostImportantVulnerablePiece = valueOfVulnerablePiece;
                    for (int i = 0; i < 8; i++){
                        for (int j = 0; j < 8; j++){
                            if (game.getChessboard().getBoard()[i][j].getPiece() == candidateMyPiece){
                                tileOfMyMostImportantVulnerablePiece = game.getChessboard().getBoard()[i][j];
                            }
                        }
                    }
                }
            }
        }

        // acum avem in tileOfMyMostImportantVulnerablePiece patratica cu cea mai vulnerabila piesa
        // trebuie sa o mutam
        for (Move myDefenceMove : validMoves){
            if (myDefenceMove.getStart() == tileOfMyMostImportantVulnerablePiece){ // daca suntem pe patratica mentionata mai sus
                boolean weCan = true;
                for (Tile vulnerableTile : vulnerableTiles){ // verificam daca cu aceasta mutare putem ajunge intr-o pozitie nevulnerabila
                    if (myDefenceMove.getEnd() == vulnerableTile){
                        weCan = false;
                        break;
                    }
                }
                if (weCan == true){
                    bestDefencePick = myDefenceMove;
                }
            }
        }

        // daca nu am putut alege o mutare de atac buna
        if (bestDefencePick == null){ // mutam cea mai puternica piesa, undeva unde n o poate lua nimeni
            int size = nullMoves.size();
            Random random = new Random();
            bestDefencePick = nullMoves.get(random.nextInt(size));
        }



        System.out.println("max value : " + maxValue + " \\ vulnarableValue :" +  valueOfMyMostImportantVulnerablePiece);
        Move bestPick;
        // acum avem si bestAttackPick / bestDefencePick
        if (maxValue > valueOfMyMostImportantVulnerablePiece){ // daca piesa pe care o putem captura
            // valoreaza mai mult decat valoarea cea mai vulnerabilei piese
            bestPick = bestAttackPick;
        }
        else {
            bestPick = bestDefencePick;
        }

        return bestPick;
    }


    private boolean isCheckMate(Player playerToMove) {
        return false;
    }

}
