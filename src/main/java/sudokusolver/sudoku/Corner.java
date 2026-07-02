package sudokusolver.sudoku;

/**
 * Represents a precise grid intersection (vertex) where four adjacent cells
 * meet.
 * <p>
 * <b>Coordinate Anchoring:</b>
 * A corner with coordinates {@code (row, col)} represents the <b>bottom-right
 * vertex</b> of {@code Cell(row, col)}.
 * </p>
 *
 * @param row the row index of the anchor cell
 * @param col the column index of the anchor cell
 */
public record Corner(int row, int col) {
    /**
     * Returns a string representation of the corner, indicating its position.
     * 
     * @return a formatted string in the format {@code Corner[row, col]}
     */
    @Override
    public String toString() {
        return String.format("Corner[%d, %d]", row, col);
    }
}
