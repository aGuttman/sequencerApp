package com.example.timer_two;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.SoundPool;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final int numBars = 8;
	private final int numSounds = 5;
	int bpm = 120;
	int per = 250;
	int something = 100;
	private Timer timer;
	private int[][] display = new int[numSounds][numBars];
	private int[][] soundArray = new int[numSounds][numBars];
	private int count = 0;
	private TimerTask timerTask;
	private TimerTask move;
	private SoundPool sp = new SoundPool (3, AudioManager.STREAM_MUSIC, 0);
	TextView textView;
	TextView perView;
	int sound1;
	int sound2;
	int sound3;
	int sound4;
	int sound5;
//	int sound6;
//	int sound7;
//	int sound8;
	int soundBlank;
	String out = "";
	String pos = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		for (int i =0; i<numSounds; i++){
			for (int j = 0; j<numBars; j++){
				display[i][j] = 0;
			}
		}
		for (int i =0; i<numSounds; i++){
			for (int j = 0; j<numBars; j++){
				soundArray[i][j] = 0;
			}
		}
		for (int i =0; i<numSounds; i++){
			for (int j = 0; j<numBars; j++){
				out += "0";
			}
			out += "\n";
		}
		sound1 = sp.load(getApplicationContext(), R.raw.tone_1, 1);
		sound2 = sp.load(getApplicationContext(), R.raw.tone_2, 1);
		sound3 = sp.load(getApplicationContext(), R.raw.tone_3, 1);
		sound4 = sp.load(getApplicationContext(), R.raw.tone_4, 1);
		sound5 = sp.load(getApplicationContext(), R.raw.tone_5, 1);
//		sound6 = sp.load(getApplicationContext(), R.raw.tone_6, 1);
//		sound7 = sp.load(getApplicationContext(), R.raw.tone_7, 1);
//		sound8 = sp.load(getApplicationContext(), R.raw.tone_8, 1);
		soundBlank = sp.load(getApplicationContext(), R.raw.nothing, 1);
		
		 textView = (TextView) findViewById(R.id.textView1);
		 perView = (TextView) findViewById(R.id.textView2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		if (timer != null){
			timer.cancel();
			timer.purge();
			timer = null;
			count=0;
		}
		super.onBackPressed();
	}
	
	final Runnable myRunnable = new Runnable() {
		public void run(){
			pos = "";
			for (int i = 0; i<numBars; i++){
				if (i==count)
					pos+="^";
				else
					pos+=" ";		
			}
			textView.setText(out+pos);
		}
	};
	
	final Handler myHandler = new Handler();
	
	public void makeTimer(){
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run(){
				playSound();
			}
		};
		
		move = new TimerTask() {
			@Override
			public void run(){
				updatePos();
			}
		};
		
		timer.schedule(timerTask,0,per);
		timer.schedule(move,50,per/2);
	}
	
	public void updatePos(){
		myHandler.post(myRunnable);
	}
	
	public void playSound(){
		for (int i = 0; i < numSounds; i++){
			int value = soundArray[i][count];
			if (value != 0)
				sp.play(value, 1, 1, 0, 0, 1);
		}
		count++;
		count = count%numBars;
	}
	
	public void click(View v) {
		String name = (String) v.getTag();
		int soundId = Character.getNumericValue(name.charAt(0));
		int timeId = Character.getNumericValue(name.charAt(2));
		out ="";
		
		if (display[soundId-1][timeId-1] == 1){
			display[soundId-1][timeId-1] = 0;
			soundArray[soundId-1][timeId-1] = 0;

		}
		else {
			display[soundId-1][timeId-1] = 1;
			soundArray[soundId-1][timeId-1] = getRawId(soundId);
		}
		
		for (int i =0; i<numSounds; i++){
			for (int j = 0; j<numBars; j++){
				out += display[i][j];
			}
			out += "\n";
		}
		
		textView.setText(out+pos);
	}
	
	public void startStop(View v) {
		if (timer != null){
			timer.cancel();
			timer.purge();
			timer = null;
		}
		
		else{
			if (count == 0)
				textView.setText(out+"^");
			makeTimer();
		}
		
	}
	
	public int getRawId (int id){
		int ID;
		switch (id){
		case 1:
			ID = sound1;
			break;
		case 2:
			ID = sound2;
			break;
		case 3:
			ID = sound3;
			break;
		case 4:
			ID = sound4;
			break;
		case 5:
			ID = sound5;
			break;
//		case 6:
//			ID = sound6;
//			break;
//		case 7:
//			ID = sound7;
//			break;
//		case 8:
//			ID = sound8;
//			break;
		default:
			ID = soundBlank;
			break;
		}
		return ID;
	}
	
	public void reset (View v){
		out = "";
		pos = "";
		for (int i =0; i<numSounds; i++){
			for (int j = 0; j<numBars; j++){
				display[i][j] = 0;
			}
		}
		for (int i =0; i<numSounds; i++){
			for (int j = 0; j<numBars; j++){
				soundArray[i][j] = 0;
			}
		}
		for (int i =0; i<numSounds; i++){
			for (int j = 0; j<numBars; j++){
				out += "0";
			}
			out += "\n";
		}
		textView.setText(out+"\n");
		
		if (timer != null){
			timer.cancel();
			timer.purge();
			timer = null;
			count=0;
		}
	}
	
	public void minus(View v){
		bpm = Math.max(bpm-5, 5);
		per = 60000/(2*bpm);
		perView.setText(Integer.toString(bpm));
		System.out.println(per);
		if (timer != null){
			timer.cancel();
			timer.purge();
			timer = null;
			makeTimer();
		}
	}
	
	public void plus(View v){
		bpm += 5;
		per = 60000/(2*bpm);
		perView.setText(Integer.toString(bpm));
		System.out.println(per);
		if (timer != null){
			timer.cancel();
			timer.purge();
			timer = null;
			makeTimer();
		}
	}
}
