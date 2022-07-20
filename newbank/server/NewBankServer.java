// Package
package newbank.server;

// Import Statements
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** 
 * The NewBank Server
 *
 * @author University of Bath | Group 3
 */
public class NewBankServer extends Thread{
	
	/**
	 * Serverside Server
	 */
	private ServerSocket server;
	
	/**
	 * NewBankServer Constructor.
	 *
	 * @param port         the port number
	 * @throws IOException throws when there is an input or output error
	 */
	public NewBankServer(int port) throws IOException {
		server = new ServerSocket(port);
	}
	
	/**
	 * Starts up a new client handler thread to receive incoming
	 * connections and process requests.
	 */
	public void run() {
		System.out.println("New Bank Server listening on " + server.getLocalPort());
		try {
			while(true) {
				Socket s = server.accept();
				NewBankClientHandler clientHandler = new NewBankClientHandler(s);
				clientHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/**
	 * Start the NewBank Server thread on a specified port number.
	 *
	 * @param args         arguments array
	 * @throws IOException throws when there is an input or output error
	 */
	public static void main(String[] args) throws IOException {
		new NewBankServer(14002).start();
	}
}
