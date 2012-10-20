package com.handsomegoats.panda7;

import java.util.Random;

import com.handsomegoats.panda7.Game.Screen;
import com.handsomegoats.panda7.events.EventListener;
import com.handsomegoats.panda7.view.*;
import com.handsomegoats.panda7.input.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

	public enum Screen {
		Nothing, Title, LevelSelect, DifficultySelect, Game, GameOver, HighScore, HowToPlay
	}

	private static final String TAG = Game.class.getSimpleName();

	// Color
	public static int cSkyBlue = Color.rgb(92, 209, 222);
	public static int cGreenBack = Color.rgb(159, 227, 40);

	private static final int DEFAULT_START_HEIGHT = 3;
	private static final int DEFAULT_DIFFICULTTY = 5;

	public static Bitmap sprites;
	public static Bitmap background;
	public static Bitmap clouds;
	public static Bitmap titlesprites;
	public static Bitmap title;

	private Input input;
	public static Random random;

	public static AController controller;
	public static IView view;

	public static boolean hd;

	public static Loop thread;
	public static long gameTick = 0;
	public static double gameTime;
	public static double delta;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int GRID_SIZE = 7;
	public static int NEW_LEVEL_DEFAULT_HEIGHT = 3;
	public static float EMPTY_SPACE_PERCENT = 0.5f;
	public static int[] CHAIN = { 7, 39, 109, 224, 391, 617, 907, 1267, 1701,
			2213, 2809, 3391, 3851, 4265, 4681, 5113 };
	public static int BRICK_TILE = -2;
	public static int BROKEN_BRICK_TILE = -1;
	public static int NO_TILE = 0;
	public static Typeface font;
	public static int HD_THRESHOLD = 480;

	public static int startHeight = 3;
	public static int difficulty = 5;

	public static IInput controllerInput;

	public Game(Context context, int screenWidth, int screenHeight,
			Typeface font) {
		super(context);

		// Set font
		Game.font = font;

		// Set screen size
		Game.SCREEN_WIDTH = screenWidth;
		Game.SCREEN_HEIGHT = screenHeight;

		// Load Images
		if (Game.SCREEN_WIDTH > HD_THRESHOLD)
			hd = true;

		// Load SD or HD graphics
		if (hd) {
			sprites = BitmapFactory.decodeResource(getResources(),
					R.drawable.spriteshd);
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.backgroundhd);
			clouds = BitmapFactory.decodeResource(getResources(),
					R.drawable.cloudshd);
			titlesprites = BitmapFactory.decodeResource(getResources(),
					R.drawable.titlespriteshd);
			title = BitmapFactory.decodeResource(getResources(),
					R.drawable.titlehd);
		} else {
			sprites = BitmapFactory.decodeResource(getResources(),
					R.drawable.sprites);
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.background);
			clouds = BitmapFactory.decodeResource(getResources(),
					R.drawable.clouds);
			titlesprites = BitmapFactory.decodeResource(getResources(),
					R.drawable.titlesprites);
			title = BitmapFactory.decodeResource(getResources(),
					R.drawable.title);
		}

		// Add Input Device
		this.input = new TouchInput();

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
		Game.controllerInput = new InputTitle();
		
		Main.debug(TAG, Game.controller.toString());
		
		// Create the Game Loop thread
		thread = new Loop(getHolder(), this);

		// Make the GamePanel focusable so it can handle events
		setFocusable(true);

		Main.debug(TAG, "Game Started. From Title Screen");
		
		// for (StackTraceElement iterable_element : Thread.currentThread().getStackTrace()) Main.debug(TAG, iterable_element.toString());
		
	}

	public Game(Context context, GameController c, int screenWidth,
			int screenHeight, Typeface font) {
		super(context);

		// Set font
		Game.font = font;

		// Set screen size
		Game.SCREEN_WIDTH = screenWidth;
		Game.SCREEN_HEIGHT = screenHeight;

		// Load Images
		if (Game.SCREEN_WIDTH > HD_THRESHOLD)
			hd = true;

		// Load SD or HD graphics
		if (hd) {
			sprites = BitmapFactory.decodeResource(getResources(), R.drawable.spriteshd);
			background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundhd);
			clouds = BitmapFactory.decodeResource(getResources(), R.drawable.cloudshd);
			titlesprites = BitmapFactory.decodeResource(getResources(), R.drawable.titlespriteshd);
			title = BitmapFactory.decodeResource(getResources(), R.drawable.titlehd);
		} else {
			sprites = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);
			background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			clouds = BitmapFactory.decodeResource(getResources(), R.drawable.clouds);
			titlesprites = BitmapFactory.decodeResource(getResources(), R.drawable.titlesprites);
			title = BitmapFactory.decodeResource(getResources(), R.drawable.title);
		}

		// Add Input Device
		this.input = new TouchInput();

		// Create Random
		Game.random = new Random();

		// Adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// Game settings
		startHeight = DEFAULT_START_HEIGHT;
		difficulty = DEFAULT_DIFFICULTTY;

		Game.controller = c;
		Game.view = new SpriteView();
		Game.controllerInput = new InputGame();

		// Create the Game Loop thread
		thread = new Loop(getHolder(), this);

		// Make the GamePanel focusable so it can handle events
		setFocusable(true);

		Main.debug(TAG, "Game Started (From GameState File)");
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
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

		if (input.down(event)) {
			// Execute TouchDown method
			Game.controllerInput.touchDown(Game.controller, Game.view, event.getX(), event.getY());
		}

		if (input.press(event)) {
			// Execute touchPress method
			Game.controllerInput.touchPress(Game.controller, Game.view, event.getX(), event.getY());
		}

		if (input.move(event)) {
			// Execute TouchMove method
			Game.controllerInput.touchMove(Game.controller, Game.view, event.getX(), event.getY());
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
			break;
		case LevelSelect:
			break;
		case DifficultySelect:
			break;
		case Game:
			Game.controller = new GameController(startHeight, difficulty);
			Game.view = new SpriteView();
			Game.controllerInput = new InputGame();
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
	
	public void startGame() {

	}

}
