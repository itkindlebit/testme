package com.example.videoplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlay extends Activity implements SurfaceHolder.Callback,
		SeekBar.OnSeekBarChangeListener {
	VideoView vx;
	MediaController mcon;
	Cursor c;
	ArrayList<HashMap<String, String>> list1;
	FrameLayout fm, main_layout;
	SeekBar songProgressBar;
	ImageButton btnPlay, btnForward, btnBackward, b1;
	private MediaPlayer mediaPlayer;
	SurfaceView mSurfaceView;
	SurfaceHolder surfaceHolder;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000;
	boolean a = false;
	Utilities utils;
	Handler mHandler = new Handler();
	ListView lv;
	LinearLayout ln;

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
		lv = (ListView) findViewById(R.id.listView2);
		main_layout = (FrameLayout) findViewById(R.id.main_layout);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
		b1 = (ImageButton) findViewById(R.id.button1);
		utils = new Utilities();
		ln = (LinearLayout) findViewById(R.id.layout1);

		list1 = new ArrayList<HashMap<String, String>>();
		final String cols[] = { MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE,
				MediaStore.Video.Media.DATA };
		c = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cols,
				null, null, null);
		if (c.moveToFirst()) {
			do {
				HashMap<String, String> hs = new HashMap<String, String>();
				hs.put("Name", c.getString(0));
				hs.put("_id", c.getString(1));
				hs.put("Size", c.getString(2));
				hs.put("Data", c.getString(3));
				list1.add(hs);
			} while (c.moveToNext());
		}
		lv.setAdapter(new listadapter1(VideoPlay.this));
		String data1 = getIntent().getStringExtra("position");
		surfaceHolder = mSurfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer = new MediaPlayer();
		try {

			mediaPlayer.setDataSource(data1);
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
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				try {
					mediaPlayer.reset();
					mediaPlayer.setDataSource(list1.get(position).get("Data"));
					mediaPlayer.prepare();
					mediaPlayer.start();
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
			}
		});
		main_layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
				// TODO Auto-generated method stub
				fm.postDelayed(new Runnable() {

					public void run() {
						fm.setVisibility(View.GONE);
					}
				}, 3000);
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
						btnPlay.setImageResource(android.R.drawable.ic_media_play);
					}
				} else {
					if (mediaPlayer != null) {
						mediaPlayer.start();
						btnPlay.setImageResource(android.R.drawable.ic_media_pause);
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

		songProgressBar.setOnSeekBarChangeListener(this);

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (a) {
					ln.setVisibility(View.VISIBLE);
					b1.setImageResource(R.drawable.bb);
					a = false;
				} else if (a == false) {
					ln.setVisibility(View.GONE);
					b1.setImageResource(R.drawable.aa);
					a = true;
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
		/*
		 * float videoWidth = mediaPlayer.getVideoWidth(); float videoHeight =
		 * mediaPlayer.getVideoHeight();
		 * 
		 * View container = (View) mSurfaceView.getParent(); float
		 * containerWidth = container.getWidth(); float containerHeight =
		 * container.getHeight();
		 * 
		 * android.view.ViewGroup.LayoutParams lp =
		 * mSurfaceView.getLayoutParams(); lp.width = (int) containerWidth;
		 * lp.height = (int) ((videoHeight / videoWidth) * containerWidth); if
		 * (lp.height > containerHeight) { lp.width = (int) ((videoWidth /
		 * videoHeight) * containerHeight); lp.height = (int) containerHeight; }
		 * mSurfaceView.setLayoutParams(lp);
		 */
		mediaPlayer.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mediaPlayer.getDuration();
			long currentDuration = mediaPlayer.getCurrentPosition();

			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			// Log.d("Progress", ""+progress);
			songProgressBar.setProgress(progress);

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	@Override
	public void onProgressChanged(SeekBar seek, int arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateTimeTask);

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mediaPlayer.seekTo(currentPosition);
		updateProgressBar();
		// update timer progress again

	}

	public class listadapter1 extends BaseAdapter {
		Context context;

		public listadapter1(Context con) {
			// TODO Auto-generated constructor stub
			context = con;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list1.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tx;
			ImageView img;

			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.listitem, null);
			tx = (TextView) v.findViewById(R.id.textView1);
			img = (ImageView) v.findViewById(R.id.imageView1);
			tx.setText(list1.get(position).get("Name"));

			Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(
					list1.get(position).get("Data"),
					MediaStore.Video.Thumbnails.MINI_KIND);
			img.setImageBitmap(thumbnail);

			return v;
		}

	}

}
