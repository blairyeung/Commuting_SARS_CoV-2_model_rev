import javax.swing.text.DefaultFormatterFactory;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

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

        /**
         * Epidemiological parameter declaration
         */

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

        /**
         * Contact matrices
         */

        double[][] work_matrix = (double[][]) Input.MatricesByCategory[Type][0].get(Input.Countrycode.indexOf("CA"));
        double[][] school_matrix = (double[][]) Input.MatricesByCategory[Type][1].get(Input.Countrycode.indexOf("CA"));
        double[][] home_matrix = (double[][]) Input.MatricesByCategory[Type][2].get(Input.Countrycode.indexOf("CA"));
        double[][] other_matrix = (double[][]) Input.MatricesByCategory[Type] [3].get(Input.Countrycode.indexOf("CA"));

        double[][][] contact_matrices = {work_matrix,school_matrix,home_matrix,other_matrix};

        /**
         * Imported_cases will only increase the number of potential contacters, rather than infected
         */

        int Total_Exported = 0;

        int[] case_exportation_by_age = new int[16];

        double total_new_daily_incidence = 0;
        double total_new_daily_exposed = 0;
        double total_new_daily_cases = 0;
        double total_new_daily_critical = 0;
        double total_new_daily_resolved = 0;
        double total_new_daily_deaths = 0;

        double total_daily_removed_exposed = 0;
        double total_daily_removed_cases = 0;
        double total_daily_removed_critical = 0;

        /**
         * Epidemiological parameter declaration End
         */

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

                total_clinical_cases += clinical_cases;
                total_sub_clinical_cases += sub_clinical_cases;
            }
        }

        /**
            * Find Immunity Matrix/Array
         */


        double immunity_vector[] = new double[16];

        for (int patient_age = 0; patient_age < 16; patient_age++) {
            double ShieldingImm = immunity_by_category[patient_age][0]/CountyData.getDataPackByAge()[0][1][patient_age];
            immunity_vector[patient_age] = 1-ShieldingImm;
        }

        /**
         *  Patients who are newly infected
         */

        for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
            double transmission_vector[] = new double[16];
            for (int age_band = 0; age_band < 16; age_band++) {
                double local_exposed = CountyData.getExposed(variant, age_band);
                double imported_exposed = total_imported_cases * Parameters.Workforce_Age_Dist[age_band];
                double age_band_transmission = local_exposed + imported_exposed;
                double transmissible = Math.round(findEffectiveContacter(SeriesData, variant, age_band, age_band_transmission)+0.4);
                transmission_vector[age_band] = transmissible;
            }

            /**
             * Number of individuals newly infected
             */

            double[] daily_incidence_by_age = AgeDistribution.getNew_Incidence(transmission_vector, immunity_vector, variant, contact_matrices, total_tier, 0);

            double total_workers;

            if (Local_workers<=0){
                Local_workers = (int) (((double)total_population) * Parameters.Work_force_ratio);
            }

            total_workers = Math.round(((double)(Local_workers + Imported)));

            double ratio = ((double) Imported)/ total_workers;

            for (int patient_age = 0; patient_age < 16; patient_age++) {

                /**
                 * Export cases by age band (REF)
                 */

                int For_Export = (int) ((daily_incidence_by_age[patient_age]) * ratio);

                int daily_incidence = (int) (Math.round(daily_incidence_by_age[patient_age] - For_Export));

                if(daily_incidence<0){
                    daily_incidence = 0;
                }

                int indices[] = {2,17};
                double data[] = {total_infected + total_new_daily_incidence, total_new_daily_incidence};
                set_new_data(new_data, indices, data, variant, patient_age);
                total_new_daily_incidence += daily_incidence;

                total_exported_cases += For_Export;
                Total_Exported += For_Export;
            }

        }


        total_infected += total_new_daily_incidence;

        /**Add new cases to total numbers*/

        total_exposed = (int) Math.round(total_exposed + total_new_daily_exposed - total_new_daily_cases);
        total_active_cases = (int) Math.round(total_active_cases + total_new_daily_cases - total_daily_removed_cases);
        total_critical_cases = (int) Math.round((total_critical_cases + total_new_daily_critical - total_daily_removed_critical));
        total_resolved += total_new_daily_resolved;
        total_deaths += total_new_daily_deaths;

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

        total_immunity_ratio += Total_vaccinated_today;
        total_vaccinated += Total_vaccinated_today;
        total_immunity_ratio += total_new_daily_incidence;

        int[] a = IntStream.range(1, Parameters.DataPackSize).toArray();

        return new_data;
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

        double waning_rate = 1;

        for (int Ageband = 0; Ageband < 16; Ageband++) {
            double Immunity_level_against_infection_mild = 0;
            double Immunity_level_against_hospitalization_severe = 0;

            for (int date = 0; date < Pastdata.getLength(); date++) {
                double vaccinated_on_day =  Pastdata.getTimeSeries()[date].getDataPackByAge()[0][23][Ageband];
                double infected_on_day =  Pastdata.getTimeSeries()[date].getDataPackByAge()[0][17][Ageband];

                int date_index = Math.min(1500,Main.Day + Pastdata.getLength() - date);
                date_index *= waning_rate;

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

    public static void set_new_data(Data new_data, int start, int end, double[] data){
        for (int i = start; i < end; i++) {
            new_data.setValueDataPack(i, data[i]);
        }
    }

}
