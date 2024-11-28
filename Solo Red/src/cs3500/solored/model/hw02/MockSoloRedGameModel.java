package cs3500.solored.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock class to simulate the behavior of the SoloRedGameModel.
 * This class is used for testing the controller.
 */
public class MockSoloRedGameModel implements RedGameModel<ConcreteCard> {

  private final StringBuilder log;
  private final List<ConcreteCard> hand;
  private final List<List<ConcreteCard>> palettes;
  private ConcreteCard canvas;
  private int numPalettes;
  private boolean isGameStarted;
  private boolean gameOver;
  private boolean playToCanvasCalled;
  private int winPalette;

  /**
   * Constructs a MockSoloRedGameModel with a StringBuilder to log method calls.
   *
   * @param log the StringBuilder used to log interactions with the model.
   */
  public MockSoloRedGameModel(StringBuilder log) {
    this.log = log;
    this.hand = new ArrayList<>();
    this.palettes = new ArrayList<>();
    this.canvas = new ConcreteCard('R', 1);
    this.isGameStarted = false;
    this.gameOver = false;
    this.playToCanvasCalled = false;
    this.winPalette = 0;
  }

  /**
   * Play the given card from the hand to the losing palette chosen.
   * The card is removed from the hand and placed at the far right end of the palette.
   *
   * @param paletteIdx    a 0-index number representing which palette to play to.
   * @param cardIdxInHand a 0-index number representing the card to play from the hand.
   */
  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    log.append(String.format("playToPalette called: paletteIdx = %d, " +
            "cardIdxInHand = %d\n", paletteIdx, cardIdxInHand));
  }

  /**
   * Play the given card from the hand to the canvas.
   * This changes the rules of the game for all palettes.
   * The method can only be called once per turn.
   *
   * @param cardIdxInHand a 0-index number representing the card to play from the hand.
   */
  @Override
  public void playToCanvas(int cardIdxInHand) {
    log.append(String.format("playToCanvas called: cardIdxInHand = %d\n", cardIdxInHand));
    this.playToCanvasCalled = true;
  }

  /**
   * Draws cards from the deck until the hand is full
   * OR until the deck is empty, whichever occurs first. Newly drawn cards
   * are added to the end of the hand (far-right conventionally).
   * SIDE-EFFECT: Allows the player to play to the canvas again.
   */
  @Override
  public void drawForHand() {
    log.append("drawForHand called\n");
  }

  /**
   * Starts the game with the given options. The deck given is used
   * to set up the palettes and hand. Modifying the deck given to this method
   * will not modify the game state in any way.
   *
   * @param deck        the cards used to set up and play the game.
   * @param shuffle     whether the deck should be shuffled prior to setting up the game.
   * @param numPalettes number of palettes in the game.
   * @param handSize    the maximum number of cards allowed in the hand.
   */
  @Override
  public void startGame(List<ConcreteCard> deck, boolean shuffle, int numPalettes, int handSize) {
    log.append(String.format("startGame called: deckSize = %d, " +
                    "shuffle = %b, numPalettes = %d, handSize = %d\n",
            deck.size(), shuffle, numPalettes, handSize));
    this.isGameStarted = true;
    this.numPalettes = numPalettes;
    this.hand.addAll(deck.subList(0, handSize));
    for (int i = 0; i < numPalettes; i++) {
      List<ConcreteCard> newPalette = new ArrayList<>();
      newPalette.add(deck.get(handSize + i));
      palettes.add(newPalette);
    }
  }

  /**
   * Returns the number of cards remaining in the deck used in the game.
   *
   * @return the number of cards in the deck.
   */
  @Override
  public int numOfCardsInDeck() {
    log.append("numOfCardsInDeck called\n");
    return 52;
  }

  /**
   * Returns the number of palettes in the running game.
   *
   * @return the number of palettes in the game.
   */
  @Override
  public int numPalettes() {
    log.append("numPalettes called\n");
    return this.numPalettes;
  }

  /**
   * Returns the index of the winning palette in the game.
   *
   * @return the 0-based index of the winning palette.
   */
  @Override
  public int winningPaletteIndex() {
    log.append("winningPaletteIndex called\n");
    return this.winPalette;
  }

  /**
   * Returns if the game is over as specified by the implementation.
   *
   * @return true if the game has ended and false otherwise.
   */
  @Override
  public boolean isGameOver() {
    log.append("isGameOver called\n");
    return this.gameOver;
  }

  /**
   * Returns if the game is won by the player as specified by the implementation.
   *
   * @return true if the game has been won or false if the game has not.
   */
  @Override
  public boolean isGameWon() {
    log.append("isGameWon called\n");
    return this.gameOver && hand.isEmpty();
  }

  /**
   * Returns a copy of the hand in the game. This means modifying the returned list
   * or the cards in the list has no effect on the game.
   */
  @Override
  public List<ConcreteCard> getHand() {
    log.append("getHand called\n");
    return new ArrayList<>(this.hand);
  }

  /**
   * Returns a copy of the specified palette. This means modifying the returned list
   * or the cards in the list has no effect on the game.
   *
   * @param paletteNum 0-based index of a particular palette.
   */
  @Override
  public List<ConcreteCard> getPalette(int paletteNum) {
    log.append(String.format("getPalette called: paletteNum = %d\n", paletteNum));
    return new ArrayList<>(palettes.get(paletteNum));
  }

  /**
   * Return the top card of the canvas.
   * Modifying this card has no effect on the game.
   *
   * @return the top card of the canvas.
   */
  @Override
  public ConcreteCard getCanvas() {
    log.append("getCanvas called\n");
    return new ConcreteCard(canvas.getColor(), canvas.getNum());
  }

  /**
   * Get a NEW list of all cards that can be used to play the game.
   * Editing this list should have no effect on the game itself.
   * Repeated calls to this method should produce a list of cards in the same order.
   * Modifying the cards in this list should have no effect on any returned list
   * or the game itself.
   *
   * @return a new list of all possible cards that can be used for the game
   */
  @Override
  public List<ConcreteCard> getAllCards() {
    log.append("getAllCards called\n");

    List<ConcreteCard> allCards = new ArrayList<>();
    allCards.addAll(hand);

    for (List<ConcreteCard> palette : palettes) {
      allCards.addAll(palette);
    }
    allCards.add(new ConcreteCard(canvas.getColor(), canvas.getNum()));
    return allCards;
  }
}
