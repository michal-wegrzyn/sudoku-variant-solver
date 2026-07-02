package sudokusolver.sudoku.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import sudokusolver.sudoku.Board;
import sudokusolver.sudoku.Edge;
import sudokusolver.sudoku.RegionSelector;
import sudokusolver.sudoku.Sudoku;
import java.util.List;

public class XVRuleTest {
    @Test
    @DisplayName("Test applying XV rules to a valid Sudoku")
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    public void testApplyXVRules() {
        String sudokuText = """
                .|.X.|.|.|.X.|.|.
                -+-+-+-+-+X+-+X+-
                .|.|.V1|.|.|.|.|.
                -+-+-+X+X+-+-+-+-
                2V.|.|.|.|4|.|.|.
                X+-+-+-+-+V+-+-+-
                .|.|3|.|.X.|.|5|.
                -+-+-+-+-+-+-+-+X
                9|.|.|7|8|.|1V.|.
                X+-+-+-+X+-+-+-+-
                .V.|.|.|.V.|.|.|.
                -+-+-+-+-+X+X+-+-
                .|.|.|.|.|.|.|.|.
                -+-+-+V+-+-+-+X+-
                .|.|.|.|.|.|.|.|.
                -+-+-+-+X+-+X+-+-
                5|.|.X.|.|.|.|.|.
                """;
        RegionSelector regionSelector = new RegionSelector(sudokuText, true);
        Sudoku sudoku = new Sudoku(regionSelector.toBoard());
        List<Edge> vEdges = regionSelector.getEdgeRegion("V");
        List<Edge> xEdges = regionSelector.getEdgeRegion("X");
        sudoku.addRule(new ClassicRule(3, 3));
        sudoku.addRule(new XVRule(vEdges, xEdges));
        List<Board> solutions = sudoku.solve();
        assertThat(solutions).hasSize(1);
        assertThat(solutions.get(0).toString()).isEqualTo("""
                719652834
                654138972
                238974561
                863491257
                925786143
                147523698
                396217485
                481365729
                572849316
                """);
    }
}
