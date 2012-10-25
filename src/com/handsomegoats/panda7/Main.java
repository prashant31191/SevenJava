package com.handsomegoats.panda7;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.handsomegoats.panda7.Game.Buttons;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerTitleScreen;
import com.handsomegoats.panda7.controller.ControllerGame;

import android.media.AudioManager;
// import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
  public static boolean       SOUND_ON           = true;
  public static boolean       MUSIC_ON           = true;
  public static Menu          menu;

  public static SoundPool     sounds;
  public static MediaPlayer   music;
  public static MediaPlayer   mediaPlayer;
  // public static int sndMicrobiaMusic = R.raw.microbia;
  // public static int sndBump = R.raw.bump;
  // public static int sndTone1 = R.raw.tone1;
  // public static int sndTone2 = R.raw.tone2;
  // public static int sndTone3 = R.raw.tone3;
  // public static int sndTone4 = R.raw.tone4;
  // public static int sndTone5 = R.raw.tone5;
  // public static int sndTone6 = R.raw.tone6;
  public static int           sndMicrobiaMusic   = R.raw.microbia; // R.raw.pandajam
  public static int           sndBump            = R.raw.noisebump;
  public static int           sndTone1           = R.raw.noisebump;
  public static int           sndTone2           = R.raw.noisebump;
  public static int           sndTone3           = R.raw.noisebump;
  public static int           sndTone4           = R.raw.noisebump;
  public static int           sndTone5           = R.raw.noisebump;
  public static int           sndTone6           = R.raw.noisebump;

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

  // @Override
  // public boolean onCreateOptionsMenu(Menu menu) {
  // getMenuInflater().inflate(R.menu.activity_main, menu);
  // Main.debug(TAG, "onCreateOptionsMenu");
  //
  // return true;
  // }
  //

  public boolean onCreateOptionsMenu(Menu menu) {
    // menu.add(int GroupID,int ItemID,int Order,String Title)
    Main.menu = menu;

    menu.add(1, 1, Buttons.Play.val, "Play").setIcon(R.drawable.ic_play);
    menu.add(1, 2, Buttons.Title.val, "Back to Title").setIcon(R.drawable.ic_back);
    menu.add(1, 3, Buttons.Sound.val, "Sound").setIcon(R.drawable.ic_sound);
    menu.add(1, 4, Buttons.Music.val, "Music").setIcon(R.drawable.ic_music);
    menu.add(1, 5, Buttons.HowToPlay.val, "How to Play").setIcon(R.drawable.ic_howtoplay);

    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    Main.debug(TAG, "onResume");

    // Load Audio
    loadAudio(this);

    // Loading preferences
    try {
      SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

      SOUND_ON = settings.getBoolean("SOUND_ON", true);
      MUSIC_ON = settings.getBoolean("MUSIC_ON", true);
      debug(TAG, "Preferences loaded");
    } catch (Exception e) {
      debug(TAG, "Error with preferences.");
    }

    // Make it full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // Rotate
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // Get Window Size
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);

    SCREEN_WIDTH = metrics.widthPixels;
    SCREEN_HEIGHT = metrics.heightPixels;

    // Load Font
    font = Typeface.createFromAsset(getAssets(), "fonts/8bitnog.ttf");

    // Set MainGamePanel as the view
    Main.debug(TAG, "Start loading...");
    startGame();
    Main.debug(TAG, "Finished loading.");

    // Load Audio
    // loadAudio(this);

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
    try {
      Main.debug(TAG, "onPause from Game");

      // Pause Music
      if (music != null)
        music.release();

      // Stop the thread
      Game.thread.setRunning(false);

      // Save preferences
      SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("SOUND_ON", SOUND_ON);
      editor.putBoolean("MUSIC_ON", MUSIC_ON);
      editor.commit();

      if (Game.controller instanceof ControllerGame) {
        // Save the current game
        Main.debug(TAG, "onPause: Game state saving...");
        saveGame(Game.controller);
      } else {
        // Delete game
        Main.debug(TAG, "onPause: Game state deleted.");
        deleteFile(FILENAME_GAMESTATE);
      }
    } catch (Exception e) {
      // TODO: handle exception
    }

    super.onPause();
  }

  @Override
  protected void onDestroy() {
    // App is killed
    Main.debug(TAG, "onDestroy");
    super.onDestroy();
  }

  private void saveGame(AbstractController controller) {
    if (controller instanceof ControllerGame) {
      writeGameState(this, (ControllerGame) controller);
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
    ControllerGame c = readGameState(this);
    game = new Game(this, c, SCREEN_WIDTH, SCREEN_HEIGHT, font);

    if (MUSIC_ON)
      playMusic(this);
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

  public void writeGameState(Context context, ControllerGame controller) {
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

  public ControllerGame readGameState(Context context) {
    ControllerGame gameController = null;
    ObjectInputStream input = null;
    FileInputStream file = null;

    try {
      file = openFileInput(FILENAME_GAMESTATE);
      input = new ObjectInputStream(file);
      gameController = (ControllerGame) input.readObject();

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
  @Override
  public void onBackPressed() {
    // super.onBackPressed();
    AbstractController.nextScreen = Game.Screen.Title;
    return;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Don't know how to do a switch with enum values

    switch (item.getItemId()) {
    case 1:
      // Play
      if (Game.controller instanceof ControllerTitleScreen) {
        // Start New Game
        AbstractController.nextScreen = Game.Screen.Game;
      } else if (Game.controller instanceof ControllerGame) {
        // Hide Menu aka do nothing
      }
      return true;
    case 2:
      // Menu
      if (Game.controller instanceof ControllerTitleScreen) {
        // Start New Game
      } else if (Game.controller instanceof ControllerGame) {
        AbstractController.nextScreen = Game.Screen.Title;
        // Hide Menu aka do nothing
      }
      return true;
    case 3:
      // Sound
      toggleSound();

      return true;
    case 4:
      // Music
      toggleMusic();
      return true;
    case 5:
      // How To Play
      return true;

    }
    return super.onOptionsItemSelected(item);

  }

  public void toggleSound() {
    MenuItem soundItem = Main.menu.findItem(3);
    if (SOUND_ON) {
      if (Main.sounds != null) {
        Main.sounds.stop(Main.sndBump);
        Main.sounds.stop(Main.sndTone1);
        Main.sounds.stop(Main.sndTone2);
        Main.sounds.stop(Main.sndTone3);
        Main.sounds.stop(Main.sndTone4);
        Main.sounds.stop(Main.sndTone5);
        Main.sounds.stop(Main.sndTone6);
      }
      SOUND_ON = false;
      soundItem.setIcon(R.drawable.ic_soundmute);
    } else {
      SOUND_ON = true;
      soundItem.setIcon(R.drawable.ic_sound);
    }

  }

  public void toggleMusic() {
    MenuItem musicItem = Main.menu.findItem(4);
    if (MUSIC_ON) {

      if (music != null)
        music.pause();

      MUSIC_ON = false;

      musicItem.setIcon(R.drawable.ic_musicmute);
    } else {

      MUSIC_ON = true;

      if (music != null)
        playMusic(this);

      musicItem.setIcon(R.drawable.ic_music);
    }

  }

  private void loadAudio(Context context) {
    try {
      Main.debug(TAG, "Start loading audio...");
      sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
      sndBump = sounds.load(context, sndBump, 1);
      sndTone1 = sounds.load(context, sndTone1, 1);
      sndTone2 = sounds.load(context, sndTone2, 1);
      sndTone3 = sounds.load(context, sndTone3, 1);
      sndTone4 = sounds.load(context, sndTone4, 1);
      sndTone5 = sounds.load(context, sndTone5, 1);
      sndTone6 = sounds.load(context, sndTone6, 1);
      Main.debug(TAG, "Finished loading audio.");
    } catch (Exception e) {
      Main.debug(TAG, "Problem loading audio.");
    }
  }

  public void onPrepared(MediaPlayer player) {
    music.start();
  }

  public static void playSound(int sound) {
    if (Main.SOUND_ON) {
      sounds.play(sound, 1f, 1f, 0, 0, 1f);
    }
  }

  public static void playMusic(Context context) {
    if (Main.MUSIC_ON) {
      Main.debug(TAG, "Loading Music...");

      if (music != null)
        music.release();

      music = MediaPlayer.create(context, sndMicrobiaMusic);
      music.setVolume(0.5f, 0.5f);
      music.start();

      Main.debug(TAG, "Finished loading music.");
    }
  }

}