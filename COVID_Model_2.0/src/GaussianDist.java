public class GaussianDist {
    public static void main(String[] args) {
        double[] DistFun = GetGaussianDist(15, 20);

        for (int i = 0; i < DistFun.length; i++) {
            System.out.println(DistFun[i]);
        }
        /**for (int i = 0; i < 16; i++) {
         double[] DistFun = GetGuassianDist(i);
         for (int i1 = 0; i1 < DistFun.length; i1++) {
         System.out.print(DistFun[i1] +",");
         }
         System.out.println();
         }*/
    }
    public static double[] GetGaussianDist(double Peak, double Range){

        double Precision = 1000;
        double sum = 0;
        double FractionSum = 0;
        int NextInt = 1;
        double Probabilities[] = new double[24];
        double theta = 0.1;
        for (double i1 = -0.5; i1 < 24; i1+=24/Precision) {
            double dx = 24/Precision;
            double x = i1;
            double y = (Math.pow(Math.E,-theta * Math.pow((x-(Peak)),2)));
            double dy = dx*y;
            sum += dy;
            FractionSum += dy;
            if(x>=NextInt - 0.5){
                Probabilities[NextInt-1] = FractionSum;
                NextInt++;
                FractionSum = 0;
            }
        }
        Probabilities = Function.Normalization(Probabilities);
        return Probabilities;
    }
}
