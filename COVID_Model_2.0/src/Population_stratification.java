import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Population_stratification {
    /**
     * Stratify
     * @param args
     */
    public static void main(String[] args) {
        Extract();
    }

    public static void Extract(){

        ArrayList<String> list = new ArrayList();

        String path = "G:\\98-400-X2016003_ENG_CSV\\98-400-X2016003_English_CSV_data.csv";
        String outputpath = "G:\\98-400-X2016003_ENG_CSV\\Ontario_Stratified_Subdivision.csv";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(outputpath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = "";
        String first_line = "";
        try {
            first_line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path);

        int no_items = 14;

        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println(line);

            if(line.indexOf("years")!=-1){
                String thisline = line;
                String[] Items = new String[no_items];
                String item = "";
                for (int i = 0; i < no_items-1; i++) {
                    //System.out.println(i);
                    item = thisline.substring(0,thisline.indexOf(","));
                    thisline = thisline.substring(thisline.indexOf(",")+1);
                    Items[i] = item;
                }
                item = thisline;
                Items[no_items-1] = item;
                for (int i = 0; i < no_items; i++) {
                    //System.out.println(Items[i]);
                }

                //System.out.println(Items[1].substring(1,3));
                if(Items[1].substring(1,3).equals("35")){
                    //System.out.println(Items[1]);
                    if(Items[1].length()==9){
                        list.add(line);
                    }
                }
                //System.out.println(true);

            }
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        try {
            writer.write(first_line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            try {
                writer.write(list.get(i));
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
