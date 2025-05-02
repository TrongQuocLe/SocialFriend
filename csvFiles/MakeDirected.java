import java.io.*;
import java.util.*;


/*
 * This program takes in .csv input file specified by the user.  The original file represented bi-directional
 * relationship between nodes.  This will convert to directional relationship and append rows to the bottom of
 * the original csv file where some of the node_2 variables are pointing to the node_1 variables.
*/

public class MakeDirected {
    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Specify input file name: java MakeDirected sample_input.csv");
            return;
        }
        
        String inputFile = args[0];
        String outputFile = (args.length > 1) ? args[1] : "modified_" + args[0];

        // holds the original edges
        List<String[]> originalEdges = new ArrayList<>();

        // read input
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        // skip the header
        reader.readLine(); 
        String line;
        while ((line = reader.readLine()) != null) {
            String[] columns = line.split(",");
            // add edges to array
            originalEdges.add(new String[]{columns[0], columns[1]});
        }
        reader.close();

        // holds reversed edges
        List<String[]> reversedEdges = new ArrayList<>();

        // shuffle originalEdges list
        Collections.shuffle(originalEdges);
        // calculate the amount of rows to reverse
        int reverseAmount = (int) (originalEdges.size() * 0.3);


        for (int i = 0; i < reverseAmount; i++) {
            String[] edge = originalEdges.get(i);
            // add swapped original edges to reversedEdges array
            reversedEdges.add(new String[]{edge[1], edge[0]});
        }

        // Write output
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write("node_1,node_2\n");

        // write the original edges to the new file
        for (String[] node : originalEdges) {
            writer.write(node[0] + "," + node[1] + "\n");
        }

        // write the reversed edges to the new file
        for (String[] node : reversedEdges) {
            writer.write(node[0] + "," + node[1] + "\n");
        }

        writer.close();
        System.out.println("Directed edge list written to: " + outputFile);
    }
}
