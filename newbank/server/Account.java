// Package
package newbank.server;

// Import Statements
import java.util.ArrayList;

/** 
 * Represents a Bank Account
 *
 * @author University of Bath | Group 3
 */
public class Account {
	
	/**
	 * The name of the account
	 */
	private String accountName;

	/**
	 * The opening balance of the account
	 */
	private double openingBalance;

	/**
	 * The current balance of the account
	 */
	private double accountBalance;

	/**
	 * Builds an Account
	 *
	 * @author University of Bath | Group 3
	 */
	public static class AccountBuilder {

		/**
		 * The name of the account
		 */
		private String accountName;

		/**
		 * The opening balance of the account
		 */
		private double openingBalance;

		/**
		 * The current balance of the account
		 */
		private double accountBalance;

		/**
		 * Constructor to Build an Account
		 *
		 * @param accountName    the name of the account
		 * @param openingBalance the opening balance of an account
		 * @param accountBalance the current account balance
		 */
		public AccountBuilder(String accountName, double openingBalance, double accountBalance){
			this.accountName = accountName;
			this.openingBalance = openingBalance;
			this.accountBalance = accountBalance;
		}

		/**
		 * Return the account that has been built
		 *
		 * @return the new account
		 */
		public Account build() {
			return new Account(this);
		}
	}

	/**
	 * Constructor for an Account Object
	 *
	 * @param builder the account builder
	 */
	private Account(AccountBuilder builder) {
		this.accountName = builder.accountName;
		this.openingBalance = builder.openingBalance;
		this.accountBalance = builder.accountBalance;
	}

	/**
	 * Get and return the name of the
	 * account.
	 * 
	 * @return the account balance
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * Get and return the opening balance
	 * of the account.
	 * 
	 * @return the account balance
	 */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * Get and return the account balance.
	 * 
	 * @return the account balance
	 */
	public double getAccountBalance() {
		return accountBalance;
	}

	/**
	 * Returns a string of the account name, along
	 * with the balance currently in the account.
	 *
	 * @return the account name and balance as a string
	 */
	public String toString() {
		return (accountName.substring(0, 1).toUpperCase() + accountName.substring(1) + ": " + "£" + String.format("%.2f",accountBalance));
	}

	/**
	 * Return the account balance after adding
	 * a specified amount of money to the
	 * account balance.
	 * 
	 * @param amount the amount of money to add
	 * @return       the account balance
	 */
	public double addToBalance(double amount) {
		accountBalance = accountBalance + amount;
		return accountBalance;
	}

	/**
	 * Return the account balance after deducting
	 * a specified amount of money from the
	 * account balance.
	 * 
	 * @param amount the amount of money to deduct
	 * @return       the account balance
	 */
	public double deductFromBalance(double amount) {
		accountBalance = accountBalance - amount;
		return accountBalance;
	}

}