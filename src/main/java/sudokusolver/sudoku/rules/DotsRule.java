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
 * Enforces that the values of cells connected by a white dot edge differ by 1,
 * the values of cells connected by a black dot edge differ by a factor of 2,
 * and the values of cells not connected by a white or black dot edge are not
 * consecutive and do not differ by a factor of 2.
 */
public class DotsRule extends SudokuRule {
    /** The set of cells to which the rule applies. */
    public Set<Cell> cells;
    /** Edges with white dots. */
    public Set<Edge> whiteDots;
    /** Edges with black dots. */
    public Set<Edge> blackDots;

    public DotsRule(Set<Edge> whiteDots, Set<Edge> blackDots, Set<Cell> cells) {
        this.cells = cells;
        this.whiteDots = whiteDots;
        this.blackDots = blackDots;
    }

    public DotsRule(List<Edge> whiteDots, List<Edge> blackDots, List<Cell> cells) {
        this.cells = new HashSet<>(cells);
        this.whiteDots = new HashSet<>(whiteDots);
        this.blackDots = new HashSet<>(blackDots);
    }

    public DotsRule(List<Edge> whiteDots, List<Edge> blackDots) {
        this.cells = null;
        this.whiteDots = new HashSet<>(whiteDots);
        this.blackDots = new HashSet<>(blackDots);
    }

    public Set<Constraint> apply(Sudoku sudoku) {
        Set<Cell> cells = this.cells != null ? this.cells : sudoku.getCells();
        Set<ValuePair> whitePairs = new HashSet<>();
        Set<ValuePair> blackPairs = new HashSet<>();
        Set<ValuePair> notWhiteNorBlack = new HashSet<>();

        if (sudoku.getMaxNumber() >= 2) {
            blackPairs.add(new ValuePair(1, 2));
            blackPairs.add(new ValuePair(2, 1));
        }
        for (int i = 1; i <= sudoku.getMaxNumber(); i++) {
            for (int j = 1; j <= sudoku.getMaxNumber(); j++) {
                if (Math.abs(i - j) == 1) {
                    whitePairs.add(new ValuePair(i, j));
                } else if (i == j * 2 || j == i * 2) {
                    blackPairs.add(new ValuePair(i, j));
                } else {
                    notWhiteNorBlack.add(new ValuePair(i, j));
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
                if (whiteDots.contains(new Edge(row, col, true))) {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), whitePairs));
                } else if (blackDots.contains(new Edge(row, col, true))) {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), blackPairs));
                } else {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), notWhiteNorBlack));
                }
            }

            // horizontal edge
            cell2 = new Cell(row + 1, col);
            if (cells.contains(cell2)) {
                if (whiteDots.contains(new Edge(row, col, false))) {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), whitePairs));
                } else if (blackDots.contains(new Edge(row, col, false))) {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), blackPairs));
                } else {
                    constraints.add(new AllowedPairsConstraint(cell.toString(), cell2.toString(), notWhiteNorBlack));
                }
            }
        }
        return constraints;
    }
}
