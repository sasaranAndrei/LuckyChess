//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public class King extends Piece {
    private static final int VALUE = 100;
    private final String TYPE = "K";
    private ArrayList<Tile> possibleVulnerableTiles = new ArrayList<>();;

    public King(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves(Game game, Tile clickedTile, boolean humanIsWhite) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int currentX = clickedTile.getCoordX(); // luam coord
        int currentY = clickedTile.getCoordY(); // curente [ca de mutat oricum muta :)) ]
                for (int i = currentX - 1; i <= currentX + 1; i++){ // parcurgem patratelu de 3x3
                    for (int j = currentY - 1; j <= currentY + 1; j++){ // (regele in centru)
                        if (!(i == currentX) || !(j == currentY)){ // am aplicat deMorgan asupra patratelui cu i = currentX si j = currentY (e imposibil sa ramana pe loc)
                            // acum verificam ca indicii sa fie in regula
                            if (!game.getChessboard().indexOutOfBound(i,j)){ // daca indexul e in regula
                                // VERIRICAM SA NU FIE PIESA ... TO DO
                                if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                                    if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                                        validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                                    }
                                    else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                                        // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                                        possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                                    }
                                    //break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                                }
                                else { // daca nu e alta piesa, inseamna ca putem muta acolo
                                    validMoves.add(new Move.BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                                }
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
