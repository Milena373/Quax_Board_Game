package comp20050.qssboard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Integration tests for the connected chain detection feature (Sprint 2, Feature 4).
 *
 * These tests validate the chain detection game logic without the use of a GUI.
 * The Quax board is represented as a 21x21 matrix using the following encoding:
 *
 * 0 = empty octagonal cell
 * 1 = empty rhombic cell
 * 2 = no cell
 * 3 = BLACK octagonal stone
 * 4 = WHITE octagonal stone
 * 5 = BLACK rhombic tile
 * 6 = WHITE rhombic tile
 *
 * Each test synthetically initialises a known board layout and verifies
 * that the chain detection logic produces the expected result.
 * Tests cover horizontal, vertical and diagonal chains for both players,
 * as well as cases where chains should NOT be connected.
 */

public class IntTestChainDetection {

    // helper to check if a specific matrix position is in the chain
    private boolean containsCell(List<int[]> chain, int[] cell) {
        for (int[] c : chain) {
            if (c[0] == cell[0] && c[1] == cell[1]) return true;
        }
        return false;
    }

    @Test
    void test_feature4_1_horizontalChainBlack() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 3); // OctCell1
        board.setCell(0, 2, 3); // OctCell2
        board.setCell(0, 4, 3); // OctCell3

        List<int[]> chain = board.getConnectedChain(0, 0, 3);
        assertEquals(3, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0})); // OctCell1
        assertTrue(containsCell(chain, new int[]{0, 2})); // OctCell2
        assertTrue(containsCell(chain, new int[]{0, 4})); // OctCell3
    }

    @Test
    void test_feature4_2_verticalChainBlack() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 3); // OctCell1
        board.setCell(2, 0, 3); // OctCell12
        board.setCell(4, 0, 3); // OctCell23

        List<int[]> chain = board.getConnectedChain(0, 0, 3);
        assertEquals(3, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0})); // OctCell1
        assertTrue(containsCell(chain, new int[]{2, 0})); // OctCell12
        assertTrue(containsCell(chain, new int[]{4, 0})); // OctCell23
    }

    @Test
    void test_feature4_3_diagonalChainWithBridge() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 3); // OctCell1
        board.setCell(1, 1, 5); // RhoCell1 - BLACK bridge
        board.setCell(2, 2, 3); // OctCell13

        List<int[]> chain = board.getConnectedChain(0, 0, 3);
        assertEquals(2, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0})); // OctCell1
        assertTrue(containsCell(chain, new int[]{2, 2})); // OctCell13
    }

    @Test
    void test_feature4_4_diagonalChainWithoutBridge() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 3); // OctCell1
        board.setCell(2, 2, 3); // OctCell13 - no bridge

        List<int[]> chain = board.getConnectedChain(0, 0, 3);
        assertEquals(1, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0}));  // only OctCell1
        assertFalse(containsCell(chain, new int[]{2, 2})); // OctCell13 not reachable
    }

    @Test
    void test_feature4_5_horizontalChainWhite() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 4); // OctCell1 - WHITE
        board.setCell(0, 2, 4); // OctCell2 - WHITE
        board.setCell(0, 4, 4); // OctCell3 - WHITE

        List<int[]> chain = board.getConnectedChain(0, 0, 4);
        assertEquals(3, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0})); // OctCell1
        assertTrue(containsCell(chain, new int[]{0, 2})); // OctCell2
        assertTrue(containsCell(chain, new int[]{0, 4})); // OctCell3
    }

    @Test
    void test_feature4_6_separateChainsNotConnected() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 3); // OctCell1
        board.setCell(0, 4, 3); // OctCell3 - gap at OctCell2

        List<int[]> chain = board.getConnectedChain(0, 0, 3);
        assertEquals(1, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0}));  // only OctCell1
        assertFalse(containsCell(chain, new int[]{0, 4})); // OctCell3 not reachable
    }

    @Test
    void test_feature4_7_mixedChainBlack() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 3); // OctCell1
        board.setCell(0, 2, 3); // OctCell2
        board.setCell(1, 1, 5); // RhoCell1 - BLACK bridge
        board.setCell(2, 2, 3); // OctCell13
        board.setCell(2, 4, 3); // OctCell14

        List<int[]> chain = board.getConnectedChain(0, 0, 3);
        assertEquals(4, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0})); // OctCell1
        assertTrue(containsCell(chain, new int[]{0, 2})); // OctCell2
        assertTrue(containsCell(chain, new int[]{2, 2})); // OctCell13
        assertTrue(containsCell(chain, new int[]{2, 4})); // OctCell14
    }

    @Test
    void test_feature4_8_diagonalChainWhiteWithBridge() {
        QuaxBoard board = new QuaxBoard();
        board.setCell(0, 0, 4); // OctCell1 - WHITE
        board.setCell(1, 1, 6); // RhoCell1 - WHITE bridge
        board.setCell(2, 2, 4); // OctCell13 - WHITE

        List<int[]> chain = board.getConnectedChain(0, 0, 4);
        assertEquals(2, chain.size());
        assertTrue(containsCell(chain, new int[]{0, 0})); // OctCell1
        assertTrue(containsCell(chain, new int[]{2, 2})); // OctCell13
    }
}