package cs3500.threetrios.controller;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a human player in the Three Trios game.
 * This class allows a human player to interact with the game by holding cards,
 * receiving actions from the game interface, and notifying the game when it's their turn.
 */
public class HumanPlayer implements Player {

  private final PlayerColor color;
  private final List<Card> hand;

  /**
   * Constructs a HumanPlayer with the given color.
   *
   * @param color the color of the player.
   */
  public HumanPlayer(PlayerColor color) {
    this.color = color;
    this.hand = new ArrayList<>();
  }

  /**
   * Returns the color of the human player.
   *
   * @return the color of the human player.
   */
  @Override
  public PlayerColor getColor() {
    return color;
  }

  /**
   * Returns a new list representing the human player's current hand of cards.
   * The list is a copy, so changes to the returned list will not affect the player's hand.
   *
   * @return a new list containing the player's hand.
   */
  @Override
  public List<Card> getHand() {
    return new ArrayList<>(hand);
  }

  /**
   * Adds a card to the human player's hand.
   * Throws an exception if the card is null.
   *
   * @param card the card to add to the hand.
   * @throws IllegalArgumentException if the card is null.
   */
  @Override
  public void addCardToHand(Card card) {
    if (card == null) {
      throw new IllegalArgumentException("Cannot add null card to hand.");
    }
    hand.add(card);
  }

  /**
   * Removes the specified card from the human player's hand.
   * Throws an exception if the card is not in the player's hand.
   *
   * @param card the card to remove from the hand.
   * @throws IllegalArgumentException if the card is not found in the hand.
   */
  @Override
  public void removeCardFromHand(Card card) {
    if (!hand.remove(card)) {
      throw new IllegalArgumentException("Card not found in hand.");
    }
  }

  /**
   * Adds an action listener that will be notified when it's the player's turn.
   * This listener is used to communicate actions with the game (such as selecting a card or cell).
   *
   * @param listener the action listener to add.
   */
  @Override
  public void addActionListener(ThreeTriosFeatures listener) {
    // nothing
  }

  /**
   * Notifies the player that it is their turn.
   * This method is typically called by the game to indicate that the player should take action.
   * It prints a message to the console indicating that it's the player's turn.
   */
  @Override
  public void notifyTurn() {
    System.out.println("HumanPlayer (" + color + "): It's your turn!");
  }
}
