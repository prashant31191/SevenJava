package com.handsomegoats.panda7;

import android.graphics.Canvas;

public interface Controller {
	public void update();
	public void draw(Canvas canvas);
	public void touchDown(float x, float y);
	public void touchMove(float x, float y);
}
