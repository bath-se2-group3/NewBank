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

		+ "NEWACCOUNT <Name>\n"
		+ "├ Creates a new account\n"
		+ "└ e.g. NEWACCOUNT Savings\n"

		+ "\n"

		+ "MOVE <Amount> <From> <To>\n"
		+ "├ Moves money between a user's existing accounts\n"
		+ "└ e.g. MOVE 100 Main Savings\n"

		+ "\n"

		+ "PAY <Payer_Account_name> <Person/Company> <Receipient_Account_name> <Sotrt_code> <Ammount>\n"
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
			String payerAcc = arguments[1];
			String receipient = arguments[2];
			String receipientAcc = arguments[3];
			String code = arguments[4];
			String amount = arguments[5];

			double amountNumber = Double.parseDouble(amount);

			if (customers.get(receipient) != null ){
				Account payerAccount = customers.get(customer.getKey()).getAccount(payerAcc);
				Account receipientAccount = customers.get(receipient).getAccount(receipientAcc);
				if (customers.containsKey(receipient)){
					if (payerAccount!=null){
						if(amountNumber <= payerAccount.getBalance()){
							if(receipientAccount!= null){
								receipientAccount.addToBalance(amountNumber);
								payerAccount.deductFromBalance(amountNumber);
								return amountNumber+ " have been transferred from "+ customer.getKey() + " to "+ receipient;
							}
							else {
								return "Receipient's account doesn't exist, please retry.";
							}
						}
						else{
							return "Insufficient funds, Please retry.";
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
			return "Bad request. Please enter your command in the following format: PAY <Payer_Account_name> <Person/Company> <Receipient_Account_name> <Sotrt_code> <Ammount> ";
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
