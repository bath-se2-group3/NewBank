// Package
package newbank.server;

// Import Statements
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/** 
 * Represents the New Bank bank
 *
 * @author University of Bath | Group 3
 */
public class NewBank {

	/**
	 * The bank object
	 */
	private static final NewBank bank = new NewBank();

	/**
	 * A list of customers belonging to the bank
	 */
	private HashMap<String, Customer> customers;

	/**
	 * Constructor for a NewBank Object
	 */
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	/**
	 * Add test customers to the NewBank object.
	 */
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

	/**
	 * Get the NewBank object.
	 *
	 * @return the NewBank Object
	 */
	public static NewBank getBank() {
		return bank;
	}

	/**
	 * Check if a login has been successful.
	 *
	 * @param userName the username of the customer
	 * @param password the password of the customer
	 * @return         the status of login
	 */
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if (customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	/**
	 * Process the requests for NewBank customers.
	 *
	 * @param customer the customer
	 * @param request  the command and arguments passed in through the command line
	 * @return         the status of processing a request
	 */
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
				// case "createaccount":
					// return createAccount(customer);
				default:
					return "FAIL";
			}
		}

		return "FAIL";
	}

	/**
	 * Process the requests for new customers.
	 *
	 * @param customer the customer
	 * @param request  the command and arguments passed in through the command line
	 * @return         the status of processing a request
	 */
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

	/**
	 * Show accounts belonging to a specfied customer.
	 *
	 * @param customer the customer
	 * @return         the accounts belonging to a customer as a string
	 */
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	/**
	 * Display help for customers.
	 *
	 * @return the help text as a string
	 */
	private String showHelp() {
		String help = "\nSHOWMYACCOUNTS\n"
		+ "├  Returns a list of all the customers accounts along with their current balance\n"
		+ "└ e.g. Main: 1000.0\n"

		+ "\n"

		+ "NEWACCOUNT <Name>\n"
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

	/**
	 * Creates a new customer.
	 *
	 * @param customer the new customer
	 * @return         the status of the creation of a new customer as a string
	 */
	private String createCustomer(Customer customer){
		customers.put(customer.getUserName(), customer);
		if(customers.containsKey(customer.getUserName())){
			return "A customer account was created for "+customer.getUserName();
		}else{
			return "No customer account was created";
		}
	}

	/**
	 * Display help for new customers.
	 *
	 * @return the help text as a string
	 */
	private String showNewCustomerHelp() {
		String help = "\nCREATECUSTOMER\n"
				+ "â”œ Create a new customer account\n";
		return help;
	}

	/**
	 * Return the customers of New Bank.
	 *
	 * @return a hash map of customers with new bank accounts
	 */
	public HashMap<String, Customer> getCustomers() {
		return customers;
	}

	/**
	 * Takes a request, and pays a specified amount of money to
	 * another customer of new bank.
	 *
	 * @param customer the customer paying the money
	 * @param request  the command and arguments passed in through the command line
	 * @return         the status of the payment as a string
	 */
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

	/**
	 * Takes a request, and moves a specified amount of money from
	 * one account to another account.
	 *
	 * @param customer the customer moving the money
	 * @param request  the command and arguments passed in through the command line
	 * @return         the status of the transfer as a string
	 */
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
}
