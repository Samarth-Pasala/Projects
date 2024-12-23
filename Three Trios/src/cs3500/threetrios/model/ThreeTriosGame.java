package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cs3500.threetrios.controller.ModelStateListener;
import cs3500.threetrios.controller.PlayerTurnListener;
import cs3500.threetrios.model.rules.BattleRule;
import cs3500.threetrios.view.ThreeTriosModelView;

/**
 * Represents the game logic for the Three Trios game.
 * This class implements the core game functionalities, including managing the game state,
 * handling player turns, placing cards on the grid, and determining the winner.
 */
public class ThreeTriosGame implements ThreeTriosModel {

  private Grid grid;
  private List<Player> players;
  private int currentPlayerIndex;
  private final List<Card> cards;
  private Random rand;
  private boolean gameStarted;
  private List<ModelStateListener> listeners = new ArrayList<>();
  private List<PlayerTurnListener> turnListeners = new ArrayList<>();
  private BattleRule battleRule;


  /**
   * Constructs a ThreeTriosGame with the specified grid, players, and cards.
   * Class Invariant: Number of card cells is always odd
   *
   * @param grid    the game grid
   * @param players a list of players in the game
   * @param cards   a list of cards to be used in the game
   * @throws IllegalArgumentException if grid dimensions are not positive,
   *                                  or if the grid does not contain an odd number of cells,
   *                                  or if the players list is null or empty.
   */

  public ThreeTriosGame(Grid grid, List<Player> players, List<Card> cards, BattleRule battleRule) {
    if (grid.getRows() <= 0 || grid.getCols() <= 0) {
      throw new IllegalArgumentException("Grid dimensions must be greater than zero.");
    }
    // Class Invariant: Number of card cells is always odd
    if ((grid.getRows() * grid.getCols()) % 2 == 0) {
      throw new IllegalArgumentException("Grid must contain an odd number of card cells.");
    }
    if (players == null || players.isEmpty()) {
      throw new IllegalArgumentException("Players list cannot be null or empty.");
    }
    this.grid = grid;
    this.players = players;
    this.cards = new ArrayList<>(cards);
    this.currentPlayerIndex = 0;
    this.gameStarted = false;
    this.battleRule = Objects.requireNonNull(battleRule, "BattleRule cannot be null");
  }

  /**
   * Constructs a ThreeTriosGame with the specified grid, players, cards,
   * and a random number generator.
   *
   * @param grid    the game grid
   * @param players a list of players in the game
   * @param cards   a list of cards to be used in the game
   * @param rand    a Random object for shuffling cards
   * @throws IllegalArgumentException if the random generator is null.
   */
  public ThreeTriosGame(Grid grid, List<Player> players, List<Card> cards, Random rand,
                        BattleRule battleRule) {
    this(grid, players, cards, battleRule);
    if (rand == null) {
      throw new IllegalArgumentException("Random cannot be null");
    }
    this.rand = rand;
  }

  /**
   * Adds a listener for model state changes.
   *
   * @param listener the listener to add.
   */
  public void addModelStateListener(ModelStateListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    this.listeners.add(listener);
  }

  /**
   * Notifies all registered listeners that the model's state has changed.
   */
  private void notifyModelStateChanged() {
    for (ModelStateListener listener : this.listeners) {
      listener.updateView();
    }
  }

  /**
   * Places a card at the specified grid location.
   *
   * @param index The index of the card in the player's hand.
   * @param row   The row in the grid where the card will be placed.
   * @param col   The column in the grid where the card will be placed.
   * @throws IllegalArgumentException if the index is out of bounds,
   *                                  or if the specified cell is not a valid move.
   */
  @Override
  public void placeCard(int index, int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Cannot place a card before the game starts.");
    }
    if (!isValidMove(row, col)) {
      throw new IllegalArgumentException("Invalid move.");
    }

    Player currPlayer = this.getCurrentPlayer();
    if (index < 0 || index >= currPlayer.getHand().size()) {
      throw new IllegalArgumentException("Invalid card index.");
    }
    Card card = currPlayer.getHand().get(index);
    grid.getCell(row, col).placeCard(card);
    currPlayer.removeCardFromHand(card);

    List<Card> flippedCards = battlePhase(this.getCurrentPlayer(), card, row, col);

