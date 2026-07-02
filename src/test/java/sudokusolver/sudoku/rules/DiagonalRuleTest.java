package sudokusolver.sudoku.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import sudokusolver.sudoku.Board;
import sudokusolver.sudoku.Sudoku;
import java.util.List;

public class DiagonalRuleTest {
    @Test
    @DisplayName("Test applying diagonal rules to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyDiagonalRules() {
        String sudokuText = """
                4.....76.
                .....7.43
                ...8.....
                6.....9..
                .17...83.
                ..8.....2
                .....3...
                83.5.....
                .72.....6
                """;
        Sudoku sudoku = new Sudoku(sudokuText);
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new DiagonalRule(9));
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                423159768
                981267543
                765834129
                654382917
                217695834
                398741652
                146973285
                839526471
                572418396
                """);
    }
}
