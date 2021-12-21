import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PHU {

    public final static int Number_of_PHUs = 34;
    public static String[] PHUs = new String[Number_of_PHUs];
    public static ArrayList<String> PHUs_list = new ArrayList<>();
    public static int[] PHU_by_Division = null;
    public static ArrayList[] Division_by_PHU = new ArrayList[Number_of_PHUs];

    public static void main(String[] args) {

    }

    public static void PHU_IO_Input(){
        CountyDataIO c = new CountyDataIO();
        PHU_by_Division = new int[CountyDataIO.DistrictCodes.length];
        String PHU_file_path = Parameters.ReadPath + "finished version of Public health matching.csv";
        System.out.println(PHU_file_path);
        System.out.println(CountyDataIO.DistrictCodes.length);

        ArrayList<String> Lines = new ArrayList<>();

        FileReader reader = null;
        try {
            reader = new FileReader(PHU_file_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bufferreader = new BufferedReader(reader);

        String str = null;
        try {
            str = bufferreader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!((str = bufferreader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Lines.add(str);
            String PHU_name = str.substring(0,str.indexOf(","));
            String Division_code =  str.substring(str.indexOf(",")+1);
            int code = Integer.parseInt(Division_code);
            if(!PHUs_list.contains(PHU_name)){
                PHUs_list.add(PHU_name);
                Division_by_PHU[PHUs_list.indexOf(PHU_name)] = new ArrayList<int[]>();
            }
            PHU_by_Division[CountyDataIO.DistrictCode.indexOf(code)] = PHUs_list.indexOf(PHU_name);
            Division_by_PHU[PHUs_list.indexOf(PHU_name)].add(code);
        }

        PHUs = new String[PHUs_list.size()];

        for (int i = 0; i < PHUs_list.size(); i++) {
            PHUs[i] = PHUs_list.get(i);
        }

        for (int i = 0; i < PHU_by_Division.length; i++) {
            System.out.print(CountyDataIO.DistrictCodes[i]+",");
            System.out.println(PHUs_list.get(PHU_by_Division[i]));
        }
    }
}
