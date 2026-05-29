package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TicTacToeCellTest {
    @Test
    void cellStoresPlaceAndMarker() {
        TicTacToeCell cell = new TicTacToeCell(5, 1, 2);

        assertEquals(5, cell.getNum());
        assertEquals(1, cell.getRow());
        assertEquals(2, cell.getCol());
        assertEquals(Game.EMPTY, cell.getMarker());
        assertTrue(cell.isEnabled());

        cell.setMarker('X');

        assertEquals('X', cell.getMarker());
        assertEquals("X", cell.getText());
        assertFalse(cell.isEnabled());
    }

    @Test
    void cellAcceptsNoughtMarker() {
        TicTacToeCell cell = new TicTacToeCell(2, 0, 2);

        cell.setMarker('O');

        assertEquals('O', cell.getMarker());
        assertFalse(cell.isEnabled());
    }
}
