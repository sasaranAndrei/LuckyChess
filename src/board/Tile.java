//package com.example.luckychess.board;
package board;
//import com.example.luckychess.pieces.Piece;

import pieces.Piece;

public class Tile {
    private int coordX;
    private int coordY;
    private Piece piece;
    private boolean forValidMove = false;

    public Tile(int coordX, int coordY){ // used for create emptyTile
        this.coordX = coordX;
        this.coordY = coordY;
        this.piece = null;
    }

    public Tile(int coordX, int coordY, Piece piece) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.piece = piece;
    }

    @Override
    public String toString() {
        String pieceType;
        if (forValidMove) pieceType = ".";
        else if (piece == null) pieceType = "";
        else pieceType = piece.getType();

        return String.format("%-3s|", pieceType);
    }

    public String getCoordinates(){
        return "[x=" +  coordX + ",y=" + coordY + "]";
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
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

    public void setForValidMove(boolean forValidMove) {
        this.forValidMove = forValidMove;
    }





}
