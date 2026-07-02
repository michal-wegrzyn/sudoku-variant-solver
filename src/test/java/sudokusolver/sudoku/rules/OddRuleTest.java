package sudokusolver.sudoku.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import sudokusolver.csp.constraints.AllowedValuesConstraint;
import sudokusolver.sudoku.Board;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.RegionSelector;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;

import java.util.List;
import java.util.Set;

public class OddRuleTest {
    @Test
    @DisplayName("Test applying odd rule to sudokus with different max numbers")
    public void testApplyOddRuleWithDifferentMaxNumbers() {
        String sudokuText = """
                ....
                .OO.
                .OO.
                ....
                """;
        RegionSelector regionSelector = new RegionSelector(sudokuText);
        Sudoku sudoku = new Sudoku(regionSelector.toBoard(), 4);
        List<Cell> oddCells = regionSelector.getCellRegion("O");
        SudokuRule oddRule = new OddRule(oddCells);
        // Test with maxNumber = 4
        Set<Constraint> constraints = oddRule.apply(sudoku);
        assertThat(constraints).hasSize(1);
        assertThat(constraints.iterator().next()).isInstanceOf(AllowedValuesConstraint.class);
        AllowedValuesConstraint constraint = (AllowedValuesConstraint) constraints.iterator().next();
        assertThat(constraint.getAllowedValues()).containsExactlyInAnyOrder(1, 3);
        assertThat(constraint.getVariables()).hasSize(4);
        // Test with maxNumber = 6
        sudoku.setMaxNumber(6);
        constraints = oddRule.apply(sudoku);
        assertThat(constraints).hasSize(1);
        assertThat(constraints.iterator().next()).isInstanceOf(AllowedValuesConstraint.class);
        constraint = (AllowedValuesConstraint) constraints.iterator().next();
        assertThat(constraint.getAllowedValues()).containsExactlyInAnyOrder(1, 3, 5);
        assertThat(constraint.getVariables()).hasSize(4);
        // Test with maxNumber = 9
        sudoku.setMaxNumber(9);
        constraints = oddRule.apply(sudoku);
        assertThat(constraints).hasSize(1);
        assertThat(constraints.iterator().next()).isInstanceOf(AllowedValuesConstraint.class);
        constraint = (AllowedValuesConstraint) constraints.iterator().next();
        assertThat(constraint.getAllowedValues()).containsExactlyInAnyOrder(1, 3, 5, 7, 9);
        assertThat(constraint.getVariables()).hasSize(4);
    }

    @Test
    @DisplayName("Test applying odd rules to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyOddRules() {
        String sudokuText = """
                ....25..7
                9.3....6.
                1...4...2
                6.....5..
                .O.17O6..
                .42......
                43O..7...
                5..3.....
                ....O.1..
                """;
        RegionSelector regionSelector = new RegionSelector(sudokuText);
        Sudoku sudoku = new Sudoku(regionSelector.toBoard());
        List<Cell> oddCells = regionSelector.getCellRegion("O");
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new OddRule(oddCells));
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                864925317
                923781465
                175643892
                619234578
                358179624
                742568931
                431897256
                586312749
                297456183
                """);
    }
}
