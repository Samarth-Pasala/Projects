package cs3500.threetrios.model;

import java.util.List;

/**
 * Represents a grid for the game, consisting of cells arranged in rows and columns.
 * The grid allows for the retrieval of cell information and movement validation,
 * which is essential for gameplay mechanics.
 */
public interface Grid {

  /**
   * Retrieves the number of rows in the grid.
   *
   * @return The number of rows.
   */
  int getRows();

  /**
   * Retrieves the number of columns in the grid.
   *
   * @return The number of columns.
   */
  int getCols();

  /**
   * Retrieves a specific cell in the grid.
   *
   * @param row The row index.
   * @param col The column index.
   * @return The cell at the specified location.
   */
  Cell getCell(int row, int col);

  /**
   * Retrieves a list of cells that are adjacent to the specified cell.
   *
   * @param row The row index of the target cell.
   * @param col The column index of the target cell.
   * @return A list of adjacent cells.
   * @throws IndexOutOfBoundsException if the row or column index is out of range.
   */
  List<Cell> getAdjacentCells(int row, int col);

  /**
   * Checks whether a move to the specified cell is valid.
   *
   * @param row The row index of the target cell.
   * @param col The column index of the target cell.
   * @return true if the move is valid; false otherwise.
   * @throws IndexOutOfBoundsException if the row or column index is out of range.
   */
  boolean isValidMove(int row, int col);

  /**
   * Creates a deep copy of the current grid, including all its cells.
   *
   * @return a new Grid instance that is a copy of the current grid, with the same
   *         dimensions and cells (but with independent copies of the cells).
   */
  Grid copy();
}
