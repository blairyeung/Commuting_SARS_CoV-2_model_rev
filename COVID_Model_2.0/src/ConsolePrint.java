public class ConsolePrint {
    public static void main(String[] args) {

    }
    public static void PrintCountyInformation(Data CountyData){
        double[] DataPack = CountyData.getDataPack();

        /**Index 0: Day*/
        /**Index 1: Population*/
        /**Index 2: Infected*/
        /**Index 3: Exposed*/
        /**Index 4: Active_Cases*/
        /**Index 5: Daily_New_Cases*/
        /**Index 6: Resolved*/
        /**Index 7: Critical_Cases*/
        /**Index 8: Death*/
        /**Index 9: New_Resolved*/
        /**Index 10 :New_Critical Cases*/
        /**Index 11 :New_Death*/
        /**Index 12 :Vaccinated*/
        /**Index 13 :Clinical_Cases*/
        /**Index 14 :Subclinical_Cases*/
        /**Index 15 :Imported_Cases*/
        /**Index 16 :Incidence_Rate*/
        /**Index 17 :Calculated_fatality_rate*/
        /**Index 18 :Immune*/
        /**Index 19 :Tier*/
        /**Index 20 :Flight_Constant*/

        for (int i = 0; i < Parameters.DataPackSize; i++) {
            System.out.print(Parameters.ColumnName[i] + ": " +(int)DataPack[i] + "  ");
            if(i%6==0&&i!=0){
                System.out.println();
            }
        }
        System.out.println();

        /**
         * The CFR here only includes tested clinical cases
         */

    }

    public static void PrintInformation_By_Age(Data CountyData){
        double[][][] DataPackByAge = CountyData.getDataPackByAge();

        /**Index 0: Day*/
        /**Index 1: Population*/
        /**Index 2: Infected*/
        /**Index 3: Exposed*/
        /**Index 4: Active_Cases*/
        /**Index 5: Daily_New_Cases*/
        /**Index 6: Resolved*/
        /**Index 7: Critical_Cases*/
        /**Index 8: Death*/
        /**Index 9: New_Resolved*/
        /**Index 10 :New_Critical Cases*/
        /**Index 11 :New_Death*/
        /**Index 12 :Vaccinated*/
        /**Index 13 :Clinical_Cases*/
        /**Index 14 :Subclinical_Cases*/
        /**Index 15 :Imported_Cases*/
        /**Index 16 :Incidence_Rate*/
        /**Index 17 :Calculated_fatality_rate*/
        /**Index 18 :Immune*/
        /**Index 19 :Tier*/
        /**Index 20 :Flight_Constant*/

        for (int Age = 0; Age < 16; Age++) {
            System.out.println("Stratified" + Parameters.AgeBand[Age]);
            for (int i = 0; i < 20; i++) {
                System.out.print(Parameters.ColumnName[i] + ": " +(int)DataPackByAge[0][i][Age] + "  ");
                if(i%6==0&&i!=0){
                    System.out.println();
                }
            }
            System.out.println();
        }

        /**
         * The CFR here only includes tested clinical cases
         */

    }

}
