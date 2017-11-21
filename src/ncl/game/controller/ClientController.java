package ncl.game.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientController {
	String ip = "127.0.0.1";
	String sentence = null;
	String modifiedSentence;
	Socket clientSocket;
	Socket hitOrMiss;
	String miss = "miss";
	String hit = "hit";
	
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	BufferedReader inFromServer;
	DataOutputStream outToServer; 

	// create the board
	BatlteShipBoardController boardController =new BatlteShipBoardController();
	public void createBroadForClient() throws IOException  {
		System.out.println(
				"Below you can input where you want to" + " place your battleships.\n Please enter them in integers"
						+ " starting with the row followed by columns\n (for example"
						+ " start with the head as 11 for row 1,\n column 1 and "
						+ " tail as 51 for row 5 and column 1)\nPlease input the data\n "
						+ "left to right and top to bottom" + "Type q when done.");
		
		while (true) {
			System.out.println("Please enter head location:");
			String line1 = null;
				line1 = inFromUser.readLine();
			System.out.println("Please enter tail location:");
			String line2 = null;
				line2 = inFromUser.readLine();
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

	}
	public Socket newSocket(String ip) throws UnknownHostException, IOException  {
			return new Socket(ip,1996);
	}
	public void createListenerFromServer(Socket s) throws IOException  {

			inFromServer = new BufferedReader(new InputStreamReader(this.newSocket(ip).getInputStream()));
			outToServer = new DataOutputStream(this.newSocket(ip).getOutputStream());
	}
	public void fireToServer() throws IOException  {
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
				}
			 else {
					outToServer.writeBytes(miss + "\n");
					outToServer.flush();
			}
		}

	}
	public void printboard() {
		boardController.printBoard();
	}
	public void createClient(String ip) throws IOException {
		
		ClientController clientController = new ClientController();
		clientController.createBroadForClient();
		clientController.printboard();
		Socket clientSocket = clientController.newSocket(ip);
		clientController.createListenerFromServer(clientSocket);
		clientController.fireToServer();
	}
}
