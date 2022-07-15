package newbank.server;

import java.util.ArrayList;

public class Customer {

	private final String userName;
	private final String firstName;
	private final String lastName;
	private String mail;
	private ArrayList<Account> accounts;

	public static class CustomerBuilder {

		private final String userName;
		private final String firstName;
		private final String lastName;
		private String mail;
		private ArrayList<Account> accounts = new ArrayList<>();

		public CustomerBuilder(String firstName, String lastName, String userName) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.userName = userName;
			this.mail = "Mail address not set by user"; //TODO: Discuss: Do we think it's ok that a mail address is expected to be manually set by a user? that is, not set by the builder class
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
		this.mail = builder.mail;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mailAddress) {
		this.mail = mailAddress;
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
