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
				.addAccounts(new Account("Main", 1000.0))
				.build();
		customers.put("bhagy", bhagy);

		Customer christina = new Customer.CustomerBuilder("Christina", "Marks", "Christina")
				.addAccounts(new Account("Savings", 1500.0))
				.build();
		customers.put("christina", christina);

		Customer john = new Customer.CustomerBuilder("John", "Tees", "John")
				.addAccounts(new Account("Checking", 250.00))
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
			switch (request.toLowerCase(Locale.ROOT)) {
				case "showmyaccounts":
					return showMyAccounts(customer);
				case "help":
					return showHelp();
				case  "pay":
					return payMoney(customer, request);
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
