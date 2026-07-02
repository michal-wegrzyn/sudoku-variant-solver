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
 * Enforces the classic Sudoku rules for a square region.
 * <ul>
 * <li>Each row must contain distinct values.</li>
 * <li>Each column must contain distinct values.</li>
 * <li>Each region must contain distinct values.</li>
 * </ul>
 */
public class ClassicRule extends SudokuRule {
    /**
     * The top-left cell of the square where the rule applies.
     * The rule applies to the square of size (regionWidth * regionHeight) x
     * (regionWidth * regionHeight)
     */
    public Cell topLeft;
    /**
     * The regions' width. Each region must contain distinct values.
     */
    public int regionWidth;
    /**
     * The regions' height. Each region must contain distinct values.
     */
    public int regionHeight;

    public ClassicRule(Cell topLeft, int regionWidth, int regionHeight) {
        this.topLeft = topLeft;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
    }

    public ClassicRule(int topRow, int leftCol, int regionWidth, int regionHeight) {
        this(new Cell(topRow, leftCol), regionWidth, regionHeight);
    }

    public ClassicRule(int regionWidth, int regionHeight) {
        this(new Cell(0, 0), regionWidth, regionHeight);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        int size = regionWidth * regionHeight;
        if (sudoku.getMaxNumber() < size) {
            throw new IllegalArgumentException("Region size exceeds maxNumber.");
        }
        Set<Constraint> constraints = new HashSet<>();
        int topRow = topLeft.row();
        int leftCol = topLeft.col();
        // Rows and columns
        constraints.addAll(new RowsAndColumnsRule(topLeft, regionWidth * regionHeight).apply(sudoku));
        // Regions
        for (int regionRow = 0; regionRow < regionHeight; regionRow++) {
            for (int regionCol = 0; regionCol < regionWidth; regionCol++) {
                List<String> variables = new ArrayList<>();
                for (int row = topRow + regionRow * regionHeight; row < topRow
                        + (regionRow + 1) * regionHeight; row++) {
                    for (int col = leftCol + regionCol * regionWidth; col < leftCol
                            + (regionCol + 1) * regionWidth; col++) {
                        variables.add(Cell.toString(row, col));
                    }
                }
                constraints.add(new AllDifferentConstraint(variables));
            }
        }
        return constraints;
    }
}
