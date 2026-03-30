package comp20050.qssboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Integration tests following a "BoardA -> logic -> BoardB" approach.
 * These validate the internal board representation (QuaxBoard) that the GUI renders.
 */
public class IntTestQuaxBoard {

    private static final int OCTAGON = 0;
    private static final int RHOMBUS = 1;
    private static final int EMPTY = 2;

    @Test
    void initialBoard_is21x21() {
        QuaxBoard board = new QuaxBoard();
        int[][] snapshot = board.snapshot();

        assertEquals(QuaxBoard.SIZE, snapshot.length, "Board must have 21 rows");
        for (int row = 0; row < QuaxBoard.SIZE; row++) {
            assertEquals(QuaxBoard.SIZE, snapshot[row].length, "Row " + row + " must have 21 columns");
        }
    }

    @Test
    void initialBoard_containsOnlyValidCodes_0_1_2() {
        QuaxBoard board = new QuaxBoard();
        int[][] snap = board.snapshot();

        for (int row = 0; row < QuaxBoard.SIZE; row++) {
            for (int col = 0; col < QuaxBoard.SIZE; col++) {
                int v = snap[row][col];
                assertTrue(v == OCTAGON || v == RHOMBUS || v == EMPTY,
                        "Invalid code at (" + row + "," + col + "): " + v + " (expected 0,1,2)");
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
        int[][] expectedBoard = new int[QuaxBoard.SIZE][QuaxBoard.SIZE];
        for (int row = 0; row < QuaxBoard.SIZE; row++) {
            for (int col = 0; col < QuaxBoard.SIZE; col++) {
                if ((row % 2 == 0) && (col % 2 == 0)) {
                    expectedBoard[row][col] = OCTAGON;
                } else if ((row % 2 == 1) && (col % 2 == 1)) {
                    expectedBoard[row][col] = RHOMBUS;
                } else {
                    expectedBoard[row][col] = EMPTY;
                }
            }
        }
        return expectedBoard;
    }

    private static void assertBoardsEqual(int[][] expected, int[][] actual) {
        assertEquals(QuaxBoard.SIZE, actual.length, "Unexpected row count");
        for (int row = 0; row < QuaxBoard.SIZE; row++) {
            assertArrayEquals(expected[row], actual[row], "Mismatch on row " + row);
        }
    }
}
