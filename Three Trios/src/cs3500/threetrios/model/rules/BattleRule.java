package cs3500.threetrios.model.rules;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardDirection;

/**
 * Interface for implementing different battle rules in the game.
 */
public interface BattleRule {
  /**
   * Resolves a battle between two cards based on the active rule.
   *
   * @param cardA     the attacking card
   * @param cardB     the defending card
   * @param direction the direction of the attack
   * @return true if cardA wins and flips cardB; false otherwise
   */
  boolean resolveBattle(Card cardA, Card cardB, CardDirection direction);
}
