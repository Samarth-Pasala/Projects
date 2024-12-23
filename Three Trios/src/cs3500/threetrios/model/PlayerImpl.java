package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

import cs3500.threetrios.controller.ThreeTriosFeatures;

/**
 * Represents a player in the game, implementing the Player interface.
 * The PlayerImpl class maintains the player's color and their hand of cards.
 * It provides methods to retrieve the player's color, manage their hand,
 * and perform actions related to the player's cards.
 */
public class PlayerImpl implements Player {

  private final PlayerColor color;
  private final List<Card> hand;

  /**
   * Constructs a PlayerImpl with the specified player color.
   *
   * @param color The PlayerColor representing the player's color.
   * @throws IllegalArgumentException if color is null.
   */
  public PlayerImpl(PlayerColor color) {
    this.color = color;
    this.hand = new ArrayList<>();
  }

  /**
   * Retrieves the color of the player.
   *
   * @return The PlayerColor representing the player's color.
   */
  @Override
  public PlayerColor getColor() {
    return color;
  }

  /**
   * Retrieves the list of cards currently in the player's hand.
   *
   * @return A list of Card objects representing the player's hand.
   */
  @Override
  public List<Card> getHand() {
    return new ArrayList<>(hand);
  }

  /**
   * Adds a card to the player's hand.
   *
   * @param card The Card object to be added.
   * @throws IllegalArgumentException if the card is null.
   */
  @Override
  public void addCardToHand(Card card) {
    hand.add(card);
  }

  /**
   * Removes a card from the player's hand.
   *
   * @param card The Card object to be removed.
   * @throws IllegalArgumentException if the card is null or not in the hand.
   */
  @Override
  public void removeCardFromHand(Card card) {
    hand.remove(card);
  }

  @Override
  public void addActionListener(ThreeTriosFeatures listener) {
    // nothing
  }

  @Override
  public void notifyTurn() {
    System.out.println(color + "'s turn has started!");
  }
}
