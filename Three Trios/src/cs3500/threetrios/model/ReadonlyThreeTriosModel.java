package cs3500.threetrios.model;

import java.util.List;

/**
 * The Readonly interface for the Three Trios game.
 * Provides read-only access to the game state.
 */
public interface ReadonlyThreeTriosModel {

  /**
   * Gets the current grid of the game.
   *
   * @return the grid representing the game state.
   */
  Grid getGrid();

  /**
   * Gets the current player.
   *
   * @return the player whose turn it is.
   */
  Player getCurrentPlayer();


  /**
   * Gets the player who is not currently taking their turn.
   *
   * @return the player whose turn it is not, aka the opponent of the current player.
   */
  Player getOtherPlayer();

  /**
   * Checks if the game is over.
   *
   * @return true if the game is over, false otherwise.
   */
  boolean isGameOver();


  /**
   * Gets the contents of a cell in the grid at a given position.
   *
   * @param row the row of the cell.
   * @param col the column of the cell.
   * @return the contents of the cell at the given position.
   */
  Cell getCell(int row, int col);

  /**
   * Checks if the current player can legally play a card at the given coordinates.
   *
   * @param row the row of the cell.
   * @param col the column of the cell.
   * @return true if the current player can place a card at the specified cell, false otherwise.
   */
  boolean canCurrentPlayerPlaceCard(int row, int col);

  /**
   * Gets the score of a player, which is the number of cards they own in their hand
   * plus the number of cards they own on the grid.
   *
   * @param player the player whose score to retrieve.
   * @return the score of the player.
   */
  int getPlayerScore(Player player);

  /**
   * Gets the list of cards in a player's hand.
   *
   * @param player the player whose hand to retrieve.
   * @return the list of cards in the player's hand.
   */
  List<Card> getPlayerHand(Player player);

  /**
   * Determines the winner of the game.
   *
   * @return the Player who has won, or null if there is no winner yet.
   */
  Player determineWinner();
}
