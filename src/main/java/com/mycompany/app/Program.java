package com.mycompany.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {

    public static void main(String[] args) {
        Game game = new Game();
        System.out.println("Tic-Tac-Toe minimax demo");
        System.out.println("Best opening move for X: " + game.chooseBestMove('X'));
    }

    public enum State {
        PLAYING, OWIN, XWIN, DRAW
    }

    public static final class Player {
        private final char symbol;

        public Player(char symbol) {
            if (symbol != 'X' && symbol != 'O') {
                throw new IllegalArgumentException("Only X and O are supported");
            }
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    public static class Game {
        public static final int INF = 100;
        public static final char EMPTY = ' ';
        private static final int BOARD_SIDE = 3;
        private static final int CELL_COUNT = BOARD_SIDE * BOARD_SIDE;
        private static final int ONGOING_POSITION = -1;
        private static final int[][] WIN_LINES = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };

        private final Player xPlayer = new Player('X');
        private final Player oPlayer = new Player('O');
        private final char[] board = new char[CELL_COUNT];
        private State state = State.PLAYING;
        private char currentSymbol = 'X';
        private int evaluatedNodes;

        public Game() {
            reset();
        }

        public void reset() {
            Arrays.fill(board, EMPTY);
            state = State.PLAYING;
            currentSymbol = 'X';
            evaluatedNodes = 0;
        }

        public State getState() {
            return state;
        }

        public char getCurrentSymbol() {
            return currentSymbol;
        }

        public int getEvaluatedNodes() {
            return evaluatedNodes;
        }

        public char[] snapshotBoard() {
            return Arrays.copyOf(board, board.length);
        }

        public void loadBoard(char[] newBoard) {
            validateBoard(newBoard);
            System.arraycopy(newBoard, 0, board, 0, board.length);
            state = resolveState(board);
        }

        public boolean playMove(int index, char symbol) {
            validateSymbol(symbol);
            if (!canUseCell(index)) {
                return false;
            }
            board[index] = symbol;
            currentSymbol = opposite(symbol);
            state = checkState(board, symbol);
            return true;
        }

        public List<Integer> generateMoves(char[] candidateBoard) {
            validateBoard(candidateBoard);
            List<Integer> moves = new ArrayList<>();
            for (int cell = 0; cell < candidateBoard.length; cell++) {
                if (isCellFree(candidateBoard, cell)) {
                    moves.add(cell);
                }
            }
            return moves;
        }

        public State checkState(char[] candidateBoard, char symbol) {
            validateBoard(candidateBoard);
            validateSymbol(symbol);
            return resolveState(candidateBoard);
        }

        public int evaluatePosition(char[] candidateBoard, Player player) {
            validateBoard(candidateBoard);
            validatePlayer(player);

            State position = resolveState(candidateBoard);
            if (position == State.PLAYING) {
                return ONGOING_POSITION;
            }
            if (position == State.DRAW) {
                return 0;
            }
            return isVictoryFor(position, player.getSymbol()) ? INF : -INF;
        }

        public int chooseBestMove(char symbol) {
            validateSymbol(symbol);
            Player player = symbol == 'X' ? xPlayer : oPlayer;
            return miniMax(board, player);
        }

        public int miniMax(char[] candidateBoard, Player player) {
            validateBoard(candidateBoard);
            validatePlayer(player);

            MoveScore result = searchBestMove(candidateBoard, player, true);
            return result.move == -1 ? -1 : result.move + 1;
        }

        private MoveScore searchBestMove(char[] candidateBoard, Player player, boolean maximizing) {
            int positionValue = evaluatePosition(candidateBoard, player);
            if (positionValue != ONGOING_POSITION) {
                return new MoveScore(-1, positionValue);
            }

            List<Integer> moves = generateMoves(candidateBoard);
            if (moves.isEmpty()) {
                return new MoveScore(-1, 0);
            }

            evaluatedNodes++;
            char activeSymbol = maximizing ? player.getSymbol() : opposite(player.getSymbol());
            MoveScore best = new MoveScore(-1, maximizing ? -INF : INF);

            for (int move : moves) {
                candidateBoard[move] = activeSymbol;
                MoveScore reply = searchBestMove(candidateBoard, player, !maximizing);
                candidateBoard[move] = EMPTY;
                best = pickBetter(best, new MoveScore(move, reply.score), maximizing);
            }

            return best;
        }

        private MoveScore pickBetter(MoveScore current, MoveScore candidate, boolean maximizing) {
            if (maximizing && candidate.score > current.score) {
                return candidate;
            }
            if (!maximizing && candidate.score < current.score) {
                return candidate;
            }
            return current;
        }

        private State resolveState(char[] candidateBoard) {
            if (hasWinningLine(candidateBoard, 'X')) {
                return State.XWIN;
            }
            if (hasWinningLine(candidateBoard, 'O')) {
                return State.OWIN;
            }
            for (char cell : candidateBoard) {
                if (cell == EMPTY) {
                    return State.PLAYING;
                }
            }
            return State.DRAW;
        }

        private boolean hasWinningLine(char[] candidateBoard, char symbol) {
            for (int[] line : WIN_LINES) {
                if (candidateBoard[line[0]] == symbol
                        && candidateBoard[line[1]] == symbol
                        && candidateBoard[line[2]] == symbol) {
                    return true;
                }
            }
            return false;
        }

        private boolean isVictoryFor(State position, char symbol) {
            return position == State.XWIN && symbol == 'X'
                    || position == State.OWIN && symbol == 'O';
        }

        private boolean canUseCell(int index) {
            return index >= 0 && index < board.length && isCellFree(board, index);
        }

        private boolean isCellFree(char[] candidateBoard, int index) {
            return candidateBoard[index] == EMPTY;
        }

        private void validateBoard(char[] candidateBoard) {
            if (candidateBoard == null || candidateBoard.length != CELL_COUNT) {
                throw new IllegalArgumentException("board must contain exactly " + CELL_COUNT + " cells");
            }
        }

        private void validateSymbol(char symbol) {
            if (symbol != 'X' && symbol != 'O') {
                throw new IllegalArgumentException("Only X and O are supported");
            }
        }

        private void validatePlayer(Player player) {
            if (player == null) {
                throw new IllegalArgumentException("player must not be null");
            }
        }

        private char opposite(char symbol) {
            return symbol == 'X' ? 'O' : 'X';
        }

        private static final class MoveScore {
            private final int move;
            private final int score;

            private MoveScore(int move, int score) {
                this.move = move;
                this.score = score;
            }
        }
    }
}
