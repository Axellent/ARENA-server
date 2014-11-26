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
		
		outThread.addOutput("ARENA-server started. press return to switch between input and output modes. type \"help\" while in input mode for instuctions\r\r"
							+ "> Output mode");
		
		int id = 0;
		while (true) {
			Socket clientSocket = serverSocket.accept();
			ClientThread clientThread = new ClientThread(clientSocket, id++, outThread);
			clientThread.start();
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
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
				
				if(nOutputs > 0){
					println(outputQueue[0]);
					remOutput(0);
				}

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
	Command command;
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
		command = new Command();
	}

	/**
	 * @author Axel Sigl
	 */
	public void run() {
		BufferedReader in;
		PrintWriter out;
		String input;
		String output;
		
		outThread.addOutput("Accepted Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
		
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
<<<<<<< HEAD
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
=======
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
>>>>>>> 8d2b41b3d87aace8a432ea3b19cbec3290ba2852

			while(running) {
				input = in.readLine();
				outThread.addOutput("Client : " + clientSocket.getInetAddress().getHostName() + " Requests command : " + input);
				
				output = command.parseUserCommand(input);

				if (output.equals("quit")) {
					outThread.addOutput("Stopping client thread for client : " + clientSocket.getInetAddress().getHostName());
					out.println("Stopping client thread");
					out.flush();
					running = false;
				}
				else{
					outThread.addOutput(output);
					out.println(output);
					out.flush();
				}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}