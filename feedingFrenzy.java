import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

// represents a world class to animate fish on a scene
class FishGame extends World {
  int width;
  int height;
  Player player;
  ILoFish listFish;
  Random rand;

  FishGame(int width, int height, Player player, ILoFish listFish, Random rand) {
    this.width = width;
    this.height = height;
    this.player = player;
    this.listFish = listFish;
    this.rand = rand;
  }

  /*
  TEMPLATE:
  Fields:
  ... this.width ...                -- int
  ... this.height ...               -- int
  ... this.player ...               -- Player
  ... this.listFish ...             -- ILoFish
  ... this.rand ...                 -- Random

  Methods:
  ... this.makeScene() ...          -- WorldScene
  ... this.onTick() ...             -- FishGame
  ... this.onKeyEvent(String) ...   -- World
  ... this.worldEnds() ...          -- WorldEnd
  ... this.lastScene(String) ...    -- WorldScene
  ... this.playerScore(WorldScene) ...              -- WorldScene

  Methods for Fields:
  ... this.player.draw(WorldScene) ...              -- WorldScene
  ... this.listFish.move() ...                      -- ILoFish
  ... this.listFish.checkCollision(Player) ...      -- ILoFish
  ... this.rand.nextInt(int) ...                    -- int
  ... this.player.movingFish(String) ...            -- Player
  ... this.listFish.endGame(Player) ...             -- boolean
   */

