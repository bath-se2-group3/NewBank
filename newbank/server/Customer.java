// Package
package newbank.server;

// Import Statements
import java.util.ArrayList;

/** 
 * Represents a Customer
 *
 * @author University of Bath | Group 3
 */
public class Customer {

	/**
	 * The username of the customer
	 */
	private final String userName;

	/**
	 * The first name of the customer
	 */
	private final String firstName;

	/**
	 * The last name of the customer
	 */
	private final String lastName;

	/**
	 * A list of accounts belonging to the user
	 */
	private ArrayList<Account> accounts;

	/**
	 * Builds a Customer
	 *
	 * @author University of Bath | Group 3
	 */
	public static class CustomerBuilder {

		// The private fields for the customer
		private final String userName;
		private final String firstName;
		private final String lastName;
		private ArrayList<Account> accounts = new ArrayList<>();

		/**
		 * Constructor to Build a Customer
		 *
		 * @param firstName the first name of the customer
		 * @param lastName  the last name of the customer
		 * @param userName  the username of the customer
		 */
		public CustomerBuilder(String firstName, String lastName, String userName) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.userName = userName;
		}

		/**
		 * Add an account to the list of customers accounts.
		 *
		 * i.e. Add a savings account, ISA etc.
		 *
		 * @param account the account to add to the list of customer accounts
		 * @return        the customer builder
		 */
		public CustomerBuilder addAccounts(Account account) {
			this.accounts.add(account);
			return this;
		}

		/**
		 * Return the customer that has been built
		 *
		 * @return the new customer
		 */
		public Customer build() {
			return new Customer(this);
		}
	}

	/**
	 * Constructor for a Customer Object
	 *
	 * @param builder the customer builder
	 */
	private Customer(CustomerBuilder builder) {
		this.userName = builder.userName;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.accounts = builder.accounts;
	}

	/**
	 * Get and return the username.
	 * 
	 * @return the username of the customer
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Get and return the first name.
	 * 
	 * @return the first name of the customer
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Get and return the last name.
	 * 
	 * @return the last name of the customer
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Return the account with a given name
	 * 
	 * @param name the name of the account to return
	 * @return     the account
	 */
	public Account getAccount(String name) {

		// Loop through the customers accounts
		for(Account a : accounts) {
			// Check if the name of the account, is equal to the
			// name that is being searched for
			if (name.equals(a.getAccountName().toLowerCase())){
				return a;
			}
		}

		// Return null if the account cannot be found
		return null;
	}

	/**
	 * Return the account with a given index
	 * 
	 * @param index the index of the account
	 * @return      the account
	 */
	public Account getAccountByIndex(int index){
		for(int i =0; i<accounts.size(); i++) {
			if (i == index){
				return accounts.get(i);
			}
		}
		return null;
	}

	/**
	 * Returns a string with the list of accounts,
	 * including the account name, along
	 * with the balance currently in the account.
	 *
	 * @return the list of accounts as a string
	 */
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

}
