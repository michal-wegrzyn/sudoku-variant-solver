package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;

/**
 * Enforces that specified cells can only obtain odd values.
 */
public class OddRule extends SudokuRule {
    /** The set of cells to which the rule applies. */
    public Set<Cell> cells;

    public OddRule(Set<Cell> cells) {
        this.cells = cells;
    }

    public OddRule(List<Cell> cells) {
        this.cells = new HashSet<>(cells);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Integer> oddValues = new HashSet<>();
        for (int i = 1; i <= sudoku.getMaxNumber(); i += 2) {
            oddValues.add(i);
        }
        return new AllowedValuesRule(cells, oddValues).apply(sudoku);
    }
}
