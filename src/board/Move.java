//package com.example.luckychess.board;
package board;

import pieces.Piece;

public abstract class Move {
    private Player player;
    private Chessboard chessboard;
    private Tile start;
    private Tile end;

    public Move(Player player, Chessboard chessboard, Tile start, Tile end) {
        this.player = player;
        this.chessboard = chessboard;
        this.start = start;
        this.end = end;
    }

    public static class BasicMove extends Move{

        public BasicMove(Player player, Chessboard chessboard, Tile start, Tile end) {
            super(player, chessboard, start, end);
        }
    }

    public static class AttackMove extends Move{
        public Piece attackedPiece;
        public AttackMove(Player player, Chessboard chessboard, Tile start, Tile end) {
            super(player, chessboard, start, end);
            attackedPiece = end.getPiece();
        }
    }


    public Tile getStart() {
        return start;
    }

    public Tile getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Player : " + player.getName() + " can move from : " + start.getCoordinates() + " to " + end.getCoordinates() + "\n";
    }



}
