package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.Game.Screen;
import com.handsomegoats.panda7.controller.AController;
import com.handsomegoats.panda7.controller.TitleScreenController;
import com.handsomegoats.panda7.view.*;

public class InputTitle implements IInput {
	public static String TAG = InputTitle.class.getSimpleName();
	
	public InputTitle(){
		Main.debug(TAG, "InputTitle Started.");	
	}
	
	public void touchDown(AController controller, IView view, float x, float y) {
		TitleScreenController c = (TitleScreenController) controller;
		c.touchX = x;
		c.touchY = y;
	}

	public void touchMove(AController controller, IView view, float x, float y) {
		TitleScreenController c = (TitleScreenController) controller;
		c.touchX = x;
		c.touchY = y;
	}

	public void touchPress(AController controller, IView view, float x, float y) {
		// TitleScreenController c = (TitleScreenController) controller;
		
		Rectangle adventure = TitleScreenController.destAdventure;
		Rectangle quickPlay = TitleScreenController.destQuickPlay;
		Rectangle howToPlay = TitleScreenController.destHowToPlay;

		if (Rectangle.intersects(adventure, x, y)) {
			// TODO: Go to adventure screen
		} else if (Rectangle.intersects(quickPlay, x, y)) {
			// TODO: Difficulty select Screen
			AController.nextScreen = Screen.Game;
		} else if (Rectangle.intersects(howToPlay, x, y)) {
			// TODO: How To Play Screen
		}
	}
}
