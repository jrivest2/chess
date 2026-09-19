package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopMoveCalculator implements MovesCalculator{
    public List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board) {
        List<ChessPosition> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        int[][] offsets = {
                {1,1},{1,-1},{-1,-1},{-1,1},
                {2,2},{2,-2},{-2,-2},{-2,2},
                {3,3},{3,-3},{-3,-3},{-3,3},
                {4,4},{4,-4},{-4,-4},{-4,4},
                {5,5},{5,-5},{-5,-5},{-5,5},
                {6,6},{6,-6},{-6,-6},{-6,6},
                {7,7},{7,-7},{-7,-7},{-7,7}
        };

        for (int[] offset: offsets) {
            ChessPosition target = position.addOffset(offset[0], offset[1]);

            if (target.getRow() > 8 || target.getColumn() > 8 || target.getRow() < 1 || target.getColumn() < 1
                    || (board.getPiece(target) != null && board.getPiece(target).getTeamColor() == piece.getTeamColor())) {
                continue;
            } else {
                moves.add(target);
            }
        }

        return moves;
    }
}
