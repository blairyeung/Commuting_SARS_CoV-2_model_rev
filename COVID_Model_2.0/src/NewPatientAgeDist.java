import java.util.Random;

public class NewPatientAgeDist {

    public static double[] GetPatientArray(int AgeBand,int Patients, double[][] Matrix, double[] ImmunityMatrix,double constant, int Variant_Code, int infected, boolean Unlock, double[][][] Mats, int level){

        double AsymRatio = Parameters.SubClinical_Ratio_By_Age[AgeBand];
        // System.out.print("Variant: "+ Parameters.Types_of_Variants[Variant_Code]+ "  AgeBand: " + Parameters.AgeBand[AgeBand] + "  Patients: " + Patients);
        //System.out.println("  Infected:  " + infected);
        double[] PatientArr = new double[16];
        double[] M = new double[16];
        double SumC = 0;

        //Comment this

        if(Unlock){
            double[][] Mtx;
            //level = 4-level;
            Mtx = MatrixSynthesisByCategory.Synthesis(Mats,Parameters.Scenarios[level]);
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
            //SchoolD[1] *= (constant*0.05);
            Mtx = MatrixSynthesisByCategory.Synthesis(Mats,SchoolD);
            for (int i = 0; i < 16; i++) {
                M[i] = Mtx[AgeBand][i] * Parameters.Susceptibility[i];
                double R = ProbabilityOutput.GetRandom(GammaDist.getGammaFunction(1));
                SumC +=  Patients * R * M[i] * (1.0/Parameters.Steps);
            }
        }

        else {
            double[][] Mtx;
            double full[] = {1,1,1,1};
            Mtx = MatrixSynthesisByCategory.Synthesis(Mats,full);
            for (int i = 0; i < 16; i++) {
                M[i] = Mtx[AgeBand][i] * Parameters.Susceptibility[i];
                double R = ProbabilityOutput.GetRandom(GammaDist.getGammaFunction(1));
                SumC +=  Patients * R * M[i] * (1.0/Parameters.Steps);
            }
        }

        for (int i = 0; i < 16; i++) {
            PatientArr[i] =  (1 + (ProbabilityOutput.GetRandom(GammaDist.FindGammaMedian(1))*0.05)) * M[i] * ImmunityMatrix[i] * Patients * Parameters.Infectiousness_By_Variant[Variant_Code];
            double b =  PatientArr[i];
            /**
             * Requires modification
             */
            b = b * AsymRatio * 0.5 + b * (1.0-AsymRatio);
            PatientArr[i] = b;
            //System.out.println(PatientArr[i]);
        }
        int Stochasticinfected = 0;
        if(Patients<20){//Stochastic if no enough
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
                PatientArr[Ind]++;
            }
            // System.out.println();
        }
        return PatientArr;
    }

    public static int getPatientAge(String name, double[] AgeMatrix, double[] ImmunityMatrix){
        String Country = name.substring(0,2);
        int ind = IO.Countrycode.indexOf(Country);
        double distribut[] = new double[16];
        for (int i = 0; i < 16; i++) {
            double p  = ImmunityMatrix[i];
            distribut[i] = p * AgeMatrix[i];
            if(AgeMatrix[i]!=0){
                System.out.println("MATRIX"+Parameters.AgeBand[i]+ ",  " + AgeMatrix[i]);
            }
        }

        double sum = 0;
        for (int i = 0; i < distribut.length; i++) {
            sum += distribut[i];
        }
        for (int i2 = 0; i2 < 16; i2++) {
            if (Double.toString(AgeMatrix[i2])=="NaN") {
                distribut = IO.AgeS.get(ind);
                sum = 10000;
                for (int i = 0; i < distribut.length; i++) {
                    distribut[i] = (int) (distribut[i]*100.0);
                }
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
