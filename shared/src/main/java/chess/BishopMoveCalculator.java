package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopMoveCalculator implements MoveCalculator {

    private List<ChessPosition> moves = new ArrayList<>();
    private final int[][] offsets = {
            {1,1},{1,-1},{-1,-1},{-1,1},
            {2,2},{2,-2},{-2,-2},{-2,2},
            {3,3},{3,-3},{-3,-3},{-3,3},
            {4,4},{4,-4},{-4,-4},{-4,4},
            {5,5},{5,-5},{-5,-5},{-5,5},
            {6,6},{6,-6},{-6,-6},{-6,6},
            {7,7},{7,-7},{-7,-7},{-7,7},
    };
    private List<ChessPosition> blacklist = new ArrayList<>();

    @Override
    public List<ChessPosition> validMoves(ChessPosition position, ChessBoard board) {
        moves = validMovesSliders(position, board, offsets, moves, blacklist);
        return moves;
    }
}
