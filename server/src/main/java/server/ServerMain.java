package server;

import chess.*;
import dataaccess.DataAccessException;
import dataaccess.sql.ResponseException;

public class ServerMain {
    public static void main(String[] args) throws ResponseException, DataAccessException {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var port = 8080;
        Server server = new Server();
        server.run(port);
        System.out.println("♕ 240 Chess Server running on port: " + port);
    }
}
