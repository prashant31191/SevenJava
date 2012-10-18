package com.handsomegoats.panda7.view;

import android.graphics.Canvas;

public interface IView {
	public void update(double gametime, double delta);
	public void draw(Canvas canvas);
	public int getTileSize();
	public int getXOffset();
	public int getYOffset();
	public int getGap();
}
