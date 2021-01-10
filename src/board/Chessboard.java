//package com.example.luckychess.board;
package board;
/*                        0 . . . 7
    8                   0
    .                   .
    .             =>    .
    .                   .
    1                   7
      A . . . H

 */

//import com.example.luckychess.pieces.Pawn;

import pieces.*;

import java.util.ArrayList;

public class Chessboard {

    private Tile[][] board = new Tile[8][8];
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    public Chessboard() {
        boolean whiteTileColor = true;

        // big pieces tiles
        // rooks
        board[0][0] = new Tile(0, 0, new Rook(false)); // black
        board[0][7] = new Tile(0, 7, new Rook(false));
        board[7][0] = new Tile(7, 0, new Rook(true)); // white
        board[7][7] = new Tile(7, 7, new Rook(true));
        // knights
        board[0][1] = new Tile(0, 1, new Knight(false)); // black
        board[0][6] = new Tile(0, 6, new Knight(false));
        board[7][1] = new Tile(7, 1, new Knight(true)); // white
        board[7][6] = new Tile(7, 6, new Knight(true));
        // bishops
        board[0][2] = new Tile(0, 2, new Bishop(false)); // black
        board[0][5] = new Tile(0, 5, new Bishop(false));
        board[7][2] = new Tile(7, 2, new Bishop(true)); // white
        board[7][5] = new Tile(7, 5, new Bishop(true));
        // queens & kings
        board[0][3] = new Tile(0, 3, new Queen(false)); // black
        board[0][4] = new Tile(0, 4, new King(false));
        board[7][3] = new Tile(7, 3, new Queen(true)); // white
        board[7][4] = new Tile(7, 4, new King(true));

        for (int j = 0; j < 8; j++){ // pawn tiles
            board[1][j] = new Tile(1, j , new Pawn(false));
            board[6][j] = new Tile(6, j , new Pawn(true));
            whiteTileColor = !whiteTileColor;
        }

        whiteTileColor = true;

        for (int i = 2; i < 6; i++){ // empty rows
            for (int j = 0; j < 8; j++){
                board[i][j] = new Tile(i,j);
                whiteTileColor = !whiteTileColor; // <w> <b> ... <b>, whiteTileColor => <w> (next)
            }
            whiteTileColor = !whiteTileColor; // NEXT -> <b> <w> ... <w>
        }

        whitePieces = storePieces(true);
        blackPieces = storePieces(false);

    }


    public Chessboard(String template){
        if (template == "EMPTY"){
            boolean whiteTileColor = true;
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    board[i][j] = new Tile(i,j);
                    whiteTileColor = !whiteTileColor;
                }
                whiteTileColor = !whiteTileColor;
            }
        } else if (template == "TEST") {
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    board[i][j] = new Tile(i,j);
                }
            }
            // black pieces
            board[0][3].setPiece(new Queen(false));
            board[0][4].setPiece(new King(false));
            board[0][5].setPiece(new Bishop(false));
            board[0][6].setPiece(new Knight(false));
            board[0][7].setPiece(new Rook(false));
            board[1][4].setPiece(new Pawn(false));
            // white pieces
            board[7][3].setPiece(new Queen(true));
            board[7][4].setPiece(new King(true));
            board[7][5].setPiece(new Bishop(true));
            board[7][6].setPiece(new Knight(true));
            board[7][7].setPiece(new Rook(true));
            board[6][4].setPiece(new Pawn(true));


        }

        whitePieces = storePieces(true);
        blackPieces = storePieces(false);

    }

    public Chessboard(Tile[][] board) {
        this.board = board;
    }



    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    //////////////////////////////////////////////// PRINTING STUFF

    public void printEmptyChessboard (){
        for (int i = 0; i < 8; i++){
            System.out.print("|");
            for (int j = 0; j < 8; j++){
                System.out.print(String.format("%-5s "));
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < 8; i++){
            result += "|";
            for (int j = 0; j < 8; j++){
                result += (board[i][j] + " ");
            }
            result += '\n';
            for (int k = 0; k < 8; k++){
                result += "-----";
            }
            result += '\n';
        }
        result += '\n';
        return result;
    }

    public boolean indexOutOfBound(int i, int j) {
        if (i < 0 || i > 7) return true;
        if (j < 0 || j > 7) return true;
        return false;
    }

    //////////////////////////////////////////////// PRINTING STUFF
    //////////////////////////////////////////////// LOGIC STUFF

    public ArrayList<Piece> storePieces (boolean white){
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece piece = board[i][j].getPiece();
                if (piece != null && (piece.white == white)){
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public void removePieceFromTile (Tile tile)
    {
        int removeX = tile.getCoordX();
        int removeY = tile.getCoordY();
        this.getBoard()[removeX][removeY].setPiece(null);

    }


    //////////////////////////////////////////////// LOGIC STUFF


}
