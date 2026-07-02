package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;

/**
 * Enforces that specified cells can only obtain even values.
 */
public class EvenRule extends SudokuRule {
    /** The set of cells to which the rule applies. */
    public Set<Cell> cells;

    public EvenRule(Set<Cell> cells) {
        this.cells = cells;
    }

    public EvenRule(List<Cell> cells) {
        this.cells = new HashSet<>(cells);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Integer> evenValues = new HashSet<>();
        for (int i = 2; i <= sudoku.getMaxNumber(); i += 2) {
            evenValues.add(i);
        }
        return new AllowedValuesRule(cells, evenValues).apply(sudoku);
    }
}
