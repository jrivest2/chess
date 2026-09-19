package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopMoveCalculator implements MovesCalculator{
    private enum Direction {POSITIVE_ROW_POSITIVE_COL, POSITIVE_ROW_NEGATIVE_COL, NEGATIVE_ROW_NEGATIVE_COL, NEGATIVE_ROW_POSITIVE_COL,}
    private Direction findOffsetDirection(int[] offset) {
        if (offset[0] >= 0) {
            if (offset[1] >= 0) return Direction.POSITIVE_ROW_POSITIVE_COL;
            else return Direction.POSITIVE_ROW_NEGATIVE_COL;
        } else if (offset[1] >= 0) return Direction.NEGATIVE_ROW_POSITIVE_COL;
        else return Direction.NEGATIVE_ROW_NEGATIVE_COL;
    }
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
        List<ChessPosition> blacklist = new ArrayList<>();

        for (int[] offset: offsets) {
            ChessPosition target = position.addOffset(offset[0], offset[1]);
            Direction offsetDirection = findOffsetDirection(offset);

            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1) {  continue;
            } else if (blacklist.contains(target)){ continue;
            } else if (board.getPiece(target) != null) {
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor()) moves.add(target);
                switch (offsetDirection) {
                    case POSITIVE_ROW_POSITIVE_COL: {
                        for (int i = target.getRow(), j = target.getColumn(); i <= 8 && j <= 8 ; i++, j++) {
                            blacklist.add(new ChessPosition(i,j));
                        }
                    }
                    case POSITIVE_ROW_NEGATIVE_COL: {
                        for (int i = target.getRow(), j = target.getColumn(); i <= 8 && j >= 1 ; i++, j--) {
                            blacklist.add(new ChessPosition(i,j));
                        }
                    }
                    case NEGATIVE_ROW_NEGATIVE_COL: {
                        for (int i = target.getRow(), j = target.getColumn(); i >= 1 && j >= 1 ; i--, j--) {
                            blacklist.add(new ChessPosition(i,j));
                        }
                    }
                    case NEGATIVE_ROW_POSITIVE_COL: {
                        for (int i = target.getRow(), j = target.getColumn(); i >= 1 && j <= 8 ; i--, j++) {
                            blacklist.add(new ChessPosition(i,j));
                        }
                    }
                }
            } else {
                moves.add(target);
            }
        }

        return moves;
    }
}
