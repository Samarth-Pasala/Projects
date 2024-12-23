package cs3500.threetrios.model;

import java.util.Objects;

/**
 * Represents a strategy that selects a move based on the maximum number of potential flips.
 * The strategy will attempt to place a card in a position where it flips the maximum number of
 * opponent's cards. If multiple moves result in the same number of flips, it will choose the one
 * that is considered "better" based on specific criteria.
 */
public class FlipMaxCardsStrategy extends AbstractStrategies {

  /**
   * Constructs a FlipMaxCardsStrategy with the specified game instance.
   *
   * @param game the ThreeTriosGame instance to use for the strategy.
   * @throws IllegalArgumentException if the game is null.
   */
  public FlipMaxCardsStrategy(ThreeTriosGame game) {
    super(game);
  }

  /**
   * Determines the best move for the player based on the strategy of maximizing the number of
   * flipped cards.
   *
   * @param player the player for whom the move is being calculated.
   * @return the best move that maximizes the number of flipped cards, or a
   *     fallback move if no valid moves are available.
   */
  @Override
  public ThreeTriosStrategies.Move getMove(Player player) {
    Objects.requireNonNull(player, "Player cannot be null");

    Grid grid = game.getGrid();
    if (grid == null || player.getHand().isEmpty()) {
      return fallbackMove(player);
    }

    int maxFlips = 0;
    ThreeTriosStrategies.Move bestMove = null;

    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        Cell cell = grid.getCell(row, col);

        if (cell.isHole() || !game.canCurrentPlayerPlaceCard(row, col)) {
          continue;
        }

        for (int cardIndex = 0; cardIndex < player.getHand().size(); cardIndex++) {
          Card card = player.getHand().get(cardIndex);
          int flipCount = calculatePotentialFlips(card, row, col, player);

          if (flipCount > maxFlips ||
                  (flipCount == maxFlips && isBetterMove(row, col, cardIndex, bestMove))) {
            maxFlips = flipCount;
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

  /**
   * Calculates the potential number of flips if a card is placed at a specific position.
   * The flips are determined based on the attack values of the card in relation to adjacent
   * opponent's cards.
   *
   * @param card   the card being considered for placement.
   * @param row    the row index of the position where the card is being considered.
   * @param col    the column index of the position where the card is being considered.
   * @param player the player who is placing the card.
   * @return the number of potential flips for placing the card at the given position.
   */
  private int calculatePotentialFlips(Card card, int row, int col, Player player) {
    int flips = 0;
    for (CardDirection direction : CardDirection.values()) {
      int[] adjacentPos = getAdjacentPosition(row, col, direction);
      if (isWithinBounds(adjacentPos[0], adjacentPos[1])) {
        Cell adjacentCell = game.getCell(adjacentPos[0], adjacentPos[1]);
        if (!adjacentCell.isEmpty() && adjacentCell.getCard().getCardOwner() != player.getColor()) {
          AttackValue playerAttackValue = card.getAttackValue(direction);
          CardDirection oppositeDirection = getOppositeDirection(direction);
          AttackValue opponentAttackValue = adjacentCell.getCard().getAttackValue(
                  oppositeDirection);

          if (playerAttackValue.getValue() > opponentAttackValue.getValue()) {
            flips++;
          }
        }
      }
    }
    return flips;
  }


  /**
   * Determines the position of the cell adjacent to a given cell in a specific direction.
   *
   * @param row       the row index of the current cell.
   * @param col       the column index of the current cell.
   * @param direction the direction in which to find the adjacent position.
   * @return an array containing the row and column indices of the adjacent cell.
   */
  private int[] getAdjacentPosition(int row, int col, CardDirection direction) {
    switch (direction) {
      case NORTH:
        return new int[]{row - 1, col};
      case SOUTH:
        return new int[]{row + 1, col};
      case EAST:
        return new int[]{row, col + 1};
      case WEST:
        return new int[]{row, col - 1};
      default:
        throw new IllegalArgumentException("Invalid direction");
    }
  }

  /**
   * Checks if the given row and column are within the bounds of the grid.
   *
   * @param row the row index to check.
   * @param col the column index to check.
   * @return true if the position is within bounds; false otherwise.
   */
  private boolean isWithinBounds(int row, int col) {
    return row >= 0 && row < game.getGrid().getRows() && col >= 0 && col < game.getGrid().getCols();
  }

  /**
   * Returns the opposite direction of the given direction.
   *
   * @param direction the direction for which to find the opposite.
   * @return the opposite CardDirection.
   */
  private CardDirection getOppositeDirection(CardDirection direction) {
    switch (direction) {
      case NORTH:
        return CardDirection.SOUTH;
      case SOUTH:
        return CardDirection.NORTH;
      case EAST:
        return CardDirection.WEST;
      case WEST:
        return CardDirection.EAST;
      default:
        throw new IllegalArgumentException("Invalid direction.");
    }
  }

  /**
   * Determines if the new move is better than the current best move in case of a tie
   * in the number of flipped cards.
   *
   * @param row       the row index of the new move.
   * @param col       the column index of the new move.
   * @param cardIndex the card index of the new move.
   * @param bestMove  the current best move.
   * @return true if the new move is better; false otherwise.
   */
  private boolean isBetterMove(int row, int col, int cardIndex,
                               ThreeTriosStrategies.Move bestMove) {
    if (bestMove == null) {
      return true;
    }
    return (row < bestMove.row || (row == bestMove.row && col < bestMove.col))
            || (row == bestMove.row && col == bestMove.col && cardIndex < bestMove.cardIndex);
  }
}

