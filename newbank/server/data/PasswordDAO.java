package newbank.server.data;

import newbank.server.Customer;

import java.io.IOException;
import java.util.Map;

public class PasswordDAO extends DAO {

    final static String filePath = "/newbank/server/data/passwords.txt";
    protected Map<String, String> passwords;

    public PasswordDAO() throws IllegalArgumentException {
            this.passwords = textToMap(filePath);
    }

    public Map<String, String> getPasswords(){
        return passwords;
    }

    public void setPassword(Customer customer) throws IOException, IllegalArgumentException {
        if(passwords.containsKey(customer.getUserName())){
            throw new IllegalArgumentException("Password could not be set because username already exists");
        }else{
            insertRecord(filePath, customer.getUserName()+":"+customer.getPassword());
        }
    }

}
