//package com.example.luckychess.board;
package board;
//import com.example.luckychess.pieces.Piece;

import pieces.Piece;

public class Tile {
    private int coordX;
    private int coordY;
    private Piece piece;
    private boolean whiteTile;

    public Tile(int coordX, int coordY, boolean whiteTile){ // used for create emptyTile
        this.coordX = coordX;
        this.coordY = coordY;
        this.whiteTile = whiteTile;
        this.piece = null;
    }

    public Tile(int coordX, int coordY, boolean whiteTile, Piece piece) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.whiteTile = whiteTile;
        this.piece = piece;
    }

    @Override
    public String toString() {
        String pieceType;
        if (piece == null) pieceType = "";
        else pieceType = piece.getType();

        return String.format("%-3s", pieceType);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public String tileColor(){
        if (whiteTile) return String.format("%-5s", "white");
        else return String.format("%-5s", "black");
    }
}
