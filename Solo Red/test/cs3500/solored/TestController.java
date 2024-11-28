package cs3500.solored;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.solored.model.hw02.ConcreteCard;
import cs3500.solored.model.hw02.MockSoloRedGameModel;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * tests the controller for the Red Game.
 * tests the various inputs that could be given for the expected outputs.
 */
public class TestController {

  /**
   * used to simply test the game for its different cases.
   */
  public static void main(String[] args) {
    SoloRedGameModel model = new SoloRedGameModel(new Random(1));
    List<ConcreteCard> deck = model.getAllCards().subList(0, 10);
    new SoloRedTextController(new InputStreamReader(System.in), System.out).
            playGame(model, deck, false, 3, 5);
  }

  private StringBuilder log;
  private RedGameModel<ConcreteCard> mockModel;
  private StringBuilder gameOutput;
  private SoloRedTextController controller;
  private List<ConcreteCard> deck;

  /**
   * set up the environment for the rest of the tests.
   */
  @Before
  public void setUp() {
    log = new StringBuilder();
    mockModel = new MockSoloRedGameModel(log);
    gameOutput = new StringBuilder();

    deck = new ArrayList<>();
    deck.add(new ConcreteCard('R', 1));
    deck.add(new ConcreteCard('O', 2));
    deck.add(new ConcreteCard('B', 3));
    deck.add(new ConcreteCard('I', 4));
    deck.add(new ConcreteCard('V', 5));
    deck.add(new ConcreteCard('R', 6));
    deck.add(new ConcreteCard('O', 7));
    deck.add(new ConcreteCard('B', 5));
    deck.add(new ConcreteCard('I', 1));

    controller = new SoloRedTextController(new StringReader("q\n"), gameOutput);
  }

  /**
   * tests that you can play to an actual palette properly and the controller calls it.
   */
  @Test
  public void testPlayToPaletteValid() {
    StringReader input = new StringReader("palette 1 1");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(log.toString().contains("playToPalette called: paletteIdx = 0, cardIdxInHand = 0"));
  }

  /**
   * tests that the controller properly handles playing to the canvas.
   */
  @Test
  public void testPlayToCanvasValid() {
    StringReader input = new StringReader("canvas 1");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(log.toString().contains("playToCanvas called: cardIdxInHand = 0"));
  }

  /**
   * tests that the controller properly handles quitting the game.
   */
  @Test
  public void testQuitGame() {
    StringReader input = new StringReader("q");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(gameOutput.toString().contains("Game quit!"));
  }

  /**
   * tests that the controller properly handles when invalid input is given.
   */
  @Test
  public void testInvalidInput() {
    StringReader input = new StringReader("invalid q");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(gameOutput.toString().contains("Invalid command. Try again."));
  }

  /**
   * tests that the controller properly handles invalid commands.
   */
  @Test
  public void testInvalidCommandHandling() {
    StringReader input = new StringReader("invalidCommand q");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(gameOutput.toString().contains("Invalid command. Try again."));
    assertTrue(gameOutput.toString().contains("Game quit!"));
  }

  /**
   * tests that an illegal argument is thrown when readable is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullReadable() {
    new SoloRedTextController(null, gameOutput);
  }

  /**
   * tests that an illegal argument is thrown when appendable is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    new SoloRedTextController(new StringReader(""), null);
  }

  /**
   * tests that the controller properly handles starting the game.
   */
  @Test
  public void testStartGame() {
    String input = "canvas 1\nq\n";
    controller = new SoloRedTextController(new StringReader(input), gameOutput);
    controller.playGame(mockModel, deck, false, 3, 5);
    assertTrue(log.toString().contains("startGame called"));
    assertTrue(log.toString().contains("playToCanvas called: cardIdxInHand = 0"));
  }

  /**
   * tests that the controller properly renders the information after quitting game.
   */
  @Test
  public void testGameRenderAfterQuit() {
    String input = "q\n";
    controller = new SoloRedTextController(new StringReader(input), gameOutput);
    controller.playGame(mockModel, deck, false, 3, 5);
    assertTrue(gameOutput.toString().contains("State of game when quit:"));
  }

  /**
   * tests that the controller properly handles drawing cards.
   */
  @Test
  public void testDrawingForHand() {
    String input = "palette 1 1\nq\n";
    controller = new SoloRedTextController(new StringReader(input), gameOutput);
    controller.playGame(mockModel, deck, false, 3, 5);
    assertTrue(log.toString().contains("drawForHand called"));
  }

  /**
   * tests that the controller properly handles negative palette index input.
   */
  @Test
  public void testNegativePaletteIndex() {
    StringReader input = new StringReader("palette -1 1 q");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(gameOutput.toString().contains("Game quit!"));
    assertTrue(gameOutput.toString().contains("State of game when quit:"));
  }

  /**
   * tests that the controller properly handles negative hand index input.
   */
  @Test
  public void testNegativeHandIndex() {
    StringReader input = new StringReader("palette 1 -1 q");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(gameOutput.toString().contains("Game quit!"));
    assertTrue(gameOutput.toString().contains("State of game when quit:"));
  }

  /**
   * tests that the controller properly handles zero canvas input.
   */
  @Test
  public void testZeroCanvasInput() {
    StringReader input = new StringReader("canvas 0 q");
    controller = new SoloRedTextController(input, gameOutput);
    controller.playGame(mockModel, deck, false, 1, 1);
    assertTrue(gameOutput.toString().contains("Game quit!"));
    assertTrue(gameOutput.toString().contains("State of game when quit:"));
  }

}


