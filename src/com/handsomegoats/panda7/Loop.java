package com.handsomegoats.panda7;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Loop extends Thread {

  private SurfaceHolder    surfaceHolder;
  private Game             gamePanel;
  private boolean          running;

  // Desired FPS
  private final static int MAX_FPS         = 60;

  // Maximum number of frames to be skipped
  private final static int MAX_FRAME_SKIPS = 5;

  // The frame period
  private final static int FRAME_PERIOD    = 1000 / MAX_FPS;

  public void setRunning(boolean running) {
    this.running = running;
  }

  public Loop(SurfaceHolder surfaceHolder, Game gamePanel) {
    super();
    this.surfaceHolder = surfaceHolder;
    this.gamePanel = gamePanel;
  }

  @Override
  public void run() {
    Canvas canvas;

    long beginTime; // the time when the cycle begun
    long delta; // the time it took for the cycle to execute
    int sleepTime; // ms to sleep (<0 if we're behind)
    int framesSkipped; // number of frames being skipped

    sleepTime = 0;

    while (running) {
      canvas = null;
      // try locking the canvas for exclusive pixel editing
      // in the surface
      try {
        canvas = this.surfaceHolder.lockCanvas();

        synchronized (surfaceHolder) {
          beginTime = System.currentTimeMillis();
          framesSkipped = 0; // Reset the frames skipped
          delta = System.currentTimeMillis() - beginTime;

          // update game state
          this.gamePanel.update(delta);
          this.gamePanel.updateGameTick();

          // render state to the screen
          // draws the canvas on the panel
          this.gamePanel.render(canvas);

          // Calculate how long the cycle took
          delta = System.currentTimeMillis() - beginTime;

          // Calculate sleep time
          sleepTime = (int) (FRAME_PERIOD - delta);

          if (sleepTime > 0) {
            // if sleepTime > 0 we're ok
            try {
              // send the thread to sleep for a short period
              // very useful for battery saving
              Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
              // TODO: handle exception
            }
          }

          while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
            // we need to catch up
            this.gamePanel.update(delta); // update without rendering
            this.gamePanel.updateGameTick();
            sleepTime += FRAME_PERIOD; // add frame period to check
            // if in next
            // frame
            framesSkipped++;
          }
        }
      } finally {
        // in case of an exception the surface is not left in
        // an inconsistent state
        if (canvas != null) {
          surfaceHolder.unlockCanvasAndPost(canvas);
        }
      } // end finally
    }

  }

}
