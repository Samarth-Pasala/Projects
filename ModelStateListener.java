package cs3500.threetrios.controller;

/**
 * Represents a listener for changes in the model's state.
 * This interface allows objects (e.g., views) to update when the model notifies of changes.
 */
public interface ModelStateListener {

  /**
   * Called when the model's state changes.
   * Implementing classes should define how they react to the model's updates.
   */
  void updateView();

  void initializeGame();
}