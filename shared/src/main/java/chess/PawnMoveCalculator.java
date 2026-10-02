package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveCalculator implements MoveCalculator {
    private List<ChessPosition> moves = new ArrayList<>();

    private int[][] whiteOffsets = {{1,1},{1,-1},{1,0}};
    private int[][] blackOffsets = {{-1,-1},{-1,1},{-1,0}};
    private int[][] starterOffsets = {{2,0},{-2,0}};

    private List<ChessPosition> blacklist = new ArrayList<>();
    private enum Direction{
        POSITIVE_ROW_POSITIVE_COL, POSITIVE_ROW_NEGATIVE_COL,
        NEGATIVE_ROW_NEGATIVE_COL, NEGATIVE_ROW_POSITIVE_COL,
        POSITIVE_ROW,NEGATIVE_COL,
        NEGATIVE_ROW, POSITIVE_COL
    };

    private int[][] addStarterOffset(int[][] offsets, int[] starterOffset) {
        List<int[]> tempList = new ArrayList<>();

        for (int i = 0; i < 3; i++) tempList.add(offsets[i]);
        tempList.add(starterOffset);

        return tempList.toArray(new int[0][]);
    }

    private Direction findDirection(ChessPosition target, int[] offset) {
        if (offset[0] > 0) {
            if (offset[1] > 0) return Direction.POSITIVE_ROW_POSITIVE_COL;
            else if (offset[1] < 0) return Direction.POSITIVE_ROW_NEGATIVE_COL;
            else return Direction.POSITIVE_ROW;
        } else if (offset[0] < 0) {
            if (offset[1] > 0) return Direction.NEGATIVE_ROW_POSITIVE_COL;
            else if (offset[1] < 0) return Direction.NEGATIVE_ROW_NEGATIVE_COL;
            else return Direction.NEGATIVE_ROW;
        } else {
            if (offset[1] > 0) return Direction.POSITIVE_COL;
            return Direction.NEGATIVE_COL;
        }
    }

    private void updateBlacklist(ChessPosition target, Direction offsetDirection, ChessBoard board) {
        switch (offsetDirection) {
            case POSITIVE_ROW: {
                if (board.getPiece(new ChessPosition(target.getRow() - 1, target.getColumn())) != null) {
                    blacklist.add(target);
                }
                break;
            }

            case NEGATIVE_ROW: {
                if (board.getPiece(new ChessPosition(target.getRow() + 1, target.getColumn())) != null) {
                    blacklist.add(target);
                }
                break;
            }
        }
    }

    @Override
    public List<ChessPosition> validMoves(ChessPosition position, ChessBoard board) {
        ChessPiece piece = board.getPiece(position);

        int[][] offsets;
        int[] starterOffset;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            offsets = whiteOffsets;
            starterOffset = starterOffsets[0];
            if (position.getRow() == 2) offsets = addStarterOffset(offsets, starterOffset);
        } else {
            offsets = blackOffsets;
            starterOffset = starterOffsets[1];
            if (position.getRow() == 7) offsets = addStarterOffset(offsets, starterOffset);
        }


        for (int[] offset : offsets) {
            ChessPosition target =  position.addOffset(offset[0],offset[1]);
            Direction offsetDirection = findDirection(target, offset);

            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1) continue;
            else if (blacklist.contains(target)) continue;
            else if (board.getPiece(target) != null) {
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor()) {
                    if (offset[1] != 0) moves.add(target);
                }
            } else if (offset[0] > 1 || offset[0] < -1) {
                updateBlacklist(target, offsetDirection, board);
                if (!blacklist.contains(target)) moves.add(target);
            } else if (board.getPiece(target) == null && offset[1] == 0) moves.add(target);

        }


        return moves;
    }
}
