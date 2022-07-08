package newbank.server;

public class Account {
	
	private String accountName;
	private double openingBalance;
	private double balance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
		this.balance = openingBalance;
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public double showBalance() {
		return balance;
	}

	public double addToBalance(double amount) {
		this.balance = balance+amount;
		return balance;
	}

	public double deductFromBalance(double amount) {
		this.balance = balance-amount;
		return balance;
	}


	/*
	public static class AccountBuilder {

		private final String accountName;
		private double openingBalance;
		private ArrayList<Account> accounts = new ArrayList<>();

		public AccountBuilder(String accountName, double openingBalance) {
			this.accountName = accountName;
			this.openingBalance = openingBalance;
		}

		public AccountBuilder addAccounts(Account account) {
			this.accounts.add(account);
			return this;
		}

		public Account build() {
			// call the private constructor in the outer class
			return new Account(this);
		}
	}

	private Account(AccountBuilder builder) {
		this.accountName = builder.accountName;
		this.openingBalance = builder.openingBalance;
		this.accounts = builder.accounts;
	}

	public String getAccountName() {
		return accountName;
	}

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}
	*/

}