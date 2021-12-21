import java.text.Normalizer;
import java.util.ArrayList;

public class CommuteBackup {

    public static final int UpperLimit = 120;/**Assumed limit for commute distance*/

    private static int Commute_Matrix[][] = null; /**Live_in_County_A_Work_in_County_B*/

    private static int Reversed_Commute_Matrix[][] = null; /**Work_in_County_A_Resident_in_County_B*/

    private static int Total_number_of_commuters[][] =  null;

    private static int Commute_distance_tier_by_county[][] = null;

    private static int Commute_distance_tier_by_county_rural_subarray[][] = null;
    private static int Commute_distance_tier_by_county_city_subarray[][] = null;

    private static ArrayList[] Rural_counties_within_commute_range_code= null;
    private static ArrayList[] City_counties_within_commute_range_code= null;

    private static ArrayList[] Rural_counties_within_commute_range_tier= null;
    private static ArrayList[] City_counties_within_commute_range_tier= null;

    private static boolean Within_same_district_by_county[][] = null;

    private static int[] Total_Population_of_potential_rural_within_range = null;
    private static int[] Total_Population_of_potential_city_within_range = null;

    private static int[][] Tiered_Total_Population_of_potential_rural_within_range = null;
    private static int[][] Tiered_Total_Population_of_potential_city_within_range = null;

    public static void getStaticCommuteMatrix(){

        /**
         * Stratify counties by distance to another city, for tiered
         * Will only be ran once to save computational power
         */

        int Number_of_Counties = CountyDataIO.Counties.length;

        Commute_distance_tier_by_county = new int[Number_of_Counties][Parameters.Travel_distance_distribution_full.length];
        Within_same_district_by_county = new boolean[Number_of_Counties][Parameters.Travel_distance_distribution_full.length];

        Total_Population_of_potential_rural_within_range = new int[Number_of_Counties];
        Total_Population_of_potential_city_within_range = new int[Number_of_Counties];

        Tiered_Total_Population_of_potential_city_within_range = new int[Number_of_Counties][Parameters.Travel_distance_distribution_full.length];
        Tiered_Total_Population_of_potential_rural_within_range = new int[Number_of_Counties][Parameters.Travel_distance_distribution_full.length];

        Rural_counties_within_commute_range_code = new ArrayList[Number_of_Counties];
        City_counties_within_commute_range_code = new ArrayList[Number_of_Counties];

        Rural_counties_within_commute_range_tier = new ArrayList[Number_of_Counties];
        City_counties_within_commute_range_tier = new ArrayList[Number_of_Counties];

        Total_number_of_commuters = new int[Number_of_Counties][2];

        /**
         * Find the number of commuters to rural/city by the population of the county and the type of the county
         */

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            int County_type = CountyDataIO.Counties[County_Code].getCounty_Type();

            int Population = CountyDataIO.Counties[County_Code].getPopulation();

            double Commuter_ratio = 0;
            double toUrban = 0;
            double toRural = 0;

            int toUrbanPopulation = 0;
            int toRuralPopulation = 0;


            switch (County_type){
                case 0:
                    /**
                     * Rural
                     */

                    Commuter_ratio = Parameters.Rural_commuter_ratio;
                    toUrban = Commuter_ratio * Parameters.Rural_to_Urban_Ratio;
                    toRural = Commuter_ratio * Parameters.Rural_to_Rural_Ratio;
                    toUrbanPopulation = (int) Math.round(toUrban * ((double) Population));
                    toRuralPopulation = (int) Math.round(toRural * ((double) Population));
                    break;
                case 1:
                    /**
                     * City
                     */

                    Commuter_ratio = Parameters.Urban_commuter_ratio;
                    toUrban = Commuter_ratio * Parameters.Urban_to_Urban_Ratio;
                    toRural = Commuter_ratio * Parameters.Urban_to_Rural_Ratio;

                    toUrbanPopulation = (int) Math.round(toUrban * ((double) Population));
                    toRuralPopulation = (int) Math.round(toRural * ((double) Population));
                    break;

                default:
            }

            Total_number_of_commuters[County_Code][0] = toRuralPopulation;
            Total_number_of_commuters[County_Code][1] = toUrbanPopulation;

        }

