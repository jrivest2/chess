package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.io.PrintStream;

public class ConsoleView {

    private final PrintStream out;

    public ConsoleView(PrintStream out) {
        this.out = out;
    }

    public void welcome() {
        print(EscapeSequences.BLACK_KING + " Welcome to Chess! " + EscapeSequences.BLACK_KING
                + "\nTry \"login <username> <password>\" or ask for \"help\".\n");
        printPrompt(EscapeSequences.RESET_TEXT_COLOR, "");
    }

    public void success(String message) {
        printPrompt(EscapeSequences.SET_TEXT_COLOR_BLUE, message);
    }

    public void usage(String message) {
        printPrompt(EscapeSequences.SET_TEXT_COLOR_BLUE, message);
    }

    public void error(String message) {
        printPrompt(EscapeSequences.SET_TEXT_COLOR_RED, message);
    }

    public void quit() {
        print(EscapeSequences.SET_TEXT_COLOR_GREEN + "Goodbye! " + EscapeSequences.WHITE_KING + "\n" + EscapeSequences.RESET_TEXT_COLOR);
    }


    public void drawBoard(ChessGame game, ChessGame.TeamColor perspective) {
        System.out.flush();
        ChessBoard board = game.getBoard();
        String[] letters = {"\u2001","a "," b ","  c ","  d  "," e ",
                "  f ","\u2001g ","\u2001 h ","\u2001"};
        String[] blackPerspLetters = {"\u2001","\u2001 a ","\u2001b ","  c ","d ","  e  ",
                "   f ","  g","h ","\u2001"};
        int[] nums = {9,8,7,6,5,4,3,2,1,0};
        int modular;
        if (perspective == ChessGame.TeamColor.WHITE) {modular = 0;}
        else {modular = 1;}
        if (perspective == ChessGame.TeamColor.WHITE) {
            drawLoop(letters,board,perspective,nums,modular);
        } else {
            drawLoop(blackPerspLetters,board,perspective,nums,modular);
        }
        resetColors();
        System.out.print('\n');
    }

    private void drawLoop(String[] letters, ChessBoard board, ChessGame.TeamColor perspective, int[] nums, int modular) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 0 || i == 9 || j == 0 || j == 9) {
                    print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                } else if (i % 2 == modular && j % 2 == modular || i == j || i % 2 != modular && j % 2 != modular) {
                    print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                } else {
                    print(EscapeSequences.SET_BG_COLOR_BLACK);
                }
                if (i == 0 || i == 9) {
                    if (j == 0 || j == 9) {
                        print(EscapeSequences.EMPTY);
                        resetColors();
                        continue;
                    } else {
                        print(letters[j]);
                    }
                } else if (j == 0 || j == 9) {
                    print("\u2001" + String.valueOf(nums[i]) + "\u2001");
                } else if (board.getPiece(new ChessPosition(i, j)) == null) {
                    print(EscapeSequences.EMPTY);
                    resetColors();
                } else {
                    ChessGame.TeamColor color = board.getPiece(new ChessPosition(i, j)).getTeamColor();
                    print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                    switch (board.getPiece(new ChessPosition(i, j)).getPieceType()) {
                        case PAWN -> {
                            if (color == ChessGame.TeamColor.WHITE) {
                                print(EscapeSequences.WHITE_PAWN);
                            } else {
                                print(EscapeSequences.BLACK_PAWN);
                            }
                        }
                        case QUEEN -> {
                            if (color == ChessGame.TeamColor.WHITE) {
                                print(EscapeSequences.WHITE_QUEEN);
                            } else {
                                print(EscapeSequences.BLACK_QUEEN);
                            }
                        }
                        case KING -> {
                            if (color == ChessGame.TeamColor.WHITE) {
                                print(EscapeSequences.WHITE_KING);
                            } else {
                                print(EscapeSequences.BLACK_KING);
                            }
                        }
                        case ROOK -> {
                            if (color == ChessGame.TeamColor.WHITE) {
                                print(EscapeSequences.WHITE_ROOK);
                            } else {
                                print(EscapeSequences.BLACK_ROOK);
                            }
                        }
                        case BISHOP -> {
                            if (color == ChessGame.TeamColor.WHITE) {
                                print(EscapeSequences.WHITE_BISHOP);
                            } else {
                                print(EscapeSequences.BLACK_BISHOP);
                            }
                        }
                        case KNIGHT -> {
                            if (color == ChessGame.TeamColor.WHITE) {
                                print(EscapeSequences.WHITE_KNIGHT);
                            } else {
                                print(EscapeSequences.BLACK_KNIGHT);
                            }
                        }
                    }
                }
                resetColors();
            }
            print("\n");
        }
    }

    private void resetColors() {
        print(EscapeSequences.RESET_TEXT_COLOR
                + EscapeSequences.RESET_BG_COLOR
                + EscapeSequences.RESET_TEXT_BLINKING
                + EscapeSequences.RESET_TEXT_BOLD_FAINT
                + EscapeSequences.RESET_TEXT_ITALIC
                + EscapeSequences.RESET_TEXT_UNDERLINE);
    }

    private void printPrompt(String color, String message) {
        print(color + message);
        print("\n" + EscapeSequences.RESET_TEXT_COLOR + "> ");
    }

    // Synchronized so we don't interleave messages from notifications
    private synchronized void print(String msg) {
        out.print(msg);
    }
}

