import tester.Tester;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//Represents a single square of the game area
class Cell {

  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;

  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, ArrayList<Color> c) {
    this.x = x;
    this.y = y;
    this.color = c.get(new Random().nextInt(c.size()));
    this.flooded = false;
  }

  Cell(int x, int y, Color c) {
    this.x = x;
    this.y = y;
    this.color = c;
    this.flooded = false;
  }

  /*
  TEMPLATE
  FIELDS:
  ... this.x ...         -- int
  ... this.y ...         -- int
  ... this.color ...     -- Color
  ... this.flooded ...   -- boolean
  ... this.left ...      -- Cell
  ... this.top ...       -- Cell
  ... this.right ...     -- Cell
  ... this.bottom ...    -- Cell

  METHODS:
  ... this.createCell(int) ...       -- WorldImage
  ... this.flood(Color) ...          -- void
  ... this.floodHelp(Color) ...      -- void
  ... this.adjacent(ArrayList<Cell>) ... -- ArrayList<Cell>

  METHODS FOR FIELDS: N/A
   */


  // draws cell as a rectangle
  WorldImage createCell(int cellSize) {
    return new RectangleImage(cellSize, cellSize, OutlineMode.SOLID, this.color);
  }

  // EFFECT: floods cells of the same color 
  void flood(Color check) {
    if (this.flooded) {
      if (this.top != null) {
        this.top.floodHelp(check);
      }
      if (this.right != null) {
        this.right.floodHelp(check);
      }
      if (this.bottom != null) {
        this.bottom.floodHelp(check);
      }
      if (this.left != null) {
        this.left.floodHelp(check);
      }
    }
  }

  // EFFECT: checks if color matches and if cell is not already flooded
  void floodHelp(Color check) {
    if (!this.flooded && this.color.equals(check)) {
      this.flooded = true;
    }
  }

  // gets right and bottom cells of current cell 
  ArrayList<Cell> adjacent(ArrayList<Cell> completed) {
    ArrayList<Cell> copy = new ArrayList<Cell>();
    if (this.right != null && !completed.contains(this.right)) {
      copy.add(this.right);
    }
    if (this.bottom != null && !completed.contains(this.bottom)) {
      copy.add(this.bottom);
    }
    return copy;
  }


}

