package cs3500.threetrios.controller;

/**
 * Interface for handling user interactions in the Three Trios game.
 * This interface provides methods for selecting cards and grid cells in the game.
 * The implementing classes will define the behavior for these actions.
 */
public interface ThreeTriosFeatures {

  /**
   * Handles the action of selecting a card from the player's hand.
   *
   * @param cardIndex the index of the card in the player's hand.
   */
  void selectCard(int cardIndex);

  /**
   * Handles the action of selecting a grid cell to play a card.
   *
   * @param row the row index of the selected cell.
   * @param col the column index of the selected cell.
   */
  void selectCell(int row, int col);
}
