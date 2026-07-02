package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AscendingConstraint;

/**
 * Enforces that the values in a specified list of cells form a "thermometer",
 * meaning that the value in each cell must be strictly less than the value in
 * the next cell in the list.
 */
public class ThermoRule extends SudokuRule {
    public List<Cell> cells;

    public ThermoRule(List<Cell> cells) {
        this.cells = cells;
    }

    public ThermoRule(Cell... cells) {
        this.cells = List.of(cells);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Constraint> constraints = new HashSet<>();
        constraints.add(new AscendingConstraint(cells.stream().map(Cell::toString).toList()));
        return constraints;
    }
}
