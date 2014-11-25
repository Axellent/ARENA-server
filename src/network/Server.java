package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import network.Server.OutputThread;

/**
 * 
 * @author Axel Sigl
 *
 */
public class Server {
	private InputThread inThread = new InputThread();
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
		ServerSocket serverSocket = new ServerSocket(12345);
		inThread.start();
		outThread.start();
		
		outThread.addOutput("Server started");
		
		int id = 0;
		while (true) {
			Socket clientSocket = serverSocket.accept();
			ClientThread clientThread = new ClientThread(clientSocket, id++, outThread);
			clientThread.start();
		}
	}
	
	public OutputThread getOutputThread(){
		return outThread;
	}

	/**
	 * 
	 * @author Axel Sigl
	 * @param msg
	 */
	public static void println(String msg) {
		System.out.println("> " + msg);
	}

	/**
	 * 
	 * @author Axel Sigl
	 * @param args
	 */
	public static void main(String[] args) {
		new Server();
	}

	/**
	 * 
	 * @author Axel Sigl
	 *
	 */
	class OutputThread extends Thread {
		private String[] outputQueue = new String[100];
		private int nOutputs = 0;
		private boolean paused = false;

		/**
		 * @author Axel Sigl
		 */
		public synchronized void run(){

			while (true) {
				if(paused) {
					try {
						wait();
						println("Output mode");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				addOutput("test output");
				println(outputQueue[0]);
				remOutput(0);

				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * 
		 * @author Axel Sigl
		 */
		public void pause() {
			paused = true;
		}

		/**
		 * 
		 * @author Axel Sigl
		 */
		public synchronized void unpause() {
			notify();
			paused = false;
		}
		
		/**
		 * 
		 * @author Axel Sigl
		 * @param msg
		 */
		public void addOutput(String msg){
			outputQueue[nOutputs++] = msg;
		}
		
		/**
		 * 
		 * @author Axel Sigl
		 * @param i
		 */
		private void remOutput(int i){
			for(i += 0; i < nOutputs; i++){
				outputQueue[i] = outputQueue[i + 1];
			}
			nOutputs--;
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 *
	 */
	private class InputThread extends Thread {
		Scanner in = new Scanner(System.in);
		String input = "";

		/**
		 * @author Axel Sigl
		 */
		public void run() {

			while(true) {
				in.nextLine();
				outThread.pause();

				println("Input mode");
				input = in.nextLine();
				Server.println(new Command().parseRootCommand(input));
				
				outThread.unpause();
			}
		}
	}
}

/**
 * 
 * @author Axel Sigl
 *
 */
class ClientThread extends Thread {
	Socket clientSocket;
	OutputThread outThread;
	int clientID = -1;
	boolean running = true;

	/**
	 * 
	 * @author Axel Sigl
	 * @param s
	 * @param i
	 * @param outThread
	 */
	ClientThread(Socket s, int i, OutputThread outThread) {
		clientSocket = s;
		clientID = i;
		this.outThread = outThread;
	}

	/**
	 * @author Axel Sigl
	 */
	public void run() {
		BufferedReader in;
		PrintWriter out;
		String input;
		
		outThread.addOutput("Accepted Client : ID - " + clientID + " : Address - "
						+ clientSocket.getInetAddress().getHostName());
		try {
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()));

			while(running) {
				input = in.readLine();
				outThread.addOutput("Client Says :" + input);

				if (input.equals("quit")) {
					outThread.addOutput("Stopping client thread for client : " + clientID);
					running = false;
				}
				else {
					out.println(input);
					out.flush();
				}
				
				Server.println(new Command().parseUserCommand(input));
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}