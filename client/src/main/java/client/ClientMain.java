package client;

import chess.*;
import ui.ConsoleView;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Repl repl = new Repl(new ServerFacade(), new ConsoleView(System.out));
        repl.run(new Scanner(System.in));
    }
}
