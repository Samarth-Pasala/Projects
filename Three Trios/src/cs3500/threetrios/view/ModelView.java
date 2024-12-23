package cs3500.threetrios.view;

/**
 * Represents the interface for the view component of the Three Trios game.
 * Defines the methods for rendering the current state of the game.
 */
public interface ModelView {

  /**
   * Renders the current game state, including the current player,
   * the grid, and the current player's hand.
   */
  void render();

  /**
   * Renders the current player and their color.
   */
  void renderCurrentPlayer();

  /**
   * Renders the game grid.
   */
  void renderGrid();

  /**
   * Renders the current player's hand.
   */
  void renderHand();
}
