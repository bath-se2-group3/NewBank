package newbank.server;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Password {
    final String filePath
            = "/newbank/server/passwords.txt";
    private Map<String, String> passwords;

    public Password() throws IllegalArgumentException{
        passwords = textToMap();

        // iterate over HashMap entries
        for (Map.Entry<String, String> entry :
                passwords.entrySet()) {
            System.out.println(entry.getKey() + ":"
                    + entry.getValue());
        }
    }

    private Map<String, String> textToMap() throws IllegalArgumentException{

        Map<String, String> map
                = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(getFile(filePath)));) {
            String line = null;

            // read file line by line
            while ((line = br.readLine()) != null) {

                // split the line by :
                String[] parts = line.split(":");

                // first part is name, second is number
                String name = parts[0].trim();
                String password = parts[1].trim();

                // put name, number in HashMap if they are
                // not empty
                if (!name.equals("") && !password.equals(""))
                    map.put(name, password);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, String> getPasswords(){
        return passwords;
    }

    public void setPassword(Customer customer) throws IOException {
        if(passwords.containsKey(customer.getUserName())){
            System.out.println("Password could not be set because username already exists");
        }else{
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(filePath), true));
            writer.write(customer.getUserName()+":"+customer.getPassword());
            writer.close();
        }
    }

    private File getFile(String fileName) throws IOException {

        File file;
        String userDir = System.getProperty("user.dir");
        file = new File(userDir);

        System.out.println("canonical path" +file.getCanonicalPath() + fileName);
        //file = new File(userDir+"/..");
        System.out.println(file.getCanonicalPath());

        File resource = new File(file.getCanonicalPath() + fileName);

        if (resource == null) {
            throw new IllegalArgumentException("Database is not available!");
        } else {
            return resource;
        }
    }

}
