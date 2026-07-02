package sudokusolver.sudoku.rules;

import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllEqualConstraint;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Enforces the set of cells to hold the same value.
 */
public class AllEqualRule extends SudokuRule {
    public Set<Cell> cells;

    public AllEqualRule(List<Cell> cells) {
        this.cells = new HashSet<>(cells);
    }

    public AllEqualRule(Set<Cell> cells) {
        this.cells = cells;
    }

    public AllEqualRule(Cell... cells) {
        this.cells = new HashSet<>(Set.of(cells));
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Constraint> constraints = new HashSet<>();
        constraints.add(new AllEqualConstraint(cells.stream().map(Cell::toString).toList()));
        return constraints;
    }
}
