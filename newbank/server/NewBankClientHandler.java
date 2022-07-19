package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			out.println("CREATECUSTOMER <FirstName> <Surname> <Username>git\n");
			out.println("Format for Username \n- 10 alphanumeric characters or less\n- No special characters\n");
			while(true){
				String[] response = in.readLine().split("\\s+");
				if(response.length == 4 && response[0].toLowerCase(Locale.ROOT).equals("createcustomer")){


					if( response[1].length() > 0 &&
						response[2].length() > 0 &&
						response[3].length() > 0
					){
						if(response[3].length() > 0){
							if(!isUsernameValid(response[3]) && !isUsernameAlreadyPresent(response[3])){
								out.printf("Sorry, that username is invalid please re-enter\n");
							} else if (isUsernameValid(response[3]) && isUsernameAlreadyPresent(response[3])){
								out.printf("Sorry this username is already in use\n");
							}else{
								return new Customer.CustomerBuilder(response[1], response[2], response[3])
										.build();
							}
						}
					}else{
						out.printf("Sorry, we cannot accept that input%n");
					}

				}else if(response[0].toLowerCase(Locale.ROOT).equals("exit")){
					return null;
				}else{
					out.printf("Sorry, we cannot accept that input%n");
				}

			}

	}

	/**
	 * Function to validate new customers usernames.
	 * @param username entered through user input
	 * @return Boolean representing validity of the username.
	 */
	private Boolean isUsernameValid(String username){
		String regex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(username);

		if(username.length() <= 10 && matcher.matches()){
			return true;
		}return false;
	}

	/**
	 * Function to check if a username is already present
	 * @param username entered through user input
	 * @return Boolean representing if a given username already exists.
	 */
	private Boolean isUsernameAlreadyPresent(String username){
		return bank.getCustomers().containsKey(username) ? true : false;
	}

}
