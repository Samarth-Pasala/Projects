package cs3500.solored.model.hw02;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * class that tests the ConcreteCard class.
 * tests all methods that are implemented within the class.
 */
public class TestConcreteCard {

  /**
   * tests that two cards are compared correctly.
   */
  @Test
  public void testCompareCard() {
    ConcreteCard card1 = new ConcreteCard('R', 1);
    ConcreteCard card2 = new ConcreteCard('B', 3);
    Assert.assertEquals(card2, card1.compareCards(card2));
  }

  /**
   * tests that compared cards' numbers are properly compared.
   */
  @Test
  public void testCompareCardsGreaterNum() {
    ConcreteCard card1 = new ConcreteCard('R', 5);
    ConcreteCard card2 = new ConcreteCard('O', 3);
    Assert.assertEquals(card1, card1.compareCards(card2));
  }

  /**
   * tests that compared cards' numbers are properly compared.
   */
  @Test
  public void testCompareCardsLesserNum() {
    ConcreteCard card1 = new ConcreteCard('R', 2);
    ConcreteCard card2 = new ConcreteCard('O', 4);
    Assert.assertEquals(card2, card1.compareCards(card2));
  }

  /**
   * tests that different colors are compared correctly.
   */
  @Test
  public void testCompareCardsEqualNumDifferentColor() {
    ConcreteCard card1 = new ConcreteCard('R', 3);
    ConcreteCard card2 = new ConcreteCard('O', 3);
    Assert.assertEquals(card1, card1.compareCards(card2));
  }

  /**
   * tests that bigger numbers are greater than smaller numbers.
   */
  @Test
  public void testCheckGreaterTrue() {
    ConcreteCard card1 = new ConcreteCard('R', 5);
    ConcreteCard card2 = new ConcreteCard('O', 3);
    Assert.assertTrue(card1.checkGreater(card2));
  }

  /**
   * tests that smaller numbers aren't greater than larger numbers.
   */
  @Test
  public void testCheckGreaterFalse() {
    ConcreteCard card1 = new ConcreteCard('R', 2);
    ConcreteCard card2 = new ConcreteCard('O', 3);
    Assert.assertFalse(card1.checkGreater(card2));
  }

  /**
   * tests that value is properly computed and attained.
   */
  @Test
  public void testGetValue() {
    ConcreteCard card = new ConcreteCard('R', 5);
    Assert.assertEquals(55, card.getValue());
  }

  /**
   * tests that color is properly attained.
   */
  @Test
  public void testGetColor() {
    ConcreteCard card = new ConcreteCard('R', 1);
    Assert.assertEquals('R', card.getColor());
  }

  /**
   * tests that number is properly attained.
   */
  @Test
  public void testGetNum() {
    ConcreteCard card = new ConcreteCard('R', 1);
    Assert.assertEquals(1, card.getNum());
  }

  /**
   * tests that color and number are converted to string.
   */
  @Test
  public void testToString() {
    ConcreteCard card = new ConcreteCard('R', 1);
    Assert.assertEquals("R1", card.toString());
  }

  /**
   * tests that two cards are equal to each other.
   */
  @Test
  public void testEqualsTrue() {
    ConcreteCard card1 = new ConcreteCard('R', 1);
    ConcreteCard card2 = new ConcreteCard('R', 1);
    Assert.assertTrue(card1.equals(card2));
  }

  /**
   * tests that equals doesn't misfunction.
   */
  @Test
  public void testEqualsFalseDifferentNum() {
    ConcreteCard card1 = new ConcreteCard('R', 1);
    ConcreteCard card2 = new ConcreteCard('R', 2);
    Assert.assertFalse(card1.equals(card2));
  }

  /**
   * tests that hash merges properties accordingly.
   */
  @Test
  public void testHashCode() {
    ConcreteCard card = new ConcreteCard('R', 1);
    int expectedHashCode = Objects.hash('R', 1);
    Assert.assertEquals(expectedHashCode, card.hashCode());
  }
}
