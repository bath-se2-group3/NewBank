package newbank.server;

import java.util.ArrayList;

public class Customer {

	private final String userName;
	private final String firstName;
	private final String lastName;
	private ArrayList<Account> accounts;

	public static class CustomerBuilder {

		private final String userName;
		private final String firstName;
		private final String lastName;
		private ArrayList<Account> accounts = new ArrayList<>();

		public CustomerBuilder(String firstName, String lastName, String userName) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.userName = userName;
		}

		public CustomerBuilder addAccounts(Account account) {
			this.accounts.add(account);
			return this;
		}

		public Customer build() {
			// call the private constructor in the outer class
			return new Customer(this);
		}
	}

	private Customer(CustomerBuilder builder) {
		this.userName = builder.userName;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.accounts = builder.accounts;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	/**
	 * Takes a request, and creates a new account for a 
	 * specified customer.
	 *
	 * @param accountName the name of the new account
	 * @param balance     the starting balance of the new account
	 * @return            the status of the transfer as a string
	 */
	public String createAccount(String accountName, double balance) {

		String strResult = "";

		try {
			// Create the new account
			Account newAccount = new Account.AccountBuilder(accountName, balance, balance).build();

			// Add the account to the list of users accounts
			this.addAccount(newAccount);

			strResult += accountName;
			strResult += " created, with a balance of ";
			strResult += balance;
			strResult += ".\n";
			strResult += "You know have the following accounts: ";
			strResult += this.accountsToString();

			return strResult;

		} catch (Exception e) {
			return "Sorry, an error occurred! Please retry.";
		}
	}

	/**
	 * Adds an account to the customers lists of accounts.
	 *
	 * @param account the account to add to the customers list of accounts
	 */
	public void addAccount (Account account) {
		this.accounts.add(account);
	}

	public Account getAccount(String name){
		for(Account a : accounts) {
			if (name.equals(a.getAccountName().toLowerCase())){
				return a;
			}
		}
		return null;
	}

	public Account getAccountByIndex(int index){
		for(int i =0; i<accounts.size(); i++) {
			if (i == index){
				return accounts.get(i);
			}
		}
		return null;
	}

	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

}
