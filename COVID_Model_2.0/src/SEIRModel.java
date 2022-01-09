import javax.swing.text.DefaultFormatterFactory;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Random;

public class SEIRModel{

    /**
     * The main model which outputs the number of cases after each iteration
     * @param PastData
     * @param SeriesData
     * @param CountyData
     * @param Imported
     * @param Exported
     * @param Responsetier
     * @param County
     * @param Local_workers
     * @param Planned_vaccination
     * @param Age_specific_vaccine_distribution
     * @return
     */

    public static Data RunModel(CountyDataArray PastData,CountyDataArray SeriesData,Data CountyData, int Imported, int Exported, int Responsetier, int County, int Local_workers, int Planned_vaccination, double[][] Age_specific_vaccine_distribution){

        int total_population = (int) CountyData.getPopulation();
        int total_infected = (int) CountyData.getIncidence();
        int total_exposed = (int) CountyData.getExposed();
        int total_active_cases = (int) CountyData.getActive();
        int total_critical_cases = (int) CountyData.getCritical();
        int total_resolved = (int) CountyData.getResolved();
        int total_deaths = (int) CountyData.getDeaths();
        int total_vaccinated = (int) CountyData.getVaccinated();
        int total_clinical_cases = (int) CountyData.getClinical();
        int total_sub_clinical_cases = (int) CountyData.getSub_clinical();
        double total_CFR = CountyData.getCFR();
        double total_immunity_ratio = CountyData.getImmunity();
        int total_tier = (int) CountyData.getTier();
        int total_commute_coeff = (int) CountyData.getCommute_coeff();
        int total_imported_cases = (int) CountyData.getImported();
        int total_exported_cases = (int) CountyData.getExported();

        int Type = 0;
        if(total_population>=10000){
            Type = 1;
        }

        double[][] Work = (double[][]) Input.MatricesByCategory[Type][0].get(Input.Countrycode.indexOf("CA"));
        double[][] School = (double[][]) Input.MatricesByCategory[Type][1].get(Input.Countrycode.indexOf("CA"));
        double[][] Home = (double[][]) Input.MatricesByCategory[Type][2].get(Input.Countrycode.indexOf("CA"));
        double[][] Other = (double[][]) Input.MatricesByCategory[Type] [3].get(Input.Countrycode.indexOf("CA"));

        double[][][] contact_matrices = {Work,School,Home,Other};

        /**
         * Imported_cases will only increase the number of potential contacters, rather than infected
         */

        int Total_Exported = 0;

        int[] case_exportation_by_age = new int[16];


        int Dailyvaccinated = 0;

        double Total_Newinfected = 0;
        double Total_Newexposed = 0;
        double Total_NewCases = 0;
        double Total_NewCritical = 0;
        double Total_Newresolved = 0;
        double Total_Newdeaths = 0;

        double Total_Removedexposed = 0;
        double Total_RemovedCases = 0;
        double Total_RemovedCritical = 0;
        //double Total_Removedresolved = 0;

        double[][] Immunity_by_category = findImmuned_ratio(PastData, SeriesData);


        /**
         * Process patients who are already infected
         */

        Data new_data = new Data();

        for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {


                /**
                 * Add cases
                 */

                double basic_CFR =  Parameters.CFR[Patient_Age_Band];
                double variant_boost = Parameters.CFR_By_Variant[Variant];

                double Specific_CFR = basic_CFR * variant_boost;

                double Age_Band_Newexposed = 0;
                double Age_Band_NewCases = 0;
                double Age_Band_NewCritical = 0;
                double Age_Band_Newresolved = 0;
                double Age_Band_Newdeaths = 0;

                double Age_Band_Removedexposed = 0;
                double Age_Band_RemovedCases = 0;
                double Age_Band_RemovedCritical = 0;

                double CFR_true = 1 - Parameters.CFR[Patient_Age_Band] * Immunity_by_category[Patient_Age_Band][0];//Add the CFR difference of different variants

                if(Main.Day>=3){
                    Age_Band_Newexposed = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-3].getDataPackByAge()[Variant][17][Patient_Age_Band]);
                }
                if (Main.Day>=6) {
                    Age_Band_NewCases = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-3].getDataPackByAge()[Variant][18][Patient_Age_Band]);
                    Age_Band_Removedexposed = Age_Band_NewCases;
                }
                if(Main.Day>=8){
                    Age_Band_NewCritical = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-5].getDataPackByAge()[Variant][19][Patient_Age_Band] * (Parameters.CriticalRate[Patient_Age_Band]));
                }
                if(Main.Day>=14){
                    Age_Band_RemovedCritical = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-14].getDataPackByAge()[Variant][19][Patient_Age_Band] * (Parameters.CriticalRate[Patient_Age_Band]));
                }
                if(Main.Day>=15){
                    Age_Band_Newresolved = Math.round((SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[Variant][19][Patient_Age_Band] * (1.0 - Specific_CFR)) - 0.3);
                    Age_Band_Newdeaths = Math.round(0.3 + (SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[Variant][19][Patient_Age_Band] * Specific_CFR));
                    Age_Band_RemovedCases = SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[Variant][19][Patient_Age_Band];
                }

                int Age_Band_exposed = (int) Math.round(CountyData.getDataPackByAge()[Variant][3][Patient_Age_Band] + Age_Band_Newexposed - Age_Band_NewCases);
                int Age_Band_active_cases = (int) Math.round((CountyData.getDataPackByAge()[Variant][4][Patient_Age_Band] + Age_Band_NewCases - Age_Band_RemovedCases));
                int Age_Band_critical_cases = (int) Math.round(CountyData.getDataPackByAge()[Variant][5][Patient_Age_Band] + Age_Band_NewCritical - Age_Band_RemovedCritical);
                int Age_Band_resolved = (int) (CountyData.getDataPackByAge()[Variant][6][Patient_Age_Band] + Age_Band_Newresolved);
                int Age_Band_deaths = (int) (CountyData.getDataPackByAge()[Variant][7][Patient_Age_Band] + Age_Band_Newdeaths);

                int Age_Band_SubclinicalCases = (int) Math.round(Parameters.SubClinical_Ratio_By_Age[Patient_Age_Band] * Age_Band_active_cases);
                int Age_Band_clinical_cases = Age_Band_active_cases - Age_Band_SubclinicalCases;

                int setter_preset_1[] = {3,4,5,6,7,9};
                double data[] = {Age_Band_exposed, };
                set_new_data(new_data, setter_preset_1, data);
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,3,Age_Band_exposed);//exposed
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,4,Age_Band_active_cases);//Active_cases
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,5,Age_Band_critical_cases);//Critical
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,6,Age_Band_resolved);//Critical
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,7,Age_Band_deaths);//Critical
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,9,Age_Band_clinical_cases);//Clinical565

                Total_Newexposed += Age_Band_Newexposed;
                Total_NewCases += Age_Band_NewCases;
                Total_NewCritical += Age_Band_NewCritical;
                Total_Newresolved += Age_Band_Newresolved;
                Total_Newdeaths += Age_Band_Newdeaths;

                Total_Removedexposed += Age_Band_Removedexposed;
                Total_RemovedCases += Age_Band_RemovedCases;
                Total_RemovedCritical += Age_Band_RemovedCritical;

                clinical_cases += Age_Band_clinical_cases;
                sub_clinical_cases += Age_Band_SubclinicalCases;
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

        for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
            double Transmissible[] = new double[16];
            for (int age_band = 0; age_band < 16; age_band++) {
                double local_exposed = CountyData.getExposed(variant, age_band);
                double imported_exposed = imported_cases * Parameters.Workforce_Age_Dist[age_band];
                double age_band_transmission = local_exposed + imported_exposed;
                double transmissible = Math.round(findEffectiveContacter(SeriesData, variant, age_band, age_band_transmission)+0.4);
                Transmissible[age_band] = transmissible;
            }

            /**
             * Number of individuals newly infected
             */

            double[] daily_incidence_by_age = AgeDistribution.getNew_Incidence(Transmissible, ImmunityMat, variant, contact_matrices, tier, 0);

            double Total_Workers;

            if (Local_workers<=0){
                Local_workers = (int) (((double)population) * Parameters.Work_force_ratio);
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

                int For_Export = (int) ((DailyinfectedArray[Patient_Age_Band]) * ratio);



                int Age_band_Newinfected = (int) (Math.round(DailyinfectedArray[Patient_Age_Band] - For_Export));

                if(Age_band_Newinfected<0){
                    Age_band_Newinfected = 0;
                }

                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,2,CountyData.getDataPackByAge()[Variant][2][Patient_Age_Band]+Age_band_Newinfected);
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,17, Age_band_Newinfected);
                Total_Newinfected += Age_band_Newinfected;

                Exported_cases += For_Export;
                Total_Exported += For_Export;
            }

        }

        Dailyinfected += Math.round(Total_Newinfected);
        infected += Dailyinfected;

        /**Add new cases to total numbers*/

        exposed = (int) Math.round(exposed + Total_Newexposed - Total_Removedexposed);
        active_cases = (int) Math.round(active_cases + Total_NewCases - Total_RemovedCases);
        critical_cases = (int) Math.round((critical_cases + Total_NewCritical - Total_RemovedCritical));
        resolved += Total_Newresolved;
        deaths += Total_Newdeaths;

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

            new_data.setValueDataPackByAge(0,Age_band,23, Age_band_vaccinated);
            new_data.setValueDataPackByAge(0,Age_band,12,Age_band_vaccinated + CountyData.getDataPackByAge()[0][12][Age_band]);
            new_data.setValueDataPackByAge(0,Age_band,8,Age_band_vaccinated + CountyData.getDataPackByAge()[0][8][Age_band]);
        }

        immunity_ratio += Total_vaccinated_today;
        vaccinated += Total_vaccinated_today;
        Dailyvaccinated += Total_vaccinated_today;

        immunity_ratio += Total_Newinfected;


        new_data.setValueDataPack(0,Main.Day+1);
        new_data.setValueDataPack(1,population);
        new_data.setValueDataPack(2,infected);
        new_data.setValueDataPack(3,exposed);
        new_data.setValueDataPack(4,active_cases);
        new_data.setValueDataPack(5,critical_cases);
        new_data.setValueDataPack(6,resolved);
        new_data.setValueDataPack(7,deaths);
        new_data.setValueDataPack(8,vaccinated);
        new_data.setValueDataPack(9,clinical_cases);
        new_data.setValueDataPack(10,sub_clinical_cases);
        new_data.setValueDataPack(11,CFR);
        new_data.setValueDataPack(12,immunity_ratio);
        new_data.setValueDataPack(13,tier);
        new_data.setValueDataPack(14,commute_coeff);
        new_data.setValueDataPack(15,ImportedCases);
        new_data.setValueDataPack(16,ExportedCases);
        new_data.setValueDataPack(17,Dailyinfected);
        new_data.setValueDataPack(18,Total_Newexposed);
        new_data.setValueDataPack(19,Total_NewCases);
        new_data.setValueDataPack(20,Total_NewCritical);
        new_data.setValueDataPack(21,Total_Newresolved);
        new_data.setValueDataPack(22,Total_Newdeaths);
        new_data.setValueDataPack(23,Dailyvaccinated);

        return new_data;
    }

    public static Data NightModel(CountyDataArray PastData,CountyDataArray SeriesData,Data CountyData, int Imported, int Exported, int Responsetier, int County, int Planned_vaccination, double[][] Age_specific_vaccine_distribution){
        int Date = Main.Day;

        int population = (int) CountyData.getEpidemiological_data_total()[1];
        int infected = (int) CountyData.getEpidemiological_data_total()[2];
        int exposed = (int) CountyData.getEpidemiological_data_total()[3];
        int active_cases = (int) CountyData.getEpidemiological_data_total()[4];
        int critical_cases = (int) CountyData.getEpidemiological_data_total()[5];
        int resolved = (int) CountyData.getEpidemiological_data_total()[6];
        int deaths = (int) CountyData.getEpidemiological_data_total()[7];
        int vaccinated = (int) CountyData.getEpidemiological_data_total()[8];
        int clinical_cases = (int) CountyData.getEpidemiological_data_total()[9];
        int sub_clinical_cases = (int) CountyData.getEpidemiological_data_total()[10];
        double CFR = CountyData.getEpidemiological_data_total()[11];
        int immunity_ratio = (int) CountyData.getEpidemiological_data_total()[12];
        int tier = (int) CountyData.getEpidemiological_data_total()[13];
        int commute_coeff = (int) CountyData.getEpidemiological_data_total()[14];
        int ImportedCases = (int) CountyData.getEpidemiological_data_total()[15];
        int ExportedCases = (int) CountyData.getEpidemiological_data_total()[16];

        double[][] Immunity_by_category = findImmuned_ratio(PastData, SeriesData);

        int Type = 0;
        if(population>=10000){
            Type = 1;
        }

        double[][] Work = (double[][]) Input.MatricesByCategory[Type][0].get(Input.Countrycode.indexOf("CA"));
        double[][] School = (double[][]) Input.MatricesByCategory[Type][1].get(Input.Countrycode.indexOf("CA"));
        double[][] Home = (double[][]) Input.MatricesByCategory[Type][2].get(Input.Countrycode.indexOf("CA"));
        double[][] Other = (double[][]) Input.MatricesByCategory[Type] [3].get(Input.Countrycode.indexOf("CA"));

        double[][][] Mat = {Work,School,Home,Other};

        /**
         * Imported_cases will only increase the number of potential contacters, rather tha infected
         */

        ExportedCases = 0;
        int Total_Exported = 0;

        int Case_Exportation_Age_Band[] = new int[16];

        int Dailyinfected = (int) CountyData.getEpidemiological_data_total()[17];
        int Dailyexposed = (int) CountyData.getEpidemiological_data_total()[18];
        int DailyAcvtiveCases = (int) CountyData.getEpidemiological_data_total()[19];
        int DailyCritical = (int) CountyData.getEpidemiological_data_total()[20];
        int Dailyresolved = (int) CountyData.getEpidemiological_data_total()[21];
        int Dailydeaths = (int) CountyData.getEpidemiological_data_total()[22];
        int Dailyvaccinated= (int) CountyData.getEpidemiological_data_total()[23];

        double Total_Newinfected = 0;
        double Total_Newexposed = 0;
        double Total_NewCases = 0;
        double Total_NewCritical = 0;
        double Total_Newresolved = 0;
        double Total_Newdeaths = 0;

        double Total_Removedexposed = 0;
        double Total_RemovedCases = 0;
        double Total_RemovedCritical = 0;
        //double Total_Removedresolved = 0;

        clinical_cases = 0;
        sub_clinical_cases = 0;
        Dailyinfected = 0;

        /**
         * Process patients who are already infected
         */


        Data new_data = new Data();


        /**
         * Find Immunity Matrix/Array
         */

        for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {

                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,1,CountyData.getDataPackByAge()[Variant][1][Patient_Age_Band]);//population
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,3,CountyData.getDataPackByAge()[Variant][3][Patient_Age_Band]);//exposed
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,4,CountyData.getDataPackByAge()[Variant][4][Patient_Age_Band]);//Active_cases
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,5,CountyData.getDataPackByAge()[Variant][5][Patient_Age_Band]);//Critical
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,6,CountyData.getDataPackByAge()[Variant][6][Patient_Age_Band]);//Critical
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,7,CountyData.getDataPackByAge()[Variant][7][Patient_Age_Band]);//Critical
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,9,CountyData.getDataPackByAge()[Variant][9][Patient_Age_Band]);//Clinical
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,10,CountyData.getDataPackByAge()[Variant][10][Patient_Age_Band]);//Subclinical

                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,18,CountyData.getDataPackByAge()[Variant][18][Patient_Age_Band]);//New_exposed
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,19,CountyData.getDataPackByAge()[Variant][19][Patient_Age_Band]);//New_Active_cases
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,20,CountyData.getDataPackByAge()[Variant][20][Patient_Age_Band]);//New_Critical_cases
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,21,CountyData.getDataPackByAge()[Variant][21][Patient_Age_Band]);//New_resolved
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,22,CountyData.getDataPackByAge()[Variant][22][Patient_Age_Band]);//New_Death

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
                double Age_Band_exposed = CountyData.getDataPackByAge()[Variant][3][Patient_Age_Band] + (Parameters.Workforce_Age_Dist[Patient_Age_Band] * ImportedCases);
                double EffectiveContacter = Math.round(findEffectiveContacter(SeriesData, Variant, Patient_Age_Band, Age_Band_exposed) + 0.4);
                Transmissible[Patient_Age_Band] = EffectiveContacter;
            }

            double[] DailyinfectedArray = AgeDistribution.getNew_Incidence(Transmissible, ImmunityMat, Variant, Mat, tier, 1);

            for (int Patient_Age_Band = 0; Patient_Age_Band < 16; Patient_Age_Band++) {

                int Age_band_Newinfected = (int) (Math.round(DailyinfectedArray[Patient_Age_Band]));

                if(Age_band_Newinfected<0){
                    Age_band_Newinfected = 0;
                }
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,2,CountyData.getDataPackByAge()[Variant][2][Patient_Age_Band]+Age_band_Newinfected);
                new_data.setValueDataPackByAge(Variant,Patient_Age_Band,17, Age_band_Newinfected);
                Total_Newinfected += Age_band_Newinfected;
            }
        }

        Dailyinfected += Math.round(Total_Newinfected);
        infected += Dailyinfected;


        /**
         * Vaccination
         */

        int Total_vaccinated_today = 0;

        for (int Age_band = 0; Age_band < 16; Age_band++) {

            /**
             * If an age band has more than 80% of its total population infected...
             */

            double Percentage_vaccinated = (CountyData.getDataPackByAge()[0][8][Age_band])/CountyData.getDataPackByAge()[0][1][Age_band];

            int Age_band_vaccinated = (int) Math.round(Age_specific_vaccine_distribution[Type][Age_band] * Planned_vaccination);
            if(Percentage_vaccinated>=0.8){
                Age_band_vaccinated = 0;
            }

            Total_vaccinated_today += Age_band_vaccinated;

            new_data.setValueDataPackByAge(0,Age_band,23, CountyData.getDataPackByAge()[0][23][Age_band]+ Age_band_vaccinated);
            new_data.setValueDataPackByAge(0,Age_band,12,Age_band_vaccinated + CountyData.getDataPackByAge()[0][12][Age_band]);
            new_data.setValueDataPackByAge(0,Age_band,8,Age_band_vaccinated + CountyData.getDataPackByAge()[0][8][Age_band]);
        }

        immunity_ratio += Total_vaccinated_today;
        vaccinated += Total_vaccinated_today;
        Dailyvaccinated += Total_vaccinated_today;

        immunity_ratio += Total_Newinfected;

        new_data.setValueDataPack(0,Main.Day+1);
        new_data.setValueDataPack(1,population);
        new_data.setValueDataPack(2,infected);
        new_data.setValueDataPack(3,CountyData.getEpidemiological_data_total()[3]);
        new_data.setValueDataPack(4,CountyData.getEpidemiological_data_total()[4]);
        new_data.setValueDataPack(5,CountyData.getEpidemiological_data_total()[5]);
        new_data.setValueDataPack(6,CountyData.getEpidemiological_data_total()[6]);
        new_data.setValueDataPack(7,CountyData.getEpidemiological_data_total()[7]);
        new_data.setValueDataPack(8,vaccinated);
        new_data.setValueDataPack(9,CountyData.getEpidemiological_data_total()[9]);
        new_data.setValueDataPack(10,CountyData.getEpidemiological_data_total()[10]);
        new_data.setValueDataPack(11,CFR);
        new_data.setValueDataPack(12,CountyData.getEpidemiological_data_total()[12]);
        new_data.setValueDataPack(13,tier);
        new_data.setValueDataPack(14,commute_coeff);
        new_data.setValueDataPack(15,CountyData.getEpidemiological_data_total()[15]);
        new_data.setValueDataPack(16,CountyData.getEpidemiological_data_total()[16]);
        new_data.setValueDataPack(17,Dailyinfected);
        new_data.setValueDataPack(18,Total_Newexposed);
        new_data.setValueDataPack(19,Total_NewCases);
        new_data.setValueDataPack(20,Total_NewCritical);
        new_data.setValueDataPack(21,Total_Newresolved);
        new_data.setValueDataPack(22,Total_Newdeaths);
        new_data.setValueDataPack(23,Dailyvaccinated);

        return CountyData;
    }

    public static double findEffectiveContacter(double exposed, double clinical_cases, double sub_clinical_cases){
        double Effective = exposed * 0.5 + sub_clinical_cases * 0.5 + clinical_cases;
        return Effective;
    }

    public static double findEffectiveContacter(CountyDataArray DataPack, int Variant, int AgeBand, double exposed){
        double Effective;
        double clinical_cases = 0;
        double sub_clinical_cases = 0;
        for (int i = 0; i < Math.min(3,Main.Day-5); i++) {
            double DailyIncidence = DataPack.getTimeSeries()[Main.Day-5+i].getDataPackByAge()[Variant][18][AgeBand];
            sub_clinical_cases += DailyIncidence * Parameters.SubClinical_Ratio_By_Age[AgeBand];
            clinical_cases += (DailyIncidence - sub_clinical_cases);
        }
        Effective = clinical_cases + sub_clinical_cases * 0.5 + exposed;
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

    public static double[][] findImmuned_ratio(CountyDataArray Pastdata, CountyDataArray DataPack){
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
                double vaccinated_on_day =  Pastdata.getTimeSeries()[date].getDataPackByAge()[0][23][Ageband];
                double infected_on_day =  Pastdata.getTimeSeries()[date].getDataPackByAge()[0][17][Ageband];

                int date_index = Math.min(1500,Main.Day + Pastdata.getLength() - date);

                Immunity_level_against_infection_mild += vaccinated_on_day * Input.Immunity_wane_mild.get(date_index);
                Immunity_level_against_infection_mild += infected_on_day * Input.Immunity_wane_mild.get((date_index)/2);
                Immunity_level_against_hospitalization_severe += vaccinated_on_day * Input.Immunity_wane_severe.get(date_index);
                Immunity_level_against_hospitalization_severe += infected_on_day * Input.Immunity_wane_severe.get((date_index)/2);
            }

            for (int date = 0; date < Main.Day; date++) {
                double vaccinated_on_day =  DataPack.getTimeSeries()[date].getDataPackByAge()[0][23][Ageband];
                double infected_on_day =  DataPack.getTimeSeries()[date].getDataPackByAge()[0][17][Ageband];
                int date_index = Math.min(1500,Main.Day-date);
                Immunity_level_against_infection_mild += vaccinated_on_day * Input.Immunity_wane_mild.get(date_index);
                Immunity_level_against_infection_mild += infected_on_day * Input.Immunity_wane_mild.get((date_index)/2);
                Immunity_level_against_hospitalization_severe += vaccinated_on_day * Input.Immunity_wane_severe.get(date_index);
                Immunity_level_against_hospitalization_severe += infected_on_day * Input.Immunity_wane_severe.get((date_index)/2);
            }

            Immunity_level_by_age_and_category[Ageband] = new double[]{Immunity_level_against_infection_mild, Immunity_level_against_hospitalization_severe};
        }

        /**
         * Assuming the immunity gained through natural protection
         * wanes twice as slow as immunity gained through vaccination
         */

        return Immunity_level_by_age_and_category;
    }

    public static void set_new_data(Data new_data, int[] indices, double[] data){
        
    }
}
