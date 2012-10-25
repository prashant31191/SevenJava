package com.handsomegoats.panda7.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Vector2;

public class ViewParticleAnimation extends AbstractAnimation {
  private static final long   serialVersionUID = 1L;
  private static final int    PARTICLE_COUNT   = 20;
  private static final int    MAX_WIDTH        = 32;
  private static final int    MAX_HEIGHT       = 32;
  private static final float  MAX_SPEED        = 100;
  private static final int    MAX_TTL          = 20;
  private int                 startX;
  private int                 startY;
  private int                 color;
  private ArrayList<Particle> particles;

  class Particle implements Serializable {
    private static final long serialVersionUID = 1L;
    float                     x;
    float                     y;
    float                     vx;
    float                     vy;
    float                     w;
    float                     h;
    int                       ttl;
    float                     scaleDecrement;
    public boolean            destroy          = false;

    // This should convert x,y to pixel coords
    public Particle(int x, int y, int maxParticleWidth, int maxParticleHeight) {
      ViewGame view = (ViewGame) Game.view;
      Vector2 p = view.coordsToPixels(x, y);

      this.x = p.x;
      this.y = p.y;

      // Random TTL
      this.ttl = Game.random.nextInt(MAX_TTL);

      if (this.ttl < 1)
        this.ttl = 1;

      // Random Size
      float scale = Game.random.nextFloat();

      if (scale < 0.01f)
        scale = 0.01f;

      this.w = scale * maxParticleWidth;
      this.h = this.w;

      // Define size decrement
      this.scaleDecrement = scale / this.ttl;

      // Random Speed
      this.vx = Game.random.nextFloat() * MAX_SPEED * getRandomPolarity();
      this.vy = Game.random.nextFloat() * MAX_SPEED * getRandomPolarity();

      if (Math.abs(this.vx) < 0)
        this.vx = 0.01f;

      if (Math.abs(this.vy) < 0)
        this.vy = 0.01f;

      // Reposition particles to center of start position
      this.x += this.w / 2;
      this.y += this.h / 2;
    }

    private float getRandomPolarity() {
      float polarity = 1;
      if (Game.random.nextFloat() * 2 - 1 < 0)
        polarity = -1;
      return polarity;
    }

    public void update(double delta) {
      if (--this.ttl <= 0)
        this.destroy = true;

      this.x += this.vx * delta;
      this.y += this.vy * delta;

      this.w -= this.scaleDecrement;
      this.h -= this.scaleDecrement;
    }

    public void draw(Canvas canvas) {
      Paint paint = new Paint();
      paint.setColor(color);

      canvas.drawRect((float) x, (float) y, (float) (x + w), (float) (y + h), paint);
    }
  }

  public ViewParticleAnimation(int x, int y, int value) {
    this.startX = x;
    this.startY = y;
    this.color = ViewGame.tileColors[value];
    this.particles = new ArrayList<Particle>();
    this.destroy = false;

    this.addParticles();
  }

  private void addParticles() {
    for (int i = 0; i < PARTICLE_COUNT; i++) {
      particles.add(new Particle(startX, startY, MAX_WIDTH, MAX_HEIGHT));
    }
  }

  public void update(long gameTime, double delta) {
    super.update(gameTime, delta);

    for (int i = 0; i < particles.size(); i++) {
      if (particles.get(i).destroy)
        particles.remove(i);
      else
        particles.get(i).update(delta);
    }

    if (particles.size() == 0)
      destroy = true;
  }

  public void draw(Canvas canvas) {
    super.draw(canvas);

    for (Particle p : particles) {
      p.draw(canvas);
    }

  }

}
