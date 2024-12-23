package cs3500.threetrios.model;


/**
 * Represents a card in the game, containing an ID, four attack values, and ownership information.
 * Each card has an associated ID and attack values corresponding to four directions
 * (North, South, East, West).
 */
public interface Card {

  /**
   * Retrieves the unique identifier for this card.
   *
   * @return a String representing the card ID
   */
  String getCard();

  /**
   * Gets the attack value for this card in the specified direction.
   *
   * @param direction the direction for which to get the attack value
   * @return the AttackValue corresponding to the specified direction
   * @throws IllegalArgumentException if the direction is null
   */
  AttackValue getAttackValue(CardDirection direction);

  /**
   * Retrieves the color of the player who owns this card.
   *
   * @return the PlayerColor representing the card owner
   */
  PlayerColor getCardOwner();

  /**
   * Sets the owner of this card to the specified player color.
   *
   * @param color the PlayerColor to assign as the owner of the card
   */
  void setCardOwner(PlayerColor color);
}
