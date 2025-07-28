package arrangecolumns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Nathaniel
 */
public class ArrangeColumnsMain {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int len = args.length;
        String output = "";
        if(len == 1){
            File input_file = new File(args[0]);
            try (Scanner s = new Scanner(new BufferedReader(new FileReader(input_file)));
                PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)))) {
                output = arrangeColumns(s, pw);
            }
        }
        else if(len == 2){
            File input_file = new File(args[0]);
            File output_file = new File(args[1]);
            output_file.createNewFile();
            try (Scanner s = new Scanner(new BufferedReader(new FileReader(input_file)));
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(output_file)))) {
                output = arrangeColumns(s, pw);
            }
        }
        else{
            System.out.println("Expected \"file1\" or \"file1\" \"file2\" as arguments.");
        }
        if(!output.equals("")){
            JOptionPane.showMessageDialog(null, "Send file: " + args[0] + " to Nathaniel with the following reasons:\n" + output);
        }
    }
    
    public static String arrangeColumns(Scanner in, PrintWriter out){
        boolean in_headers = true;//flag for if we are reading in the file headers
        boolean capture_both = true;//flag for if we are reading the pair of columns (in which case we want to commit both column0 and column1 to the columns list
        ArrayList<Column> columns = new ArrayList<>();//list of all columns
        Column column0 = new Column();//left column
        Column column1 = new Column();//right column
        
        String send_to_NL_message = "";
        Pattern col_labels = Pattern.compile("#\\s*[cC]olumn[lL]abels:.*");
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(line.strip().startsWith("#")){//'#' prefixes a commented line           
                Matcher m = col_labels.matcher(line.strip());//check if this line has the column headers
                if(m.matches()){
                    if(!in_headers){
                        if(capture_both){
                            columns.add(column0);
                            capture_both = false;
                        }
                        columns.add(column1);
                        column0 = new Column();
                        column1 = new Column();
                    }
                    else{
                        in_headers = false;
                    }
                    int labelstart = line.indexOf(":") + 1;
                    String labelsubstr = line.substring(labelstart).strip();
                    int label0end = labelsubstr.indexOf(" ");
                    int label1end = labelsubstr.indexOf("(", label0end);
                    if(label1end < label0end){
                        label1end = labelsubstr.length();
                    }
                    String header0 = labelsubstr.substring(0, label0end).strip();
                    String header1 = labelsubstr.substring(label0end, label1end).strip();
                    column0.setHeader(header0);
                    column1.setHeader(header1);
                }
                
                if(in_headers){//all commented header lines get pushed to the output file
                    out.println(line.stripTrailing());
                }
            }
            else if(line.strip().length() > 3){//if line isn't commented and is theoretically long enough to be split, split it
                String[] items = line.strip().split("\\s+");
                if(items.length == 2){
                    column0.addDataEntry(items[0].strip());
                    column1.addDataEntry(items[1].strip());
                }
            }
        }
        
        if(capture_both){
            columns.add(column0);
        }
        columns.add(column1);
        
        int numrows = columns.getFirst().getDataLength();
        String delimiter = "\t";
        
        String hline = "#" + columns.getFirst().getHeader();
        for(int col = 1; col < columns.size(); col++){
            hline = hline + delimiter + columns.get(col).getHeader();
        }
        out.println(hline);
        for(int row = 0; row < numrows; row++){
            String outln = columns.getFirst().getDataEntry(row);
            for(int col = 1; col < columns.size(); col++){
                outln = outln + delimiter + columns.get(col).getDataEntry(row);
            }
            out.println(outln);
        }
        
        return send_to_NL_message;
    }
    
}
