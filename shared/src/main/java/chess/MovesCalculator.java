package chess;

import java.util.List;

public interface MovesCalculator {
    List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board);
}
