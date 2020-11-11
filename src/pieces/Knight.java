//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public class Knight extends Piece {
    private final String TYPE = "H";

    public Knight(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves(Game game, Tile clickedTile) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int currentX = clickedTile.getCoordX(); // luam coord
        int currentY = clickedTile.getCoordY(); // curente [ca de mutat oricum muta :)) ]
        if (game.getPlayerToMove() == game.getHumanPlayer()) { // white moves [testam pentru ca utilizatoru sa nu apese pe piese inamicului pe tura inamicului

            if (game.getStatus() == Game.Status.WHITE_IN_CHECK) { // daca e in sah -> genereaza mutarile care ar stopa sahul
                // urmeaza a fii implementat
                return null;
            } else { // daca muta 'liber'
                // si mergem pe toate pozitiile de L posibile facut de KNIGHT ca sa gasim mutarile valide

                for (int i = 1; i <= 2; i++) {
                    int j = 3 - i;
                    // now that we have (i,j) => generate all +- values for (i,j)
                    if (!game.getChessboard().indexOutOfBound(currentX - i, currentY - j)) {
                        if (game.getChessboard().getBoard()[currentX - i][currentY - j].getPiece() != null) { // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            if (game.getChessboard().getBoard()[currentX - i][currentY - j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                                validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[currentX - i][currentY - j]) );
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
                            //break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                        } else { // daca nu e alta piesa, inseamna ca putem muta acolo
                            validMoves.add(new Move.BasicMove(game.getPlayerToMove(), game.getChessboard(), clickedTile, game.getChessboard().getBoard()[currentX + i][currentY + j]));
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
}