        for (int Resident = 0; Resident < Number_of_Counties; Resident++) {

            Rural_counties_within_commute_range_code[Resident] = new ArrayList<Integer>();
            City_counties_within_commute_range_code[Resident] = new ArrayList<Integer>();

            Rural_counties_within_commute_range_tier[Resident] = new ArrayList<Integer>();
            City_counties_within_commute_range_tier[Resident] = new ArrayList<Integer>();

            for (int Work = 0; Work < Number_of_Counties; Work++) {

                if(Resident==Work){
                    continue;
                }

                double distance = CountyDataIO.DistanceBetweenCounties[Resident][Work];

                if(distance<=UpperLimit){
                    int distance_tier = 6;

                    distance_tier = (int) distance/5;

                    /**for (int Tier = 0; Tier < Parameters.Travel_distance_distribution_full.length-1; Tier++) {
                     if(distance<=((Tier+1)*5)){
                     distance_tier = Tier;
                     break;
                     }
                     }*/

                    int County_type = CountyDataIO.Counties[Work].getCounty_Type();
                    int Workplace_population = CountyDataIO.Counties[Work].getPopulation();

                    switch (County_type){
                        case 0:
                            /**
                             * If this county belongs to rural
                             */

                            Rural_counties_within_commute_range_code[Resident].add(Work);
                            Rural_counties_within_commute_range_tier[Resident].add(distance_tier);

                            Total_Population_of_potential_rural_within_range[Resident] += Workplace_population;

                            Tiered_Total_Population_of_potential_rural_within_range[Resident][distance_tier] += Workplace_population;

                            continue;
                        case 1:
                            /**
                             * If this county belongs to city
                             */

                            City_counties_within_commute_range_code[Resident].add(Work);
                            City_counties_within_commute_range_tier[Resident].add(distance_tier);

                            Total_Population_of_potential_city_within_range[Resident] += Workplace_population;

                            Tiered_Total_Population_of_potential_city_within_range[Resident][distance_tier] += Workplace_population;

                            continue;
                        default:
                            /**throw new Exception(){

                             }*/

                            continue;
                    }

                }

            }

            System.out.println(CountyDataIO.Counties[Resident].getName());

            for (int i = 0; i < 24; i++) {
                System.out.println("Tier= " + Parameters.Travel_distance_tiers_full[i]);
                System.out.println("Rural_destination_sum_of_Population= " + Tiered_Total_Population_of_potential_rural_within_range[Resident][i]);
                System.out.println("City_destination_sum_of_Population= " + Tiered_Total_Population_of_potential_city_within_range[Resident][i]);
            }

            System.out.println("Total");

            System.out.println("Rural_destination_sum_of_Population= " + Total_Population_of_potential_rural_within_range[Resident]);
            System.out.println("City_destination_sum_of_Population= " + Total_Population_of_potential_city_within_range[Resident]);

        }

        Commute_Matrix = new int[Number_of_Counties][Number_of_Counties];
        Reversed_Commute_Matrix = new int[Number_of_Counties][Number_of_Counties];


