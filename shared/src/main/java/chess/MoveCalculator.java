package chess;

import java.util.List;

public interface MoveCalculator {
    List<ChessPosition> validMoves(ChessPosition position, ChessBoard board);
    enum Direction{
        POSITIVE_ROW_POSITIVE_COL, POSITIVE_ROW_NEGATIVE_COL,
        NEGATIVE_ROW_NEGATIVE_COL, NEGATIVE_ROW_POSITIVE_COL,
        POSITIVE_ROW,NEGATIVE_COL,
        NEGATIVE_ROW, POSITIVE_COL
    }
    default Direction omniDirectionalFindDirection(int[] offset) {
        if (offset[0] > 0) {
            if (offset[1] > 0) {
                return Direction.POSITIVE_ROW_POSITIVE_COL;
            }
            else if (offset[1] < 0) {
                return Direction.POSITIVE_ROW_NEGATIVE_COL;
            }
            else {
                return Direction.POSITIVE_ROW;
            }
        } else if (offset[0] < 0) {
            if (offset[1] > 0) {
                return Direction.NEGATIVE_ROW_POSITIVE_COL;
            }
            else if (offset[1] < 0) {
                return Direction.NEGATIVE_ROW_NEGATIVE_COL;
            }
            else {
                return Direction.NEGATIVE_ROW;
            }
        } else {
            if (offset[1] > 0) {
                return Direction.POSITIVE_COL;
            }
            return Direction.NEGATIVE_COL;
        }
    }

    default List<ChessPosition> knightKingvalidMoves(ChessPosition position, ChessBoard board, int[][] offsets, List<ChessPosition> moves) {
        ChessPiece piece = board.getPiece(position);

        for (int[] offset : offsets) {
            ChessPosition target =  position.addOffset(offset[0],offset[1]);

            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1) {
                continue;
            }
            else if (board.getPiece(target) != null) {
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor()) {
                    moves.add(target);
                }
            }else {
                moves.add(target);
            }
        }
        return moves;
    }

    default List<ChessPosition> updateBlacklistMaster(ChessPosition target, Direction offsetDirection, List<ChessPosition> blacklist) {
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
        return blacklist;
    }
}
