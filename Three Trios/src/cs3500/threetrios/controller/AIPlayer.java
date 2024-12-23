package cs3500.threetrios.controller;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.Strategies;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.model.ThreeTriosStrategies.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an AI player in the Three Trios game.
 * This class interacts with the game model to make decisions based on a given strategy.
 * It also provides the logic for making moves during a player's turn and communicates
 * with a listener to update the game state.
 */
public class AIPlayer implements Player, PlayerTurnListener {

  private final PlayerColor color;
  private final List<Card> hand;
  private final Strategies strategy;
  private ThreeTriosFeatures listener;

  /**
   * Constructs a MachinePlayer with the given color and strategy.
   *
   * @param color    the color of the player.
   * @param strategy the strategy the AI will use to decide moves.
   */
  public AIPlayer(PlayerColor color, Strategies strategy) {
    this.color = color;
    this.hand = new ArrayList<>();
    this.strategy = strategy;
  }

  /**
   * Returns the color of the AI player.
   *
   * @return the color of the AI player.
   */
  @Override
  public PlayerColor getColor() {
    return color;
  }

  /**
   * Returns a new list containing the AI player's current hand of cards.
   *
   * @return a new list representing the AI player's hand.
   */
  @Override
  public List<Card> getHand() {
    return new ArrayList<>(hand);
  }


  /**
   * Adds a card to the AI player's hand.
   * Throws an exception if the card is null.
   *
   * @param card the card to add to the AI player's hand.
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
   * Removes a specified card from the AI player's hand.
   * Throws an exception if the card is not found in the hand.
   *
   * @param card the card to remove from the AI player's hand.
   * @throws IllegalArgumentException if the card is not found in the hand.
   */
  @Override
  public void removeCardFromHand(Card card) {
    if (!hand.remove(card)) {
      throw new IllegalArgumentException("Card not found in hand.");
    }
  }

  /**
   * Executes the AI player's turn by calculating the best possible move
   * and applying it to the game model.
   *
   * @param model the game model to interact with.
   * @throws IllegalStateException if it is not the AI player's turn or if no valid moves are
   *     available.
   */
  public void playTurn(ThreeTriosModel model) {
    if (model.getCurrentPlayer().getColor() != this.color) {
      throw new IllegalStateException("Not this player's turn.");
    }
    Move bestMove = strategy.getMove(this);
    if (bestMove == null) {
      throw new IllegalStateException("No valid moves available.");
    }
    try {
      model.placeCard(bestMove.cardIndex, bestMove.row, bestMove.col);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Machine player attempted an invalid move.", e);
    }
  }

  /**
   * Adds an action listener to the AI player.
   * The listener is notified whenever it is the AI player's turn.
   *
   * @param listener the listener to add.
   */
  @Override
  public void addActionListener(ThreeTriosFeatures listener) {
    this.listener = listener;
  }

  /**
   * Notifies the action listener that it is the AI player's turn and executes the best move.
   * The listener is used to update the game UI by selecting the card and cell.
   */
  @Override
  public void notifyTurn() {
    System.out.println("AIPlayer (" + color + "): It's my turn!");

    Move bestMove = strategy.getMove(this);

    if (bestMove != null && listener != null) {
      listener.selectCard(bestMove.cardIndex);
      listener.selectCell(bestMove.row, bestMove.col);
    } else {
      System.out.println("AIPlayer: No valid moves available.");
    }
  }

  /**
   * Called when it is the AI player's turn.
   * The method calculates the best move using the AI player's strategy,
   * and notifies the listener to select the appropriate card and cell for the move.
   *
   * @param playerColor the color of the player whose turn it is.
   */
  @Override
  public void onTurn(PlayerColor playerColor) {
    System.out.println("AIPlayer (" + color + "): It's my turn!");

    if (playerColor.equals(this.color)) {
      Move bestMove = strategy.getMove(this);

      if (bestMove != null && listener != null) {
        System.out.println("AIPlayer: Making move at (" + bestMove.row + ", " + bestMove.col + ")");
        listener.selectCard(bestMove.cardIndex);
        listener.selectCell(bestMove.row, bestMove.col);
      } else {
        System.out.println("AIPlayer: No valid moves available.");
      }
    }
  }
}
