package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllowedValuesConstraint;

/**
 * Enforces that the values of a set of cells belong to a specified set of
 * allowed values.
 */
public class AllowedValuesRule extends SudokuRule {
    public Set<Cell> cells;
    public Set<Integer> allowedValues;

    public AllowedValuesRule(Set<Cell> cells, Set<Integer> allowedValues) {
        this.cells = cells;
        this.allowedValues = allowedValues;
    }

    public AllowedValuesRule(List<Cell> cells, List<Integer> allowedValues) {
        this.cells = new HashSet<>(cells);
        this.allowedValues = new HashSet<>(allowedValues);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Cell> cells = this.cells != null ? this.cells : sudoku.getCells();

        Set<Constraint> constraints = new HashSet<>();
        constraints.add(new AllowedValuesConstraint(cells.stream().map(Cell::toString).toList(), allowedValues));
        return constraints;
    }
}
