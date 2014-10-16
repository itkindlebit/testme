package com.example.videoplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements SurfaceHolder.Callback,OnCompletionListener {
	Cursor c;
	ListView lv;
	ArrayList<HashMap<String, String>> list1, thumb1;
	String[] thumbcol = { MediaStore.Video.Thumbnails.DATA,
			MediaStore.Video.Thumbnails.VIDEO_ID };
	String thumbPath;
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
	String data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list1 = new ArrayList<HashMap<String, String>>();
		thumb1 = new ArrayList<HashMap<String, String>>();
		lv = (ListView) findViewById(R.id.listView1);
		getWindow().setFormat(PixelFormat.UNKNOWN);
		fm = (FrameLayout) findViewById(R.id.media_layout2);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay2);
		btnForward = (ImageButton) findViewById(R.id.btnForward2);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward2);
		btnNext = (ImageButton) findViewById(R.id.btnNext2);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious2);
		main_layout = (FrameLayout) findViewById(R.id.main_layout2);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar2);
		mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView2);
		b1 = (Button) findViewById(R.id.button2);
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
		// c.close();
		
		lv.setAdapter(new listadapter1(MainActivity.this));
		surfaceHolder = mSurfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer = new MediaPlayer();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				mediaPlayer.reset();
				surfaceHolder.addCallback(MainActivity.this);

				 data=list1.get(position).get("Data");
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
				main_layout.setVisibility(View.VISIBLE);
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
//			if(a){
//				float videoWidth = mediaPlayer.getVideoWidth();
//				float videoHeight = mediaPlayer.getVideoHeight();
//
//				View container = (View) mSurfaceView.getParent();
//				float containerWidth = container.getWidth();
//				float containerHeight = container.getHeight();
//
//				android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
//				lp.width = (int) containerWidth;
//				lp.height = (int) ((videoHeight / videoWidth) * containerWidth);
//				if (lp.height > containerHeight) {
//					lp.width = (int) ((videoWidth / videoHeight) * containerHeight);
//					lp.height = (int) containerHeight;
//				}
//				mSurfaceView.setLayoutParams(lp);
//				a=false;
//			}else if(a==false){
//				android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
//			
//				lp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
//				lp.width=android.view.ViewGroup.LayoutParams.MATCH_PARENT;
//				mSurfaceView.setLayoutParams(lp);
//				a=true;
//			}
				if(a == true){
					lv.setVisibility(View.VISIBLE);
					a=false;
				}else if(a == false){
					lv.setVisibility(View.GONE);
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
//		float videoWidth = mediaPlayer.getVideoWidth();
//		float videoHeight = mediaPlayer.getVideoHeight();
//
//		View container = (View) mSurfaceView.getParent();
//		float containerWidth = container.getWidth();
//		float containerHeight = container.getHeight();
//
//		android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
//		lp.width = (int) containerWidth;
//		lp.height = (int) ((videoHeight / videoWidth) * containerWidth);
//		if (lp.height > containerHeight) {
//			lp.width = (int) ((videoWidth / videoHeight) * containerHeight);
//			lp.height = (int) containerHeight;
//		}
//		mSurfaceView.setLayoutParams(lp);
		mediaPlayer.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

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
			
			int videoId = Integer.parseInt(list1.get(position).get("_id"));
			Cursor videoThumbnailCursor = managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
			thumbcol, MediaStore.Video.Thumbnails.VIDEO_ID+ "=" + videoId, null, null);
			if (videoThumbnailCursor.moveToFirst()) {
				do {
					thumbPath = videoThumbnailCursor.getString(videoThumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
					
				} while (videoThumbnailCursor.moveToNext());
			}
			Log.i("ThumbPath: ",""+thumbPath);
			Log.i("id", ""+list1.get(position).get("Name"));
			File f=new File(thumbPath);
			Bitmap b=BitmapFactory.decodeFile(thumbPath);
			 img.setImageBitmap(b);

			return v;
		}

	}



	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}
	
}
