package cs3500.threetrios.view;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.CardDirection;
import cs3500.threetrios.model.AttackValue;
import cs3500.threetrios.model.ThreeTriosGame;

/**
 * Represents the view component of the Three Trios game.
 * Renders the current state of the game, including the current player's turn,
 * the game grid, and the player's hand.
 */
public class ThreeTriosModelView implements ModelView {

  private final ThreeTriosGame game;

  /**
   * Constructs a ThreeTriosModelView with the specified game.
   *
   * @param game the ThreeTriosGame to be represented by this view
   */
  public ThreeTriosModelView(ThreeTriosGame game) {
    this.game = game;
  }

  /**
   * Renders the current game state, including the current player,
   * the grid, and the current player's hand.
   */
  @Override
  public void render() {
    renderCurrentPlayer();
    renderGrid();
    renderHand();
  }

  /**
   * Displays the current player and their color.
   */
  @Override
  public void renderCurrentPlayer() {
    Player currentPlayer = game.getCurrentPlayer();
    System.out.println("Player: " + currentPlayer.getColor());
  }

  /**
   * Renders the game grid to the console.
   * Each cell is represented by:
   * - A space for holes
   * - An underscore for empty cells
   * - 'R' for cards owned by the RED player
   * - 'B' for cards owned by the BLUE player
   */
  @Override
  public void renderGrid() {
    for (int row = 0; row < game.getGrid().getRows(); row++) {
      for (int col = 0; col < game.getGrid().getCols(); col++) {
        Cell cell = game.getGrid().getCell(row, col);

        if (cell.isHole()) {
          System.out.print(" ");
        } else if (cell.isEmpty()) {
          System.out.print("_");
        } else {
          PlayerColor cardOwner = cell.getCard().getCardOwner();
          if (cardOwner == PlayerColor.RED) {
            System.out.print('R');
          } else if (cardOwner == PlayerColor.BLUE) {
            System.out.print('B');
          }
        }
      }
      System.out.println();
    }
  }

  /**
   * Displays the current player's hand, showing each card
   * along with its attack values in all directions.
   */
  @Override
  public void renderHand() {
    Player currentPlayer = game.getCurrentPlayer();
    System.out.println("Hand:");
    for (Card card : currentPlayer.getHand()) {
      StringBuilder cardInfo = new StringBuilder(card.getCard());
      for (CardDirection direction : CardDirection.values()) {
        cardInfo.append(" ").append(getAttackValueString(card.getAttackValue(direction)));
      }
      System.out.println(cardInfo);
    }
    System.out.println("-------");
  }

  /**
   * Converts an AttackValue to its string representation.
   *
   * @param value the AttackValue to convert
   * @return a string representing the AttackValue
   */
  private String getAttackValueString(AttackValue value) {
    switch (value) {
      case A:
        return "A";
      case ONE:
        return "1";
      case TWO:
        return "2";
      case THREE:
        return "3";
      case FOUR:
        return "4";
      case FIVE:
        return "5";
      case SIX:
        return "6";
      case SEVEN:
        return "7";
      case EIGHT:
        return "8";
      case NINE:
        return "9";
      default:
        return "0";
    }
  }
}