// represents the game world 
class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> board;
  ArrayList<Cell> cells;
  ArrayList<Cell> floodedCells;
  int cellSize;
  int boardSize;
  ArrayList<Color> c;
  int num;
  Color currentC;
  int moves;
  int steps;
  boolean checkWin;


  FloodItWorld(int boardSize, int num, int cellSize) {
    this.boardSize = boardSize;
    this.num = num;
    this.cellSize = cellSize;
    this.c = new ArrayList<Color>();
    for (int i = 0; i < num; i++) {
      this.c.add(new Color(new Random().nextInt(256), 
          new Random().nextInt(256), new Random().nextInt(256)));
    }
    makeBoard();
  }

  /*
  TEMPLATE
  FIELDS:
  ... this.board ...         -- ArrayList<Cell>
  ... this.cellSize ...      -- int
  ... this.boardSize ...     -- int
  ... this.c ...             -- ArrayList<Color>
  ... this.num ...           -- int
  ... this.cells ...         -- ArrayList<Cell>
  ... this.floodedCells ...  -- ArrayList<Cell>
  ... this.currentC ...      -- Color
  ... this.moves ...         -- int
  ... this.steps ...         -- int
  ... this.checkWin ...      -- boolean

  METHODS:
  ... this.makeBoard() ...      -- void
  ... this.makeNext(Cell) ...   -- void
  ... this.makeScene() ...      -- WorldScene
  ... this.onTick() ...         -- void
  ... this.waterfall() ...      -- void
  ... this.result() ...         -- boolean
  ... this.flood() ...          -- void
  ... this.onKeyEvent(String) ... -- void
  ... this.onMouseClicked(Posn) ... -- void

  METHODS FOR FIELDS: 
  ... this.rect.createCell(int) ... -- WorldImage
  ... this.rect.flood(Color) ...    -- void
  ... this.rect.floodHelp(Color) ... -- void
  ... this.rect.adjacent(ArrayList<Cell>) ... -- ArrayList<Cell>
   */


  // initializes game board with cells
  void makeBoard() {
    this.board = new ArrayList<Cell>();
    for (int y = 0; y < boardSize; y++) {
      for (int x = 0; x < boardSize; x++) {
        this.board.add(new Cell(x, y, this.c));
      }
    }
    for (Cell rect : board) {
      makeNext(rect);
    }
    Cell orig = this.board.get(0);
    orig.flood(orig.color);
    orig.flooded = true;  
    this.currentC = orig.color;
    this.steps = 0;
    this.moves = (int) Math.round(1.75 * boardSize) + 3;
    this.cells = new ArrayList<Cell>(Arrays.asList(orig));
    this.floodedCells = new ArrayList<Cell>();
    this.checkWin = false;

  }

  // makes neighboring cells
  void makeNext(Cell rect) {
    int x = rect.x;
    int y = rect.y;
    if (y > 0) {
      rect.top = board.get((y - 1) * (boardSize) + (x));
    }
    if (x > 0) {
      rect.left = board.get((x - 1) + (boardSize) * (y));
    }
    if (y < boardSize - 1) {
      rect.bottom = board.get((y + 1) * (boardSize) + (x));
    }
    if (x < boardSize - 1) {
      rect.right = board.get((x + 1) + (boardSize) * (y));
    }
  }

  // EFFECT: produces waterfall effect in game
  public void onTick() {
    if (this.cells.size() > 0) {
      this.waterfall();
    }
    if (this.result() && this.steps <= this.moves) {
      this.checkWin = true;
    }
  }

  // EFFECT: visually shows waterfall effect
  public void waterfall() {
    ArrayList<Cell> temp = new ArrayList<Cell>();
    for (Cell rect : this.cells) {
      if (rect.flooded) {
        rect.color = this.currentC;
      }
      temp.addAll(rect.adjacent(this.floodedCells));
      this.floodedCells.addAll(rect.adjacent(this.floodedCells));
    }
    this.cells = new ArrayList<Cell>();
    this.cells.addAll(temp);
  }

  // checks if game is won
  public boolean result() {
    for (Cell rect : this.board) {
      if (!rect.flooded) {
        return false;
      }
    }
    return true;
  }

  // EFFECT: sets flooded cells equal to true 
  public void flood() {
    for (Cell rect : this.board) {
      rect.flood(this.currentC);
    }
    this.cells = new ArrayList<Cell>(Arrays.asList(this.board.get(0)));
    this.floodedCells = new ArrayList<Cell>();
  }

  // EFFECT: resets the game
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      makeBoard();
    }
  }

  // EFFECT: handles mouse inputs
  public void onMouseClicked(Posn pos) {
    int xCoordinate = pos.x / cellSize;
    int yCoordinate = pos.y / cellSize;
    if (xCoordinate >= 0 && xCoordinate < boardSize && 
        yCoordinate >= 0 && yCoordinate < boardSize) {
      Cell chosenCell = this.board.get(yCoordinate * boardSize + xCoordinate);
      if (!chosenCell.color.equals(this.currentC)) {
        this.steps++;
        this.currentC = chosenCell.color;
        this.flood();
      }
    }
  }


  // makes the scene
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene((boardSize * cellSize), (boardSize * cellSize));
    for (Cell rect : board) {
      scene.placeImageXY(rect.createCell(cellSize), rect.x * cellSize + cellSize / 2, 
          rect.y * cellSize + cellSize / 2);
    }
    // displays the score of the user, lower score is better
    scene.placeImageXY(new TextImage("Score: " + this.steps + " of " + this.moves, 
        15, Color.BLACK), 420, 18);
    if (this.checkWin) {
      scene.placeImageXY(new TextImage("You Win!!!", 50, Color.BLUE),
          boardSize * cellSize / 2, boardSize * cellSize / 2);
    }
    else if (this.steps > this.moves) {
      scene.placeImageXY(new TextImage("You Lose!!!", 50, Color.BLUE),
          boardSize * cellSize / 2, boardSize * cellSize / 2);
    }
    return scene;
  }
}

