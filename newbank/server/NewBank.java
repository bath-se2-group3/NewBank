package newbank.server;

import java.util.HashMap;
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
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
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
				case  "PAY": 
					return payMoney(customer, request);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
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

		+ "PAY <Person/Company> <Account_name> <Sotrt_code> <Ammount>\n"
		+ "├ Pay another user from your account to their account\n"
		+ "└ e.g. PAY Bhagy Main EC12345 1500\n";
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
				if(amountNumber <= Double.parseDouble(showMyAccounts(customer).split( "\\s+" )[1])){
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

}
	