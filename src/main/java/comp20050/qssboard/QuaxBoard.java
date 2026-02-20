package comp20050.qssboard;

import java.util.Arrays;

/*
 * 21x21 matrix representation of the Quax board used to test the game logic
 * without having a GUI present
 *
 * Encoding used by integration tests:
 * 0 = octagonal cell
 * 1 = rhombic cell
 * 2 = empty / no cell
 */
public final class QuaxBoard {

    public static final int SIZE = 21;

    private final int[][] cells = new int[SIZE][SIZE];

    public QuaxBoard() {
        initialise();
    }

    /*
     * Initialise the 21x21 board pattern:
     * - even/even -> 0 (octagon)
     * - odd/odd   -> 1 (rhombus)
     * - otherwise -> 2 (empty)
     */
    public void initialise() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if ((r % 2 == 0) && (c % 2 == 0)) {
                    cells[r][c] = 0;
                } else if ((r % 2 == 1) && (c % 2 == 1)) {
                    cells[r][c] = 1;
                } else {
                    cells[r][c] = 2;
                }
            }
        }
    }

    // Deep copy
    public int[][] snapshot() {
        int[][] copy = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            copy[r] = Arrays.copyOf(cells[r], SIZE);
        }
        return copy;
    }

    public int get(int row, int col) {
        return cells[row][col];
    }
}
