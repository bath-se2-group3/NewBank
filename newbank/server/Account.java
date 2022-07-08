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
		return balance+amount;
	}

	public double deductFromBalance(double amount) {
		return balance-amount;
	}

}
