//package com.example.luckychess.board;
package board;
//import android.util.Log;

import pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private Player humanPlayer;
    private Player computerPlayer;

    private Player playerToMove; // omu joaca cu albu mereu

    private Chessboard chessboard;

    private Status status = Status.PLAY;

    public enum Status {
        PLAY,
        WHITE_IN_CHECK,
        BLACK_IN_CHECK,
        WHITE_WIN,
        BLACK_WIN,
        TIE
    }


    public Game (String playerName){
        humanPlayer = new Player(playerName, true);
        playerToMove = humanPlayer;
        //chessboard = new Chessboard("EMPTY");
        chessboard = new Chessboard();
    }

    public Status getStatus() {
        return status;
    }

    public Player getPlayerToMove() {
        return playerToMove;
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








}
