package cs3500.solored;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.solored.model.hw02.ConcreteCard;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.model.hw04.RedGameCreator;

/**
 * abstract class that takes the original models test and applies to the advanced version
 * of the game as well to see if they pass in the both scenarios.
 * refers to the original model tests by calling basic.
 */
public class TestAbstractModel {

  protected RedGameCreator.GameType gameType = RedGameCreator.GameType.BASIC;

  /**
   * tests that the deck size is 35.
   */
  @Test
  public void getAllCards() {
    List<ConcreteCard> allCards = new ArrayList<>();
    allCards.add(new ConcreteCard('R', 1));
    allCards.add(new ConcreteCard('R', 2));
    allCards.add(new ConcreteCard('R', 3));
    allCards.add(new ConcreteCard('R', 4));
    allCards.add(new ConcreteCard('R', 5));
    allCards.add(new ConcreteCard('R', 6));
    allCards.add(new ConcreteCard('R', 7));
    allCards.add(new ConcreteCard('O', 1));
    allCards.add(new ConcreteCard('O', 2));
    allCards.add(new ConcreteCard('O', 3));
    allCards.add(new ConcreteCard('O', 4));
    allCards.add(new ConcreteCard('O', 5));
    allCards.add(new ConcreteCard('O', 6));
    allCards.add(new ConcreteCard('O', 7));
    allCards.add(new ConcreteCard('B', 1));
    allCards.add(new ConcreteCard('B', 2));
    allCards.add(new ConcreteCard('B', 3));
    allCards.add(new ConcreteCard('B', 4));
    allCards.add(new ConcreteCard('B', 5));
    allCards.add(new ConcreteCard('B', 6));
    allCards.add(new ConcreteCard('B', 7));
    allCards.add(new ConcreteCard('I', 1));
    allCards.add(new ConcreteCard('I', 2));
    allCards.add(new ConcreteCard('I', 3));
    allCards.add(new ConcreteCard('I', 4));
    allCards.add(new ConcreteCard('I', 5));
    allCards.add(new ConcreteCard('I', 6));
    allCards.add(new ConcreteCard('I', 7));
    allCards.add(new ConcreteCard('V', 1));
    allCards.add(new ConcreteCard('V', 2));
    allCards.add(new ConcreteCard('V', 3));
    allCards.add(new ConcreteCard('V', 4));
    allCards.add(new ConcreteCard('V', 5));
    allCards.add(new ConcreteCard('V', 6));
    allCards.add(new ConcreteCard('V', 7));
    SoloRedGameModel model = new SoloRedGameModel();
    Assert.assertEquals(35, model.getAllCards().size());
    for (char color : List.of('R', 'O', 'B', 'I', 'V')) {
      for (int i = 1; i <= 7; i++) {
        Assert.assertTrue(allCards.contains(new ConcreteCard(color, i)));
      }
    }
  }

  /**
   * test that modifying list has no effect on method.
   */
  @Test
  public void testModifiedListForAllCards() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> allCards = model.getAllCards();

    allCards.clear();

    List<ConcreteCard> newAllCards = model.getAllCards();
    Assert.assertEquals(35, newAllCards.size());

