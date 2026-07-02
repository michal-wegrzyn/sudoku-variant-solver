package sudokusolver.sudoku;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionSelectorTest {
    @Test
    @DisplayName("Test cells-only region selection")
    public void testCellsOnlyRegionSelection() {
        String sudokuText = """
                ....
                .CA.
                ....BB
                AA..CC
                  B...
                  BC.B
                """;
        RegionSelector regionSelector = new RegionSelector(sudokuText);
        assertThat(regionSelector.getCellRegion('A')).containsExactlyInAnyOrder(
                new Cell(1, 2), new Cell(3, 0), new Cell(3, 1));
        assertThat(regionSelector.getCellRegion('B')).containsExactlyInAnyOrder(
                new Cell(2, 4), new Cell(2, 5), new Cell(4, 2), new Cell(5, 2), new Cell(5, 5));
        assertThat(regionSelector.getCellRegion('C')).containsExactlyInAnyOrder(
                new Cell(1, 1), new Cell(3, 4), new Cell(3, 5), new Cell(5, 3));
    }

    @Test
    @DisplayName("Test cells+edges+corners region selection")
    public void testCellsEdgesCornersRegionSelection() {
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
        assertThat(regionSelector.getEdgeRegion('V')).hasSize(7);
        assertThat(regionSelector.getEdgeRegion('X')).hasSize(17);
        assertThat(regionSelector.getEdgeRegion("-|")).hasSize(120);
        assertThat(regionSelector.getCellRegion('.')).hasSize(71);
        assertThat(regionSelector.getCornerRegion('+')).hasSize(64);
        Board board = regionSelector.toBoard();
        assertThat(board.getCells()).hasSize(81);
        assertThat(board.getCellValue(0, 0)).isEqualTo(0);
        assertThat(board.getCellValue(1, 3)).isEqualTo(1);
        assertThat(board.getCellValue(10, 5)).isEqualTo(-1);
    }
}
