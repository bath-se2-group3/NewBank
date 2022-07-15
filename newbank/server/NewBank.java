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
		Customer bhagy = new Customer.CustomerBuilder("Sam", "Bhagy", "bhagy").addAccounts(new Account.AccountBuilder("Main", 0.00, 1000.00).build())
				.build();
		customers.put("bhagy", bhagy);

		Customer christina = new Customer.CustomerBuilder("Christina", "Marks", "Christina").addAccounts(new Account.AccountBuilder("Savings", 0.00, 1500.00).build())
				.build();
		customers.put("christina", christina);

		Customer john = new Customer.CustomerBuilder("John", "Tees", "John").addAccounts(new Account.AccountBuilder("Checking", 0.00, 250.00).build())
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
			switch (command) {
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
				case "adddetails":
					return addDetails(customer, request);
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

	private String addDetails(CustomerID customer, String request) {

		String flag = null;
		String mailAddress = null;
		String parsedRequest = null;

		String [] arguments = request.split( "\\s+" );

		if (arguments.length==2){
			flag = arguments[0];
			mailAddress = arguments[1];
		} else {
			return "Bad request";
		}

		//TODO: Verify that mailAddress is actually a mail address

		parsedRequest = mailAddress;

		// Use setter to update customer mail address field
		customers.get(customer.getKey()).setMail(parsedRequest);

		return "Updated e-mail address to: " + (customers.get(customer.getKey())).getMail();
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

		+ "MOVE <Account From> <Account To> <Amount>\n"
		+ "├ Moves money between a user's existing accounts\n"
		+ "└ e.g. PAY Bhagy Main EC12345 1500\n"

		+ "\n"

		+ "PAY <Person/Company> <Account_name> <Sort_code> <Amount>\n"
		+ "├ Pay another user from your account to their account\n"
		+ "└ e.g. PAY Bhagy Main EC12345 1500\n"
		
		+ "\n"

		+ "ADDDETAILS <Mail_address>\n"
		+ "├ Add your mail address\n"
		+ "└ e.g. ADDDETAILS foo@bar.baz\n";
	
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
				+ "├ Create a new customer account\n";
		return help;
	}

	public HashMap<String, Customer> getCustomers() {
		return customers;
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
				if(amountNumber <= customers.get(customer.getKey()).getAccountByIndex(0).getAccountBalance()){
					(customers.get(person)).getAccountByIndex(0).addToBalance(amountNumber); //
					(customers.get(customer.getKey())).getAccountByIndex(0).deductFromBalance(amountNumber);
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
			String accountTo= arguments[2];
			String amount = arguments[3];
			double amountNumber = Double.parseDouble(amount);


				if(amountNumber <= 0){
					return "Please enter a positive value.";
				}
				else if(amountNumber <= customers.get(customer.getKey()).getAccountByIndex(0).getAccountBalance()){
					(customers.get(customer.getKey())).getAccountByIndex(1).addToBalance(amountNumber); //
					(customers.get(customer.getKey())).getAccountByIndex(0).deductFromBalance(amountNumber);
					return String.format("%.2f",amountNumber)+ " has been transferred from "+ accountFrom + " to "+ accountTo;
				}
				else{
					return "There are insufficient funds in" + accountFrom + ". Please try again.";
				}
		}
		else {
			return "Bad request. Please enter your command in the following format: MOVE <Account From> <Account To> <Amount> ";
		}
	}


}
