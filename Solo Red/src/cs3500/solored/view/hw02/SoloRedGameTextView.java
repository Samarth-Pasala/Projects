package cs3500.solored.view.hw02;

import java.io.IOException;
import java.util.List;

import cs3500.solored.model.hw02.RedGameModel;

/**
 * class that represents the view for the model.
 * has a ToString method for users to see the model easily.
 */
public class SoloRedGameTextView implements RedGameView {

  private final RedGameModel<?> model;
  private final Appendable ap;

  /**
   * Constructor that initializes the model.
   *
   * @param model represents the model,
   */
  public SoloRedGameTextView(RedGameModel<?> model) {
    this.model = model;
    this.ap = new StringBuilder();
  }

  /**
   * Constructor that initializes the model and the Appendable.
   *
   * @param model represents the model,
   * @param ap    represents the destination for the output.
   */
  public SoloRedGameTextView(RedGameModel<?> model, Appendable ap) {
    if (ap == null) {
      throw new IllegalArgumentException("appendable cannot be null");
    }
    this.model = model;
    this.ap = ap;
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render() throws IOException {
    if (ap != null) {
      this.ap.append(this.toString());
    }
  }

  /**
   * gets the result and outputs it to the user.
   */
  public String getResult() {
    return this.ap.toString();
  }

  /**
   * Converts into a string to display to user / client.
   */
  @Override
  public String toString() {
    StringBuilder canvasBuilder = new StringBuilder();
    canvasBuilder.append("Canvas: ").append(model.getCanvas().toString().charAt(0)).append("\n");

    for (int i = 0; i < model.numPalettes(); i++) {
      if (i == model.winningPaletteIndex()) {
        canvasBuilder.append("> ");
      }
      canvasBuilder.append("P").append(i + 1).append(": ").append(cardList(model.getPalette(i)));
      if (canvasBuilder.length() > 0) {
        canvasBuilder.deleteCharAt(canvasBuilder.length() - 1);
      }
      canvasBuilder.append("\n");
    }

    String cardsInHand = cardList(model.getHand());
    if (!cardsInHand.isEmpty()) {
      cardsInHand = cardsInHand.substring(0, cardsInHand.length() - 1);
    }
    canvasBuilder.append("Hand: ").append(cardsInHand);

    return canvasBuilder.toString();
  }

  /**
   * provides the list of cards.
   *
   * @param palette List of the number of palettes.
   */
  private String cardList(List<?> palette) {
    StringBuilder canvasBuilder = new StringBuilder();
    if (palette.isEmpty()) {
      return " ";
    }
    for (Object card : palette) {
      canvasBuilder.append(card.toString()).append(" ");
    }
    return canvasBuilder.toString();
  }

}
