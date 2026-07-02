package sudokusolver.sudoku;

import java.util.Set;
import java.util.stream.Collectors;

import sudokusolver.csp.Constraint;

/**
 * Represents an abstract rule within the Sudoku domain.
 * <p>
 * This class acts as a bridge between high-level Sudoku domain concepts and the
 * low-level, mathematical Constraint Satisfaction Problem (CSP). Each concrete
 * rule is responsible for translating its logic into a set of CSP constraints.
 * </p>
 */
public abstract class SudokuRule {
    /**
     * Translates this high-level Sudoku rule into a set of lower-level CSP
     * constraints bound to the current game configuration.
     * 
     * <p>
     * <b>Variable Naming Contract:</b> When implementing this method, the text
     * identifiers (variable names) passed into the {@link Constraint} constructors
     * MUST strictly correspond to the string representation of the target cells
     * (like returned by {@link Cell#toString()}).
     * </p>
     *
     * @param sudoku the context and configuration of the active Sudoku board
     * @return a set of {@link Constraint} objects that enforce this rule within the
     *         CSP model of the Sudoku puzzle
     */
    public abstract Set<Constraint> apply(Sudoku sudoku);

    /**
     * Identifies and returns all board cells that are actively constrained by this
     * specific rule.
     * 
     * @param sudoku the context and configuration of the active Sudoku board
     * @return a set of {@link Cell} objects affected by this rule
     */
    public Set<Cell> affectedCells(Sudoku sudoku) {
        return apply(sudoku).stream()
                .flatMap(constraint -> constraint.getVariables().stream())
                .distinct()
                .map(Cell::new)
                .collect(Collectors.toSet());
    }
}
