package cs3500.threetrios.model.rules;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardDirection;

import java.util.List;

/**
 * Represents a combination of multiple battle rules that can be applied in sequence.
 */
public class CombineRules implements BattleRule {

  private final List<BattleRule> rules;

  /**
   * Constructs a CombineRules instance that applies all given rules.
   *
   * @param rules a list of BattleRules to combine
   * @throws IllegalArgumentException if the list is null or empty
   */
  public CombineRules(List<BattleRule> rules) {
    if (rules == null || rules.isEmpty()) {
      throw new IllegalArgumentException("CombineRules requires at least one rule.");
    }
    this.rules = rules;
  }

  /**
   * Resolves a battle between two cards using the combined set of rules.
   *
   * @param cardA     the first card in the battle
   * @param cardB     the second card in the battle
   * @param direction the direction of the battle (affects how the cards are compared)
   * @return `true` if the battle is resolved by any of the combined rules, `false` otherwise
   */
  @Override
  public boolean resolveBattle(Card cardA, Card cardB, CardDirection direction) {
    for (BattleRule rule : rules) {
      if (rule.resolveBattle(cardA, cardB, direction)) {
        return true;
      }
    }
    return false;
  }
}
