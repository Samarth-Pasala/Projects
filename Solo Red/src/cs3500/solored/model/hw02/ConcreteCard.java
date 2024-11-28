package cs3500.solored.model.hw02;

import java.util.Objects;

/**
 * A class representing a card in the SoloRedGameModel.
 * Has methods in it that are used throughout the code implementation.
 */
public class ConcreteCard implements Card {

  private char color;
  private int num;

  String orderColor = "ROBIV";

  /**
   * Constructor for the card.
   *
   * @param color represents the color of the card.
   * @param num   reresents the number of the card.
   */
  public ConcreteCard(char color, int num) {
    if (num < 1 || num > 7) {
      throw new IllegalArgumentException("Card number must be between 1 and 7");
    }
    if (orderColor.indexOf(color) == -1) {
      throw new IllegalArgumentException("Card color must be one of R, O, B, I, or V");
    }
    this.color = color;
    this.num = num;
  }

  /**
   * Compares two cards to ensure game rules are being followed.
   *
   * @param card the card being compared against.
   */
  public ConcreteCard compareCards(ConcreteCard card) {
    if (this.num > card.num) {
      return this;
    } else if (this.num < card.num) {
      return card;
    }

    int origColor = orderColor.indexOf(this.color);
    int newColor = orderColor.indexOf(card.color);
    if (origColor < newColor) {
      return this;
    } else {
      return card;
    }
  }

  /**
   * Compares two cards and returns the card with the greater value.
   *
   * @param card represents the card being compared against.
   */
  public boolean checkGreater(ConcreteCard card) {
    return this.getValue() > card.getValue();
  }

  /**
   * Gets the value of the card to show how close the color is to red.
   */
  public int getValue() {
    int value = 0;
    value += this.num * 10;

    switch (this.color) {
      case 'R':
        value += 5;
        break;
      case 'O':
        value += 4;
        break;
      case 'B':
        value += 3;
        break;
      case 'I':
        value += 2;
        break;
      case 'V':
        value += 1;
        break;
      default:
        break;
    }
    return value;
  }

  /**
   * gets the color of the card.
   */
  public char getColor() {
    return color;
  }

  /**
   * gets the number of the card.
   */
  public int getNum() {
    return num;
  }

  /**
   * gets value stored in the color.
   *
   * @param color represents the current color.
   */
  public void setColor(char color) {
    this.color = color;
  }

  /**
   * Converts the character representing color and the int representing number into one string.
   */
  @Override
  public String toString() {
    return "" + color + num;
  }

  /**
   * checks if a created object has the correct requirements.
   *
   * @param card is an object to check requirements against.
   */
  @Override
  public boolean equals(Object card) {
    return this == card || (this.color == ((ConcreteCard) card).getColor() &&
            this.num == ((ConcreteCard) card).getNum());
  }

  /**
   * method that maps both color and number together for the card.
   */
  @Override
  public int hashCode() {
    return Objects.hash(color, num);
  }
}

