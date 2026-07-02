package sudokusolver.sudoku;

/**
 * Represents a single cell coordinate on the Sudoku board defined by its row
 * and column.
 * <p>
 * Supports string-based serialization and deserialization.
 * </p>
 *
 * @param row row index of the cell
 * @param col column index of the cell
 */
public record Cell(int row, int col) {
    /**
     * Constructs a Cell by parsing its string representation.
     *
     * @param s the string to parse, must follow the exact format
     *          {@code Cell[row, col]}
     * @throws IllegalArgumentException if the string format is malformed or indices
     *                                  are not integers
     */
    public Cell(String s) {
        this(parseString(s)[0], parseString(s)[1]);
    }

    /**
     * Helper method to parse the internal components of a cell string
     * representation.
     *
     * @param s the string to parse
     * @return a primitive array where index 0 is the row and index 1 is the column
     * @throws IllegalArgumentException if validation or integer parsing fails
     */
    private static int[] parseString(String s) {
        if (!s.startsWith("Cell[") || !s.endsWith("]")) {
            throw new IllegalArgumentException("Invalid cell string: " + s);
        }
        String[] parts = s.substring(5, s.length() - 1).split(", ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid cell string: " + s);
        }
        try {
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            return new int[] { row, col };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cell string: " + s, e);
        }
    }

    /**
     * Returns the string representation of this cell instance.
     *
     * @return a formatted string in the format {@code Cell[row, col]}
     */
    @Override
    public String toString() {
        return "Cell[" + row + ", " + col + "]";
    }

    /**
     * Static utility to generate a standardized cell string identifier without
     * instantiating a {@code Cell} object.
     *
     * @param row the row index
     * @param col the column index
     * @return a formatted string in the format {@code Cell[row, col]}
     */
    public static String toString(int row, int col) {
        return "Cell[" + row + ", " + col + "]";
    }
}