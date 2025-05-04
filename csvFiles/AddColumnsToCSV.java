import java.io.*;
import java.util.*;


/*
 * This program takes in .csv input file specified by the user and adds dummy information to it.
*/

public class AddColumnsToCSV {
    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Specify input file name: java AddColumnsToCSV sample_input.csv");
            return;
        }

        // read file
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        String line;
        
        // write to output file
        BufferedWriter writer = new BufferedWriter(new FileWriter("modified_" + args[0]));
        
        // add headers to output file
        writer.write("id,name,email,username,password,bio\n");

        // skip first line so data starts at id 0
        reader.readLine(); 

        while ((line = reader.readLine()) != null) {
            String[] columns = line.split(",");
            
            // create dummy variables
            String name = "name " + columns[0];
            String email = "user" + columns[0] + "@example.com";
            String username = "username_" + columns[0];
            String password = "password" + columns[0];
            String bio = "this is the user " + columns[0] + " bio";

            // Write the updated line to the new CSV file
            writer.write(columns[0] + "," + name + "," + email + "," + username + "," + password + "," + bio + "\n");
        }

        reader.close();
        writer.close();

        System.out.println("CSV has been updated.");
    }
}
