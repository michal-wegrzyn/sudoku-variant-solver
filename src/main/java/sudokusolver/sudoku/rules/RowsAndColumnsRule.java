package sudokusolver.sudoku.rules;

import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllDifferentConstraint;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Enforces that the values in each row and column of a specified rectangle must
 * be distinct.
 */
public class RowsAndColumnsRule extends SudokuRule {
    /** The top-left cell of the rectangle. */
    public Cell topLeft;
    /** The width of the rectangle. */
    public int width;
    /** The height of the rectangle. */
    public int height;

    public RowsAndColumnsRule(Cell topLeft, int width) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = width;
    }

    public RowsAndColumnsRule(int topRow, int leftCol, int width) {
        this(new Cell(topRow, leftCol), width, width);
    }

    public RowsAndColumnsRule(Cell topLeft, int width, int height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
    }

    public RowsAndColumnsRule(int topRow, int leftCol, int width, int height) {
        this(new Cell(topRow, leftCol), width, height);
    }

    public RowsAndColumnsRule(int width, int height) {
        this(new Cell(0, 0), width, height);
    }

    public RowsAndColumnsRule(int width) {
        this(new Cell(0, 0), width, width);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        if (sudoku.getMaxNumber() < width) {
            throw new IllegalArgumentException("Width exceeds maxNumber.");
        }
        if (sudoku.getMaxNumber() < height) {
            throw new IllegalArgumentException("Height exceeds maxNumber.");
        }
        Set<Constraint> constraints = new HashSet<>();
        int topRow = topLeft.row();
        int leftCol = topLeft.col();
        // Rows
        for (int row = topRow; row < topRow + height; row++) {
            List<String> variables = new ArrayList<>();
            for (int col = leftCol; col < leftCol + width; col++) {
                if (sudoku.getCellValue(row, col) < 0) {
                    throw new IllegalArgumentException(
                            "Sudoku board contains doesn't contain cell " + Cell.toString(row, col));
                }
                variables.add(Cell.toString(row, col));
            }
            constraints.add(new AllDifferentConstraint(variables));
        }
        // Columns
        for (int col = leftCol; col < leftCol + width; col++) {
            List<String> variables = new ArrayList<>();
            for (int row = topRow; row < topRow + height; row++) {
                variables.add(Cell.toString(row, col));
            }
            constraints.add(new AllDifferentConstraint(variables));
        }
        return constraints;
    }
}
