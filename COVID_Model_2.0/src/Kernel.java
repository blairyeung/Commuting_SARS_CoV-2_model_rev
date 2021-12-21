import java.sql.ClientInfoStatus;
import java.util.ArrayList;

public class Kernel {

    /*public static void main(String[] args) {
        double Ontario_Age_distribution[] = {723016, 762654, 792947, 852405, 1039661, 1077433, 1041952, 992844, 921378, 932058, 968546, 1073519, 961243, 803962, 673546, 461015, 319548, 204227, 98638, 29527, 3895};
        //double Ontario_bias[] =    {0.93233476,0.9690367,0.909552598,0.83729059,0.944890746,1.004471513,1.040728868,1.070075112,1.007271233,0.934853606,0.942618458,1.151368848,1.271489248,1.386529457,1.629781421,1.689743487,1.889962568,2.51862702,3.335309895,2.855353005,0.2,0.2,0.2,0.2};
        double Ontario_bias[] = {1.029995593, 1.017385222, 1.044708337, 1.090123314, 0.990317339, 0.976549085, 0.978488904, 0.975483164, 1.01795722, 1.023833297, 1.034343857, 0.931690828, 0.932943761, 0.935675809, 0.872771964, 0.906153957, 0.87737099, 0.826286392, 0.818528024, 0.981450217, 2.513963678,0.2,0.2,0.2,0.2};

        double Calibrated[] = Gaussian_Kernel(Ontario_Age_distribution,5);
        double Smoothed[] = new double[Ontario_Age_distribution.length];

        double[] Smoothed_bias = Gaussian_Kernel(Ontario_bias,5);

        for (int i = 0; i < Calibrated.length; i++) {
            //System.out.println(Smoothed_bias[i]);
            System.out.println(Calibrated[i] * Smoothed_bias[i] * 5);
        }
        for (int i = 0; i < Smoothed.length; i++) {
            for (int i1 = 0; i1 < 5; i1++) {
                Smoothed[i]+=Calibrated[i1+5*i];
            }
            //System.out.println(Smoothed[i]);
        }
    }*/

    public static void main(String[] args) {

        ArrayList<String> Input1 = Function.Buffered_IO("F:\\Original 0-10C.bak.csv");
        double Ontario_Age_distribution[] = new double[Input1.size()];
        for (int i = 0; i < Ontario_Age_distribution.length; i++) {
            Ontario_Age_distribution[i] = Double.parseDouble(Input1.get(i));
        }

        //double Ontario_Age_distribution[] = {723016, 762654, 792947, 852405, 1039661, 1077433, 1041952, 992844, 921378, 932058, 968546, 1073519, 961243, 803962, 673546, 461015, 319548, 204227, 98638, 29527, 3895};
        //double Ontario_bias[] =    {0.93233476,0.9690367,0.909552598,0.83729059,0.944890746,1.004471513,1.040728868,1.070075112,1.007271233,0.934853606,0.942618458,1.151368848,1.271489248,1.386529457,1.629781421,1.689743487,1.889962568,2.51862702,3.335309895,2.855353005,0.2,0.2,0.2,0.2};
        double Ontario_bias[] = {1.029995593, 1.017385222, 1.044708337, 1.090123314, 0.990317339, 0.976549085, 0.978488904, 0.975483164, 1.01795722, 1.023833297, 1.034343857, 0.931690828, 0.932943761, 0.935675809, 0.872771964, 0.906153957, 0.87737099, 0.826286392, 0.818528024, 0.981450217, 2.513963678,0.2,0.2,0.2,0.2};

        double Calibrated[] = Gaussian_Kernel(Ontario_Age_distribution,5);
        double Smoothed[] = new double[Ontario_Age_distribution.length];

        double[] Smoothed_bias = Gaussian_Kernel(Ontario_bias,5);

        for (int i = 0; i < Calibrated.length; i++) {
            //System.out.println(Smoothed_bias[i]);
            System.out.println(Calibrated[i] * 1 * 5);
        }
        for (int i = 0; i < Smoothed.length; i++) {
            for (int i1 = 0; i1 < 5; i1++) {
                Smoothed[i]+=Calibrated[i1+5*i];
            }
            //System.out.println(Smoothed[i]);
        }
    }

    public static double[] Gaussian_Kernel(double[] Unsmoothed, int Smooth_level){
        double[] Output = new double[Unsmoothed.length*((int) ((double)Smooth_level*1.2))];
        for (int i = 0; i < Output.length; i++) {
            int center = i/Smooth_level;
            double calibrated_center = (((double) i)/((double) Smooth_level));
            double[] Calibration_array = GuassianDist.GetGaussianDist(calibrated_center,1, Unsmoothed.length);
            double Sum = 0;

            for (int i1 = 0; i1 < Calibration_array.length; i1++) {
                if(i1>Calibration_array.length-1){
                    Sum = Calibration_array[i1] * Unsmoothed[i1];
                }
                Sum += Calibration_array[i1] * Unsmoothed[i1];
            }

            Output[i] = Sum * 0.2;

            //System.out.println(calibrated_center);

        }
        return Output;
    }
}
