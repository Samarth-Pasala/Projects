package cs3500.solored;

import org.junit.Test;

import java.util.Random;

import cs3500.solored.model.hw04.AdvancedSoloRedGameModel;
import cs3500.solored.model.hw04.RedGameCreator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * class that tests the advanced version of the model.
 * tests to make sure the newly made draw mechanisms work.
 */
public class TestAdvancedSoloRedGameModel extends TestAbstractModel {

  /**
   * constructor that calls upon the advanced game type.
   */
  public TestAdvancedSoloRedGameModel() {
    this.gameType = RedGameCreator.GameType.ADVANCED;
  }

  /**
   * tests that the game works with the default values.
   */
  @Test
  public void testStartGameWithDefaultValues() {
    AdvancedSoloRedGameModel model = new AdvancedSoloRedGameModel(new Random(1));
    model.startGame(model.getAllCards(), false, 4, 7);
    assertEquals(4, model.numPalettes());
    assertEquals(7, model.getHand().size());
    assertFalse(model.isGameOver());
  }

  /**
   * tests that the draw twice behavior works properly.
   */
  @Test
  public void testPlayToCanvasTriggersDrawTwice() {
    AdvancedSoloRedGameModel model = new AdvancedSoloRedGameModel(new Random(1));
    model.startGame(model.getAllCards(), false, 4, 7);
    model.playToCanvas(4);
    model.playToPalette(2, 4);
    model.drawForHand();
    assertEquals(7, model.getHand().size());
  }

  /**
   * tests the playing only to palette triggers only one draw.
   */
  @Test
  public void testPlayToPaletteDrawOnce() {
    AdvancedSoloRedGameModel model = new AdvancedSoloRedGameModel(new Random(1));
    model.startGame(model.getAllCards(), false, 4, 7);
    model.playToPalette(1, 1);
    model.drawForHand();
    assertEquals(7, model.getHand().size());
  }

  /**
   * tests the scenario where no card is drawn when card is played to palette.
   */
  @Test
  public void testPlayToPaletteNoDraw() {
    AdvancedSoloRedGameModel model = new AdvancedSoloRedGameModel(new Random(1));
    model.startGame(model.getAllCards(), false, 4, 7);
    model.playToPalette(1, 2);
    assertEquals(6, model.getHand().size());
  }

  /**
   * tests that an exception is thrown when trying to play to canvas when game hasn't started.
   */
  @Test(expected = IllegalStateException.class)
  public void testPlayToCanvasBeforeGameStarts() {
    AdvancedSoloRedGameModel model = new AdvancedSoloRedGameModel(new Random(1));
    model.playToCanvas(0);
  }

}
