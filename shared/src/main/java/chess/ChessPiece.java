package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moveChoices = new ArrayList<>();

        switch (piece.getPieceType()) {
            case BISHOP: {
                List<ChessPosition> validMoves = new BishopMoveCalculator().getValidMoves(myPosition, board);
                for (ChessPosition move : validMoves) {
                    moveChoices.add(new ChessMove(myPosition, move, null));
                }
                return moveChoices;
            }
            case KING: {
                List<ChessPosition> validMoves = new KingMoveCalculator().getValidMoves(myPosition, board);
                for (ChessPosition move : validMoves) {
                    moveChoices.add(new ChessMove(myPosition, move, null));
                }
                return moveChoices;
            }
            case ROOK: {
                List<ChessPosition> validMoves = new RookMoveCalculator().getValidMoves(myPosition, board);
                for (ChessPosition move : validMoves) {
                    moveChoices.add(new ChessMove(myPosition, move, null));
                }
                return moveChoices;
            }
            case KNIGHT: {
                List<ChessPosition> validMoves = new KnightMoveCalculator().getValidMoves(myPosition, board);
                for (ChessPosition move : validMoves) {
                    moveChoices.add(new ChessMove(myPosition, move, null));
                }
                return moveChoices;
            }
            case QUEEN: {
                List<ChessPosition> validMoves = new QueenMoveCalculator().getValidMoves(myPosition, board);
                for (ChessPosition move : validMoves) {
                    moveChoices.add(new ChessMove(myPosition, move, null));
                }
                return moveChoices;
            }
            case PAWN: {
                List<ChessPosition> validMoves = new PawnMoveCalculator().getValidMoves(myPosition, board);

                for (ChessPosition move : validMoves) {
                    if (move.getRow() == 1 || move.getRow() == 8) {
                        moveChoices.add(new ChessMove(myPosition, move, PieceType.QUEEN));
                        moveChoices.add(new ChessMove(myPosition, move, PieceType.BISHOP));
                        moveChoices.add(new ChessMove(myPosition, move, PieceType.KNIGHT));
                        moveChoices.add(new ChessMove(myPosition, move, PieceType.ROOK));
                    } else moveChoices.add(new ChessMove(myPosition, move, null));
                }
                return moveChoices;
            }
        }
        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (o == this) {
            return true;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
