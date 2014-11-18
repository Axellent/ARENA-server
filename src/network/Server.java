package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Axel Sigl
 *
 */
public class Server{

	/**
	 * 
	 * @author Axel Sigl
	 */
	public Server() {
		try {
			runServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author Axel Sigl
	 * @throws Exception
	 */
	private void runServer() throws Exception {
		ServerSocket serverSocket = new ServerSocket(0);
		System.out.println("Server started");
		
		new IOThread().start();
		
		int id = 0;
		while(true) {
			Socket clientSocket = serverSocket.accept();
			ClientThread clientThread = new ClientThread(clientSocket, id++);
			clientThread.start();
		}
	}

	public static void main(String[] args) {
		new Server();
	}
}

/**
 * 
 * @author Axel Sigl
 *
 */
class IOThread extends Thread{
	Scanner in = new Scanner(System.in);
	String input = "";
	
	public void run(){
		System.out.println("Started IO thread");
		
		while(true){
			input = in.nextLine();
			System.out.println("Parsing command: " + input);
			
			if(input.equals("q")){
				System.out.println("Server shutting down");
				System.exit(0);
			}
		}
	}
}

class ClientThread extends Thread {
	Socket clientSocket;
	int clientID = -1;
	boolean running = true;

	ClientThread(Socket s, int i) {
		clientSocket = s;
		clientID = i;
	}

	public void run() {
		System.out
				.println("Accepted Client : ID - " + clientID + " : Address - "
						+ clientSocket.getInetAddress().getHostName());
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()));

			while (running) {
				String clientCommand = in.readLine();
				System.out.println("Client Says :" + clientCommand);
				if (clientCommand.equalsIgnoreCase("quit")) {
					System.out.print("Stopping client thread for client : "
							+ clientID);
				} else {
					out.println(clientCommand);
					out.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}