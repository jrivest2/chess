package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        squares = new ChessPiece[8][8];

        int pawnRow;
        ChessGame.TeamColor pawnColor;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                pawnRow = 1;
                pawnColor = ChessGame.TeamColor.WHITE;
            }
            else {
                pawnRow = 6;
                pawnColor = ChessGame.TeamColor.BLACK;
            }
            for (int j = 0; j < 8; j++) {
                squares[pawnRow][j] = new ChessPiece(pawnColor, ChessPiece.PieceType.PAWN);
            }
        }

        int pieceRow;
        ChessGame.TeamColor pieceColor;

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                pieceRow = 0;
                pieceColor = ChessGame.TeamColor.WHITE;
            }
            else {
                pieceRow = 7;
                pieceColor = ChessGame.TeamColor.BLACK;
            }
            for (int j = 0; j < 8; j++) {
                if (j == 0 || j == 7) squares[pieceRow][j] = new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK);
                if (j == 1 || j == 6) squares[pieceRow][j] = new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT);
                if (j == 2 || j == 5) squares[pieceRow][j] = new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP);
                if (j == 3) squares[pieceRow][j] = new ChessPiece(pieceColor, ChessPiece.PieceType.QUEEN);
                if (j == 4) squares[pieceRow][j] = new ChessPiece(pieceColor, ChessPiece.PieceType.KING);

            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
