package sudokusolver.sudoku;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import sudokusolver.sudoku.rules.ClassicRule;
import sudokusolver.sudoku.rules.EvenRule;
import sudokusolver.sudoku.rules.OddRule;
import sudokusolver.sudoku.rules.AllDifferentRule;
import sudokusolver.sudoku.rules.DiagonalRule;
import sudokusolver.sudoku.rules.RowsAndColumnsRule;

import java.util.List;

public class SudokuTest {
    @Test
    @DisplayName("Test counting ways to fill a 4x4 Sudoku")
    public void testCountWaysToFill4x4() {
        String sudokuText = """
                ....
                .2..
                ....
                ....
                """;
        Sudoku sudoku = new Sudoku(sudokuText, 4);
        sudoku.addRule(new ClassicRule(0, 0, 2, 2));
        assertThat(sudoku.getMaxNumber()).isEqualTo(4);
        assertThat(sudoku.getCells()).hasSize(16);
        List<Board> solutions = sudoku.solve(100);
        assertThat(solutions).hasSize(72);
    }

    @Test
    @DisplayName("Solve a huge sudoku")
    @Timeout(value = 750, unit = TimeUnit.MILLISECONDS)
    public void testHugeSudoku() {
        /*
         * Top left: Diagonal
         * Top right: Irregular
         * Center: Classic
         * Bottom left: Even-Odd
         * Bottom right: Extra regions
         */
        String sudokuText = """
                .1.7.8.2.   .5.7.8.2.
                9.4...6.8   1.7...8.5
                .2.....5.   .9.....8.
                2...3...6   2...4...1
                ...2.7...   ...6.1...
                4...8.2.7   6.8.1...4
                .4...3...4.9...5...9.
                5.7.......7.......6.8
                .9.5.4....1....3.2.1.
                      9.4.6...7
                      .319.768.
                      5...4.9.3
                .3.9.8....3....6.7.4.
                9.4.......9.......7.2
                .1...2...2.6...2...9.
                4...2.7.1   1.7.8...5
                ...6.5...   ...5.3...
                8...7...2   5...6...7
                .8.....7.   .4.....7.
                3.2...8.6   8.3...6.4
                .6.3.4.1.   .5.3.6.1.
                """;
        String evenOddText = """
                EOOOOEEEO
                OOEOEOOEE
                EOEOEEOOO
                EOEEEOOOO
                OEOEOOOEE
                EOOEOOOEE
                OEOEOEEOO
                OEEOOOEOE
                OEOOEEEOO
                """;
        String irregularRegionsText = """
                AACDDFFFF
                ACCCDDDFF
                ACDDDGDGF
                ACCEEGGGF
                AACEGGHHF
                AACEEGHHH
                BBBEEGHIH
                BBBEEIHIH
                BBBIIIIII
                """;
        String extraRegionsText = """
                ....AA...
                .....A...
                .....A...
                .....AAAA
                B.......A
                BBBB.....
                ...B.....
                ...B.....
                ...BB....
                """;
        Sudoku sudoku = new Sudoku(sudokuText, 9);
        assertThat(sudoku.getCells()).hasSize(81 * 5 - 4 * 9);
        assertThat(sudoku.getCellValue(0, 1)).isEqualTo(1);
        assertThat(sudoku.getCellValue(19, 20)).isEqualTo(4);
        assertThat(sudoku.getCellValue(5, 10)).isEqualTo(-1);
        // Top left: Diagonal
        sudoku.addRule(new ClassicRule(0, 0, 3, 3));
        sudoku.addRule(new DiagonalRule(0, 0, 9));
        // Top right: Irregular
        sudoku.addRule(new RowsAndColumnsRule(0, 12, 9));
        RegionSelector regionSelector = new RegionSelector(irregularRegionsText, 0, 12);
        for (char c = 'A'; c <= 'I'; c++) {
            sudoku.addRule(new AllDifferentRule(regionSelector.getCellRegion(c)));
        }
        // Center: Classic
        sudoku.addRule(new ClassicRule(6, 6, 3, 3));
        // Bottom left: Even-Odd
        sudoku.addRule(new ClassicRule(12, 0, 3, 3));
        regionSelector = new RegionSelector(evenOddText, 12, 0);
        sudoku.addRule(new EvenRule(regionSelector.getCellRegion('E')));
        sudoku.addRule(new OddRule(regionSelector.getCellRegion('O')));
        // Bottom right: Extra regions
        sudoku.addRule(new ClassicRule(12, 12, 3, 3));
        regionSelector = new RegionSelector(extraRegionsText, 12, 12);
        sudoku.addRule(new AllDifferentRule(regionSelector.getCellRegion('A')));
        sudoku.addRule(new AllDifferentRule(regionSelector.getCellRegion('B')));

        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                316758924   951768423
                954312678   127493865
                728649351   394156287
                279435186   265849371
                831267549   483621759
                465981237   678215934
                642893715429836574192
                587126493678512937648
                193574862513749382516
                      984362157     \s
                      231957684     \s
                      576841923     \s
                235918647135298637541
                974563128794365491782
                618742359286471258396
                456829731   137982465
                127635984   684573129
                893471562   529164837
                581296473   946815273
                342157896   813729654
                769384215   752346918
                """);
    }
}
