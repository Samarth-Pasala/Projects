package cs3500.threetrios.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import cs3500.threetrios.view.GUIView;
import cs3500.threetrios.model.rules.BattleRule;

/**
 * Initializes the Three Trios game by setting up the grid, players, and cards using the
 * provided configuration files. This class handles the game setup, including reading
 * grid and card configurations from files, dealing cards to players, and preparing the game state.
 */
public class GameInitializer {

  /**
   * Initializes a new game using the configuration files for the grid and the cards.
   *
   * @param gridFilePath  the path to the grid configuration file
   * @param cardsFilePath the path to the cards configuration file
   * @param rand          a Random object to shuffle cards
   * @param battleRule    the BattleRule to be applied during the game
   * @return a new ThreeTriosGame instance with the view already set up.
   */
  public ThreeTriosGame initializeGame(String gridFilePath, String cardsFilePath, Random rand,
                                       BattleRule battleRule) {
    Grid grid = readGridFromFile(gridFilePath);
    List<Card> cards = readCardsFromFile(cardsFilePath);

    Player redPlayer = new PlayerImpl(PlayerColor.RED);
    Player bluePlayer = new PlayerImpl(PlayerColor.BLUE);
    List<Player> players = Arrays.asList(redPlayer, bluePlayer);

    int cardCells = grid.getRows() * grid.getCols();
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        if (grid.getCell(row, col).isHole()) {
          cardCells--;
        }
      }
    }

    if (cards.size() < cardCells + 1) {
      throw new IllegalArgumentException("Not enough cards available for the game.");
    }

    // Determine hand size based on (N + 1) / 2 rule
    int handSize = (cardCells + 1) / 2;

    Collections.shuffle(cards, rand);
    for (int i = 0; i < handSize; i++) {
      redPlayer.addCardToHand(cards.remove(0));
      redPlayer.getHand().get(i).setCardOwner(PlayerColor.RED);
      bluePlayer.addCardToHand(cards.remove(0));
      bluePlayer.getHand().get(i).setCardOwner(PlayerColor.BLUE);
    }

    // Create the game model with the provided battleRule
    ThreeTriosGame game = new ThreeTriosGame(grid, players, cards, rand, battleRule);

    GUIView view = new GUIView(game);

    return game;
  }

  /**
   * Reads the grid configuration from the specified file and creates a Grid instance.
   *
   * @param filePath the path to the grid file
   * @return a Grid instance constructed from the file
   */
  private Grid readGridFromFile(String filePath) {
    try (Scanner scanner = new Scanner(new File(filePath))) {
      int rows = scanner.nextInt();
      int cols = scanner.nextInt();
      scanner.nextLine();

      List<List<Cell>> cells = new ArrayList<>();

      for (int i = 0; i < rows; i++) {
        List<Cell> row = new ArrayList<>();
        String line = scanner.nextLine();

        for (char c : line.toCharArray()) {
          boolean isHole = (c == 'X');
          row.add(new CellImpl(isHole));
        }

        cells.add(row);
      }

      return new GridImpl(rows, cols, cells);

    } catch (FileNotFoundException e) {
      throw new RuntimeException("Grid configuration file not found: " + filePath);
    }
  }

  /**
   * Reads card configurations from the specified file and creates a list of Card instances.
   *
   * @param filePath the path to the cards file
   * @return a list of Card instances
   */
  private List<Card> readCardsFromFile(String filePath) {
    List<Card> cards = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filePath))) {
      while (scanner.hasNextLine()) {
        String[] parts = scanner.nextLine().split(" ");
        String cardId = parts[0];

        Map<CardDirection, AttackValue> attackValues = new HashMap<>();
        attackValues.put(CardDirection.NORTH, getAttackValue(parts[1]));
        attackValues.put(CardDirection.SOUTH, getAttackValue(parts[2]));
        attackValues.put(CardDirection.EAST, getAttackValue(parts[3]));
        attackValues.put(CardDirection.WEST, getAttackValue(parts[4]));

        cards.add(new CardImpl(cardId, attackValues));
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Card configuration file not found: " + filePath);
    }
    return cards;
  }

  /**
   * Converts a string representation of an attack value to its corresponding AttackValue enum.
   *
   * @param value the string representation of the attack value
   * @return the corresponding AttackValue enum
   * @throws IllegalArgumentException if the value is invalid
   */
  private AttackValue getAttackValue(String value) {
    switch (value) {
      case "A":
        return AttackValue.A;
      case "1":
        return AttackValue.ONE;
      case "2":
        return AttackValue.TWO;
      case "3":
        return AttackValue.THREE;
      case "4":
        return AttackValue.FOUR;
      case "5":
        return AttackValue.FIVE;
      case "6":
        return AttackValue.SIX;
      case "7":
        return AttackValue.SEVEN;
      case "8":
        return AttackValue.EIGHT;
      case "9":
        return AttackValue.NINE;
      default:
        throw new IllegalArgumentException("Invalid attack value: " + value);
    }
  }
}
