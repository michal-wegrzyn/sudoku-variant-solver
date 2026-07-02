package sudokusolver.sudoku;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a Sudoku board which is a grid of cells, where each cell can have
 * an assigned value, be empty, or be outside the puzzle.
 */
public class Board {
    /**
     * A map representing the Sudoku grid, where keys are Cell involved in the
     * puzzle and values are their assigned values (0 for empty cells).
     */
    private Map<Cell, Integer> grid;

    /**
     * Initializes the board with a 2D list of integers, where each inner list
     * represents a row of the Sudoku grid. Empty cells should be represented by 0
     * and cells not involved in the puzzle can be represented by -1 or any negative
     * number. The top-left cell has coordinates (0, 0).
     * 
     * @param grid the 2D list of integers representing the Sudoku grid
     */
    public Board(List<List<Integer>> grid) {
        this.grid = new HashMap<>();
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                int value = grid.get(i).get(j);
                if (value < 0) {
                    continue; // Skip cells not involved in the puzzle
                }
                this.grid.put(new Cell(i, j), value);
            }
        }
    }

    /**
     * Initializes the board from a string representation. The string should consist
     * of rows of characters, where each character represents a cell. Valid
     * characters are:
     * - Digits '1' to '9' for values 1 to 9
     * - Letters 'A' to 'Z' for values 10 to 35
     * - '.' for empty cells (value 0)
     * - Any other character (e.g., space) for cells not involved in the puzzle
     * Rows should be separated by newline characters ('\n'). The top-left cell has
     * coordinates (0, 0).
     * 
     * @param text the string representation of the Sudoku grid
     */
    public Board(String text) {
        grid = new HashMap<>();
        int row = 0;
        int col = 0;
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                if (row != 0 || col != 0) {
                    row++;
                    col = 0;
                }
                continue;
            } else if (c == '.') {
                grid.put(new Cell(row, col), 0);
            } else if (Character.isDigit(c)) {
                grid.put(new Cell(row, col), Character.getNumericValue(c));
            } else if (Character.isLetter(c)) {
                grid.put(new Cell(row, col), Character.toUpperCase(c) - 'A' + 10);
            } else if (c != ' ') {
                throw new IllegalArgumentException("Invalid character in board string: " + c);
            }
            col++;
        }
    }

    /**
     * Initializes an empty board.
     */
    public Board() {
        grid = new HashMap<>();
    }

    /**
     * Initializes the board with a map of cell coordinates involved in the puzzle
     * to their values or 0 for empty cells.
     * 
     * @param cellValues the map of cell coordinates to their values
     */
    public Board(Map<Cell, Integer> cellValues) {
        grid = new HashMap<>(cellValues);
        // Remove any cells with negative values, as they are not involved in the puzzle
        grid.entrySet().removeIf(entry -> entry.getValue() < 0);
    }

    /**
     * Creates a Board instance from a map of cell coordinates to their values or 0
     * for empty cells. Keys in the map are string representations of Cell objects.
     *
     * @param cellValues the map of cell coordinates to their values
     * @return a Board instance representing the given cell values
     */
    public static Board fromStringMap(Map<String, Integer> cellValues) {
        Board board = new Board(new ArrayList<>());
        for (Map.Entry<String, Integer> entry : cellValues.entrySet()) {
            Cell cell = new Cell(entry.getKey());
            int value = entry.getValue();
            if (value < 0) {
                continue; // Skip cells not involved in the puzzle
            }
            board.setCellValue(cell.row(), cell.col(), value);
        }
        return board;
    }

    /**
     * Returns the value of the given cell.
     * 
     * @param cell the target cell
     * @return the value of the cell, or 0 for empty cells, or -1 for cells not
     *         involved in the puzzle
     */
    int getCellValue(Cell cell) {
        return grid.getOrDefault(cell, -1);
    }

    /**
     * Returns the value of the cell at the specified row and column.
     * 
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the value of the cell, or 0 for empty cells, or -1 for cells not
     *         involved in the puzzle
     */
    int getCellValue(int row, int col) {
        return grid.getOrDefault(new Cell(row, col), -1);
    }

    /**
     * Sets the value of the given cell. A negative value indicates that the cell is
     * not involved in the puzzle and will be removed from the grid.
     * 
     * @param cell  the target cell
     * @param value the value to set, where 0 represents an empty cell and negative
     *              values represent cells not involved in the puzzle
     */
    void setCellValue(Cell cell, int value) {
        if (value < 0) {
            grid.remove(cell);
        } else {
            grid.put(cell, value);
        }
    }

    /**
     * Sets the value of the cell at the specified row and column. A negative value
     * indicates that the cell is not involved in the puzzle and will be removed
     * from the grid.
     * 
     * @param row   the row of the cell
     * @param col   the column of the cell
     * @param value the value to set, where 0 represents an empty cell and negative
     *              values represent cells not involved in the puzzle
     */
    void setCellValue(int row, int col, int value) {
        if (value < 0) {
            grid.remove(new Cell(row, col));
        } else {
            grid.put(new Cell(row, col), value);
        }
    }

    /**
     * Returns a set of all cells involved in the puzzle.
     *
     * @return a set of all cells involved in the puzzle
     */
    public Set<Cell> getCells() {
        return grid.keySet();
    }

    /**
     * Returns a string representation of the board.
     * Finds the bounding box of the cells involved in the puzzle and constructs a
     * string representation of that area.
     * <p>
     * Cells are represented as follows:
     * </p>
     * <ul>
     * <li>Empty cells: '.'</li>
     * <li>Cells not involved in the puzzle: ' ' (space)</li>
     * <li>Values 1-9: '1' to '9'</li>
     * <li>Values 10-35: 'A' to 'Z'</li>
     * </ul>
     * <p>
     * Rows are separated by newline characters ('\n').
     * </p>
     *
     * @return a string representation of the board
     */
    @Override
    public String toString() {
        if (grid.isEmpty()) {
            return "";
        }
        int topRow = grid.keySet().stream().mapToInt(Cell::row).min().orElse(0);
        int bottomRow = grid.keySet().stream().mapToInt(Cell::row).max().orElse(0);
        int leftCol = grid.keySet().stream().mapToInt(Cell::col).min().orElse(0);
        int rightCol = grid.keySet().stream().mapToInt(Cell::col).max().orElse(0);
        StringBuilder sb = new StringBuilder();
        for (int r = topRow; r <= bottomRow; r++) {
            for (int c = leftCol; c <= rightCol; c++) {
                int value = getCellValue(r, c);
                if (value == 0) {
                    sb.append('.');
                } else if (value < 0) {
                    sb.append(' ');
                } else if (value <= 9) {
                    sb.append(value);
                } else if (value <= 35) {
                    sb.append((char) ('A' + value - 10));
                } else {
                    throw new IllegalArgumentException("Cell value " + value + " exceeds max number 35");
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public Board copy() {
        return new Board(new HashMap<>(this.grid));
    }

    public int maxAssignedValue() {
        return grid.values().stream().max(Integer::compareTo).orElse(-1);
    }
}
