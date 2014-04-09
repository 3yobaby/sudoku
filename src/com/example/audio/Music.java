package com.example.audio;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer mp = null;
	public static void play(Context context, int resource) {
		stop(context);
		mp = MediaPlayer.create(context, resource);
		mp.setLooping(true);
		mp.start();
	}
	public static void stop(Context context) {
		if(mp != null){
			mp.stop();
			mp.release(); // 플레이어에 관련된 시스템 리소스를 풀어준다. 빠뜨리면 gc를 기다리지 못하고 프로그램 실패
			mp = null;
		}
	}

}
