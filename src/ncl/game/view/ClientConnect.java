package ncl.game.view;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import ncl.game.controller.BatlteShipBoardController;



public class ClientConnect {

	public static void main(String argv[]) throws Exception {

		String ip = "127.0.0.1";
		String sentence = null;
		String modifiedSentence;
		Socket clientSocket;
		Socket hitOrMiss;
		String miss = "miss";
		String hit = "hit";

		// from command line
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		// create the board
		BatlteShipBoardController boardController =new BatlteShipBoardController();
		// player is asked to add the ships to the board
		System.out.println(
				"Below you can input where you want to" + " place your battleships.\n Please enter them in integers"
						+ " starting with the row followed by columns\n (for example"
						+ " start with the head as 11 for row 1,\n column 1 and "
						+ " tail as 51 for row 5 and column 1)\nPlease input the data\n "
						+ "left to right and top to bottom" + "Type q when done.");
		
		while (true) {
			System.out.println("Please enter head location:");
			String line1 = inFromUser.readLine();
			System.out.println("Please enter tail location:");
			String line2 = inFromUser.readLine();
			if (!line1.equals("q") || !line2.equals("q")) {
				int head = Integer.parseInt(line1);
				int tail = Integer.parseInt(line2);
				// we call the testPos method to verify that we can place
				// the battleship at the inputed locations
				boardController.testPos(head, tail);
				if (boardController.getBoardbool() == true) {
					boardController.createBoat(head, tail);
					System.out.println("Creating boat at " + head + " and " + tail);
				} else
					System.out.println("Sorry, can't place the battleship using these locations.");
			} else
				break;
		}

		boardController.printBoard();

		////////////////////////////////////
		// game starts
		////////////////////////////////////
		clientSocket = new Socket(ip, 6789);
		// from server
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		// out to server the hit or miss message
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		while (true) {

			int hitRow;
			int hitCol;
			// the client sends a hit
			int fireToServer = 0;
			while (fireToServer <= 0 || fireToServer > 99) {
				System.out.println("Your Turn: \n");
				sentence = inFromUser.readLine();
				fireToServer = Integer.parseInt(sentence);
				System.out.println("Hit from client: (0=>99)" + fireToServer);
			}
			outToServer.writeBytes(sentence + "\n");
			outToServer.flush();

			// the client receives a hit from the server
			modifiedSentence = inFromServer.readLine();
			System.out.println("Result from server: " + modifiedSentence);

			modifiedSentence = inFromServer.readLine();
			System.out.println("Hit from server: " + modifiedSentence);
			int clientInt = Integer.parseInt(modifiedSentence);
			hitRow = Math.abs(clientInt / 10) - 1;
			hitCol = clientInt % 10 - 1;

			if (boardController.testHit(hitRow, hitCol)) {
				boardController.testLoss();
				if (boardController.testLoss() == false) {
					hit = miss = "You won!";
					System.out.println("Sorry, you lost!");
					return;
				}
				outToServer.writeBytes(hit + "\n");
				outToServer.flush();
			} else {
				outToServer.writeBytes(miss + "\n");
				outToServer.flush();
			}
		}

	}

}
