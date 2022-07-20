// Package
package newbank.server;

// Import Statements
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

/** 
 * The NewBank Handler
 *
 * @author University of Bath | Group 3
 */
public class NewBankClientHandler extends Thread{

	/**
	 * New Bank Object
	 */
	private NewBank bank;

	/**
	 * Input
	 */
	private BufferedReader in;

	/**
	 * Output
	 */
	private PrintWriter out;

	/**
	 * NewBank Client Handler
	 *
	 * @param s the client server
	 * @throws IOException throws when there is an input or output error
	 */
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	/**
	 * Keep getting requests from the client and processing them.
	 */
	public void run() {
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

	/**
	 * If the user is an existing customer, allow the customer to login
	 * and carry out a range of bank commands.
	 *
	 * @throws IOException throws when there is an input or output error
	 */
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

	/**
	 * If the user is a new customer, create a new customer.
	 *
	 * @throws IOException throws when there is an input or output error
	 */
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

	/**
	 * Create a new customer
	 *
	 * @param request      the request from the customer
	 * @return             the customer
	 * @throws IOException throws when there is an input or output error
	 */
	private Customer getCustomerDetails(String request) throws IOException {
			out.println("Please submit customer details like so:");
			out.println("CREATECUSTOMER <FirstName> <Surname> <Username>git\n");
			out.println("Format for Username \n- 10 alphanumeric characters or less\n- No special characters\n");
			while(true){
				String[] response = in.readLine().split("\\s+");
				if(response.length == 4 && response[0].toLowerCase(Locale.ROOT).equals("createcustomer")){
					String enteredFirstname = response[1];
					String enteredSurname = response[2];
					String enteredUsername = response[3];

					if( enteredFirstname.length() > 0 &&
						enteredSurname.length() > 0 &&
						enteredUsername.length() > 0
					){
						if(enteredUsername.length() > 0){
							if(!isUsernameValid(enteredUsername) && !isUsernameAlreadyPresent(enteredUsername)){
								out.printf("Sorry, that username is invalid please re-enter\n");
							} else if (isUsernameValid(response[3]) && isUsernameAlreadyPresent(enteredUsername)){
								out.printf("Sorry this username is already in use\n");
							}else{
								return new Customer.CustomerBuilder(enteredFirstname, enteredSurname, enteredUsername)
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
