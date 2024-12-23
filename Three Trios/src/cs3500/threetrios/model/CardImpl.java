package cs3500.threetrios.model;


import java.util.Map;


/**
 * Implementation of the Card interface.
 * This class represents a card in the game, containing an ID, a mapping of attack values
 * for each direction (North, South, East, West), and the color of the player who owns the card.
 */
public class CardImpl implements Card {

  private final String cardId;
  private final Map<CardDirection, AttackValue> attackValues;
  private PlayerColor cardOwner;

  /**
   * Constructs a CardImpl instance with the specified card ID and attack values.
   *
   * @param cardId       the unique identifier for this card
   * @param attackValues a map of attack values for each direction
   * @throws ArrayIndexOutOfBoundsException if card is null or empty
   * @throws ArrayIndexOutOfBoundsException if cards don't have all attack values.
   */
  public CardImpl(String cardId, Map<CardDirection, AttackValue> attackValues) {
    if (cardId == null || cardId.isEmpty()) {
      throw new ArrayIndexOutOfBoundsException("Name cannot be null or empty.");
    }
    if (attackValues == null || attackValues.size() != 4) {
      throw new ArrayIndexOutOfBoundsException("Attack values must not be null and must contain " +
              "exactly 4 values.");
    }
    for (CardDirection direction : CardDirection.values()) {
      if (!attackValues.containsKey(direction)) {
        throw new ArrayIndexOutOfBoundsException("Missing attack value for " +
                "direction: " + direction);
      }
      AttackValue value = attackValues.get(direction);
      if (value.getValue() < 0) {
        throw new IllegalArgumentException("Attack value must be non-negative for " +
                "direction: " + direction);
      }
    }

    this.cardId = cardId;
    this.attackValues = attackValues;
    this.cardOwner = null;
  }

  /**
   * Retrieves the unique identifier for this card.
   *
   * @return a String representing the card ID
   */
  @Override
  public String getCard() {
    return cardId;
  }

  /**
   * Gets the attack value for this card in the specified direction.
   *
   * @param direction the direction for which to get the attack value
   * @return the AttackValue corresponding to the specified direction
   * @throws IllegalArgumentException if the direction is null
   */
  @Override
  public AttackValue getAttackValue(CardDirection direction) {
    return attackValues.get(direction);
  }

  /**
   * Retrieves the color of the player who owns this card.
   *
   * @return the PlayerColor representing the card owner
   */
  @Override
  public PlayerColor getCardOwner() {
    return cardOwner;
  }

  /**
   * Sets the owner of this card to the specified player color.
   *
   * @param color the PlayerColor to assign as the owner of the card
   */
  @Override
  public void setCardOwner(PlayerColor color) {
    this.cardOwner = color;
  }

  /**
   * Returns a string representation of this card.
   *
   * @return a string representing the card's ID
   */
  @Override
  public String toString() {
    return this.cardId;
  }
}