    comboPhase(flippedCards);
    notifyModelStateChanged();
    endTurn();
  }

  /**
   * Checks if a move to the specified grid cell is valid.
   * A valid move is defined as placing a card in an empty cell
   * that is not a hole.
   *
   * @param row the row of the cell to check
   * @param col the column of the cell to check
   * @return true if the move is valid; false otherwise
   * @throws IllegalArgumentException if the cell is a hole
   */
  private boolean isValidMove(int row, int col) {
    Cell cell = grid.getCell(row, col);
    if (cell.isHole()) {
      throw new IllegalArgumentException("Invalid move: cannot place a card in hole.");
    }
    return !cell.isHole() && cell.isEmpty();
  }

  private boolean isValidMoveCheck(int row, int col) {
    Cell cell = grid.getCell(row, col);
    return !cell.isHole() && cell.isEmpty();
  }

  @Override
  public boolean canCurrentPlayerPlaceCard(int row, int col) {
    return isValidMoveCheck(row, col);
  }

  /**
   * Conducts the battle phase when a card is placed.
   * Checks all adjacent cells for enemy cards and resolves battles
   * against them based on the attack values of the cards involved.
   *
   * @param playerA the player placing the card
   * @param cardA   the card being placed
   * @param row     the row of the placed card
   * @param col     the column of the placed card
   * @return a list of cards that were flipped during the battle phase
   */
  private List<Card> battlePhase(Player playerA, Card cardA, int row, int col) {
    PlayerColor colorA = playerA.getColor();
    CardDirection[] directions = CardDirection.values();
    List<Card> flippedCards = new ArrayList<>();


    for (CardDirection direction : directions) {
      int[] adjacentPosition = getAdjacentPosition(row, col, direction);
      int adjacentRow = adjacentPosition[0];
      int adjacentCol = adjacentPosition[1];


      if (isWithinBounds(adjacentRow, adjacentCol)) {
        Cell adjacentCell = grid.getCell(adjacentRow, adjacentCol);
        if (!adjacentCell.isEmpty()) {
          Card cardB = adjacentCell.getCard();
          PlayerColor colorB = cardB.getCardOwner();


          if (colorB != colorA) {
            if (resolveBattle(cardA, cardB, direction)) {
              flippedCards.add(cardB);
            }
          }
        }
      }
    }
    return flippedCards;
  }

  /**
   * Calculates the position of a cell adjacent to the specified
   * row and column based on the given direction.
   *
   * @param row       the current row
   * @param col       the current column
   * @param direction the direction to check (NORTH, SOUTH, EAST, WEST)
   * @return an array containing the row and column of the adjacent cell
   * @throws IllegalArgumentException if the direction is invalid
   */
  private int[] getAdjacentPosition(int row, int col, CardDirection direction) {
    switch (direction) {
      case NORTH:
        return new int[]{row - 1, col};
      case SOUTH:
        return new int[]{row + 1, col};
      case EAST:
        return new int[]{row, col + 1};
      case WEST:
        return new int[]{row, col - 1};
      default:
        throw new IllegalArgumentException("Invalid direction.");
    }
  }

  /**
   * Checks if the given row and column are within the valid bounds
   * of the grid.
   *
   * @param row the row to check
   * @param col the column to check
   * @return true if the position is within bounds; false otherwise
   */
  private boolean isWithinBounds(int row, int col) {
    return row >= 0 && row < grid.getRows() && col >= 0 && col < grid.getCols();
  }

  /**
   * Resolves a battle between two cards. The winner of the battle
   * is determined based on the attack values of the cards in the
   * specified direction.
   *
   * @param cardA     the attacking card
   * @param cardB     the defending card
   * @param direction the direction of the attack
   * @return true if cardA wins the battle and flips cardB; false otherwise
   */
  private boolean resolveBattle(Card cardA, Card cardB, CardDirection direction) {
    return battleRule.resolveBattle(cardA, cardB, direction);
  }

  /**
   * Returns the opposite direction of the given direction.
   *
   * @param direction the direction for which to find the opposite
   * @return the opposite CardDirection
   * @throws IllegalArgumentException if the direction is invalid
   */
  private CardDirection getOppositeDirection(CardDirection direction) {
    switch (direction) {
      case NORTH:
        return CardDirection.SOUTH;
      case SOUTH:
        return CardDirection.NORTH;
      case EAST:
        return CardDirection.WEST;
      case WEST:
        return CardDirection.EAST;
      default:
        throw new IllegalArgumentException("Invalid direction.");
    }
  }

  /**
   * Executes the combo phase for each card that was flipped during
   * the battle phase. In this phase, the flipped cards can trigger
   * further battles with their adjacent cards.
   *
   * @param flippedCards the list of cards that were flipped during the battle phase
   */
  private void comboPhase(List<Card> flippedCards) {
    for (Card card : flippedCards) {
      int[] position = findCardPosition(card);
      if (position != null) {
        int row = position[0];
        int col = position[1];
        PlayerColor colorA = card.getCardOwner();
        CardDirection[] directions = CardDirection.values();


        for (CardDirection direction : directions) {
          int[] adjacentPosition = getAdjacentPosition(row, col, direction);
          int adjacentRow = adjacentPosition[0];
          int adjacentCol = adjacentPosition[1];


          if (isWithinBounds(adjacentRow, adjacentCol)) {
            Cell adjacentCell = grid.getCell(adjacentRow, adjacentCol);
            if (!adjacentCell.isEmpty()) {
              Card adjacentCard = adjacentCell.getCard();
              PlayerColor colorB = adjacentCard.getCardOwner();


              if (colorB != colorA) {
                resolveBattle(card, adjacentCard, direction);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Finds the position (row and column) of the specified card on the grid.
   *
   * @param card the card to locate
   * @return an array containing the row and column of the card, or null if not found
   */
  private int[] findCardPosition(Card card) {
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        if (grid.getCell(row, col).getCard() == card) {
          return new int[]{row, col};
        }
      }
    }
    return null;
  }

  /**
   * Initializes and starts the game.
   * This method sets up the game state, including shuffling cards,
   * and determining the first player.
   */
  @Override
  public void startGame() {
    if (players == null || players.isEmpty()) {
      throw new IllegalArgumentException("Cannot start game with no players.");
    }
    Collections.shuffle(cards, rand);
    int handSize = (grid.getRows() * grid.getCols() + 1) / 2;


    if (cards.size() < handSize * players.size()) {
      throw new IllegalArgumentException("Not enough cards to start the game.");
    }

    currentPlayerIndex = 0;
    render();
    this.gameStarted = true;

    notifyGameStarted();
  }

  private void notifyGameStarted() {
    for (ModelStateListener listener : this.listeners) {
      listener.initializeGame();
    }
  }

  /**
   * Ends the current player's turn, passing control to the next player.
   * This method should update the game state accordingly.
   */
  @Override
  public void endTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    notifyTurnChange();
  }

  /**
   * Checks if the game has ended.
   *
   * @return true if the game is over, false otherwise.
   */
  @Override
  public boolean isGameOver() {
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        if (grid.getCell(row, col).isEmpty() && !grid.getCell(row, col).isHole()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Determines the winner of the game.
   *
   * @return the Player who has won, or null if there is no winner yet.
   */
  @Override
  public Player determineWinner() {
    int maxCardCount = -1;
    Player winner = null;
    boolean isTie = false;


    for (Player player : players) {
      int cardCount = player.getHand().size() + countCardsOnGrid(player.getColor());


      if (cardCount > maxCardCount) {
        maxCardCount = cardCount;
        winner = player;
        isTie = false;
      } else if (cardCount == maxCardCount) {
        isTie = true;
      }
    }
    if (isTie) {
      winner = null;
    }
    return winner;
  }

  /**
   * Counts the number of cards owned by the specified player color
   * that are currently on the grid.
   *
   * @param color the color of the player whose cards to count
   * @return the total count of cards owned by the player on the grid
   */
  private int countCardsOnGrid(PlayerColor color) {
    int count = 0;
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        Cell cell = grid.getCell(row, col);
        if (!cell.isEmpty() && cell.getCard().getCardOwner() == color) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Retrieves the current player.
   *
   * @return the Player whose turn it is.
   */
  @Override
  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /**
   * Gets the player who is not currently taking their turn.
   *
   * @return the player whose turn it is not, aka the opponent of the current player.
   */
  @Override
  public Player getOtherPlayer() {
    return players.get(1 - currentPlayerIndex);
  }

  /**
   * Retrieves the current game grid.
   *
   * @return the Grid representing the current state of the game.
   */
  @Override
  public Grid getGrid() {
    return grid;
  }


  /**
   * Renders the current state of the game using the ThreeTriosModelView.
   * This method updates the display to reflect the current game state.
   */
  private void render() {
    ThreeTriosModelView view = new ThreeTriosModelView(this);
    view.render();
  }

  /**
   * Gets the contents of a cell in the grid at a given position.
   *
   * @param row the row of the cell.
   * @param col the column of the cell.
   * @return the contents of the cell at the given position.
   */
  @Override
  public Cell getCell(int row, int col) {
    return grid.getCell(row, col);
  }

  /**
   * Gets the score of a player, which is the number of cards they own in their hand
   * plus the number of cards they own on the grid.
   *
   * @param player the player whose score to retrieve.
   * @return the score of the player.
   */
  @Override
  public int getPlayerScore(Player player) {
    int score = player.getHand().size();
    score += countCardsOnGrid(player.getColor());
    return score;
  }

  /**
   * Gets the list of cards in a player's hand.
   *
   * @param player the player whose hand to retrieve.
   * @return the list of cards in the player's hand.
   */
  @Override
  public List<Card> getPlayerHand(Player player) {
    return player.getHand();
  }

  /**
   * Adds a listener to notify when it's a player's turn.
   *
   * @param listener the PlayerTurnListener to add.
   */
  public void addTurnListener(PlayerTurnListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    turnListeners.add(listener);
  }

  private void notifyTurnChange() {
    Player currentPlayer = getCurrentPlayer();
    System.out.println("Notifying turn for: " + currentPlayer.getColor());

    for (PlayerTurnListener listener : turnListeners) {
      listener.onTurn(this.getCurrentPlayer().getColor());
    }
  }

}
