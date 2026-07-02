package sudokusolver.sudoku.rules;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Edge;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.ValuePair;
import sudokusolver.csp.constraints.AllowedPairsConstraint;

/**
 * Enforces that the values of cells connected by an edge are consecutive
 * (differ by 1) only if the edge is marked as consecutive, and not consecutive
 * otherwise.
 */
public class ConsecutiveRule extends SudokuRule {
    /** The set of cells to which the rule applies. */
    public Set<Cell> cells;
    /** The set of edges separating cells with consecutive values. */
    public Set<Edge> edges;

    public ConsecutiveRule(Set<Edge> edges, Set<Cell> cells) {
        this.cells = cells;
        this.edges = edges;
    }

    public ConsecutiveRule(List<Edge> edges, List<Cell> cells) {
        this.cells = new HashSet<>(cells);
        this.edges = new HashSet<>(edges);
    }

    public ConsecutiveRule(List<Edge> edges) {
        this.cells = null;
        this.edges = new HashSet<>(edges);
    }

    public ConsecutiveRule(Set<Edge> edges) {
        this.cells = null;
        this.edges = new HashSet<>(edges);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Cell> cells = this.cells != null ? this.cells : sudoku.getCells();
        Set<ValuePair> consecutivePairs = new HashSet<>();
        Set<ValuePair> notConsecutivePairs = new HashSet<>();
        for (int i = 1; i <= sudoku.getMaxNumber(); i++) {
            for (int j = 1; j <= sudoku.getMaxNumber(); j++) {
                if (Math.abs(i - j) == 1) {
                    consecutivePairs.add(new ValuePair(i, j));
                } else {
                    notConsecutivePairs.add(new ValuePair(i, j));
                }
            }
        }
        Set<Constraint> constraints = new HashSet<>();
        for (Cell cell : cells) {
            int row = cell.row();
            int col = cell.col();

            // vertical edge
            Cell cell2 = new Cell(row, col + 1);
            if (cells.contains(cell2)) {
                if (edges.contains(new Edge(row, col, true))) {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), consecutivePairs));
                } else {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), notConsecutivePairs));
                }
            }

            // horizontal edge
            cell2 = new Cell(row + 1, col);
            if (cells.contains(cell2)) {
                if (edges.contains(new Edge(row, col, false))) {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), consecutivePairs));
                } else {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), notConsecutivePairs));
                }
            }
        }
        return constraints;
    }
}
