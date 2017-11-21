package ncl.game.model;

public class board {
	public int[][] board;
	//this will hold info if the boat can be created
	public boolean boatBool;
	public boolean loss = true;
	public String  str;
	public board(){
		board = new int[10][10];
	}
	
}
