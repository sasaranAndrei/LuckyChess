//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

import static board.Move.*;


public class Bishop extends Piece {
    private final String TYPE = "B";
    //private boolean white;

    public Bishop(boolean white){
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves(Game game, Tile clickedTile) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int currentX = clickedTile.getCoordX(); // luam coord
        int currentY = clickedTile.getCoordY(); // curente [ca de mutat oricum muta :)) ]
        /*
        if (game.getPlayerToMove() == game.getHumanPlayer()){ // white moves [testam pentru ca utilizatoru sa nu apese pe piese inamicului pe tura inamicului

            if (game.getStatus() == Game.Status.WHITE_IN_CHECK){ // daca e in sah -> genereaza mutarile care ar stopa sahul
                // urmeaza a fii implementat
                return null;
            }
            else {*/ // daca muta 'liber'
                // si mergem pe toate laturile X-ului facut de bishop ca sa gasim mutarile valide
                ////// NV
                for (int i = currentX - 1, j = currentY - 1; !game.getChessboard().indexOutOfBound(i, j); i--, j--){ // NV
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo
                        validMoves.add(new BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                } // NV
                ////// NE
                for (int i = currentX - 1, j = currentY + 1; !game.getChessboard().indexOutOfBound(i, j); i--, j++){ // NE
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo
                        validMoves.add(new BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                }// NE
                ////// SE
                for (int i = currentX + 1, j = currentY + 1; !game.getChessboard().indexOutOfBound(i, j); i++, j++){ // SE
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo
                        validMoves.add(new BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                }// SE
                ////// SV
                for (int i = currentX + 1, j = currentY - 1; !game.getChessboard().indexOutOfBound(i, j); i++, j--){ // SV
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo
                        validMoves.add(new BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                }// SV
        /*


            }

        }
        */
        return validMoves; // dupa ce am epuizat mutarile posibile, returnam rezultatu
    }



    @Override
    public String getType() {
        if (isWhite()) return TYPE.toUpperCase();
        else return TYPE.toLowerCase();
    }
}
