package cs3500.threetrios.model;

import java.util.List;

/**
 * Represents a strategy for selecting a move that prioritizes placing cards in the
 * corners of the grid. The strategy will try to place a card in one of the four corners of the
 * grid (if the move is valid). If no valid moves are available in the corners,
 * the strategy falls back to a default move.
 */
public class ChooseCornerStrategy extends AbstractStrategies {

  /**
   * Constructs a ChooseCornerStrategy with the specified game instance.
   *
   * @param game the ThreeTriosGame instance to use for the strategy.
   * @throws IllegalArgumentException if the game is null.
   */
  public ChooseCornerStrategy(ThreeTriosGame game) {
    super(game);
  }

  /**
   * Determines the best move for the player based on the corner strategy.
   * The strategy attempts to place a card in one of the four corners of the grid.
   * If no valid move is available in the corners, the strategy falls back to another move.
   *
   * @param player the player for whom the move is being calculated.
   * @return the best move that places a card in one of the corners, or a fallback move if no valid
   *         corner moves are available.
   */
  @Override
  public ThreeTriosStrategies.Move getMove(Player player) {
    ThreeTriosStrategies.Move bestMove = null;

    int[][] corners = {
            {0, 0},
            {0, game.getGrid().getCols() - 1},
            {game.getGrid().getRows() - 1, 0},
            {game.getGrid().getRows() - 1, game.getGrid().getCols() - 1}
    };

    List<Card> hand = player.getHand();
    for (int cardIndex = 0; cardIndex < hand.size(); cardIndex++) {
      Card card = hand.get(cardIndex);

      for (int[] corner : corners) {
        int row = corner[0];
        int col = corner[1];

        if (game.canCurrentPlayerPlaceCard(row, col)) {
          if (bestMove == null || (row < bestMove.row || (row == bestMove.row && col <
                  bestMove.col))) {
            bestMove = new ThreeTriosStrategies.Move(row, col, cardIndex);
          }
        }
      }
    }

    if (bestMove != null) {
      return bestMove;
    } else {
      return fallbackMove(player);
    }
  }
}
