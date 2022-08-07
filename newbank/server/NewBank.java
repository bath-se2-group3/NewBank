// Package
package newbank.server;

// Import Statements
import newbank.server.data.PasswordDAO;
import newbank.server.data.TestData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

	public NewBank() {
		TestData.addTestData();
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
		PasswordDAO passwordDao = new PasswordDAO();

		if (getCustomers().containsKey(userName) && validatePassword(userName, password, passwordDao.getPasswords())) {
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
		if (getCustomers().containsKey(customer.getKey())) {
			switch (command.toLowerCase(Locale.ROOT)) {
				case "showmyaccounts":
					return showMyAccounts(customer);
				case "help":
					return showHelp();
				case  "pay":
					return payMoney(customer, request);
				case  "move":
					return moveMoney(customer, request);
				case "changeusername":
					return updateUserName(customer, request);
				case "createaccount":
					return createAccount(customer, request);
				case "closeaccount":
					return closeAccount(customer, request);
				case "addmycontactdetails":
					return addmycontactdetails(customer, request);
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
	public synchronized String processRequest(Customer customer, String request) throws IOException  {
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
   * Check whether a provided mail address is in a
   * valid form or not
   *
   * @param mailAddress the provided mail address
   * @return            whether it is a valid mail address or not
   */
	private boolean isMailAddress(String mailAddress) {
		return mailAddress.matches( ".*@.*\\.[a-zA-Z]{2,}");
	}

  /**
   * Check whether a provided phone number is in a
   * valid form or not
   *
   * @param phoneNumber the provided phone number
   * @return            whether it is a valid phone number or not
   */
	private boolean isPhoneNumber(String phoneNumber) {
		return phoneNumber.matches( ".*[0-9]");
	}

  /**
	 * Takes a request, and adds customer details to the customer.
	 *
	 * @param customer the customer the details are provided for
	 * @param request  the command and arguments passed in through the command line
	 * @return         the status of adding the customer details
	 */
	private String addmycontactdetails(CustomerID customer, String request) {

		String flag = null;
		String argument = null;
		String [] arguments = request.split( "\\s+" );

		HashMap<String, Customer> customers = getCustomers();

		if (arguments.length==2) {
			flag = arguments[0];
			argument = arguments[1];
			if (isMailAddress(argument)) {
				customers.get(customer.getKey()).setMail(argument);
				return "Updated e-mail address to: " + (customers.get(customer.getKey())).getMail();
			} else if (isPhoneNumber(argument)) {
				customers.get(customer.getKey()).setPhoneNumber(argument);
				return "Updated phone number to: " + (customers.get(customer.getKey())).getPhoneNumber();
			} else {
			return "Bad request. Please enter your command in the following format: ADDMYCONTACTDETAILS <E-Mail Address | Phone Number>";
			}
		} else {
			return "Bad request.";
		}

	}

  /**
	 * Show accounts belonging to a specfied customer.
	 *
	 * @param customer the customer
	 * @return         the accounts belonging to a customer as a string
	 */
	private String showMyAccounts(CustomerID customer) {
		return getCustomers().get(customer.getKey()).accountsToString();
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

		+ "CREATEACCOUNT <Account_Name> <Starting_Balance>\n"
		+ "├ Creates a new account\n"
		+ "└ e.g. CREATEACCOUNT Savings\n"

		+ "\n"

		+ "CLOSEACCOUNT <Account_Name>\n"
		+ "├ Closes an account\n"
		+ "└ e.g. CLOSEACCOUNT Savings\n"

		+ "\n"

		+ "CHANGEUSERNAME <Username>\n"
		+ "├ Changes the username\n"
		+ "└ e.g. CHANGEUSERNAME Lola\n"

		+ "\n"

		+ "MOVE <Payer_Account_name> <Recipient_Account_name> <Amount>\n"
		+ "├ Moves money between a user's existing accounts\n"
		+ "└ e.g. MOVE Main Savings 100\n"

		+ "\n"

		+ "PAY <Payer_Account_name> <Person/Company> <Recipient_Account_name> <Sort_code> <Ammount>\n"
		+ "├ Pay another user from your account to their account\n"
		+ "└ e.g. PAY Checking Bhagy Main EC12345 1500\n"

		+ "\n"

		+ "LOGOUT\n"
		+ "├ Log out of your account\n"
		+ "└ e.g. LOGOUT\n"

		+ "\n"

		+ "ADDMYCONTACTDETAILS <Mail_address | Phone_number>\n"
		+ "├ Add your e-mail address or phone number\n"
		+ "├ e.g. ADDMYCONTACTDETAILS foo@bar.baz\n"
		+ "├ or\n"
		+ "└ e.g. ADDMYCONTACTDETAILS 0123456789\n";

		return help;
	}


	private String updateUserName(CustomerID customerId, String request) {
		String key = customerId.getKey();
		String [] arguments = request.split( "\\s+" );
		String newUsername = arguments[1];
		HashMap<String, Customer> customers = getCustomers();

		if (customers.containsKey(key)) {
			Customer customer = customers.get(key);
			return "Username was updated to " + customer.setUserName(newUsername);
		} else {
			return "Customer could be located with key " + key + "; Username was not updated";
		}
	}

	/**
	 * Creates a new customer.
	 *
	 * @param customer the new customer
	 * @return         the status of the creation of a new customer as a string
	 */

	private String createCustomer(Customer customer) throws IOException {
		if(TestData.addCustomer(customer)){
			PasswordDAO passwordDao = new PasswordDAO();
			passwordDao.setPassword(customer);
		}

		if(getCustomers().containsKey(customer.getUserName())){
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
				+ "└ Create a new customer account\n"
				+ "\n"

				+ "RESTART\n"
				+ "├ Return to beginning\n"

				+ "\n"

				+ "LogIn\n"
				+ "├ Login as a customer\n";
		return help;
	}

	/**
	 * Return the customers of New Bank.
	 *
	 * @return a hash map of customers with new bank accounts
	 */
	public HashMap<String, Customer> getCustomers() {
		return TestData.getCustomers();
	}

	public static String capitalizeFirstLetter(String str) {
		if(str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
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

		if (arguments.length==6 && arguments[5].matches("^[0-9]*(\\.[0-9]{1,2})?$")){
			String command = arguments[0];
			String accFrom = arguments[1];
			String recipient = arguments[2];
			String accTo = arguments[3];
			String code = arguments[4];
			String amount = arguments[5];

			double amountNumber = Double.parseDouble(amount);
			HashMap<String, Customer> customers = getCustomers();

			if (customers.get(recipient) != null ){
				Account accountFrom = customers.get(customer.getKey()).getAccount(accFrom);
				Account accountTo = customers.get(recipient).getAccount(accTo);
				if (customers.containsKey(recipient)){
					if (accountFrom!=null){
						if(amountNumber <= accountFrom.getAccountBalance()){
							if(accountTo!= null){
								accountTo.addToBalance(amountNumber);
								accountFrom.deductFromBalance(amountNumber);
								return ("£" + String.format("%.2f", amountNumber) + " has been transferred from "+ customer.getKey() + " to "+ recipient);
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
			return "Bad request. Please enter your command in the following format: PAY <Payer_Account_name> <Person/Company> <Recipient_Account_name> <Sort_code> <Amount_to_two_decimal_places> ";
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

		if (arguments.length==4 && arguments[3].matches("^[0-9]*(\\.[0-9]{1,2})?$")){
			String command = arguments[0];
			String accFrom = arguments[1];
			String accTo = arguments[2];
			String amount = arguments[3];

			double amountNumber = Double.parseDouble(amount);
			HashMap<String, Customer> customers = getCustomers();

				Account accountFrom = customers.get(customer.getKey()).getAccount(accFrom);
				Account accountTo = customers.get(customer.getKey()).getAccount(accTo);
					if (accountFrom!=null){
						if(amountNumber <= accountFrom.getAccountBalance()){
							if(accountTo!= null){
								accountTo.addToBalance(amountNumber);
								accountFrom.deductFromBalance(amountNumber);

								String strResult = "";
								strResult += "£";
								strResult += String.format("%.2f", amountNumber);
								strResult += " has been transferred from ";
								strResult += capitalizeFirstLetter(accFrom);
								strResult += " to ";
								strResult += capitalizeFirstLetter(accTo);

								return strResult;
							}
							else {
								return accTo + " doesn't exist, please retry.";
							}
						}
						else{
							return "There are insufficient funds in " + capitalizeFirstLetter(accFrom) + ". Please retry.";
						}
					}
					else{
						return capitalizeFirstLetter(accFrom) + " doesn't exist, please retry.";
					}
		}
		else {
			return "Bad request. Please enter your command in the following format: MOVE <Payer_Account_name> <Recipient_Account_name> <Amount_to_two_decimal_places> ";
		}
	}

	/**
	 * Takes a request, and creates a new account for a specified customer.
	 *
	 * @param customer the customer creating the new account
	 * @param request  the command and arguments passed in through the command line
	 * @return         the status of the transfer as a string
	 */
	private String createAccount (CustomerID customer, String request) {

		// Split the String into arguments
		String [] arguments = request.split( "\\s+" );

		if (arguments.length != 3) {
			return "Incorrect Number of Arguments! Please enter your command in the following format: CREATEACCOUNT <Account_Name> <Starting_Balance> ";
		}

		if (arguments[2].matches("^[0-9]*(\\.[0-9]{1,2})?$")) {
			
			// Save the arguments as variables
			String command = arguments[0];
			String accountName = arguments[1];
			String strBalance = arguments[2];

			// Convert the string balance to a double
			double balance = Double.parseDouble(strBalance);
			HashMap<String, Customer> customers = getCustomers();

			// Add the new account to the customer's list of
			return customers.get(customer.getKey()).createAccount(accountName, balance);

		} else {
			return "Invalid Amount! Please retry.";
		}
	}

	private boolean validatePassword(String username, String password, Map<String, String> passwords){
		if(passwords.get(username).equals(password)){
			return true;
		}
		return false;
	}


	private String closeAccount (CustomerID customer, String request) {

		// Split the String into arguments
		String [] arguments = request.split( "\\s+" );

		if (arguments.length==2){
			// Save the arguments as variables
			String command = arguments[0];
			String accountName = arguments[1];

			Account accountToClose = customers.get(customer.getKey()).getAccount(accountName);
			if (accountToClose!=null){
				if(accountToClose.getAccountBalance() == 0) {
					String result = customers.get(customer.getKey()).closeAccount(accountToClose);
					return result;
				}
				else{
					return "There are funds remaining in " + capitalizeFirstLetter(accountName) + ". You cannot close an account with funds remaining. Please retry.";
				}
			}
			else{
				return capitalizeFirstLetter(accountName) + " doesn't exist, please retry.";
			}
		}
		else {
			return "Bad request. Please enter your command in the following format: CLOSEACCOUNT <Account_Name> ";
		}
	}
}
