package cs3500.threetrios.controller;

import cs3500.threetrios.model.PlayerColor;

/**
 * Interface for listening to player turn events in the game.
 */
public interface PlayerTurnListener {

  /**
   * Notifies the player that it's their turn.
   */
  void onTurn(PlayerColor player);
}
