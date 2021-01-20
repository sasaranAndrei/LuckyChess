package pieces;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public abstract class Piece implements Comparable{

    public boolean white;
    public boolean alive = true;

    public Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    public abstract ArrayList<Move> generateValidMoves(Game game, Tile clickedTile, boolean humanIsWhite);
    public abstract String getType();
    public abstract int getValue();
    public abstract ArrayList<Tile> getPossibleVulnerableTiles(Game game, Tile clickedTile);

    @Override
    public int compareTo(Object o) {
        Piece comparedPiece = (Piece) o;
        return this.getValue() - comparedPiece.getValue();
    }
}
