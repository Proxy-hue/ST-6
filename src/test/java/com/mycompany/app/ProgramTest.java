package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgramTest {

    private Program.Game game;

    @BeforeEach
    void setUp() {
        game = new Program.Game();
    }

    @Test
    void resetClearsBoardAndState() {
        game.playMove(0, 'X');
        game.playMove(4, 'O');

        game.reset();

        assertArrayEquals(new char[] {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, game.snapshotBoard());
        assertEquals(Program.State.PLAYING, game.getState());
        assertEquals('X', game.getCurrentSymbol());
    }

    @Test
    void playMovePlacesSymbolAndChangesTurn() {
        assertTrue(game.playMove(4, 'X'));

        assertEquals('X', game.snapshotBoard()[4]);
        assertEquals('O', game.getCurrentSymbol());
        assertEquals(Program.State.PLAYING, game.getState());
    }

    @Test
    void playMoveRejectsIllegalIndexesAndOccupiedCells() {
        assertFalse(game.playMove(-1, 'X'));
        assertFalse(game.playMove(9, 'X'));
        assertTrue(game.playMove(0, 'X'));
        assertFalse(game.playMove(0, 'O'));
    }

    @Test
    void generateMovesReturnsAllEmptyCells() {
        char[] board = new char[] {'X', ' ', 'O', ' ', 'X', ' ', 'O', ' ', ' '};

        List<Integer> moves = game.generateMoves(board);

        assertEquals(List.of(1, 3, 5, 7, 8), moves);
    }

    @Test
    void checkStateDetectsRowWinForX() {
        char[] board = new char[] {'X', 'X', 'X', 'O', ' ', 'O', ' ', ' ', ' '};

        assertEquals(Program.State.XWIN, game.checkState(board, 'X'));
    }

    @Test
    void checkStateDetectsColumnWinForO() {
        char[] board = new char[] {'O', 'X', 'X', 'O', 'X', ' ', 'O', ' ', ' '};

        assertEquals(Program.State.OWIN, game.checkState(board, 'O'));
    }

    @Test
    void checkStateDetectsDiagonalWin() {
        char[] board = new char[] {'X', 'O', ' ', ' ', 'X', 'O', ' ', ' ', 'X'};

        assertEquals(Program.State.XWIN, game.checkState(board, 'X'));
    }

    @Test
    void checkStateDetectsDrawAndPlaying() {
        char[] drawBoard = new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        char[] playingBoard = new char[] {'X', 'O', 'X', 'X', ' ', 'O', 'O', 'X', 'X'};

        assertEquals(Program.State.DRAW, game.checkState(drawBoard, 'X'));
        assertEquals(Program.State.PLAYING, game.checkState(playingBoard, 'O'));
    }

    @Test
    void evaluatePositionScoresWinLossDrawAndOngoing() {
        Program.Player x = new Program.Player('X');
        Program.Player o = new Program.Player('O');
        char[] xWin = new char[] {'X', 'X', 'X', 'O', ' ', 'O', ' ', ' ', ' '};
        char[] oWin = new char[] {'O', 'X', 'X', 'O', 'X', ' ', 'O', ' ', ' '};
        char[] draw = new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        char[] ongoing = new char[] {'X', 'O', 'X', 'X', ' ', 'O', 'O', 'X', 'X'};

        assertEquals(Program.Game.INF, game.evaluatePosition(xWin, x));
        assertEquals(-Program.Game.INF, game.evaluatePosition(xWin, o));
        assertEquals(Program.Game.INF, game.evaluatePosition(oWin, o));
        assertEquals(0, game.evaluatePosition(draw, x));
        assertEquals(-1, game.evaluatePosition(ongoing, x));
    }

    @Test
    void miniMaxFindsImmediateWinningMove() {
        char[] board = new char[] {'X', 'X', ' ', 'O', ' ', 'O', ' ', ' ', ' '};

        int move = game.miniMax(board, new Program.Player('X'));

        assertEquals(3, move);
    }

    @Test
    void miniMaxBlocksOpponentThreat() {
        char[] board = new char[] {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};

        int move = game.miniMax(board, new Program.Player('X'));

        assertEquals(3, move);
    }

    @Test
    void miniMaxReturnsMinusOneWhenNoMovesExist() {
        char[] fullBoard = new char[] {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};

        assertEquals(-1, game.miniMax(fullBoard, new Program.Player('X')));
    }

    @Test
    void loadBoardUpdatesInternalState() {
        char[] board = new char[] {'X', 'X', 'X', 'O', ' ', 'O', ' ', ' ', ' '};

        game.loadBoard(board);

        assertArrayEquals(board, game.snapshotBoard());
        assertEquals(Program.State.XWIN, game.getState());
    }

    @Test
    void constructorsAndValidationRejectBadInput() {
        assertThrows(IllegalArgumentException.class, () -> new Program.Player('Z'));
        assertThrows(IllegalArgumentException.class, () -> game.checkState(new char[8], 'X'));
        assertThrows(IllegalArgumentException.class, () -> game.generateMoves(null));
        assertThrows(IllegalArgumentException.class, () -> game.evaluatePosition(new char[9], null));
    }
}
