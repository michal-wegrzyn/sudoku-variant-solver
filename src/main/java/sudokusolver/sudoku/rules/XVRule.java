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
import sudokusolver.csp.constraints.SumConstraint;
import sudokusolver.csp.constraints.AllowedPairsConstraint;

/**
 * Enforces that the values of cells connected by a V edge sum to 5,
 * the values of cells connected by a X edge sum to 10, and the values of cells
 * not connected by a V or X edge are don't sum to 5 or 10.
 */
public class XVRule extends SudokuRule {
    /** The set of cells to which the rule applies. */
    public Set<Cell> cells;
    /** The set of V edges (connecting cells that must sum to 5). */
    public Set<Edge> vEdges;
    /** The set of X edges (connecting cells that must sum to 10). */
    public Set<Edge> xEdges;

    public XVRule(Set<Edge> vEdges, Set<Edge> xEdges, Set<Cell> cells) {
        this.cells = cells;
        this.vEdges = vEdges;
        this.xEdges = xEdges;
    }

    public XVRule(List<Edge> vEdges, List<Edge> xEdges, List<Cell> cells) {
        this.cells = new HashSet<>(cells);
        this.vEdges = new HashSet<>(vEdges);
        this.xEdges = new HashSet<>(xEdges);
    }

    public XVRule(List<Edge> vEdges, List<Edge> xEdges) {
        this.cells = null;
        this.vEdges = new HashSet<>(vEdges);
        this.xEdges = new HashSet<>(xEdges);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Cell> cells = this.cells != null ? this.cells : sudoku.getCells();
        Set<ValuePair> not5nor10 = new HashSet<>();
        for (int i = 1; i <= sudoku.getMaxNumber(); i++) {
            for (int j = 1; j <= sudoku.getMaxNumber(); j++) {
                if ((i + j) != 5 && (i + j) != 10) {
                    not5nor10.add(new ValuePair(i, j));
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
                if (vEdges.contains(new Edge(row, col, true))) {
                    constraints.add(new SumConstraint(List.of(cell.toString(), cell2.toString()), 5));
                } else if (xEdges.contains(new Edge(row, col, true))) {
                    constraints.add(new SumConstraint(List.of(cell.toString(), cell2.toString()), 10));
                } else {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), not5nor10));
                }
            }

            // horizontal edge
            cell2 = new Cell(row + 1, col);
            if (cells.contains(cell2)) {
                if (vEdges.contains(new Edge(row, col, false))) {
                    constraints.add(new SumConstraint(List.of(cell.toString(), cell2.toString()), 5));
                } else if (xEdges.contains(new Edge(row, col, false))) {
                    constraints.add(new SumConstraint(List.of(cell.toString(), cell2.toString()), 10));
                } else {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), not5nor10));
                }
            }
        }
        return constraints;
    }
}
