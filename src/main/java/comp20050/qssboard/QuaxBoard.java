package comp20050.qssboard;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 21x21 matrix representation of the Quax board used to test the game logic
 * without the GUI
 */
public final class QuaxBoard {

    public static final int SIZE = 21;

    private static final int LOGICAL_SIZE = 11;
    private static final int LAST_INDEX = LOGICAL_SIZE - 1;

    private static final int OCTAGON = 0;
    private static final int RHOMBUS = 1;
    private static final int EMPTY = 2;

    private static final int BLACK_OCT = 3;
    private static final int WHITE_OCT = 4;
    private static final int BLACK_RHO = 5;
    private static final int WHITE_RHO = 6;

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
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row % 2 == 0) && (col % 2 == 0)) {
                    cells[row][col] = OCTAGON;
                } else if ((row % 2 == 1) && (col % 2 == 1)) {
                    cells[row][col] = RHOMBUS;
                } else {
                    cells[row][col] = EMPTY;
                }
            }
        }
    }

    // Deep copy
    public int[][] snapshot() {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            copy[row] = Arrays.copyOf(cells[row], SIZE);
        }
        return copy;
    }

    public int getCellValue(int row, int col) {
        return cells[row][col];
    }

    public void setCell(int row, int col, int value) {
        cells[row][col] = value;
    }

    /**
     * Converts a matrix position (row, col) to a logical board position.
     * The 21x21 matrix has octagons at even/even positions, so dividing by 2
     * gives the logical 11x11 board coordinates.
     * e.g. matrix (0,0) -> logical (0,0), matrix (2,2) -> logical (1,1)
     */
    private int[] matrixToLogical(int matrixRow, int matrixCol) {
        return new int[]{matrixRow / 2, matrixCol / 2};
    }

    /**
     * Converts a logical board position to a matrix position.
     * Multiplying by 2 maps the logical 11x11 coordinates back
     * to the even/even octagon positions in the 21x21 matrix.
     * e.g. logical (0,0) -> matrix (0,0), logical (1,1) -> matrix (2,2)
     */
    private int[] logicalToMatrix(int logicalRow, int logicalCol) {
        return new int[]{logicalRow * 2, logicalCol * 2};
    }

/**
 * Determines whether a connected chain of same-colour cells exists
 * starting from the given matrix position using BFS.
 *
 * Two octagonal cells are connected if:
 * - They are horizontally or vertically adjacent, OR
 * - They are diagonally adjacent AND there is a same-colour rhombic
 *   cell bridging them at the odd/odd matrix position between them.
 */
    public List<int[]> getConnectedChain(int startMatRow, int startMatCol, int playerValue) {
        if (cells[startMatRow][startMatCol] != playerValue) {
            return new ArrayList<>();
        }

        List<int[]> visited = new ArrayList<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startMatRow, startMatCol});
        visited.add(new int[]{startMatRow, startMatCol});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int[] logical = matrixToLogical(current[0], current[1]);
            int logRow = logical[0], logCol = logical[1];

            // horizontal/vertical neighbours (step 2 in matrix = 1 in logical)
            int[][] directOffsets = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] off : directOffsets) {
                int newLogRow = logRow + off[0];
                int newLogCol = logCol + off[1];
                if (newLogRow < 0 || newLogRow > LAST_INDEX || newLogCol < 0 || newLogCol > LAST_INDEX) continue;
                int[] newMat = logicalToMatrix(newLogRow, newLogCol);
                if (cells[newMat[0]][newMat[1]] == playerValue && !containsCell(visited, newMat)) {
                    visited.add(newMat);
                    queue.add(newMat);
                }
            }

            // diagonal neighbours via rhombus bridge
            int[][] diagOffsets = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            for (int[] off : diagOffsets) {
                int newLogRow = logRow + off[0];
                int newLogCol = logCol + off[1];
                if (newLogRow < 0 || newLogRow > LAST_INDEX || newLogCol < 0 || newLogCol > LAST_INDEX) continue;
                int[] newMat = logicalToMatrix(newLogRow, newLogCol);
                // rhombus is at the odd/odd position between the two octagons
                int rhoMatRow = current[0] + off[0];
                int rhoMatCol = current[1] + off[1];
                int rhombusValue = playerValue == BLACK_OCT ? BLACK_RHO : WHITE_RHO;
                if (cells[rhoMatRow][rhoMatCol] == rhombusValue
                        && cells[newMat[0]][newMat[1]] == playerValue
                        && !containsCell(visited, newMat)) {
                    visited.add(newMat);
                    queue.add(newMat);
                }
            }
        }
        return visited;
    }

    /**
     * Checks whether a visited list already contains a given cell position.
     * Used by hasConnectedChain to avoid revisiting cells during BFS.
     */
    private boolean containsCell(List<int[]> visited, int[] cell) {
        for (int[] visitedCell : visited) {
            if (visitedCell[0] == cell[0] && visitedCell[1] == cell[1]){
                return true;
            }
        }
        return false;
    }
}
