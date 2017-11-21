package ncl.game.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

import ncl.game.controller.ServerController;

public class View2 {

	public static void main(String[] args) {
		ServerController svController = new ServerController();
		try {
			svController.createBoardForServer();
			ServerSocket s =  svController.newSocket();
			boolean checkinClient =svController.createListenerFromClient();
			while(!checkinClient) {
				svController.createListenerFromClient();
				svController.fireToClient();}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
