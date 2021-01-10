package board;

public class MoveTransition {

    private Chessboard transitionBoard;
    private Move theMove;
    private MoveStatus moveStatus;

    public MoveTransition(Chessboard transitionBoard, Move theMove, MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.theMove = theMove;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }
}
