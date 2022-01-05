import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PHU {

    public final static int number_of_phu = 34;
    public static String[] phu_arr = new String[number_of_phu];
    public static ArrayList<String> phu_list = new ArrayList<>();
    public static int[] phu_by_division = null;
    public static ArrayList[] division_by_phu = new ArrayList[number_of_phu];

    public static void main(String[] args) {

    }

    public static void PHU_IO_Input(){
        CountyDataIO c = new CountyDataIO();
        phu_by_division = new int[CountyDataIO.district_code_array.length];
        String PHU_file_path = Parameters.Read_path + "finished version of Public health matching.csv";
        System.out.println(PHU_file_path);
        System.out.println(CountyDataIO.district_code_array.length);

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
            if(!phu_list.contains(PHU_name)){
                phu_list.add(PHU_name);
                division_by_phu[phu_list.indexOf(PHU_name)] = new ArrayList<int[]>();
            }
            phu_by_division[CountyDataIO.district_code_list .indexOf(code)] = phu_list.indexOf(PHU_name);
            division_by_phu[phu_list.indexOf(PHU_name)].add(code);
        }

        phu_arr = new String[phu_list.size()];

        for (int i = 0; i < phu_list.size(); i++) {
            phu_arr[i] = phu_list.get(i);
        }

        for (int i = 0; i < phu_by_division.length; i++) {
            System.out.print(CountyDataIO.district_code_array[i]+",");
            System.out.println(phu_list.get(phu_by_division[i]));
        }
    }
}
