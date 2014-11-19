package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

/**
 * 
 * @author Axel Sigl
 *
 */
public class Server {
	private OutputThread outThread = new OutputThread();
	private String mode = "output";

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
		println("Server started");

		new InputThread().start();

		int id = 0;
		while (true) {
			Socket clientSocket = serverSocket.accept();
			ClientThread clientThread = new ClientThread(clientSocket, id++);
			clientThread.start();
		}
	}

	/**
	 * 
	 * @author Axel Sigl
	 * @param msg
	 */
	public static void println(String msg) {
		System.out.println("> " + msg);
	}

	public static void main(String[] args) {
		new Server();
	}

	class InputThread extends Thread {
		String cmd = "";
		Scanner in = new Scanner(System.in);

		public void run() {

			outThread.start();

			while(true) {
				in.nextLine();
				outThread.pause();

				Server.println("Input mode");
				cmd = in.nextLine();

				if(cmd.equals("quit")){
					Server.println("Server shutting down");
					System.exit(0);
				}
				
				outThread.unpause();
			}
		}
	}

	/**
	 * 
	 * @author Axel Sigl
	 *
	 */
	class OutputThread extends Thread {
		private boolean paused = false;

		public synchronized void run() {

			while (true) {

				if (paused) {
					try {
						wait();
						Server.println("Output mode");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

				Server.println("output");

				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void pause() {
			paused = true;
		}

		public synchronized void unpause() {
			notify();
			paused = false;
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