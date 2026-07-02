package sudokusolver.sudoku;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A parser designed to map ASCII-art grid representations into
 * lists of cells, edges, and corners grouped by character identifiers.
 * <p>
 * This class allows developers to visually layout complex, non-standard Sudoku
 * regions or geometric
 * constraints using simple text blocks.
 * </p>
 * <p>
 * <b>Cells-only mode</b>
 * Treats every single character as a {@link Cell}. Newlines advance the row
 * counter.
 * </p>
 * 
 * <pre>{@code
 * String extraRegionsText = """
 *         ......
 *         .AA...
 *         .AA.BB
 *         .AA.BB
 *         ....BB
 *         ......
 *         """;
 * RegionSelector selector = new RegionSelector(extraRegionsText);
 * List<Cell> regionA = selector.getCellRegion('A');
 * sudoku.addRule(new AllDifferentRule(regionA));
 * }</pre>
 * 
 * <p>
 * <b>Cells+Edges+Corners mode</b>
 * The parity (odd/even) of the current character
 * coordinates determines the element type::
 * </p>
 * <ul>
 * <li>{@code (even, even)} &rarr; Maps character to a {@link Cell}.</li>
 * <li>{@code (even, odd)} &rarr; Maps character to a vertical {@link Edge}
 * (right boundary).</li>
 * <li>{@code (odd, even)} &rarr; Maps character to a horizontal {@link Edge}
 * (bottom boundary).</li>
 * <li>{@code (odd, odd)} &rarr; Maps character to a {@link Corner}
 * (bottom-right vertex).</li>
 * </ul>
 * 
 * <pre>{@code
 * String sudokuText = """
 *         1|.V.|.|.|.
 *         -+-+-+-+-+V
 *         .|.X.|.V.|.
 *         -+-+-O-+-+-
 *         .|.V3|.|.|5
 *         X+-+-+V+-+-
 *         E|.|.|.|.V.
 *         -+-O-+-+-+V
 *         .|E|.|.|.|.
 *         V+-+V+-+V+-
 *         .|5|.|3|1|.
 *         """;
 * RegionSelector selector = new RegionSelector(sudokuText, 0, 0, true);
 * Sudoku sudoku = new Sudoku(selector.toBoard(), 6);
 * List<Edge> vEdges = selector.getEdgeRegion('V');
 * List<Edge> xEdges = selector.getEdgeRegion('X');
 * sudoku.addRule(new XVRule(vEdges, xEdges));
 * List<Cell> evenCells = selector.getCellRegion('E');
 * sudoku.addRule(new EvenRule(evenCells));
 * List<Corner> oCorners = selector.getCornerRegion('O');
 * }</pre>
 * 
 * <p>
 * <b>Note:</b> In examples '.', '-', '|' and '+' are used for visual clarity
 * but are treated the same way as any other character.
 * </p>
 */
public class RegionSelector {
    /**
     * Maps each character identifier to its parsed list of board cells.
     */
    private Map<Character, List<Cell>> cellRegions;
    /**
     * Maps each character identifier to its parsed list of grid edges.
     */
    private Map<Character, List<Edge>> edgeRegions;
    /**
     * Maps each character identifier to its parsed list of grid corners/vertices.
     */
    private Map<Character, List<Corner>> cornerRegions;

    /**
     * Constructs a RegionSelector by parsing the provided ASCII-art string.
     *
     * @param s                      ASCII-art string
     * @param topRow                 the starting row offset for the layout
     * @param leftCol                the starting column offset for the layout
     * @param includeEdgesAndCorners {@code true} to enable parsing for cells,
     *                               edges, and corners; {@code false} to parse in
     *                               cells-only mode
     */
    public RegionSelector(String s, int topRow, int leftCol, boolean includeEdgesAndCorners) {
        init(s, topRow, leftCol, includeEdgesAndCorners);
    }

