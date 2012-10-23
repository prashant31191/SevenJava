package com.handsomegoats.panda7;

import java.util.Random;

import com.handsomegoats.panda7.view.*;
import com.handsomegoats.panda7.controller.AController;
import com.handsomegoats.panda7.controller.GameController;
import com.handsomegoats.panda7.controller.TitleScreenController;
import com.handsomegoats.panda7.input.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

  public enum Screen {
    Nothing, Title, LevelSelect, DifficultySelect, Game, GameOver, HighScore, HowToPlay
  }

  private static final String TAG                  = Game.class.getSimpleName();

  // Color
  public static int           cSkyBlue             = Color.rgb(92, 209, 222);
  public static int           cGreenBack           = Color.rgb(159, 227, 40);

  private static final int    DEFAULT_START_HEIGHT = 3;
  private static final int    DEFAULT_DIFFICULTTY  = 5;

  public static Bitmap        sprites;
  public static Bitmap        background;
  public static Bitmap        clouds;
  public static Bitmap        titlesprites;
  public static Bitmap        title;

  public static Random        random;
  private Input               touchInput;

  public static AController   controller;
  public static IView         view;
  public static IInput        input;

  public static boolean       hd;

  public static Loop          thread;
  public static long          gameTick             = 0;
  public static double        gameTime;
  public static double        delta;
  public static int           SCREEN_WIDTH;
  public static int           SCREEN_HEIGHT;
  public static int           GRID_SIZE            = 7;
  public static int           NewLevelStartHeight  = 3;
  public static float         EMPTY_SPACE_PERCENT  = 0.5f;
  public static int[]         CHAIN                = { 7, 39, 109, 224, 391, 617, 907, 1267, 1701, 2213, 2809, 3391, 3851, 4265, 4681, 5113 };
  public static int           BRICK_TILE           = -2;
  public static int           BROKEN_BRICK_TILE    = -1;
  public static int           NO_TILE              = 0;
  public static Typeface      font;
  public static int           HD_THRESHOLD         = 480;

  public static int           startHeight          = 3;
  public static int           difficulty           = 5;

  public static SoundPool     sounds               = null;
  public static MediaPlayer   music                = null;
  public static int           sndMicrobiaMusic     = R.raw.microbia;
  public static int           sndBump              = R.raw.bump;
  public static int           sndTone1             = R.raw.tone1;
  public static int           sndTone2             = R.raw.tone2;
  public static int           sndTone3             = R.raw.tone3;
  public static int           sndTone4             = R.raw.tone4;
  public static int           sndTone5             = R.raw.tone5;
  public static int           sndTone6             = R.raw.tone6;

  public Game(Context context, int screenWidth, int screenHeight, Typeface font) {
    super(context);
    // Load Sounds
    loadAudio(context);

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

    // Game settings
    startHeight = DEFAULT_START_HEIGHT;
    difficulty = DEFAULT_DIFFICULTTY;

    // Start the game Controller (This will be the Title screen)
    // this.controller = new GameController(this, startHeight, difficulty);
    Game.controller = new TitleScreenController();
    Game.view = new TitleView();
    Game.input = new InputTitle();

    Main.debug(TAG, Game.controller.toString());

    // Create the Game Loop thread
    thread = new Loop(getHolder(), this);

    // Make the GamePanel focusable so it can handle events
    setFocusable(true);

    Main.debug(TAG, "Game Started. From Title Screen");

    // for (StackTraceElement iterable_element :
    // Thread.currentThread().getStackTrace()) Main.debug(TAG,
    // iterable_element.toString());

  }

  public Game(Context context, GameController c, int screenWidth, int screenHeight, Typeface font) {
    super(context);
    loadAudio(context);

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

    // Game settings
    startHeight = DEFAULT_START_HEIGHT;
    difficulty = DEFAULT_DIFFICULTTY;

    Game.controller = c;
    Game.view = new GameView();
    Game.input = new InputGame();
    Game.playMusic(Game.sndMicrobiaMusic);

    // Create the Game Loop thread
    thread = new Loop(getHolder(), this);

    // Make the GamePanel focusable so it can handle events
    setFocusable(true);

    Main.debug(TAG, "Game Started (From GameState File)");
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
        Main.debug(TAG, "HD title screen images loaded");
        break;
      case LevelSelect:
        break;
      case DifficultySelect:
        break;
      case Game:
        sprites = BitmapFactory.decodeResource(getResources(), R.drawable.spriteshd);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundhd);
        clouds = BitmapFactory.decodeResource(getResources(), R.drawable.cloudshd);
        Main.debug(TAG, "HD game images loaded");
        break;
      case GameOver:
        break;
      case HighScore:
        break;
      case HowToPlay:
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
        Main.debug(TAG, "SD title screen images loaded");
        break;
      case LevelSelect:
        break;
      case DifficultySelect:
        break;
      case Game:
        sprites = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        clouds = BitmapFactory.decodeResource(getResources(), R.drawable.clouds);
        Main.debug(TAG, "SD game images loaded");
        break;
      case GameOver:
        break;
      case HighScore:
        break;
      case HowToPlay:
        break;
      case Nothing:
        // Do nothing
        break;
      }

    }
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // TODO Auto-generated method stub
  }

  public void surfaceCreated(SurfaceHolder arg0) {
    thread.setRunning(true);
    thread.start();
  }

  public void surfaceDestroyed(SurfaceHolder arg0) {
    boolean retry = true;
    while (retry) {
      try {
        thread.join();
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

  /**
   * Main Update Method
   */
  public void update(long delta) {
    try {
      if (AController.nextScreen != Screen.Nothing) {
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

  /**
   * Main Draw Method
   * 
   * @param canvas
   */
  protected void render(Canvas canvas) {
    try {
      Game.view.draw(canvas);
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  private void startNewController() {
    switch (AController.nextScreen) {
    case Title:
      Main.debug(TAG, "Next Screen: Title Screen");
      loadImages(hd, AController.nextScreen, true);
      Game.controller = new TitleScreenController();
      Game.view = new TitleView();
      Game.input = new InputTitle();
      Game.playMusic(Game.sndMicrobiaMusic);
      break;
    case LevelSelect:
      break;
    case DifficultySelect:
      break;
    case Game:
      Main.debug(TAG, "Next Screen: Game");
      loadImages(hd, AController.nextScreen, true);
      Game.controller = new GameController(startHeight, difficulty);
      Game.view = new GameView();
      Game.input = new InputGame();
      break;
    case GameOver:
      break;
    case HighScore:
      break;
    case HowToPlay:
      break;
    case Nothing:
      // Do nothing
      break;
    }

    AController.nextScreen = Screen.Nothing;
  }

  private void loadAudio(Context context) {
    music = MediaPlayer.create(context, sndMicrobiaMusic);

    sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    sndBump = sounds.load(context, sndBump, 1);
    sndTone1 = sounds.load(context, sndTone1, 1);
    sndTone2 = sounds.load(context, sndTone2, 1);
    sndTone3 = sounds.load(context, sndTone3, 1);
    sndTone4 = sounds.load(context, sndTone4, 1);
    sndTone5 = sounds.load(context, sndTone5, 1);
    sndTone6 = sounds.load(context, sndTone6, 1);
  }

  public static void playSound(int sound) {
    if (Main.SOUND_ON) {
      sounds.play(sound, 1f, 1f, 0, 0, 1f);
    }
  }

  public static void playMusic(int song) {
    if (Main.MUSIC_ON) {
      music.start();
    }
  }
}
