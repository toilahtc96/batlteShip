package ncl.game.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
	String clientSentence;
	String serverSentence;
	ServerSocket welcomeSocket;
	Socket serverSocket;
	String miss = "miss";
	String hit = "hit";
	String won = "won";
	boolean turn = true;
	BufferedReader inFromClient=null;
	DataOutputStream outToClient1;

	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	// create the board

	BatlteShipBoardController boardController = new BatlteShipBoardController();

	public void createBoardForServer() throws IOException {
		System.out.println(
				"Below you can input where you want to" + " place your battleships.\n Please enter them in integers"
						+ " starting with the row followed by columns\n (for example"
						+ " start with the head as 11 for row 1,\n column 1 and "
						+ " tail as 51 for row 5 and column 1)\n. Please input the data\n "
						+ "left to right and top to bottom\n" + "Type q when done.");
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

	public ServerSocket newSocket() throws IOException {
			return new ServerSocket(1996);
	}

	public boolean createListenerFromClient() throws IOException {
			welcomeSocket = new ServerSocket(5698);
			serverSocket = welcomeSocket.accept();
			inFromClient = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			outToClient1 = new DataOutputStream(serverSocket.getOutputStream());
			if(inFromClient!=null) {
				return true;
			}
			else return false;
	}

	public void fireToClient() throws IOException {
		while (true) {
			int hitRow;
			int hitCol;

			// the server receives a hit from the client
			// and replies with a hit or miss
				clientSentence = inFromClient.readLine();
			System.out.println("Hit from client: " + clientSentence);
			int clientInt = Integer.parseInt(clientSentence);
			hitRow = Math.abs(clientInt / 10) - 1;
			hitCol = clientInt % 10 - 1;

			if (boardController.testHit(hitRow, hitCol)) {
				boardController.testLoss();
				if (boardController.testLoss() == false) {
					hit = miss = "You won!";
					System.out.println("Sorry, you lost!");
					return;
				}
					outToClient1.writeBytes(hit + "\n");
					outToClient1.flush();
			} else {
					outToClient1.writeBytes(miss + "\n");
					outToClient1.flush();
			}

			// the server sends a hit
			int fireToClient = 0;
			String newS = "";
			while (fireToClient <= 0 || fireToClient > 99) {
				System.out.println("Your Turn: \n");
					newS = inFromUser.readLine();
				fireToClient = Integer.parseInt(newS);
				System.out.println("Hit from client: (0=>99)" + fireToClient);
			}
				outToClient1.writeBytes(newS + "\n");
				outToClient1.flush();
				clientSentence = inFromClient.readLine();
			System.out.println("Result from client: " + clientSentence);

		}
	}

	public void createServer() throws IOException {
		ServerController serverController = new ServerController();
		serverController.createBoardForServer();
		serverController.boardController.printBoard();
		serverController.newSocket();
		serverController.createListenerFromClient();
		serverController.fireToClient();
	}

}
