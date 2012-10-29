package com.handsomegoats.panda7;

import java.util.Random;

import com.handsomegoats.panda7.view.*;
import com.handsomegoats.panda7.controller.*;
import com.handsomegoats.panda7.controller.ControllerDifficulty.Difficulty;
import com.handsomegoats.panda7.input.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

  public enum Screen {
    Nothing, Title, LevelSelect, DifficultySelect, Game, GameOver, HighScore, HowToPlay
  }

  public enum Buttons {
    Play(0), Title(1), HowToPlay(2), Sound(3), SoundMuted(4), Settings(5), Music(6), MusicMuted(7);

    public final int val;

    Buttons(int value) {
      this.val = value;
    }
  }

  private static final String      TAG                 = Game.class.getSimpleName();

  // Color
  public static int                cSkyBlue            = Color.rgb(92, 209, 222);
  public static int                cGreenBack          = Color.rgb(159, 227, 40);

  public static Bitmap             sprites;
  public static Bitmap             background;
  public static Bitmap             clouds;
  public static Bitmap             titlesprites;
  public static Bitmap             title;

  public static Random             random;
  private Input                    touchInput;

  public static AbstractController controller;
  public static AbstractView       view;
  public static InterfaceInput     input;

  public static boolean            hd;

  public static Loop               loop;
  public static long               gameTick            = 0;
  public static double             gameTime;
  public static double             delta;
  public static int                SCREEN_WIDTH;
  public static int                SCREEN_HEIGHT;
  public static int                GRID_SIZE           = 7;
  public static int                NewLevelStartHeight = 3;
  public static float              EMPTY_SPACE_PERCENT = 0.5f;
  // public static int[] CHAIN = { 7, 39, 109, 224, 391, 617, 907, 1267, 1701,
  // 2213, 2809, 3391, 3851, 4265, 4681, 5113 };
  public static int[]              CHAIN               = { 10, 50, 100, 200, 400, 600, 900, 1250, 1700, 2250, 3000, 3500, 4000, 4250, 4750,
      5000                                            };
  public static int                NEW_ROW_BONUS       = 10000;
  public static int                BRICK_TILE          = -2;
  public static int                BROKEN_BRICK_TILE   = -1;
  public static int                NO_TILE             = 0;
  public static Typeface           font;
  public static int                HD_THRESHOLD        = 480;

  public static int                startHeight         = 3;
  public static Difficulty         difficulty          = Difficulty.Medium;

  Context                          context;

  public static Bitmap[]           howToImages;

  public Game(Context context, int screenWidth, int screenHeight, Typeface font) {
    super(context);
    Main.debug(TAG, "Game starting...");

    this.context = context;

    // Set font
    Game.font = font;

    // Set screen size
    Game.SCREEN_WIDTH = screenWidth;
    Game.SCREEN_HEIGHT = screenHeight;

    // Load Images
    if (Game.SCREEN_WIDTH > HD_THRESHOLD)
      hd = true;

    // Load SD or HD graphics
    loadImages(hd, Screen.Title, true);

    // Add Input Device
    this.touchInput = new TouchInput();

    // Create Random
    Game.random = new Random();

    // Adding the callback (this) to the surface holder to intercept events
    getHolder().addCallback(this);

    // Start the game Controller (This will be the Title screen)
    Game.controller = new ControllerTitleScreen();
    Game.view = new ViewTitle();
    Game.input = new InputTitleScreen();

    // Create the Game Loop thread
    loop = new Loop(getHolder(), this);

    // Make the GamePanel focusable so it can handle events
    setFocusable(true);

    Main.debug(TAG, "Game Started. From Title Screen");

  }

  public Game(Context context, ControllerGame c, int screenWidth, int screenHeight, Typeface font) {
    super(context);

    Main.debug(TAG, "Game starting... (from file)");

    this.context = context;

    // Set font
    Game.font = font;

    // Set screen size
    Game.SCREEN_WIDTH = screenWidth;
    Game.SCREEN_HEIGHT = screenHeight;

    // Load Images
    if (Game.SCREEN_WIDTH > HD_THRESHOLD)
      hd = true;

    // Load SD or HD graphics
    loadImages(hd, Screen.Game, true);

    // Add Input Device
    this.touchInput = new TouchInput();

    // Create Random
    Game.random = new Random();

    // Adding the callback (this) to the surface holder to intercept events
    getHolder().addCallback(this);

    Game.controller = c;
    Game.view = new ViewGame();
    Game.input = new InputGame();

    // Create the Game Loop thread
    loop = new Loop(getHolder(), this);

    // Make the GamePanel focusable so it can handle events
    setFocusable(true);

    Main.debug(TAG, "Game Started (From GameState File)");
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // TODO Auto-generated method stub
  }

  public void surfaceCreated(SurfaceHolder arg0) {
    loop.setRunning(true);
    loop.start();
  }

  public void surfaceDestroyed(SurfaceHolder arg0) {
    boolean retry = true;
    while (retry) {
      try {
        loop.join();
        retry = false;
      } catch (InterruptedException e) {
        // Try again shutting down the thread
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    // Exit the game if you touch the top 50 pixels
    // if (event.getAction() == MotionEvent.ACTION_DOWN) {
    // if (event.getY() < 50) {
    // thread.setRunning(false);
    // ((Activity) getContext()).finish();
    // }
    // }

    if (touchInput.down(event)) {
      // Execute TouchDown method
      Game.input.touchDown(Game.controller, Game.view, event.getX(), event.getY());
    }

    if (touchInput.press(event)) {
      // Execute touchPress method
      Game.input.touchPress(Game.controller, Game.view, event.getX(), event.getY());
    }

    if (touchInput.move(event)) {
      // Execute TouchMove method
      Game.input.touchMove(Game.controller, Game.view, event.getX(), event.getY());
    }

    return true;
  }

  public void updateGameTick() {
    gameTick++;
  }

  public long getGameTime() {
    return (long) (gameTick * 60);
  }

  public void update(long delta) {
    try {
      if (AbstractController.nextScreen != Screen.Nothing) {
        startNewController();
      } else {
        Game.gameTime = getGameTime();
        Game.delta = delta / 1000;

        Game.controller.update();
        Game.view.update(Game.controller, Game.gameTime, Game.delta);
      }

    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  protected void render(Canvas canvas) {
    try {
      Game.view.draw(canvas);
      drawFPS(canvas);
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  private void drawFPS(Canvas canvas) {
    Paint paint = new Paint();
    paint.setColor(Color.WHITE);

    if (Main.DEBUGGING_ON) {
      if (loop != null) {
        int fps = Loop.FRAME_PERIOD * 60;
        canvas.drawText(Integer.toString(fps), 20, 20, paint);
      }
    }
  }

  private void startNewController() {
    Main.stopMusic();

    if (!AbstractController.nextScreen.equals(Screen.Nothing)) {
      loadImages(hd, AbstractController.nextScreen, true);

      // loop.setFPS(1);
      //
      // if (AbstractController.nextScreen.equals(Screen.Game))
      // loop.setFPS(60);
    }

    switch (AbstractController.nextScreen) {
    case Title:
      Main.debug(TAG, "Screen: Title Screen");

      Game.controller = new ControllerTitleScreen();
      Game.view = new ViewTitle();
      Game.input = new InputTitleScreen();
      break;
    case LevelSelect:
      break;
    case DifficultySelect:
      Main.debug(TAG, "Screen: Difficulty Select");

      Game.controller = new ControllerDifficulty();
      Game.view = new ViewDifficulty();
      Game.input = new InputDifficulty();
      break;
    case Game:
      Main.debug(TAG, "Screen: Game");

      Game.controller = new ControllerGame();
      Game.view = new ViewGame();
      Game.input = new InputGame();

      Main.playMusic(context);
      break;
    case GameOver:
      break;
    case HighScore:
      break;
    case HowToPlay:
      Main.debug(TAG, "Screen: How To Play");

      Game.controller = new ControllerHowToPlay();
      Game.view = new ViewHowToPlay();
      Game.input = new InputHowToPlay();

      break;
    case Nothing:
      // Do nothing
      break;
    }

    AbstractController.nextScreen = Screen.Nothing;
  }

  // For devices with low RAM, loadImages clears Bitmaps
  // from memory and only loads what it needs
  // This slows down the game between screens.
  private void loadImages(boolean hd, Screen screen, boolean recycle) {
    try {
      if (recycle) {
        // Clear Memory
        titlesprites.recycle();
        title.recycle();
        sprites.recycle();
        background.recycle();
        clouds.recycle();

        Main.debug(TAG, "Cleared all images from memory");
      }
    } catch (Exception e) {
      // TODO: handle exception
    }

    if (hd) {
      switch (screen) {
      case Title:
        titlesprites = BitmapFactory.decodeResource(getResources(), R.drawable.titlespriteshd);
        title = BitmapFactory.decodeResource(getResources(), R.drawable.titlehd);
        Main.debug(TAG, "Load Images: HD Title");
        break;
      case LevelSelect:
        break;
      case DifficultySelect:
        loop.setFPS(1);
        titlesprites = BitmapFactory.decodeResource(getResources(), R.drawable.titlespriteshd);
        Main.debug(TAG, "Load Images: HD Difficulty");
        break;
      case Game:
        sprites = BitmapFactory.decodeResource(getResources(), R.drawable.spriteshd);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundhd);
        clouds = BitmapFactory.decodeResource(getResources(), R.drawable.cloudshd);
        Main.debug(TAG, "Load Images: HD Game");
        break;
      case GameOver:
        break;
      case HighScore:
        break;
      case HowToPlay:
        howToImages = new Bitmap[6];
        howToImages[0] = BitmapFactory.decodeResource(getResources(), R.drawable.howto0);
        howToImages[1] = BitmapFactory.decodeResource(getResources(), R.drawable.howto1);
        howToImages[2] = BitmapFactory.decodeResource(getResources(), R.drawable.howto2);
        howToImages[3] = BitmapFactory.decodeResource(getResources(), R.drawable.howto3);
        howToImages[4] = BitmapFactory.decodeResource(getResources(), R.drawable.howto4);
        howToImages[5] = BitmapFactory.decodeResource(getResources(), R.drawable.howto5);
        Main.debug(TAG, "Load Images: How To Play");
        break;
      case Nothing:
        // Do nothing
        break;
      }
    } else {
      switch (screen) {
      case Title:
        titlesprites = BitmapFactory.decodeResource(getResources(), R.drawable.titlesprites);
        title = BitmapFactory.decodeResource(getResources(), R.drawable.title);
        Main.debug(TAG, "Load Images: SD Title");
        break;
      case LevelSelect:
        break;
      case DifficultySelect:
        titlesprites = BitmapFactory.decodeResource(getResources(), R.drawable.titlesprites);
        Main.debug(TAG, "Load Images: SD Difficulty");
        break;
      case Game:
        sprites = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        clouds = BitmapFactory.decodeResource(getResources(), R.drawable.clouds);
        Main.debug(TAG, "Load Images: SD Game");
        break;
      case GameOver:
        break;
      case HighScore:
        break;
      case HowToPlay:
        howToImages = new Bitmap[6];
        howToImages[0] = BitmapFactory.decodeResource(getResources(), R.drawable.howto0);
        howToImages[1] = BitmapFactory.decodeResource(getResources(), R.drawable.howto1);
        howToImages[2] = BitmapFactory.decodeResource(getResources(), R.drawable.howto2);
        howToImages[3] = BitmapFactory.decodeResource(getResources(), R.drawable.howto3);
        howToImages[4] = BitmapFactory.decodeResource(getResources(), R.drawable.howto4);
        howToImages[5] = BitmapFactory.decodeResource(getResources(), R.drawable.howto5);
        Main.debug(TAG, "Load Images: SD How To Play");
        break;
      case Nothing:
        // Do nothing
        break;
      }

    }
  }
}
