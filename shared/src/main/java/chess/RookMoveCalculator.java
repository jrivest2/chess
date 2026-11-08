package chess;

import java.util.ArrayList;
import java.util.List;

public class RookMoveCalculator implements MoveCalculator {
    private List<ChessPosition> moves = new ArrayList<>();
    private final int[][] offsets = {
            {1,0},{0,-1},{-1,-0},{0,1},
            {2,0},{0,-2},{-2,-0},{0,2},
            {3,0},{0,-3},{-3,-0},{0,3},
            {4,0},{0,-4},{-4,-0},{0,4},
            {5,0},{0,-5},{-5,-0},{0,5},
            {6,0},{0,-6},{-6,-0},{0,6},
            {7,0},{0,-7},{-7,-0},{0,7},
    };
    private List<ChessPosition> blacklist = new ArrayList<>();

    private void updateBlacklist(ChessPosition target, Direction offsetDirection) {
        blacklist = updateBlacklistMaster(target,offsetDirection,blacklist);
    }

    @Override
    public List<ChessPosition> validMoves(ChessPosition position, ChessBoard board) {
        ChessPiece piece = board.getPiece(position);

        for (int[] offset : offsets) {
            ChessPosition target =  position.addOffset(offset[0],offset[1]);
            Direction offsetDirection = omniDirectionalFindDirection(offset);

            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1) {
                continue;
            }
            else if (blacklist.contains(target)) {
                continue;
            }
            else if (board.getPiece(target) != null) {
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor()) {
                    moves.add(target);
                    updateBlacklist(target, offsetDirection);
                } else {
                    updateBlacklist(target, offsetDirection);
                }
            }else {
                moves.add(target);
            }

        }
        return moves;
    }
}
