package cs3500.threetrios.model.rules;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardDirection;
import cs3500.threetrios.model.AttackValue;

/**
 * A battle rule where the card with the higher attack value wins the battle.
 */
public class StandardRule implements BattleRule {

  @Override
  public boolean resolveBattle(Card cardA, Card cardB, CardDirection direction) {
    AttackValue attackValueA = cardA.getAttackValue(direction);
    CardDirection oppositeDirection = getOppositeDirection(direction);
    AttackValue attackValueB = cardB.getAttackValue(oppositeDirection);

    if (attackValueA.getValue() > attackValueB.getValue()) {
      cardB.setCardOwner(cardA.getCardOwner());
      return true;
    }
    return false;
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
        throw new IllegalArgumentException("Invalid direction.");
    }
  }
}
