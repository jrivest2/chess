package chess;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveCalculator implements MoveCalculator {
    private List<ChessPosition> moves = new ArrayList<>();
    private final int[][] offsets = {
            {1,2},{2,1},{-1,2},{2,-1},
            {-2,1},{1,-2},{-1,-2},{-2,-1}
    };

    @Override
    public List<ChessPosition> validMoves(ChessPosition position, ChessBoard board) {
        moves = knightKingvalidMoves(position, board, offsets, moves);
        return moves;
    }
}
