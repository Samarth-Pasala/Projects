package cs3500.threetrios.model;

import java.util.Objects;

/**
 * Abstract base class for strategies in the Three Trios game.
 * Provides common functionality for different strategy implementations, such as interacting with
 * the game model and selecting fallback moves.
 */
public abstract class AbstractStrategies implements Strategies {

  protected ThreeTriosGame game;

  /**
   * Constructs a ThreeTriosStrategies with the specified game instance.
   *
   * @param game the ThreeTriosGame instance to use for the strategies.
   * @throws IllegalArgumentException if the game is null
   */
  public AbstractStrategies(ThreeTriosGame game) {
    this.game = Objects.requireNonNull(game, "Game cannot be null");
  }

  /**
   * Fallback move selection that finds the upper-leftmost available position with card index 0.
   */
  protected ThreeTriosStrategies.Move fallbackMove(Player player) {
    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.canCurrentPlayerPlaceCard(row, col)) {
          return new ThreeTriosStrategies.Move(row, col, 0);
        }
      }
    }
    throw new IllegalStateException("No valid moves available");
  }
}
