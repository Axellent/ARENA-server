package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Axel Sigl
 */
public class Connection extends Thread{
	private ServerSocket serverSocket;
	private Socket opponentSocket;
	private BufferedReader inputReader;
	private PrintWriter outputWriter;
	private boolean running = true;
	private String adress = "";
	private int port;

	/**
	 *
	 * @author Axel Sigl
	 */
	public Connection(int port){
		this.port = port;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @throws IOException 
	 */
	public void run() {
		String input = "";
		
		while(running) {
			
			try {
				serverSocket = new ServerSocket(port);
				opponentSocket = serverSocket.accept();
				setAdress();
				System.out.println("Connected to opponent: " + getAdress() + ": " + port);
				
				inputReader = new BufferedReader(new InputStreamReader(opponentSocket.getInputStream()));
				
				input = inputReader.readLine();
				System.out.println(input);
				
				serverSocket.close();
				
			} catch (IOException e) {
				
			}
		}
	}
	
	/**
	 * 
	 *
	 * @author Axel Sigl
	 * @param output
	 * @return
	 */
	public String sendOutput(String input){
		String output = "";
		try {
			outputWriter = new PrintWriter(opponentSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputWriter.println(input);
		
		/*try {
			while(!(output = inputReader.readLine()).equals(null)){
				return output;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return "";
	}
	
	/**
	 * 
	 *
	 * @author Axel Sigl
	 * @return
	 */
	public int getPort(){
		return port;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void setAdress(){
		adress = opponentSocket.getInetAddress().getHostName();
	}
	
	/**
	 *
	 * @author Axel Sigl
	 * @return
	 */
	public String getAdress(){
		return adress;
	}
}
