package cs3500.threetrios.model;

import java.util.List;

import cs3500.threetrios.controller.ThreeTriosFeatures;

/**
 * Represents a player in the game.
 * The Player interface defines the essential attributes and behaviors of a player,
 * including their color, hand of cards, and methods to manage their hand.
 */
public interface Player {

  /**
   * Retrieves the color of the player.
   *
   * @return The PlayerColor representing the player's color.
   */
  PlayerColor getColor();

  /**
   * Retrieves the list of cards currently in the player's hand.
   *
   * @return A list of Card objects representing the player's hand.
   */
  List<Card> getHand();

  /**
   * Adds a card to the player's hand.
   *
   * @param card The Card object to be added.
   * @throws IllegalArgumentException if the card is null.
   */
  void addCardToHand(Card card);

  /**
   * Removes a card from the player's hand.
   *
   * @param card The Card object to be removed.
   * @throws IllegalArgumentException if the card is null or not in the hand.
   */
  void removeCardFromHand(Card card);

  /**
   * Registers an action listener to handle the player's actions.
   *
   * @param listener The listener to register.
   */
  void addActionListener(ThreeTriosFeatures listener);

  /**
   * Notifies the player that it is their turn.
   */
  void notifyTurn();
}
