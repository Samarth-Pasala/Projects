package cs3500.solored.model.hw04;

import cs3500.solored.model.hw02.ConcreteCard;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * Class that creates the game based on the type of game requested.
 * Can be used to create the game requested for the client/user.
 */
public class RedGameCreator {

  /**
   * Enum to represent the type of game being created.
   */
  public enum GameType {
    BASIC, ADVANCED
  }

  /**
   * Creates a game based on the given game type.
   * @param gameType the type of game to create (BASIC or ADVANCED).
   */
  public static RedGameModel<ConcreteCard> createGame(GameType gameType) {
    if (gameType == GameType.BASIC) {
      return new SoloRedGameModel();
    } else {
      return new AdvancedSoloRedGameModel();
    }
  }
}
