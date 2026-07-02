package sudokusolver.sudoku.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import sudokusolver.sudoku.Board;
import sudokusolver.sudoku.Cell;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.constraints.AllDifferentConstraint;
import sudokusolver.sudoku.RegionSelector;
import java.util.List;
import java.util.Set;

public class AllDifferentRuleTest {
    @Test
    @DisplayName("Check generated constraints for a region")
    public void testGeneratedConstraints() {
        String regionText = """
                ....
                .AA.
                .AA.
                ....
                """;
        RegionSelector regionSelector = new RegionSelector(regionText);
        Sudoku sudoku = new Sudoku(regionSelector.toBoard(), 4);
        AllDifferentRule ruleA = new AllDifferentRule(regionSelector.getCellRegion('A'));
        Set<Constraint> constraints = ruleA.apply(sudoku);
        assertThat(constraints).hasSize(1);
        Constraint constraint = constraints.iterator().next();
        assertThat(constraint).isInstanceOf(AllDifferentConstraint.class);
        assertThat(constraint.getVariables()).containsExactlyInAnyOrder(Cell.toString(1, 1),
                Cell.toString(1, 2),
                Cell.toString(2, 1), Cell.toString(2, 2));
    }

    @Test
    @DisplayName("Test applying extra regions to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyAllDifferentRules() {
        String sudokuText = """
                6.8..5.1.
                ......28.
                ...8.3...
                ...1.6..4
                .......7.
                5........
                .7......3
                ...2.45..
                .1.7.....
                """;
        String extraRegionsText = """
                .........
                .AAA.BBB.
                .AAA.BBB.
                .AAA.BBB.
                .........
                .CCC.DDD.
                .CCC.DDD.
                .CCC.DDD.
                .........
                """;
        RegionSelector regionSelector = new RegionSelector(extraRegionsText);
        Sudoku sudoku = new Sudoku(sudokuText);
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new AllDifferentRule(regionSelector.getCellRegion('A')));
        sudoku.addRule(new AllDifferentRule(regionSelector.getCellRegion('B')));
        sudoku.addRule(new AllDifferentRule(regionSelector.getCellRegion('C')));
        sudoku.addRule(new AllDifferentRule(regionSelector.getCellRegion('D')));
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                628475319
                347691285
                159823746
                732186954
                496352871
                581947632
                974568123
                863214597
                215739468
                """);
    }
}
