package chess;

import java.util.List;

public interface MoveCalculator {
    public List<ChessPosition> validMoves(ChessPosition position, ChessBoard board);
}
