package chess;

import java.util.ArrayList;
import java.util.List;

public class RookMoveCalculator implements MoveCalculator {
    private List<ChessPosition> moves = new ArrayList<>();
    private int[][] offsets = {
            {1,0},{0,-1},{-1,-0},{0,1},
            {2,0},{0,-2},{-2,-0},{0,2},
            {3,0},{0,-3},{-3,-0},{0,3},
            {4,0},{0,-4},{-4,-0},{0,4},
            {5,0},{0,-5},{-5,-0},{0,5},
            {6,0},{0,-6},{-6,-0},{0,6},
            {7,0},{0,-7},{-7,-0},{0,7},
    };
    private List<ChessPosition> blacklist = new ArrayList<>();
    private enum Direction{ POSITIVE_ROW,NEGATIVE_COL,
        NEGATIVE_ROW, POSITIVE_COL}

    private Direction findDirection(ChessPosition target, int[] offset) {
        if (offset[0] == 0) {
            if (offset[1] > 0) return Direction.POSITIVE_COL;
            else return Direction.NEGATIVE_COL;
        } else {
            if (offset[0] > 0) return Direction.POSITIVE_ROW;
            else return Direction.NEGATIVE_ROW;
        }



    }

    private void updateBlacklist(ChessPosition target, Direction offsetDirection) {
        switch (offsetDirection) {
            case POSITIVE_ROW: {
                for (int i = target.getRow() + 1; i <= 8; i++) {
                    blacklist.add(new ChessPosition(i,target.getColumn()));
                }
                break;
            }
            case NEGATIVE_COL: {
                for (int i = target.getColumn() -1; i >= 1; i--) {
                    blacklist.add(new ChessPosition(target.getRow(),i));
                }
                break;
            }
            case NEGATIVE_ROW: {
                for (int i = target.getRow() - 1; i >= 1; i--) {
                    blacklist.add(new ChessPosition(i, target.getColumn()));
                }
                break;
            }
            case POSITIVE_COL: {
                for (int i = target.getColumn() + 1; i <= 8; i++ ) {
                    blacklist.add(new ChessPosition(target.getRow(), i));
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
            Direction offsetDirection = findDirection(target, offset);

            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1) continue;
            else if (blacklist.contains(target)) continue;
            else if (board.getPiece(target) != null) {
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor()) {
                    moves.add(target);
                    updateBlacklist(target, offsetDirection);
                } else updateBlacklist(target, offsetDirection);
            }else moves.add(target);

        }


        return moves;
    }
}
