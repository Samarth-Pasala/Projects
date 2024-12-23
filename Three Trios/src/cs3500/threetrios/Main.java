package cs3500.threetrios;

import cs3500.threetrios.model.AttackValue;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardDirection;
import cs3500.threetrios.model.CardImpl;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.CellImpl;
import cs3500.threetrios.model.Grid;
import cs3500.threetrios.model.GridImpl;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.PlayerImpl;
import cs3500.threetrios.model.ThreeTriosGame;
import cs3500.threetrios.model.rules.BattleRule;
import cs3500.threetrios.model.rules.StandardRule;
import cs3500.threetrios.view.ThreeTriosModelView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * This class initializes the game, reads the grid and card configurations from files,
 * and manages the game flow. Includes starting the game and rendering the game state.
 */
public class Main {

  /**
   * The main method initializes the game by reading grid and card configurations from
   * specified files, starts the game, and renders the initial state of the game.
   */
  public static void main(String[] args) {
    String gridFilePath = "BoardConfigs/BoardWithHoles3x3";
    String cardsFilePath = "BoardConfigs/EnoughCards";

    // Use a standard rule for this example
    BattleRule battleRule = new StandardRule();

    ThreeTriosGame game = initializeGame(gridFilePath, cardsFilePath, new Random(), battleRule);

    game.startGame();

    ThreeTriosModelView view = new ThreeTriosModelView(game);

    view.render();

    game.endTurn();
    view.render();
  }

  /**
   * Initializes the Three Trios game by reading the grid and card configurations from specified
   * files. It also creates the players and sets up the initial game state.
   *
   * @param gridFilePath  the path to the file containing the grid configuration
   * @param cardsFilePath the path to the file containing the card configurations
   * @param rand          a Random object used for shuffling or randomness in the game
   * @param battleRule    the BattleRule to use for resolving battles
   * @return an instance of ThreeTriosGame initialized with the specified grid, players, and cards
   */
  private static ThreeTriosGame initializeGame(String gridFilePath, String cardsFilePath,
                                               Random rand, BattleRule battleRule) {
    Grid grid = readGridFromFile(gridFilePath);
    List<Card> cards = readCardsFromFile(cardsFilePath);
    List<Player> players = new ArrayList<>();
    players.add(new PlayerImpl(PlayerColor.RED));
    players.add(new PlayerImpl(PlayerColor.BLUE));
    return new ThreeTriosGame(grid, players, cards, rand, battleRule);
  }

  /**
   * Reads the grid configuration from a specified file and constructs a Grid object.
   * The file format should specify the number of rows and columns, followed by the grid layout,
   * where 'X' represents a hole and other characters represent empty cells.
   *
   * @param filePath the path to the grid configuration file
   * @return a Grid object representing the layout specified in the file
   * @throws RuntimeException if the specified file cannot be found
   */
  private static Grid readGridFromFile(String filePath) {
    try (Scanner scanner = new Scanner(new File(filePath))) {
      int rows = scanner.nextInt();
      int cols = scanner.nextInt();
      List<List<Cell>> cells = new ArrayList<>();

      for (int i = 0; i < rows; i++) {
        List<Cell> row = new ArrayList<>();
        String line = scanner.next();
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
   * Reads card configurations from a specified file and constructs a list of Card objects.
   * The file format should specify each card's ID and its attack values for the four directions.
   *
   * @param filePath the path to the card configuration file
   * @return a List of Card objects constructed from the data in the file
   * @throws RuntimeException if the specified file cannot be found
   */
  private static List<Card> readCardsFromFile(String filePath) {
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
   * Converts a string representation of an attack value into the corresponding AttackValue enum.
   *
   * @param value the string representation of the attack value (e.g., "A", "1", "2", ..., "9")
   * @return the corresponding AttackValue enum
   * @throws IllegalArgumentException if the provided string does not match a valid attack value
   */
  private static AttackValue getAttackValue(String value) {
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
