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
import java.util.List;
import java.util.Set;

public class AntiKnightRuleTest {
    @Test
    @DisplayName("Check generated constraints for anti-knight rule")
    public void testGeneratedConstraints() {
        String sudokuText = """
                ....
                .1..
                ..2.
                ....
                """;
        Sudoku sudoku = new Sudoku(sudokuText);
        AntiKnightRule antiKnightRule = new AntiKnightRule();
        Set<Constraint> constraints = antiKnightRule.apply(sudoku);
        assertThat(constraints).hasSize(24);
        boolean[][][][] seenConstraints = new boolean[4][4][4][4];
        for (Constraint constraint : constraints) {
            assertThat(constraint).isInstanceOf(AllDifferentConstraint.class);
            AllDifferentConstraint allDifferentConstraint = (AllDifferentConstraint) constraint;
            assertThat(allDifferentConstraint.getVariables()).hasSize(2);
            String var1 = allDifferentConstraint.getVariables().get(0);
            String var2 = allDifferentConstraint.getVariables().get(1);
            Cell cell1 = new Cell(var1);
            Cell cell2 = new Cell(var2);
            int rowDiff = Math.abs(cell1.row() - cell2.row());
            int colDiff = Math.abs(cell1.col() - cell2.col());
            assertThat(rowDiff == 2 && colDiff == 1 || rowDiff == 1 && colDiff == 2).isTrue();
            assertThat(seenConstraints[cell1.row()][cell1.col()][cell2.row()][cell2.col()]).isFalse();
            seenConstraints[cell1.row()][cell1.col()][cell2.row()][cell2.col()] = true;
            seenConstraints[cell2.row()][cell2.col()][cell1.row()][cell1.col()] = true;
        }
    }

    @Test
    @DisplayName("Test applying anti-knight rules to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyAntiKnightRules() {
        String sudokuText = """
                ......9.6
                .....3...
                ........1
                .6.1.4..2
                ....2....
                .....87..
                .......9.
                .........
                692......
                """;
        Sudoku sudoku = new Sudoku(sudokuText);
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new AntiKnightRule());
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                347812956
                219653847
                586947321
                968174532
                173529468
                425368719
                851736294
                734291685
                692485173
                """);
    }
}
