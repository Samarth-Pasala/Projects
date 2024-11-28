package cs3500.solored;

import java.io.InputStreamReader;

import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.ConcreteCard;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw04.RedGameCreator;
import cs3500.solored.model.hw04.RedGameCreator.GameType;

/**
 * class that serves as the main for the game to be actually played.
 * starts a new game based on whether the basic or advanced version of the game is chosen.
 */
public final class SoloRed {

  /**
   * creates a static method for the main function to run properly.
   * @param args arguments for type of game being played.
   */
  public static void main(String[] args) {

    RedGameModel<ConcreteCard> newGame;
    int numPalettes;
    int handSize;

    String gameType = args[0];
    if (gameType.equals("basic")) {
      newGame = RedGameCreator.createGame(GameType.BASIC);
    } else if (gameType.equals("advanced")) {
      newGame = RedGameCreator.createGame(GameType.ADVANCED);
    } else {
      throw new IllegalArgumentException("Invalid game type");
    }

    if (args.length > 1) {
      numPalettes = Integer.parseInt(args[1]);
      if (numPalettes < 2) {
        numPalettes = 4;
      }
    } else {
      numPalettes = 4;
      handSize = 7;
    }

    if (args.length > 2) {
      handSize = Integer.parseInt(args[2]);
      if (handSize <= 0) {
        handSize = 7;
      }
    } else {
      handSize = 7;
    }

    try {
      new SoloRedTextController(new InputStreamReader(System.in), System.out).playGame(newGame,
              newGame.getAllCards(), true, numPalettes, handSize);
    } catch (Exception ignore) {
      // do nothing
    }
  }
}