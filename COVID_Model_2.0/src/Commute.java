import java.text.Normalizer;
import java.util.ArrayList;

public class Commute {

    public static final int UpperLimit = 120;/**Assumed limit for commute distance*/

    /**
     * Raw data
     */

    private static int Commute_Matrix[][] = null; /**Live_in_County_A_Work_in_County_B*/
    private static int Reversed_Commute_Matrix[][] = null; /**Work_in_County_A_Resident_in_County_B*/
    private static int Exportations[] = null; /**Live_in_County_A_Work_in_County_B*/
    private static int Importations[] = null; /**Work_in_County_A_Resident_in_County_B*/

    /**
     * Processed data
     */

    private static int Generated_Matrix[][] = null; /**Live_in_County_A_Work_in_County_B*/
    private static int Generated_Reversed_Commute_Matrix[][] = null; /**Live_in_County_A_Work_in_County_B*/
    private static int Generated_Exportations[] = null; /**Live_in_County_A_Work_in_County_B*/
    private static int Generated_Importations[] = null; /**Live_in_County_A_Work_in_County_B*/






    private static int Total_number_of_commuters[][] =  null;

    private static int Commute_distance_tier_by_county[][] = null;

    private static int Commute_distance_tier_by_county_rural_subarray[][] = null;
    private static int Commute_distance_tier_by_county_city_subarray[][] = null;

    private static ArrayList[] Rural_counties_within_commute_range_code= null;
    private static ArrayList[] City_counties_within_commute_range_code= null;

    private static double[] Rural_counties_within_commute_range_population= null;
    private static double[] City_counties_within_commute_range_population= null;

    private static ArrayList[] Rural_counties_within_commute_range_tier= null;
    private static ArrayList[] City_counties_within_commute_range_tier= null;

    private static boolean Within_same_district_by_county[][] = null;

    private static int[] Total_Population_of_potential_rural_within_range = null;
    private static int[] Total_Population_of_potential_city_within_range = null;

    private static int[][] Tiered_Total_Population_of_potential_rural_within_range = null;
    private static int[][] Tiered_Total_Population_of_potential_city_within_range = null;

    public static void getStaticCommuteMatrix(){

        /**
         * Original code aborted
         */

        Commute_IO.Commute_IO_Input();
        Commute_Matrix = Commute_IO.getCommuting_matrix();
        Reversed_Commute_Matrix = Commute_IO.getReverse_Commuting_matrix();
        Exportations = Commute_IO.getNumber_of_commuters_departing();
        Importations = Commute_IO.getNumber_of_commuters_arriving();
        Generated_Matrix = Commute_Matrix;
        Generated_Exportations = Exportations;
    }

    public static void main(String[] args) {
        CountyDataIO c = new CountyDataIO();
        getStaticCommuteMatrix();
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


    public static void GenerateWeeklyCommuteMatrixfromMutiplicatoin(int Tiers[]){
        /**
         * Stratify counties by distance to another city, for tiered
         * Will only be ran once to save computational power
         */

        int Number_of_Counties = CountyDataIO.Counties.length;

        Generated_Matrix = new int[Number_of_Counties][Number_of_Counties];
        Generated_Reversed_Commute_Matrix = new int[Number_of_Counties][Number_of_Counties];

        Generated_Exportations = new int[Number_of_Counties];
        Generated_Importations = new int[Number_of_Counties];

        for (int Resident = 0; Resident < Number_of_Counties; Resident++) {
            for (int Work = 0; Work < Number_of_Counties; Work++) {
                int Maximum_level = Math.max(Tiers[Work],Tiers[Resident]);
                double Workplace_lockdown_coefficient = Parameters.WorkPlaceReductionbyLevel[Maximum_level];
                int Number = (int) Math.round(Workplace_lockdown_coefficient * Commute_Matrix[Resident][Work]);
                Generated_Matrix[Resident][Work] = Number;
                Generated_Reversed_Commute_Matrix[Work][Resident] = Number;
                Generated_Exportations[Resident] = Number;
                Generated_Importations[Work] = Number;
            }
        }
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

    public static int[] getExportations() {
        return Exportations;
    }

    public static int[] getImportations() {
        return Importations;
    }

    public static int[][] getGenerated_Matrix() {
        return Generated_Matrix;
    }

    public static int[] getGenerated_Exportations() {
        return Generated_Exportations;
    }
}
