package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UtilityTest {
    @Test
    void printCharBoardReturnsLine() {
        assertEquals("X-O- - -X- - - - ", Utility.print(new char[] {'X', 'O', ' ', ' ', 'X', ' ', ' ', ' ', ' '}));
    }

    @Test
    void printIntBoardReturnsLine() {
        assertEquals("1-2-3-4-5-6-7-8-9", Utility.print(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}));
    }

    @Test
    void printMovesReturnsLine() {
        List<Integer> moves = new ArrayList<>();
        moves.add(1);
        moves.add(4);
        moves.add(8);

        assertEquals("1-4-8", Utility.print(moves));
        assertEquals("", Utility.print(new ArrayList<Integer>()));
    }
}
