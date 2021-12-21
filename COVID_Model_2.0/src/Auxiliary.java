import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Scanner;

public class Auxiliary{
    public static void main(String[] args) throws Exception{
        System.out.println("Hi");

        double[] input2_assumed_age_distribution_of_cases = new double[16];
        double[] input2_assumed_age_distribution_of_deaths = new double[16];
        double[] input2_assumed_age_distribution_of_resolved = new double[16];

        int York_region_total_population = 10000;
        int Richmond_total_population = 2000;
        int Markham_total_population = 3000;
        int North_york_total_population = 5000;

        int Num_cases_in_York_Region = 200;

        /**
         * First step: calculate the number of cases in diff counties in York region
         * thus we need to get:
         * 1. int Num_cases_in_Richmond
         * 2. int Num_cases_in_Markham
         * 3. int Num_cases_in_North_York
         */

        int Num_cases_in_Richmond = (Richmond_total_population/York_region_total_population) * Num_cases_in_York_Region;
        int Num_cases_in_Markham = (Markham_total_population/York_region_total_population) * Num_cases_in_York_Region;
        int Num_cases_in_North_York = (North_york_total_population/York_region_total_population) * Num_cases_in_York_Region;

        /**
         * Second step: calculate the number of cases in diff counties in diff age bands
         * thus we need to get:
         *  1. int Number_cases_in_Richmond_in_0_to_4_yrs
         *  2. int Number_cases_in_Richmond_in_5_to_9_yrs
         *  ...
         */

        int Number_cases_in_Richmond_in_0_to_4_yrs = (int) (Num_cases_in_Richmond * input2_assumed_age_distribution_of_cases[0]);
        int Number_cases_in_Richmond_in_5_to_9_yrs = (int) (Num_cases_in_Richmond * input2_assumed_age_distribution_of_cases[1]);

        /*for (int county_code = 0; county_code < 528; county_code++) {
            for (int Age_band = 0; Age_band < 16; Age_band++) {
                Number_cases_in_county_in_ageband[county_code][Age_band] = Num_cases_in_County[county_code] * input2_assumed_age_distribution_of_cases[Age_band];
            }
        }*/

        //...

        /**
         * buffered_Output
         * desired:
         * double[][] Number_of_cases_in_a_county_within_a_specific_age_band = new double[528(no. counties)][16 (no. age_bands)]
         * double[][] Number_of_deaths_in_a_county_within_a_specific_age_band = new double[528(no. counties)][16 (no. age_bands)]
         * double[][] Number_of_resolved_in_a_county_within_a_specific_age_band = new double[528(no. counties)][16 (no. age_bands)]
         */

    }

}

