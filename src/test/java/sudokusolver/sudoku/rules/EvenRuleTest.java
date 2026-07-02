package sudokusolver.sudoku.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import sudokusolver.sudoku.Board;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.RegionSelector;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.SudokuRule;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllowedValuesConstraint;
import java.util.List;
import java.util.Set;

public class EvenRuleTest {
    @Test
    @DisplayName("Test applying even rule to sudokus with different max numbers")
    public void testApplyEvenRuleWithDifferentMaxNumbers() {
        String sudokuText = """
                ....
                .EE.
                .EE.
                ....
                """;
        RegionSelector regionSelector = new RegionSelector(sudokuText);
        Sudoku sudoku = new Sudoku(regionSelector.toBoard(), 4);
        List<Cell> evenCells = regionSelector.getCellRegion("E");
        SudokuRule evenRule = new EvenRule(evenCells);
        // Test with maxNumber = 4
        Set<Constraint> constraints = evenRule.apply(sudoku);
        assertThat(constraints).hasSize(1);
        assertThat(constraints.iterator().next()).isInstanceOf(AllowedValuesConstraint.class);
        AllowedValuesConstraint constraint = (AllowedValuesConstraint) constraints.iterator().next();
        assertThat(constraint.getAllowedValues()).containsExactlyInAnyOrder(2, 4);
        assertThat(constraint.getVariables()).hasSize(4);
        // Test with maxNumber = 6
        sudoku.setMaxNumber(6);
        constraints = evenRule.apply(sudoku);
        assertThat(constraints).hasSize(1);
        assertThat(constraints.iterator().next()).isInstanceOf(AllowedValuesConstraint.class);
        constraint = (AllowedValuesConstraint) constraints.iterator().next();
        assertThat(constraint.getAllowedValues()).containsExactlyInAnyOrder(2, 4, 6);
        assertThat(constraint.getVariables()).hasSize(4);
        // Test with maxNumber = 9
        sudoku.setMaxNumber(9);
        constraints = evenRule.apply(sudoku);
        assertThat(constraints).hasSize(1);
        assertThat(constraints.iterator().next()).isInstanceOf(AllowedValuesConstraint.class);
        constraint = (AllowedValuesConstraint) constraints.iterator().next();
        assertThat(constraint.getAllowedValues()).containsExactlyInAnyOrder(2, 4, 6, 8);
        assertThat(constraint.getVariables()).hasSize(4);
    }

    @Test
    @DisplayName("Test applying even rule to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyEvenRules() {
        String sudokuText = """
                .5.43....
                ..4.....6
                8.....7..
                ..5..617.
                ...81....
                2........
                .E6E...E.
                ......8.9
                .3.6.5...
                """;
        RegionSelector regionSelector = new RegionSelector(sudokuText);
        Sudoku sudoku = new Sudoku(regionSelector.toBoard());
        List<Cell> evenCells = regionSelector.getCellRegion("E");
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new EvenRule(evenCells));
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                657438921
                324971586
                891562734
                945326178
                763819452
                218754693
                186297345
                572143869
                439685217
                """);
    }
}
