package com.clubkunz.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Radar extends SurfaceView implements SurfaceHolder.Callback{
	RunnerThread runner;
	
	public Radar(Context context, AttributeSet set) {
		super(context, set);
		getHolder().addCallback(this);
		setFocusable(true);
		runner = new RunnerThread(getHolder(), this);
	}

	public void repaint(){
		Canvas c=getHolder().lockCanvas();
		try{if(c!=null) draw(c);}
		finally{if(c!=null) getHolder().unlockCanvasAndPost(c);}
	}
int i = 0;
	public void draw(Canvas canvas) {
		Paint p = new Paint();
		p.setColor(Color.RED);
		canvas.drawCircle(500, 1000, 50+i, p);
		i++;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		runner.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}

	private class RunnerThread extends Thread{
		private Radar radar;
		public boolean running = false;
		private Canvas c;
		private SurfaceHolder surface;
		
		public RunnerThread(SurfaceHolder s, Radar r) {
            surface = s;
            radar = r;
        }
		
		public void run(){
			running = true;
			while(running){
				c = surface.lockCanvas(null);
				synchronized(surface){
					if(c!=null) radar.draw(c);
				}
				if (c != null) surface.unlockCanvasAndPost(c);
			}
		}
	}

}
