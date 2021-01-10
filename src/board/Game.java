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

        ArrayList<Move> whiteStandardLegalMoves = generateAllValidMoves(chessboard.getWhitePieces());
        ArrayList<Move> blackStandardLegalMoves = generateAllValidMoves(chessboard.getBlackPieces());

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
    public void showValidMoves (Tile clickedTile) {
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
    private Tile findTheTile (Chessboard chessboard, Piece piece){
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
            allLegalMoves.addAll(piece.generateValidMoves(this, tileSpecific));
        }
        return allLegalMoves;
    }

    public void makeMove (Player player, Move move){
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








}
