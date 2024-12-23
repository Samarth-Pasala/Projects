package cs3500.threetrios.model;

/**
 * Represents a cell on the grid in the game.
 * A cell can either contain a Card or be empty, indicating a space where a card can be placed.
 * The methods in this interface allow checking the status of the cell and managing the card
 * it contains.
 */
public interface Cell {

  /**
   * Checks if the cell is a hole (an empty space where a card cannot be placed).
   *
   * @return true if the cell is a hole; false otherwise
   */
  boolean isHole();

  /**
   * Checks if the cell is empty (contains no card).
   *
   * @return true if the cell is empty; false otherwise
   */
  boolean isEmpty();

  /**
   * Retrieves the card currently placed in the cell.
   *
   * @return the Card in the cell, or null if the cell is empty
   */
  Card getCard();

  /**
   * Places a card in the cell.
   *
   * @param card the Card to be placed in the cell
   * @throws IllegalArgumentException if the cell is not empty or is a hole
   */
  void placeCard(Card card);

  /**
   * Creates a copy of this Cell.
   * @return a new Cell instance with the same hole status as the original,
   *         but no card placed in it.
   */
  Cell copy();
}
