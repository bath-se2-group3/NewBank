// Package
package newbank.client;

// Import Statements
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/** 
 * Example Client
 *
 * @author University of Bath | Group 3
 */
public class ExampleClient extends Thread{
	
	/**
	 * Server
	 */
	private Socket server;

	/**
	 * Bank Server Print Writer
	 */
	private PrintWriter bankServerOut;	

	/**
	 * User Input via Buffered Reader
	 */
	private BufferedReader userInput;

	/**
	 * Bank Server Response Thread
	 */
	private Thread bankServerResponceThread;
	
	/**
	 * ExampleClient Constructor.
	 *
	 * @param ip   the ip
	 * @param port the port number
	 */
	public ExampleClient(String ip, int port) throws UnknownHostException, IOException {
		server = new Socket(ip,port);
		userInput = new BufferedReader(new InputStreamReader(System.in)); 
		bankServerOut = new PrintWriter(server.getOutputStream(), true); 
		
		bankServerResponceThread = new Thread() {
			private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream())); 
			public void run() {
				try {
					while(true) {
						String responce = bankServerIn.readLine();
						System.out.println(responce);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		};
		bankServerResponceThread.start();
	}
	
	/**
	 * Read users input
	 */
	public void run() {
		while(true) {
			try {
				while(true) {
					String command = userInput.readLine();
					bankServerOut.println(command);
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Start the Example Client on a specified port number.
	 *
	 * @param args arguments array
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new ExampleClient("localhost",14002).start();
	}
}
