import board.Chessboard;
import board.Game;
import board.HumanPlayer;
import board.Tile;
import gui.Board;
import gui.Start;
import pieces.*;

public class MainClass {
    public static void main(String[] args) {
        //Start startGUI = new Start();


        String humanPlayerName = "sosy";

        Board boardFrame = new Board(humanPlayerName);
        //boardFrame.playInterface();

        //boardFrame.play();

        /*
         */

        //Board board = new Board("s");

        //Game game = boardGUI.getGame();

        //System.out.println(game.getChessboard());
        //game.showAllValidMoves(game.getChessboard().getWhitePieces());
        //game.getChessboard().removePieceFromTile(6,5);
        //game.getChessboard().removePieceFromTile(6,3);
        //game.getChessboard().removePieceFromTile(6,4);

        //game.showValidMoves(game.getChessboard().getBoard()[7][4]);

        /*
        Game game = new Game("sosy");

        // pun un bishop pe 3 3 pt test
        //int coordX = 3;
        //int coordY = 3;

        Rook rook = new Rook(true);
        Bishop bishop = new Bishop(true);
        Knight knight = new Knight(true);
        Queen queen = new Queen(true);
        King king = new King(true);

        game.getChessboard().getBoard()[3][2].setPiece(rook);
        game.getChessboard().getBoard()[4][5].setPiece(bishop);
        game.getChessboard().getBoard()[2][5].setPiece(knight);
        game.getChessboard().getBoard()[4][4].setPiece(queen);
        game.getChessboard().getBoard()[4][3].setPiece(king);
        //game.getChessboard().getBoard()[6][2].setPiece(king);
        //System.out.println(game.chessboard);


        Tile tile1 = game.getChessboard().getBoard()[3][2];
        Tile tile2 = game.getChessboard().getBoard()[4][5];
        Tile tile3 = game.getChessboard().getBoard()[2][5];
        Tile tile4 = game.getChessboard().getBoard()[4][4];
        Tile tile5 = game.getChessboard().getBoard()[4][3];



        /*
        game.showValidMoves(tile1);
        game.showValidMoves(tile2);
        game.showValidMoves(tile3);
        game.showValidMoves(tile4);
        game.showValidMoves(tile5);


        //System.out.println(clickedTile.getPiece().getType());

        //System.out.println(game.generateValidMoves(tile1));

        // while (status != win / tie) { if isCheckMate set status & break...
        /*
            if (isCheck) {
                if isCheckMate
            else
                generate the moves that stop the attack
         */

        //game.chessboard.printEmptyChessboard();


    }
}
