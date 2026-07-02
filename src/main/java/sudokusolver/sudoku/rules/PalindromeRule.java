package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllEqualConstraint;

/**
 * Enforces that the values in a specified list of cells form a palindrome,
 * meaning that the k-th cell in the list must have the same value as the k-th
 * cell from the end of the list for all k.
 */
public class PalindromeRule extends SudokuRule {
    /** The list of cells to which the rule applies. */
    public List<Cell> cells;

    public PalindromeRule(List<Cell> cells) {
        this.cells = cells;
    }

    public PalindromeRule(Cell... cells) {
        this.cells = List.of(cells);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Constraint> constraints = new HashSet<>();
        int length = cells.size();
        for (int i = 0; i < length / 2; i++) {
            constraints.add(new AllEqualConstraint(cells.get(i).toString(), cells.get(length - 1 - i).toString()));
        }
        return constraints;
    }
}
