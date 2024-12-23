package cs3500.threetrios.view;

import cs3500.threetrios.controller.ThreeTriosFeatures;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardDirection;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ReadonlyThreeTriosModel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * The GUIView class is the graphical user interface (GUI) for the ThreeTrios game.
 * It provides a visual representation of the game state, including the game grid and the
 * players' hands.
 */
public class GUIView extends JFrame implements ExtendedView {

  private ReadonlyThreeTriosModel model;
  private HandPanel leftHandPanel;
  private HandPanel rightHandPanel;
  private final GridPanel gridPanel;
  private int selectedCardIndex = -1;
  private final int height = 800;
  private boolean hintsEnabled = false;

  /**
   * Constructs a GUIView with the specified model.
   *
   * @param model the ReadonlyThreeTriosModel to initialize the view with.
   *              This model provides the state of the game that the view will display.
   */
  public GUIView(ReadonlyThreeTriosModel model) {
    this.model = model;

    String currentPlayerColor = model.getCurrentPlayer().getColor().toString();
    setTitle("Current Player: " + currentPlayerColor);

    setSize(1200, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setLayout(new BorderLayout());

    leftHandPanel = new HandPanel(Color.PINK, model.getPlayerHand(model.getCurrentPlayer()),
            "RED");
    rightHandPanel = new HandPanel(Color.CYAN, model.getPlayerHand(model.getOtherPlayer()),
            "BLUE");

    Dimension sidePanelSize = new Dimension(200, 800);
    leftHandPanel.setPreferredSize(sidePanelSize);
    rightHandPanel.setPreferredSize(sidePanelSize);

    gridPanel = new GridPanel();
    gridPanel.setPreferredSize(new Dimension(800, 800));

    add(leftHandPanel, BorderLayout.WEST);
    add(gridPanel, BorderLayout.CENTER);
    add(rightHandPanel, BorderLayout.EAST);

    JButton toggleHintsButton = new JButton("Toggle Hints");
    toggleHintsButton.addActionListener(this::handleToggleHints);
    add(toggleHintsButton, BorderLayout.SOUTH);
  }

  private void handleToggleHints(ActionEvent e) {
    hintsEnabled = !hintsEnabled;

    if (hintsEnabled && selectedCardIndex >= 0) {
      gridPanel.setDecorator(new HintDecorator(model, selectedCardIndex));
    } else {
      gridPanel.setDecorator(null);
    }
  }

  /**
   * Displays a message dialog with the given message.
   *
   * @param message the message to display in the dialog.
   */
  @Override
  public void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message, "Message",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Displays a dialog with information about the winner of the game when the game ends.
   */
  public void showWinnerDialog() {
    Player winner = model.determineWinner();
    int winnerScore = model.getPlayerScore(winner);
    String message = "Game Over! The winner is: " + winner.getColor().toString() +
            "\nWinning Score: " + winnerScore;
    JOptionPane.showMessageDialog(this, message, "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Updates the displayed hand of a player based on their updated list of cards.
   *
   * @param color       the color of the player whose hand is being updated.
   * @param updatedHand the updated list of cards in the player's hand.
   */
  public void getUpdatedHand(PlayerColor color, List<Card> updatedHand) {
    if (color == PlayerColor.RED) {
      leftHandPanel.updateHand(updatedHand);
    } else {
      rightHandPanel.updateHand(updatedHand);
    }
  }


  /**
   * Adds a Features listener to handle player actions.
   *
   * @param features the Features listener to register.
   */
  @Override
  public void addFeaturesListener(ThreeTriosFeatures features) {

    leftHandPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (model.getCurrentPlayer().getColor() != PlayerColor.RED) {
          showMessageDialog("You cannot select cards from the opponent's hand.");
          return;
        }

        int cardHeight = leftHandPanel.getHeight() / leftHandPanel.getHandSize();
        int cardIndex = e.getY() / cardHeight;

        if (cardIndex >= 0 && cardIndex < leftHandPanel.getHandSize()) {
          features.selectCard(cardIndex);
        }
      }
    });

    rightHandPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (model.getCurrentPlayer().getColor() != PlayerColor.BLUE) {
          showMessageDialog("You cannot select cards from the opponent's hand.");
          return;
        }

        int cardHeight = rightHandPanel.getHeight() / rightHandPanel.getHandSize();
        int cardIndex = e.getY() / cardHeight;

        if (cardIndex >= 0 && cardIndex < rightHandPanel.getHandSize()) {
          features.selectCard(cardIndex);
        }
      }
    });


    gridPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int rows = model.getGrid().getRows();
        int cols = model.getGrid().getCols();
        int cellSize = Math.min(gridPanel.getWidth() / cols, gridPanel.getHeight() / rows);

        int row = e.getY() / cellSize;
        int col = e.getX() / cellSize;

        if (row >= 0 && row < rows && col >= 0 && col < cols) {
          features.selectCell(row, col);
        }
      }
    });
  }

  /**
   * Renders the entire game state, including the current player, grid, and hand.
   */
  @Override
  public void render() {
    renderCurrentPlayer();
    renderGrid();
    renderHand();
  }

  /**
   * Renders the current player and updates the window title.
   */
  @Override
  public void renderCurrentPlayer() {
    Player currentPlayer = model.getCurrentPlayer();
    setTitle("Three Trios - Current Player: " + currentPlayer.getColor());
  }

  /**
   * Renders the game grid by repainting the grid panel.
   */
  @Override
  public void renderGrid() {
    gridPanel.repaint();
  }

  /**
   * Renders the current player's hand by repainting the hand panels.
   */
  @Override
  public void renderHand() {
    leftHandPanel.repaint();
    rightHandPanel.repaint();
  }

  /**
   * HandPanel represents the hand of a player. It displays the player's cards
   * in a vertical list and allows the user to select a card by clicking on it.
   */
  private class HandPanel extends JPanel {
    private final Color panelColor;
    private List<Card> hand;
    private final String playerColor;

    /**
     * Constructs a HandPanel with the specified color, hand, and player color.
     *
     * @param color       the background color of the hand panel.
     * @param hand        the list of cards in the player's hand.
     * @param playerColor the color of the player ("RED" or "BLUE").
     */
    public HandPanel(Color color, List<Card> hand, String playerColor) {
      this.panelColor = color;
      this.hand = hand;
      this.playerColor = playerColor;
      setBackground(panelColor);

      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (!model.getCurrentPlayer().getColor().toString().equals(playerColor)) {
            // Do nothing if this panel does not belong to the current player
            return;
          }

          int cardHeight = getHeight() / hand.size();
          int cardIndex = e.getY() / cardHeight;

          if (cardIndex < 0 || cardIndex >= hand.size()) {
            return;
          }

          if (cardIndex == selectedCardIndex) {
            selectedCardIndex = -1; // Deselect if already selected
          } else {
            selectedCardIndex = cardIndex;
          }
          repaint();

          System.out.println("Card clicked at index: " + cardIndex + ", Player: " + playerColor);
        }
      });
    }

    /**
     * Returns the number of cards in the player's hand.
     *
     * @return the number of cards in the hand.
     */
    public int getHandSize() {
      return hand.size();
    }

    /**
     * Updates the displayed hand with the specified list of cards.
     *
     * @param updatedHand the updated list of cards to display.
     */
    public void updateHand(List<Card> updatedHand) {
      this.hand.clear();
      this.hand.addAll(updatedHand);
      repaint();
    }

    /**
     * Handles the mouse click event to select a card from the hand.
     *
     * @param mouseY the Y-coordinate of the mouse click.
     */
    private void handleHandClick(int mouseY) {
      if (hand.isEmpty()) {
        return;
      }

      int cardHeight = getHeight() / hand.size();
      int cardIndex = mouseY / cardHeight;

      if (cardIndex < 0 || cardIndex >= hand.size()) {
        return;
      }

      selectedCardIndex = (selectedCardIndex == cardIndex) ? -1 : cardIndex; // Toggle selection
      if (hintsEnabled) {
        gridPanel.setDecorator(selectedCardIndex >= 0
                ? new HintDecorator(model, selectedCardIndex)
                : null);
      }
      repaint();
    }

    /**
     * Paints the hand panel with the cards and their values.
     *
     * @param g the Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      int panelWidth = getWidth();
      int panelHeight = getHeight();
      int cardHeight = hand.isEmpty() ? 0 : panelHeight / hand.size();

      Font font = new Font("SansSerif", Font.BOLD, 20);
      g.setFont(font);

      int offsetY = 20;
      int offsetX = 15;

      for (int i = 0; i < hand.size(); i++) {
        Card card = hand.get(i);
        int startY = i * cardHeight;

        // Highlight the card only if it belongs to the current player and is selected
        if (model.getCurrentPlayer().getColor().toString().equals(playerColor)
                && i == selectedCardIndex) {
          g.setColor(Color.LIGHT_GRAY);
        } else {
          g.setColor(panelColor);
        }
        g.fillRect(0, startY, panelWidth, cardHeight);

        g.setColor(Color.BLACK);

        int centerX = panelWidth / 2;
        int centerY = startY + cardHeight / 2;


        g.drawString(card.getAttackValue(CardDirection.NORTH).toString(),
                centerX - 5, centerY - offsetY);
        g.drawString(card.getAttackValue(CardDirection.SOUTH).toString(),
                centerX - 5, centerY + offsetY);
        g.drawString(card.getAttackValue(CardDirection.EAST).toString(),
                centerX + offsetX, centerY);
        g.drawString(card.getAttackValue(CardDirection.WEST).toString(),
                centerX - offsetX - 10, centerY);

        g.drawLine(0, startY + cardHeight, panelWidth, startY + cardHeight);
      }
    }
  }

  /**
   * GridPanel represents the game grid, where cards are placed during the game.
   * The grid is displayed as a grid of cells, and cards are drawn in the cells.
   */
  private class GridPanel extends JPanel {
    private GridDecorator decorator; // Optional decorator for additional rendering (e.g., hints)

    /**
     * Constructs a GridPanel to display the game grid.
     */
    public GridPanel() {
      setBackground(Color.CYAN);

      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          handleGridClick(e.getX(), e.getY());
        }
      });
    }

    /**
     * Handles the mouse click event to select a grid cell for placing a card.
     *
     * @param mouseX the X-coordinate of the mouse click.
     * @param mouseY the Y-coordinate of the mouse click.
     */
    private void handleGridClick(int mouseX, int mouseY) {
      int rows = model.getGrid().getRows();
      int cols = model.getGrid().getCols();
      int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

      int row = mouseY / cellSize;
      int col = mouseX / cellSize;

      if (row < 0 || row >= rows || col < 0 || col >= cols) {
        return;
      }

      System.out.println("Grid cell clicked at coordinates: (" + row + ", " + col + ")");
    }

    /**
     * Sets the decorator for additional rendering, such as hints.
     *
     * @param decorator the GridDecorator to set, or null to disable decoration.
     */
    public void setDecorator(GridDecorator decorator) {
      this.decorator = decorator;
      repaint();
    }

    /**
     * Paints the grid panel with cells, the cards placed on the grid, and any additional
     * decoration.
     *
     * @param g the Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      int rows = model.getGrid().getRows();
      int cols = model.getGrid().getCols();
      int cellSize = Math.min(getWidth() / cols, height / rows);

      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          if (model.getGrid().getCell(row, col).isHole()) {
            g.setColor(Color.LIGHT_GRAY);
          } else {
            g.setColor(Color.YELLOW);
          }
          g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
          g.setColor(Color.BLACK);
          g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
        }
      }

      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          Card card = model.getGrid().getCell(row, col).getCard();
          if (card != null) {
            Color cardColor = card.getCardOwner() == PlayerColor.RED ? Color.PINK : Color.CYAN;
            g.setColor(cardColor);
            g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);

            g.setColor(Color.BLACK);
            drawCardInDiamondFormat(g, card, col * cellSize, row * cellSize, cellSize);
          }
        }
      }

      if (decorator != null) {
        decorator.decorate(g, rows, cols, cellSize);
      }
    }

    /**
     * Helper method to draw a card in a diamond format on the grid.
     *
     * @param g        The Graphics object.
     * @param card     The card to be drawn.
     * @param x        The x-coordinate of the cell's top-left corner.
     * @param y        The y-coordinate of the cell's top-left corner.
     * @param cellSize The size of the cell.
     */
    private void drawCardInDiamondFormat(Graphics g, Card card, int x, int y, int cellSize) {
      int centerX = x + cellSize / 2;
      int centerY = y + cellSize / 2;
      int offset = cellSize / 4;

      g.drawString(card.getAttackValue(CardDirection.NORTH).toString(),
              centerX - 5, centerY - offset);
      g.drawString(card.getAttackValue(CardDirection.SOUTH).toString(),
              centerX - 5, centerY + offset);
      g.drawString(card.getAttackValue(CardDirection.EAST).toString(),
              centerX + offset, centerY);
      g.drawString(card.getAttackValue(CardDirection.WEST).toString(),
              centerX - offset - 10, centerY);
    }
  }

}
