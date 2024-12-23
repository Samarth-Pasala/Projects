package cs3500.threetrios;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.GameInitializer;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ThreeTriosGame;
import cs3500.threetrios.model.rules.BattleRule;
import cs3500.threetrios.model.rules.CombineRules;
import cs3500.threetrios.model.rules.FallenAceRule;
import cs3500.threetrios.model.rules.ReverseRule;
import cs3500.threetrios.model.rules.StandardRule;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration tests for the ThreeTriosGame using actual board and card configurations.
 */
public class TestRules {
  private GameInitializer initializer;
  private final String gridFilePath = "BoardConfigs/BoardWithNoHoles3x3";
  private final String cardsFilePath = "BoardConfigs/EnoughCards";

  /**
   * Initializes the game initializer for creating a new game instance.
   */
  @Before
  public void setup() {
    initializer = new GameInitializer();
  }

  /**
   * Test case for simulating a standard gameplay scenario using the StandardRule.
   */
  @Test
  public void testStandardRuleGameplay() {
    ThreeTriosGame game = initializer.initializeGame(gridFilePath, cardsFilePath, new Random(),
            new StandardRule());

    game.startGame();

    // Validate initial state
    assertNotNull(game.getGrid());
    assertEquals(3, game.getGrid().getRows());
    assertEquals(3, game.getGrid().getCols());

    Player playerRed = game.getCurrentPlayer();
    Player playerBlue = game.getOtherPlayer();

    assertEquals(PlayerColor.RED, playerRed.getColor());
    assertEquals(PlayerColor.BLUE, playerBlue.getColor());

    // Simulate gameplay
    game.placeCard(0, 0, 0); // Player RED places a card at (0, 0)
    assertEquals(PlayerColor.RED, game.getCell(0, 0).getCard().getCardOwner());

    //game.endTurn(); // Switch to Player BLUE
    assertEquals(PlayerColor.BLUE, game.getCurrentPlayer().getColor());

    game.placeCard(0, 0, 1); // Player BLUE places a card at (0, 1)
    assertEquals(PlayerColor.BLUE, game.getCell(0, 1).getCard().getCardOwner());

    //game.endTurn(); // Switch back to Player RED
    assertEquals(PlayerColor.RED, game.getCurrentPlayer().getColor());
  }

  /**
   * Test case for simulating a battle using the ReverseRule.
   */
  @Test
  public void testReverseRuleBattle() {
    // Initialize the game with ReverseRule
    ThreeTriosGame game = initializer.initializeGame(gridFilePath, cardsFilePath, new Random(),
            new ReverseRule());

    game.startGame();

    // Validate initial state
    assertNotNull(game.getGrid());
    assertEquals(3, game.getGrid().getRows());
    assertEquals(3, game.getGrid().getCols());

    // Get the current player and opponent (Red first)
    Player playerRed = game.getCurrentPlayer();
    Player playerBlue = game.getOtherPlayer();

    // Get two cards from each player's hand (Red's and Blue's cards)
    Card redCard = playerRed.getHand().get(0);  // Red's card
    Card blueCard = playerBlue.getHand().get(0);  // Blue's card

    // Place Red's card at position (0, 0) and Blue's card at position (0, 1)
    game.placeCard(0, 0, 0);  // Red places card at (0, 0)
    game.endTurn();  // End Red's turn, switch to Blue

    game.placeCard(0, 1, 1);  // Blue places card at (0, 1)

    // Assert that the Red player's card should win and flip the Blue player's card under
    // the ReverseRule
    // Since the ReverseRule causes the card with a lower attack value to win, we need to check
    // the result.
    assertNotEquals(redCard.getCardOwner(), blueCard.getCardOwner());  // Ownership of blueCard
    // should change after flip
    assertEquals(PlayerColor.BLUE, blueCard.getCardOwner());  // Player RED should own the flipped
    // card due to Reverse Rule logic
  }

