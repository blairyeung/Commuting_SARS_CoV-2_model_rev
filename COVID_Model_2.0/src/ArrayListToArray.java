import javax.swing.text.Element;
import java.util.ArrayList;

public class ArrayListToArray {
    /*public static CountyData[] toArray(ArrayList<CountyData> input, CountyData[] output){
        output = new CountyData[input.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = input.get(i);
        }
        return output;
    }*/

    public static CountyData[] toArray(ArrayList<CountyData> input, CountyData[] output){
        output = new CountyData[input.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = input.get(i);
        }
        return output;
    }

    public static double[] toArray(ArrayList<Double> input, double[] output){
        output = new double[input.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = input.get(i);
        }
        return output;
    }


    public static ArrayList<CountyData> toList(ArrayList<CountyData> output, CountyData[] input){
        output = new ArrayList<CountyData>();
        for (int i = 0; i < input.length; i++) {
            output.add(input[i]);
        }
        return output;
    }
}
