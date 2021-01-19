//package com.example.luckychess.board;
package board;
//import android.util.Log;

import gui.Board;
import pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private Player humanPlayer;
    private Player computerPlayer;

    private Player playerToMove; // referinta catre jucatorul curent

    private Player winner;
    private Chessboard chessboard;

    //private Board boardGUI;
    private ArrayList<Piece> capturedPieces = new ArrayList<>();
    private ArrayList<Move> madeMoves = new ArrayList<>();
    private Status status = Status.PLAY;

    public enum Status {
        PLAY,
        WHITE_IN_CHECK,
        BLACK_IN_CHECK,
        WIN, // si jucatoru care a castigat in Player winner
        TIE
    }


    public Game (String playerName){
        //this.boardGUI = boardGUI;
        chessboard = new Chessboard();

        //ArrayList<Move> whiteStandardLegalMoves = generateAllValidMoves(chessboard.getWhitePieces());
        //ArrayList<Move> blackStandardLegalMoves = generateAllValidMoves(chessboard.getBlackPieces());

        //humanPlayer = new HumanPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        //computerPlayer = new ComputerPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        humanPlayer = new Player(playerName, true, true, (King) chessboard.getBoard()[7][4].getPiece());
        computerPlayer = new Player("LORD INATEUR", false, false, (King) chessboard.getBoard()[0][4].getPiece());
        playerToMove = humanPlayer;
        //his.play();
    }



    private boolean isCheckMate(Player playerToMove) {
        return false;
    }

    public Status getStatus() {
        return status;
    }

    public Player getPlayerToMove() {
        return playerToMove;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getComputerPlayer() {
        return computerPlayer;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }


    public void setPlayerToMove(Player playerToMove) {
        this.playerToMove = playerToMove;
    }





    // valid moves for printing.
    public ArrayList<Move> showValidMoves (Tile clickedTile) {
        ArrayList<Move> validMoves = this.generateValidMoves(clickedTile);
        Chessboard copyChessboard = new Chessboard(this.chessboard.getBoard()); // copy the chessboard to print valid moves
        // reset the valid moves
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                copyChessboard.getBoard()[i][j].setForValidMove(false);
            }
        }
        // mark the new valid moves
        for (Move validMove : validMoves){
            Tile markedTile = validMove.getEnd(); // let s mark the valid move on the chessboard
            int tileCoordX = markedTile.getCoordX();
            int tileCoordY = markedTile.getCoordY();
            copyChessboard.getBoard()[tileCoordX][tileCoordY].setForValidMove(true);
        }
        System.out.println(copyChessboard);
        return validMoves;
    }

    public void showAllValidMoves (ArrayList<Piece> pieces){
        for (Piece piece : pieces) {
            Tile tileSpecific = findTheTile(chessboard, piece);
            if (tileSpecific == null) {
                System.out.println("some bug right there ma man u know what am sayin");
            }
            this.showValidMoves(tileSpecific);
        }
    }

    // generate allValidMoves
    public Tile findTheTile (Chessboard chessboard, Piece piece){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (chessboard.getBoard()[i][j].getPiece() == piece){
                    return chessboard.getBoard()[i][j];
                }
            }
        }
        return null;
    }

    //arg = a tile from where it will be computed the valid moves for the piece on that tile
    public ArrayList<Move> generateValidMoves(Tile tileClicked){
        Piece currentPiece = tileClicked.getPiece();
        return currentPiece.generateValidMoves(this, tileClicked);
    }

    public ArrayList<Move> generateAllValidMoves (ArrayList<Piece> pieces){
        ArrayList<Move> allLegalMoves = new ArrayList<>();
        for (Piece piece : pieces){
            Tile tileSpecific = findTheTile(chessboard, piece);
            if (tileSpecific == null){
                System.out.println("some bug right there ma man u know what am sayin");
            }
            else {
                allLegalMoves.addAll(piece.generateValidMoves(this, tileSpecific));
            }
        }
        return allLegalMoves;
    }


    public void makeMove(Move move){
        madeMoves.add(move);
        Piece piece = move.getEnd().getPiece();
        if (piece != null) { // daca e o piesa e rip
            chessboard.removePieceFromTile(move.getEnd());
            piece.alive = false;
            capturedPieces.add(piece);
        }
        move.getEnd().setPiece(move.getStart().getPiece()); // mutam piesa care trebe
        chessboard.removePieceFromTile(move.getStart());
    }

    /*
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
    */








}
