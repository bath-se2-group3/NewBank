package newbank.server;

import java.util.HashMap;

public final class TestData {

    private static HashMap<String, Customer> customers = new HashMap<>();

    public static void addTestData() {
        Customer bhagy = new Customer.CustomerBuilder("Sam", "Bhagy", "bhagy")
                .addAccounts(new Account.AccountBuilder("Main", 0.00, 1000.00).build())
                .addAccounts(new Account.AccountBuilder("Savings", 0.00, 1500.00).build())
                .build();
        customers.put("bhagy", bhagy);

        Customer christina = new Customer.CustomerBuilder("Christina", "Marks", "Christina")
                .addAccounts(new Account.AccountBuilder("Savings", 0.00, 1500.00).build())
                .build();
        customers.put("christina", christina);

        Customer john = new Customer.CustomerBuilder("John", "Tees", "John")
                .addAccounts(new Account.AccountBuilder("Checking", 0.00, 250.00).build())
                .addAccounts(new Account.AccountBuilder("Test", 0.00, 99.99).build())
                .build();
        customers.put("john", john);
    }

    public static HashMap<String, Customer> getCustomers(){
        return customers;
    }

    public static boolean addCustomer(Customer customer){
        if(customers.putIfAbsent(customer.getUserName(), customer) == null ){
            return true;
        }
        return false;
    }
}
