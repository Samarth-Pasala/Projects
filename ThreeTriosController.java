package cs3500.threetrios.controller;

import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.view.GUIView;

/**
 * Represents a controller for a single player in the Three Trios game.
 */
public class ThreeTriosController implements ThreeTriosFeatures, ModelStateListener {

  private final ThreeTriosModel model;
  private final Player player;
  private final GUIView view;
  private int selectedCardIndex = -1;

  /**
   * Constructs a controller for the given model, player, and view.
   *
   * @param model  the game model.
   * @param player the player this controller controls.
   * @param view   the view associated with this controller and player.
   */
  public ThreeTriosController(ThreeTriosModel model, Player player, GUIView view) {
    this.model = model;
    this.player = player;
    this.view = view;
    this.view.setVisible(true);

    this.player.addActionListener(this);
    this.view.addFeaturesListener(this);
    this.model.addModelStateListener(this);
  }

  /**
   * Handles the selection of a card by the player.
   * Validates the selection to ensure the card is part of the player's hand and it is their turn.
   * If valid, updates the selected card index and re-renders the hand.
   *
   * @param cardIndex the index of the card to select.
   */
  @Override
  public void selectCard(int cardIndex) {
    if (model.getCurrentPlayer().getColor() != player.getColor()) {
      view.showMessageDialog("It's not your turn. You cannot select cards from the opponent's " +
              "hand.");
      return;
    }
    System.out.println(model.getCurrentPlayer().getHand());
    if (cardIndex < 0 || cardIndex >= model.getCurrentPlayer().getHand().size()) {
      view.showMessageDialog("Invalid card selection.");
      return;
    }
    this.selectedCardIndex = cardIndex;
    view.renderHand();
  }

  /**
   * Handles the selection of a cell on the game board by the player.
   * Validates that it is the player's turn and that a card is selected before attempting
   * to place the card. If valid, places the card on the board and updates the view.
   *
   * @param row the row of the cell to select.
   * @param col the column of the cell to select.
   */
  @Override
  public void selectCell(int row, int col) {
    if (model.getCurrentPlayer().getColor() != player.getColor()) {
      throw new IllegalStateException("It's not your turn.");
    }

    int rows = model.getGrid().getRows();
    int cols = model.getGrid().getCols();
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IllegalArgumentException("Invalid move: Cell is out of grid bounds.");
    }

    if (selectedCardIndex == -1) {
      throw new IllegalStateException("Please select a card first.");
    }

    try {
      model.placeCard(selectedCardIndex, row, col);
      view.getUpdatedHand(model.getOtherPlayer().getColor(),
              model.getPlayerHand(model.getOtherPlayer()));
      selectedCardIndex = -1;
      view.render();
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid move.", e);
    }
  }

  /**
   * Called when the model's state changes.
   * Implementing classes should define how they react to the model's updates.
   */
  @Override
  public void updateView() {
    view.getUpdatedHand(model.getCurrentPlayer().getColor(),
            model.getPlayerHand(model.getCurrentPlayer()));
    view.getUpdatedHand(model.getOtherPlayer().getColor(),
            model.getPlayerHand(model.getOtherPlayer()));
    view.render();

    if (model.isGameOver()) {
      Player winner = model.getCurrentPlayer();
      view.showWinnerDialog();
    }
  }

  @Override
  public void initializeGame() {
    this.view.setVisible(true);
  }

}
