package com.mycompany.app;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

enum State {
    PLAYING, OWIN, XWIN, DRAW
}

class Player {
    char sign;
    int lastMove;
    boolean selected;
    boolean winner;

    Player() {
    }

    Player(char sign) {
        this.sign = sign;
        this.lastMove = -1;
    }
}

class Game {
    static final char EMPTY = ' ';
    static final int SIZE = 9;
    static final int WIN_SCORE = 100;

    final Player human;
    final Player computer;
    Player currentPlayer;
    State state;
    char[] board;
    char checkedSign;
    int steps;

    Game() {
        human = new Player('X');
        computer = new Player('O');
        currentPlayer = human;
        state = State.PLAYING;
        board = new char[SIZE];
        clearBoard();
    }

    void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = EMPTY;
        }
    }

    State checkState(char[] source) {
        if (isWinner(source, checkedSign)) {
            return checkedSign == 'X' ? State.XWIN : State.OWIN;
        }
        return isFull(source) ? State.DRAW : State.PLAYING;
    }

    boolean isWinner(char[] source, char sign) {
        int[][] lines = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] line : lines) {
            if (source[line[0]] == sign && source[line[1]] == sign && source[line[2]] == sign) {
                return true;
            }
        }
        return false;
    }

    boolean isFull(char[] source) {
        for (char cell : source) {
            if (cell == EMPTY) {
                return false;
            }
        }
        return true;
    }

    boolean makeMove(int cell, Player player) {
        if (cell < 0 || cell >= SIZE || board[cell] != EMPTY) {
            return false;
        }
        board[cell] = player.sign;
        player.lastMove = cell + 1;
        checkedSign = player.sign;
        state = checkState(board);
        return true;
    }

    void generateMoves(char[] source, List<Integer> moves) {
        for (int i = 0; i < source.length; i++) {
            if (source[i] == EMPTY) {
                moves.add(i);
            }
        }
    }

    int evaluatePosition(char[] source, Player player) {
        checkedSign = 'X';
        if (checkState(source) == State.XWIN) {
            return player.sign == 'X' ? WIN_SCORE : -WIN_SCORE;
        }
        checkedSign = 'O';
        if (checkState(source) == State.OWIN) {
            return player.sign == 'O' ? WIN_SCORE : -WIN_SCORE;
        }
        if (isFull(source)) {
            return 0;
        }
        return -1;
    }

    int minimax(char[] source, Player player) {
        int bestValue = -WIN_SCORE;
        int bestMove = -1;
        List<Integer> moves = new ArrayList<>();
        generateMoves(source, moves);

        for (Integer move : moves) {
            source[move] = player.sign;
            int value = minMove(source, player);
            source[move] = EMPTY;
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }
        steps = 0;
        return bestMove + 1;
    }

    int minMove(char[] source, Player player) {
        int position = evaluatePosition(source, player);
        if (position != -1) {
            return position;
        }

        steps++;
        int bestValue = WIN_SCORE;
        char otherSign = player.sign == 'X' ? 'O' : 'X';
        List<Integer> moves = new ArrayList<>();
        generateMoves(source, moves);

        for (Integer move : moves) {
            source[move] = otherSign;
            int value = maxMove(source, player);
            source[move] = EMPTY;
            if (value < bestValue) {
                bestValue = value;
            }
        }
        return bestValue;
    }

    int maxMove(char[] source, Player player) {
        int position = evaluatePosition(source, player);
        if (position != -1) {
            return position;
        }

        steps++;
        int bestValue = -WIN_SCORE;
        List<Integer> moves = new ArrayList<>();
        generateMoves(source, moves);

        for (Integer move : moves) {
            source[move] = player.sign;
            int value = minMove(source, player);
            source[move] = EMPTY;
            if (value > bestValue) {
                bestValue = value;
            }
        }
        return bestValue;
    }
}

public class Program {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Tic Tac Toe");
                frame.add(new TicTacToePanel(new GridLayout(3, 3)));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setBounds(100, 100, 480, 480);
                frame.setVisible(true);
            }
        });
    }
}

class TicTacToeCell extends JButton {
    private final int num;
    private final int row;
    private final int col;
    private char marker;

    TicTacToeCell(int num, int row, int col) {
        this.num = num;
        this.row = row;
        this.col = col;
        this.marker = Game.EMPTY;
        setText(Character.toString(marker));
        setFont(new Font("Arial", Font.PLAIN, 42));
    }

    void setMarker(char marker) {
        this.marker = marker;
        setText(Character.toString(marker));
        setEnabled(false);
    }

    char getMarker() {
        return marker;
    }

    int getNum() {
        return num;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }
}

class Utility {
    static String print(char[] board) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            result.append(board[i]);
            if (i < board.length - 1) {
                result.append('-');
            }
        }
        String text = result.toString();
        System.out.println(text);
        return text;
    }

    static String print(int[] board) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            result.append(board[i]);
            if (i < board.length - 1) {
                result.append('-');
            }
        }
        String text = result.toString();
        System.out.println(text);
        return text;
    }

    static String print(List<Integer> moves) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            result.append(moves.get(i));
            if (i < moves.size() - 1) {
                result.append('-');
            }
        }
        String text = result.toString();
        System.out.println(text);
        return text;
    }
}

class TicTacToePanel extends JPanel implements ActionListener {
    final Game game;
    private final TicTacToeCell[] cells = new TicTacToeCell[Game.SIZE];
    private String resultText = "";

    TicTacToePanel(GridLayout layout) {
        super(layout);
        for (int i = 0; i < Game.SIZE; i++) {
            createCell(i, i / 3, i % 3);
        }
        game = new Game();
    }

    private void createCell(int num, int row, int col) {
        cells[num] = new TicTacToeCell(num, row, col);
        cells[num].addActionListener(this);
        add(cells[num]);
    }

    TicTacToeCell getCell(int index) {
        return cells[index];
    }

    String getResultText() {
        return resultText;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        for (TicTacToeCell cell : cells) {
            if (event.getSource() == cell && cell.getMarker() == Game.EMPTY) {
                playHumanMove(cell.getNum());
                break;
            }
        }
    }

    boolean playHumanMove(int index) {
        if (!game.makeMove(index, game.human)) {
            return false;
        }
        cells[index].setMarker(game.human.sign);
        finishIfNeeded();
        if (game.state == State.PLAYING) {
            playComputerMove();
        }
        return true;
    }

    boolean playComputerMove() {
        int move = game.minimax(game.board, game.computer);
        if (move <= 0) {
            return false;
        }
        int index = move - 1;
        boolean moved = game.makeMove(index, game.computer);
        if (moved) {
            cells[index].setMarker(game.computer.sign);
            finishIfNeeded();
        }
        return moved;
    }

    void finishIfNeeded() {
        if (game.state == State.XWIN) {
            resultText = "X wins";
        } else if (game.state == State.OWIN) {
            resultText = "O wins";
        } else if (game.state == State.DRAW) {
            resultText = "Draw";
        }
        if (!resultText.isEmpty() && !java.awt.GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(this, resultText, "Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
