import javax.swing.text.Element;
import java.util.ArrayList;

public class ArrayListToArray {
    /*public static CountyData[] toArray(ArrayList<CountyData> Input, CountyData[] Output){
        Output = new CountyData[Input.size()];
        for (int i = 0; i < Output.length; i++) {
            Output[i] = Input.get(i);
        }
        return Output;
    }*/

    public static CountyData[] toArray(ArrayList<CountyData> Input, CountyData[] Output){
        Output = new CountyData[Input.size()];
        for (int i = 0; i < Output.length; i++) {
            Output[i] = Input.get(i);
        }
        return Output;
    }

    public static double[] toArray(ArrayList<Double> Input, double[] Output){
        Output = new double[Input.size()];
        for (int i = 0; i < Output.length; i++) {
            Output[i] = Input.get(i);
        }
        return Output;
    }


    public static ArrayList<CountyData> toList(ArrayList<CountyData> Output, CountyData[] Input){
        Output = new ArrayList<CountyData>();
        for (int i = 0; i < Input.length; i++) {
            Output.add(Input[i]);
        }
        return Output;
    }
}
