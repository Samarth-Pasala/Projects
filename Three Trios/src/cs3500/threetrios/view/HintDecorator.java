package cs3500.threetrios.view;

import cs3500.threetrios.model.AttackValue;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardDirection;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.ReadonlyThreeTriosModel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

/**
 * A decorator for rendering hints on the grid. Displays the number of potential card flips
 * for each cell when a card is selected.
 */
public class HintDecorator implements GridDecorator {
  private final ReadonlyThreeTriosModel model;
  private final int selectedCardIndex;

  public HintDecorator(ReadonlyThreeTriosModel model, int selectedCardIndex) {
    this.model = model;
    this.selectedCardIndex = selectedCardIndex;
  }

  @Override
  public void decorate(Graphics g, int rows, int cols, int cellSize) {
    List<Card> hand = model.getCurrentPlayer().getHand();
    if (selectedCardIndex < 0 || selectedCardIndex >= hand.size()) {
      return;
    }

    Card selectedCard = hand.get(selectedCardIndex);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (model.canCurrentPlayerPlaceCard(row, col)) {
          int flips = calculatePotentialFlips(row, col, selectedCard);

          int centerX = col * cellSize + cellSize / 2;
          int centerY = row * cellSize + cellSize / 2;

          g.setColor(Color.RED);
          g.drawString(String.valueOf(flips), centerX, centerY);
        }
      }
    }
  }

  private int calculatePotentialFlips(int row, int col, Card selectedCard) {
    int flips = 0;

    for (CardDirection direction : CardDirection.values()) {
      int[] adjacentPos = getAdjacentPosition(row, col, direction);
      int adjRow = adjacentPos[0];
      int adjCol = adjacentPos[1];

      if (isWithinBounds(adjRow, adjCol)) {
        Cell adjacentCell = model.getGrid().getCell(adjRow, adjCol);
        if (!adjacentCell.isEmpty()) {
          Card adjacentCard = adjacentCell.getCard();

          if (adjacentCard.getCardOwner() != model.getCurrentPlayer().getColor()) {
            if (winsBattle(selectedCard, adjacentCard, direction)) {
              flips++;
            }
          }
        }
      }
    }

    return flips;
  }

  private boolean winsBattle(Card attackingCard, Card defendingCard,
                             CardDirection attackDirection) {
    AttackValue attackValue = attackingCard.getAttackValue(attackDirection);

    CardDirection defendingDirection = getOppositeDirection(attackDirection);
    AttackValue defenseValue = defendingCard.getAttackValue(defendingDirection);

    return attackValue.getValue() > defenseValue.getValue();
  }

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
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }
  }

  private boolean isWithinBounds(int row, int col) {
    return row >= 0 && row < model.getGrid().getRows()
            && col >= 0 && col < model.getGrid().getCols();
  }

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
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }
  }
}

