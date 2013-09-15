package com.clubkunz.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Radar extends SurfaceView implements SurfaceHolder.Callback{
	private final int range = 1000;
	RunnerThread runner;
	Game g;
	
	public Radar(Context context, AttributeSet set) {
		super(context, set);
		getHolder().addCallback(this);
		setFocusable(true);
		runner = new RunnerThread(getHolder(), this);
		g=(Game)context;
	}

	public void repaint(){
		Canvas c=getHolder().lockCanvas();
		try{if(c!=null) draw(c);}
		finally{if(c!=null) getHolder().unlockCanvasAndPost(c);}
	}
	
	public void draw(Canvas canvas) {
		int w = canvas.getWidth();
		int h = canvas.getHeight();
		Paint p = new Paint();
		p.setColor(0xDDDDDDDD);
		double radius = Math.min(w/2d, h/2d);
		canvas.drawCircle(w/2, h/2, (float)radius, p);
		p.setColor(Color.RED);
		synchronized(g.bombs){
			for(Bomb b : g.bombs){
				double dist = b.location.distanceTo(g.me.tracker.currentLocation);
				if(dist < range){
					double angle = Math.toRadians(90 - g.me.tracker.currentLocation.getBearing() - g.me.tracker.currentLocation.bearingTo(b.location));
					int r = (int)(((double)dist/range)*radius);
					canvas.drawCircle((float)(r*Math.cos(angle) + w/2d), (float)(r*Math.sin(angle) + h/2d), 10, p);
				}
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		runner.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		runner.running=false;
	}

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
