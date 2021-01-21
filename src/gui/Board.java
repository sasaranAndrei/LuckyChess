package gui;

import board.*;
import console.Command;
import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Board implements MouseListener {
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


        // from game logic
        private Tile startTile;
        private Tile endTile;
        private ArrayList<Tile> validEndTiles = new ArrayList<>();
        private ArrayList<Move> validMoves = new ArrayList<>();


        private Piece humanMovedPiece;

        private Game game;
        private Dice dice = new Dice();
        private Start startGUI;


    private void loadIcons (){ // load DICE ICONS
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

    public Board(String humanPlayerName, Start startGUI) throws HeadlessException {

        loadIcons(); // dices icons
        game = new Game(humanPlayerName);
        startGUI = startGUI;

        this.mainFrame = new JFrame("LUCKY CHESS");
        this.mainFrame.setResizable(false);
        this.mainFrame.setSize(700,700); // [600,600]
        this.mainFrame.setLayout(new BorderLayout());
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // COMPUTER PANEL
        this.computerPanel = new ComputerPanel(this);
        this.mainFrame.add(computerPanel, BorderLayout.NORTH);
        // BOARD PANEL
        this.boardPanel = new BoardPanel(game, this);
        this.mainFrame.add(boardPanel, BorderLayout.CENTER);
        // HUMAN PANEL
        this.humanPanel = new HumanPanel(humanPlayerName, this);
        this.mainFrame.add(humanPanel, BorderLayout.SOUTH);

        this.mainFrame.setVisible(true);

        this.humanPlayerName = humanPlayerName;

        ///// afisam utilizatorului roll dices
        ((HumanPanel) humanPanel).drawRollDices();

        loadPieceIcons();

        ///////////this.mainFrame.setVisible(false);
        //this.mainFrame.setVisible(false);

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

        if (player.isHuman()){
            HumanPanel humanPanel = (HumanPanel) this.humanPanel;
            humanPanel.drawUpdated(diceIcons.get(firstDice), diceIcons.get(secondDice), dice.getRule());
                    /*
            humanPanel.getFirstDiceLabel().setIcon(diceIcons.get(firstDice));
            humanPanel.getSecondDiceLabel().setIcon(diceIcons.get(secondDice));
            humanPanel.getRuleDescriptionLabel().setText(dice.getRule());
                     */
        }
        else {
            ComputerPanel computerPanel = (ComputerPanel) this.computerPanel;
            computerPanel.drawUpdated(diceIcons.get(firstDice), diceIcons.get(secondDice), dice.getRule());

            /*
            // dices
            computerPanel.getFirstDiceLabel().setIcon(diceIcons.get(firstDice));
            computerPanel.getSecondDiceLabel().setIcon(diceIcons.get(secondDice));
            // rule
            computerPanel.getRuleDescriptionLabel().setText(dice.getRule());

             */
        }

    }

    /*
        Tinand cont ca doar utilizatorul are de apasat pe interfata
        in mouseClicked vom efectua o mutare a utilizatorului, urmata de o mutare a calculatorului

        Dupa iful legat de event.getSource, vom converti while-ul
     */

    /// THATS THE LOOP FUNCTION RIGHT THERE (I GUESS)
    boolean humanClickedOnRoll = false;
    boolean humanHasMoved = false;

    boolean computerClickedOnRoll = false;
    boolean computerHasMoved = false;

    int rowCounter = 0;

    ///// THE DICE GAME LOGIC
    boolean humanDiceActivated = true;
    boolean computerDiceActivated = true;

    /// rule 2 / 12
    boolean humanIsWhite = true;    // human = WHITE & computer = BLACK if true
                                    // human = BLACK & computer = WHITE if false
    /// rule 3 / 11
    int counterForRuleNo3 = 0;
//    Dimension lastWhiteQueen = new Dimension(-1, -1); // folosim dimension pt a sttoca
//    Dimension lastBlackQueen = new Dimension(-1, -1); // 2 coordonate
    Piece whiteQueen, blackQueen; // references to the queens.
    int lastWhiteQueenX = -1; // uninitialize
    int lastWhiteQueenY = -1; // uninitialize
    int lastBlackQueenX = -1; // uninitialize
    int lastBlackQueenY = -1; // uninitialize

    /// rule 4 / 10
    boolean rookDiagonal = false; // true cand se da 4 / 10

    /// rule 5 / 9
    boolean canSwap = false; // true cand se da 5 / 9

    /// rule 6 / 8
    int humanMagicPoints = 0;
    int computerMagicPoints = 0;

    // last moves
    Move lastHumanMoveMade = null;
    Move lastComputerMoveMade = null;
    @Override
    public void mouseClicked(MouseEvent e) { // inainte de primu click tre sa afisam Roll DICES!!! -> prima data in constructor

        if (rowCounter % 2 == 0) game.setPlayerToMove(game.getHumanPlayer());
        else game.setPlayerToMove(game.getComputerPlayer());

        if (isCheckMate(game.getPlayerToMove())){
            if (game.getPlayerToMove().isHuman()){
                JOptionPane.showMessageDialog(mainFrame, "GAME OVER, HUMAN PLAYER!");
            }
            else {
                System.out.println("GAME OVER, COMPUTER PLAYER!");
            }
            //try { Thread.sleep(4000); } catch (InterruptedException ex) { ex.printStackTrace(); }

            //mainFrame.setVisible(false);
            //startGUI.getStartFrame().setVisible(true);
            //mainFrame.dispose();
        }

        if (game.getPlayerToMove().isHuman()){ // daca i tura lui
            // si no dat roll i zicem sa dea
            if (!humanClickedOnRoll){
                HumanPanel humanPanel = (HumanPanel) this.humanPanel;
                if (e.getSource() == humanPanel){
                    dice.rollDice();
                    setDices(game.getHumanPlayer());

                    if (humanDiceActivated){
                        int diceRuleIndex = dice.getFirstDice() + dice.getSecondDice();
                        manangeRules(game.getHumanPlayer(), diceRuleIndex);
                    }
                    else {
                        humanPanel.disableDice();
                        humanDiceActivated = true;
                    }

                    humanClickedOnRoll = true;

                    //System.out.println(humanHasMoved);

                }
            }

            else if (!humanHasMoved) { // daca o dat click pe roll, il punem sa mute si stabilim daca o mutatstabilim daca o mutat

                //TODO tile click logic
                for (int i = 0; i < 8; i++){
                    for (int j = 0; j < 8; j++){
                        if (e.getSource() == boardPanel.tileBoard[i][j]){ // daca o fost apasat acest tilePanel

                            TilePanel tilePanel = boardPanel.tileBoard[i][j];
                            //System.out.println("cine muta " + game.getPlayerToMove().getName());


                            if (game.getPlayerToMove().isHuman()){ // muta omul
                                if (isLeftMouseButton(e)) { // select a move
                                    if (startTile == null) { // first click => startTile
                                        System.out.println("u here????? first click");

                                        startTile = game.getChessboard().getBoard()[tilePanel.getCoordX()][tilePanel.getCoordY()];
                                        humanMovedPiece = startTile.getPiece();

                                        if (humanMovedPiece == null) { // daca o selectat o patratica care nu i buna
                                            startTile = null; // resetam patratica aleasa

                                        } else { // afisam mutarile bune posibile
                                            validMoves = game.generateValidMoves(startTile, humanIsWhite);

                                            // maybe here
                                            if (rookDiagonal){

                                                if (startTile.getPiece() instanceof Rook) {
                                                    // tinem referinta catre rook
                                                    Piece rook = startTile.getPiece();
                                                    game.getChessboard().removePieceFromTile(startTile);
                                                    // punem un bishop ca sa vedem mutarile posibile
                                                    Piece bishop = new Bishop(rook.white);
                                                    startTile.setPiece(bishop);
                                                    validMoves.addAll(bishop.generateValidMoves(game, startTile, humanIsWhite));
                                                    // dupa ce am adaugat mutarile posibile, punem la loc tura
                                                    game.getChessboard().removePieceFromTile(startTile);
                                                    startTile.setPiece(rook);

                                                    rookDiagonal = false; // gata mutarea speciala, pregatim variabila booleana pt calculator

                                                    /*
                                                    ////// so... a facut un valid move, tre sa comportam ca atare
                                                    boardPanel.drawBoard(game);
                                                    // afisam tabla dupa ce am mutat noi
                                                    System.out.println(game.getChessboard());
                                                    // astea tre sa le facem in mom in care termina mutarea
                                                    startTile = null;
                                                    endTile = null;
                                                    validEndTiles.clear();
                                                    // validMoves.clear() l am mutat mai jos ca sa nu dea ConcurrentException
                                                    humanHasMoved = true;

                                                    humanClickedOnRoll = false;
                                                    humanHasMoved = false;

                                                    rowCounter++;
                                                    // i spunem pc ului sa roll dices
                                                    ((ComputerPanel) computerPanel).drawRollDices();

                                                    // si dam click pe el.
                                                    clickOnComputerPanel();
                                                     */
                                                }
                                            }

                                            // up there

                                            for (Move move : validMoves) {
                                                validEndTiles.add(move.getEnd());
                                            }


                                            highlightTiles(validEndTiles);
                                        }
                                    }
                                    else{

                                        System.out.println(startTile.getCoordinates());
                                        // second click => endTile
                                        endTile = game.getChessboard().getBoard()[tilePanel.getCoordX()][tilePanel.getCoordY()];

                                        if (tilePanel.itWasClickedAnEndTile(endTile)) {

                                            for (Move validMove : validMoves) {
                                                if (endTile == validMove.getEnd()) {

                                                    game.makeMove(validMove);
                                                    lastHumanMoveMade = validMove;
                                                    // he made a valid move so....

                                                    boardPanel.drawBoard(game);
                                                    // afisam tabla dupa ce am mutat noi
                                                    System.out.println(game.getChessboard());
                                                    // astea tre sa le facem in mom in care termina mutarea
                                                    startTile = null;
                                                    endTile = null;
                                                    validEndTiles.clear();
                                                    // validMoves.clear() l am mutat mai jos ca sa nu dea ConcurrentException
                                                    humanHasMoved = true;

                                                    humanClickedOnRoll = false;
                                                    humanHasMoved = false;

                                                    rowCounter++;
                                                    // i spunem pc ului sa roll dices
                                                    ((ComputerPanel) computerPanel).drawRollDices();

                                                    // si dam click pe el.
                                                   clickOnComputerPanel();

                                                   // if something wrong commment break;
                                                   break;
                                                }
                                            }
                                            validMoves.clear();
                                        }
                                        else {  // ori o mutat gresit
                                                // ori vrea sa facam interschimbarea de la regula 5 / 9
                                                // ori vrea sa mute tura regula 4 / 10
                                            if (canSwap){
                                                // daca vrea sa faca schimbu Cal -> Nebun
                                                if (startTile.getPiece() instanceof Knight && endTile.getPiece() instanceof Bishop){
                                                    Piece knight = startTile.getPiece();
                                                    Piece bishop = endTile.getPiece();
                                                    game.getChessboard().removePieceFromTile(startTile);
                                                    game.getChessboard().removePieceFromTile(endTile);
                                                    startTile.setPiece(bishop);
                                                    endTile.setPiece(knight);
                                                    /// aici sfarseste interschimbarea

                                                    canSwap = false; // gata interschimbarea, pregatim variabila booleana pt calculator

                                                    ////// so... a facut un valid move, tre sa comportam ca atare
                                                    boardPanel.drawBoard(game);
                                                    // afisam tabla dupa ce am mutat noi
                                                    System.out.println(game.getChessboard());
                                                    // astea tre sa le facem in mom in care termina mutarea
                                                    startTile = null;
                                                    endTile = null;
                                                    validEndTiles.clear();
                                                    // validMoves.clear() l am mutat mai jos ca sa nu dea ConcurrentException
                                                    humanHasMoved = true;

                                                    humanClickedOnRoll = false;
                                                    humanHasMoved = false;

                                                    rowCounter++;
                                                    // i spunem pc ului sa roll dices
                                                    ((ComputerPanel) computerPanel).drawRollDices();

                                                    // si dam click pe el.
                                                    clickOnComputerPanel();
                                                }
                                                // daca vrea sa faca schimbu Cal -> Nebun
                                                else if (startTile.getPiece() instanceof Bishop && endTile.getPiece() instanceof Knight){
                                                    Piece bishop = startTile.getPiece();
                                                    Piece knight = endTile.getPiece();
                                                    game.getChessboard().removePieceFromTile(startTile);
                                                    game.getChessboard().removePieceFromTile(endTile);
                                                    startTile.setPiece(knight);
                                                    endTile.setPiece(bishop);
                                                    /// aici sfarseste interschimbarea

                                                    // marcam mutarea valida, facem ce se cuvine
                                                    canSwap = false; // gata interschimbarea, pregatim variabila booleana pt calculator

                                                    ////// so... a facut un valid move, tre sa comportam ca atare
                                                    boardPanel.drawBoard(game);
                                                    // afisam tabla dupa ce am mutat noi
                                                    System.out.println(game.getChessboard());
                                                    // astea tre sa le facem in mom in care termina mutarea
                                                    startTile = null;
                                                    endTile = null;
                                                    validEndTiles.clear();
                                                    // validMoves.clear() l am mutat mai jos ca sa nu dea ConcurrentException
                                                    humanHasMoved = true;

                                                    humanClickedOnRoll = false;
                                                    humanHasMoved = false;

                                                    rowCounter++;
                                                    // i spunem pc ului sa roll dices
                                                    ((ComputerPanel) computerPanel).drawRollDices();

                                                    // si dam click pe el.
                                                    clickOnComputerPanel();
                                                }
                                                else { // daca ajunge aici inseamna ca nu poate efectua regula 5/9
                                                    // dar nu a apsatt pe cal / nebun, deci facem deselectie...
                                                    boardPanel.unHighlightTiles();

                                                    startTile = null;
                                                    endTile = null;
                                                    validEndTiles.clear();
                                                    validMoves.clear();

                                                    humanHasMoved = false;
                                                }



                                            }
                                            // mai jos <doar o mutat prost>
                                            else { // doar o mutat prost
                                                boardPanel.unHighlightTiles();

                                                startTile = null;
                                                endTile = null;
                                                validEndTiles.clear();
                                                validMoves.clear();

                                                humanHasMoved = false;
                                            }

                                        }
                                    }
                                } // if u de la isLeftMouseButton(e)
                                else if (isRightMouseButton(e)){ // deselect
                                    boardPanel.unHighlightTiles();

                                    // astea tre sa le facem in mom in care deselecteaza mutarea
                                    startTile = null;
                                    endTile = null;
                                    validEndTiles.clear();
                                    validMoves.clear();

                                    humanHasMoved = false;
                                }
                            }
                        }
                    }
                }
            }
            else { // tre sa resetam valoriile booleane
                // daca o dat roll si o mutat si iara i tura lui

                // unComment if it doesnt work in the "move" section
                //humanClickedOnRoll = false;
                //humanHasMoved = false;
            }

            // pana aici am facut sa dea roll si sa faca move.
        }

        else { // asta i practic tura calculatorului
            if (! computerClickedOnRoll){
                ComputerPanel computerPanel = (ComputerPanel) this.computerPanel;
                if (e.getSource() == computerPanel) {

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    dice.rollDice();
                    setDices(game.getComputerPlayer());

                    if (computerDiceActivated){
                        int diceRuleIndex = dice.getFirstDice() + dice.getSecondDice();
                        manangeRules(game.getComputerPlayer(), diceRuleIndex);
                    }
                    else {
                        computerPanel.disableDice();
                    }


                    clickOnComputerBoom();
                    computerClickedOnRoll = true;
                    clickOnComputerPanel();
                }
            }
            else if (! computerHasMoved){
                //ArrayList<Move> pcValidMoves = game.generateAllValidMoves(game.getChessboard().storePieces(!humanIsWhite), humanIsWhite);
                //Random random = new Random();
                //Move bestPick = pcValidMoves.get(random.nextInt(pcValidMoves.size()));
                //// end of the complex algoritm... ;)))

                // aici incercam sa implementam functia mai complexa putin
                ArrayList<Move> pcValidMoves = game.generateAllValidMoves(game.getChessboard().storePieces(!humanIsWhite), humanIsWhite);
                Move bestPick = algorithmMove(pcValidMoves, humanIsWhite);
                //Move bestPick = newPickTheBestMove(pcValidMoves, lastHumanMoveMade, humanIsWhite);

                // asteptam 2 secunde sa para ca se ganeste ce sa mute
                try { Thread.sleep(2000); } catch (InterruptedException exception) { exception.printStackTrace(); }
                game.makeMove(bestPick);
                lastComputerMoveMade = bestPick;

                // he made a valid move so....

                boardPanel.drawBoard(game);
                // afisam tabla dupa o mutat pcul
                System.out.println(game.getChessboard());
                rowCounter++;
                // si resetam valorile booleene
                computerClickedOnRoll = false;
                computerHasMoved = false;

                // chiar daca a avut tura asta dice u dezactivat, tura vitoare no sa aiba
                computerDiceActivated = true;

                // i spunem omului sa mute
                ((HumanPanel) humanPanel).drawRollDices();
            }

            /// dar am mutat o mai sus, dupa tura UTILIZATORULUI ca sa nu trebuiasca sa asteptam
            // functia asta de trigger de click ca sa faca el o mutare

        }



    }

    private Move algorithmMove(ArrayList<Move> pcValidMoves, boolean humanIsWhite) {
        Move bestPick = null;

        ArrayList<Move.BasicMove> basicMoves = new ArrayList<>();
        ArrayList<Move.AttackMove> attackMoves = new ArrayList<>();
        for (Move validMove : pcValidMoves){
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
        if (attackMoves.size() > 0) {
            for (Move.AttackMove attackMove : attackMoves) {

                Piece pieceThatAttack = attackMove.getStart().getPiece();
                Piece attackedPiece = attackMove.attackedPiece;

                if (attackedPiece.getValue() >= pieceThatAttack.getValue()) { // trade sau mai bine
                    int currentValueOfAttackMove = attackedPiece.getValue() - pieceThatAttack.getValue();
                    if (currentValueOfAttackMove > bestValueOfAttackMove) {
                        bestValueOfAttackMove = currentValueOfAttackMove;
                        bestAttackMove = attackMove;
                    }
                }
                else { // verificam daca piesa e neaparata. adica punem piesa noastra pe acea pozitie si generam validMoves ale adversarului
                    //Piece potentialTakenPiece = attackedPiece;
                    Tile attackedTile = game.findTheTile(game.getChessboard(), attackedPiece);
                    attackedTile.setPiece(pieceThatAttack); // incercam sa facem mutarea
                    // iar acuma generam valid movesurile ale jucatorului
                    boolean canMoveThere = true;
                    ArrayList<Move> opponentMoves = game.generateAllValidMoves(game.getChessboard().storePieces(humanIsWhite), humanIsWhite);
                    for (Move opponentMove : opponentMoves){
                        if (opponentMove.getEnd() == attackedTile){
                            // daca mi poate lua piesa dupa ce mut acolo,
                            // mai bine zic PAS
                            canMoveThere = false;
                            break;
                        }
                    }
                    if (canMoveThere) {
                        int currentValueOfAttackMove = attackedPiece.getValue();
                        if (currentValueOfAttackMove > bestValueOfAttackMove) {
                            bestValueOfAttackMove = currentValueOfAttackMove;
                            bestAttackMove = attackMove;
                        }
                    }
                    // dupa ce ne am terminat prostiile
                    // refacem totu cum era initial
                    attackedTile.setPiece(attackedPiece);
                }
            }
        }

        System.out.println("bestVAttakc " + bestValueOfAttackMove + " : " + bestAttackMove);

        // the more complex way to find the best basic move.
        // incercam sa salvam cea mai importanta piesa atacata
        ArrayList<Piece> ownPieces = game.getChessboard().storePieces( ! humanIsWhite);
        Collections.sort(ownPieces);
//        System.out.println("own pieces sorted by value");
//        for (Piece piece : ownPieces){
//            System.out.println(piece.getType());
//        }

        // le avem sortate

        // stabilim mutariile adversarului
        // ca sa vedem patratele vulnerabile


        ArrayList<Move> opponentMoves = game.generateAllValidMoves(game.getChessboard().storePieces(humanIsWhite), humanIsWhite);
        HashSet<Tile> vulnerableTiles = new HashSet<>();
        for (Move opponentMove : opponentMoves){
            vulnerableTiles.add(opponentMove.getEnd());
        }

        // sa vedem cea mai importanta piesa vulnerabila
        Piece mostImportantVulnerablePiece = null;
        boolean weFoundOne = false;
        for (Piece piece : ownPieces){
            Tile tileOfPiece = game.findTheTile(game.getChessboard(), piece);
            for (Tile vulnerableTile : vulnerableTiles){
                if (vulnerableTile == tileOfPiece){
                    mostImportantVulnerablePiece = piece;
                    weFoundOne = true;
                    break;
                }
            }
            if (weFoundOne){
                break;
            }
        }


        // find best basic move
        // generam o mutare safe
        Move bestBasicMove = null;
        int bestValueOfBasicMove = -1;
        if (weFoundOne && mostImportantVulnerablePiece != null){
            System.out.println(mostImportantVulnerablePiece.getType());

            Tile tileOfIt =  game.findTheTile(game.getChessboard(), mostImportantVulnerablePiece);
            // ii cautam un safe tile
            ArrayList<Move> safeMovesVulnerablePiece = findSafeMovesForVulnerablePiece(tileOfIt, basicMoves, vulnerableTiles);
            Random random = new Random();
            // alegem o mutare basic si safe random
            if (safeMovesVulnerablePiece.size() > 1){
                bestBasicMove = safeMovesVulnerablePiece.get(Math.abs(random.nextInt()) % safeMovesVulnerablePiece.size());
                bestValueOfBasicMove = mostImportantVulnerablePiece.getValue();

            }
        }



        if (bestValueOfBasicMove > bestValueOfAttackMove){ // daca piesa vulnerabila
            // valoreaza mai mult ca piesa pe care o putem captura
            bestPick = bestBasicMove;
        }
        else { // daca piesa pe care o putem captura / schimbul valoreaza mai mult
            // ca piesa pe care o putem pierde, mutam in atac
            bestPick = bestAttackMove;
        }

        Move randomPick = null;
        if (bestPick == null){ // daca nu putem captura nici o piesa
            // si nu avem nici o piesa amenintata, mutam safe si random
            int size = basicMoves.size();
            if (size > 0){

                boolean isSafeMove;
                do {
                    isSafeMove = true; // presupunem ca i true

                    Random random = new Random();
                    // luam o mutare basic random
                    randomPick = basicMoves.get(random.nextInt(size));

                    // vedem daca e safe
                    Piece pieceFromStartTile = randomPick.getStart().getPiece();

                    Tile endTile = randomPick.getEnd();
                    Piece pieceFromEndTile = endTile.getPiece();

                    endTile.setPiece(pieceFromStartTile); // incercam sa facem mutarea

                    // iar acuma generam valid movesurile ale jucatorului
                    //boolean canMoveThere = true;
                    opponentMoves = game.generateAllValidMoves(game.getChessboard().storePieces(humanIsWhite), humanIsWhite);
                    for (Move opponentMove : opponentMoves){
                        if (opponentMove.getEnd() == endTile){
                            // daca mi poate lua piesa dupa ce mut acolo,
                            // mai bine zic PAS
                            // canMoveThere = false;
                            isSafeMove = false;
                            break;
                        }
                    }
                    // dupa ce ne am terminat prostiile
                    // refacem totu cum era initial
                    endTile.setPiece(pieceFromEndTile);
                } while (! isSafeMove);
            }

            bestPick = randomPick;
        }

        return bestPick;
    }

    private ArrayList<Move> findSafeMovesForVulnerablePiece(Tile tileOfIt, ArrayList<Move.BasicMove> basicMoves, HashSet<Tile> vulnerableTiles) {
        ArrayList<Move> safeMoves = new ArrayList<>();
        for (Move basicMove :  basicMoves){ // mergem pe la toate mutarile
            if (basicMove.getStart() == tileOfIt){ // tile ul unde i piesa notSafe

                boolean isSafeTile = true;
                for (Tile vulnerableTile : vulnerableTiles){
                    if (basicMove.getEnd() == vulnerableTile){ // nu e safe
                        isSafeTile = false;
                        break;
                    }
                }

                if (isSafeTile){ // move care are startTile = unde i piesa si endTile = safe
                    safeMoves.add(basicMove);
                }
            }
        }
        return safeMoves;
    }

    private void manangeRules(Player player, int diceRuleIndex) {
        if (player.isHuman()){
            switch (diceRuleIndex){ // HUMAN CASE
                // rule 2 / 12
                case 2 : case 12 : { // SWITCH THE BOARD
                    switchSides();
                    rotateQueens(); // coordinates for rule no 3/11
                    break;
                }
                // rule 3 / 11
                case 3 : case 11 : { // HIDE / SHOW QUEENS

                    if (counterForRuleNo3 % 2 == 0){ // dispar reginele
                        findLastQueens();
                        hideQueens();
                    }
                    else { // apar reginele
                        //showQueens();
                        randomShowQueens();
                    }
                    counterForRuleNo3++;

                    break;

                }
                // rule 4 / 10
                case 4 : case 10 : { // ROOK CAN MOVE LIKE A BISHOP TOO
                    rookDiagonal = true;
                    break;
                }
                // rule 5 / 9
                case 5 : case 9 : { // CAN SWAP A KNIGHT WITH A BISHOP
                    canSwap = true;
                    break;
                }
                // rule 6 / 8
                case 6 : case 8 : { // MAGIC POINTS => BOOM
                    humanMagicPoints++;
                    ((HumanPanel) humanPanel).updateMagicPoints(humanMagicPoints);
                    break;
                }
                case 7 : break; // NO RULE

                default : {
                    System.out.println("DICE ERROR INDEX");
                    break;
                }
            }
        }
        else {
            switch (diceRuleIndex){ // COMPUTER CASE
                // rule 2 / 12
                case 2 : case 12 : {
                    switchSides();
                    rotateQueens(); // coordinates for rule no 3/11
                    break;
                }
                // rule 3 / 11
                case 3 : case 11 : {

                    if (counterForRuleNo3 % 2 == 0){ // dispar reginele
                        findLastQueens();
                        hideQueens();
                    }
                    else { // apar reginele
                        //showQueens();
                        randomShowQueens();
                    }
                    counterForRuleNo3++;

                    break;
                }
                // rule 4 / 10
                case 4 : case 10 : {
                    rookDiagonal = true;
                    break;
                }
                // rule 5 / 9
                case 5 : case 9 : {
                    canSwap = true;
                    break;
                }
                // rule 6 / 8
                case 6 : case 8 : {
                    computerMagicPoints++;
                    ((ComputerPanel) computerPanel).updateMagicPoints(computerMagicPoints);
                    break;
                }
                case 7 : break;

                default : {
                    System.out.println("DICE ERROR INDEX");
                    break;
                }
            }
        }
    }

    private void randomShowQueens() {
        Random random = new Random();
        int whiteQueenX, whiteQueenY;
        do {
            do {
                whiteQueenX = random.nextInt() % 8;
                whiteQueenY = random.nextInt() % 8;
                // cat timp nu gasim o pozitie libera, cautam alta
            } while (game.getChessboard().indexOutOfBound(whiteQueenX, whiteQueenY));

        } while (game.getChessboard().getBoard()[whiteQueenX][whiteQueenY].isTileOccupied());

        int blackQueenX, blackQueenY;
        do {
            do {
                blackQueenX = random.nextInt() % 8;
                blackQueenY = random.nextInt() % 8;
                // cat timp nu gasim o pozitie libera, cautam alta
            } while (game.getChessboard().indexOutOfBound(blackQueenX, blackQueenY));
        } while (game.getChessboard().getBoard()[blackQueenX][blackQueenY].getPiece() != null);

        // daca am ajuns aici avem 2 pozitii valide pt unde vor aparea reginele
        System.out.println("the random values:");
        System.out.println(whiteQueenX + " " + whiteQueenY);
        System.out.println(blackQueenX + " " + blackQueenY);

        if (whiteQueen != null) game.getChessboard().getBoard()[whiteQueenX][whiteQueenY].setPiece(whiteQueen);
        if (blackQueen != null) game.getChessboard().getBoard()[blackQueenX][blackQueenY].setPiece(blackQueen);

        boardPanel.drawBoard(game);
    }

    //TODO when the board is rotating we have to rotate this positions too
    private void rotateQueens(){
        // white queen
        lastWhiteQueenX = 7 - lastWhiteQueenX;
        lastWhiteQueenY = 7 - lastWhiteQueenY;
        // black queen
        lastBlackQueenX = 7 - lastBlackQueenX;
        lastBlackQueenY = 7 - lastBlackQueenY;
    }

    private void showQueens() {
        if (lastWhiteQueenX != -1) game.getChessboard().getBoard()[lastWhiteQueenX][lastWhiteQueenY].setPiece(whiteQueen);
        if (lastBlackQueenX != -1) game.getChessboard().getBoard()[lastBlackQueenX][lastBlackQueenY].setPiece(blackQueen);
        boardPanel.drawBoard(game);
    }

    private void hideQueens() {
        if (lastWhiteQueenX != -1){ // daca regina alba NU a fost capturata

            if (lastBlackQueenX != -1){ // daca ambele regine NU au fost capturate
                // atunci le facem sa dispara pe ambele
                // save pointers
                whiteQueen = game.getChessboard().getBoard()[lastWhiteQueenX][lastWhiteQueenY].getPiece();
                blackQueen = game.getChessboard().getBoard()[lastBlackQueenX][lastBlackQueenY].getPiece();
                // erase from board
                game.getChessboard().getBoard()[lastWhiteQueenX][lastWhiteQueenY].setPiece(null);
                game.getChessboard().getBoard()[lastBlackQueenX][lastBlackQueenY].setPiece(null);
            }
            else { // daca doar regina neagra a fost caputarata
                // o facem pe cea alba sa dispara
                // save pointer
                whiteQueen = game.getChessboard().getBoard()[lastWhiteQueenX][lastWhiteQueenY].getPiece();
                // erase from board
                game.getChessboard().getBoard()[lastWhiteQueenX][lastWhiteQueenY].setPiece(null);
            }

        }
        else { // daca regina alba a fost capturata

            if (lastBlackQueenX != -1){ // dar regina neagra NU au fost capturata
                // o facem pe cea neagra sa dispara
                // save pointer
                blackQueen = game.getChessboard().getBoard()[lastBlackQueenX][lastBlackQueenY].getPiece();
                // erase from board
                game.getChessboard().getBoard()[lastBlackQueenX][lastBlackQueenY].setPiece(null);
            }
            // daca ambele au fost caputarate... nu se intampla nimic
        }
        boardPanel.drawBoard(game);
    }

    private void findLastQueens() {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece piece = game.getChessboard().getBoard()[i][j].getPiece();
                if (piece != null){
                    // regina alba
                    if (piece.getType().equals("Q")){
                        lastWhiteQueenX = i;
                        lastWhiteQueenY = j;
                        //System.out.println(piece.getType() + "->" + lastWhiteQueenX + " " + lastWhiteQueenY);
                    }
                    // regina neagra
                else if (piece.getType().equals("q")){
                        lastBlackQueenX = i;
                        lastBlackQueenY = j;
                        //System.out.println(piece.getType() + "->" + lastBlackQueenX + " " + lastBlackQueenY);
                    }
                }

            }
        }
    }

    private void switchSides() {
        // deocamdata facem redraw si dupa vedem ce chestii de logica mai tre sa implementam.
        // ca nu stiu exact unde i duma cu alb = human
        //TODO ce scrie mai sus
        game.switchSides();
        System.out.println(game.getChessboard());
        humanIsWhite = !humanIsWhite; // daca era alb -> negru, si invers
        boardPanel.drawBoard(game);

    }

    public void clickOnComputerBoom (){
        Component boomButton = ((ComputerPanel) this.computerPanel).boomButton;
        Point boomButtonPoint = boomButton.getLocationOnScreen();

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robot.mouseMove(boomButtonPoint.x + 3, boomButtonPoint.y + 3); // coltu stanga sus
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);


    }

    public void clickOnComputerPanel () {
        Component computerPanel = this.computerPanel;
        Point computerPanelPoint = computerPanel.getLocationOnScreen();

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robot.mouseMove(computerPanelPoint.x + 3, computerPanelPoint.y + 3); // coltu stanga sus
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }


    /*
    public void computerTurn (){
        // daca e computerPlayer
        // asteptam sa treaca o secunda.


        try { Thread.sleep(2000); } catch (InterruptedException exception) { exception.printStackTrace(); }
        // afisam rollu
        ((ComputerPanel) this.computerPanel).drawRollDices();// si asteptam o secunda -> pentru estitica
        try { Thread.sleep(2000); } catch (InterruptedException exception) { exception.printStackTrace(); }
        dice.rollDice();
        setDices(game.getComputerPlayer());
        System.out.println("Am actualizat");
        /// acuma face move.
        //TODO a little more complex algorithm...
        ArrayList<Move> pcValidMoves = game.generateAllValidMoves(game.getChessboard().storePieces(false));
        Random random = new Random();
        Move bestPick = pcValidMoves.get(random.nextInt(pcValidMoves.size()));
        //// end of the complex algoritm... ;)))
        // asteptam 2 secunde sa para ca se ganeste ce sa mute
        try { Thread.sleep(2000); } catch (InterruptedException exception) { exception.printStackTrace(); }
        game.makeMove(bestPick);
        // he made a valid move so....

        boardPanel.drawBoard(game);
        // afisam tabla dupa o mutat pcul
        System.out.println(game.getChessboard());

        // la sfarsitul turei lui, pregatim noua tura a UTILIZATORULUI.
        game.setPlayerToMove(game.getHumanPlayer());
        ///// afisam utilizatorului roll dices
        ((HumanPanel) humanPanel).drawRollDices();

    }
    */


    //////////////////////VVVV///////////////////////
    //////////////////////||||///////////////////////
    /////////////////////////////////////////////////
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
    //////////////////////////////////////////////
    ///////////////////////////////////////////////

    private class HumanPanel extends JPanel{
        JLabel nameLabel = new JLabel("sosy");
        JLabel ruleDescriptionLabel = new JLabel("");
        JLabel firstDiceLabel = new JLabel("");
        JLabel secondDiceLabel = new JLabel("");
        JLabel magicPointsLabel = new JLabel("MAGIC POINTS : 0");
        JButton boomButton = new JButton("BOOM");

        HumanPanel (String name, Board thisBoard){
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
            addMouseListener(thisBoard);
            validate();
            boomButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (humanMagicPoints >= 3){ // la 3 magic points
                        // poate activa butonul de boom
                        computerDiceActivated = false;
                        humanMagicPoints -= 3;
                        updateMagicPoints(humanMagicPoints);
                    }
                }
            });
        }

        public void drawRollDices (){
            //this.removeAll();
            //firstDiceLabel.setIcon(null);
            //secondDiceLabel.setIcon(null);
            firstDiceLabel.setIcon(diceIcons.get(dice.getFirstDice()));
            secondDiceLabel.setIcon(diceIcons.get(dice.getSecondDice()));
            //TODO scrisu cu albastru pt ROLL faze
            Font rollFont = new Font("Serif", Font.BOLD, 20);
            ruleDescriptionLabel.setForeground(Color.BLUE);
            ruleDescriptionLabel.setFont(rollFont);
            ruleDescriptionLabel.setText("Your turn! Click the dices to roll 'em!");
            this.validate();
            this.repaint();
        }

        public void drawUpdated(ImageIcon firstDice, ImageIcon secondDice, String rule) {
            //this.removeAll();
            // set the things
            //humanPanel.drawUpdated(diceIcons.get(firstDice), diceIcons.get(secondDice), dice.getRule());

            firstDiceLabel.setIcon(firstDice);
            secondDiceLabel.setIcon(secondDice);

            //TODO scrisu cu negru pt BASIC UPDATED
            Font basicFont = new Font("Dialog", Font.BOLD, 12);
            ruleDescriptionLabel.setForeground(Color.BLACK);
            ruleDescriptionLabel.setFont(basicFont);
            ruleDescriptionLabel.setText(rule);

            this.validate();
            this.repaint();

        }

        public void updateMagicPoints(int humanMagicPoints) {
            magicPointsLabel.setText("MAGIC POINTS : " + humanMagicPoints);
            this.validate();
            this.repaint();
        }

        public void disableDice() {
            firstDiceLabel.setIcon(diceIcons.get(dice.getFirstDice()));
            secondDiceLabel.setIcon(diceIcons.get(dice.getSecondDice()));

            //TODO scrisu cu negru pt BASIC UPDATED
            Font disableFont = new Font("Serif", Font.BOLD, 20);
            ruleDescriptionLabel.setForeground(Color.RED);
            ruleDescriptionLabel.setFont(disableFont);
            ruleDescriptionLabel.setText("YOUR DICES POWER ARE DISABLED!");
            this.validate();
            this.repaint();
        }
    }

    private class ComputerPanel extends JPanel{
        JLabel nameLabel = new JLabel("LORD INATEUR");
        JLabel ruleDescriptionLabel = new JLabel("");
        JLabel firstDiceLabel = new JLabel("");
        JLabel secondDiceLabel = new JLabel("");
        JLabel magicPointsLabel = new JLabel("MAGIC POINTS : 0");
        JButton boomButton = new JButton("BOOM");

        ComputerPanel (Board thisBoard){
            super(new FlowLayout());

            nameLabel.setFont(new Font("LORD INATEUR", Font.BOLD, 20));

            this.add(ruleDescriptionLabel);
            this.add(firstDiceLabel);
            this.add(secondDiceLabel);
            this.add(magicPointsLabel);
            this.add(boomButton);

            this.add(nameLabel);
            setPreferredSize(COMPUTER_PANEL_DIMENSION);
            addMouseListener(thisBoard);
            boomButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (computerMagicPoints >= 3){ // la 3 magic points
                        // poate activa butonul de boom
                        humanDiceActivated = false;
                        computerMagicPoints -= 3;
                        updateMagicPoints(computerMagicPoints);
                    }
                    System.out.println(humanDiceActivated);
                }
            });
            validate();
        }


        public void drawRollDices (){
            //this.removeAll();
            //firstDiceLabel.setIcon(null);
            //secondDiceLabel.setIcon(null);
            firstDiceLabel.setIcon(diceIcons.get(dice.getFirstDice()));
            secondDiceLabel.setIcon(diceIcons.get(dice.getSecondDice()));

            //TODO scrisu cu albastru pt ROLL faze
            Font rollFont = new Font("Serif", Font.BOLD, 20);
            ruleDescriptionLabel.setForeground(Color.BLUE);
            ruleDescriptionLabel.setFont(rollFont);
            ruleDescriptionLabel.setText("Your turn! Click the dices to roll 'em!");
            this.validate();
            this.repaint();
        }

        public void drawUpdated(ImageIcon firstDice, ImageIcon secondDice, String rule) {
            //this.removeAll();
            // set the things
            //humanPanel.drawUpdated(diceIcons.get(firstDice), diceIcons.get(secondDice), dice.getRule());

            firstDiceLabel.setIcon(firstDice);
            secondDiceLabel.setIcon(secondDice);

            //TODO scrisu cu negru pt BASIC UPDATED
            Font basicFont = new Font("Dialog", Font.BOLD, 14);
            ruleDescriptionLabel.setForeground(Color.BLACK);
            ruleDescriptionLabel.setFont(basicFont);
            ruleDescriptionLabel.setText(rule);

            this.validate();
            this.repaint();

        }

        public void updateMagicPoints(int computerMagicPoints) {
            magicPointsLabel.setText("MAGIC POINTS : " + computerMagicPoints);
            this.validate();
            this.repaint();
        }

        public void disableDice() {
            firstDiceLabel.setIcon(diceIcons.get(dice.getFirstDice()));
            secondDiceLabel.setIcon(diceIcons.get(dice.getSecondDice()));

            //TODO scrisu cu negru pt BASIC UPDATED
            Font disableFont = new Font("Serif", Font.BOLD, 20);
            ruleDescriptionLabel.setForeground(Color.RED);
            ruleDescriptionLabel.setFont(disableFont);
            ruleDescriptionLabel.setText("YOUR DICES POWER ARE DISABLED!");
            this.validate();
            this.repaint();
        }
    }

    private class BoardPanel extends JPanel{
        TilePanel[][] tileBoard = new TilePanel[8][8];

        BoardPanel(Game game, Board thisBoard) {
            super(new GridLayout(8,8));
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    TilePanel tilePanel = new TilePanel(this, i, j, game, thisBoard); // aici porbabil voi face legutura cu controlleru cum ar vnei
                    tileBoard[i][j] = tilePanel;
                    this.add(tilePanel); // ?
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        private void drawBoard (Game game){
            this.removeAll();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    tileBoard[i][j].drawTile(game);
                    add(tileBoard[i][j]);
                }
            }
            this.validate();
            this.repaint();
        }

        private void unHighlightTiles (){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    tileBoard[i][j].unHighlight();
                }
            }
        }

    }

    private class TilePanel extends JPanel {
        private int coordX;
        private int coordY;
        private Color tileColor;
        //private ActionListener actionListener;

        TilePanel(BoardPanel boardPanel, int coordX, int coordY, Game game, Board thisBoard){
            super(new GridBagLayout());
            this.coordX = coordX;
            this.coordY = coordY;

            setPreferredSize(TILE_PANEL_DIMENSION);
            setTileColor(); // am facut asta in Tile
            setPieceIcon(game);

            /*
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isLeftMouseButton(e)){ // select a move
                        if (startTile == null){ // first click => startTile

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
                            endTile = game.getChessboard().getBoard()[coordX][coordY];

                            if (itWasClickedAnEndTile(endTile)){
                                for (Move validMove : validMoves){
                                    if (endTile == validMove.getEnd()){
                                        game.makeMove(validMove);
                                        //setPieceIcon(game);

                                    }
                                }
                            }
                            else endTile = null;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    boardPanel.drawBoard(game);
                                    // afisam tabla dupa ce am mutat noi
                                    System.out.println(game.getChessboard());

                                    /*
                                    // incercam sa efectuam miscarea computerului
                                    ArrayList<Move> pcValidMoves = game.generateAllValidMoves(game.getChessboard().storePieces(false));
                                    Random random = new Random();
                                    Move bestPick = pcValidMoves.get(random.nextInt(pcValidMoves.size()));

                                    // sleep a little sa para ca se gandeste
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    game.makeMove(bestPick);
                                    System.out.println(game.getChessboard());
                                    // *(/)
                                }
                            };
                        }



                    } else if (isRightMouseButton(e)){ // deselect
                        boardPanel.unHighlightTiles();
                        startTile = null;
                        endTile = null;
                        validEndTiles.clear();
                        validMoves.clear();
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

            */

            // new method
            //actionListener = thisBoard;
            addMouseListener(thisBoard);
            validate();
        }

        public int getCoordX() {
            return coordX;
        }

        public void setCoordX(int coordX) {
            this.coordX = coordX;
        }

        public int getCoordY() {
            return coordY;
        }

        public void setCoordY(int coordY) {
            this.coordY = coordY;
        }

        public Color getTileColor() {
            return tileColor;
        }

        public void setTileColor(Color tileColor) {
            this.tileColor = tileColor;
        }

        private boolean itWasClickedAnEndTile (Tile clickedTile){
            for (Tile validTile : validEndTiles){
                if (clickedTile == validTile){
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

        public void drawTile(Game game) {
            this.setTileColor();
            this.setPieceIcon(game);
            this.validate();
            this.repaint();
        }
    }

    // board methods :

    public void highlightTiles (ArrayList<Tile> tiles){
        for (Tile tile : tiles){
            int tileX = tile.getCoordX();
            int tileY = tile.getCoordY();
            boardPanel.tileBoard[tileX][tileY].highlight();
        }
    }


    public void playInterface (){
        while (rowCounter >= 30 || game.getStatus() == Game.Status.PLAY){

            if (rowCounter % 2 == 0) game.setPlayerToMove(game.getHumanPlayer());
            else game.setPlayerToMove(game.getComputerPlayer());

            //System.out.println(game.getChessboard());

            if (game.getPlayerToMove().isHuman()){ // daca omu muta
                if (humanHasMoved){
                    System.out.println(game.getChessboard());
                    humanHasMoved = false;
                    System.out.println("HE HAS MOVED !! DONT LISTEN TO INTELIJ");
                }
            }
            else { //if (! game.getPlayerToMove().isHuman()){ // calculatoru muta
                // ii luam piesele si generam mutarile posibile
                ArrayList<Move> pcValidMoves = game.generateAllValidMoves(game.getChessboard().storePieces(false), true);
                Random random = new Random();
                Move bestPick = pcValidMoves.get(random.nextInt(pcValidMoves.size()));


                // sleep a little sa para ca se gandeste
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                game.makeMove(bestPick);
                System.out.println(game.getChessboard());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            rowCounter++;
        }

    }




    //int switchSides = 0;
    /*
    /// this is for the console version
    public void playConsole (){
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
                            validMoves = game.generateValidMoves(sourceTile, humanIsWhite);
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


        System.out.println("here");
        if (game == null){
            System.out.println("game pl");
        }
        if (boardGUI == null){
            System.out.println("boardgui pl");
        }

    }
*/
    private Move newPickTheBestMove(ArrayList<Move> validMoves, Move lastHumanMoveMade, boolean humanIsWhite){
        //TODO in functie de humanIsWhite
        // daca  humanIsWhite => computer is Black
        // daca !humanIsWhite => computer is White


        HashSet<Piece> vulnerablePiecesSet = new HashSet<>();  // piesele vulnerabile ale PCului
        HashSet<Tile> vulnerableTiles = this.findVulnerableTiles(lastHumanMoveMade, humanIsWhite);
        for (Tile vulnerableTile : vulnerableTiles){
            if (vulnerableTile.getPiece() != null && (vulnerableTile.getPiece().isWhite() == !humanIsWhite)){
                vulnerablePiecesSet.add(vulnerableTile.getPiece());
            }
        }

        System.out.println("piesele vulnerabile");
        for (Piece piece : vulnerablePiecesSet){
            System.out.println(piece.getType());
        }
        System.out.println("patratele vulnerabile");
        for (Tile tile : vulnerableTiles){
            System.out.println(tile.getCoordinates());
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
            ArrayList<Move> possibleMovesForIt = game.generateValidMoves(tileOfIt, humanIsWhite);
            for (Move mustMove : possibleMovesForIt){
                boolean canMoveThere = findIfCanMove(mustMove, vulnerableTiles);
                if (canMoveThere){
                    bestBasicMove = mustMove;
                    break;
                }
            }
            //bestValueOfBasicMove = -10; // daca am ajuns aici inseamna ca piesa aia ii rip deci alegem bestAttackMove
        }

        else { // daca nu avem piese vulnerabile atacam
            bestPick = bestAttackMove;
        }

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
            System.out.println("safe moves:");
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

    private HashSet<Tile> findVulnerableTiles(Move lastHumanMoveMade, boolean humanIsWhite){
        //  humanIsWhite => storePiece(true)  <= color
        // !humanIsWhite => storePiece(false) <= color

        HashSet<Tile> vulnerableTiles = new HashSet<>();

        ArrayList<Piece> opponentPieces = game.getChessboard().storePieces(humanIsWhite); // piesele omului
        //// tre sa impartim intre pawn si notPawn pentru ca
        //// inafara de pawn, toate piesele captureaza la fel ca si cum muta

        //
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

        ArrayList<Move> validOpponentMoves = game.generateAllValidMoves(opponentPieces, humanIsWhite); // deocamdata
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
            for (Piece piece : opponentPieces){
                if (piece != lastHumanMovedPiece){
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
    // alea de mai sus is bune
    /*
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
    /// this is for the console version


     */

    private boolean isCheckMate(Player playerToMove) {
        if (playerToMove.isHuman()) { // daca i tura omului
            Tile humanKing = findKing(humanIsWhite);
            if (humanKing == null){ // nu l o gasit deci e sah mat pt human
                return true;
            }
        }
        else {
            Tile computerKing = findKing(!humanIsWhite);
            if (computerKing == null) {
                return true;
            }
        }
        return false;
    }

    private Tile findKing (boolean white){
        String kingToFind;
        if (white) kingToFind = "K";
        else kingToFind = "k";
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){

                Piece piece = game.getChessboard().getBoard()[i][j].getPiece();
                if (piece != null){

                    if (piece.getType().equals(kingToFind)){
                        return game.getChessboard().getBoard()[i][j];
                    }

                }

            }
        }
        return null;
    }

}
