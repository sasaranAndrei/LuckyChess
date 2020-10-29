package pieces;

import board.Move;

import java.util.ArrayList;

public abstract class Piece {

    public boolean white = false;

    public Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    public abstract ArrayList<Move> generateValidMoves();
    public abstract String getType();

}
