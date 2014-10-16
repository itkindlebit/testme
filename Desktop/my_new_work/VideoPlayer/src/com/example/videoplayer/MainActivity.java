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
import android.media.ThumbnailUtils;
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

public class MainActivity extends Activity  {
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
	boolean a = false;
	String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list1 = new ArrayList<HashMap<String, String>>();
		thumb1 = new ArrayList<HashMap<String, String>>();
		lv = (ListView) findViewById(R.id.listView1);
		getWindow().setFormat(PixelFormat.UNKNOWN);
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
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent in=new Intent(getApplicationContext(), VideoPlay.class);
				in.putExtra("position", list1.get(position).get("Data"));
				startActivity(in);
				
			}
		});

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
