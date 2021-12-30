import javax.swing.text.DefaultFormatterFactory;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Random;

public class SEIRModel{
    public static void main(String[] args) {

    }

    public static Data RunModel(CountyDataArray PastData,CountyDataArray SeriesData,Data CountyData, int Imported, int Exported, int ResponseTier, int County, int Local_workers, int Planned_vaccination, double[][] Age_specific_vaccine_distribution){

        int Date = Main.Day;

        int Population = (int) CountyData.getDataPack()[1];
        int Infected = (int) CountyData.getDataPack()[2];
        int Exposed = (int) CountyData.getDataPack()[3];
        int ActiveCases = (int) CountyData.getDataPack()[4];
        int CriticalCases = (int) CountyData.getDataPack()[5];
        int Resolved = (int) CountyData.getDataPack()[6];
        int Deaths = (int) CountyData.getDataPack()[7];
        int Vaccinated = (int) CountyData.getDataPack()[8];
        int ClinicalCases = (int) CountyData.getDataPack()[9];
        int SubClinicalCases = (int) CountyData.getDataPack()[10];
        double CFR = CountyData.getDataPack()[11];
        int Immune = (int) CountyData.getDataPack()[12];
        int Tier = (int) CountyData.getDataPack()[13];
        int FlightConstant = (int) CountyData.getDataPack()[14];
        int ImportedCases = (int) CountyData.getDataPack()[15];
        int ExportedCases = (int) CountyData.getDataPack()[16];

        int Type = 0;
        if(Population>=10000){
            Type = 1;
        }

        double[][] Work = (double[][]) IO.MatricesByCategory[Type][0].get(IO.Countrycode.indexOf("CA"));
        double[][] School = (double[][]) IO.MatricesByCategory[Type][1].get(IO.Countrycode.indexOf("CA"));
        double[][] Home = (double[][]) IO.MatricesByCategory[Type][2].get(IO.Countrycode.indexOf("CA"));
        double[][] Other = (double[][]) IO.MatricesByCategory[Type] [3].get(IO.Countrycode.indexOf("CA"));

        double[][][] Mat = {Work,School,Home,Other};

        /**
         * Imported_cases will only increase the number of potential contacters, rather than infected
         */

        ExportedCases = 0;
        int Total_Exported = 0;

        int Case_Exportation_Age_Band[] = new int[16];

        int DailyInfected = (int) CountyData.getDataPack()[17];
        int DailyExposed = (int) CountyData.getDataPack()[18];
        int DailyAcvtiveCases = (int) CountyData.getDataPack()[19];
        int DailyCritical = (int) CountyData.getDataPack()[20];
        int DailyResolved = (int) CountyData.getDataPack()[21];
        int DailyDeaths = (int) CountyData.getDataPack()[22];
        int DailyVaccinated= (int) CountyData.getDataPack()[23];

        DailyVaccinated = 0;

        double Total_NewInfected = 0;
        double Total_NewExposed = 0;
        double Total_NewCases = 0;
        double Total_NewCritical = 0;
        double Total_NewResolved = 0;
        double Total_NewDeaths = 0;

        double Total_RemovedExposed = 0;
        double Total_RemovedCases = 0;
        double Total_RemovedCritical = 0;
        //double Total_RemovedResolved = 0;

        double[][] Immunity_by_category = findImmune(PastData, SeriesData);

        ClinicalCases = 0;
        SubClinicalCases = 0;
        DailyInfected = 0;

        /**
         * Process patients who are already infected
         */

        Data ReturnData = new Data(Population);

        for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {


                /**
                 * Add cases
                 */

                double Specific_CFR = Parameters.CFR[Patient_Age_Band] * Parameters.CFR_By_Variant[Variant];

                double Age_Band_NewExposed = 0;
                double Age_Band_NewCases = 0;
                double Age_Band_NewCritical = 0;
                double Age_Band_NewResolved = 0;
                double Age_Band_NewDeaths = 0;

                double Age_Band_RemovedExposed = 0;
                double Age_Band_RemovedCases = 0;
                double Age_Band_RemovedCritical = 0;

                double CFR_true = 1 - Parameters.CFR[Patient_Age_Band] * Immunity_by_category[Patient_Age_Band][0];//Add the CFR difference of different variants

                if(Main.Day>=3){
                    Age_Band_NewExposed = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-3].getDataPackByAge()[Variant][17][Patient_Age_Band]);
                }
                if (Main.Day>=6) {
                    Age_Band_NewCases = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-3].getDataPackByAge()[Variant][18][Patient_Age_Band]);
                    Age_Band_RemovedExposed = Age_Band_NewCases;
                }
                if(Main.Day>=8){
                    Age_Band_NewCritical = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-5].getDataPackByAge()[Variant][19][Patient_Age_Band] * (Parameters.CriticalRate[Patient_Age_Band]));
                }
                if(Main.Day>=14){
                    Age_Band_RemovedCritical = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-14].getDataPackByAge()[Variant][19][Patient_Age_Band] * (Parameters.CriticalRate[Patient_Age_Band]));
                }
                if(Main.Day>=15){
                    Age_Band_NewResolved = Math.round((SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[Variant][19][Patient_Age_Band] * (1.0 - Specific_CFR)) - 0.3);
                    Age_Band_NewDeaths = Math.round(0.3 + (SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[Variant][19][Patient_Age_Band] * Specific_CFR));
                    Age_Band_RemovedCases = SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[Variant][19][Patient_Age_Band];
                }

                int Age_Band_Exposed = (int) Math.round(CountyData.getDataPackByAge()[Variant][3][Patient_Age_Band] + Age_Band_NewExposed - Age_Band_NewCases);
                int Age_Band_ActiveCases = (int) Math.round((CountyData.getDataPackByAge()[Variant][4][Patient_Age_Band] + Age_Band_NewCases - Age_Band_RemovedCases));
                int Age_Band_CriticalCases = (int) Math.round(CountyData.getDataPackByAge()[Variant][5][Patient_Age_Band] + Age_Band_NewCritical - Age_Band_RemovedCritical);
                int Age_Band_Resolved = (int) (CountyData.getDataPackByAge()[Variant][6][Patient_Age_Band] + Age_Band_NewResolved);
                int Age_Band_Deaths = (int) (CountyData.getDataPackByAge()[Variant][7][Patient_Age_Band] + Age_Band_NewDeaths);

                int Age_Band_SubclinicalCases = (int) Math.round(Parameters.SubClinical_Ratio_By_Age[Patient_Age_Band] * Age_Band_ActiveCases);
                int Age_Band_ClinicalCases = Age_Band_ActiveCases - Age_Band_SubclinicalCases;

                ReturnData.setValueDataPackByAge(0,Patient_Age_Band,0,Main.Day+1);//Date
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,1,CountyData.getDataPackByAge()[Variant][1][Patient_Age_Band]);//Population
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,3,Age_Band_Exposed);//Exposed
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,4,Age_Band_ActiveCases);//Active_cases
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,5,Age_Band_CriticalCases);//Critical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,6,Age_Band_Resolved);//Critical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,7,Age_Band_Deaths);//Critical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,9,Age_Band_ClinicalCases);//Clinical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,10,Age_Band_SubclinicalCases);//Subclinical

                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,18,Age_Band_NewExposed);//New_Exposed
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,19,Age_Band_NewCases);//New_Active_cases
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,20,Age_Band_NewCritical);//New_Critical_cases
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,21,Age_Band_NewResolved);//New_Resolved
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,22,Age_Band_NewDeaths);//New_Death

                Total_NewExposed += Age_Band_NewExposed;
                Total_NewCases += Age_Band_NewCases;
                Total_NewCritical += Age_Band_NewCritical;
                Total_NewResolved += Age_Band_NewResolved;
                Total_NewDeaths += Age_Band_NewDeaths;

                Total_RemovedExposed += Age_Band_RemovedExposed;
                Total_RemovedCases += Age_Band_RemovedCases;
                Total_RemovedCritical += Age_Band_RemovedCritical;

                ClinicalCases += Age_Band_ClinicalCases;
                SubClinicalCases += Age_Band_SubclinicalCases;
            }
        }

        /**
            * Find Immunity Matrix/Array
         */


        double ImmunityMat[] = new double[16];

        for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {
            double ShieldingImm = Immunity_by_category[Patient_Age_Band][0]/CountyData.getDataPackByAge()[0][1][Patient_Age_Band];
            ImmunityMat[Patient_Age_Band] = 1-ShieldingImm;
        }

        /**
         *  Patients who are newly infected
         */

        for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
            double Transmissible[] = new double[16];
            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {
                double Age_Band_Exposed = CountyData.getDataPackByAge()[Variant][3][Patient_Age_Band] + (Parameters.Workforce_Age_Dist[Patient_Age_Band] * ImportedCases);
                double EffectiveContacter = Math.round(findEffectiveContacter(SeriesData, Variant, Patient_Age_Band, Age_Band_Exposed)+0.4);
                Transmissible[Patient_Age_Band] = EffectiveContacter;
            }

            /**
             * Number of individuals newly infected
             */

            double[] DailyInfectedArray = AgeDistribution.getNew_Incidence(Transmissible, ImmunityMat, Variant, Mat,Tier, 0);


            double Total_Workers;

            if (Local_workers<=0){
                Local_workers = (int) (((double)Population) * Parameters.Work_force_ratio);
            }

            Total_Workers = Math.round(((double)(Local_workers + Imported)));

            double ratio = ((double) Imported)/ Total_Workers;

            Random rad = new Random();

            ratio = ratio * (1 + 0.1 * rad.nextGaussian());

            if (ratio>1){
                ratio = 1;
            }

            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {

                /**
                 * Export cases by age band (REF)
                 */

                int For_Export = (int) ((DailyInfectedArray[Patient_Age_Band]) * ratio);



                int Age_band_NewInfected = (int) (Math.round(DailyInfectedArray[Patient_Age_Band] - For_Export));

                if(Age_band_NewInfected<0){
                    Age_band_NewInfected = 0;
                }

                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,2,CountyData.getDataPackByAge()[Variant][2][Patient_Age_Band]+Age_band_NewInfected);
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,17, Age_band_NewInfected);
                Total_NewInfected += Age_band_NewInfected;

                ExportedCases += For_Export;
                Total_Exported += For_Export;
            }

        }

        DailyInfected += Math.round(Total_NewInfected);
        Infected += DailyInfected;

        /**Add new cases to total numbers*/

        Exposed = (int) Math.round(Exposed + Total_NewExposed - Total_RemovedExposed);
        ActiveCases = (int) Math.round(ActiveCases + Total_NewCases - Total_RemovedCases);
        CriticalCases = (int) Math.round((CriticalCases + Total_NewCritical - Total_RemovedCritical));
        Resolved += Total_NewResolved;
        Deaths += Total_NewDeaths;

        /**Transcript to daily cases*/

        /**
         * Vaccination
         */

        int Total_vaccinated_today = 0;

        for (int Age_band = 0; Age_band < 16; Age_band++) {
            /**
             * If an age band has more than 80% of its total population infected...
             */

            double Percentage_vaccinated = CountyData.getDataPackByAge()[0][8][Age_band]/CountyData.getDataPackByAge()[0][1][Age_band];

            int Age_band_vaccinated = (int) Math.round(Age_specific_vaccine_distribution[Type][Age_band] * Planned_vaccination);
            if(Percentage_vaccinated>=0.8){
                Age_band_vaccinated = 0;
            }

            Total_vaccinated_today += Age_band_vaccinated;

            ReturnData.setValueDataPackByAge(0,Age_band,23, Age_band_vaccinated);
            ReturnData.setValueDataPackByAge(0,Age_band,12,Age_band_vaccinated + CountyData.getDataPackByAge()[0][12][Age_band]);
            ReturnData.setValueDataPackByAge(0,Age_band,8,Age_band_vaccinated + CountyData.getDataPackByAge()[0][8][Age_band]);
        }

        Immune += Total_vaccinated_today;
        Vaccinated += Total_vaccinated_today;
        DailyVaccinated += Total_vaccinated_today;

        Immune += Total_NewInfected;

        ReturnData.setValueDataPack(0,Main.Day+1);
        ReturnData.setValueDataPack(1,Population);
        ReturnData.setValueDataPack(2,Infected);
        ReturnData.setValueDataPack(3,Exposed);
        ReturnData.setValueDataPack(4,ActiveCases);
        ReturnData.setValueDataPack(5,CriticalCases);
        ReturnData.setValueDataPack(6,Resolved);
        ReturnData.setValueDataPack(7,Deaths);
        ReturnData.setValueDataPack(8,Vaccinated);
        ReturnData.setValueDataPack(9,ClinicalCases);
        ReturnData.setValueDataPack(10,SubClinicalCases);
        ReturnData.setValueDataPack(11,CFR);
        ReturnData.setValueDataPack(12,Immune);
        ReturnData.setValueDataPack(13,Tier);
        ReturnData.setValueDataPack(14,FlightConstant);
        ReturnData.setValueDataPack(15,ImportedCases);
        ReturnData.setValueDataPack(16,ExportedCases);
        ReturnData.setValueDataPack(17,DailyInfected);
        ReturnData.setValueDataPack(18,Total_NewExposed);
        ReturnData.setValueDataPack(19,Total_NewCases);
        ReturnData.setValueDataPack(20,Total_NewCritical);
        ReturnData.setValueDataPack(21,Total_NewResolved);
        ReturnData.setValueDataPack(22,Total_NewDeaths);
        ReturnData.setValueDataPack(23,DailyVaccinated);

        return ReturnData;
    }

    public static Data NightModel(CountyDataArray PastData,CountyDataArray SeriesData,Data CountyData, int Imported, int Exported, int ResponseTier, int County, int Planned_vaccination, double[][] Age_specific_vaccine_distribution){
        int Date = Main.Day;

        int Population = (int) CountyData.getDataPack()[1];
        int Infected = (int) CountyData.getDataPack()[2];
        int Exposed = (int) CountyData.getDataPack()[3];
        int ActiveCases = (int) CountyData.getDataPack()[4];
        int CriticalCases = (int) CountyData.getDataPack()[5];
        int Resolved = (int) CountyData.getDataPack()[6];
        int Deaths = (int) CountyData.getDataPack()[7];
        int Vaccinated = (int) CountyData.getDataPack()[8];
        int ClinicalCases = (int) CountyData.getDataPack()[9];
        int SubClinicalCases = (int) CountyData.getDataPack()[10];
        double CFR = CountyData.getDataPack()[11];
        int Immune = (int) CountyData.getDataPack()[12];
        int Tier = (int) CountyData.getDataPack()[13];
        int FlightConstant = (int) CountyData.getDataPack()[14];
        int ImportedCases = (int) CountyData.getDataPack()[15];
        int ExportedCases = (int) CountyData.getDataPack()[16];

        double[][] Immunity_by_category = findImmune(PastData, SeriesData);

        int Type = 0;
        if(Population>=10000){
            Type = 1;
        }

        double[][] Work = (double[][]) IO.MatricesByCategory[Type][0].get(IO.Countrycode.indexOf("CA"));
        double[][] School = (double[][]) IO.MatricesByCategory[Type][1].get(IO.Countrycode.indexOf("CA"));
        double[][] Home = (double[][]) IO.MatricesByCategory[Type][2].get(IO.Countrycode.indexOf("CA"));
        double[][] Other = (double[][]) IO.MatricesByCategory[Type] [3].get(IO.Countrycode.indexOf("CA"));

        double[][][] Mat = {Work,School,Home,Other};

        /**
         * Imported_cases will only increase the number of potential contacters, rather tha infected
         */

        ExportedCases = 0;
        int Total_Exported = 0;

        int Case_Exportation_Age_Band[] = new int[16];

        int DailyInfected = (int) CountyData.getDataPack()[17];
        int DailyExposed = (int) CountyData.getDataPack()[18];
        int DailyAcvtiveCases = (int) CountyData.getDataPack()[19];
        int DailyCritical = (int) CountyData.getDataPack()[20];
        int DailyResolved = (int) CountyData.getDataPack()[21];
        int DailyDeaths = (int) CountyData.getDataPack()[22];
        int DailyVaccinated= (int) CountyData.getDataPack()[23];

        double Total_NewInfected = 0;
        double Total_NewExposed = 0;
        double Total_NewCases = 0;
        double Total_NewCritical = 0;
        double Total_NewResolved = 0;
        double Total_NewDeaths = 0;

        double Total_RemovedExposed = 0;
        double Total_RemovedCases = 0;
        double Total_RemovedCritical = 0;
        //double Total_RemovedResolved = 0;

        ClinicalCases = 0;
        SubClinicalCases = 0;
        DailyInfected = 0;

        /**
         * Process patients who are already infected
         */


        Data ReturnData = new Data();


        /**
         * Find Immunity Matrix/Array
         */

        for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {

                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,1,CountyData.getDataPackByAge()[Variant][1][Patient_Age_Band]);//Population
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,3,CountyData.getDataPackByAge()[Variant][3][Patient_Age_Band]);//Exposed
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,4,CountyData.getDataPackByAge()[Variant][4][Patient_Age_Band]);//Active_cases
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,5,CountyData.getDataPackByAge()[Variant][5][Patient_Age_Band]);//Critical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,6,CountyData.getDataPackByAge()[Variant][6][Patient_Age_Band]);//Critical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,7,CountyData.getDataPackByAge()[Variant][7][Patient_Age_Band]);//Critical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,9,CountyData.getDataPackByAge()[Variant][9][Patient_Age_Band]);//Clinical
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,10,CountyData.getDataPackByAge()[Variant][10][Patient_Age_Band]);//Subclinical

                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,18,CountyData.getDataPackByAge()[Variant][18][Patient_Age_Band]);//New_Exposed
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,19,CountyData.getDataPackByAge()[Variant][19][Patient_Age_Band]);//New_Active_cases
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,20,CountyData.getDataPackByAge()[Variant][20][Patient_Age_Band]);//New_Critical_cases
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,21,CountyData.getDataPackByAge()[Variant][21][Patient_Age_Band]);//New_Resolved
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,22,CountyData.getDataPackByAge()[Variant][22][Patient_Age_Band]);//New_Death

            }
        }



        double ImmunityMat[] = new double[16];

        for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {
            double ShieldingImm = Immunity_by_category[Patient_Age_Band][0]/CountyData.getDataPackByAge()[0][1][Patient_Age_Band];
            ImmunityMat[Patient_Age_Band] = 1-ShieldingImm;
        }


        /**
         *  Patients who are newly infected
         */

        for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
            double Transmissible[] = new double[16];
            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {
                double Age_Band_Exposed = CountyData.getDataPackByAge()[Variant][3][Patient_Age_Band] + (Parameters.Workforce_Age_Dist[Patient_Age_Band] * ImportedCases);
                double EffectiveContacter = Math.round(findEffectiveContacter(SeriesData, Variant, Patient_Age_Band, Age_Band_Exposed) + 0.4);
                Transmissible[Patient_Age_Band] = EffectiveContacter;
            }

            double[] DailyInfectedArray = AgeDistribution.getNew_Incidence(Transmissible, ImmunityMat, Variant, Mat, Tier, 1);

            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {

                int Age_band_NewInfected = (int) (Math.round(DailyInfectedArray[Patient_Age_Band]));

                if(Age_band_NewInfected<0){
                    Age_band_NewInfected = 0;
                }
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,2,CountyData.getDataPackByAge()[Variant][2][Patient_Age_Band]+Age_band_NewInfected);
                ReturnData.setValueDataPackByAge(Variant,Patient_Age_Band,17, Age_band_NewInfected);
                Total_NewInfected += Age_band_NewInfected;
            }
        }

        DailyInfected += Math.round(Total_NewInfected);
        Infected += DailyInfected;


        /**
         * Vaccination
         */

        int Total_vaccinated_today = 0;

        for (int Age_band = 0; Age_band < 16; Age_band++) {

            /**
             * If an age band has more than 80% of its total population infected...
             */

            double Percentage_vaccinated = (CountyData.getDataPackByAge()[0][8][Age_band])/CountyData.getDataPackByAge()[0][1][Age_band];

            /*System.out.println(CountyDataIO.Counties[County].getName());
            System.out.println("Age Band: " + Parameters.AgeBand[Age_band]);
            System.out.println(CountyData.getDataPackByAge()[0][8][Age_band]);
            System.out.println(CountyData.getDataPackByAge()[0][1][Age_band]);*/


            int Age_band_vaccinated = (int) Math.round(Age_specific_vaccine_distribution[Type][Age_band] * Planned_vaccination);
            if(Percentage_vaccinated>=0.8){
                Age_band_vaccinated = 0;
            }

            Total_vaccinated_today += Age_band_vaccinated;

            ReturnData.setValueDataPackByAge(0,Age_band,23, CountyData.getDataPackByAge()[0][23][Age_band]+ Age_band_vaccinated);
            ReturnData.setValueDataPackByAge(0,Age_band,12,Age_band_vaccinated + CountyData.getDataPackByAge()[0][12][Age_band]);
            ReturnData.setValueDataPackByAge(0,Age_band,8,Age_band_vaccinated + CountyData.getDataPackByAge()[0][8][Age_band]);
        }

        Immune += Total_vaccinated_today;
        Vaccinated += Total_vaccinated_today;
        DailyVaccinated += Total_vaccinated_today;

        Immune += Total_NewInfected;

        ReturnData.setValueDataPack(0,Main.Day+1);
        ReturnData.setValueDataPack(1,Population);
        ReturnData.setValueDataPack(2,Infected);
        ReturnData.setValueDataPack(3,CountyData.getDataPack()[3]);
        ReturnData.setValueDataPack(4,CountyData.getDataPack()[4]);
        ReturnData.setValueDataPack(5,CountyData.getDataPack()[5]);
        ReturnData.setValueDataPack(6,CountyData.getDataPack()[6]);
        ReturnData.setValueDataPack(7,CountyData.getDataPack()[7]);
        ReturnData.setValueDataPack(8,Vaccinated);
        ReturnData.setValueDataPack(9,CountyData.getDataPack()[9]);
        ReturnData.setValueDataPack(10,CountyData.getDataPack()[10]);
        ReturnData.setValueDataPack(11,CFR);
        ReturnData.setValueDataPack(12,CountyData.getDataPack()[12]);
        ReturnData.setValueDataPack(13,Tier);
        ReturnData.setValueDataPack(14,FlightConstant);
        ReturnData.setValueDataPack(15,CountyData.getDataPack()[15]);
        ReturnData.setValueDataPack(16,CountyData.getDataPack()[16]);
        ReturnData.setValueDataPack(17,DailyInfected);
        ReturnData.setValueDataPack(18,Total_NewExposed);
        ReturnData.setValueDataPack(19,Total_NewCases);
        ReturnData.setValueDataPack(20,Total_NewCritical);
        ReturnData.setValueDataPack(21,Total_NewResolved);
        ReturnData.setValueDataPack(22,Total_NewDeaths);
        ReturnData.setValueDataPack(23,DailyVaccinated);

        return CountyData;
    }

    public static double findEffectiveContacter(double Exposed, double ClinicalCases, double SubClinicalCases){
        double Effective = Exposed * 0.5 + SubClinicalCases * 0.5 + ClinicalCases;
        return Effective;
    }

    public static double findEffectiveContacter(CountyDataArray DataPack, int Variant, int AgeBand, double Exposed){
        double Effective;
        double ClinicalCases = 0;
        double SubClinicalCases = 0;
        for (int i = 0; i < Math.min(3,Main.Day-5); i++) {
            double DailyIncidence = DataPack.getTimeSeries()[Main.Day-5+i].getDataPackByAge()[Variant][18][AgeBand];
            SubClinicalCases += DailyIncidence * Parameters.SubClinical_Ratio_By_Age[AgeBand];
            ClinicalCases += (DailyIncidence - SubClinicalCases);
        }
        Effective = ClinicalCases + SubClinicalCases * 0.5 + Exposed;
        return Effective;
    }

    public static  double findArraySum(double[] Array){
        double sum = 0;
        for (int i = 0; i < Array.length; i++) {
            sum +=Array[i];
        }
        return sum;
    }

    public static  double find2DArraySum(double[][] Array){
        double sum = 0;

        for (int i = 0; i < Array.length; i++) {
            for (int i1 = 0; i1 < Array[i].length; i1++) {
                sum +=Array[i][i1];
            }
        }
        return sum;
    }

    public static double[][] findImmune(CountyDataArray Pastdata, CountyDataArray DataPack){
        /**
         * Function immune
         */

        double[][] Immunity_level_by_age_and_category = new double[16][2];
        /**
         * 16 age bands
         * 2 Types: Mild and severe
         */
        for (int Ageband = 0; Ageband < 16; Ageband++) {
            double Immunity_level_against_infection_mild = 0;
            double Immunity_level_against_hospitalization_severe = 0;

            for (int date = 0; date < Pastdata.getLength(); date++) {
                double Vaccinated_on_day =  Pastdata.getTimeSeries()[date].getDataPackByAge()[0][23][Ageband];
                double Infected_on_day =  Pastdata.getTimeSeries()[date].getDataPackByAge()[0][17][Ageband];

                int date_index = Math.min(1500,Main.Day + Pastdata.getLength() - date);

                Immunity_level_against_infection_mild += Vaccinated_on_day * IO.Immunity_wane_mild.get(date_index);
                Immunity_level_against_infection_mild += Infected_on_day * IO.Immunity_wane_mild.get((date_index)/2);
                Immunity_level_against_hospitalization_severe += Vaccinated_on_day * IO.Immunity_wane_severe.get(date_index);
                Immunity_level_against_hospitalization_severe += Infected_on_day * IO.Immunity_wane_severe.get((date_index)/2);
            }

            for (int date = 0; date < Main.Day; date++) {
                double Vaccinated_on_day =  DataPack.getTimeSeries()[date].getDataPackByAge()[0][23][Ageband];
                double Infected_on_day =  DataPack.getTimeSeries()[date].getDataPackByAge()[0][17][Ageband];
                int date_index = Math.min(1500,Main.Day-date);
                Immunity_level_against_infection_mild += Vaccinated_on_day * IO.Immunity_wane_mild.get(date_index);
                Immunity_level_against_infection_mild += Infected_on_day * IO.Immunity_wane_mild.get((date_index)/2);
                Immunity_level_against_hospitalization_severe += Vaccinated_on_day * IO.Immunity_wane_severe.get(date_index);
                Immunity_level_against_hospitalization_severe += Infected_on_day * IO.Immunity_wane_severe.get((date_index)/2);
            }

            Immunity_level_by_age_and_category[Ageband] = new double[]{Immunity_level_against_infection_mild, Immunity_level_against_hospitalization_severe};
        }

        /**
         * Assuming the immunity gained through natural protection
         * wanes twice as slow as immunity gained through vaccination
         */

        return Immunity_level_by_age_and_category;
    }
    public static void set_data_package(){
        
    }
}
