package sudokusolver.sudoku;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import sudokusolver.csp.Constraint;
import sudokusolver.csp.CSPProblem;
import sudokusolver.csp.CSPSolver;
import sudokusolver.csp.DomainStore;

/**
 * Represents a complete Sudoku puzzle instance, serving as the main entry point
 * and orchestrator for puzzle configuration and solving.
 * <p>
 * This class acts as a Facade over the underlying {@link Board} and registered
 * {@link SudokuRule}s.
 * It is responsible for bridging the puzzle's domain data with the low-level
 * {@link CSPSolver} by compiling the grid state and custom rules into a
 * {@link CSPProblem}.
 * </p>
 */
public class Sudoku {
    /**
     * The underlying state representing the cells and values on the puzzle grid.
     */
    private final Board board;

    /**
     * The maximum number allowed on this board size.
     * <p>
     * <b>Note:</b> The minimum number allowed is always 1, so the valid range of
     * values for cells is from 1 to {@code maxNumber} inclusive.
     * </p>
     */
    private int maxNumber;

    /**
     * The active list of rules applied to this puzzle instance.
     */
    public final List<SudokuRule> rules;

    /**
     * Constructs a Sudoku instance with given board, max allowed value, and rules.
     *
     * @param board     the initialized grid model
     * @param maxNumber the upper number limit for valid values
     * @param rules     the initial list of rules to register
     * @throws IllegalArgumentException if the board contains any assigned value
     *                                  greater than maxNumber
     */
    public Sudoku(Board board, int maxNumber, List<SudokuRule> rules) {
        if (board.maxAssignedValue() > maxNumber) {
            throw new IllegalArgumentException("Board contains assigned value greater than maxNumber");
        }
        this.board = board.copy();
        this.maxNumber = maxNumber;
        this.rules = rules;
    }

    /**
     * Constructs a Sudoku instance with an empty rules list.
     *
     * @param board     the initialized grid model
     * @param maxNumber the upper number limit for valid values
     * @throws IllegalArgumentException if the board contains any assigned value
     *                                  greater than maxNumber
     */
    public Sudoku(Board board, int maxNumber) {
        if (board.maxAssignedValue() > maxNumber) {
            throw new IllegalArgumentException("Board contains assigned value greater than maxNumber");
        }
        this.board = board.copy();
        this.maxNumber = maxNumber;
        this.rules = new ArrayList<>();
    }

    /**
     * Constructs a Sudoku instance by parsing an ASCII-art string.
     *
     * @param text      the String representation of the puzzle grid
     * @param maxNumber the upper number limit for valid values
     * @param rules     the initial list of variation rules to register
     * @throws IllegalArgumentException if the board contains any assigned value
     *                                  greater than maxNumber
     */
    public Sudoku(String text, int maxNumber, List<SudokuRule> rules) {
        this.board = new Board(text);
        if (this.board.maxAssignedValue() > maxNumber) {
            throw new IllegalArgumentException("Board contains assigned value greater than maxNumber");
        }
        this.maxNumber = maxNumber;
        this.rules = rules;
    }

    /**
     * Constructs a Sudoku instance from an ASCII-art string with an empty rules
     * list.
     *
     * @param text      the String representation of the puzzle grid
     * @param maxNumber the upper number limit for valid values
     * @throws IllegalArgumentException if the board contains any assigned value
     *                                  greater than maxNumber
     */
    public Sudoku(String text, int maxNumber) {
        this.board = new Board(text);
        if (this.board.maxAssignedValue() > maxNumber) {
            throw new IllegalArgumentException("Board contains assigned value greater than maxNumber");
        }
        this.maxNumber = maxNumber;
        this.rules = new ArrayList<>();
    }

    /**
     * Constructs a Sudoku instance with upper number limit set to 9 and an empty
     * rules list.
     *
     * @param board the initialized grid model
     * @throws IllegalArgumentException if the board contains any assigned value
     *                                  greater than 9
     */
    public Sudoku(Board board) {
        if (board.maxAssignedValue() > 9) {
            throw new IllegalArgumentException("Board contains assigned value greater than maxNumber");
        }
        this.board = board.copy();
        this.maxNumber = 9;
        this.rules = new ArrayList<>();
    }

    /**
     * Constructs a Sudoku instance from an ASCII-art string with an empty rules
     * list and upper number limit set to 9.
     *
     * @param text the String representation of the puzzle grid
     * @throws IllegalArgumentException if the board contains any assigned value
     *                                  greater than 9
     */
    public Sudoku(String text) {
        this.board = new Board(text);
        if (this.board.maxAssignedValue() > 9) {
            throw new IllegalArgumentException("Board contains assigned value greater than maxNumber");
        }
        this.maxNumber = 9;
        this.rules = new ArrayList<>();
    }

    /**
     * Returns the upper number limit for valid values in the puzzle.
     *
     * @return the maximum number allowed in the puzzle
     */
    public int getMaxNumber() {
        return maxNumber;
    }

    /**
     * Sets the upper number limit for valid values in the puzzle. If any currently
     * assigned cell value exceeds the new maximum, an exception is thrown and the
     * change is not applied.
     * 
     * @param maxNumber the new maximum number allowed in the puzzle
     * @throws IllegalArgumentException if any assigned cell value exceeds the new
     *                                  maximum
     */
    public void setMaxNumber(int maxNumber) {
        if (board.maxAssignedValue() > maxNumber) {
            throw new IllegalArgumentException("Board contains assigned value greater than maxNumber");
        }
        this.maxNumber = maxNumber;
    }

    /**
     * Registers a new rule to apply.
     *
     * @param rule the rule to add to the puzzle's active rule list.
     */
    public void addRule(SudokuRule rule) {
        this.rules.add(rule);
    }

