package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		 * 
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
	private Command command;
	private Socket clientSocket;
	private OutputThread outThread;
	private int clientID = -1;
	private boolean running = true;
	private BufferedReader inputReader;
	private PrintWriter outputWriter;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

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
	 * 
	 * @author Axel Sigl
	 */
	public void run() {
		String input;
		
		outThread.addOutput("Accepted Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
		
		try {
			inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			while(running) {
				input = inputReader.readLine();
				
				outThread.addOutput("Client : " + clientSocket.getInetAddress().getHostName() + " Requests command : " + input);
				
				if(input.charAt(0) == '-'){
					sendObject(command.parseObjectCommand(input));
				}
				
				else{
					sendOutput(command.parseUserCommand(input), input);
				}
					
			}
		} catch (Exception e) {
			if(command.getClientAccount() != null){
				command.getClientAccount().setAuthenticated(false);	
			}
			outThread.addOutput("Stopping client thread for client : " + clientSocket.getInetAddress().getHostName() + " : Connection interuppted");
		}
	}
	
	/**
	 * Splits the output into lines (with newline characters as separators) and sends the result to the client.
	 * @author Axel Sigl
	 * @param out
	 * @param output
	 */
	private void sendOutput(String output, String input){
		String[] outputLines;
		
		try {
			outputWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (output.equals("quit")) {
			command.getClientAccount().setAuthenticated(false);
			outThread.addOutput("Stopping client thread for client : " + clientSocket.getInetAddress().getHostName());
			outputWriter.println("Stopping client thread");
			running = false;
		}
		else{
			
			outThread.addOutput("Sending output for command : " + input + " to client : " + clientSocket.getInetAddress().getHostName());
			
			outputLines = output.split("\\r");
			
			for(int i = 0; i < outputLines.length; i++){
				outputWriter.println(outputLines[i]);
			}
		}
		outputWriter.flush();
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param outputObject
	 */
	private void sendObject(Object outputObject){

		if(outputObject == null){
			outThread.addOutput("Failed sending object to client :" + clientSocket.getInetAddress().getHostName() + " : Object does not exist");
			outputWriter.println("Requested object does not exist");
		}
		
		else{
			try {
				objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				
				objectOutputStream.writeObject(outputObject);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}