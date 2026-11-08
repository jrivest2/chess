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
}
