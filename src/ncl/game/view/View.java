package ncl.game.view;

import java.io.IOException;
import java.net.Socket;

import ncl.game.controller.ClientController;
import ncl.game.controller.ServerController;

public class View {
	public static void main(String[] args) {
		String ip = "127.0.0.1";
		ClientController clientController = new ClientController();
		try {
			clientController.createBroadForClient();
			Socket s=clientController.newSocket(ip);
			clientController.createListenerFromServer(s);
			clientController.fireToServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
