package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlayerStateTest {
    @Test
    void playerCanKeepSimpleFields() {
        Player player = new Player();

        assertEquals('\0', player.sign);
        assertEquals(0, player.lastMove);
        assertFalse(player.selected);
        assertFalse(player.winner);

        player.sign = 'O';
        player.lastMove = 7;
        player.selected = true;
        player.winner = true;

        assertEquals('O', player.sign);
        assertEquals(7, player.lastMove);
        assertTrue(player.selected);
        assertTrue(player.winner);
    }

    @Test
    void enumContainsGameStates() {
        assertEquals(State.PLAYING, State.valueOf("PLAYING"));
        assertEquals(State.OWIN, State.valueOf("OWIN"));
        assertEquals(State.XWIN, State.valueOf("XWIN"));
        assertEquals(State.DRAW, State.valueOf("DRAW"));
        assertEquals(4, State.values().length);
    }
}
