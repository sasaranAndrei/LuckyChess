//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public class Pawn extends Piece {
    private final String TYPE = "P";

    public final int VALUE = 1;
    //private boolean white;
    private ArrayList<Tile> possibleVulnerableTiles = new ArrayList<>();;

    public Pawn(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves(Game game, Tile clickedTile, boolean humanIsWhite) {
        // cand humanIsWhite = pionu alb merge in sus si cel negru in jos
        // cand !humanIsWhite = pionu alb merge in jos si cel negru in sus

        int direction, INITIAL_WHITE, INITIAL_BLACK;
        if (humanIsWhite) {
            direction = 1;
            INITIAL_WHITE = 6;
            INITIAL_BLACK = 1;
        }
        else {
            direction = -1;
            INITIAL_WHITE = 1;
            INITIAL_BLACK = 6;
        }

        ArrayList<Move> validMoves = new ArrayList<>();
        int currentX = clickedTile.getCoordX(); // luam coord
        int currentY = clickedTile.getCoordY(); // curente [ca de mutat oricum muta :)) ]
        if (clickedTile.getPiece().isWhite()){// && game.getPlayerToMove().isWhite()) { // pion alb
            // mers in fata
            if (!game.getChessboard().indexOutOfBound(currentX - (1 * direction) , currentY)){ // daca indexu i ok CAND MERGE IN SUS
                // mutarea in fata
                if (game.getChessboard().getBoard()[currentX - (1 * direction)][currentY].getPiece() == null){ // daca poate merge in fata
                    /// TO DO => promotion move
                    validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - (1 * direction)][currentY]));
                    // verificam jump-ul
                    if (currentX == INITIAL_WHITE) { // initial
                        if (game.getChessboard().getBoard()[currentX - (2 * direction)][currentY].getPiece() == null){ // daca pozitia jumpului e libera
                            validMoves.add(new Move.BasicMove (game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - (2 * direction)][currentY]));
                        }
                    }
                }
            }
            //System.out.println("inainte de capturare ALB");
            // capturarea pe care o executa PION ALB
            Piece candidateCapturePiece;
            if (!game.getChessboard().indexOutOfBound(currentX - (1 * direction), currentY - 1)){
                candidateCapturePiece = game.getChessboard().getBoard()[currentX - (1 * direction)][currentY - 1].getPiece(); // piesa din fata stanga
                if (candidateCapturePiece != null && !candidateCapturePiece.white){ // o putem caputra
                    validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - (1 * direction)][currentY - 1]));
                }
                else {
                    possibleVulnerableTiles.add(game.getChessboard().getBoard()[currentX - (1 * direction)][currentY - 1]);
                }
            }
            if (!game.getChessboard().indexOutOfBound(currentX - (1 * direction), currentY + 1)){
                candidateCapturePiece = game.getChessboard().getBoard()[currentX - (1 * direction)][currentY + 1].getPiece(); // piesa din fata stanga
                if (candidateCapturePiece != null && !candidateCapturePiece.white){ // o putem caputra
                    validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - (1 * direction)][currentY + 1]));
                }
                else {
                    possibleVulnerableTiles.add(game.getChessboard().getBoard()[currentX - (1 * direction)][currentY + 1]);
                }
            }

        }

        else if (!clickedTile.getPiece().isWhite()){// && !game.getPlayerToMove().isWhite()) { // pion negru
            // mutarea in fata
            /// TO DO => promotion move
            if (!game.getChessboard().indexOutOfBound(currentX + (1 * direction) , currentY)) { // daca indexu i ok CAND MERGE IN FATA

                if (game.getChessboard().getBoard()[currentX + (1 * direction)][currentY].getPiece() == null) { // daca poate merge in fata
                    validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + (1 * direction)][currentY]));
                    // verificam jump-ul
                    if (currentX == INITIAL_BLACK) { // initial
                        if (game.getChessboard().getBoard()[currentX + (2 * direction)][currentY].getPiece() == null) { // daca pozitia jumpului e libera
                            validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + (2 * direction)][currentY]));
                        }
                    }
                }
                // capturarea pe care o executa PION NEGRU
                Piece candidateCapturePiece;
                if (!game.getChessboard().indexOutOfBound(currentX + (1 * direction), currentY - 1)) {
                    candidateCapturePiece = game.getChessboard().getBoard()[currentX + (1 * direction)][currentY - 1].getPiece(); // piesa din fata stanga
                    if (candidateCapturePiece != null && candidateCapturePiece.white) { // o putem caputra
                        validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + (1 * direction)][currentY - 1]));
                    }
                    else {
                        possibleVulnerableTiles.add(game.getChessboard().getBoard()[currentX + (1 * direction)][currentY - 1]);
                    }
                }
                if (!game.getChessboard().indexOutOfBound(currentX + (1 * direction), currentY + 1)) {
                    candidateCapturePiece = game.getChessboard().getBoard()[currentX + (1 * direction)][currentY + 1].getPiece(); // piesa din fata stanga
                    if (candidateCapturePiece != null && candidateCapturePiece.white) { // o putem caputra
                        validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + (1 * direction)][currentY + 1]));
                    }
                    else {
                        possibleVulnerableTiles.add(game.getChessboard().getBoard()[currentX + (1 * direction)][currentY + 1]);
                    }

                }
            }
        }

        return validMoves;
    }




    @Override
    public String getType() {
        if (isWhite()) return TYPE.toUpperCase();
        else return TYPE.toLowerCase();
    }

    @Override
    public int getValue() {
        return VALUE;
    }

    @Override
    public ArrayList<Tile> getPossibleVulnerableTiles(Game game, Tile clickedTile) {
        return possibleVulnerableTiles;
    }

}
