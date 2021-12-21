import java.util.ArrayList;

public class Data {

    private double DataPack[];
    private double DataPackByVariant[][];
    private ArrayList<Patient> PatientArray[][];

    private double[][][] DataPackByAge;

    private double InitialInfected = 50;

    /**Store the epidemiological information of each county of each day*/

    public Data(){

        /**
         * Initialization
         */

        DataPack = new double[Parameters.DataPackSize];
        DataPackByVariant = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize];
        PatientArray = new ArrayList[Parameters.Total_number_of_variants][Parameters.AgeBand.length];
        DataPackByAge = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize][Parameters.AgeBand.length];

        DataPack[0] = Main.Day;/**Day*/
        DataPack[1] = 100000;/**Population*/
        DataPack[2] = InitialInfected;/**Infected*/
        DataPack[3] = 0;/**Exposed*/
        DataPack[4] = 0;/**Active Cases*/
        DataPack[5] = 0;/**Critical Cases*/
        DataPack[6] = 0;/**Resolved*/
        DataPack[7] = 0;/**Death*/
        DataPack[8] = 0;/**Vaccinated*/
        DataPack[9] = 0;/**Clinical Cases*/
        DataPack[10] = 0;/**Subclinical Cases*/
        DataPack[11] = 0;/**CFR (Calculated fatality rate)*/
        DataPack[12] = InitialInfected;/**Immuned*/
        DataPack[13] = 0;/**Tier*/
        DataPack[14] = 0;/**Flight_Constant*/
        DataPack[15] = 0;/**Imported Cases*/
        DataPack[Parameters.AgeBand.length] = 0;/**Exported Cases*/
        
        /**
         * Daily
         * These are for model calculations
         */

        DataPack[17] = InitialInfected;/**New Infected*/
        DataPack[18] = 0;/**New Exposed*/
        DataPack[19] = 0;/**Daily New Cases (Incidence Rate)*/
        DataPack[20] = 0;/**Daily Critical Cases*/
        DataPack[21] = 0;/**Daily Resolved*/
        DataPack[22] = 0;/**Daily Death*/
        DataPack[23] = 0;/**Daily Vaccinated One Dose*/
        DataPack[24] = 0;/**Daily Vaccinated Two Dose*/


        for (int i = 0; i < Parameters.Total_number_of_variants; i++) {
            for (int i1 = 0; i1 < DataPack.length; i1++) {
                DataPackByVariant[i][i1] = DataPack[i1];
            }
            for (int i1 = 0; i1 < Parameters.AgeBand.length; i1++) {
                PatientArray[i][i1] = new ArrayList<Patient>();
            }
        }



    }
    public Data(double Day){

        /**
         * Initialization
         */

        DataPack = new double[Parameters.DataPackSize];
        DataPackByVariant = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize];
        PatientArray = new ArrayList[2][Parameters.AgeBand.length];
        DataPackByAge = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize][Parameters.AgeBand.length];


        DataPack[0] = (int) Day;/**Day*/
        DataPack[1] = 0;/**Population*/
        DataPack[2] = 0;/**Infected*/
        DataPack[3] = 0;/**Exposed*/
        DataPack[4] = 0;/**Active Cases*/
        DataPack[5] = 0;/**Critical Cases*/
        DataPack[6] = 0;/**Resolved*/
        DataPack[7] = 0;/**Death*/
        DataPack[8] = 0;/**Vaccinated*/
        DataPack[9] = 0;/**Clinical Cases*/
        DataPack[10] = 0;/**Subclinical Cases*/
        DataPack[11] = 0;/**CFR (Calculated fatality rate)*/
        DataPack[12] = 0;/**Immuned*/
        DataPack[13] = 0;/**Tier*/
        DataPack[14] = 0;/**Flight_Constant*/
        DataPack[15] = 0;/**Imported Cases*/
        DataPack[Parameters.AgeBand.length] = 0;/**Exported Cases*/

        /**
         * Daily
         * These are for model calculations
         */

        DataPack[17] = 0;/**New Infected*/
        DataPack[18] = 0;/**New Exposed*/
        DataPack[19] = 0;/**Daily New Cases (Incidence Rate)*/
        DataPack[20] = 0;/**Daily Critical Cases*/
        DataPack[21] = 0;/**Daily Resolved*/
        DataPack[22] = 0;/**Daily Death*/
        DataPack[23] = 0;/**Daily Vaccinated One Dose*/
        DataPack[23] = 0;/**Daily Vaccinated Two Dose*/



        for (int i = 0; i < Parameters.Total_number_of_variants; i++) {
            for (int i1 = 0; i1 < DataPack.length; i1++) {
                DataPackByVariant[i][i1] = DataPack[i1];
            }
            for (int i1 = 0; i1 < Parameters.AgeBand.length; i1++) {
                PatientArray[i][i1] = new ArrayList<Patient>();
            }
        }

    }

    public Data(int Population){

        /**
         * Initialization
         */

        DataPack = new double[Parameters.DataPackSize];
        DataPackByVariant = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize];
        PatientArray = new ArrayList[Parameters.Total_number_of_variants][Parameters.AgeBand.length];
        DataPackByAge = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize][Parameters.AgeBand.length];

        /**
         * Cumulative
         * These are for prints
         */

        DataPack[0] = Main.Day;/**Day*/
        DataPack[1] = Population;/**Population*/
        DataPack[2] = InitialInfected;/**Infected*/
        DataPack[3] = 0;/**Exposed*/
        DataPack[4] = 0;/**Active Cases*/
        DataPack[5] = 0;/**Critical Cases*/
        DataPack[6] = 0;/**Resolved*/
        DataPack[7] = 0;/**Death*/
        DataPack[8] = 0;/**Vaccinated*/
        DataPack[9] = 0;/**Clinical Cases*/
        DataPack[10] = 0;/**Subclinical Cases*/
        DataPack[11] = 0;/**CFR (Calculated fatality rate)*/
        DataPack[12] = InitialInfected;/**Immuned*/
        DataPack[13] = 0;/**Tier*/
        DataPack[14] = 0;/**Flight_Constant*/
        DataPack[15] = 0;/**Imported Cases*/
        DataPack[Parameters.AgeBand.length] = 0;/**Exported Cases*/

        /**
         * Daily
         * These are for model calculations
         */

        DataPack[17] = InitialInfected;/**New Infected*/
        DataPack[18] = 0;/**New Exposed*/
        DataPack[19] = 0;/**Daily New Cases (Incidence Rate)*/
        DataPack[20] = 0;/**Daily Resolved*/
        DataPack[21] = 0;/**Daily Critical Cases*/
        DataPack[22] = 0;/**Daily Death*/
        DataPack[23] = 0;/**Daily Vaccinated One Dose*/
        DataPack[24] = 0;/**Daily Vaccinated Two Dose*/



        for (int i = 0; i < Parameters.AgeBand.length; i++) {
            DataPackByAge[0][1][i] = (int) (((double) Population) * Parameters.Population_By_Age[i]);
            //System.out.println("Population: " + Parameters.AgeBand[i] + "  " + DataPackByAge[0][1][i]);
        }

        DataPackByAge[0][2][7] = InitialInfected;
        DataPackByAge[0][12][7] = InitialInfected;
        DataPackByAge[0][17][7] = InitialInfected;

        for (int i = 0; i < Parameters.Total_number_of_variants; i++) {
            for (int i1 = 0; i1 < DataPack.length; i1++) {
                DataPackByVariant[i][i1] = DataPack[i1];
            }
            for (int i1 = 0; i1 < Parameters.AgeBand.length; i1++) {
                PatientArray[i][i1] = new ArrayList<Patient>();
            }
        }

    }

    public Data(int Population, double[][] Preset_Infected, double[] Preset_Immunity){

        /**
         * Initialization
         */

        DataPack = new double[Parameters.DataPackSize];
        DataPackByVariant = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize];
        PatientArray = new ArrayList[Parameters.Total_number_of_variants][Parameters.AgeBand.length];
        DataPackByAge = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize][Parameters.AgeBand.length];

        /**
         * Cumulative
         * These are for prints
         */

        DataPack[0] = Main.Day;/**Day*/
        DataPack[1] = Population;/**Population*/
        DataPack[2] = InitialInfected;/**Infected*/
        DataPack[3] = 0;/**Exposed*/
        DataPack[4] = 0;/**Active Cases*/
        DataPack[5] = 0;/**Critical Cases*/
        DataPack[6] = 0;/**Resolved*/
        DataPack[7] = 0;/**Death*/
        DataPack[8] = 0;/**Vaccinated*/
        DataPack[9] = 0;/**Clinical Cases*/
        DataPack[10] = 0;/**Subclinical Cases*/
        DataPack[11] = 0;/**CFR (Calculated fatality rate)*/
        DataPack[12] = InitialInfected;/**Immuned*/
        DataPack[13] = 0;/**Tier*/
        DataPack[14] = 0;/**Flight_Constant*/
        DataPack[15] = 0;/**Imported Cases*/
        DataPack[Parameters.AgeBand.length] = 0;/**Exported Cases*/

        /**
         * Daily
         * These are for model calculations
         */

        DataPack[17] = InitialInfected;/**New Infected*/
        DataPack[18] = 0;/**New Exposed*/
        DataPack[19] = 0;/**Daily New Cases (Incidence Rate)*/
        DataPack[20] = 0;/**Daily Resolved*/
        DataPack[21] = 0;/**Daily Critical Cases*/
        DataPack[22] = 0;/**Daily Death*/
        DataPack[23] = 0;/**Daily Vaccinated One Dose*/
        DataPack[24] = 0;/**Daily Vaccinated Two Dose*/

        for (int i = 0; i < Parameters.AgeBand.length; i++) {
            DataPackByAge[0][1][i] = (int) ((double) Population * Parameters.Population_By_Age[i]);
        }

        DataPackByAge[0][2][7] = InitialInfected;
        DataPackByAge[0][12][7] = InitialInfected;
        DataPackByAge[0][17][7] = InitialInfected;

        for (int i = 0; i < Parameters.Total_number_of_variants; i++) {
            for (int i1 = 0; i1 < DataPack.length; i1++) {
                DataPackByVariant[i][i1] = DataPack[i1];
            }
            for (int i1 = 0; i1 < Parameters.AgeBand.length; i1++) {
                PatientArray[i][i1] = new ArrayList<Patient>();
            }
        }

    }

    public Data(double[] DataPack, double[][] DataPackByVariant, double[][] DataPackByAge){
        this.DataPack = DataPack;
        this.DataPackByVariant = DataPackByVariant;
    }

    public Data(int Population, int[] Initial_Case_Arr, int[] Initial_Immunity_Arr){

        /**
         * Initialization
         */

        DataPack = new double[Parameters.DataPackSize];
        DataPackByVariant = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize];
        PatientArray = new ArrayList[Parameters.Total_number_of_variants][Parameters.AgeBand.length];
        DataPackByAge = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize][Parameters.AgeBand.length];

        /**
         * Cumulative
         * These are for prints
         */

        DataPack[0] = Main.Day;/**Day*/
        DataPack[1] = Population;/**Population*/
        DataPack[2] = InitialInfected;/**Infected*/
        DataPack[3] = 0;/**Exposed*/
        DataPack[4] = 0;/**Active Cases*/
        DataPack[5] = 0;/**Critical Cases*/
        DataPack[6] = 0;/**Resolved*/
        DataPack[7] = 0;/**Death*/
        DataPack[8] = 0;/**Vaccinated*/
        DataPack[9] = 0;/**Clinical Cases*/
        DataPack[10] = 0;/**Subclinical Cases*/
        DataPack[11] = 0;/**CFR (Calculated fatality rate)*/
        DataPack[12] = InitialInfected;/**Immuned*/
        DataPack[13] = 0;/**Tier*/
        DataPack[14] = 0;/**Flight_Constant*/
        DataPack[15] = 0;/**Imported Cases*/
        DataPack[Parameters.AgeBand.length] = 0;/**Exported Cases*/

        /**
         * Daily
         * These are for model calculations
         */

        DataPack[17] = InitialInfected;/**New Infected*/
        DataPack[18] = 0;/**New Exposed*/
        DataPack[19] = 0;/**Daily New Cases (Incidence Rate)*/
        DataPack[20] = 0;/**Daily Resolved*/
        DataPack[21] = 0;/**Daily Critical Cases*/
        DataPack[22] = 0;/**Daily Death*/
        DataPack[23] = 0;/**Daily Vaccinated One Dose*/
        DataPack[24] = 0;/**Daily Vaccinated Two Dose*/

        for (int i = 0; i < Parameters.AgeBand.length; i++) {
            DataPackByAge[0][1][i] = (int) ((double) Population * Parameters.Population_By_Age[i]);
        }

        DataPackByAge[0][2][7] = InitialInfected;
        DataPackByAge[0][12][7] = InitialInfected;
        DataPackByAge[0][17][7] = InitialInfected;

        for (int i = 0; i < Parameters.Total_number_of_variants; i++) {
            for (int i1 = 0; i1 < DataPack.length; i1++) {
                DataPackByVariant[i][i1] = DataPack[i1];
            }
            for (int i1 = 0; i1 < Parameters.AgeBand.length; i1++) {
                PatientArray[i][i1] = new ArrayList<Patient>();
            }
        }

    }

    public void setDataPack(double[] DataPack, double[][] DataPackByVariant){
        this.DataPack = DataPack;
        this.DataPackByVariant = DataPackByVariant;
    }

    public void setPatientArray(ArrayList<Patient>[][] PatientArray){
        this.PatientArray = PatientArray;
    }

    public double[] getDataPack(){
        return DataPack;
    }

    public double[][] getDataPackByVariant(){
        return DataPackByVariant;
    }

    public ArrayList<Patient>[][] getPatientArray() {
        return PatientArray;
    }

    public double[][][] getDataPackByAge(){return DataPackByAge;}

    public void setDataPackByAge(double[][][] Package){DataPackByAge = Package;}

    public void setValueDataPack(int Index, double Value){
        DataPack[Index] = Value;
    }

    public void addValueDataPack(int Index, double Delta_Value){
        DataPack[Index] += Delta_Value;
    }

    public void setValueDataPackByAge(int Variant_index,int AgeBand_index, int Index, double Value){
        DataPackByAge[Variant_index][Index][AgeBand_index] = Value;
    }

    public void addValueDataPackByAge(int Variant_index,int AgeBand_index, int Index, double Delta_Value){
        DataPackByAge[Variant_index][Index][AgeBand_index] += Delta_Value;
    }

    public void reCalculate(){

        /**
         * recalculate total value from age_specific data
         */

            for (int index = 0; index < DataPack.length; index++) {
                double subtotal = 0;
                for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
                    for (int Age_band = 0; Age_band < Parameters.AgeBand.length; Age_band++) {
                        subtotal += DataPackByAge[variant][index][Age_band];
                    }
                }
                DataPack[index] = subtotal;
            }

    }

    public void setAll_index(double[] array, int variant, int index){
        double subtotal = 0;
        for (int i = 0; i < array.length; i++) {
            subtotal += array[i];
            DataPackByAge[variant][index][i] = array[i];
        }
        DataPack[index] = subtotal;
    }

    public void setAll_index(double Total, double[] array, int variant, int index){
        for (int i = 0; i < array.length; i++) {
            DataPackByAge[variant][index][i] = array[i];
        }
        DataPack[index] = Total;
    }

}
