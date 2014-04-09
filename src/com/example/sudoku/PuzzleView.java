package com.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class PuzzleView extends View{
	private static final String TAG =  "Sudoku";
	private final Game game;
	public PuzzleView(Context context) {
		super(context);
		game = (Game)context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	private float width; // 타일의 폭
	private float height;
	private int selX; // 셀렉션의 x인덱스
	private int selY;
	private final Rect selRect = new Rect(); // 선택용 커서를 추적하는데 사용하는 사각형
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) { // 화면의 각 타일 크기를 계산
		width = w/ 9f;
		height = h/9f;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged : width " + width + ", height " + height);
		super.onSizeChanged(w, h, oldw, oldh); 
	}
	
	// 사각형의 위치를 계산한다.
	private void getRect(int x, int y, Rect rect){
		rect.set((int)(x*width), (int)(y*height), 
				(int)(x * width + width), (int)(y * height + height));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// 배경그리기
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		// 보드 그리기
			// 그리드 라인 색상 정의하기
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));
			// 보조 그리드 라인 그리기
		for (int i = 0; i < 9; i++) {
			canvas.drawLine(0, i*height,  getWidth(), i * height, light);
			canvas.drawLine(0, i*height + 1,  getWidth(), i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width+1, 0, i*width+1, getHeight(), hilite);
		}
			// 주 그리드 라인 그리기
		for (int i = 0; i < 9; i++) {
			if(i % 3 != 0)
				continue;
			canvas.drawLine(0, i*height,  getWidth(), i * height, dark);
			canvas.drawLine(0, i*height + 1,  getWidth(), i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width+1, 0, i*width+1, getHeight(), hilite);
		}
		// 숫자 그리기
			// 숫자 생상과 스타일 정의하기
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height); // ??
		foreground.setTextAlign(Paint.Align.CENTER);
			// 타일의 중앙에 숫자 그리기
		FontMetrics fm = foreground.getFontMetrics();
		float x = width / 2;
			// 중앙에 놓기, 오름/내림 경사 먼저 측정?
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				canvas.drawText(this.game.getTileString(i, j), i * width + x, j * height + y, foreground);
			}
		}
		// 힌트 그리기
			// 남은 기회 수에 따라 힌트색 설정하기
		/*
		Paint hint = new Paint();
		int c[] = {getResources().getColor(R.color.puzzle_hint_0),
				getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2),}; // 책에는 콤마만잇다.
		Rect r = new Rect();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				
			}
		}
		*/
		// 셀렉션 그리기
		Log.d(TAG, "selRect=" + selRect); // 반환되는 String?
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selected); // Rect, Paint
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown: keycode= " + keyCode + ", event = " + event);
		switch(keyCode){
		// 이동
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX,selY - 1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX,selY + 1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX - 1,selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX + 1,selY);
			break;
		// 키 입력
		case KeyEvent.KEYCODE_0:
			setSelectedTile(0);
			break;
		case KeyEvent.KEYCODE_SPACE:
			setSelectedTile(0);
			break;
		case KeyEvent.KEYCODE_1:
			setSelectedTile(1);
			break;
		case KeyEvent.KEYCODE_2:
			setSelectedTile(2);
			break;
		case KeyEvent.KEYCODE_3:
			setSelectedTile(3);
			break;
		case KeyEvent.KEYCODE_4:
			setSelectedTile(4);
			break;
		case KeyEvent.KEYCODE_5:
			setSelectedTile(5);
			break;
		case KeyEvent.KEYCODE_6:
			setSelectedTile(6);
			break;
		case KeyEvent.KEYCODE_7:
			setSelectedTile(7);
			break;
		case KeyEvent.KEYCODE_8:
			setSelectedTile(8);
			break;
		case KeyEvent.KEYCODE_9:
			setSelectedTile(9);
			break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeypadOrError(selX,selY);
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN){
			return super.onTouchEvent(event);
		}
		select((int)(event.getX() / width), (int)(event.getY() / height));
		game.showKeypadOrError(selX, selY);
		Log.d(TAG, "onTouchEvent: x" + selX + ", Y " + selY);
		return true;
	}
	
	public void setSelectedTile(int tile) {
		if(game.setTileIfValid(selX,selY,tile)){
			invalidate(); // 힌트를 바꿀 수 있음?
		}else{
			Log.d(TAG, "setSelectedTile: invalid:" + tile);
		}
	}

	private void select(int x, int y) {
		invalidate(selRect); // ?
		selX = Math.min(Math.max(x, 0), 8); // 0에서 8 사이의 값
		selY = Math.min(Math.max(y, 0), 8);
		getRect(selX,selY,selRect);
		invalidate(selRect);
	}
}
