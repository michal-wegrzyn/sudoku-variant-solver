package sudokusolver.sudoku.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import sudokusolver.sudoku.Board;
import sudokusolver.sudoku.Sudoku;
import sudokusolver.sudoku.RegionSelector;
import java.util.List;

public class ThermoRuleTest {
    @Test
    @DisplayName("Test applying thermo rules to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyThermoRules() {
        String sudokuText = """
                ......5.4
                .......6.
                .........
                ....2....
                .........
                ..1......
                .........
                .9.....3.
                7.8......
                """;
        String thermometersText = """
                01.......
                .2.......
                .3.567...
                .4...8...
                BCDlA9qrs
                ...m....t
                ...nopKJI
                ........H
                ......EFG
                """;
        RegionSelector regionSelector = new RegionSelector(thermometersText);
        Sudoku sudoku = new Sudoku(sudokuText);
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new ThermoRule(regionSelector.getCellRegion("01234")));
        sudoku.addRule(new ThermoRule(regionSelector.getCellRegion("56789A")));
        sudoku.addRule(new ThermoRule(regionSelector.getCellRegion("BCD")));
        sudoku.addRule(new ThermoRule(regionSelector.getCellRegion("EFGHIJK")));
        sudoku.addRule(new ThermoRule(regionSelector.getCellRegion("lmnop")));
        sudoku.addRule(new ThermoRule(regionSelector.getCellRegion("qrst")));
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                123869574
                849571362
                657234891
                984625713
                236197458
                571348629
                312456987
                495782136
                768913245
                """);
    }
}