//represents examples 
class ExamplesFlood {

  ExamplesFlood() {}

  boolean testBigBang(Tester t) {
    // keep cellSize the same to ensure it fills entire canvas
    FloodItWorld game = new FloodItWorld(25, 5, 20); 
    game.bigBang(500, 500, 0.1);
    return true;
  }

  void testMakeBoard(Tester t) {
    FloodItWorld world = new FloodItWorld(3, 5, 20);
    Cell centerCell = world.board.get(4); 

    t.checkExpect(centerCell.left, world.board.get(3));
    t.checkExpect(centerCell.right, world.board.get(5));
    t.checkExpect(centerCell.top, world.board.get(1));
    t.checkExpect(centerCell.bottom, world.board.get(7));
  }

  void testMakeNext(Tester t) {
    // Create a small 2x2 board
    Cell cell00 = new Cell(0, 0, Color.RED);
    Cell cell01 = new Cell(0, 1, Color.BLUE);
    Cell cell10 = new Cell(1, 0, Color.GREEN);
    Cell cell11 = new Cell(1, 1, Color.YELLOW);

    ArrayList<Cell> board = new ArrayList<Cell>();
    board.add(cell00);
    board.add(cell01);
    board.add(cell10);
    board.add(cell11);

    FloodItWorld world = new FloodItWorld(2, 2, 20);
    world.board = board;

    // Manually set the neighbors
    world.makeNext(cell00);
    world.makeNext(cell01);
    world.makeNext(cell10);
    world.makeNext(cell11);

    // Check neighbors for cell at (0,0)
    t.checkExpect(cell00.left, null);
    t.checkExpect(cell00.top, null);

    // Check neighbors for cell at (0,1)
    t.checkExpect(cell01.right, cell11);
    t.checkExpect(cell01.bottom, null);
    t.checkExpect(cell01.left, null);
    t.checkExpect(cell01.top, cell00);

    // Check neighbors for cell at (1,0)
    t.checkExpect(cell10.right, null);
    t.checkExpect(cell10.bottom, cell11);
    t.checkExpect(cell10.left, cell00);
    t.checkExpect(cell10.top, null);

    // Check neighbors for cell at (1,1)
    t.checkExpect(cell11.right, null);
    t.checkExpect(cell11.bottom, null);
  }

