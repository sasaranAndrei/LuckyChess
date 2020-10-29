//package com.example.luckychess.board;
package board;
//import android.util.Log;

public class Game {
    private Player humanPlayer;
    private Player computerPlayer;

    private Chessboard chessboard;

    public Game (String playerName){
        humanPlayer = new Player(playerName, true);
        chessboard = new Chessboard();
    }

    public static void main(String[] args) {
        Game game = new Game("sosy");
        System.out.println(game.chessboard);
        //game.chessboard.printEmptyChessboard();
    }

}
