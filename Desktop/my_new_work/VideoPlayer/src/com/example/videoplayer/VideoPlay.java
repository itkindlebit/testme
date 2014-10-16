package com.example.videoplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

public class VideoPlay extends Activity implements SurfaceHolder.Callback {
	VideoView vx;
	MediaController mcon;
	Cursor c;
	ArrayList<HashMap<String, String>> list1;
	FrameLayout fm, main_layout;
	SeekBar songProgressBar;
	ImageButton btnPlay, btnForward, btnBackward, btnNext, btnPrevious;
	private MediaPlayer mediaPlayer;
	SurfaceView mSurfaceView;
	SurfaceHolder surfaceHolder;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000;
	Button b1;
	boolean a=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_play);

		// vx = (VideoView) findViewById(R.id.videoView1);
		getWindow().setFormat(PixelFormat.UNKNOWN);
		fm = (FrameLayout) findViewById(R.id.media_layout);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		main_layout = (FrameLayout) findViewById(R.id.main_layout);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
		b1 = (Button) findViewById(R.id.button1);
		 final String cols[] = { MediaStore.Video.Media.DISPLAY_NAME,
		 MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE,
		 MediaStore.Video.Media.DATA };
		 c = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cols,null, null, null);
		 if (c.moveToFirst()) {
		 do {
		 HashMap<String, String> hs = new HashMap<String, String>();
		 hs.put("Name", c.getString(0));
		 hs.put("_id", c.getString(1));
		 hs.put("Size", c.getString(2));
		 hs.put("Data", c.getString(3));
		 list1.add(hs);
		 Log.i("list", ""+list1);
		 } while (c.moveToNext());
		 }

		String data = getIntent().getStringExtra("position");
		mediaPlayer = new MediaPlayer();
		surfaceHolder = mSurfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(data);
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		main_layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
				// TODO Auto-generated method stub
				fm.postDelayed(new Runnable() {

					public void run() {

						fm.setVisibility(View.GONE);

					}
				}, 3000); // (3000 == 3secs)
				fm.setVisibility(View.VISIBLE);
				return false;
			}
		});
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mediaPlayer.isPlaying()) {
					if (mediaPlayer != null) {
						mediaPlayer.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				} else {
					// Resume song
					if (mediaPlayer != null) {
						mediaPlayer.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}
			}
		});

		btnForward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int currentPosition = mediaPlayer.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if (currentPosition + seekForwardTime <= mediaPlayer
						.getDuration()) {
					// forward song
					mediaPlayer.seekTo(currentPosition + seekForwardTime);
				} else {
					// forward to end position
					mediaPlayer.seekTo(mediaPlayer.getDuration());
				}
			}
		});
		btnBackward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int currentPosition = mediaPlayer.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if (currentPosition - seekBackwardTime >= 0) {
					// forward song
					mediaPlayer.seekTo(currentPosition - seekBackwardTime);
				} else {
					// backward to starting position
					mediaPlayer.seekTo(0);
				}
			}
		});
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(a){
				float videoWidth = mediaPlayer.getVideoWidth();
				float videoHeight = mediaPlayer.getVideoHeight();

				View container = (View) mSurfaceView.getParent();
				float containerWidth = container.getWidth();
				float containerHeight = container.getHeight();

				android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
				lp.width = (int) containerWidth;
				lp.height = (int) ((videoHeight / videoWidth) * containerWidth);
				if (lp.height > containerHeight) {
					lp.width = (int) ((videoWidth / videoHeight) * containerHeight);
					lp.height = (int) containerHeight;
				}
				mSurfaceView.setLayoutParams(lp);
				a=false;
			}else if(a==false){
				android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
			
				lp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
				lp.width=android.view.ViewGroup.LayoutParams.MATCH_PARENT;
				mSurfaceView.setLayoutParams(lp);
				a=true;
			}
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mediaPlayer.setDisplay(holder);
		float videoWidth = mediaPlayer.getVideoWidth();
		float videoHeight = mediaPlayer.getVideoHeight();

		View container = (View) mSurfaceView.getParent();
		float containerWidth = container.getWidth();
		float containerHeight = container.getHeight();

		android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
		lp.width = (int) containerWidth;
		lp.height = (int) ((videoHeight / videoWidth) * containerWidth);
		if (lp.height > containerHeight) {
			lp.width = (int) ((videoWidth / videoHeight) * containerHeight);
			lp.height = (int) containerHeight;
		}
		mSurfaceView.setLayoutParams(lp);
		mediaPlayer.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
