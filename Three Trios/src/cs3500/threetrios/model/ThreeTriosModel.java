package cs3500.threetrios.model;

import cs3500.threetrios.controller.ModelStateListener;

/**
 * Represents the core game model for the game.
 * This interface defines the main functionalities for managing the game state,
 * including initializing the game, placing cards, and determining the winner.
 */
public interface ThreeTriosModel extends ReadonlyThreeTriosModel {

  /**
   * Places a card at the specified grid location.
   *
   * @param index The index of the card in the player's hand.
   * @param row   The row in the grid where the card will be placed.
   * @param col   The column in the grid where the card will be placed.
   * @throws IllegalArgumentException if the index is out of bounds,
   *                                  or if the specified cell is not a valid move.
   */
  void placeCard(int index, int row, int col);

  /**
   * Initializes and starts the game.
   * This method sets up the game state, including shuffling cards,
   * and determining the first player.
   */
  void startGame();


  /**
   * Ends the current player's turn, passing control to the next player.
   * This method should update the game state accordingly.
   */
  void endTurn();

  /**
   * Adds a listener to track changes in the model's state.
   *
   * @param listener the listener to add.
   */
  void addModelStateListener(ModelStateListener listener);
}
