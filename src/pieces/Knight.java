//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public class Knight extends Piece {
    private final String TYPE = "H";
    public final int VALUE = 3;
    private ArrayList<Tile> possibleVulnerableTiles = new ArrayList<>();;

    public Knight(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves(Game game, Tile clickedTile, boolean humanIsWhite) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int currentX = clickedTile.getCoordX(); // luam coord
        int currentY = clickedTile.getCoordY(); // curente [ca de mutat oricum muta :)) ]

                for (int i = 1; i <= 2; i++) {
                    int j = 3 - i;
                    // now that we have (i,j) => generate all +- values for (i,j)
                    if (!game.getChessboard().indexOutOfBound(currentX - i, currentY - j)) {
                        if (game.getChessboard().getBoard()[currentX - i][currentY - j].getPiece() != null) { // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            if (game.getChessboard().getBoard()[currentX - i][currentY - j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                                validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[currentX - i][currentY - j]) );
                            }
                            else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                                // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                                possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                            }
                            //break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                        } else { // daca nu e alta piesa, inseamna ca putem muta acolo
                            validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - i][currentY - j]));
                        }
                    }

                    if (!game.getChessboard().indexOutOfBound(currentX - i, currentY + j)) {
                        if (game.getChessboard().getBoard()[currentX - i][currentY + j].getPiece() != null) { // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            if (game.getChessboard().getBoard()[currentX - i][currentY + j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                                validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[currentX - i][currentY + j]) );
                            }
                            else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                                // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                                possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                            }
                            //break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                        } else { // daca nu e alta piesa, inseamna ca putem muta acolo
                            validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX - i][currentY + j]));
                        }
                    }

                    if (!game.getChessboard().indexOutOfBound(currentX + i, currentY - j)) {
                        if (game.getChessboard().getBoard()[currentX + i][currentY - j].getPiece() != null) { // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            if (game.getChessboard().getBoard()[currentX + i][currentY - j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                                validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[currentX + i][currentY - j]) );
                            }
                            else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                                // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                                possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                            }
                            //break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                        } else { // daca nu e alta piesa, inseamna ca putem muta acolo
                            validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + i][currentY - j]));
                        }
                    }

                    if (!game.getChessboard().indexOutOfBound(currentX + i, currentY + j)) {
                        if (game.getChessboard().getBoard()[currentX + i][currentY + j].getPiece() != null) { // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            if (game.getChessboard().getBoard()[currentX + i][currentY + j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                                validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[currentX + i][currentY + j]) );
                            }
                            else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                                // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                                possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                            }
                            //break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                        } else { // daca nu e alta piesa, inseamna ca putem muta acolo
                            validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + i][currentY + j]));
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
