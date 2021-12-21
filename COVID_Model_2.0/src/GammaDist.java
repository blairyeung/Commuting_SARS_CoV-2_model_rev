
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class GammaDist {
    public static ArrayList<double[]> Dists = new ArrayList<>();
    public static ArrayList<Double> Index = new ArrayList<>();

    public static void main(String[] args) {
        double input = 40;
        double[] Result = FindGammaMedian(input);
        for (int i = 0; i < Result.length; i++) {
            System.out.println(i +"," + Result[i]);
        }
    }

    public static double[] FindGammaMedian(double Mean){
        double Expectancy = Mean/1.892;
        if(!Index.contains(Mean)||Index.isEmpty()){
            double[] buffer = getGammaFunction(Mean);
            Index.add(Mean);
            Dists.add(buffer);
            return buffer;
        }
        else {
            return Dists.get(Index.indexOf(Mean));
        }
    }

    public static double[] getGammaFunction(double Median){
        double sum = 0;
        double Mean = Median;
        Median = Median/1.892;
        double Precision = 50000.0;
        double ObservationRange = 10.0;
        double lamda = 1/Median;
        Random Rad = new Random();
        int Samplesize = 2000;
        double Result = 0;
        int NextInt = 1;
        int MaxInt = (int) (ObservationRange*Mean);
        double Probabilities[] = new double[MaxInt];
        double FractionSum = 0;

        for (double i1 = 0; i1 < ObservationRange*Median; i1+=Median/Precision) {
            double dx = Median/Precision;
            double x = i1;
            double y = Math.pow(lamda,2)*(x*Math.pow(Math.E,-lamda*x));
            double dy = dx*y;
            sum += dy;
            FractionSum += dy;
            if(x>=NextInt - 0.5){
                Result = Math.round(x);
                Probabilities[NextInt-1] = FractionSum;
                NextInt++;
                FractionSum = 0;
            }
        }
        double Average = 0;
        double s = 1;
        for (int i = 0; i < Probabilities.length; i++) {
            s -= Probabilities[i];
            Average += i * Probabilities[i];
        }
        if(Probabilities.length!=0){
            Probabilities[Probabilities.length-1] = s;
        }
        return Probabilities;
    }

    public static double factorial(double d)
    {
        if (d == 0.0)
        {
            return 1.0;
        }

        double abs = Math.abs(d);
        double decimal = abs - Math.floor(abs);
        double result = 1.0;

        for (double i = Math.floor(abs); i > decimal; --i)
        {
            result *= (i + decimal);
        }
        if (d < 0.0)
        {
            result = -result;
        }

        return result;
    }
}
