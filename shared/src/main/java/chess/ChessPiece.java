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
        Collection<ChessMove> moves = new ArrayList<>();

        switch (type) {
            case BISHOP: {
                List<ChessPosition> validMoves = new BishopMoveCalculator().validMoves(myPosition, board);

                for (ChessPosition move : validMoves) {
                    moves.add(new ChessMove(myPosition, move, null));
                }
                break;
            }
            case ROOK: {
                List<ChessPosition> validMoves = new RookMoveCalculator().validMoves(myPosition, board);

                for (ChessPosition move : validMoves) {
                    moves.add(new ChessMove(myPosition, move, null));
                }
                break;
            }
            case KING: {
                List<ChessPosition> validMoves = new KingMoveCalculator().validMoves(myPosition, board);

                for (ChessPosition move : validMoves) {
                    moves.add(new ChessMove(myPosition, move, null));
                }
                break;
            }
            case KNIGHT: {
                List<ChessPosition> validMoves = new KnightMoveCalculator().validMoves(myPosition, board);

                for (ChessPosition move : validMoves) {
                    moves.add(new ChessMove(myPosition, move, null));
                }
                break;
            }
            case QUEEN: {
                List<ChessPosition> validMoves = new QueenMoveCalculator().validMoves(myPosition, board);

                for (ChessPosition move : validMoves) {
                    moves.add(new ChessMove(myPosition, move, null));
                }
                break;
            }
            case PAWN: {
                List<ChessPosition> validMoves = new PawnMoveCalculator().validMoves(myPosition, board);

                for (ChessPosition move : validMoves) {

                    if (move.getRow() == 1 || move.getRow() == 8) {
                        moves.add(new ChessMove(myPosition, move, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, move, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, move, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, move, PieceType.KNIGHT));
                    } else moves.add(new ChessMove(myPosition, move, null));
                }
                break;
            }
        }


        return moves;
    }

    @Override
    public String toString() {
        return type + ", " + pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
