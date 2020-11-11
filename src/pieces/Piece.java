package pieces;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public abstract class Piece {

    public boolean white;
    public boolean alive = true;

    public Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    public abstract ArrayList<Move> generateValidMoves(Game game, Tile clickedTile);
    public abstract String getType();

}
