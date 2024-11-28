package cs3500.solored.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Class that represents the model for the game.
 * Has methods for the various implementations.
 */
public class SoloRedGameModel implements RedGameModel<ConcreteCard> {

  protected ConcreteCard canvas;
  private int numPalettes;
  protected int handSize;
  protected List<List<ConcreteCard>> palettes;
  private ArrayList<ConcreteCard> cards;
  protected List<ConcreteCard> deck = new ArrayList<>();
  protected List<ConcreteCard> hand;

  protected boolean isGameStarted = false;
  private boolean gameOver = false;
  private boolean gameWon = false;
  private boolean gameLost = false;
  protected int winPalette = 0;
  private boolean playToCanvasCalled = false;
  private Random rand;

  /**
   * Default constructor that sets variables to default values.
   */
  public SoloRedGameModel() {
    this.canvas = new ConcreteCard('R', 1);
    this.deck = new ArrayList<>();
    this.hand = new ArrayList<>();
    this.rand = new Random();
  }

  /**
   * Constructor that takes in a random for getting a random order of the deck.
   *
   * @param random to randomize the order.
   */
  public SoloRedGameModel(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null");
    }
    this.canvas = new ConcreteCard('R', 1);
    this.deck = new ArrayList<>();
    this.hand = new ArrayList<>();
    this.rand = random;
  }

  /**
   * Play the given card from the hand to the losing palette chosen.
   * The card is removed from the hand and placed at the far right
   * end of the palette.
   *
   * @param paletteIdx    a 0-index number representing which palette to play to
   * @param cardIdxInHand a 0-index number representing the card to play from the hand
   * @throws IllegalStateException    if the game has not started or the game is over
   * @throws IllegalArgumentException if paletteIdx < 0 or more than the number of palettes
   * @throws IllegalArgumentException if cardIdxInHand < 0
   *                                  or greater/equal to the number of cards in hand
   * @throws IllegalStateException    if the palette referred to by paletteIdx is winning
   */
  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    if (!this.isGameStarted || this.gameOver) {
      throw new IllegalStateException("Game not started");
    }
    if (paletteIdx < 0 || paletteIdx > this.palettes.size() - 1) {
      throw new IllegalArgumentException("Invalid palette index");
    }
    if (cardIdxInHand < 0 || cardIdxInHand > this.hand.size() - 1) {
      throw new IllegalArgumentException("Invalid card index");
    }
    if (paletteIdx == this.winPalette) {
      throw new IllegalStateException("Palette referred to is winning.");
    }

    try {
      int prevWinner = this.winPalette;
      ConcreteCard currentCard = this.hand.get(cardIdxInHand);
      this.palettes.get(paletteIdx).add(currentCard);
      this.hand.remove(currentCard);
      int newWinner = getWinningPalette();

      if (this.deck.isEmpty() && this.hand.isEmpty()) {
        this.gameOver = true;
        this.gameWon = true;
      }

      if (newWinner == prevWinner || newWinner != paletteIdx) {
        this.gameOver = true;
        this.gameLost = true;
      }
      
      this.winPalette = newWinner;
      this.playToCanvasCalled = false;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("Not a valid index.");
    }
  }

  /**
   * Play the given card from the hand to the canvas.
   * This changes the rules of the game for all palettes.
   * The method can only be called once per turn.
   *
   * @param cardIdxInHand a 0-index number representing the card to play from the hand
   * @throws IllegalStateException    if the game has not started or the game is over
   * @throws IllegalArgumentException if cardIdxInHand < 0
   *                                  or greater/equal to the number of cards in hand
   * @throws IllegalStateException    if this method was already called once in a given turn
   * @throws IllegalStateException    if there is exactly one card in hand
   */
  @Override
  public void playToCanvas(int cardIdxInHand) {
    if (!this.isGameStarted || this.gameOver) {
      throw new IllegalStateException("Can't play since game hasn't started or game is over!");
    }
    if (cardIdxInHand < 0 || cardIdxInHand > this.hand.size() - 1) {
      throw new IllegalArgumentException("Card index out of bounds");
    }
    if (playToCanvasCalled) {
      throw new IllegalStateException("Can't play since play to game has already been called");
    }
    if (this.hand.size() == 1) {
      throw new IllegalStateException("Can't play since hand size is 1");
    }
    ConcreteCard currentCard = this.hand.get(cardIdxInHand);
    this.hand.remove(currentCard);
    this.canvas.setColor(currentCard.getColor());
    this.winPalette = getWinningPalette();
    playToCanvasCalled = true;

    if (this.deck.isEmpty() && this.hand.isEmpty()) {
      this.gameOver = true;
      this.gameWon = true;
    }

  }

  /**
   * Draws cards from the deck until the hand is full
   * OR until the deck is empty, whichever occurs first. Newly drawn cards
   * are added to the end of the hand (far-right conventionally).
   * SIDE-EFFECT: Allows the player to play to the canvas again.
   *
   * @throws IllegalStateException if the game has not started or the game is over
   */
  @Override
  public void drawForHand() {
    if (!this.isGameStarted || this.gameOver) {
      throw new IllegalStateException("Can't draw since game hasn't started or game is over!");
    }
    while (hand.size() < this.handSize && !this.deck.isEmpty()) {
      this.hand.add(this.deck.get(0));
      this.deck.remove(0);
    }
  }

  /**
   * Starts the game with the given options. The deck given is used
   * to set up the palettes and hand. Modifying the deck given to this method
   * will not modify the game state in any way.
   *
   * @param deck        the cards used to set up and play the game
   * @param shuffle     whether the deck should be shuffled prior to setting up the game
   * @param numPalettes number of palettes in the game
   * @param handSize    the maximum number of cards allowed in the hand
   * @throws IllegalStateException    if the game has started or the game is over
   * @throws IllegalArgumentException if numPalettes < 2 or handSize <= 0
   * @throws IllegalArgumentException if deck's size is not large enough to setup the game
   * @throws IllegalArgumentException if deck has non-unique cards or null cards
   */
  @Override
  public void startGame(List<ConcreteCard> deck, boolean shuffle, int numPalettes, int handSize) {
    if (deck == null || deck.isEmpty() || !checkUnique(deck)) {
      throw new IllegalArgumentException("Deck can't be null, empty, or have non-unique cards");
    }
    if (this.isGameStarted || this.gameOver) {
      throw new IllegalStateException("Game can't have started or be over");
    }
    if (numPalettes < 2 || handSize <= 0) {
      throw new IllegalArgumentException("Number of palettes and hand size must be greater than 0");
    }
    if (deck.size() < handSize + numPalettes) {
      throw new IllegalArgumentException("Deck size must be greater than card size");
    }

    this.numPalettes = numPalettes;
    this.isGameStarted = true;
    this.deck = new ArrayList<>(deck);
    if (shuffle) {
      this.shuffle();
    }

    //Starts the game
    this.gameOver = false;

    this.palettes = new ArrayList<>();
    for (int i = 0; i < numPalettes; i++) {
      List<ConcreteCard> makeNewPalette = new ArrayList<>();
      makeNewPalette.add(this.deck.remove(0));
      this.palettes.add(makeNewPalette);
    }
    this.handSize = handSize;
    this.drawForHand();
    this.winPalette = getWinningPalette();
  }

  /**
   * Checks if the cards in the deck are all unique.
   */
  private boolean checkUnique(List<ConcreteCard> deck) {
    Set<ConcreteCard> uniqueCards = new HashSet<>(deck);
    return uniqueCards.size() == deck.size();
  }

  /**
   * Checks if the deck is shuffled.
   */
  private void shuffle() {
    Collections.shuffle(this.deck, rand);
    boolean isShuffled = true;
  }

  /**
   * The palette with the highest card wins.
   */
  private int redRule() {
    List<ConcreteCard> highestCard = new ArrayList<>(this.palettes.size());
    for (List<ConcreteCard> currentPalette : this.palettes) {
      highestCard.add(currentPalette.get(this.getHighestIndex(currentPalette)));
    }
    return getHighestIndex(highestCard);
  }

  /**
   * The palette with the most of a single number wins.
   */
  private int orangeRule() {
    List<List<ConcreteCard>> commonNums = new ArrayList<>(this.palettes.size());
    for (List<ConcreteCard> palette : this.palettes) {
      commonNums.add(getCommonNums(palette));
    }

    List<ConcreteCard> largest = null;

    for (List<ConcreteCard> palette : commonNums) {
      if (largest == null || palette.size() > largest.size()) {
        largest = palette;
      } else if (palette.size() == largest.size()) {
        if (palette.get(0).checkGreater(largest.get(0))) {
          largest = palette;
        }
      }
    }
    return commonNums.indexOf(largest);
  }

  /**
   * Finds highest number of duplicate cards in a given list.
   *
   * @param cards List of Concrete Cards.
   */
  private List<ConcreteCard> getCommonNums(List<ConcreteCard> cards) {
    List<ConcreteCard> longest = new ArrayList<>();

    for (ConcreteCard card : cards) {
      List<ConcreteCard> currentList = new ArrayList<>();
      int currentNum = card.getNum();

      for (ConcreteCard innerCard : cards) {
        if (innerCard.getNum() == currentNum) {
          currentList.add(innerCard);
        }
      }

      if (currentList.size() > longest.size()) {
        longest = currentList;
      } else if (currentList.size() == longest.size()) {
        if (longest.isEmpty()) {
          longest = currentList;
        } else {
          int longestNum = longest.get(0).getNum();
          if (currentNum > longestNum) {
            longest = currentList;
          }
        }
      }
    }
    return longest;
  }

  /**
   * The palette with the most cards of different colors wins.
   */
  private int blueRule() {
    List<List<ConcreteCard>> diffColors = new ArrayList<>();

    for (List<ConcreteCard> palette : this.palettes) {
      diffColors.add(differentColors(palette));
    }

    List<ConcreteCard> largest = null;
    int largestIndex = -1;

    for (int i = 0; i < diffColors.size(); i++) {
      List<ConcreteCard> palette = diffColors.get(i);
      if (largest == null || palette.size() > largest.size()) {
        largest = palette;
        largestIndex = i;
      } else if (palette.size() == largest.size()) {
        ConcreteCard highestInList = largest.get(getHighestIndex(largest));
        ConcreteCard highestInPalette = palette.get(getHighestIndex(palette));

        if (highestInPalette.checkGreater(highestInList)) {
          largest = palette;
          largestIndex = i;
        }
      }
    }
    return largestIndex;
  }

  /**
   * Finds all the different colors for the blue rule.
   *
   * @param cards List of Concrete Cards.
   */
  private List<ConcreteCard> differentColors(List<ConcreteCard> cards) {
    List<ConcreteCard> uniqueColors = new ArrayList<>();
    for (ConcreteCard card : cards) {
      boolean found = false;
      for (ConcreteCard uniqueCard : uniqueColors) {
        if (uniqueCard.getColor() == card.getColor()) {
          found = true;
          // Check if the current card is greater than the existing one
          if (!uniqueCard.checkGreater(card)) {
            // Replace with the current card if it's greater
            uniqueColors.set(uniqueColors.indexOf(uniqueCard), card);
          }
          break;
        }
      }
      if (!found) {
        uniqueColors.add(card); // Add new color if not found
      }
    }
    return uniqueColors;
  }

  /**
   * The palette with the cards that form the longest run wins.
   * A run is an unbroken increasing sequence of consecutive numbers after sorting.
   */
  private int indigoRule() {
    List<List<ConcreteCard>> longestRuns = new ArrayList<>();

    for (List<ConcreteCard> palette : this.palettes) {
      longestRuns.add(getLongestRun(palette));
    }

    List<ConcreteCard> longestRun = null;

    for (List<ConcreteCard> palette : longestRuns) {
      if (longestRun == null || palette.size() > longestRun.size()) {
        longestRun = palette;
      } else if (palette.size() == longestRun.size()) {
        ConcreteCard highestInLargest = longestRun.get(getHighestIndex(longestRun));
        ConcreteCard highestInPalette = palette.get(getHighestIndex(palette));

        if (highestInPalette.checkGreater(highestInLargest)) {
          longestRun = palette;
        }
      }
    }

    return longestRuns.indexOf(longestRun);
  }

  /**
   * Gets the longest run for the Indigo rule.
   *
   * @param cards is a list of Concrete Cards.
   */
  private List<ConcreteCard> getLongestRun(List<ConcreteCard> cards) {
    List<ConcreteCard> sortedCards = new ArrayList<>(cards);
    Collections.sort(sortedCards, new ConcreteCardComparator());

    List<ConcreteCard> curr = new ArrayList<>();
    List<ConcreteCard> max = new ArrayList<>();

    for (ConcreteCard card : sortedCards) {
      if (curr.isEmpty() || curr.get(curr.size() - 1).getNum() + 1 == card.getNum()) {
        curr.add(card);
      } else {
        if (curr.size() > max.size() ||
                (curr.size() == max.size() && curr.get(0).checkGreater(max.get(0)))) {
          max = new ArrayList<>(curr);
        }
        curr.clear();
        curr.add(card);
      }
    }

    if (curr.size() > max.size() ||
            (curr.size() == max.size() && curr.get(0).checkGreater(max.get(0)))) {
      max = new ArrayList<>(curr);
    }

    return max;
  }

  /**
   * Compares two cards' numbers.
   */
  private static class ConcreteCardComparator implements Comparator<ConcreteCard> {
    @Override
    public int compare(ConcreteCard c1, ConcreteCard c2) {
      return Integer.compare(c1.getNum(), c2.getNum());
    }
  }

  /**
   * The palette with the most cards below the value of 4 wins.
   */
  private int violetRule() {
    List<List<ConcreteCard>> belowFour = new ArrayList<>();

    for (List<ConcreteCard> palette : this.palettes) {
      belowFour.add(getNumbersBelowFour(palette));
    }

    List<ConcreteCard> largest = null;

    for (List<ConcreteCard> palette : belowFour) {
      if (largest == null || palette.size() > largest.size()) {
        largest = palette;
      } else if (palette.size() == largest.size()) {
        ConcreteCard highestInList = largest.get(getHighestIndex(largest));
        ConcreteCard highestInPalette = palette.get(getHighestIndex(palette));

        if (highestInPalette.checkGreater(highestInList)) {
          largest = palette;
        }
      }
    }
    return belowFour.indexOf(largest);
  }

  /**
   * Helper for Violet rule to get all cards below four.
   *
   * @param cards list of Concrete Cards.
   */
  private List<ConcreteCard> getNumbersBelowFour(List<ConcreteCard> cards) {
    List<ConcreteCard> cardsBelowFour = new ArrayList<>();
    for (ConcreteCard card : cards) {
      if (card.getNum() < 4) {
        cardsBelowFour.add(card);
      }
    }
    return cardsBelowFour;
  }

  /**
   * finds index of highest value card in list.
   *
   * @param cards list of Concrete Cards.
   */
  private int getHighestIndex(List<ConcreteCard> cards) {
    ConcreteCard highestCard = cards.get(0);
    if (cards.size() > 1) {
      for (int i = 1; i < cards.size(); i++) {
        if (cards.get(i).checkGreater(highestCard)) {
          highestCard = cards.get(i);
        }
      }
    }
    return cards.indexOf(highestCard);
  }

  /**
   * Returns the number of cards remaining in the deck used in the game.
   *
   * @return the number of cards in the deck
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public int numOfCardsInDeck() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game is not started");
    }
    return this.deck.size();
  }

  /**
   * Returns the number of palettes in the running game.
   *
   * @return the number of palettes in the game
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public int numPalettes() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game is not started");
    }
    return numPalettes;
  }

  /**
   * gets the winning palette.
   */
  private int getWinningPalette() {
    switch (canvas.getColor()) {
      case 'R':
        return redRule();
      case 'O':
        return orangeRule();
      case 'B':
        return blueRule();
      case 'I':
        return indigoRule();
      case 'V':
        return violetRule();
      default:
        break;
    }
    return -1;
  }

  /**
   * Returns the index of the winning palette in the game.
   *
   * @return the 0-based index of the winning palette
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public int winningPaletteIndex() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return this.winPalette;
  }

  /**
   * Returns if the game is over as specified by the implementation.
   *
   * @return true if the game has ended and false otherwise
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public boolean isGameOver() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game is not started");
    }
    return this.gameOver;
  }

  /**
   * Returns if the game is won by the player as specified by the implementation.
   *
   * @return true if the game has been won or false if the game has not
   * @throws IllegalStateException if the game has not started or the game is not over
   */
  @Override
  public boolean isGameWon() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return this.deck.isEmpty() && this.hand.isEmpty() && !this.gameLost;
  }

  /**
   * Returns a copy of the hand in the game. This means modifying the returned list
   * or the cards in the list has no effect on the game.
   */
  @Override
  public List<ConcreteCard> getHand() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
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
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (paletteNum < 0 || paletteNum > this.palettes.size() - 1) {
      throw new IllegalArgumentException("number of palettes can't be less " +
              "than zero or greater than size");
    }
    return new ArrayList<>(this.palettes.get(paletteNum));
  }

  /**
   * Return the top card of the canvas.
   * Modifying this card has no effect on the game.
   *
   * @return the top card of the canvas
   * @throws IllegalStateException if the game has not started or the game is over
   */
  @Override
  public ConcreteCard getCanvas() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return new ConcreteCard(this.canvas.getColor(), this.canvas.getNum());
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
    List<ConcreteCard> cardsList = new ArrayList<>();
    List<Character> colors =
            List.of('R', 'O', 'B', 'I', 'V');
    for (int i = 0; i < 5; i++) {
      for (int j = 1; j < 8; j++) {
        cardsList.add(new ConcreteCard(colors.get(i), j));
      }
    }
    return cardsList;
  }
}