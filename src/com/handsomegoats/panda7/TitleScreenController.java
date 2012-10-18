package com.handsomegoats.panda7;

public class TitleScreenController {
	enum Menu {
		QuickPlay,
		HowToPlay
	}
	
	Menu selected;
	
	public TitleScreenController(){
		selected = Menu.QuickPlay;
	}
}
