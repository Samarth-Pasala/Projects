package cs3500.threetrios.model;


/**
 * Represents the possible attack values for the cards in the game.
 * Attack values can range from 1 to 9, with 'A' representing 10.
 */
public enum AttackValue {
  A(10),  // 'A' = 10
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9);


  private final int value;


  /**
   * Constructs an AttackValue with the specified integer value.
   *
   * @param value the integer value associated with this attack value
   */
  AttackValue(int value) {
    this.value = value;
  }


  /**
   * Returns the integer value associated with this attack value.
   *
   * @return the integer value of this attack value
   */
  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    if (this == A) {
      return "A";
    }
    return String.valueOf(value);
  }
}
