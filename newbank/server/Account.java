package newbank.server;

import java.util.ArrayList;

public class Account {
	
	private String accountName;
	private double openingBalance;
	private double accountBalance;
	//

	public static class AccountBuilder {

		private String accountName;
		private double openingBalance;
		private double accountBalance;
		//
		//

		public AccountBuilder(String accountName, double openingBalance, double accountBalance){
			this.accountName = accountName;
			this.openingBalance = openingBalance;
			this.accountBalance = accountBalance;
			//
			//
		}

		public Account build() {
			//
			return new Account(this);
		}
	}

	private Account(AccountBuilder builder) {
		this.accountName = builder.accountName;
		this.openingBalance = builder.openingBalance;
		this.accountBalance = builder.accountBalance;
		//
		//
	}
  
	public String getAccountName() {
		return accountName;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public String toString() {
		return (accountName + ": " + accountBalance);
	}

	public double addToBalance(double amount) {
		accountBalance = accountBalance + amount;
		return accountBalance;
	}

	public double deductFromBalance(double amount) {
		accountBalance = accountBalance - amount;
		return accountBalance;
	}

}
