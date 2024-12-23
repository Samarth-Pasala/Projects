package cs3500.threetrios.view;

import java.awt.Graphics;

/**
 * Represents a decorator for the grid rendering. A GridDecorator is responsible for
 * adding additional visual elements to the grid, such as hints or overlays.
 */
public interface GridDecorator {
  /**
   * Decorates the grid with additional rendering elements.
   *
   * @param g      the Graphics object used for rendering.
   * @param rows   the number of rows in the grid.
   * @param cols   the number of columns in the grid.
   * @param cellSize the size of each cell in the grid.
   */
  void decorate(Graphics g, int rows, int cols, int cellSize);
}