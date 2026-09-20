package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PawnMoveCalculator implements MovesCalculator {
    private enum Direction {
        POSITIVE_ROW_POSITIVE_COL, POSITIVE_ROW_NEGATIVE_COL, NEGATIVE_ROW_NEGATIVE_COL, NEGATIVE_ROW_POSITIVE_COL,
        POSITIVE_ROW, NEGATIVE_ROW
    }
    private List<ChessPosition> moves = new ArrayList<>();
    private List<ChessPosition> blacklist = new ArrayList<>();
    private final int[][] whiteOffsets = {{1,1},{1,-1},{1,0}};
    private final int[][] blackOffsets = {{-1,-1},{-1,1},{-1,0}};
    private final int[][] starterOffsets = {{2,0},{-2,0}};

    private int[][] addStarterOffset(int[][] offsets, int[] starter) {
        List<int[]> listOffsets = new ArrayList<>();
        listOffsets.addAll(Arrays.asList(offsets));
        listOffsets.add(starter);
        return listOffsets.toArray(new int[0][]);
    }

    private Direction findOffsetDirection(int[] offset) {
        if (offset[0] > 0) {
            if (offset[1] > 0) return Direction.POSITIVE_ROW_POSITIVE_COL;
            else if (offset[1] < 0) return Direction.POSITIVE_ROW_NEGATIVE_COL;
            else return Direction.POSITIVE_ROW;
        } else if (offset[0] < 0) {
            if (offset[1] > 0) return Direction.NEGATIVE_ROW_POSITIVE_COL;
            else if (offset[1] < 0) return Direction.NEGATIVE_ROW_NEGATIVE_COL;
        } else return Direction.NEGATIVE_ROW;
        return Direction.NEGATIVE_ROW;
    }
    private void updateBlacklist(ChessPosition target, List<ChessPosition> blacklist, Direction offsetDirection, ChessBoard board) {
        switch (offsetDirection) {
//            case POSITIVE_ROW_POSITIVE_COL: {
//                blacklist.add(new ChessPosition(target.getRow() + 1,target.getColumn() + 1));
//                break;
//            }
//            case POSITIVE_ROW_NEGATIVE_COL: {
//                blacklist.add(new ChessPosition(target.getRow() + 1, target.getColumn() - 1));
//                break;
//            }
//            case NEGATIVE_ROW_NEGATIVE_COL: {
//                blacklist.add(new ChessPosition(target.getRow() - 1, target.getColumn() - 1));
//                break;
//            }
//            case NEGATIVE_ROW_POSITIVE_COL: {
//                blacklist.add(new ChessPosition(target.getRow()- 1, target.getColumn() + 1));
//                break;
//            }
            case POSITIVE_ROW: {
                if (board.getPiece(new ChessPosition(target.getRow() - 1, target.getColumn())) != null) {
                    blacklist.add(new ChessPosition(target.getRow(), target.getColumn()));
                }
                break;
            }
            case NEGATIVE_ROW: {
                if (board.getPiece(new ChessPosition(target.getRow() +  1, target.getColumn())) != null) {
                    blacklist.add(new ChessPosition(target.getRow(), target.getColumn()));
                }


                break;
            }
            default: break;
        }
    }
    private void promote(ChessPosition target) {
        for (int i = 0; i < 4; i++) moves.add(target);
    }

    @Override
    public List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board) {
        ChessPiece piece = board.getPiece(position);
        int[][] offsets;
        int[] starterOffset;
        int starterRow;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            offsets = whiteOffsets;
            starterOffset = starterOffsets[0];
            starterRow = 2;
        }
        else {
            offsets = blackOffsets;
            starterOffset = starterOffsets[1];
            starterRow = 7;
        }
        if (position.getRow() == starterRow ) offsets = addStarterOffset(offsets, starterOffset);

        for (int[] offset: offsets) {
            ChessPosition target = position.addOffset(offset[0], offset[1]);
            Direction offsetDirection = findOffsetDirection(offset);
            if (target.getRow() > 8 || target.getColumn() > 8
                    || target.getRow() < 1 || target.getColumn() < 1) {  continue;
            } else if (blacklist.contains(target)){ continue;
            } else if (board.getPiece(target) != null) {
                if (board.getPiece(target).getTeamColor() != piece.getTeamColor()) {
                    if (offset[1] != 0) {
                        if (target.getRow() == 8 || target.getRow() == 1) promote(target);
                        else moves.add(target);
                    }
                }
            } else if (board.getPiece(target) == null) {
                if (offset[1] != 0) continue;
                else {
                    if (target.getRow() == 8 || target.getRow() == 1) promote(target);
                    else if (offset[0] == 2) {
                        updateBlacklist(target, blacklist, offsetDirection, board);
                    } else moves.add(target);
                }
            } else {
                if (target.getRow() == 8 || target.getRow() == 1) promote(target);
                else if (piece.getTeamColor() != board.getPiece(target).getTeamColor()) moves.add(target);
                else moves.add(target);
            }
        }

        blacklist.clear();
        return moves;
    }
}

