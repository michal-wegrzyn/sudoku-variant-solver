package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllDifferentConstraint;

/**
 * Enforces that no two cells a knight's move apart can have the same value.
 */
public class AntiKnightRule extends SudokuRule {
    /**
     * The set of cells to which the rule applies.
     * If null, the rule applies to all cells in the Sudoku grid.
     */
    public Set<Cell> cells;

    public AntiKnightRule(Set<Cell> cells) {
        this.cells = cells;
    }

    public AntiKnightRule(List<Cell> cells) {
        this.cells = new HashSet<>(cells);
    }

    public AntiKnightRule(Cell... cells) {
        this.cells = new HashSet<>(List.of(cells));
    }

    /** Applies the rule to all cells in the Sudoku grid. */
    public AntiKnightRule() {
        this.cells = null;
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Cell> cells = this.cells != null ? this.cells : sudoku.getCells();

        Set<Constraint> constraints = new HashSet<>();
        for (Cell cell : cells) {
            for (int dr : List.of(-2, -1, 1, 2)) {
                int dc = 3 - Math.abs(dr);
                Cell cell2 = new Cell(cell.row() + dr, cell.col() + dc);
                if (cells.contains(cell2)) {
                    constraints.add(new AllDifferentConstraint(cell.toString(),
                            Cell.toString(cell.row() + dr, cell.col() + dc)));
                }
            }
        }
        return constraints;
    }
}
