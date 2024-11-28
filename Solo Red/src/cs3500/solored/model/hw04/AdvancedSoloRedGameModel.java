package cs3500.solored.model.hw04;

import java.util.Random;

import cs3500.solored.model.hw02.ConcreteCard;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * A class that implements the advanced version of the SoloRedGameModel.
 * In this version, additional drawing rules are applied when playing to the canvas
 * and palette.
 */
public class AdvancedSoloRedGameModel extends SoloRedGameModel {

  private boolean extraDrawAllowed = false;

  /**
   * Default constructor that sets variables to default values for the advanced game.
   */
  public AdvancedSoloRedGameModel() {
    super();
  }

  /**
   * Constructor that initializes a random so that deck can be in different orders/shuffled.
   *
   * @param rand represents random.
   */
  public AdvancedSoloRedGameModel(Random rand) {
    super(rand);
  }

  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    super.playToPalette(paletteIdx, cardIdxInHand);

    if (extraDrawAllowed && !this.deck.isEmpty()) {
      drawForHand(2);
    } else {
      drawForHand(1);
    }

    extraDrawAllowed = false;
  }

  @Override
  public void playToCanvas(int cardIdxInHand) {
    if (!this.isGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }

    super.playToCanvas(cardIdxInHand);

    ConcreteCard playedCard = this.canvas;
    int winningPaletteSize = getWinningPaletteSize();

    if (playedCard.getNum() > winningPaletteSize) {
      extraDrawAllowed = true;
    }
  }

  /**
   * gets the winning palette size to determine how many cards can be drawn.
   */
  private int getWinningPaletteSize() {
    if (this.winPalette == -1 || this.palettes.isEmpty()) {
      throw new IllegalStateException("Game has not started yet or there is no winning palette");
    }
    return this.palettes.get(this.winPalette).size();
  }

  /**
   * refactors the drawForHand method from the original model to determine the number of cards
   * that can be drawn in the advanced version of the game.
   *
   * @param numToDraw represents the number of cards to draw.
   */
  private void drawForHand(int numToDraw) {
    for (int i = 0; i < numToDraw; i++) {
      if (!this.deck.isEmpty() && extraDrawAllowed && this.hand.size() < this.handSize) {
        this.hand.add(this.deck.remove(0));
      }
    }
  }

}