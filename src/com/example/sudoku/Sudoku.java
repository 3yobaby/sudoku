package com.example.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.audio.Music;

public class Sudoku extends Activity implements OnClickListener{
	private static final String TAG = "Sudoku";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View continueButton = this.findViewById(R.id.continue_button);
		View newButton = this.findViewById(R.id.new_button);
		View exitButton = this.findViewById(R.id.exit_button);
		View aboutButton = this.findViewById(R.id.about_button);
		continueButton.setOnClickListener(this);
		newButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
		aboutButton.setOnClickListener(this);
	}

	// 메뉴 관련 메서드
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater(); // xml을 불러오기 위함
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	protected void onResume() {	
		super.onResume();
		Music.play(this, R.raw.background);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
		}
		return false;
	}
	
	//
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.continue_button:
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}
	
	private void openNewGameDialog(){
		new AlertDialog.Builder(this).setTitle(R.string.new_game_title)
			.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					startGame(i);
				}
			})
			.show();
	}
	private void startGame(int i){
		Log.d(TAG, "clicked on " + i);
		Intent startIntent = new Intent(this, Game.class);
		startIntent.putExtra(Game.KEY_DIFFICULTY, i); // 실제에는 KEY앞에 package이름을 붙인다.
		startActivity(startIntent);
	}
}
