package sudokusolver.sudoku;

/**
 * Represents a grid edge positioned between adjacent cells on the Sudoku board.
 * <p>
 * <b>Coordinate Anchoring:</b>
 * An edge with coordinates {@code (row, col)} is anchored to the cell at
 * {@code Cell(row, col)}.
 * </p>
 * <ul>
 * <li>If {@code isVertical} is {@code true}, the edge runs vertically and
 * represents the <b>right boundary</b> of {@code Cell(row, col)}.</li>
 * <li>If {@code isVertical} is {@code false}, the edge runs horizontally and
 * represents the <b>bottom boundary</b> of {@code Cell(row, col)}.</li>
 * </ul>
 *
 * @param row        the row index of the anchor cell
 * @param col        the column index of the anchor cell
 * @param isVertical {@code true} for a vertical edge (right border),
 *                   {@code false} for a horizontal edge (bottom border)
 */
public record Edge(int row, int col, boolean isVertical) {
    /**
     * Returns a string representation of the edge, indicating its position and
     * orientation.
     * 
     * @return a formatted string in the format {@code Edge[row, col, orientation]},
     *         where {@code orientation} is "V" for vertical edges and "H" for
     *         horizontal edges.
     */
    @Override
    public String toString() {
        String orientation = isVertical ? "V" : "H";
        return String.format("Edge[%d, %d, %s]", row, col, orientation);
    }
}
