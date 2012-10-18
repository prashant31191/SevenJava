package com.handsomegoats.panda7;

import java.util.Random;

import com.handsomegoats.panda7.events.EventListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = Game.class.getSimpleName();
	
	public static Bitmap sprites;
	public static Bitmap background;

	private Input input;
	public static Random random;
	public GridController controller;

	public static Loop thread;
	public static long gameTick = 0;
	public static double gameTime;
	public static double delta;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int GRID_SIZE = 7;
	public static int DROPS_TILL_NEW_ROW = 5;
	public static int NEW_LEVEL_HEGHT = 3;
	public static float EMPTY_SPACE_PERCENT = 0.5f;
	public static int[] CHAIN = { 7, 39, 109, 224, 391, 617, 907, 1267, 1701,
			2213, 2809, 3391, 3851, 4265, 4681, 5113 };
	public static int BRICK_TILE = -2;
	public static int BROKEN_BRICK_TILE = -1;
	public static int NO_TILE = 0;

	public Game(Context context, int screenWidth, int screenHeight) {
		super(context);
		
		// Load Images
		sprites = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		
		// Set screen size
		this.SCREEN_WIDTH = screenWidth;
		this.SCREEN_HEIGHT = screenHeight;
		
		// Adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		this.input = new TouchInput();
		this.random = new Random();
		int difficulty = 3;
		this.controller = new GridController(this, difficulty);

		// Create the Game Loop thread
		thread = new Loop(getHolder(), this);

		// Make the GamePanel focusable so it can handle events
		setFocusable(true);
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
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (event.getY() < 50) {
				thread.setRunning(false);
				((Activity) getContext()).finish();
			}
		}

		if (input.down(event)) {
			// Execute TouchDown method
			// this.controller.touchDown(event.getX(), event.getY());
		}

		if (input.press(event)) {
			// Execute touchPress method
			this.controller.touchPress(event.getX(), event.getY());
		}

		if (input.move(event)) {
			// Execute TouchMove method
			this.controller.touchMove(event.getX(), event.getY());
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
			this.gameTime = getGameTime();
			this.delta = delta / 1000;
			
			this.controller.update();

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
			canvas.drawColor(Color.BLACK); // Clear Screen
			this.controller.draw(canvas);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
