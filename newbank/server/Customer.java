package newbank.server;

import java.io.IOException;
import java.util.ArrayList;

public class Customer {

	private final String userName;
	private final String firstName;
	private final String lastName;
	private String password;
	private ArrayList<Account> accounts;

	public static class CustomerBuilder {

		private final String userName;
		private final String firstName;
		private final String lastName;
		private String password = null;

		private ArrayList<Account> accounts = new ArrayList<>();
		public CustomerBuilder(String firstName, String lastName, String userName) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.userName = userName;
		}

		public CustomerBuilder(String firstName, String lastName, String userName, String password) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.userName = userName;
			this.password = password;
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
		this.password = builder.password;
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

	public String getPassword() {
		return password;
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

	public void setPassword(Customer customer) throws IOException {
		Password passwords = new Password();
		passwords.setPassword(customer);
	}

}
