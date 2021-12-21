import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Waning_immunity {
    public static void main(String[] args) {
        Integral_fitting();
    }

    public static void Integral_fitting(){

        ArrayList<Integer> Efficacy_mild = new ArrayList<>();
        ArrayList<Integer> Efficacy_severe = new ArrayList<>();

        int Steps = 300;

        double Integral_start = -6;
        double Integral_end = 6;

        double x = Integral_start;
        double deltax = (Integral_end - Integral_start)/((double) Steps * (Integral_end - Integral_start));
        double Riemann_sum = 0;

        ArrayList<Double> ys = new ArrayList<>();

        for (int i = 0; i < Steps * (Integral_end - Integral_start); i++) {
            //double value =  logistic_model(x) - (0.01 * x) + 0.7;
            double value =  logistic_model(x);
            Riemann_sum += value * deltax;
            //ys.add(Riemann_sum - (0.01 * x) + 0.97);
            ys.add(Riemann_sum - (0.01 * x) + 0.785);
            //ys.add(Riemann_sum - (0.01 * x) + 0.7);
            //ys.add(Riemann_sum);
            //ys.add(value);
            x += deltax;
            //System.out.println(x);
        }

        for (int i = 0; i < Steps  * (Integral_end - Integral_start); i++) {
            if(i>=(Steps * (Integral_end - Integral_start)/2)){
                System.out.println((i * (1.0/(double) Steps) + Integral_start) +","+ys.get(i));
            }
            //System.out.println(ys.get(i));
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("G://Efficacy.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public static double logistic_model_2(double x){
        x = 1.2 * (x-1);
        double constant = -(1.3/(2 * Math.sqrt(Math.PI)));

        if(x<0){
            constant = -constant;
        }

        double value = constant * Math.pow(Math.E, - Math.pow(x,2));
        return value;
    }*/

    public static double logistic_model(double x){
        x = 0.8 * (x-1);
        double constant = -(1.05/(2 * Math.sqrt(Math.PI)));

        /*if(x<0){
            constant = -constant;
        }*/

        double value = constant * Math.pow(Math.E, - Math.pow(x,2));
        return value;
    }
}