  void testCreateCell(Tester t) {
    Cell cell = new Cell(0, 0, Color.BLUE); 
    t.checkExpect(cell.createCell(20), new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE));
  }

  void testFlood(Tester t) {
    Cell cell01 = new Cell(0, 0, Color.BLUE);
    Cell cell02 = new Cell(0, 1, Color.BLUE);
    cell01.bottom = cell02;
    cell01.flooded = true;
    cell01.flood(Color.RED);
    t.checkExpect(cell01.flooded, true);
    t.checkExpect(cell02.flooded, false);
  }

  void testFloodHelp(Tester t) {
    Cell cell01 = new Cell(0, 0, Color.BLUE);
    Cell cell02 = new Cell(0, 1, Color.BLUE);
    cell01.bottom = cell02;
    cell01.flooded = true;
    cell01.floodHelp(Color.RED);
    t.checkExpect(cell01.flooded, true);
    t.checkExpect(cell02.flooded, false);
  }


  void testAdjacent(Tester t) {
    Cell cell01 = new Cell(0, 0, Color.BLUE);
    Cell cell02 = new Cell(0, 1, Color.BLUE);
    Cell cell03 = new Cell(1, 0, Color.BLUE);
    cell01.right = cell03;
    cell01.bottom = cell02;
    ArrayList<Cell> check = new ArrayList<Cell>();
    ArrayList<Cell> neighbor = cell01.adjacent(check);
    t.checkExpect(neighbor.contains(cell02), true);
    t.checkExpect(neighbor.contains(cell03), true);
    t.checkExpect(neighbor.size(), 2);

  }

  void testOnTick(Tester t) {
    FloodItWorld world = new FloodItWorld(3, 5, 20);
    world.cells = new ArrayList<Cell>(Arrays.asList(world.board.get(0)));
    world.floodedCells = new ArrayList<Cell>();
    world.onTick();
    t.checkExpect(world.cells.size(), 2);
  }

  void testWaterfall(Tester t) {
    FloodItWorld world = new FloodItWorld(3, 5, 20);
    Cell cell01 = world.board.get(0);
    Cell cell02 = world.board.get(1);
    Cell cell03 = world.board.get(2);
    Cell cell04 = world.board.get(3);
    Cell cell05 = world.board.get(4);
    Cell cell06 = world.board.get(5);
    Cell cell07 = world.board.get(6);
    Cell cell08 = world.board.get(7);
    Cell cell09 = world.board.get(8);

    cell01.color = Color.RED;
    cell02.color = Color.RED;
    cell03.color = Color.BLUE;
    cell04.color = Color.RED;
    cell05.color = Color.BLUE;
    cell06.color = Color.RED;
    cell07.color = Color.RED;
    cell08.color = Color.RED;
    cell09.color = Color.RED;

    cell01.flooded = true;
    cell02.flooded = true;
    cell04.flooded = true;

    world.cells = new ArrayList<Cell>(Arrays.asList(cell01, cell02, cell04));
    world.floodedCells = new ArrayList<Cell>();

    world.currentC = Color.RED;

    t.checkExpect(world.cells.size(), 3);
    t.checkExpect(world.floodedCells.size(), 0);

    world.waterfall();

    t.checkExpect(world.cells.size(), 5);
    t.checkExpect(world.floodedCells.size(), 5);
    t.checkExpect(cell01.color, Color.RED);
    t.checkExpect(cell01.color, Color.RED);
    t.checkExpect(cell04.color, Color.RED);
  }

  void testOnMouseClicked(Tester t) {
    FloodItWorld world = new FloodItWorld(3, 5, 20);
    world.currentC = Color.BLUE;
    world.board.get(0).color = Color.RED;
    t.checkExpect(world.currentC, Color.BLUE);
    world.onMouseClicked(new Posn(7, 7));
    t.checkExpect(world.currentC, Color.RED);
  }

  void testFlood2(Tester t) {
    FloodItWorld world = new FloodItWorld(3, 5, 20);
    world.board.get(0).color = Color.BLUE;
    world.board.get(1).color = Color.BLUE;
    world.board.get(2).color = Color.BLUE;
    world.board.get(3).color = Color.BLUE;
    world.currentC = Color.black;
    world.flood();
    t.checkExpect(world.board.get(0).flooded, true);
    t.checkExpect(world.board.get(1).flooded, false);
    t.checkExpect(world.board.get(2).flooded, false);
  }

  void testResult(Tester t) {
    FloodItWorld world = new FloodItWorld(3, 5, 20);
    for (Cell i : world.board) {
      i.flooded = true;
    }
    t.checkExpect(world.result(), true);
  }

  void testOnKeyEvent(Tester t) {
    FloodItWorld world = new FloodItWorld(3, 5, 20);
    world.onKeyEvent("r"); 
    t.checkExpect(world.cells.size(), 1); 
    t.checkExpect(world.steps, 0); 
  }
}