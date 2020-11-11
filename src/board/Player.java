//package com.example.luckychess.board;
package board;
public class Player {

    private String name;
    private boolean human; // omu joaca cu albu mereu
    private boolean underAttack = false;

    public Player(String name, boolean human) {
        this.name = name;
        this.human = human;
    }

    public String getName() {
        return name;
    }
}
