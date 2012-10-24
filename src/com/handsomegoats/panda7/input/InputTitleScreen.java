package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.Game.Screen;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerTitleScreen;
import com.handsomegoats.panda7.view.*;

public class InputTitleScreen implements InterfaceInput {
	public static String TAG = InputTitleScreen.class.getSimpleName();
	
	public InputTitleScreen(){
		Main.debug(TAG, "InputTitle Started.");	
	}
	
	public void touchDown(AbstractController controller, InterfaceView view, float x, float y) {
		ControllerTitleScreen c = (ControllerTitleScreen) controller;
		c.touchX = x;
		c.touchY = y;
	}

	public void touchMove(AbstractController controller, InterfaceView view, float x, float y) {
		ControllerTitleScreen c = (ControllerTitleScreen) controller;
		c.touchX = x;
		c.touchY = y;
	}

	public void touchPress(AbstractController controller, InterfaceView view, float x, float y) {
		// TitleScreenController c = (TitleScreenController) controller;
		
		Rectangle adventure = ControllerTitleScreen.destAdventure;
		Rectangle quickPlay = ControllerTitleScreen.destQuickPlay;
		Rectangle howToPlay = ControllerTitleScreen.destHowToPlay;

		if (Rectangle.intersects(adventure, x, y)) {
			// TODO: Go to adventure screen
      Main.debug(TAG, "Adventure");
		} else if (Rectangle.intersects(quickPlay, x, y)) {
      Main.debug(TAG, "Clicked Quick Play");
			AbstractController.nextScreen = Screen.Game;
      // TODO: Go to Difficulty select Screen
		} else if (Rectangle.intersects(howToPlay, x, y)) {
		  Main.debug(TAG, "Clicked How to Play");
		  AbstractController.nextScreen = Screen.HowToPlay;
		}
	}
}
