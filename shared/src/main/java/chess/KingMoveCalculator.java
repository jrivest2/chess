package chess;

import java.util.ArrayList;
import java.util.List;

public class KingMoveCalculator implements MoveCalculator {
    private List<ChessPosition> moves = new ArrayList<>();
    private int[][] offsets = {
            {1,0},{0,-1},{-1,-0},{0,1},
            {1,1},{1,-1},{-1,-1},{-1,1}
    };


    @Override
    public List<ChessPosition> validMoves(ChessPosition position, ChessBoard board) {
        ChessPiece piece = board.getPiece(position);

        for (int[] offset : offsets) {
            ChessPosition target =  position.addOffset(offset[0],offset[1]);

            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1) continue;
            else if (board.getPiece(target) != null) {
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor()) {
                    moves.add(target);
                }
            }else moves.add(target);

        }


        return moves;
    }
}
