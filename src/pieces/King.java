//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Move;

import java.util.ArrayList;

public class King extends Piece {
    private final String TYPE = "K";

    public King(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves() {
        return null;
    }

    @Override
    public String getType() {
        if (isWhite()) return TYPE.toUpperCase();
        else return TYPE.toLowerCase();
    }
}
