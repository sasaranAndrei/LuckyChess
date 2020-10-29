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

import java.util.Arrays;

public class Chessboard {
    private Tile[][] board = new Tile[8][8];

    public Chessboard() {
        //board =
        // initialize chessboard
        boolean whiteTileColor = true;

        // big pieces tiles
        // rooks
        board[0][0] = new Tile(0, 0, true, new Rook(false)); // black
        board[0][7] = new Tile(0, 7, false, new Rook(false));
        board[7][0] = new Tile(7, 0, false, new Rook(true)); // white
        board[7][7] = new Tile(7, 7, true, new Rook(true));
        // knights
        board[0][1] = new Tile(0, 1, false, new Knight(false)); // black
        board[0][6] = new Tile(0, 6, true, new Knight(false));
        board[7][1] = new Tile(7, 1, true, new Knight(true)); // white
        board[7][6] = new Tile(7, 6, false, new Knight(true));
        // bishops
        board[0][2] = new Tile(0, 2, true, new Bishop(false)); // black
        board[0][5] = new Tile(0, 5, false, new Bishop(false));
        board[7][2] = new Tile(7, 2, false, new Bishop(true)); // white
        board[7][5] = new Tile(7, 5, true, new Bishop(true));
        // queens & kings
        board[0][3] = new Tile(0, 3, false, new Queen(false)); // black
        board[0][4] = new Tile(0, 4, true, new King(false));
        board[7][3] = new Tile(7, 3, true, new Queen(true)); // white
        board[7][4] = new Tile(7, 4, false, new King(true));

        for (int j = 0; j < 8; j++){ // pawn tiles
            board[1][j] = new Tile(1, j , !whiteTileColor, new Pawn(false));
            board[6][j] = new Tile(6, j , whiteTileColor, new Pawn(true));
            whiteTileColor = !whiteTileColor;
        }

        whiteTileColor = true;

        for (int i = 2; i < 6; i++){ // empty rows
            for (int j = 0; j < 8; j++){
                board[i][j] = new Tile(i,j,whiteTileColor);
                whiteTileColor = !whiteTileColor; // <w> <b> ... <b>, whiteTileColor => <w> (next)
            }
            whiteTileColor = !whiteTileColor; // NEXT -> <b> <w> ... <w>
        }

    }

    public void printEmptyChessboard (){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                System.out.print(board[i][j].tileColor() + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                result += (board[i][j] + " ");
            }
            result += '\n';
        }
        result += '\n';
        return result;
    }
}
