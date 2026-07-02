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
 * Enforces that the values on both main diagonals of a square region are
 * distinct.
 */
public class DiagonalRule extends SudokuRule {
    /**
     * The top-left cell of the square region to which the rule applies.
     */
    public Cell topLeft;
    /**
     * The width of the square region to which the rule applies.
     */
    public int width;

    public DiagonalRule(Cell topLeft, int width) {
        this.width = width;
        this.topLeft = topLeft;
    }

    public DiagonalRule(int topRow, int leftCol, int width) {
        this(new Cell(topRow, leftCol), width);
    }

    public DiagonalRule(int width) {
        this(new Cell(0, 0), width);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Constraint> constraints = new HashSet<>();
        List<String> variables = new ArrayList<>();
        int topRow = topLeft.row();
        int leftCol = topLeft.col();
        for (int i = 0; i < width; i++) {
            variables.add(Cell.toString(topRow + i, leftCol + i));
        }
        constraints.add(new AllDifferentConstraint(variables));
        variables = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            variables.add(Cell.toString(topRow + i, leftCol + width - 1 - i));
        }
        constraints.add(new AllDifferentConstraint(variables));
        return constraints;
    }
}
