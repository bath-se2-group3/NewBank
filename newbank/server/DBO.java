package newbank.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DBO {

    protected Map<String, String> textToMap(String filePath) throws IllegalArgumentException{

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

    public void insertRecord(Customer customer, String filePath, String record) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(filePath), true))){
            writer.write(record);
        }
    }

    private File getFile(String fileName) throws IOException {

        File file;
        String userDir = System.getProperty("user.dir");
        file = new File(userDir);

        File resource = new File(file.getCanonicalPath() + fileName);

        if (resource == null) {
            throw new IllegalArgumentException("Database is not available!");
        } else {
            return resource;
        }
    }

}
