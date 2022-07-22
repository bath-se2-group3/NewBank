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
			while(true) {
				out.println("Welcome to NewBank");
				out.println("Are you an existing customer?");
				out.println("Yes or No");
				String existingCustomer = in.readLine().toLowerCase(Locale.ROOT);
				if (existingCustomer.equals("yes")) {
					existingCustomer();
				} else if (existingCustomer.equals("no")) {
					newCustomer();
				} else {
					out.printf("Sorry, that input %s is not recognised%n", existingCustomer);
				}
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
		String userName = in.readLine().toLowerCase(Locale.ROOT);
		// ask for password
		out.println("Enter Password");
		String password = in.readLine().toLowerCase(Locale.ROOT);
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
				String request = in.readLine().toLowerCase(Locale.ROOT);
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
			out.println("What would you like to do?\n"
					+ "\n"
					+ "Type HELP to list available commands"
					+ "\n");
				String request = in.readLine().toLowerCase(Locale.ROOT);
				if(request.equals("createcustomer")) {
					customer = createCustomerRecord();
				}else if(request.equals("restart") || request.equals("login")){
					run();
				}
				String response = bank.processRequest(customer, request);
				out.println(response);
			}

	}

	private Customer createCustomerRecord() throws IOException {
			out.println("Please submit customer details like so:");
			out.println("CREATECUSTOMER <FirstName> <Surname> <Username> <Password>");

			while(true){
				String[] response = in.readLine().split("\\s+");
				String command = response[0];
				String firstName = response[1];
				String surname = response[2];
				String username = response[3];
				String password = response[4];

				if(response.length == 5 && command.toLowerCase(Locale.ROOT).equals("createcustomer")){
					if( firstName.length() > 0 &&
						surname.length() > 0 &&
						username.length() > 0 &&
						password.length() > 0
					){
						if(TestData.getCustomers().containsKey(username)){
							out.printf("Sorry, that username already exists%n");
						}else{
							return new Customer.CustomerBuilder(firstName, surname, username, password)
									.build();
						}
					}else{
						out.printf("Sorry, we cannot accept that input%n");
					}

				}else if(command.toLowerCase(Locale.ROOT).equals("exit")){
					return null;
				}else{
					out.printf("Sorry, we cannot accept that input%n");
				}

			}

	}

}