    /**
     * Registers multiple new rules to apply.
     * 
     * @param rules the list of rules to add to the puzzle's active rule list.
     */
    public void addRules(List<SudokuRule> rules) {
        this.rules.addAll(rules);
    }

    /**
     * Returns the string representation of the underlying board grid.
     *
     * @return a formatted text representation of the current board state
     */
    @Override
    public String toString() {
        return board.toString();
    }

    /**
     * Number assigned to given cell, 0 if the cell is unassigned, or -1 if the cell
     * is not involved in the puzzle.
     *
     * @param row the target zero-based row index
     * @param col the target zero-based column index
     * @return the current cell value, or 0 if unassigned
     */
    public int getCellValue(int row, int col) {
        return board.getCellValue(row, col);
    }

    /**
     * Number assigned to given cell, 0 if the cell is unassigned, or -1 if the cell
     * is not involved in the puzzle.
     *
     * @param cell the target cell
     * @return the current cell value
     */
    public int getCellValue(Cell cell) {
        return board.getCellValue(cell);
    }

    /**
     * Returns a set of all cells involved in the puzzle.
     * 
     * @return a set of all cells involved in the puzzle
     */
    public Set<Cell> getCells() {
        return board.getCells();
    }

    /**
     * Sets the value of the given cell. A negative value indicates that the cell is
     * not involved in the puzzle and will be removed from the grid.
     * 
     * @param cell  the target cell
     * @param value the value to set, where 0 represents an empty cell and negative
     *              values represent cells not involved in the puzzle
     * @throws IllegalArgumentException if the value exceeds the current maxNumber
     */
    void setCellValue(Cell cell, int value) {
        if (value > maxNumber) {
            throw new IllegalArgumentException("Value " + value + " exceeds maxNumber " + maxNumber);
        }
        board.setCellValue(cell, value);
    }

    /**
     * Sets the value of the cell at the specified row and column. A negative value
     * indicates that the cell is not involved in the puzzle and will be removed
     * from the grid.
     * 
     * @param row   the row of the cell
     * @param col   the column of the cell
     * @param value the value to set, where 0 represents an empty cell and negative
     *              values represent cells not involved in the puzzle
     * @throws IllegalArgumentException if the value exceeds the current maxNumber
     */
    void setCellValue(int row, int col, int value) {
        if (value > maxNumber) {
            throw new IllegalArgumentException("Value " + value + " exceeds maxNumber " + maxNumber);
        }
        board.setCellValue(row, col, value);
    }

    /**
     * Compiles the Sudoku object into a {@link CSPProblem}.
     * <p>
     * <b>Compilation Logic:</b>
     * </p>
     * <ol>
     * <li>Create domains for each cell based on the maximum number allowed and
     * whether they are assigned.</li>
     * <li>Maps cells to variables via {@link Cell#toString()}.</li>
     * <li>Iterates through all registered {@link SudokuRule} instances,
     * accumulating {@link Constraint}s generated by their
     * {@link SudokuRule#apply(Sudoku)} method.</li>
     * </ol>
     *
     * @return a fully populated, immutable {@link CSPProblem} ready to be processed
     *         by a solver engine
     */
    public CSPProblem buildCSPProblem() {
        Set<Integer> domain = new HashSet<>();
        List<Set<Integer>> assignedDomain = new ArrayList<>();
        for (int i = 1; i <= maxNumber; i++) {
            domain.add(i);
            HashSet<Integer> assignedValue = new HashSet<>();
            assignedValue.add(i);
            assignedDomain.add(assignedValue);
        }
        Map<String, Set<Integer>> domains = new HashMap<>();
        for (Cell cell : getCells()) {
            String variable = cell.toString();
            int value = getCellValue(cell);
            if (value > maxNumber) {
                throw new IllegalArgumentException(
                        variable + " has value " + value + " which exceeds maxNumber " + maxNumber);
            }
            if (value > 0) {
                domains.put(variable, assignedDomain.get(value - 1));
            } else {
                domains.put(variable, domain);
            }
        }
        DomainStore domainStore = new DomainStore(domains);
        List<Constraint> constraints = new ArrayList<>();
        for (SudokuRule rule : this.rules) {
            constraints.addAll(rule.apply(this));
        }
        return new CSPProblem(domainStore, constraints);
    }

    /**
     * Finds solutions to this Sudoku puzzle by compiling it into a
     * {@link CSPProblem}, invoking the {@link CSPSolver} and translating the
     * resulting variable assignments back into {@link Board} objects.
     *
     * @param maxSolutions the maximum number of solutions to look for
     * @return a list containing {@link Board}s with all cells assigned.
     */
    public List<Board> solve(int maxSolutions) {
        CSPProblem problem = buildCSPProblem();
        List<Map<String, Integer>> solutions = CSPSolver.solve(problem, maxSolutions);
        List<Board> solvedBoards = new ArrayList<>();
        for (Map<String, Integer> solution : solutions) {
            solvedBoards.add(Board.fromStringMap(solution));
        }
        return solvedBoards;
    }

    /**
     * Finds solutions (max 2) to this Sudoku puzzle by compiling it into a
     * {@link CSPProblem}, invoking the {@link CSPSolver} and translating the
     * resulting variable assignments back into {@link Board} objects.
     *
     * @return a list containing {@link Board}s with all cells assigned.
     */
    public List<Board> solve() {
        CSPProblem problem = buildCSPProblem();
        List<Map<String, Integer>> solutions = CSPSolver.solve(problem, 2);
        List<Board> solvedBoards = new ArrayList<>();
        for (Map<String, Integer> solution : solutions) {
            solvedBoards.add(Board.fromStringMap(solution));
        }
        return solvedBoards;
    }
}
