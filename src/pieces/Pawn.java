//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public class Pawn extends Piece {
    private final String TYPE = "P";
    //private boolean white;

    public Pawn(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves(Game game, Tile clickedTile) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int currentX = clickedTile.getCoordX(); // luam coord
        int currentY = clickedTile.getCoordY(); // curente [ca de mutat oricum muta :)) ]
        if (clickedTile.getPiece().isWhite()) { // pion alb
            // mers in fata
            if (!game.getChessboard().indexOutOfBound(currentX - 1 , currentY)){ // daca indexu i ok CAND MERGE IN FATA
                // mutarea in fata
                if (game.getChessboard().getBoard()[currentX - 1][currentY].getPiece() == null){ // daca poate merge in fata
                    /// TO DO => promotion move
                    validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - 1][currentY]));
                    // verificam jump-ul
                    if (currentX == 6) { // initial
                        if (game.getChessboard().getBoard()[currentX - 2][currentY].getPiece() == null){ // daca pozitia jumpului e libera
                            validMoves.add(new Move.BasicMove (game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - 2][currentY]));
                        }
                    }
                }
            }

            // capturarea
            Piece candidateCapturePiece;
            if (!game.getChessboard().indexOutOfBound(currentX - 1, currentY - 1)){
                candidateCapturePiece = game.getChessboard().getBoard()[currentX - 1][currentY - 1].getPiece(); // piesa din fata stanga
                if (candidateCapturePiece != null && !candidateCapturePiece.isWhite()){ // o putem caputra
                    validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - 1][currentY - 1]));
                }
            }
            if (!game.getChessboard().indexOutOfBound(currentX - 1, currentY + 1)){
                candidateCapturePiece = game.getChessboard().getBoard()[currentX - 1][currentY + 1].getPiece(); // piesa din fata stanga
                if (candidateCapturePiece != null && !candidateCapturePiece.isWhite()){ // o putem caputra
                    validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - 1][currentY + 1]));
                }
            }
        }
        /*
        else { // pion negru
            // mutarea in fata
            /// TO DO => promotion move

            if (game.getChessboard().getBoard()[currentX + 1][currentY].getPiece() == null){ // daca poate merge in fata
                validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + 1][currentY]));
                // verificam jump-ul
                if (currentX == 1) { // initial
                    if (game.getChessboard().getBoard()[currentX + 2][currentY].getPiece() == null){ // daca pozitia jumpului e libera
                        validMoves.add(new Move.BasicMove (game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + 2][currentY]));
                    }
                }
            }
            // capturarea
            Piece candidateCapturePiece = game.getChessboard().getBoard()[currentX + 1][currentY - 1].getPiece(); // piesa din fata stanga
            if (candidateCapturePiece != null && !candidateCapturePiece.isWhite()){ // o putem caputra
                validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + 1][currentY - 1]));
            }
            candidateCapturePiece = game.getChessboard().getBoard()[currentX + 1][currentY + 1].getPiece(); // piesa din fata stanga
            if (candidateCapturePiece != null && !candidateCapturePiece.isWhite()){ // o putem caputra
                validMoves.add(new Move.AttackMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + 1][currentY + 1]));
            }



        }
        */
        return validMoves;
    }




    @Override
    public String getType() {
        if (isWhite()) return TYPE.toUpperCase();
        else return TYPE.toLowerCase();
    }

}
