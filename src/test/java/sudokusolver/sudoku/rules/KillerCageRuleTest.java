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

public class KillerCageRuleTest {
    @Test
    @DisplayName("Test applying killer cage rules to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyKillerCageRules() {
        String sudokuText = """
                ...4.....
                ........7
                ......9..
                1........
                ......1..
                ....1....
                .......9.
                .1.......
                ..4..1...
                """;
        String killerCagesText = """
                001123344
                556622778
                999AAA778
                BCCCDDEFG
                BHIIJEEFG
                KHIJJLMMG
                KNOPPLQQQ
                OOORSSTTT
                UUOVVVTWW
                """;
        RegionSelector regionSelector = new RegionSelector(killerCagesText);
        Sudoku sudoku = new Sudoku(sudokuText);
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new KillerCageRule(5, regionSelector.getCellRegion('0')));
        sudoku.addRule(new KillerCageRule(5, regionSelector.getCellRegion('1')));
        sudoku.addRule(new KillerCageRule(17, regionSelector.getCellRegion('2')));
        sudoku.addRule(new KillerCageRule(12, regionSelector.getCellRegion('3')));
        sudoku.addRule(new KillerCageRule(14, regionSelector.getCellRegion('4')));
        sudoku.addRule(new KillerCageRule(17, regionSelector.getCellRegion('5')));
        sudoku.addRule(new KillerCageRule(6, regionSelector.getCellRegion('6')));
        sudoku.addRule(new KillerCageRule(17, regionSelector.getCellRegion('7')));
        sudoku.addRule(new KillerCageRule(9, regionSelector.getCellRegion('8')));
        sudoku.addRule(new KillerCageRule(17, regionSelector.getCellRegion('9')));
        sudoku.addRule(new KillerCageRule(16, regionSelector.getCellRegion('A')));
        sudoku.addRule(new KillerCageRule(10, regionSelector.getCellRegion('B')));
        sudoku.addRule(new KillerCageRule(18, regionSelector.getCellRegion('C')));
        sudoku.addRule(new KillerCageRule(9, regionSelector.getCellRegion('D')));
        sudoku.addRule(new KillerCageRule(9, regionSelector.getCellRegion('E')));
        sudoku.addRule(new KillerCageRule(7, regionSelector.getCellRegion('F')));
        sudoku.addRule(new KillerCageRule(21, regionSelector.getCellRegion('G')));
        sudoku.addRule(new KillerCageRule(9, regionSelector.getCellRegion('H')));
        sudoku.addRule(new KillerCageRule(12, regionSelector.getCellRegion('I')));
        sudoku.addRule(new KillerCageRule(15, regionSelector.getCellRegion('J')));
        sudoku.addRule(new KillerCageRule(9, regionSelector.getCellRegion('K')));
        sudoku.addRule(new KillerCageRule(14, regionSelector.getCellRegion('L')));
        sudoku.addRule(new KillerCageRule(10, regionSelector.getCellRegion('M')));
        sudoku.addRule(new KillerCageRule(8, regionSelector.getCellRegion('N')));
        sudoku.addRule(new KillerCageRule(25, regionSelector.getCellRegion('O')));
        sudoku.addRule(new KillerCageRule(6, regionSelector.getCellRegion('P')));
        sudoku.addRule(new KillerCageRule(17, regionSelector.getCellRegion('Q')));
        sudoku.addRule(new KillerCageRule(6, regionSelector.getCellRegion('R')));
        sudoku.addRule(new KillerCageRule(15, regionSelector.getCellRegion('S')));
        sudoku.addRule(new KillerCageRule(17, regionSelector.getCellRegion('T')));
        sudoku.addRule(new KillerCageRule(9, regionSelector.getCellRegion('U')));
        sudoku.addRule(new KillerCageRule(13, regionSelector.getCellRegion('V')));
        sudoku.addRule(new KillerCageRule(11, regionSelector.getCellRegion('W')));
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                231497586
                895126437
                467583912
                178354629
                943762158
                652819374
                386245791
                519678243
                724931865
                """);
    }
}
