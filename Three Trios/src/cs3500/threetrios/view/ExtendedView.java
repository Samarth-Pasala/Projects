package cs3500.threetrios.view;

import cs3500.threetrios.controller.ThreeTriosFeatures;

/**
 * Interface for a view component in the Three Trios game that extends the basic model-view
 * functionality with additional capabilities to interact with player actions and display messages.
 */
public interface ExtendedView extends ModelView {

  /**
   * Adds a Features listener to handle player actions.
   *
   * @param features the Features listener to register.
   */
  void addFeaturesListener(ThreeTriosFeatures features);

  /**
   * Displays an error message dialog to the player.
   *
   * @param message the message to display in the dialog.
   */
  void showMessageDialog(String message);
}
