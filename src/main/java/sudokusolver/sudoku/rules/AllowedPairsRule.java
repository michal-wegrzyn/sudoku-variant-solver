package sudokusolver.sudoku.rules;

import java.util.Set;
import sudokusolver.sudoku.Cell;
import sudokusolver.csp.ValuePair;
import sudokusolver.csp.Constraint;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.constraints.AllowedPairsConstraint;

/**
 * Enforces that the values of two cells belong to a specified set of allowed
 * pairs.
 */
public class AllowedPairsRule extends SudokuRule {
    public Cell first;
    public Cell second;
    public Set<ValuePair> allowedPairs;

    public AllowedPairsRule(Cell first, Cell second, Set<ValuePair> allowedPairs) {
        this.first = first;
        this.second = second;
        this.allowedPairs = Set.copyOf(allowedPairs);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        return Set.of(new AllowedPairsConstraint(first.toString(), second.toString(), allowedPairs));
    }
}
