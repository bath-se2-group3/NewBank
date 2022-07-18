package newbank.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		Customer bhagy = new Customer.CustomerBuilder("Sam", "Bhagy", "bhagy")
				.addAccounts(new Account.AccountBuilder("Main", 0.00, 1000.00).build())
				.addAccounts(new Account.AccountBuilder("Savings", 0.00, 1500.00).build())
				.build();
		customers.put("bhagy", bhagy);

		Customer christina = new Customer.CustomerBuilder("Christina", "Marks", "Christina")
				.addAccounts(new Account.AccountBuilder("Savings", 0.00, 1500.00).build())
				.build();
		customers.put("christina", christina);

		Customer john = new Customer.CustomerBuilder("John", "Tees", "John")
				.addAccounts(new Account.AccountBuilder("Checking", 0.00, 250.00).build())
				.addAccounts(new Account.AccountBuilder("Test", 0.00, 99.99).build())
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
			switch (command.toLowerCase(Locale.ROOT)) {
				case "showmyaccounts":
					return showMyAccounts(customer);
				case "help":
					return showHelp();
				case  "pay":
					return payMoney(customer, request);
				case  "move":
					return moveMoney(customer, request);
				case "createaccount":
					return createAccount(customer, request);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}

	public synchronized String processRequest(Customer customer, String request) {
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
		+ "├  Returns a list of all the customers accounts along with their current balance\n"
		+ "└ e.g. Main: 1000.0\n"

		+ "\n"

		+ "CREATEACCOUNT <Name>\n"
		+ "├ Creates a new account\n"
		+ "└ e.g. NEWACCOUNT Savings\n"

		+ "\n"

		+ "MOVE <Payer_Account_name> <Recipient_Account_name> <Amount>\n"
		+ "├ Moves money between a user's existing accounts\n"
		+ "└ e.g. MOVE Main Savings 100\n"

		+ "\n"

		+ "PAY <Payer_Account_name> <Person/Company> <Recipient_Account_name> <Sort_code> <Ammount>\n"
		+ "├ Pay another user from your account to their account\n"
		+ "└ e.g. PAY Checking Bhagy Main EC12345 1500\n";
		return help;

	}

	private String createCustomer(Customer customer){
		customers.put(customer.getUserName(), customer);
		if(customers.containsKey(customer.getUserName())){
			return "A customer account was created for "+customer.getUserName();
		}else{
			return "No customer account was created";
		}
	}

	private String showNewCustomerHelp() {
		String help = "\nCREATECUSTOMER\n"
				+ "â”œ Create a new customer account\n";
		return help;
	}

	public HashMap<String, Customer> getCustomers() {
		return customers;
	}

	private String payMoney(CustomerID customer, String request) {

		String [] arguments = request.split( "\\s+" );

		if (arguments.length==6 && arguments[5].matches("[0-9]+")){
			String command = arguments[0];
			String accFrom = arguments[1];
			String recipient = arguments[2];
			String accTo = arguments[3];
			String code = arguments[4];
			String amount = arguments[5];

			double amountNumber = Double.parseDouble(amount);

			if (customers.get(recipient) != null ){
				Account accountFrom = customers.get(customer.getKey()).getAccount(accFrom);
				Account accountTo = customers.get(recipient).getAccount(accTo);
				if (customers.containsKey(recipient)){
					if (accountFrom!=null){
						if(amountNumber <= accountFrom.getAccountBalance()){
							if(accountTo!= null){
								accountTo.addToBalance(amountNumber);
								accountFrom.deductFromBalance(amountNumber);
								return amountNumber+ " has been transferred from "+ customer.getKey() + " to "+ recipient;
							}
							else {
								return "Recipient's account doesn't exist, please retry.";
							}
						}
						else{
							return "Insufficient funds, please retry.";
						}
					}
					else{
						return "Payer's account doesn't exist, please retry.";
					}
				}
				else {
					return "Bad request. The requested person is not a customer of NewBank.";
				}
			}
			else{
				return "Bad request. The requested person is not a customer of NewBank.";
			}
		}
		else {
			return "Bad request. Please enter your command in the following format: PAY <Payer_Account_name> <Person/Company> <Recipient_Account_name> <Sort_code> <Amount> ";
		}
	}

	private String moveMoney(CustomerID customer, String request) {

		String [] arguments = request.split( "\\s+" );

		if (arguments.length==4 && arguments[3].matches("[0-9]+")){
			String command = arguments[0];
			String accFrom = arguments[1];
			String accTo = arguments[2];
			String amount = arguments[3];

			double amountNumber = Double.parseDouble(amount);

				Account accountFrom = customers.get(customer.getKey()).getAccount(accFrom);
				Account accountTo = customers.get(customer.getKey()).getAccount(accTo);
					if (accountFrom!=null){
						if(amountNumber <= accountFrom.getAccountBalance()){
							if(accountTo!= null){
								accountTo.addToBalance(amountNumber);
								accountFrom.deductFromBalance(amountNumber);
								return amountNumber+ " have been transferred from "+ accFrom + " to "+ accTo;
							}
							else {
								return accTo + " doesn't exist, please retry.";
							}
						}
						else{
							return "There are insufficient funds in " + accFrom + ". Please retry.";
						}
					}
					else{
						return accFrom + " doesn't exist, please retry.";
					}
		}
		else {
			return "Bad request. Please enter your command in the following format: MOVE <Payer_Account_name> <Recipient_Account_name> <Amount> ";
		}
	}
	
	/**
	 * Takes a request, and creates a new account for a 
	 * specified customer.
	 *
	 * @param customer the customer creating the new account
	 * @param request  the command and arguments passed in through the command line
	 * @return         the status of the transfer as a string
	 */
	private String createAccount (CustomerID customer, String request) {
		
		// Split the String into arguments
		String [] arguments = request.split( "\\s+" );

		// Save the arguments as variables
		String command = arguments[0];
		String accountName = arguments[1];
		String strBalance = arguments[2];

		if (arguments.length != 3) {
			return "Incorrect Number of Arguments! Please enter your command in the following format: CREATEACCOUNT <Account_Name> <Starting_Balance> ";
		}

		if (arguments[3].matches("[0-9]+")) {

			// Convert the string balance to a double
			double balance = Double.parseDouble(strBalance);

			// Add the new account to the customer's list of accounts
			boolean result = customer.createAccount(accountName, balance);

			if (result) {
				return "Success!";
			} else {
				return "Failure!";
			}
		}
	}

}
