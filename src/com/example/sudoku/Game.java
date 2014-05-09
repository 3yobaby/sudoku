package com.example.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.audio.Music;

public class Game extends Activity{
	
	private String TAG = "Sudoku";
	public static String KEY_DIFFICULTY = "difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	private int puzzle[] = new int[9*9];
	private PuzzleView puzzleView;
	
	/** 사용된 타일 캐시 */
	private final int used[][][] = new int[9][9][];
	
	/** 퍼즐 */
	private final String easyPuzzle =
			"360000000004230800000004200" +
			"070460003820000014500013020" +
			"001900000007048300000000045";
	private final String mediumPuzzle =
			"650000070000506000014000005" +
			"007009000002314700000700800" +
			"500000630000201000030000097";
	private final String hardPuzzle =
			"009000000080605020501078000" +
			"000000700706040102004000000" +
			"000720903090301080000000600";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();
		
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}
	/** 음악 */
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
	// 각 타일의 더이상 사용될 수 없는 숫자를 알아낸다.
	/** 사용된 타일의 2차원 배열 만들기 */
	private void calculateUsedTiles() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				used[x][y] = calculateUsedTiles(x,y);
			}
		}
	}

	private int[] calculateUsedTiles(int x, int y) {
		int temp[] = new int[9];
		// 가로
		for (int i = 0; i < 9; i++) {
			if(i == y)
				continue;
			int t = getTile(x, i);
			if(t != 0)
				temp[t -1] = t;
		}
		// 세로
		for (int i = 0; i < 9; i++) {
			if(i == x)
				continue;
			int t = getTile(i, y);
			if(t != 0)
				temp[t -1] = t;
		}
		// 블럭
		int startX = x / 3 * 3;
		int startY = y / 3 * 3;
		for (int i = startX; i < startX + 3; i++) {
			for (int j = startY; j < startY + 3; j++) {
				if(i == x && j == y)
					continue;
				int t = getTile(i,j);
				if(t != 0)
					temp[t-1] = t;
			}
		}
		// 압축
		int nused = 0;
		for (int n : temp) {
			if(n != 0)
				nused++;
		}
		int result[] = new int[nused];
		nused = 0;
		for(int n : temp){
			if(n != 0)
				result[nused++] = n;
		}
		return result;
	}
	private int getTile(int x, int y){
		return puzzle[x + y*9];
	}
	
	private int[] getPuzzle(int diff){
		String puz;
		switch(diff){
		case DIFFICULTY_HARD:
			puz = hardPuzzle;
			break;
		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			break;
		case DIFFICULTY_EASY:
			puz = easyPuzzle;
			break;
		default:
			puz = easyPuzzle;
			break;	
		}
		return fromPuzzleString(puz);
	}
	
	static private String toPuzzleString(int[] puz){
		StringBuilder buf = new StringBuilder();
		for(int ele : puz){
			buf.append(ele);
		}
		return buf.toString();
	}
	
	static protected int[] fromPuzzleString(String puzzle) {
		int[] puz = new int[puzzle.length()];
		for (int i = 0; i < puz.length; i++) {
			puz[i] = puzzle.charAt(i) - '0';
		}
		return puz;
	}

	public String getTileString(int x, int y) {
		int temp = getTile(x,y);
		if(temp == 0)
			return "";
		else return String.valueOf(temp);
	}

	/** 아직 횟수가 남아 있다면 키패드를 연다	 */
	public void showKeypadOrError(int x, int y) {
		int tiles[] = getUsedTiles(x,y);
		if(tiles.length == 9){
			Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
			Dialog v = new Keypad(this, tiles, puzzleView);
			v.show();
		}
	}

	// value가 가능한가
	public boolean setTileIfValid(int x, int y, int value) {
		int tiles[] = getUsedTiles(x,y);
		if(value != 0){
			for(int tile : tiles){
				if(tile == value){
					return false;
				}
			}
		}
		setTile(x,y,value);
		calculateUsedTiles();
		return true;
	}
	
	private void setTile(int x, int y, int value) {
		puzzle[x+y*9] = value;
	}
	
	/** 이미 사용된 타일 중 주어진 위치에서 보이는 것의 캐시 값 반환하기 */
	public int[] getUsedTiles(int x, int y) {
		return used[x][y];
	}
	
}
