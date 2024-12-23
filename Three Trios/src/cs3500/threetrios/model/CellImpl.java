package cs3500.threetrios.model;

/**
 * Represents an implementation of a cell on the grid in the game.
 * This class manages the state of the cell, including whether it is a hole and the card
 * it contains.
 */
public class CellImpl implements Cell {

  private final boolean isHole;
  private Card card;

  /**
   * Constructs a CellImpl instance.
   *
   * @param isHole true if the cell is a hole; false otherwise
   */
  public CellImpl(boolean isHole) {
    this.isHole = isHole;
    this.card = null;
  }

  /**
   * Checks if the cell is a hole (an empty space where a card cannot be placed).
   *
   * @return true if the cell is a hole; false otherwise
   */
  @Override
  public boolean isHole() {
    return isHole;
  }

  /**
   * Checks if the cell is empty (contains no card).
   *
   * @return true if the cell is empty; false otherwise
   */
  @Override
  public boolean isEmpty() {
    return card == null;
  }

  /**
   * Retrieves the card currently placed in the cell.
   *
   * @return the Card in the cell, or null if the cell is empty
   */
  @Override
  public Card getCard() {
    return card;
  }

  /**
   * Places a card in the cell.
   *
   * @param card the Card to be placed in the cell
   * @throws IllegalArgumentException if the cell is not empty or is a hole
   */
  @Override
  public void placeCard(Card card) {
    if (!isHole && isEmpty()) {
      this.card = card;
    } else {
      throw new IllegalArgumentException("Cannot place card in this cell.");
    }
  }

  /**
   * Creates a copy of this Cell.
   * @return a new Cell instance with the same hole status as the original,
   *         but no card placed in it.
   */
  @Override
  public Cell copy() {
    return new CellImpl(this.isHole);
  }
}
