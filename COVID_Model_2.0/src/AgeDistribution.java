import java.util.Random;

public class AgeDistribution {

    public static double[] getNew_Incidence(double Patients_By_Age[], double[] immunity_matrix, int Variant_Code, double[][][] Mats, int Scenario, int time_step){
        double[] patient_matrix = new double[16];
        double[] SynthesizedPatientArr = new double[16];
        double[] M = new double[16];
        double[][] M2 = new double[16][16];
        double SumC = 0;

        double[][] Mtx;
        /**
         * Finish Calculation all at once to increase eff
         */
        Mtx = MatrixSynthsis.Synthesis_from_preset(Mats,Parameters.Scenarios[Scenario], time_step);

        for (int Agebandi = 0; Agebandi < 16; Agebandi++) {
            for (int Agebandj = 0; Agebandj < 16; Agebandj++) {
                M2[Agebandi][Agebandj] = Mtx[Agebandi][Agebandj] * Parameters.LockdownResPro[Scenario] * Parameters.Susceptibility[Agebandi];
            }
        }

        for (int AgeBand = 0; AgeBand < 16; AgeBand++) {
            for (int AgeBandj = 0; AgeBandj < 16; AgeBandj++) {
                patient_matrix[AgeBandj] =  (0.95 + (ProbabilityOutput.GetRandom(GammaDist.FindGammaMedian(1))*0.05)) * M2[AgeBand][AgeBandj] * immunity_matrix[AgeBandj] * Patients_By_Age[AgeBand] * Parameters.Infectiousness_By_Variant[Variant_Code];
                SynthesizedPatientArr[AgeBandj] += patient_matrix[AgeBandj];
            }
        }

        for (int i = 0; i < 16; i++) {
            SynthesizedPatientArr[i] = (int) Math.round(SynthesizedPatientArr[i]);
        }

        return SynthesizedPatientArr;
    }

    public static double[] GetPatientArray(int AgeBand,double Patients, double[] ImmunityMatrix,double constant, int Variant_Code, int infected, boolean Unlock, double[][][] Mats, int level){

        if(Patients==0){
            return new double[16];
        }

        double AsymRatio = Parameters.SubClinical_Ratio_By_Age[AgeBand];
        double[] patient_matrix = new double[16];
        double[] M = new double[16];
        double SumC = 0;


        if(Unlock){
            double[][] Mtx;
            Mtx = MatrixSynthsis.Synthesis(Mats,Parameters.Scenarios[level]);
            for (int i = 0; i < 16; i++) {
                M[i] = Mtx[AgeBand][i] * Parameters.LockdownResPro[level] * Parameters.Susceptibility[i];
                double R = ProbabilityOutput.GetRandom(GammaDist.getGammaFunction(1));
                SumC +=  Patients * R * M[i] * (1.0/Parameters.Steps);
            }
        }

        else if(constant<0.9){
            double[][] Mtx;
            double[] SchoolD = new double[4];
            for (int i = 0; i < 4; i++) {
                SchoolD[i] = constant;
            }
            Mtx = MatrixSynthsis.Synthesis(Mats,SchoolD);
            for (int i = 0; i < 16; i++) {
                M[i] = Mtx[AgeBand][i] * Parameters.Susceptibility[i];
                double R = ProbabilityOutput.GetRandom(GammaDist.getGammaFunction(1));
                SumC +=  Patients * R * M[i] * (1.0/Parameters.Steps);
            }
        }

        else {
            double[][] Mtx;
            double full[] = {1,1,1,1};
            Mtx = MatrixSynthsis.Synthesis(Mats,full);
            for (int i = 0; i < 16; i++) {
                M[i] = Mtx[AgeBand][i] * Parameters.Susceptibility[i];
                double R = ProbabilityOutput.GetRandom(GammaDist.getGammaFunction(1));
                SumC +=  Patients * R * M[i] * (1.0/Parameters.Steps);
            }
        }

        //System.out.println("AgeGroup_of_Contact:" + Parameters.AgeBand[AgeBand]);

        double sum = 0;
        for (int i = 0; i < 16; i++) {
            sum += M[i];
        }

        for (int i = 0; i < 16; i++) {
            patient_matrix[i] =  (1 + (ProbabilityOutput.GetRandom(GammaDist.FindGammaMedian(1))*0.05)) * M[i] * ImmunityMatrix[i] * Patients * Parameters.Infectiousness_By_Variant[Variant_Code];
            double b =  patient_matrix[i];
            /**
             * Requires modification
             */
            b = b * AsymRatio * 0.5 + b * (1.0-AsymRatio);
            patient_matrix[i] = b;
            //System.out.println(patient_matrix[i]);
        }
        int Stochasticinfected = 0;

        /**if(Patients<20){//Stochastic if not enough
            for (int i = 0; i < (int) SumC; i++) {
                boolean Asymtomatic = false;
                if(Math.random()<Parameters.SubClinical_Ratio_By_Age[AgeBand]){
                    Asymtomatic = true;
                }
                if (Asymtomatic) {
                    if(Math.random()<Parameters.Infectiousness_By_Variant[Variant_Code]*0.5){
                        Stochasticinfected++;
                    }
                }
                else {
                    if(Math.random()<Parameters.Infectiousness_By_Variant[Variant_Code]){
                        Stochasticinfected++;
                    }
                }
            }
            for (int i = 0; i < 16; i++) {
                M[i]= ImmunityMatrix[i]*M[i]*Parameters.Susceptibility[i];
            }
            for (int i = 0; i < Stochasticinfected; i++) {
                int Ind = (int) ProbabilityOutput.GetRandom(M);
                patient_matrix[Ind]++;
            }
        }*/
        return patient_matrix;
    }

    public static int getPatientAge(String name, double[] AgeMatrix, double[] ImmunityMatrix){
        String Country = name.substring(0,2);
        int ind = Input.Countrycode.indexOf(Country);
        double distribut[] = new double[16];
        for (int i = 0; i < 16; i++) {
            double p  = ImmunityMatrix[i];
            distribut[i] = p * AgeMatrix[i];
            if(AgeMatrix[i]!=0){
                //System.out.println("MATRIX"+Parameters.AgeBand[i]+ ",  " + AgeMatrix[i]);
            }
        }

        double sum = 0;
        for (int i = 0; i < distribut.length; i++) {
            sum += distribut[i];
        }
        for (int i2 = 0; i2 < 16; i2++) {
            if (Double.toString(AgeMatrix[i2])=="NaN") {
                distribut = Input.AgeS.get(ind);
                sum = 10000;
                for (int i = 0; i < distribut.length; i++) {
                    distribut[i] = (int) (distribut[i]*100.0);
                }
                //System.out.println(true);
                break;
            }
        }
        if(sum<10000){
            for (int i = 0; i < 16; i++) {
                distribut[i] = 10000.0*distribut[i]/sum;
            }
            sum = 10000;
        }
        Random rad = new Random();
        int age = 0;
        int rado = rad.nextInt((int)sum);
        int residu = 5;
        int Chara = 0;
        int last = 0;
        for (int i = 0; i < distribut.length; i++) {
            if(rado>0.98*sum){
                residu =15;
                break;
            }
            Chara += distribut[i];
            if(last<=rado&&rado<Chara) {
                residu = i;
                break;
            }
            last = Chara;
        }

        return residu;
    }

    public static double getMortalityRate(int age){
        return (0.047*Math.pow(Math.E,0.07*age));
    }
}
