package com.handsomegoats.panda7;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.handsomegoats.panda7.controller.AController;
import com.handsomegoats.panda7.controller.GameController;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Main extends Activity {
  private static final String TAG                = Main.class.getSimpleName();
  // private final String FILENAME_SETTINGS = "settings.dat";
  private final String        FILENAME_GAMESTATE = "gamestate.dat";
  public static int           SCREEN_WIDTH;
  public static int           SCREEN_HEIGHT;
  public static Typeface      font;
  Game                        game;
  public static boolean       DEBUGGING_ON       = true;

  public static int getLineNumber() {
    return Thread.currentThread().getStackTrace()[4].getLineNumber();
  }

  public static void debug(String tag, String msg) {
    if (Main.DEBUGGING_ON) {
      Log.i(tag, getLineNumber() + ": " + msg);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // Requesting to turn the title off
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    super.onCreate(savedInstanceState);

    Main.debug(TAG, "onCreate");

    // Everything is in onResume
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    Main.debug(TAG, "onCreateOptionsMenu");

    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();

    Main.debug(TAG, "onResume");

    // Load Font
    font = Typeface.createFromAsset(getAssets(), "fonts/8bitnog.ttf");

    // Make it full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // Rotate
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // Get Window Size
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);

    SCREEN_WIDTH = metrics.widthPixels;
    SCREEN_HEIGHT = metrics.heightPixels;

    // Set MainGamePanel as the view
    Main.debug(TAG, "Load Game");
    startGame();
    Main.debug(TAG, "End Load Game");

    setContentView(game);
  }

  @Override
  protected void onStop() {
    // This is no longer visible
    Main.debug(TAG, "onStop");
    super.onStop();
  }

  @Override
  protected void onPause() {
    // This happens as the app is minimized
    Main.debug(TAG, "onPause");
    Game.thread.setRunning(false);

    // Save the current game
    saveGame(Game.controller);

    super.onPause();
  }

  @Override
  protected void onDestroy() {
    // App is killed
    Main.debug(TAG, "onDestroy");
    super.onDestroy();
  }

  private void saveGame(AController controller) {
    if (controller instanceof GameController) {
      writeGameState(this, (GameController) controller);
    } else {
      Main.debug(TAG, controller.toString() + " Not on game screen. Did not save file.");
    }
  }

  private void startGame() {
    if (gameFileExists()) {
      Main.debug(TAG, "Starting game from GameState file " + FILENAME_GAMESTATE);
      startGameFromGamestate();
    } else {
      Main.debug(TAG, FILENAME_GAMESTATE + " File does not exists.");
      Main.debug(TAG, "Starting from Title Screen Instead.");
      startGameFromTitle();
    }
  }

  private void startGameFromGamestate() {
    GameController c = readGameState(this);
    game = new Game(this, c, SCREEN_WIDTH, SCREEN_HEIGHT, font);
  }

  private boolean gameFileExists() {
    try {
      FileInputStream fis = openFileInput(FILENAME_GAMESTATE);
      // FileInputStream fin = new FileInputStream(FILENAME_GAMESTATE);

      int fileContents = fis.read();

      fis.close();

      if (fileContents == -1) {
        debug(TAG, FILENAME_GAMESTATE + " file is empty");
        return false;
      }

      return true;
    } catch (Exception e) {
      debug(TAG, e.getMessage());
      for (StackTraceElement ste : e.getStackTrace()) {
        debug(TAG, ste.toString());
      }
      return false;
    }

  }

  private void startGameFromTitle() {
    game = new Game(this, SCREEN_WIDTH, SCREEN_HEIGHT, font);
  }

  public void writeGameState(Context context, GameController controller) {
    ObjectOutputStream oos = null;
    FileOutputStream fos = null;

    try {
      fos = openFileOutput(FILENAME_GAMESTATE, Context.MODE_PRIVATE);
      oos = new ObjectOutputStream(fos);
      oos.writeObject(controller);

      debug(TAG, "Game saved.");
      Toast.makeText(context, "Game state saved", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
      for (StackTraceElement ste : e.getStackTrace()) {
        debug(TAG, ste.toString());
      }

      debug(TAG, "Game save failed.");
      Toast.makeText(context, "Game state NOT saved", Toast.LENGTH_SHORT).show();
    }

    finally {
      try {
        fos.close();
        oos.close();
      } catch (IOException e) {
      }
    }
  }

  public GameController readGameState(Context context) {
    GameController gameController = null;
    ObjectInputStream input = null;
    FileInputStream file = null;

    try {
      file = openFileInput(FILENAME_GAMESTATE);
      input = new ObjectInputStream(file);
      gameController = (GameController) input.readObject();

      debug(TAG, "Game state read");
      Toast.makeText(context, "Game state read", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {

      debug(TAG, "Game state NOT read");
      debug(TAG, "Problem with " + FILENAME_GAMESTATE + ". Deleting File");
      deleteFile(FILENAME_GAMESTATE);

      for (StackTraceElement ste : e.getStackTrace())
        debug(TAG, ste.toString());
      
      Toast.makeText(context, "Game state NOT read", Toast.LENGTH_SHORT).show();
    }

    finally {
      try {
        file.close();
        input.close();
      } catch (IOException e) {
        debug(TAG, "IO Exception");
      }
    }

    return gameController;
  }

  // Disable default on Back Press
  // @Override
  // public void onBackPressed() {
  // // super.onBackPressed();
  // return;
  // }

}