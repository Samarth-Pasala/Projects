package cs3500.threetrios;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.threetrios.controller.AIPlayer;
import cs3500.threetrios.controller.HumanPlayer;
import cs3500.threetrios.controller.ThreeTriosController;
import cs3500.threetrios.model.ChooseCornerStrategy;
import cs3500.threetrios.model.FlipMaxCardsStrategy;
import cs3500.threetrios.model.GameInitializer;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ThreeTriosGame;
import cs3500.threetrios.model.rules.BattleRule;
import cs3500.threetrios.model.rules.FallenAceRule;
import cs3500.threetrios.model.rules.ReverseRule;
import cs3500.threetrios.model.rules.CombineRules;
import cs3500.threetrios.model.rules.StandardRule;
import cs3500.threetrios.view.GUIView;

/**
 * The main class for running a ThreeTrios game.
 */
public class ThreeTriosMain {

  /**
   * Main method that initializes and starts the ThreeTrios game.
   *
   * @param args Command-line arguments that define the players, rules, and other game
   *             configurations.
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      return;
    }

    PlayerColor player1Color = PlayerColor.RED;
    PlayerColor player2Color = PlayerColor.BLUE;

    String gridFilePath = "BoardConfigs/BoardWithHoles3x3";
    String cardsFilePath = "BoardConfigs/EnoughCards";

    // Parse rules from remaining arguments
    List<BattleRule> rules = parseRules(args, 2); // Start parsing from the third argument
    BattleRule battleRule = rules.isEmpty() ? new StandardRule() : new CombineRules(rules);

    // Initialize the model with the selected rule
    GameInitializer initializer = new GameInitializer();
    ThreeTriosGame model = initializer.initializeGame(gridFilePath, cardsFilePath, new Random(),
            battleRule);

    // Create players
    Player player1 = createPlayer(args[0], player1Color, model);
    Player player2 = createPlayer(args[1], player2Color, model);

    // Set up views and controllers
    GUIView viewPlayer1 = new GUIView(model);
    GUIView viewPlayer2 = new GUIView(model);

    new ThreeTriosController(model, player1, viewPlayer1);
    new ThreeTriosController(model, player2, viewPlayer2);

    // Add AI turn listeners if applicable
    if (player1 instanceof AIPlayer) {
      model.addTurnListener((AIPlayer) player1);
    }
    if (player2 instanceof AIPlayer) {
      model.addTurnListener((AIPlayer) player2);
    }

    // Start the game
    model.startGame();

    // Notify AI player if the first player is AI
    if (player1 instanceof AIPlayer) {
      ((AIPlayer) player1).notifyTurn();
    }
  }

  private static Player createPlayer(String type, PlayerColor color, ThreeTriosGame model) {
    switch (type.toLowerCase()) {
      case "humanplayer":
        return new HumanPlayer(color);
      case "choosecornerstrategy":
        return new AIPlayer(color, new ChooseCornerStrategy(model));
      case "flipmaxcardsstrategy":
        return new AIPlayer(color, new FlipMaxCardsStrategy(model));
      default:
        throw new IllegalArgumentException("Unknown player type or strategy: " + type);
    }
  }

  private static List<BattleRule> parseRules(String[] args, int startIndex) {
    List<BattleRule> rules = new ArrayList<>();
    for (int i = startIndex; i < args.length; i++) {
      switch (args[i].toLowerCase()) {
        case "reverserule":
          rules.add(new ReverseRule());
          break;
        case "fallenacerule":
          rules.add(new FallenAceRule());
          break;
        default:
          System.out.println("Unknown rule: " + args[i]);
      }
    }
    return rules;
  }
}



