package sudokusolver.sudoku.rules;

import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllDifferentConstraint;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Enforces the set of cells to hold different values.
 */
public class AllDifferentRule extends SudokuRule {
    public Set<Cell> cells;

    public AllDifferentRule(List<Cell> cells) {
        this.cells = new HashSet<>(cells);
    }

    public AllDifferentRule(Set<Cell> cells) {
        this.cells = cells;
    }

    public AllDifferentRule(Cell... cells) {
        this.cells = new HashSet<>(Set.of(cells));
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Constraint> constraints = new HashSet<>();
        constraints.add(new AllDifferentConstraint(cells.stream().map(Cell::toString).toList()));
        return constraints;
    }
}
