package cs3500.solored.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.view.hw02.RedGameView;
import cs3500.solored.view.hw02.SoloRedGameTextView;

/**
 * represents the controller for the RedGame.
 * handles user input and output.
 */
public class SoloRedTextController implements RedGameController {

  private final Readable rd;
  private final Appendable ap;
  private RedGameView viewController;
  private RedGameModel<?> model;
  private Scanner sc;
  private boolean gameOver = false;
  private boolean gameIsQuit = false;

  /**
   * constructor for the controller that reads the input and appends output.
   *
   * @param rd input.
   * @param ap output.
   */
  public SoloRedTextController(Readable rd, Appendable ap) {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Readable or Appendable cannot be null");
    }
    this.rd = rd;
    this.ap = ap;
  }

  /**
   * plays a new game of Solo Red using the provided model.
   *
   * @param model       the model used to play the game.
   * @param deck        the deck of cards used in the game.,
   * @param shuffle     used to shuffle/randomize the deck.
   * @param numPalettes number of palettes.
   * @param handSize    hand size in the game.
   * @param <C>         represents a generic.
   */
  @Override
  public <C extends Card> void playGame(RedGameModel<C> model, List<C> deck,
                                        boolean shuffle, int numPalettes, int handSize) {
    if (model == null || deck == null) {
      throw new IllegalArgumentException("Model or deck cannot be null");
    }
    sc = new Scanner(rd);
    this.model = model;
    viewController = new SoloRedGameTextView(model, ap);
    try {
      model.startGame(deck, shuffle, numPalettes, handSize);
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to start game", e);
    }
    render();
    while (sc.hasNext() && !gameIsQuit && !gameOver) {
      String getCase;
      try {
        getCase = sc.next().toLowerCase();
      } catch (Exception e) {
        throw new IllegalStateException("Unable to read input");
      }
      checkCase(getCase);
      if (gameOver) {
        if (model.isGameWon()) {
          transmission("Game won.");
        } else {
          transmission("Game lost.");
        }
        render();
        return;
      } else if (!gameIsQuit) {
        render();
      } else {
        return;
      }
    }
  }

  /**
   * renders the model in some manner.
   */
  public void render() {
    try {
      viewController.render();
      ap.append("\n");
      transmission("Number of cards in deck: " + model.numOfCardsInDeck());
    } catch (IOException e) {
      throw new IllegalStateException("can't render view");
    }
  }

  /**
   * checks the condition where the game should be quit and prints the according messages.
   */
  private void checkQuit() {
    gameIsQuit = true;
    transmission("Game quit!\nState of game when quit:");
    render();
  }

  /**
   * checks the various states that the game can be in.
   *
   * @param getCase represents the current case.
   */
  private void checkCase(String getCase) {
    switch (getCase) {
      case "palette":
        int paletteNum = getValidIndex();
        int handIdx = getValidIndex();
        if (paletteNum < 0 || handIdx < 0) {
          checkQuit();
          break;
        }
        try {
          model.playToPalette(paletteNum - 1, handIdx - 1);
          if (model.isGameWon() || model.isGameOver()) {
            gameOver = true;
            break;
          } else {
            model.drawForHand();
          }
        } catch (Exception e) {
          transmission("Invalid move. Try again. " + e.getMessage());
        }
        break;
      case "canvas":
        int canvasNum = getValidIndex();
        if (canvasNum < 0) {
          checkQuit();
          break;
        }
        try {
          model.playToCanvas(canvasNum - 1);
        } catch (Exception e) {
          transmission("Invalid move. Try again. " + e.getMessage());
        }
        break;
      case "q":
        checkQuit();
        break;
      default:
        transmission("Invalid command. Try again. Valid commands: 'q', 'Q', palette', or " +
                "'canvas'.");
        break;
    }
  }

  /**
   * makes sure valid index is printed for the game and handles errors.
   */
  private int getValidIndex() {
    while (true) {
      if (sc.hasNextInt()) {
        int num = sc.nextInt();
        if (num >= 0) {
          return num;
        }
      } else if (sc.hasNext()) {
        String input = sc.next();
        if (input.equals("q")) {
          gameIsQuit = true;
          return -1;
        }
      }
    }
  }

  /**
   * transmits messages to the user based on the game played.
   */
  private void transmission(String message) {
    try {
      ap.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}