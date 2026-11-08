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

    private void updateBlacklist(ChessPosition target, Direction offsetDirection) {
        switch (offsetDirection) {
            case POSITIVE_ROW_POSITIVE_COL: {
                for (int i = target.getRow() + 1, j = target.getColumn() + 1; i <= 8 && j <= 8; i++, j++ ) {
                    blacklist.add(new ChessPosition(i,j));
                }
                break;
            }
            case POSITIVE_ROW_NEGATIVE_COL: {
                for (int i = target.getRow() + 1, j = target.getColumn() -1; i <= 8 && j >= 1; i++, j-- ) {
                    blacklist.add(new ChessPosition(i,j));
                }
                break;
            }
            case NEGATIVE_ROW_NEGATIVE_COL: {
                for (int i = target.getRow() - 1, j = target.getColumn() - 1; i >= 1 && j >= 1; i--, j-- ) {
                    blacklist.add(new ChessPosition(i,j));
                }
                break;
            }
            case NEGATIVE_ROW_POSITIVE_COL: {
                for (int i = target.getRow() - 1, j = target.getColumn() + 1; i >= 1 && j < 8; i--, j++ ) {
                    blacklist.add(new ChessPosition(i,j));
                }
                break;
            }
        }
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
