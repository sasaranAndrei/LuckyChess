//package com.example.luckychess.board;
package board;

import pieces.King;

public class Player {

    private String name;
    private boolean human;
    private boolean white; // pentru ca se poate schimba pe parcursu jocului.
    private King king;

    public Player (){

    }

    public Player(String name, boolean human, boolean white, King king) {
        this.name = name;
        this.human = human;
        this.white = white;
        this.king = king;
    }

    public String getName() {
        return name;
    }

    public boolean isWhite() {
        return white;
    }

    public boolean isHuman() {
        return human;
    }

    public King getKing() {
        return king;
    }
}