  // initializes the WorldScene
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    scene = this.player.draw(scene);
    scene = this.listFish.draw(scene);
    scene = this.playerSize(scene);
    return scene;
  }

  // moves the fish and adds new fish at random y-coordinate with a certain probability 
  public FishGame onTick() {
    this.listFish = this.listFish.move();
    this.listFish = this.listFish.checkCollision(this.player);
    if (rand.nextInt(100) < 7) {
      this.listFish = new ConsLoFish(new Background(0, rand.nextInt(this.height), 
          Color.red, rand.nextInt(30) + 30, rand.nextInt(5) + 1, 
          rand.nextBoolean()), this.listFish);
    }
    return this;
  }

  // starts a new fish game 
  public World onKeyEvent(String key) {
    return new FishGame(this.width, this.height, 
        this.player.movingFish(key), this.listFish, this.rand);
  }

  // checks to see if fish is a certain size, and returns new scene depending on outcome
  public WorldEnd worldEnds() {
    if (listFish.endGame(player)) {
      return new WorldEnd(true, this.lastScene("You Win!!!"));
    }
    else if (this.player.size <= 0) {
      return new WorldEnd(true, this.lastScene("You Lost!!!"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // represents the text that is displayed on the win or lose screen
  public WorldScene lastScene(String msg) {
    WorldScene scene = new WorldScene(this.width, this.height);
    return scene.placeImageXY(new TextImage(msg, 30, Color.green), 
        this.width / 2, this.height / 2);
  }

  // Keeps track of the size of the player fish
  public WorldScene playerSize(WorldScene acc) {
    return acc.placeImageXY(
        new TextImage("Size: " + Integer.toString(this.player.size), 20, Color.cyan), 
            650, 25);
  }

}

// represents an abstract class for the fish 
abstract class AFish {
  int x;
  int y;
  Color color;
  int size;
  int speed;
  boolean isMoving;

  AFish(int x, int y, Color color, int size, int speed, boolean isMoving) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.size = size;
    this.speed = speed;
    this.isMoving = isMoving;
  }

  /*
  TEMPLATE:
  Fields:
  ... this.x ...          -- int
  ... this.y ...          -- int
  ... this.color ...      -- Color
  ... this.size ...       -- int
  ... this.speed ...      -- int
  ... this.isMoving ...   -- boolean

  Methods:
  ... this.draw(WorldScene) ...     -- WorldScene
  ... this.doesCollide(AFish) ...   -- boolean

  Methods for Fields: N/A
   */

  // draws fish as a CircleImage
  public WorldScene draw(WorldScene acc) {
    return acc.placeImageXY(new CircleImage(this.size, OutlineMode.SOLID, this.color), 
        this.x, this.y);
  }

  // checks if this fish is bigger than the fish it collided with
  public boolean doesCollide(AFish other) {
    return Math.abs(this.x - other.x) < (this.size / 2) + (other.size / 2)
        && Math.abs(this.y - other.y) < (this.size / 2) + (other.size / 2);
  }

}

// a class to represent the background fish 
class Background extends AFish {

  Background(int x, int y, Color color, int size, int speed, boolean isMoving) {
    super(x, y, color, size, speed, isMoving);
  }

  /*
  TEMPLATE:
  Fields:
  ... this.x ...          -- int
  ... this.y ...          -- int
  ... this.color ...      -- Color
  ... this.size ...       -- int
  ... this.speed ...      -- int
  ... this.isMoving ...   -- boolean

  Methods:
  ... this.movingFish() ...         -- Background
  ... this.draw(WorldScene) ...     -- WorldScene
  ... this.doesCollide(AFish) ...   -- boolean
  ... this.backgroundSize() ...     -- int

  Methods for Fields: N/A
   */

  // allows the fish to move and also checks to see if the background fish are off of the scene
  public Background movingFish() {
    if (this.x > 700) {
      return new Background(0, this.y, this.color, this.size, this.speed, this.isMoving);
    }
    else if (this.x < 0) {
      return new Background(700, this.y, this.color, this.size, this.speed, this.isMoving);
    }
    else if (this.isMoving) {
      return new Background(this.x + this.speed, this.y, this.color, 
          this.size, this.speed, this.isMoving);
    }
    else {
      return new Background(this.x - this.speed, this.y, this.color, 
          this.size, this.speed, this.isMoving);
    }
  }

  // gets size of background fish
  public int backgroundSize() {
    return this.size;
  }
}

// a class to represents the player fish
class Player extends AFish {

  Player(int x, int y, Color color, int size, int speed, boolean isMoving) {
    super(x, y, color, size, speed, isMoving);
  }

  /*
  TEMPLATE:
  Fields:
  ... this.x ...          -- int
  ... this.y ...          -- int
  ... this.color ...      -- Color
  ... this.size ...       -- int
  ... this.speed ...      -- int
  ... this.isMoving ...   -- boolean

  Methods:
  ... this.movingFish(String) ...     -- Player
  ... this.makeBigger(AFish) ...      -- void
  ... this.makeSmaller(AFish) ...     -- void
  ... this.draw(WorldScene) ...       -- WorldScene
  ... this.doesCollide(AFish) ...     -- boolean
  ... this.makeBigger(AFish) ...      -- Player
  ... this.makeSmaller(AFish) ...     -- Player

  Methods for Fields: N/A
   */

  // allows the player fish to move and also checks to see if player fish is off the screen
  public Player movingFish(String key) {
    if (this.x > 700) {
      return new Player(0, this.y, this.color, this.size, this.speed, this.isMoving);
    }
    else if (this.x < 0) {
      return new Player(700, this.y, this.color, this.size, this.speed, this.isMoving);
    }
    else if (this.y < 0) {
      return new Player(this.x, 0, this.color, this.size, this.speed, this.isMoving);
    }
    else if (this.y > 700) {
      return new Player(this.x, 700, this.color, this.size, this.speed, this.isMoving);
    }
    else if (key.equals("up")) {
      return new Player(this.x, this.y - this.speed, this.color, 
          this.size, this.speed, this.isMoving);
    }
    else if (key.equals("right")) {
      return new Player(this.x + this.speed, this.y, this.color, 
          this.size, this.speed, this.isMoving);
    }
    else if (key.equals("down")) {
      return new Player(this.x, this.y + this.speed, this.color, 
          this.size, this.speed, this.isMoving);
    }
    else if (key.equals("left")) {
      return new Player(this.x - this.speed, this.y, this.color, 
          this.size, this.speed, this.isMoving);
    }
    else {
      return this;
    }
  }

  // makes player fish bigger after consuming another background fish
  public Player makeBigger(AFish consumedFish) {
    return new Player(0, this.y, this.color, this.size += consumedFish.size, 
        this.speed, this.isMoving);
  }

  // makes player fish smaller after colliding with larger background fish
  public Player makeSmaller(AFish consumedFish) {
    return new Player(0, this.y, this.color, this.size = 0, this.speed, this.isMoving);
  }
}

// an interface that represents a list of fish
interface ILoFish {

  // draws fish from list onto the scene
  WorldScene draw(WorldScene acc); 

  // moves the fish within the list
  ILoFish move();

  // checks to see if player collides with background fish of a smaller size and then makes
  // player fish smaller or bigger accordingly
  ILoFish checkCollision(Player player);

  // checks to see if player fish is largest in the game
  boolean endGame(Player player);

}

// represents an empty list of fish
class MtLoFish implements ILoFish {

  MtLoFish() {}

  /*
  TEMPLATE:
  Fields: N/A

  Methods:
  ... this.draw(WorldScene) ...           -- WorldScene
  ... this.move() ...                     -- ILoFish
  ... this.checkCollision(Player) ...     -- ILoFish
  ... this.endGame(Player) ...            -- boolean

  Methods for Fields: N/A
   */

  // draws fish from list onto the scene
  public WorldScene draw(WorldScene acc) {
    return acc;
  }

  // moves fish in the empty list
  public ILoFish move() {
    return this;
  }

  // checks to see if player collides with background fish of a smaller size and then makes
  // player fish smaller or bigger accordingly
  public ILoFish checkCollision(Player player) {
    return this;
  }

  // checks to see if player fish is largest in the game
  public boolean endGame(Player player) {
    return true;
  }
}

// represents a non-empty list of fish
class ConsLoFish implements ILoFish {
  Background first;
  ILoFish rest;

  ConsLoFish(Background first, ILoFish rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
  TEMPLATE:
  Fields: 
  ... this.first ...     -- Background
  ... this.rest ...      -- ILoFish

  Methods:
  ... this.draw(WorldScene) ...           -- WorldScene
  ... this.move() ...                     -- ILoFish
  ... this.checkCollision(Player) ...     -- ILoFish
  ... this.endGame(Player) ...            -- boolean

  Methods for Fields:
  ... this.first.draw(WorldScene) ...        -- WorldScene
  ... this.first.movingFish() ...            -- Background
  ... this.rest.move() ...                   -- ILoFish
  ... this.first.doesCollide(AFish) ...      -- boolean
  ... this.rest.checkCollision(Player) ...   -- ILoFish

   */

  // draws fish from list onto the scene
  public WorldScene draw(WorldScene acc) {
    return this.rest.draw(this.first.draw(acc));
  }

  // moves fish in the non-empty list
  public ILoFish move() {
    return new ConsLoFish(this.first.movingFish(), this.rest.move());
  }

  // checks to see if player collides with background fish of a smaller size and then makes
  // player fish smaller or bigger accordingly
  public ILoFish checkCollision(Player player) {
    if (this.first.doesCollide(player) && (player.size > this.first.size)) {
      player.makeBigger(this.first);
      return this.rest.checkCollision(player);
    }
    else if (this.first.doesCollide(player) && (player.size < this.first.size)) {
      player.makeSmaller(this.first);
      return this.rest.checkCollision(player);
    }
    else {
      return new ConsLoFish(this.first, this.rest.checkCollision(player));
    }
  }

  // checks to see if player fish is largest in the game
  public boolean endGame(Player player) {
    if (player.size > this.first.backgroundSize()) {
      return this.rest.endGame(player);
    }
    else {
      return false;
    }
  }
}

// a class to represents examples of fish
class ExamplesFish {

  Player player = new Player(300, 300, Color.blue, 10, 10, true);
  Player player2 = new Player(250, 250, Color.blue, 10, 10, true);
  Player player3 = new Player(300, 300, Color.blue, 5, 10, true);
  Player player4 = new Player(300, 300, Color.blue, 15, 10, true);
  Player player5 = new Player(0, 0, Color.blue, 25, 10, true);
  Player player6 = new Player(300, 300, Color.blue, 30, 30, true);

  Random rand = new Random(30);

  Background fish1 = new Background(0, rand.nextInt(700), Color.red, 13, 3, true);
  Background fish2 = new Background(0, rand.nextInt(700), Color.red, 17, 5, true);
  Background fish3 = new Background(0, rand.nextInt(700), Color.red, 5, 7, false);
  Background fish4 = new Background(0, rand.nextInt(700), Color.red, 7, 7, false);
  Background fish5 = new Background(0, rand.nextInt(700), Color.red, 9, 13, true);
  Background fish6 = new Background(700, 300, Color.red, 7, 3, true);
  Background fish7 = new Background(0, 300, Color.red, 7, 3, true);
  Background fish8 = new Background(350, 700, Color.red, 7, 3, true);
  Background fish9 = new Background(350, 0, Color.red, 7, 3, true);
  Background fish10 = new Background(100, 100, Color.red, 20, 3, true);
  Background fish11 = new Background(100, 100, Color.red, 20, 3, true);

  Background movedFish1 = fish1.movingFish();
  Background movedFish2 = fish2.movingFish();
  Background movedFish3 = fish3.movingFish();
  Background movedFish4 = fish4.movingFish();
  Background movedFish5 = fish5.movingFish();
  Background movedFish6 = fish6.movingFish();
  Background movedFish7 = fish7.movingFish();
  Background movedFish8 = fish8.movingFish();
  Background movedFish9 = fish9.movingFish();

  Background bg1 = new Background(50, 50, Color.blue, 10, 5, true);
  Background bg2 = new Background(710, 50, Color.blue, 10, 5, true);
  Background bg3 = new Background(-10, 50, Color.blue, 10, 5, true);
  Background bg4 = new Background(50, 50, Color.blue, 10, 5, false);


  ILoFish emptyList = new MtLoFish();
  ILoFish list1 = new ConsLoFish(fish1, emptyList);
  ILoFish list2 = new ConsLoFish(fish2, list1);
  ILoFish list3 = new ConsLoFish(fish3, list2);
  ILoFish list4 = new ConsLoFish(fish4, list3);
  ILoFish list5 = new ConsLoFish(fish5, list4);

  ILoFish movedList1 = new ConsLoFish(movedFish1, emptyList);
  ILoFish movedList2 = new ConsLoFish(movedFish2, movedList1);
  ILoFish listWithTwoFish = new ConsLoFish(fish1, new ConsLoFish(fish2, emptyList));

  WorldScene initialScene = new WorldScene(700, 700);


  WorldScene scene1 = initialScene.placeImageXY(new CircleImage(13, OutlineMode.SOLID, Color.red), 
      0, 406);
  WorldScene scene2 = scene1.placeImageXY(new CircleImage(17, OutlineMode.SOLID, Color.red), 
      0, 568);

  FishGame initWorld = new FishGame(700, 700, player, emptyList, rand);
  FishGame fishWorld = new FishGame(700, 700, player, list5, rand);

  boolean testBigBang(Tester t) {
    FishGame world = new FishGame(700, 700, player, list5, rand);
    int worldWidth = 700;
    int worldHeight = 700;
    double tickRate = 0.1;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }

  boolean testOnTick(Tester t) {  
    return t.checkExpect(fishWorld.onTick().listFish, new ConsLoFish(movedFish5, 
        new ConsLoFish(movedFish4, 
            new ConsLoFish(movedFish3, new ConsLoFish(movedFish2, 
                new ConsLoFish(movedFish1, emptyList))))))
        && t.checkExpect(initWorld.onTick().listFish, emptyList);
  }

  boolean testOnKeyEvent(Tester t) {  
    return t.checkExpect(fishWorld.onKeyEvent("right"), 
        new FishGame(700, 700, new Player(310, 300, Color.blue, 10, 10, true), 
            list5, new Random()))
        && t.checkExpect(fishWorld.onKeyEvent("left"), 
            new FishGame(700, 700, new Player(290, 300, Color.blue, 10, 10, true), 
                list5, new Random()))
        && t.checkExpect(fishWorld.onKeyEvent("up"), 
            new FishGame(700, 700, new Player(300, 290, Color.blue, 10, 10, true), 
                list5, new Random()))
        && t.checkExpect(fishWorld.onKeyEvent("down"), 
            new FishGame(700, 700, new Player(300, 310, Color.blue, 10, 10, true), 
                list5, new Random()))
        && t.checkExpect(initWorld.onKeyEvent("left"), 
            new FishGame(700, 700, player.movingFish("left"), emptyList, new Random()))
        && t.checkExpect(initWorld.onKeyEvent("x"), 
            new FishGame(700, 700, new Player(300, 300, Color.blue, 10, 10, true), 
                emptyList, new Random()));
  }

  boolean testLastScene(Tester t) {
    WorldScene scene = new WorldScene(700, 700);

    return t.checkExpect(fishWorld.lastScene("You Win!!!"), 
        scene.placeImageXY(new TextImage("You Win!!!", 30, Color.green), 350, 350))
        && t.checkExpect(new FishGame(700, 700, new Player(300, 300, Color.blue, 0, 10, true), 
            list5, rand).lastScene("You Lost!!!"), 
            scene.placeImageXY(new TextImage("You Lost!!!", 30, Color.green), 350, 350));
  }

  boolean testBackgroundMovingFish(Tester t) {
    return t.checkExpect(bg1.movingFish(), new Background(55, 50, Color.blue, 10, 5, true)) &&
        t.checkExpect(bg2.movingFish(), new Background(0, 50, Color.blue, 10, 5, true)) &&
        t.checkExpect(bg3.movingFish(), new Background(700, 50, Color.blue, 10, 5, true)) &&
        t.checkExpect(bg4.movingFish(), new Background(45, 50, Color.blue, 10, 5, false));
  }

  boolean testPlayerMovingFish(Tester t) {
    return t.checkExpect(player.movingFish("right"), 
        new Player(310, 300, Color.blue, 10, 10, true)) &&
        t.checkExpect(player.movingFish("up"), new Player(300, 290, Color.blue, 10, 10, true)) &&
        t.checkExpect(player.movingFish("left"), new Player(290, 300, Color.blue, 10, 10, true));
  }

  boolean testDoesCollide(Tester t) {
    return t.checkExpect(fish1.doesCollide(fish2), false) &&
        t.checkExpect(fish1.doesCollide(fish3), false) &&
        t.checkExpect(fish10.doesCollide(fish11), true);
  }

  boolean testBackgroundSize(Tester t) {
    return t.checkExpect(fish1.backgroundSize(), 13) &&
        t.checkExpect(fish2.backgroundSize(), 17) &&
        t.checkExpect(fish3.backgroundSize(), 5);
  }

  boolean testEndGame(Tester t) {
    return t.checkExpect(list1.endGame(player3), false) && 
        t.checkExpect(list3.endGame(player6), true) &&
        t.checkExpect(emptyList.endGame(player5), true);
  }

  boolean testMove(Tester t) {
    return t.checkExpect(list1.move(), movedList1) &&
        t.checkExpect(list2.move(), movedList2) &&
        t.checkExpect(emptyList.move(), emptyList);
  }  

  boolean testMakeBigger(Tester t) {
    return t.checkExpect(this.player2.makeBigger(fish7), 
        new Player(0, 250, Color.BLUE, 17, 10, true))
        && t.checkExpect(this.player3.makeBigger(fish8), 
            new Player(0, 300, Color.BLUE, 12, 10, true));
  }

  boolean testMakeSmaller(Tester t) {
    return t.checkExpect(this.player4.makeSmaller(fish7), 
        new Player(0, 300, Color.BLUE, 0, 10, true)) &&
        t.checkExpect(this.player5.makeSmaller(fish8), 
            new Player(0, 0, Color.BLUE, 0, 10, true));
  }

  boolean testDraw(Tester t) {
    return t.checkExpect(emptyList.draw(initialScene), initialScene) &&
        t.checkExpect(list1.draw(initialScene), scene1) &&
        t.checkExpect(list2.draw(initialScene), scene2);
  }

  boolean testCheckCollision(Tester t) {
    return t.checkExpect(listWithTwoFish.checkCollision(player3), listWithTwoFish)
        && t.checkExpect(emptyList.checkCollision(player), emptyList);
  }

  boolean testPlayerSize(Tester t) {
    return t.checkExpect(initWorld.playerSize(initialScene), initialScene.placeImageXY(
        new TextImage("Size: " + Integer.toString(player.size), 20, Color.cyan), 650, 25));
  } 

}