    for (char color : List.of('R', 'O', 'B', 'I', 'V')) {
      for (int i = 1; i <= 7; i++) {
        Assert.assertTrue(newAllCards.contains(new ConcreteCard(color, i)));
      }
    }
  }

  /**
   * test that order is same for all cards and doesn't randomly change.
   */
  @Test
  public void testConsistentOrderForAllCards() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> allCards1 = model.getAllCards();
    List<ConcreteCard> allCards2 = model.getAllCards();
    Assert.assertEquals(allCards1, allCards2);
  }

  /**
   * Makes sure that the correct card (top card of the deck) is returned.
   */
  @Test
  public void testReturnCorrectCard() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 3, 7);
    ConcreteCard card = model.getCanvas();
    Assert.assertEquals('R', card.getColor());
    Assert.assertEquals(1, card.getNum());
  }

  /**
   * Checks if IllegalStateException is thrown when game is over.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetCanvasThrowsExceptionWhenGameOver() {
    SoloRedGameModel model = new SoloRedGameModel();
    while (model.numOfCardsInDeck() > 0) {
      model.drawForHand();
    }
    model.getCanvas();
  }

  /**
   * Checks if IllegalStateException is thrown when game is not started.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetCanvasThrowsExceptionWhenGameNotStarted() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.getCanvas();
  }

  /**
   * tests method when game is not started.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetPaletteGameNotStarted() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.getPalette(0);
  }

  /**
   * tests method when index is negative.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPaletteNegativeIndex() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 3, 7);
    model.getPalette(-1);
  }

  /**
   * tests method when index is out of range.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPaletteOutOfRangeIndex() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 3, 7);
    model.getPalette(7);
  }


  /**
   * tests that the getHand method works correctly and that modifying the list has no effect.
   */
  @Test
  public void testGetHandCorrectly() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('R', 4));
    deck.add(new ConcreteCard('O', 5));
    deck.add(new ConcreteCard('B', 6));
    deck.add(new ConcreteCard('I', 1));
    deck.add(new ConcreteCard('V', 2));
    model.startGame(deck, false, 3, 5);
    List<ConcreteCard> hand = model.getHand();
    Assert.assertEquals(5, hand.size());
    hand.remove(0);
    Assert.assertEquals(5, model.getHand().size());
  }

  /**
   * tests if hand can be seen even when game isn't started.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetHandGameNotStarted() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('R', 4));
    deck.add(new ConcreteCard('O', 5));
    deck.add(new ConcreteCard('B', 6));
    deck.add(new ConcreteCard('I', 1));
    deck.add(new ConcreteCard('V', 2));
    SoloRedGameModel model = new SoloRedGameModel();
    model.playToCanvas(0);
    model.getHand();
  }

  /**
   * checks playToPalette when game has not started.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlayToPaletteWhenGameNotStarted() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.playToPalette(0, 0);
  }

  /**
   * checks method to make sure invalid index isn't played.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlayToInvalidCardIndex() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 3, 7);
    model.playToPalette(-1, 0);
  }

  /**
   * checks to make sure invalid card index can't be in players hand.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlayToPaletteInvalidIndexInHand() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 3, 7);
    model.playToPalette(0, -1);
  }

  /**
   * checks to make sure you can't play to a winning palette.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlayToPaletteWinningPalette() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 3, 7);
    int winningPaletteIdx = model.winningPaletteIndex();
    model.playToPalette(winningPaletteIdx, 0);
  }

  /**
   * test to make sure you can't play after game is over.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlayAfterGameEnds() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 3, 7);
    while (!model.isGameOver()) {
      model.playToPalette(0, 0);
    }
    model.playToPalette(0, 0);
  }

  /**
   * Tests a valid case of playing to the palette.
   */
  @Test
  public void testPlayToPaletteValid() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();

    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 6));
    deck.add(new ConcreteCard('O', 7));
    deck.add(new ConcreteCard('B', 5));
    deck.add(new ConcreteCard('I', 1));
    model.startGame(deck, false, 3, 5);
    model.playToPalette(0, 0);
    Assert.assertEquals(4, model.getHand().size());
  }

  /**
   * test start game when game has already started.
   */
  @Test(expected = IllegalStateException.class)
  public void testStartGameWhenAlreadyStarted() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('R', 4));
    deck.add(new ConcreteCard('O', 5));
    deck.add(new ConcreteCard('B', 6));
    deck.add(new ConcreteCard('I', 1));
    deck.add(new ConcreteCard('V', 2));
    model.startGame(deck, false, 3, 5);
    model.startGame(deck, false, 3, 5);
  }

  /**
   * test to make sure exception is thrown when deck is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(null, false, 3, 5);
  }

  /**
   * tests to make sure exception is thrown when deck is empty.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameEmptyDeck() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    model.startGame(deck, false, 3, 5);
  }

  /**
   * tests to make sure all cards are unique.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNonUniqueCards() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('R', 1)); // Duplicate
    model.startGame(deck, false, 3, 5);
  }

  /**
   * tests to make sure game can't be started when there are not enough cards.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNotEnoughards() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    model.startGame(deck, false, 3, 5);
  }

  /**
   * tests to make sure you can't start game without right amount of palettes.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidPalette() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    model.startGame(deck, false, 1, 5);
  }

  /**
   * tests to make sure you don't have an invalid hand size.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidHandSize() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    model.startGame(deck, false, 3, 0);
  }

  /**
   * tests to make sure startGame works properly.
   */
  @Test
  public void testStartGameValidStart() {
    SoloRedGameModel model = new SoloRedGameModel();
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 6));
    deck.add(new ConcreteCard('O', 7));
    deck.add(new ConcreteCard('B', 5));
    deck.add(new ConcreteCard('I', 1));
    model.startGame(deck, false, 3, 5);
    Assert.assertEquals(3, model.numPalettes());
    Assert.assertEquals(5, model.getHand().size());
    Assert.assertEquals(1, model.numOfCardsInDeck());
    Assert.assertEquals(2, model.winningPaletteIndex());
  }

  /**
   * tests to make sure playToCanvas functions accordingly.
   */
  @Test
  public void testPlayToCanvasCorrectly() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('O', 1));
    deck.add(new ConcreteCard('B', 1));
    deck.add(new ConcreteCard('R', 3));
    deck.add(new ConcreteCard('B', 2));
    deck.add(new ConcreteCard('R', 4));
    deck.add(new ConcreteCard('O', 5));
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(deck, false, 2, 3);
    model.playToCanvas(1);
    Assert.assertEquals('B', model.getCanvas().getColor());
    Assert.assertEquals(1, model.getCanvas().getNum());
    Assert.assertEquals(2, model.getHand().size());
  }

  /**
   * tests to make sure you can't play to canvas with only one card in hand.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlayToCanvasWithOneCardInHand() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 1));
    deck.add(new ConcreteCard('V', 2));
    deck.add(new ConcreteCard('I', 2));
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(deck, false, 2, 2);
    model.playToCanvas(0);
    model.playToCanvas(0);
  }

  /**
   * tests to make sure you can't play an invalid index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlayToCanvasInvalidCardIndex() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 1));
    deck.add(new ConcreteCard('V', 2));
    deck.add(new ConcreteCard('I', 2));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3); // 2 palettes, hand size is 3
    model.playToCanvas(3); // Invalid index, should throw exception
  }

  /**
   * tests to make sure drawing works properly.
   */
  @Test
  public void testDrawForHandSuccessfully() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 5));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    model.drawForHand();
    Assert.assertEquals(3, model.getHand().size());
    Assert.assertEquals(1, model.numOfCardsInDeck());
  }

  /**
   * tests to make sure you can't draw when game hasn't started.
   */
  @Test(expected = IllegalStateException.class)
  public void testDrawForHandWhenGameNotStarted() {
    List<ConcreteCard> deck = new ArrayList<>();
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.drawForHand();
  }

  /**
   * tests to make sure you can't draw when deck is empty.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDrawForHandWhenDeckIsEmpty() {
    List<ConcreteCard> deck = new ArrayList<>();
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    model.drawForHand();
    Assert.assertEquals(0, model.getHand().size());
  }

  /**
   * tests to make sure you can't get the number of cards in deck when game hasn't started.
   */
  @Test(expected = IllegalStateException.class)
  public void testNumOfCardsInDeckWhenGameNotStarted() {
    List<ConcreteCard> deck = new ArrayList<>();
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.numOfCardsInDeck();
  }

  /**
   * tests to make sure number of cards in deck are right after starting game.
   */
  @Test
  public void testNumOfCardsInDeckAfterStartingGame() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('B', 1));
    deck.add(new ConcreteCard('R', 2));
    deck.add(new ConcreteCard('O', 3));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    Assert.assertEquals(1, model.numOfCardsInDeck());
  }

  /**
   * tests to make sure appropriate cards in deck after drawing occurs.
   */
  @Test
  public void testNumOfCardsInDeckAfterAllCardsDrawn() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    model.drawForHand();
    Assert.assertEquals(0, model.numOfCardsInDeck());
  }

  /**
   * tests number of palettes after game has started.
   */
  @Test
  public void testNumPalettesAfterStartingGame() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 5));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 3, 3);
    Assert.assertEquals(3, model.numPalettes());
  }

  /**
   * tests that you can't get numPalettes after game has started.
   */
  @Test(expected = IllegalStateException.class)
  public void testNumPalettesWhenGameNotStarted() {
    List<ConcreteCard> deck = new ArrayList<>();
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.numPalettes();
  }

  /**
   * tests winning palette after game has started.
   */
  @Test
  public void testWinningPaletteIndexAfterGameStarts() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 5));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    Assert.assertEquals(1, model.winningPaletteIndex());
  }

  /**
   * tests that you can't get winning palette index when game hasn't started.
   */
  @Test(expected = IllegalStateException.class)
  public void testWinningPaletteIndexWhenGameNotStarted() {
    List<ConcreteCard> deck = new ArrayList<>();
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.winningPaletteIndex();
  }

  /**
   * make sure that a copy of list is returned.
   */
  @Test
  public void testGetHandReturnsCopy() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('V', 1));
    deck.add(new ConcreteCard('R', 2));
    deck.add(new ConcreteCard('O', 3));
    deck.add(new ConcreteCard('O', 4));
    deck.add(new ConcreteCard('B', 5));
    deck.add(new ConcreteCard('B', 6));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    List<ConcreteCard> handCopy = model.getHand();
    Assert.assertEquals(3, handCopy.size());
    Assert.assertEquals(3, model.getHand().size());
    handCopy.remove(0);
    Assert.assertEquals(3, model.getHand().size());
  }

  /**
   * can't get hand when game hasn't started.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetHandThrowsExceptionWhenGameNotStarted() {
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.getHand();
  }

  /**
   * makes sure get palette returns a copy of list.
   */
  @Test
  public void testGetPaletteReturnsCopy() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 6));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    List<ConcreteCard> paletteCopy = model.getPalette(0);
    Assert.assertEquals(1, paletteCopy.size());
    paletteCopy.remove(0);
    Assert.assertEquals(1, model.getPalette(0).size());
  }

  /**
   * tests the get palette throws exceptions when negative index given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPaletteThrowsExceptionForNegativeIndex() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 6));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 2);
    model.getPalette(-1);
  }

  /**
   * tests get palette throws exception when invalid index is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPaletteThrowsExceptionForInvalidIndex() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 6));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 2);
    model.getPalette(2);
  }

  /**
   * tests the exception of the method where game isn't even started.
   */
  @Test(expected = IllegalStateException.class)
  public void testIsGameOverException() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 6));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    Assert.assertFalse(model.isGameOver());
  }

  /**
   * tests the case where there are still moves to be played in the game.
   */
  @Test
  public void testIsGameOverNotTrue() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 5));
    SoloRedGameModel model = new SoloRedGameModel(new Random());
    model.startGame(deck, false, 2, 3);
    model.playToPalette(0, 1);
    Assert.assertFalse(model.isGameOver());
  }

  /**
   * tests the case where both the hand and deck are empty.
   */
  @Test
  public void testIsGameOverTrue() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 5));
    SoloRedGameModel model = new SoloRedGameModel(new Random(1));
    model.startGame(deck, false, 2, 2);
    model.playToPalette(0, 1);
    model.playToPalette(1, 0);
    Assert.assertTrue(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testIsGameWonWhenGameNotStarted() {
    List<ConcreteCard> deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 5));
    SoloRedGameModel model = new SoloRedGameModel(new Random(1));
    model.isGameWon();
  }
}
