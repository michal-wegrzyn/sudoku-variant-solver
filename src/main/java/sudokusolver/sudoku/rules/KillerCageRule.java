package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;

/**
 * Enforces that the values in a specified set of cells must sum to a given
 * value.
 */
public class KillerCageRule extends SudokuRule {
    /** The target sum */
    public int sum;
    /** The set of cells to which the rule applies. */
    public Set<Cell> cells;

    public KillerCageRule(int sum, Set<Cell> cells) {
        this.sum = sum;
        this.cells = cells;
    }

    public KillerCageRule(int sum, List<Cell> cells) {
        this.sum = sum;
        this.cells = new HashSet<>(cells);
    }

    public KillerCageRule(int sum, Cell... cells) {
        this.sum = sum;
        this.cells = new HashSet<>(List.of(cells));
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        return Set
                .of(new sudokusolver.csp.constraints.SumConstraint(cells.stream().map(Cell::toString).toList(), sum));
    }
}
