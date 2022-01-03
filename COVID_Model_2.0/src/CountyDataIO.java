import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CountyDataIO {

    public static CountyData[] counties;
    public static ArrayList<CountyData>[] county_by_district;
    public static ArrayList<Integer> code_index;
    public static double[][] distance_between_county;

    public static int[] district_code_array;
    public static ArrayList<Integer> district_code_list = new ArrayList<>();

    public static void main(String[] args) {
        CountyData_IO_Input();
    }

    public static void CountyData_IO_Input(){
        String Path = Parameters.ReadPath + "GeoCode.csv";

        ArrayList<String> lines = Function.Buffered_IO(Path,true);
        ArrayList<String> Locations = new ArrayList<>();
        ArrayList<Integer> counties_ID = new ArrayList<>();
        ArrayList<Double> Longitudes = new ArrayList<>();
        ArrayList<Double> Latitudes = new ArrayList<>();
        ArrayList<Integer> Populations = new ArrayList<>();
        ArrayList<Integer> Districts = new ArrayList<>();
        ArrayList<String> OrgCounty = new ArrayList<>();
        ArrayList<CountyData> CountyDataList = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String str = lines.get(i);
            String[] Stratified = Function.Stratification(str);
            String ID_str = Stratified[0];
            int ID_int = Integer.parseInt(ID_str);
            String Location = Stratified[1];
            double Longitude = Double.parseDouble(Stratified[2]);
            double Latitude = Double.parseDouble(Stratified[3]);
            int Population = Integer.parseInt(Stratified[4]);
            int District = Integer.parseInt(Stratified[5]);
            String County_Type = Stratified[6];

            /**
             * Remove if population is 0
             */

            if (Population==0) {
                break;
            }

            int CountyType = 0;

            System.out.println("County_Type: " + County_Type);

            if(County_Type.equals("Town")||County_Type.equals("City")||County_Type.equals("Municipality")){
                CountyType = 1;
            }

            System.out.println("ID: " + ID_int + "  Location: " + Location + "  Longtitude: " + Longitude + "  Latitude: " + Latitude + "  Population: " + Population + "  District: " + District + "  County_Type: " + CountyType);

            counties_ID.add(ID_int);
            Locations.add(Location);
            Longitudes.add(Longitude);
            Latitudes.add(Longitude);
            Populations.add(Population);
            Districts.add(District);
            OrgCounty.add(County_Type);

            double Coordinates[] = {Longitude,Latitude};

            CountyData c = new CountyData(ID_int, Location,District, Coordinates, Population, CountyType);
            CountyDataList.add(c);

            /**Add Code*/

            if(!district_code_list.contains(District)){
                district_code_list.add(District);
            }

        }

        district_code_array = new int[district_code_list.size()];
        county_by_district = new ArrayList[district_code_list.size()];
        code_index = new ArrayList<>();

        for (int i = 0; i < district_code_list.size(); i++) {
            district_code_array[i] = district_code_list.get(i);
            county_by_district[i] = new ArrayList<CountyData>();
        }

        counties = ArrayListToArray.toArray(CountyDataList,counties);

        for (int i1 = 0; i1 < counties.length; i1++) {
            /**
             * Categorize counties by district
             */
            int ind = district_code_list.indexOf(counties[i1].getDistrict());
            county_by_district[ind].add(counties[i1]);
        }

        /**
         * The List "Locations" will act as an menu/index
         */

        distance_between_county = new double[Populations.size()][Populations.size()];

        for (int i = 0; i < Populations.size(); i++) {
            for (int i1 = 0; i1 < Populations.size(); i1++) {
                double distance;
                double Coord1[] = counties[i].getCoordinate();
                double Coord2[] = counties[i1].getCoordinate();

                //System.out.print("Depart: " + counties[i].getName());

                //System.out.print("    Arrival: " + counties[i1].getName());

                distance = CartesianDistance(Coord1,Coord2) * Parameters.Kilometers_per_degree;
                /**
                 * Kilometers_per_degree is a constant
                 */
                distance_between_county[i][i1] = distance;

            }
        }

        for (int i = 0; i < counties.length; i++) {
            code_index.add(counties[i].getID());
        }
        Commute.getStaticCommuteMatrix();
    }

    public CountyDataIO(){

    }

    public static double CartesianDistance(double[] Coord1, double[] Coord2){
        double a = (Coord1[0] - Coord2[0]);
        double b = (Coord1[1] - Coord2[1]);
        double distance =Math.pow((a*a+b*b),0.5);
        return distance;
    }
}
