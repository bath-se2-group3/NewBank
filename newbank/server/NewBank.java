package newbank.server;

import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		Customer bhagy = new Customer.CustomerBuilder("Sam", "Bhagy", "bhagy")
				.addAccounts(new Account("Main", 1000.0))
				.build();
		customers.put("bhagy", bhagy);

		Customer christina = new Customer.CustomerBuilder("Christina", "Marks", "Christina")
				.addAccounts(new Account("Savings", 1500.0))
				.build();
		customers.put("christina", christina);

		Customer john = new Customer.CustomerBuilder("John", "Tees", "John")
				.addAccounts(new Account("Checking", 250.0))
				.build();
		customers.put("john", john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if (customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {

		String command = request.split( "\\s+" )[0];
		if (customers.containsKey(customer.getKey())) {
			switch (command) {
				case "SHOWMYACCOUNTS":
					return showMyAccounts(customer);
				case "HELP":
					return showHelp();
				case "PAY":
					return payMoney(customer, request);
				case "MOVE":
					return moveMoney(customer, request);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}

	public synchronized String processRequest(Customer customer, String request) {
		System.out.println("processRequest");
			switch (request.toLowerCase(Locale.ROOT)) {
				case "createcustomer":
					return createCustomer(customer);
				case "help":
					return showNewCustomerHelp();
				default:
					return "FAIL";
			}
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String showHelp() {
		String help = "\nSHOWMYACCOUNTS\n"
		+ "├ Returns a list of all the customers accounts along with their current balance\n"
		+ "└ e.g. Main: 1000.0\n"

		+ "\n"

		+ "NEWACCOUNT <Name>\n"
		+ "├ Creates a new account\n"
		+ "└ e.g. NEWACCOUNT Savings\n"

		+ "\n"

		+ "MOVE <Amount> <From> <To>\n"
		+ "├ Moves money between a users existing accounts\n"
		+ "└ e.g. MOVE 100 Main Savings\n"

		+ "\n"

		+ "PAY <Person/Company> <Ammount>\n"
		+ "├ Pay another user from your account to their account\n"
		+ "└ e.g. PAY John 100\n";
		return help;

	}

	private String createCustomer(Customer customer){
		customers.put(customer.getUserName(), customer);
		if(customers.containsKey(customer.getUserName())){
			return "SUCCESS - customer account added";
		}else{
			return "FAILURE - customer account NOT added";
		}
	}


	private String showNewCustomerHelp() {
		String help = "\nCREATECUSTOMER\n"
				+ "├ Create a new customer account\n"
				+ "└ e.g. CreateCustomer: FirstName Surname Username\n";
		return help;

	}

	private String payMoney(CustomerID customer, String request) {

		String [] arguments = request.split( "\\s+" );

		if (arguments.length==5){
			String command = arguments[0];
			String person = arguments[1];
			String account= arguments[2];
			String code = arguments[3];
			String amount = arguments[4];
			double amountNumber = Double.parseDouble(amount);



			if (customers.containsKey(person)){
				if(amountNumber < Double.parseDouble(showMyAccounts(customer).split( "\\s+" )[1])){
					return amountNumber+ " have been transferred from "+ customer.getKey() + " to "+ person;
				}
				else{
					return "insufficient funds, Please retry.";
				}
			}
			else {
				return "Bad request. The requested person is not a customer of NewBank.";
			}
		}
		else {
			return "Bad request. Please enter your command in the following format: PAY <Person/Company> <Account> <Sort Code> <Amount> ";
		}
	}

	private String moveMoney(CustomerID customer, String request) {

		String [] arguments = request.split( "\\s+" );

		if (arguments.length==4){
			String command = arguments[0];
			String accountFrom = arguments[1];
			String accountTo = arguments[2];
			String amount = arguments[3];
			double amountNumber = Double.parseDouble(amount);



			if (customers.containsKey(accountFrom)){
				if(amountNumber <= Double.parseDouble(showMyAccounts(customer).split( "\\s+" )[1])){

					return amountNumber+ " has been transferred from '" + accountFrom + "' to '" + accountTo + "'.";
				}
				else{
					return "Invalid request. There are insufficient funds in '" + accountFrom + "'. Please retry.";
				}
			}
			else {
				return "Invalid request. You do not have a registered account with the name '" + accountFrom + "'. Please retry.";
			}
		}
		// Currently, input format is actually more like MOVE <Person> <Account To> <Amount>
		else {
			return "Invalid request. Please enter your command in the following format: MOVE <Account From> <Account To> <Amount>.";
		}
	}
}