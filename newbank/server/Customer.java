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
   
	private String userName;

	/**
	 * The first name of the customer
	 */
	
	private final String firstName;

	/**
	 * The last name of the customer
	 */
	private final String lastName;

  /**
   * The mail of the customer
   */
	private String mail;
  
  /**
   * The phone number of the customer
   */
	private Integer phoneNumber;

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
     * The mail of the customer
     */
	  private String mail;
  
    /**
     * The phone number of the customer
     */
	  private Integer phoneNumber;

    /**
	   * A list of accounts belonging to the user
	   */
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
			this.mail = "Mail address not set by user"; //TODO: Discuss: Do we think it's ok that a mail address is expected to be manually set by a user? that is, not set by the builder class
			this.phoneNumber = null;
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
		this.mail = builder.mail;
		this.phoneNumber = builder.phoneNumber;
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
	 * Get and return the mail.
	 * 
	 * @return the mail of the customer
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Set the username
	 */
	public String setUserName(String name) {
		this.userName = name;
		return userName;
	}

  /**
	 * Get and return the phone number
	 * 
	 * @return the phone number of the customer
	 */
	public Integer getPhoneNumber() {
		return phoneNumber;
	}

  /**
	 * Set the mail address
	 * 
	 * @param mailAddress the mail address of the customer
	 */
	public void setMail(String mailAddress) {
		this.mail = mailAddress;
	}

  /**
	 * Set the phone number
	 * 
	 * @param phoneNumber the phone number of the customer
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = Integer.parseInt(phoneNumber);
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
		try {
			// Create the new account
			Account newAccount = new Account.AccountBuilder(accountName, balance, balance).build();

			// Add the account to the list of users accounts
			this.addAccount(newAccount);

			String strResult = "";
			strResult += accountName;
			strResult += " created, with a balance of ";
			strResult += balance;
			strResult += ".\n";
			strResult += "You now have the following accounts: ";
			strResult += this.accountsToString();

			return strResult;

		} catch (Exception e) {
			return "Sorry, an error occurred! Please retry.";
		}
	}

	public String closeAccount(Account accountName) {
		try {
			
			

			// Remove account
			this.removeAccount(accountName);

			String strResult = "";
			strResult += "Account deleted";
			strResult += ".\n";
			strResult += "You now have the following accounts: ";
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

	public void removeAccount (Account account) {
		this.accounts.remove(account);
	}
  
  /**
	 * Return the account with a given name
	 * 
	 * @param name the name of the account to return
	 * @return     the account
	 */
	public Account getAccount(String name){
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
		String s = "\n";
		for(Account a : accounts) {
			s += a.toString() + "\n";
		}
		return s;
	}

}
