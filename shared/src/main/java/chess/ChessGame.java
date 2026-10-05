package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurnIndicator = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();


    public ChessGame() {
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurnIndicator;
    }

    private TeamColor getOtherTeam(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurnIndicator = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        /*
        *   List of things this needs:
        *       -checks if move in pieceMoves(startPosition)
        *           -> Should be fine as is. Just get piece at startPosition and grab moves.
        *       -Checks if move would put you in check
        *           ->Needs a copy of board to simulate moves to use isInCheck()
        */

        ChessPiece piece = this.board.getPiece(startPosition);
        TeamColor teamColor = piece.getTeamColor();
        Collection<ChessMove> resultMoves  = piece.pieceMoves(this.board,startPosition);
        Collection<ChessMove> validMoves = piece.pieceMoves(this.board,startPosition);

        for (ChessMove move : validMoves) {
            ChessBoard testBoard = this.board.clone();
            makeTestMove(move, this.board);
            if (isInCheck(teamColor)) {
                resultMoves.remove(move);
            }
            this.board = testBoard.clone();
        }

        return resultMoves;
    }

    private void makeTestMove(ChessMove move, ChessBoard testBoard) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = testBoard.getPiece(startPosition);
        ChessPiece.PieceType pieceType = piece.getPieceType();

        testBoard.addPiece(startPosition, null);
        testBoard.addPiece(endPosition, new ChessPiece(piece.getTeamColor(), pieceType));
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
//        throw new RuntimeException("Not implemented");
        /*
        *   List of things this needs:
        *       -if move in this.validMoves(move.getStartPosition())
        *       -and startPosition piece team color == teamColorIndicator
        *           -make the move
        *       -else: throw exception.
        */
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        if (piece == null) throw new InvalidMoveException();
        ChessPiece.PieceType pieceType = piece.getPieceType();
        TeamColor teamColor = piece.getTeamColor();
        ChessBoard testBoard = this.board.clone();

        if (piece.getTeamColor() != getTeamTurn()) throw new InvalidMoveException();
        if (validMoves(move.getStartPosition()).contains(move)) {
            makeTestMove(move, this.board);
            if (isInCheck(teamColor)) {
                this.board = testBoard.clone();
                throw new InvalidMoveException("Can't move there! That puts you in Check!");
            }
            if (pieceType == ChessPiece.PieceType.PAWN) {
                if (move.getPromotionPiece() != null) {
                    ChessPiece.PieceType newPieceType = move.getPromotionPiece();
                    this.board.addPiece(move.getEndPosition(), new ChessPiece(teamColor, newPieceType));
                }
            }
            setTeamTurn(getOtherTeam(getTeamTurn()));
//            isInCheckmate(getTeamTurn());

        } else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition square = new ChessPosition(i,j);
                if (board.getPiece(square) == null) continue;
                if (board.getPiece(square).equals(new ChessPiece(teamColor, ChessPiece.PieceType.KING))) {
                    return square;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = findKing(teamColor);
        TeamColor otherTeamColor = getOtherTeam(teamColor);

        // USE findKing(otherTeamColor), then check black pieces for pieceMoves
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition square = new ChessPosition(i,j);
                ChessPiece otherPiece = board.getPiece(square);
                if (otherPiece == null) continue;
                if (otherPiece.getTeamColor() == otherTeamColor) {
                    Collection<ChessMove> otherPieceMoves = otherPiece.pieceMoves(this.board, square);
                    for (ChessMove move : otherPieceMoves) {
                       if (move.getEndPosition().equals(king)) {
                           return true;
                       }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> totalValidMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition square = new ChessPosition(i,j);
                if (board.getPiece(square) == null) continue;
                if (board.getPiece(square).getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = validMoves(square);
                    totalValidMoves.addAll(pieceMoves);
                }
            }
        }
        return isInCheck(teamColor) && totalValidMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> totalValidMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition square = new ChessPosition(i,j);
                if (board.getPiece(square) == null) continue;
                if (board.getPiece(square).getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = validMoves(square);
                    totalValidMoves.addAll(pieceMoves);
                }
            }
        }

        return !isInCheck(teamColor) && totalValidMoves.isEmpty();
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = new ChessBoard();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition square = new ChessPosition(i,j);
                this.board.addPiece(square,board.getPiece(square));
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurnIndicator=" + teamTurnIndicator +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurnIndicator == chessGame.teamTurnIndicator && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurnIndicator, board);
    }
}
