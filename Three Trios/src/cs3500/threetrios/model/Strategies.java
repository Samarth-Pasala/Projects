package cs3500.threetrios.model;

/**
 * Represents a strategy for determining the best move for a player in the Three Trios game.
 * Different strategies can be implemented to decide how a player should place their cards on
 * the grid based on different criteria or game mechanics.
 */
public interface Strategies {

  /**
   * Determines the best move for a given player according to the specific strategy.
   * The move consists of selecting a card from the player's hand and a position on the grid.
   *
   * @param player the player for whom the move is being calculated.
   * @return the best move for the player, represented as a ThreeTriosStrategies.Move.
   */
  ThreeTriosStrategies.Move getMove(Player player);

}
