package resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Stores level data extracted from a file
 */
public class LevelData {

	private static final char LINE_FEED = '\n';
	private static final char CARRIAGE_RETURN = '\r';
	private static final int NEWLINE_LENGTH_IN_CHARACTERS = 2;
	private static final String FILE_TYPE = ".map";
	private static final String FILE_LOCATION = "levels/lvl";
	
	private int width;
	private int height;
	private char[][] data;
	
	public LevelData(int levelNumber) {
		char[] fileData = readDataFromFile(levelNumber);
		this.width = calculateLevelWidth(fileData);
		this.height = calculateLevelHeight(fileData);
		this.data = new char[width][height];
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				data[i][height - j - 1] = fileData[i + ( j * (width + 2))];
			}
		}
	}
	
	private char[] readDataFromFile(int levelNumber) {
		FileHandle file = Gdx.files.internal(FILE_LOCATION + levelNumber + FILE_TYPE);
		return file.readString().toCharArray();
	}
	
	private int calculateLevelWidth(char[] fileData) {
		int count = 0;
		for (char character : fileData) {
			if (character == CARRIAGE_RETURN || character == LINE_FEED) {
				return count;
			}
			count++;
		}
		return count;
	}
	
	private int calculateLevelHeight(char[] fileData) {
		return fileData.length / (width + NEWLINE_LENGTH_IN_CHARACTERS);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public char dataAt(int x, int y) {
		return data[x][y];
	}
}
