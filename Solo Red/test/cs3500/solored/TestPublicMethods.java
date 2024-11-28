package cs3500.solored;

import cs3500.solored.model.hw04.RedGameCreator;

/**
 * Tests all public methods given within the RedGameModel interface.
 * Tests for exceptions as well as proper functionality.
 */
public class TestPublicMethods extends TestAbstractModel {

  public TestPublicMethods() {
    this.gameType = RedGameCreator.GameType.BASIC;
  }

}
