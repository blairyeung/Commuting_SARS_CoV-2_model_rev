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

        double total_new_daily_incidence = 0;
        double total_new_daily_exposed = 0;
        double total_new_daily_cases = 0;
        double total_new_daily_critical = 0;
        double total_new_daily_resolved = 0;
        double total_new_daily_deaths = 0;

        double total_daily_removed_exposed = 0;
        double total_daily_removed_cases = 0;
        double total_daily_removed_critical = 0;

        double[][] immunity_by_category = findImmuned_ratio(PastData, SeriesData);


        /**
         * Process patients who are already infected
         */

        Data new_data = new Data();

        for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
            for (int patient_age = 0; patient_age < 16; patient_age++) {


                /**
                 * Add cases
                 */

                double basic_CFR =  Parameters.CFR[patient_age];
                double variant_boost = Parameters.CFR_By_Variant[variant];

                double Specific_CFR = basic_CFR * variant_boost;

                double new_daily_exposed = 0;
                double new_daily_cases = 0;
                double new_daily_critical = 0;
                double new_daily_resolved = 0;
                double new_daily_deaths = 0;

                double z = 0;
                double new_removed_exposed = 0;
                double new_removed_cases = 0;
                double new_removed_critical = 0;

                double CFR_true = 1 - Parameters.CFR[patient_age] * immunity_by_category[patient_age][0];//Add the CFR difference of different variants

                if(Main.Day>=3){
                    new_daily_exposed = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-3].getDaily_incidence(variant,patient_age));
                }
                if (Main.Day>=6) {
                    new_daily_cases = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-3].getDaily_exposed(variant,patient_age));
                    new_removed_exposed = new_daily_cases;
                }
                if(Main.Day>=8){
                    new_daily_critical = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-5].getDataPackByAge()[variant][19][patient_age] * (Parameters.CriticalRate[patient_age]));
                }
                if(Main.Day>=14){
                    new_removed_critical = (int) Math.round(SeriesData.getTimeSeries()[Main.Day-14].getDataPackByAge()[variant][19][patient_age] * (Parameters.CriticalRate[patient_age]));
                }
                if(Main.Day>=15){
                    //new_daily_critical = Math.round((SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[variant][19][patient_age] * (1.0 - Specific_CFR)) - 0.3);
                    new_daily_deaths = Math.round(0.3 + (SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[variant][19][patient_age] * Specific_CFR));
                    new_removed_cases = SeriesData.getTimeSeries()[Main.Day-9].getDataPackByAge()[variant][19][patient_age];
                }

                int exposed = (int) Math.round(CountyData.getDataPackByAge()[variant][3][patient_age] + new_daily_exposed - new_daily_cases);
                int cases = (int) Math.round((CountyData.getDataPackByAge()[variant][4][patient_age] + new_daily_cases - new_removed_cases));
                int critical = (int) Math.round(CountyData.getDataPackByAge()[variant][5][patient_age] + new_daily_critical - new_removed_critical);
                int resolved = (int) (CountyData.getDataPackByAge()[variant][6][patient_age] + new_daily_resolved);
                int deaths = (int) (CountyData.getDataPackByAge()[variant][7][patient_age] + new_daily_deaths);

                int sub_clinical_cases = (int) Math.round(Parameters.SubClinical_Ratio_By_Age[patient_age] * cases);
                int clinical_cases = cases - sub_clinical_cases;

                int setter_preset_1[] = {3,4,5,6,7,9,10};
                double data[] = {exposed, cases, critical, resolved, deaths, clinical_cases, sub_clinical_cases};
                set_new_data(new_data, setter_preset_1, data, variant, patient_age);

                total_new_daily_exposed += new_daily_exposed;
                total_new_daily_cases += new_daily_cases;
                total_new_daily_critical += new_daily_critical;
                total_new_daily_resolved += new_daily_resolved;
                total_new_daily_deaths += new_daily_deaths;

                total_daily_removed_cases += new_removed_cases;
                total_daily_removed_critical  += new_removed_critical;

                clinical_cases += clinical_cases;
                sub_clinical_cases += sub_clinical_cases;
            }
        }

        /**
            * Find Immunity Matrix/Array
         */


        double ImmunityMat[] = new double[16];

        for (int patient_age = 0; patient_age < 16; patient_age++) {
            double ShieldingImm = immunity_by_category[patient_age][0]/CountyData.getDataPackByAge()[0][1][patient_age];
            ImmunityMat[patient_age] = 1-ShieldingImm;
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

            for (int patient_age = 0; patient_age < 16; patient_age++) {

                /**
                 * Export cases by age band (REF)
                 */

                int For_Export = (int) ((DailyinfectedArray[patient_age]) * ratio);



                int Age_band_Newinfected = (int) (Math.round(DailyinfectedArray[patient_age] - For_Export));

                if(Age_band_Newinfected<0){
                    Age_band_Newinfected = 0;
                }

                new_data.setValueDataPackByAge(variant,patient_age,2,CountyData.getDataPackByAge()[variant][2][patient_age]+Age_band_Newinfected);
                new_data.setValueDataPackByAge(variant,patient_age,17, Age_band_Newinfected);
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

        for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
            for (int patient_age = 0; patient_age < 16; patient_age++) {

                new_data.setValueDataPackByAge(variant,patient_age,1,CountyData.getDataPackByAge()[variant][1][patient_age]);//population
                new_data.setValueDataPackByAge(variant,patient_age,3,CountyData.getDataPackByAge()[variant][3][patient_age]);//exposed
                new_data.setValueDataPackByAge(variant,patient_age,4,CountyData.getDataPackByAge()[variant][4][patient_age]);//Active_cases
                new_data.setValueDataPackByAge(variant,patient_age,5,CountyData.getDataPackByAge()[variant][5][patient_age]);//Critical
                new_data.setValueDataPackByAge(variant,patient_age,6,CountyData.getDataPackByAge()[variant][6][patient_age]);//Critical
                new_data.setValueDataPackByAge(variant,patient_age,7,CountyData.getDataPackByAge()[variant][7][patient_age]);//Critical
                new_data.setValueDataPackByAge(variant,patient_age,9,CountyData.getDataPackByAge()[variant][9][patient_age]);//Clinical
                new_data.setValueDataPackByAge(variant,patient_age,10,CountyData.getDataPackByAge()[variant][10][patient_age]);//Subclinical

                new_data.setValueDataPackByAge(variant,patient_age,18,CountyData.getDataPackByAge()[variant][18][patient_age]);//New_exposed
                new_data.setValueDataPackByAge(variant,patient_age,19,CountyData.getDataPackByAge()[variant][19][patient_age]);//New_Active_cases
                new_data.setValueDataPackByAge(variant,patient_age,20,CountyData.getDataPackByAge()[variant][20][patient_age]);//New_Critical_cases
                new_data.setValueDataPackByAge(variant,patient_age,21,CountyData.getDataPackByAge()[variant][21][patient_age]);//New_resolved
                new_data.setValueDataPackByAge(variant,patient_age,22,CountyData.getDataPackByAge()[variant][22][patient_age]);//New_Death

            }
        }



        double ImmunityMat[] = new double[16];

        for (int patient_age = 0; patient_age < 16; patient_age++) {
            double ShieldingImm = Immunity_by_category[patient_age][0]/CountyData.getDataPackByAge()[0][1][patient_age];
            ImmunityMat[patient_age] = 1-ShieldingImm;
        }


        /**
         *  Patients who are newly infected
         */

        for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
            double Transmissible[] = new double[16];
            for (int patient_age = 0; patient_age < 16; patient_age++) {
                double Age_Band_exposed = CountyData.getDataPackByAge()[variant][3][patient_age] + (Parameters.Workforce_Age_Dist[patient_age] * ImportedCases);
                double EffectiveContacter = Math.round(findEffectiveContacter(SeriesData, variant, patient_age, Age_Band_exposed) + 0.4);
                Transmissible[patient_age] = EffectiveContacter;
            }

            double[] DailyinfectedArray = AgeDistribution.getNew_Incidence(Transmissible, ImmunityMat, variant, Mat, tier, 1);

            for (int patient_age = 0; patient_age < 16; patient_age++) {

                int Age_band_Newinfected = (int) (Math.round(DailyinfectedArray[patient_age]));

                if(Age_band_Newinfected<0){
                    Age_band_Newinfected = 0;
                }
                new_data.setValueDataPackByAge(variant,patient_age,2,CountyData.getDataPackByAge()[variant][2][patient_age]+Age_band_Newinfected);
                new_data.setValueDataPackByAge(variant,patient_age,17, Age_band_Newinfected);
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

    public static double findEffectiveContacter(CountyDataArray DataPack, int variant, int AgeBand, double exposed){
        double Effective;
        double clinical_cases = 0;
        double sub_clinical_cases = 0;
        for (int i = 0; i < Math.min(3,Main.Day-5); i++) {
            double DailyIncidence = DataPack.getTimeSeries()[Main.Day-5+i].getDataPackByAge()[variant][18][AgeBand];
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

    public static void set_new_data(Data new_data, int[] indices, double[] data, int variant, int age){
        if (indices.length==data.length) {
            for (int i = 0; i < data.length; i++) {
                new_data.setValueDataPackByAge(variant, age, indices[i], data[i]);
            }
        }
    }

    public static void set_new_data(Data new_data, int[] indices, double[] data){
        if (indices.length==data.length) {
            for (int i = 0; i < data.length; i++) {
                new_data.setValueDataPack(indices[i], data[i]);
            }
        }
    }
}
