package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void newGameHasEmptyBoardAndTwoPlayers() {
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.human.sign);
        assertEquals('O', game.computer.sign);
        for (char cell : game.board) {
            assertEquals(Game.EMPTY, cell);
        }
    }

    @Test
    void checkStateFindsAllXWins() {
        game.checkedSign = 'X';
        assertEquals(State.XWIN, game.checkState(new char[] {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '}));
        assertEquals(State.XWIN, game.checkState(new char[] {'X', ' ', ' ', 'X', ' ', ' ', 'X', ' ', ' '}));
        assertEquals(State.XWIN, game.checkState(new char[] {'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X'}));
        assertEquals(State.XWIN, game.checkState(new char[] {' ', ' ', 'X', ' ', 'X', ' ', 'X', ' ', ' '}));
    }

    @Test
    void checkStateFindsOWinDrawAndPlaying() {
        game.checkedSign = 'O';
        assertEquals(State.OWIN, game.checkState(new char[] {'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '}));

        game.checkedSign = 'X';
        assertEquals(State.DRAW, game.checkState(new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'}));
        assertEquals(State.PLAYING, game.checkState(new char[] {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '}));
    }

    @Test
    void generateMovesReturnsOnlyFreeCells() {
        List<Integer> moves = new ArrayList<>();

        game.generateMoves(new char[] {'X', ' ', 'O', ' ', 'X', ' ', ' ', 'O', ' '}, moves);

        assertEquals(5, moves.size());
        assertTrue(moves.contains(1));
        assertTrue(moves.contains(3));
        assertTrue(moves.contains(5));
        assertTrue(moves.contains(6));
        assertTrue(moves.contains(8));
    }

    @Test
    void makeMoveRejectsBadCellsAndBusyCells() {
        assertFalse(game.makeMove(-1, game.human));
        assertFalse(game.makeMove(9, game.human));

        assertTrue(game.makeMove(4, game.human));
        assertEquals('X', game.board[4]);
        assertEquals(5, game.human.lastMove);
        assertFalse(game.makeMove(4, game.computer));
    }

    @Test
    void evaluatePositionScoresWinLossDrawAndOpenGame() {
        Player x = new Player('X');
        Player o = new Player('O');

        assertEquals(Game.WIN_SCORE, game.evaluatePosition(
                new char[] {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '}, x));
        assertEquals(-Game.WIN_SCORE, game.evaluatePosition(
                new char[] {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '}, o));
        assertEquals(0, game.evaluatePosition(
                new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'}, x));
        assertEquals(-1, game.evaluatePosition(
                new char[] {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, x));
    }

    @Test
    void minimaxTakesWinningCell() {
        char[] board = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};

        assertEquals(3, game.minimax(board, game.computer));
    }

    @Test
    void minimaxBlocksOpponentWin() {
        char[] board = {'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O', ' '};
        int move = game.minimax(board, game.computer);

        board[move - 1] = game.computer.sign;
        game.checkedSign = 'X';

        assertEquals(3, move);
        assertNotEquals(State.XWIN, game.checkState(board));
    }

    @Test
    void minAndMaxHandleTerminalAndNonTerminalPositions() {
        Player x = new Player('X');
        char[] win = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        char[] open = {'X', ' ', ' ', ' ', 'O', ' ', ' ', ' ', ' '};

        assertEquals(Game.WIN_SCORE, game.minMove(win, x));
        assertEquals(Game.WIN_SCORE, game.maxMove(win, x));
        assertTrue(game.minMove(open, x) >= -Game.WIN_SCORE);
        assertTrue(game.maxMove(open, x) <= Game.WIN_SCORE);
    }
}
