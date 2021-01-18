//package com.example.luckychess.pieces;
package pieces;
//import com.example.luckychess.board.Move;

import board.Game;
import board.Move;
import board.Tile;

import java.util.ArrayList;

public class Rook extends Piece {
    private final String TYPE = "R";
    public final int VALUE = 5;
    private ArrayList<Tile> possibleVulnerableTiles = new ArrayList<>();;

    public Rook(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Move> generateValidMoves(Game game, Tile clickedTile) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int currentX = clickedTile.getCoordX(); // luam coord
        int currentY = clickedTile.getCoordY(); // curente [ca de mutat oricum muta :)) ]
        /*
        if (game.getPlayerToMove() == game.getHumanPlayer()) { // white moves [testam pentru ca utilizatoru sa nu apese pe piese inamicului pe tura inamicului

            if (game.getStatus() == Game.Status.WHITE_IN_CHECK) { // daca e in sah -> genereaza mutarile care ar stopa sahul
                // urmeaza a fii implementat
                return null;
            }
            else { // daca muta 'liber'
                // si mergem pe toate laturile + ului facut de rook ca sa gasim mutarile valide
                */
                for (int i = currentX - 1, j = currentY; !game.getChessboard().indexOutOfBound(i, j); i--) { // NORD
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                            // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                            possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo

                        validMoves.add(new Move.BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                } // NORD

                for (int i = currentX, j = currentY + 1; !game.getChessboard().indexOutOfBound(i, j); j++) { // EST
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                            // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                            possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo
                        validMoves.add(new Move.BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                } // EST

                for (int i = currentX + 1, j = currentY; !game.getChessboard().indexOutOfBound(i, j); i++) { // SUD
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                            // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                            possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo
                        validMoves.add(new Move.BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                } // SUD

                for (int i = currentX, j = currentY - 1; !game.getChessboard().indexOutOfBound(i, j); j--) { // SUD
                    if (game.getChessboard().getBoard()[i][j].getPiece() != null){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                        ////////////////////// TO DO ///////////////////////////
                        if (game.getChessboard().getBoard()[i][j].getPiece().white != clickedTile.getPiece().white){ // daca am dat de o piesa -> ori o capturam / ori nu putem muta peste ea
                            validMoves.add(new Move.AttackMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                        }
                        else { // daca au aceeasi culoare inseamna ca tura viitoare o poate apara
                            // aceasta mutare este un validMove tura urmatoarea (pt logica PCului)
                            possibleVulnerableTiles.add(game.getChessboard().getBoard()[i][j]);
                        }
                        break; // dupa ce da de o piesa oprim bucla ca nu are sens sa cautam mai departe
                    }
                    else { // daca nu e alta piesa, inseamna ca putem muta acolo
                        validMoves.add(new Move.BasicMove( game.getPlayerToMove() , game.getChessboard() , clickedTile , game.getChessboard().getBoard()[i][j]) );
                    }
                } // VEST


            /*
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

    @Override
    public int getValue() {
        return VALUE;
    }

    @Override
    public ArrayList<Tile> getPossibleVulnerableTiles(Game game, Tile clickedTile) {
        return possibleVulnerableTiles;
    }


}
