package comp20050.qssboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Integration tests following a "BoardA -> logic -> BoardB" approach.
 * These validate the internal board representation (QuaxBoard) that the GUI renders.
 */
public class QuaxBoardIntegrationTest {

    @Test
    void initialBoard_is21x21() {
        QuaxBoard board = new QuaxBoard();
        int[][] snap = board.snapshot();

        assertEquals(QuaxBoard.SIZE, snap.length, "Board must have 21 rows");
        for (int r = 0; r < QuaxBoard.SIZE; r++) {
            assertEquals(QuaxBoard.SIZE, snap[r].length, "Row " + r + " must have 21 columns");
        }
    }

    @Test
    void initialBoard_containsOnlyValidCodes_0_1_2() {
        QuaxBoard board = new QuaxBoard();
        int[][] snap = board.snapshot();

        for (int r = 0; r < QuaxBoard.SIZE; r++) {
            for (int c = 0; c < QuaxBoard.SIZE; c++) {
                int v = snap[r][c];
                assertTrue(v == 0 || v == 1 || v == 2,
                        "Invalid code at (" + r + "," + c + "): " + v + " (expected 0,1,2)");
            }
        }
    }

    @Test
    void initialBoard_matchesExpectedPattern_boardB() {
        // BoardA: a fresh QuaxBoard
        QuaxBoard boardA = new QuaxBoard();

        // Logic under test
        boardA.initialise();

        // BoardB: expected result
        int[][] boardB = expectedInitialBoard();

        assertBoardsEqual(boardB, boardA.snapshot());
    }

    private static int[][] expectedInitialBoard() {
        int[][] e = new int[QuaxBoard.SIZE][QuaxBoard.SIZE];
        for (int r = 0; r < QuaxBoard.SIZE; r++) {
            for (int c = 0; c < QuaxBoard.SIZE; c++) {
                if ((r % 2 == 0) && (c % 2 == 0)) {
                    e[r][c] = 0; // octagon
                } else if ((r % 2 == 1) && (c % 2 == 1)) {
                    e[r][c] = 1; // rhombus
                } else {
                    e[r][c] = 2; // empty
                }
            }
        }
        return e;
    }

    private static void assertBoardsEqual(int[][] expected, int[][] actual) {
        assertEquals(QuaxBoard.SIZE, actual.length, "Unexpected row count");
        for (int r = 0; r < QuaxBoard.SIZE; r++) {
            assertArrayEquals(expected[r], actual[r], "Mismatch on row " + r);
        }
    }
}
