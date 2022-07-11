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
		return (accountName + ": " + balance);
	}

	public double showBalance() {
		return balance;
	}

	public double addToBalance(double amount) {
		balance = balance + amount;
		return balance;
	}

	public double deductFromBalance(double amount) {
		balance = balance - amount;
		return balance;
	}
	
	public String getAcountName() {
		return accountName;
	}

}
