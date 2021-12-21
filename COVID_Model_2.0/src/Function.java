import javax.swing.text.Element;
import javax.swing.text.Segment;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Function {
    public static void main(String[] args) {
        /* ArrayList i = Buffered_IO(Parameters.ReadPath + "GeoCode.csv");
        for (int i1 = 0; i1 < i.size(); i1++) {
            System.out.println(i.get(i1));
        }*/
        /*for (int i = 0; i < 24; i++) {
            System.out.println(findGuassianBlur(Parameters.Travel_distance_distribution_full, i));
        }*/
    }

    public static double[] Normalization(double[] Array){
        double Sum = 0;
        for (int i = 0; i < Array.length; i++) {
            Sum += Array[i];
        }
        for (int i = 0; i < Array.length; i++) {
            if(Array[i]!=0){
                Array[i]/=Sum;
            }
        }
        return Array;
    }

    public static ArrayList<Double> Normalization(ArrayList<Double> Array){
        double Sum = 0;
        for (int i = 0; i < Array.size(); i++) {
            Sum += Array.get(i);
        }
        for (int i = 0; i < Array.size(); i++) {
            if(Array.get(i)!=0){
                Array.set(i,Array.get(i)/Sum);
            }
        }
        return Array;
    }

    public static double[] addArrays(double Array1[], double Array2[]){
        if(Array1.length!=Array2.length){
            System.err.println("Error different array length");
            return Array1;
        }
        else {
            double Combined[] = new double[Array1.length];
            for (int i = 0; i < Array1.length; i++) {
                Combined[i] = Array1[i] + Array2[i];
            }
            return Combined;
        }
    }

    public static double findGuassianBlur(double[] Input, int Median){
        double[] GuassianArr = GaussianDist.GetGaussianDist(Median, 20);
        double[] Output = new double[Input.length];
        double Sum = 0;
        for (int i = 0; i < Input.length; i++) {
            Output[i] = Input[i] * GuassianArr[i];
            Sum += Output[i];
        }
        return Sum;
    }

    public static int[] RandomAssign(int[] Probabilities, int Total){
        double Buffer[] = new double[Probabilities.length];
        for (int Index = 0; Index < Probabilities.length; Index++) {
            Buffer[Index] = Probabilities[Index];
        }
        return RandomAssignArr(Buffer, Total);
    }

    public static int RandomAssign(double[] Probabilities, int Total){
        int Return_index = 0;
        double Normalized_Probabilities[] = Normalization(Probabilities);
        double Generated_rad = Math.random();
        double Iterated = 0;
        for (int Index = 0; Index < Normalized_Probabilities.length; Index++) {
            if(Generated_rad>=Iterated){
                Return_index = Index;
                break;
            }
            Iterated += Normalized_Probabilities[Index];
        }



        return Return_index;
    }

    public static int[] RandomAssignArr(double[] Probabilities, int Total){

        /**
         * Efficiency optimization required
         */

        int[] Return_arr = new int[Probabilities.length];

        int Return_index = 0;
        double Normalized_Probabilities[] = Normalization(Probabilities);

        for (int i = 0; i < Total; i++) {
            double Generated_rad = Math.random();
            double Iterated = 0;

            for (int Index = 0; Index < Normalized_Probabilities.length; Index++) {
                Iterated += Normalized_Probabilities[Index];
                if(Generated_rad<=Iterated){
                    Return_index = Index;
                    break;
                }
                Iterated += Normalized_Probabilities[Index];
            }
            Return_arr[Return_index] += 1;
        }
        return Return_arr;
    }

    public static int Column_count(String input){
        return Comma_count(input)+1;
    }

    public static int Comma_count(String input){
        int count = 0;
        //String copy = input;
        while (input.contains(",")){
            input = input.substring(input.indexOf(",")+1);
            count++;
        }
        return count;
    }

    public static String[] Stratification(String input){
        /**
         * This will first detect the number of elements, takes longer but it is more flexible
         */
        return Stratification(input, Column_count(input));
    }


    public static String[] Stratification(String input, int length){

        /**
         * Overrides the previous one, no detection thus runs much faster
         */

        String[] returned = new String[length];

        for (int elm = 0; elm < length - 1; elm++) {
            String Segment = input.substring(0,input.indexOf(","));
            input = input.substring(input.indexOf(",")+1);
            returned[elm] = Segment;
        }

        returned[length-1] = input;

        return returned;
    }

    public static int index_of_object_in_array(Object target, Object[] Array){
        for (int i = 0; i < Array.length; i++) {
            Object o = Array[i];
            if(o.equals(target)){
                return i;
            }
        }
        return -1;
    }

    public static int index_of_object_in_array(int target, int[] Array){
        for (int i = 0; i < Array.length; i++) {
            Object o = Array[i];
            if(o.equals(target)){
                return i;
            }
        }
        return -1;
    }

    public static ArrayList<String> Buffered_IO(String Path){
       return Buffered_IO(Path,false);
    }

    public static ArrayList<String> Buffered_IO(String Path, boolean Remove_first_line){
        System.out.println("Path acquired!\nPath: " + Path);

        FileReader read = null;
        try {
            read = new FileReader(Path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader buffread = new BufferedReader(read);

        String str;

        if(Remove_first_line){
            System.out.println("Remove_first_line = "+ true);
            try {
                str = buffread.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {
            str = "";
        }

        ArrayList<String> list = new ArrayList<>();

        while (true) {
            try {
                if (!((str = buffread.readLine()) != null)) break;
                list.add(str);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        for (int i = 0; i < list.size(); i++) {
            //System.out.println(list.get(i));
        }
        return list;
    }

    public static String getName(Object obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getName());
        }
        return fields[0].getName();
    }
}