        for (int Resident = 0; Resident < Number_of_Counties; Resident++) {
            for (int Work = 0; Work < Number_of_Counties; Work++) {

                int Tier = 0;

                if(Resident==Work){
                    continue;
                }

                double distance = CountyDataIO.DistanceBetweenCounties[Resident][Work];

                if(distance<=UpperLimit){

                    int Workplace_population = CountyDataIO.Counties[Work].getPopulation();

                    Tier = (int) (distance/5.0);
                    int County_Type = CountyDataIO.Counties[Work].getCounty_Type();
                    int Total_population_range = 0;

                    double Proportion = 0;
                    double Total_commuter = Total_number_of_commuters[Resident][County_Type];

                    int Expected_number_of_commuters = 0; /**Assumming baseline scenario*/



                    switch (County_Type){
                        case 0:
                            /**
                             * Rural
                             */

                            Total_population_range = Tiered_Total_Population_of_potential_rural_within_range[Resident][Tier];

                            Proportion = (((double)Workplace_population)/ ((double)Total_population_range));

                            Expected_number_of_commuters = (int) Math.round(Proportion * Total_commuter * Parameters.Travel_distance_distribution_full[Tier]);

                            if(Work==0){
                                /**
                                 * If the county sits nears toronto
                                 * Nearly 35% of the total population
                                 *
                                 * */
                                Expected_number_of_commuters = (int) Math.round(0.8 * Parameters.Urban_commuter_ratio * ((double) CountyDataIO.Counties[Resident].getPopulation()));
                            }

                            break;
                        case 1:

                            /**
                             * City
                             */

                            Total_population_range = Tiered_Total_Population_of_potential_city_within_range[Resident][Tier];

                            Proportion = (((double)Workplace_population)/ ((double)Total_population_range));

                            Expected_number_of_commuters = (int) Math.round(Proportion * Total_commuter * Parameters.Travel_distance_distribution_full[Tier]);

                            break;
                    }



                    Commute_Matrix[Resident][Work] = Expected_number_of_commuters;
                    Reversed_Commute_Matrix[Work][Resident] = Expected_number_of_commuters;
                }
            }
        }
    }

    public static void GenerateWeeklyCommuteMatrix(){

    }

    public static ArrayList[] getCity_counties_within_commute_range_code() {
        return City_counties_within_commute_range_code;
    }

    public static ArrayList[] getCity_counties_within_commute_range_tier() {
        return City_counties_within_commute_range_tier;
    }

    public static ArrayList[] getRural_counties_within_commute_range_code() {
        return Rural_counties_within_commute_range_code;
    }

    public static ArrayList[] getRural_counties_within_commute_range_tier() {
        return Rural_counties_within_commute_range_tier;
    }

    public static boolean[][] getWithin_same_district_by_county() {
        return Within_same_district_by_county;
    }

    public static int[] getTotal_Population_of_potential_city_within_range() {
        return Total_Population_of_potential_city_within_range;
    }

    public static int[] getTotal_Population_of_potential_rural_within_range() {
        return Total_Population_of_potential_rural_within_range;
    }

    public static int[][] getCommute_distance_tier_by_county() {
        return Commute_distance_tier_by_county;
    }

    public static int[][] getCommute_distance_tier_by_county_city_subarray() {
        return Commute_distance_tier_by_county_city_subarray;
    }

    public static int[][] getCommute_distance_tier_by_county_rural_subarray() {
        return Commute_distance_tier_by_county_rural_subarray;
    }

    public static int[][] getTiered_Total_Population_of_potential_city_within_range() {
        return Tiered_Total_Population_of_potential_city_within_range;
    }

    public static int[][] getTiered_Total_Population_of_potential_rural_within_range() {
        return Tiered_Total_Population_of_potential_rural_within_range;
    }

    public static int[][] getCommute_Matrix() {
        return Commute_Matrix;
    }

    public static int[][] getReversed_Commute_Matrix() {
        return Reversed_Commute_Matrix;
    }

    public static int[][] getTotal_number_of_commuters() {
        return Total_number_of_commuters;
    }

    public static void main(String[] args) {

    }

    public double[][] Commute(double[] Tier, double[][] StratificationByCouty){
        int CountyCount = CountyDataIO.Counties.length;
        double[][] CommuteFromTo = new double[CountyCount][CountyCount];
        for (int i = 0; i < CountyCount; i++) {
            for (int i1 = 0; i1 < CountyCount; i1++) {
                double Distance = CountyDataIO.DistanceBetweenCounties[i][i1];
                int CaseFlux;

            }
        }
        return new double[1][1];
    }

    public double[] FindCoutiesWithinRange(double[] StartCoord, int InitialCounty){
        double[] Distances = CountyDataIO.DistanceBetweenCounties[InitialCounty];
        double[] Exported = new double[CountyDataIO.Counties.length];
        ArrayList<Integer> CountyWithinRange = new ArrayList<>();

        for (int i = 0; i < CountyDataIO.Counties.length; i++) {
            if(Distances[i]<150){
                CountyWithinRange.add(i);
            }
        }

        double[] Relative_commuters = new double[CountyWithinRange.size()];

        for (int DestinationCounty = 0; DestinationCounty < CountyWithinRange.size(); DestinationCounty++) {
            double Population = CountyDataIO.Counties[DestinationCounty].getPopulation();
            double Distance = CountyDataIO.DistanceBetweenCounties[InitialCounty][CountyWithinRange.get(DestinationCounty)];
            double Weight = getWeight(InitialCounty, DestinationCounty, Distance);
            Relative_commuters[DestinationCounty] = Weight;
        }

        Relative_commuters = Function.Normalization(Relative_commuters);

        return Exported;
    }

    public double getWeight(int InitialCounty, int DestinationCounty, double Distance){

        double Weight = 0;

        /**
         * Calculate the relative percentage of commuters
         */

        double Dist = 0;

        for (int i = 0; i < 20; i++) {
            Dist += GammaDist.getGammaFunction(Parameters.MedianDistance_of_Travel_for_Commute)[(int) Distance - 10 + i];
        }

        double DestinationPopulation = CountyDataIO.Counties[DestinationCounty].getPopulation();

        Weight = Dist * DestinationPopulation;

        return Weight;
    }

}
