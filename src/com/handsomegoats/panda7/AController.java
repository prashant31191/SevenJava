package com.handsomegoats.panda7;

import com.handsomegoats.panda7.Game.Screen;

public abstract class AController implements IController {
	public static boolean destroy = false;
	public static Game.Screen nextScreen = Screen.Nothing;
}
