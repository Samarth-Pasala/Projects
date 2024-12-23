package cs3500.threetrios.model;

import java.util.List;
import java.util.Objects;

import cs3500.threetrios.controller.AIPlayer;

/**
 * This class encapsulates the strategies used by the AI or automated player in the Three Trios
 * game. The strategies determine the best possible move based on various criteria such as
 * maximizing flips or selecting strategic positions like corners.
 */
public class ThreeTriosStrategies {

  private final ThreeTriosGame game;

  /**
   * Constructs a ThreeTriosStrategies with the specified game instance.
   *
   * @param game the ThreeTriosGame instance to use for the strategies.
   * @throws IllegalArgumentException if the game is null
   */
  public ThreeTriosStrategies(ThreeTriosGame game) {
    this.game = Objects.requireNonNull(game, "Game cannot be null");
  }

  /**
   * Strategy 1: Flip as many cards on this turn as possible.
   * This strategy evaluates each valid move (position and card combination)
   * and selects the move that results in the maximum number of opponent flips.
   *
   * @param player the player for whom to determine the best move.
   * @return a Move representing the best card and position to play.
   */
  public Move flipMaxCardsStrategy(AIPlayer player) {
    Objects.requireNonNull(player, "Player cannot be null");

    Grid grid = game.getGrid();
    if (grid == null || player.getHand().isEmpty()) {
      return fallbackMove(player);
    }

    int maxFlips = 0;
    Move bestMove = null;

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
            bestMove = new Move(row, col, cardIndex);
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
   * Strategy 2: Choose a corner position with minimal chance of being flipped.
   * If no corners are available, returns the upper-leftmost available position and card.
   */
  public Move chooseCorner(AIPlayer player) {
    Move bestMove = null;

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
            bestMove = new Move(row, col, cardIndex);
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
   * Fallback move selection that finds the upper-leftmost available position with card index 0.
   */
  private Move fallbackMove(AIPlayer player) {
    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        if (game.canCurrentPlayerPlaceCard(row, col)) {
          return new Move(row, col, 0);
        }
      }
    }
    throw new IllegalStateException("No valid moves available");
  }

  public int callCalculatePotentialFlips(Card card, int row, int col, AIPlayer player) {
    return calculatePotentialFlips(card, row, col, player);
  }

  /**
   * Calculates the potential flips if a card is placed at a specific position.
   */
  private int calculatePotentialFlips(Card card, int row, int col, AIPlayer player) {
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
   * Determines if the new move is better than the current best move in case of a tie
   * in the number of flipped cards.
   *
   * @param row       the row index of the new move.
   * @param col       the column index of the new move.
   * @param cardIndex the card index of the new move.
   * @param bestMove  the current best move.
   * @return true if the new move is better; false otherwise.
   */
  private boolean isBetterMove(int row, int col, int cardIndex, Move bestMove) {
    if (bestMove == null) {
      return true;
    }
    return (row < bestMove.row || (row == bestMove.row && col < bestMove.col))
            || (row == bestMove.row && col == bestMove.col && cardIndex < bestMove.cardIndex);
  }

  /**
   * Determines the position of the cell adjacent to a given cell in a specific direction.
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
   * Inner class to represent a Move (position + card index).
   * This class is used to encapsulate a specific move with the position on the grid
   * and the index of the card being placed.
   */
  public static class Move {
    public final int row;
    public final int col;
    public final int cardIndex;

    /**
     * Constructs a new Move object with the given position and card index.
     *
     * @param row       the row index of the move.
     * @param col       the column index of the move.
     * @param cardIndex the card index of the move.
     */
    public Move(int row, int col, int cardIndex) {
      this.row = row;
      this.col = col;
      this.cardIndex = cardIndex;
    }

    /**
     * Returns a string representation of the Move object.
     *
     * @return a string representing the Move in the format:
     */
    @Override
    public String toString() {
      return "Move: Card Index " + cardIndex + " at (" + row + ", " + col + ")";
    }
  }
}
