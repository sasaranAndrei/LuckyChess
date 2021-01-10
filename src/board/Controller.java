package board;

import board.Game;
import gui.Board;
import pieces.Piece;

import java.util.concurrent.TimeUnit;

public class Controller {
    private Game game;
    private Board boardGUI;

    private Dice dice = new Dice();

    public Controller(Game game, Board boardGUI) {
        this.game = game;
        this.boardGUI = boardGUI;
    }

    public void play (){
        int rowCounter = 0;
        game.setStatus(Game.Status.PLAY);


        while (rowCounter < 5 && game.getStatus() == Game.Status.PLAY ||  // cat timp inca se poate juca
                game.getStatus() == Game.Status.WHITE_IN_CHECK || game.getStatus() == Game.Status.BLACK_IN_CHECK){
            if (game.getStatus() == Game.Status.WHITE_IN_CHECK || game.getStatus() == Game.Status.BLACK_IN_CHECK){ // daca jucatorul curent e in sah
                if (isCheckMate(game.getPlayerToMove())){ // in cazu asta playerToMove o sa fie jucatorul cu alb
                    if (game.getPlayerToMove() == game.getHumanPlayer()){ // daca omu si o luat sah mat
                        game.setWinner(game.getComputerPlayer());// = computerPlayer;
                    }
                    else { // daca calculatorul si a luat sah mat
                        game.setWinner(game.getHumanPlayer());
                    }
                    game.setStatus(Game.Status.WIN);//status = Game.Status.WIN;
                    break; // daca e sah mat se iese din while (play) si se constata castigatoruu
                }

            }


            if (rowCounter % 2 == 0) game.setPlayerToMove(game.getHumanPlayer());
            else game.setPlayerToMove(game.getComputerPlayer());

            // TO DO => PLAY MODE
            // mai intai dam cu zaru
            dice.rollDice();
            System.out.println(dice);
            boardGUI.setDices(game.getPlayerToMove());


            //    boardGUI.setDices(dice, playerToMove);
            //game.setStatus(Game.Status.WIN);
            // status = Game.Status.WIN; // ca sa nu intre in bucla infinita

            System.out.println("Tura : " + rowCounter + " muta " + game.getPlayerToMove().getName());




            rowCounter++;
            /*
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             */
        }


        //System.out.println(rowCounter);
        //System.out.println("finnal");
        // TO DO => anunta castigatorul

        /*
        System.out.println("here");
        if (game == null){
            System.out.println("game pl");
        }
        if (boardGUI == null){
            System.out.println("boardgui pl");
        }
        */
    }

    private boolean isCheckMate(Player playerToMove) {
        return false;
    }

    public void loadPieceIcons (){
        Piece piece = game.getChessboard().getBoard()[0][0].getPiece();

        //boardGUI
    }
}