    /**
     * Constructs a RegionSelector by parsing the provided ASCII-art string.
     *
     * @param s                      ASCII-art string
     * @param topLeft                the top-left corner of the layout
     * @param includeEdgesAndCorners {@code true} to enable parsing for cells,
     *                               edges, and corners; {@code false} to parse in
     *                               cells-only mode
     */
    public RegionSelector(String s, Cell topLeft, boolean includeEdgesAndCorners) {
        init(s, topLeft.row(), topLeft.col(), includeEdgesAndCorners);
    }

    /**
     * Constructs a RegionSelector by parsing the provided ASCII-art string in
     * cells-only mode.
     * 
     * @param s       ASCII-art string
     * @param topRow  the starting row offset for the layout
     * @param leftCol the starting column offset for the layout
     */
    public RegionSelector(String s, int topRow, int leftCol) {
        init(s, topRow, leftCol, false);
    }

    /**
     * Constructs a RegionSelector by parsing the provided ASCII-art string in
     * cells-only mode.
     * 
     * @param s       ASCII-art string
     * @param topLeft the top-left corner of the layout
     */
    public RegionSelector(String s, Cell topLeft) {
        init(s, topLeft.row(), topLeft.col(), false);
    }

    /**
     * Constructs a RegionSelector by parsing the provided ASCII-art string with
     * top-left corner as the origin (0,0) in cells-only mode.
     * 
     * @param s ASCII-art string
     */
    public RegionSelector(String s) {
        init(s, 0, 0, false);
    }

    /**
     * Constructs a RegionSelector by parsing the provided ASCII-art string with
     * top-left corner as the origin (0,0).
     * 
     * @param s                      ASCII-art string
     * @param includeEdgesAndCorners {@code true} to enable parsing for cells,
     *                               edges, and corners; {@code false} to parse in
     *                               cells-only mode
     */
    public RegionSelector(String s, boolean includeEdgesAndCorners) {
        init(s, 0, 0, includeEdgesAndCorners);
    }

    /**
     * Initializes the RegionSelector by parsing the provided ASCII-art string
     * according to the specified parameters.
     * 
     * @param s                      ASCII-art string
     * @param topRow                 the starting row offset for the layout
     * @param leftCol                the starting column offset for the layout
     * @param includeEdgesAndCorners {@code true} to enable parsing for cells,
     *                               edges, and corners; {@code false} to parse in
     *                               cells-only mode
     */
    private void init(String s, int topRow, int leftCol, boolean includeEdgesAndCorners) {
        this.cellRegions = new HashMap<>();
        this.edgeRegions = new HashMap<>();
        this.cornerRegions = new HashMap<>();
        if (!includeEdgesAndCorners) {
            int currRow = topRow;
            int currCol = leftCol;
            for (char c : s.toCharArray()) {
                if (c == '\n') {
                    if (currRow != topRow || currCol != leftCol) {
                        currRow++;
                        currCol = leftCol;
                    }
                } else {
                    cellRegions.computeIfAbsent(c, k -> new ArrayList<>()).add(new Cell(currRow, currCol));
                    currCol++;
                }
            }
        } else {
            int currRow = topRow * 2;
            int currCol = leftCol * 2;
            for (char c : s.toCharArray()) {
                if (c == '\n') {
                    if (currRow != topRow * 2 || currCol != leftCol * 2) {
                        currRow++;
                        currCol = leftCol * 2;
                    }
                } else {
                    if (currCol % 2 == 0 && currRow % 2 == 0) {
                        cellRegions.computeIfAbsent(c, k -> new ArrayList<>())
                                .add(new Cell(currRow / 2, currCol / 2));
                    }
                    if (currCol % 2 == 1 && currRow % 2 == 0) {
                        edgeRegions.computeIfAbsent(c, k -> new ArrayList<>())
                                .add(new Edge(currRow / 2, currCol / 2, true));
                    }
                    if (currCol % 2 == 0 && currRow % 2 == 1) {
                        edgeRegions.computeIfAbsent(c, k -> new ArrayList<>())
                                .add(new Edge(currRow / 2, currCol / 2, false));
                    }
                    if (currCol % 2 == 1 && currRow % 2 == 1) {
                        cornerRegions.computeIfAbsent(c, k -> new ArrayList<>())
                                .add(new Corner(currRow / 2, currCol / 2));
                    }
                    currCol++;
                }
            }
        }
    }

