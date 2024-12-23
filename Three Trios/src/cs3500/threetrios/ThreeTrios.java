package cs3500.threetrios;

import cs3500.threetrios.controller.AIPlayer;
import cs3500.threetrios.controller.HumanPlayer;
import cs3500.threetrios.controller.ThreeTriosController;
import cs3500.threetrios.model.ChooseCornerStrategy;
import cs3500.threetrios.model.GameInitializer;
import cs3500.threetrios.model.ThreeTriosGame;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.rules.BattleRule;
import cs3500.threetrios.model.rules.StandardRule;
import cs3500.threetrios.view.GUIView;

import java.util.Random;

/**
 * The ThreeTrios class contains the main entry point for the ThreeTrios game application.
 */
public final class ThreeTrios {

  /**
   * The main method that serves as the entry point for the ThreeTrios game application.
   * It initializes the game using the provided board and cards configuration files,
   * sets up the game view and controllers, and starts the GUI for both players.
   *
   * @param args command line arguments (not used in this implementation).
   */
  public static void main(String[] args) {
    String gridFilePath = "BoardConfigs/BoardWithHoles3x3";
    String cardsFilePath = "BoardConfigs/EnoughCards";

    BattleRule battleRule = new StandardRule();

    GameInitializer initializer = new GameInitializer();

    ThreeTriosGame model = initializer.initializeGame(gridFilePath, cardsFilePath, new Random(),
            battleRule);

    HumanPlayer player1 = new HumanPlayer(PlayerColor.RED);
    AIPlayer player2 = new AIPlayer(PlayerColor.BLUE, new ChooseCornerStrategy(model));

    GUIView viewPlayer1 = new GUIView(model);
    GUIView viewPlayer2 = new GUIView(model);

    ThreeTriosController controller1 = new ThreeTriosController(model, player1, viewPlayer1);
    ThreeTriosController controller2 = new ThreeTriosController(model, player2, viewPlayer2);

    model.addTurnListener(player2);

    model.startGame();
  }
}
