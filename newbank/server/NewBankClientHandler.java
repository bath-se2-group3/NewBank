package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class NewBankClientHandler extends Thread{

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;


	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	public void run() {
		// keep getting requests from the client and processing them
		try {
			out.println("Welcome to NewBank");
			out.println("Are you an existing customer?");
			out.println("Yes or No");
			String existingCustomer = in.readLine().toLowerCase(Locale.ROOT);
			if(existingCustomer.equals("yes")){
				existingCustomer();
			}else{
				newCustomer();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	private void existingCustomer() throws IOException {
		// ask for user name
		out.println("Enter Username");
		String userName = in.readLine();
		// ask for password
		out.println("Enter Password");
		String password = in.readLine();
		out.println("Checking Details...");
		// authenticate user and get customer ID token from bank for use in subsequent requests
		CustomerID customer = bank.checkLogInDetails(userName, password);
		// if the user is authenticated then get requests from the user and process them
		if(customer != null) {
			out.println("Log In Successful. What do you want to do?\n"
					+ "\n"
					+ "Type HELP to list available commands"
					+ "\n");
			while(true) {
				String request = in.readLine();
				System.out.println("Request from " + customer.getKey());
				String response = bank.processRequest(customer, request);
				out.println(response);
			}
		}
		else {
			out.println("Log In Failed");
		}
	}

	private void newCustomer() throws IOException {
		Customer customer = null;
		out.println("We would like to set up your customer details.");
		while(true) {
			out.println("What do you want to do?\n"
					+ "\n"
					+ "Type HELP to list available commands"
					+ "\n");
				String request = in.readLine().toLowerCase(Locale.ROOT);
				if(request.equals("createcustomer")) {
					customer = getCustomerDetails(request);
				}
				String response = bank.processRequest(customer, request);
				out.println(response);
			}

	}

	private Customer getCustomerDetails(String request) throws IOException {
			out.println("Please submit customer details like so:");
			out.println("CREATECUSTOMER <FirstName> <Surname> <Username>");
			String[] response = in.readLine().split("\\s+");
			return new Customer.CustomerBuilder(response[1], response[2], response[3])
					.build();
	}

}