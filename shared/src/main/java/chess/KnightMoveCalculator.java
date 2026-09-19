package chess;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveCalculator implements MovesCalculator {
    private List<ChessPosition> moves = new ArrayList<>();
    private final int[][] offsets = {
            {2,1},{2,-1},{-2,1},{-2,-1},
            {1,2},{-1,2},{1,-2},{-1,-2}
    };
    @Override
    public List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board) {
        ChessPiece piece = board.getPiece(position);

        for (int[] offset : offsets) {
            ChessPosition target = position.addOffset(offset[0], offset[1]);
            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1
                    || (board.getPiece(target) != null
                    && board.getPiece(target).getTeamColor() == piece.getTeamColor())) {
                continue;
            } else moves.add(target);
        }

        return moves;
    }
}
