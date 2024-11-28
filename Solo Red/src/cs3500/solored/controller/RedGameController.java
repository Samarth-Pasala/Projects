package cs3500.solored.controller;

import java.util.List;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * Behaviors for a Card in the Game of RedSeven.
 * Any additional behaviors for cards must be made
 * creating a new interface that extends this one.
 */
public interface RedGameController {

  /**
   * plays a new game using the model.
   * @param model the model used to play the game.
   * @param deck the deck of cards used in the game.,
   * @param shuffle used to shuffle/randomize the deck.
   * @param numPalettes number of palettes.
   * @param handSize hand size in the game.
   * @param <C> The card type for the game.
   * @throws IllegalArgumentException if the provided model is null.
   * @throws IllegalStateException if the controller is unable to receive input or transmit output.
   * @throws IllegalArgumentException if the game cannot be started.
   */
  <C extends Card> void playGame(RedGameModel<C> model,
                                 List<C> deck, boolean shuffle, int numPalettes, int handSize);
}
