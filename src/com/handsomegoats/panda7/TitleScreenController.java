package com.handsomegoats.panda7;

import com.handsomegoats.panda7.Game.Screen;
import com.handsomegoats.panda7.view.TitleView;

import android.graphics.Canvas;
import android.util.Log;

public class TitleScreenController extends AController implements IController {
	private static final String TAG = TitleScreenController.class
			.getSimpleName();

	double HEADER_H = 0.1;
	double TITLE_H = 0.5;
	double MENU_H = 0.1;

	private Rectangle destHeader;
	public static Rectangle destTitle;
	private Rectangle destMenuItem;

	long gameTime = 0;
	double delta = 0;
	long then = 0;

	public static Rectangle destQuickPlay;
	public static Rectangle destHowToPlay;
	public static Rectangle destAdventure;

	public float touchX = 0;
	public float touchY = 0;

	public TitleScreenController() {
		destHeader = new Rectangle(0, 0, Game.SCREEN_WIDTH, (HEADER_H * Game.SCREEN_HEIGHT));
		destTitle = new Rectangle(0, destHeader.y + destHeader.h, Game.SCREEN_WIDTH, (TITLE_H * Game.SCREEN_HEIGHT));
		destMenuItem = new Rectangle(0, destTitle.y + destTitle.h, Game.SCREEN_WIDTH, (MENU_H * Game.SCREEN_HEIGHT));

		// Create Button
		int buttonStart = (destTitle.y + destTitle.h);
		destAdventure = destMenuItem.clone();
		destAdventure.y = buttonStart + (destMenuItem.h * 0);
		destQuickPlay = destMenuItem.clone();
		destQuickPlay.y = buttonStart + (destMenuItem.h * 1);
		destHowToPlay = destMenuItem.clone();
		destHowToPlay.y = buttonStart + (destMenuItem.h * 2);
	}

	public void update() {
		// Game Time
		gameTime++;
		delta = (double) (then - System.currentTimeMillis()) / 1000.0;
		then = System.currentTimeMillis();
	}

	public void draw(Canvas canvas) {
	}

}
