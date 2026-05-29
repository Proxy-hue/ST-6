package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import org.junit.jupiter.api.Test;

class TicTacToePanelTest {
    @Test
    void panelCreatesNineCells() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));

        assertEquals(9, panel.getComponentCount());
        assertEquals(Game.EMPTY, panel.getCell(0).getMarker());
    }

    @Test
    void playerMoveAlsoStartsComputerAnswer() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));

        assertTrue(panel.playHumanMove(0));

        assertEquals('X', panel.getCell(0).getMarker());
        assertFalse(panel.getCell(0).isEnabled());
        assertTrue(hasComputerMove(panel));
    }

    @Test
    void actionEventWorksLikeButtonClick() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        ActionEvent event = new ActionEvent(panel.getCell(2), ActionEvent.ACTION_PERFORMED, "click");

        panel.actionPerformed(event);

        assertEquals('X', panel.getCell(2).getMarker());
    }

    @Test
    void occupiedCellIsNotPlayedAgain() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));

        assertTrue(panel.playHumanMove(0));
        assertFalse(panel.playHumanMove(0));
        assertEquals('X', panel.getCell(0).getMarker());
    }

    @Test
    void finishTextShowsWinnerAndDraw() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        panel.game.state = State.XWIN;
        panel.finishIfNeeded();
        assertEquals("X wins", panel.getResultText());

        TicTacToePanel drawPanel = new TicTacToePanel(new GridLayout(3, 3));
        drawPanel.game.state = State.DRAW;
        drawPanel.finishIfNeeded();
        assertEquals("Draw", drawPanel.getResultText());
    }

    private boolean hasComputerMove(TicTacToePanel panel) {
        for (int i = 0; i < Game.SIZE; i++) {
            if (panel.getCell(i).getMarker() == 'O') {
                return true;
            }
        }
        return false;
    }
}