  /**
   * Test case for simulating a battle using the FallenAceRule.
   */
  @Test
  public void testFallenAceRule() {
    // Initialize the game with FallenAceRule
    ThreeTriosGame game = initializer.initializeGame(gridFilePath, cardsFilePath, new Random(),
            new FallenAceRule());

    game.startGame();

    // Validate initial state
    assertNotNull(game.getGrid());
    assertEquals(3, game.getGrid().getRows());
    assertEquals(3, game.getGrid().getCols());

    // Get the current player and opponent (Red first)
    Player playerRed = game.getCurrentPlayer();
    Player playerBlue = game.getOtherPlayer();

    // Get the cards from each player's hand (CardTwo from Red, and CardOne with 1 from Blue)
    Card redCard = playerRed.getHand().get(0);  // Red's card (CardTwo with an A attack value)
    Card blueCard = playerBlue.getHand().get(1);  // Blue's card (CardOne with a 1 attack value)

    game.placeCard(0, 1, 1);  // Red places CardTwo at (0, 0) (with A)
    game.endTurn();  // End Red's turn, switch to Blue

    // Place Blue's card (CardOne) at position (0, 1)
    game.placeCard(1, 0, 1);  // Blue places CardOne at (0, 1) (with 1)

    // Assert that Blue's card (with 1) should win and flip Red's card (with A) under the Fallen
    // Ace rule
    assertNotEquals(redCard.getCardOwner(), blueCard.getCardOwner());  // Ownership of redCard
    // should change after flip
    assertEquals(PlayerColor.RED, redCard.getCardOwner());  // Player BLUE should own the flipped
    // card due to Fallen Ace logic
  }

  /**
   * Test case for combining ReverseRule and FallenAceRule.
   */
  @Test
  public void testCombinedRules() {
    // Initialize the game with CombineRules (using both ReverseRule and FallenAceRule)
    List<BattleRule> rules = List.of(new ReverseRule(), new FallenAceRule());
    ThreeTriosGame game = initializer.initializeGame(gridFilePath, cardsFilePath, new Random(),
            new CombineRules(rules));

    game.startGame();

    // Validate initial state
    assertNotNull(game.getGrid());
    assertEquals(3, game.getGrid().getRows());
    assertEquals(3, game.getGrid().getCols());

    // Get the current player and opponent (Red first)
    Player playerRed = game.getCurrentPlayer();
    Player playerBlue = game.getOtherPlayer();

    // Get the cards from each player's hand (CardThirtyFour with A for Red and CardThirtyFive with
    // 1 for Blue)
    Card redCard = playerRed.getHand().get(0);  // Red's card (CardThirtyFour with A attack value)
    Card blueCard = playerBlue.getHand().get(1);  // Blue's card (CardThirtyFive with 1 attack
    // value)

    // Red places CardThirtyFour (with A) at position (0, 0)
    game.placeCard(0, 0, 0);
    game.endTurn();  // End Red's turn, switch to Blue

    // Blue places CardThirtyFive (with 1) at position (0, 1)
    game.placeCard(0, 1, 1);

    // Assert that Blue's card (with 1) should win and flip Red's card (with A) under the combined
    // rules:
    // 1) The FallenAceRule allows 1 to beat A, and
    // 2) The ReverseRule flips ownership based on attack values.
    assertNotEquals(redCard.getCardOwner(), blueCard.getCardOwner());  // Ownership of redCard
    // should change after flip
    assertEquals(PlayerColor.RED, redCard.getCardOwner());  // Player BLUE should own the flipped
    // card due to combined rules
  }


  /**
   * Test case to verify that the StandardRule does not apply any flipping of ownership
   * when cards have equal attack values.
   */
  @Test
  public void testStandardRuleNoFlip() {
    // Initialize the game with StandardRule (no special logic for flipping cards)
    ThreeTriosGame game = initializer.initializeGame(gridFilePath, cardsFilePath, new Random(),
            new StandardRule());

    game.startGame();

    // Validate initial state
    assertNotNull(game.getGrid());
    assertEquals(3, game.getGrid().getRows());
    assertEquals(3, game.getGrid().getCols());

    // Get the current player and opponent (Red first)
    Player playerRed = game.getCurrentPlayer();
    Player playerBlue = game.getOtherPlayer();

    // Get the cards from each player's hand (CardOne with a 2 attack value for Red, and CardTwo
    // with a 2 attack value for Blue)
    Card redCard = playerRed.getHand().get(0);  // Red's card (CardOne with attack value 2)
    Card blueCard = playerBlue.getHand().get(1);  // Blue's card (CardTwo with attack value 2)

    game.placeCard(0, 0, 0);  // Red places CardOne at (0, 0)
    game.endTurn();  // End Red's turn, switch to Blue

    // Blue places CardTwo at (0, 1)
    game.placeCard(0, 1, 1);

    // Assert that no ownership flips happen since both cards have equal attack values
    assertEquals(PlayerColor.RED, redCard.getCardOwner());  // Red should still own its card
    assertEquals(PlayerColor.BLUE, blueCard.getCardOwner());  // Blue should still own its card
  }

}