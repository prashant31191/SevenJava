package com.handsomegoats.panda7;

import android.graphics.Rect;

public class Rectangle {
	public int x;
	public int y;
	public int w;
	public int h;

	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Rectangle(double x, double y, double w, double h) {
		this.x = (int) x;
		this.y = (int) y;
		this.w = (int) w;
		this.h = (int) h;
	}

	public Rectangle(float x, float y, float w, float h) {
		this.x = (int) x;
		this.y = (int) y;
		this.w = (int) w;
		this.h = (int) h;
	}

	public Rectangle clone() {
		return new Rectangle(this.x, this.y, this.w, this.h);
	}

	public Rect getRect() {
		Rect rect = new Rect(this.x, this.y, this.x + this.w, this.y + this.h);

		return rect;
	}
}
