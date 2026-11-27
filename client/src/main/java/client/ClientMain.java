package client;

import chess.*;
import ui.ConsoleView;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:0";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Repl repl = new Repl(new ServerFacade(serverUrl), new ConsoleView(System.out));
        repl.run(new Scanner(System.in));
    }
}
