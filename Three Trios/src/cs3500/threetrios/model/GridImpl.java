package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a concrete implementation of the Grid interface.
 * The GridImpl class manages a two-dimensional array of cells, providing functionality
 * to retrieve cell information, check for valid moves, and access adjacent cells.
 */
public class GridImpl implements Grid {

  private final int rows;
  private final int cols;
  private final List<List<Cell>> cells;

  /**
   * Constructs a GridImpl with the specified number of rows and columns,
   * and initializes the grid with the given cells.
   *
   * @param rows  The number of rows in the grid.
   * @param cols  The number of columns in the grid.
   * @param cells A two-dimensional list representing the grid's cells.
   * @throws IllegalArgumentException if rows or cols are non-positive,
   *                                  or if cells dimensions do not match.
   */
  public GridImpl(int rows, int cols, List<List<Cell>> cells) {
    if (rows <= 0 || cols <= 0) {
      throw new IllegalArgumentException("Grid dimensions must be greater than zero.");
    }
    if (cells.size() != rows) {
      throw new IllegalArgumentException("Number of rows does not match " +
              "specified grid dimensions.");
    }
    for (List<Cell> row : cells) {
      if (row.size() != cols) {
        throw new IllegalArgumentException("All rows must have the same number of columns.");
      }
    }
    this.rows = rows;
    this.cols = cols;
    this.cells = cells;
  }

  /**
   * Retrieves a specific cell in the grid.
   *
   * @param row The row index.
   * @param col The column index.
   * @return The cell at the specified location.
   */
  @Override
  public Cell getCell(int row, int col) {
    return cells.get(row).get(col);
  }

  /**
   * Retrieves the number of rows in the grid.
   *
   * @return The number of rows.
   */
  @Override
  public int getRows() {
    return rows;
  }

  /**
   * Retrieves the number of columns in the grid.
   *
   * @return The number of columns.
   */
  @Override
  public int getCols() {
    return cols;
  }

  /**
   * Retrieves a list of cells that are adjacent to the specified cell.
   *
   * @param row The row index of the target cell.
   * @param col The column index of the target cell.
   * @return A list of adjacent cells.
   * @throws IndexOutOfBoundsException if the row or column index is out of range.
   */
  @Override
  public List<Cell> getAdjacentCells(int row, int col) {
    List<Cell> adjacentCells = new ArrayList<>();
    if (row > 0) {
      adjacentCells.add(getCell(row - 1, col)); // North
    }
    if (row < rows - 1) {
      adjacentCells.add(getCell(row + 1, col)); // South
    }
    if (col > 0) {
      adjacentCells.add(getCell(row, col - 1)); // West
    }
    if (col < cols - 1) {
      adjacentCells.add(getCell(row, col + 1)); // East
    }
    return adjacentCells;
  }

  /**
   * Checks whether a move to the specified cell is valid.
   *
   * @param row The row index of the target cell.
   * @param col The column index of the target cell.
   * @return true if the move is valid; false otherwise.
   * @throws IndexOutOfBoundsException if the row or column index is out of range.
   */
  @Override
  public boolean isValidMove(int row, int col) {
    Cell cell = getCell(row, col);
    return !cell.isHole() && cell.isEmpty();
  }

  /**
   * Creates a deep copy of the current grid, including all its cells.
   *
   * @return a new Grid instance that is a copy of the current grid, with the same
   *         dimensions and cells (but with independent copies of the cells).
   */
  @Override
  public Grid copy() {
    List<List<Cell>> copiedCells = new ArrayList<>();

    for (List<Cell> row : this.cells) {
      List<Cell> copiedRow = new ArrayList<>();

      for (Cell cell : row) {
        copiedRow.add(cell.copy());
      }
      copiedCells.add(copiedRow);
    }
    return new GridImpl(this.rows, this.cols, copiedCells);
  }
}
