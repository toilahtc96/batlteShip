package ncl.game.controller;

import ncl.game.model.board;

public class BatlteShipBoardController {
	board board =new board();
	public void testPos(int a, int b){
		if(a<100&&a>0&&b<100&&b>0) {
		int row1 = Math.abs(a/10);
		int col1 = a%10;
		int row2 = Math.abs(b/10);
		int col2 = b%10;
			if(row1 == row2 || col1 == col2){
			board.boatBool = true;
			}
			else {
				board.boatBool = false;
			}
		}
		else {
			board.boatBool= false;
		}
		
	}
	//we create a boat
	public void createBoat(int a, int b){
		//we also need to test for the existence of other boats
		
		//create the head and tail of the ship
		int row1 = Math.abs(a/10);
		int col1 = a%10;
		int row2 = Math.abs(b/10);
		int col2 = b%10;
		//testing to see if they are on the same column or row
		//this way we know how to put the ship in the array
		if(row1 == row2){
			//if the boat is on the same row we have to fill the 
			//columns out
			for(int i = col1; i<=col2; i++){
				board.board[row1-1][i-1] = 1;
			}
		}
		else if(col1 == col2){
			for(int i = row1; i<=row2; i++){
				board.board[i-1][col1-1] = 1;
			}
		}
		else{
			board.boatBool = false;
		}
	}
	//printing the board
	public void printBoard(){
		int rowleght = board.board.length;
		int colleght = board.board[0].length;
		for(int i = 0; i<rowleght; i++){
			for(int c = 0; c<colleght; c++){
				System.out.print(board.board[i][c]+" ");
			}
			System.out.println();
		}
	}
	public boolean testHit(int row, int col){
		if(board.board[row][col] == 1){
			board.board[row][col] = 5;
			printBoard();
			return true;
		}
		else 
			return false;
	}
	//test for loss, when all the board is 0's again
	public boolean testLoss(){
		int count = 0;
		boolean bool1 = true;
		for(int i = 0; i<board.board.length; i++){
			for(int c = 0; c<board.board[i].length; c++){
				if(board.board[i][c]==0 || board.board[i][c]==5){
					count++;
				}				
			}
		}
		if(count == 100)
			bool1 = false;
		return bool1;
	}
	public boolean getBoardbool() {
		return this.board.boatBool;
	}
}