    /**
     * Returns the list of cells in the region represented by the character c. If
     * the character is not found, returns an empty list.
     * 
     * @param c the character representing the region
     * @return the list of cells in the region
     */
    public List<Cell> getCellRegion(char c) {
        return cellRegions.getOrDefault(c, new ArrayList<>());
    }

    /**
     * Returns the list of cells in the regions represented by the characters in the
     * string s. If a character is not found, it is ignored. The resulting list is
     * the concatenation of the lists for each character in s in the order they
     * appear in s.
     * 
     * @param s the string representing the regions
     * @return the list of cells in the regions
     */
    public List<Cell> getCellRegion(String s) {
        List<Cell> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            result.addAll(getCellRegion(c));
        }
        return result;
    }

    /**
     * Returns the list of edges in the region represented by the character c. If
     * the character is not found, returns an empty list.
     * 
     * @param c the character representing the region
     * @return the list of edges in the region
     */
    public List<Edge> getEdgeRegion(char c) {
        return edgeRegions.getOrDefault(c, new ArrayList<>());
    }

    /**
     * Returns the list of edges in the regions represented by the characters in the
     * string s. If a character is not found, it is ignored. The resulting list is
     * the concatenation of the lists for each character in s in the order they
     * appear in s.
     * 
     * @param s the string representing the regions
     * @return the list of edges in the regions
     */
    public List<Edge> getEdgeRegion(String s) {
        List<Edge> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            result.addAll(getEdgeRegion(c));
        }
        return result;
    }

    /**
     * Returns the list of corners in the region represented by the character c. If
     * the character is not found, returns an empty list.
     * 
     * @param c the character representing the region
     * @return the list of corners in the region
     */
    public List<Corner> getCornerRegion(char c) {
        return cornerRegions.getOrDefault(c, new ArrayList<>());
    }

    /**
     * Returns the list of corners in the regions represented by the characters in
     * the
     * string s. If a character is not found, it is ignored. The resulting list is
     * the concatenation of the lists for each character in s in the order they
     * appear in s.
     * 
     * @param s the string representing the regions
     * @return the list of corners in the regions
     */
    public List<Corner> getCornerRegion(String s) {
        List<Corner> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            result.addAll(getCornerRegion(c));
        }
        return result;
    }

    /**
     * Uses '1'-'9' regions to create a Board where cells in '1' are set to 1, cells
     * in '2' are set to 2, etc. Cells in other regions are involved in the puzzle
     * with no assigned value.
     * 
     * @return a Board representing the regions defined by '1'-'9' characters
     */
    public Board toBoard() {
        return toBoard(9);
    }

    /**
     * Uses regions represeting numbers up to maxNumber to create a Board.
     * Uses '1'-'9', 'A'-'Z', and 'a'-'z' regions to create a Board where cells in
     * '1' are set to 1, cells in '2' are set to 2, ..., cells in '9' are set to 9,
     * cells in 'A' are set to 10, ..., cells in 'Z' are set to 35, cells in 'a' are
     * set to 10, ..., cells in 'z' are set to 35. Cells in other regions are
     * involved in the puzzle with no assigned value.
     * 
     * <p>
     * <i>Example:</i> If maxNumber is 16, the method will consider characters
     * '1'-'9', 'A'-'G', 'a'-'g' to represent numbers 1-16.
     * <b>Note:</b> If maxNumber is greater than 35, the method will only consider
     * characters up to 'z' (representing 35).
     * </p>
     * 
     * @param maxNumber the maximum number to be represented by regions
     * @return a board representing the regions defined by '1'-'9', 'A'-'Z', and
     *         'a'-'z' characters
     */
    public Board toBoard(int maxNumber) {
        Board board = new Board();
        for (Map.Entry<Character, List<Cell>> entry : cellRegions.entrySet()) {
            if (entry.getKey() != ' ') {
                for (Cell cell : entry.getValue()) {
                    board.setCellValue(cell.row(), cell.col(), 0);
                }
            }
        }
        for (int i = 0; i <= maxNumber; i++) {
            if (i <= 9) {
                char c = (char) ('0' + i);
                for (Cell cell : getCellRegion(c)) {
                    board.setCellValue(cell.row(), cell.col(), i);
                }
            } else {
                char c = (char) ('A' + i - 10);
                for (Cell cell : getCellRegion(c)) {
                    board.setCellValue(cell.row(), cell.col(), i);
                }
                c = (char) ('a' + i - 10);
                for (Cell cell : getCellRegion(c)) {
                    board.setCellValue(cell.row(), cell.col(), i);
                }
            }
        }
        return board;
    }

    /**
     * Returns the list of cells in the rectangle defined by the top-left corner
     * (topRow, leftCol) and the bottom-right corner (bottomRow, rightCol).
     * 
     * @param topRow    the top row of the rectangle
     * @param leftCol   the left column of the rectangle
     * @param bottomRow the bottom row of the rectangle
     * @param rightCol  the right column of the rectangle
     * @return the list of cells in the defined rectangle
     */
    public static List<Cell> rectangle(int topRow, int leftCol, int bottomRow, int rightCol) {
        List<Cell> result = new ArrayList<>();
        for (int r = topRow; r <= bottomRow; r++) {
            for (int c = leftCol; c <= rightCol; c++) {
                result.add(new Cell(r, c));
            }
        }
        return result;
    }

    /**
     * Returns the list of cells in the rectangle defined by the top-left corner and
     * the bottom-right corner.
     * 
     * @param topLeft     the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle
     * @return the list of cells in the defined rectangle
     */
    public static List<Cell> rectangle(Cell topLeft, Cell bottomRight) {
        return rectangle(topLeft.row(), topLeft.col(), bottomRight.row(), bottomRight.col());
    }

    /**
     * Returns the list of cells in the sequence defined by the starting cell
     * (startRow, startCol) and the direction (dr, dc) for the given length. For
     * <p>
     * <i>Example:</i> If startRow=0, startCol=0, dr=1, dc=1, and length=9 the
     * method will return the list of cells in the main diagonal of a 9x9 board:
     * (0,0), (1,1), ..., (8,8).
     * </p>
     * 
     * @param startRow the row of the starting cell
     * @param startCol the column of the starting cell
     * @param dr       the row direction
     * @param dc       the column direction
     * @param length   the length of the sequence
     * @return the list of cells in the defined sequence
     */
    public static List<Cell> sequence(int startRow, int startCol, int dr, int dc, int length) {
        List<Cell> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(new Cell(startRow + dr * i, startCol + dc * i));
        }
        return result;
    }

    /**
     * Returns the list of cells in the sequence defined by the starting cell
     * (start) and the direction (dr, dc) for the given length.
     * 
     * <p>
     * <i>Example:</i> If start=(0,0), dr=1, dc=1, and length=9 the
     * method will return the list of cells in the main diagonal of a 9x9 board:
     * (0,0), (1,1), ..., (8,8).
     * </p>
     * 
     * @param start  the starting cell
     * @param dr     the row direction
     * @param dc     the column direction
     * @param length the length of the sequence
     * @return the list of cells in the defined sequence
     */
    public static List<Cell> sequence(Cell start, int dr, int dc, int length) {
        return sequence(start.row(), start.col(), dr, dc, length);
    }
}
