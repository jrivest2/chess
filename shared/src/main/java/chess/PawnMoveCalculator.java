package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveCalculator implements MoveCalculator {
    private List<ChessPosition> moves = new ArrayList<>();

    private final int[][] whiteOffsets = {{1,1},{1,-1},{1,0}};
    private final int[][] blackOffsets = {{-1,-1},{-1,1},{-1,0}};
    private final int[][] starterOffsets = {{2,0},{-2,0}};

    private List<ChessPosition> blacklist = new ArrayList<>();

    private int[][] addStarterOffset(int[][] offsets, int[] starterOffset) {
        List<int[]> tempList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            tempList.add(offsets[i]);
        }
        tempList.add(starterOffset);

        return tempList.toArray(new int[0][]);
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
            if (position.getRow() == 2) {
                offsets = addStarterOffset(offsets, starterOffset);
            }
        } else {
            offsets = blackOffsets;
            starterOffset = starterOffsets[1];
            if (position.getRow() == 7) {
                offsets = addStarterOffset(offsets, starterOffset);
            }
        }


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
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor() && offset[1] != 0) {
                    moves.add(target);
                }
            } else if (offset[0] > 1 || offset[0] < -1) {
                updateBlacklist(target, offsetDirection, board);
                if (!blacklist.contains(target)) {
                    moves.add(target);
                }
            } else if (board.getPiece(target) == null && offset[1] == 0) {
                moves.add(target);
            }

        }


        return moves;
    }
}
