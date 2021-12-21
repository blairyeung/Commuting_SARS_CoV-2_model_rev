import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CountyDataIO {

    public static CountyData[] Counties;
    public static ArrayList<CountyData>[] Counties_By_District;
    public static ArrayList<Integer> Code_Index;
    public static double[][] DistanceBetweenCounties;

    public static int[] DistrictCodes;
    public static ArrayList<Integer> DistrictCode = new ArrayList<>();

    public static void main(String[] args) {
        CountyData_IO_Input();
    }

    public static void CountyData_IO_Input(){
        String Path = Parameters.ReadPath + "GeoCode.csv";

        ArrayList<String> lines = Function.Buffered_IO(Path,true);
        ArrayList<String> Locations = new ArrayList<>();
        ArrayList<Integer> Counties_ID = new ArrayList<>();
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

            Counties_ID.add(ID_int);
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

            if(!DistrictCode.contains(District)){
                DistrictCode.add(District);
            }

        }

        DistrictCodes = new int[DistrictCode.size()];
        Counties_By_District = new ArrayList[DistrictCode.size()];
        Code_Index = new ArrayList<>();

        for (int i = 0; i < DistrictCode.size(); i++) {
            DistrictCodes[i] = DistrictCode.get(i);
            Counties_By_District[i] = new ArrayList<CountyData>();
        }

        Counties = ArrayListToArray.toArray(CountyDataList,Counties);

        for (int i1 = 0; i1 < Counties.length; i1++) {
            /**
             * Categorize counties by district
             */
            int ind = DistrictCode.indexOf(Counties[i1].getDistrict());
            Counties_By_District[ind].add(Counties[i1]);
        }

        /**
         * The List "Locations" will act as an menu/index
         */

        DistanceBetweenCounties = new double[Populations.size()][Populations.size()];

        for (int i = 0; i < Populations.size(); i++) {
            for (int i1 = 0; i1 < Populations.size(); i1++) {
                double distance;
                double Coord1[] = Counties[i].getCoordinate();
                double Coord2[] = Counties[i1].getCoordinate();

                //System.out.print("Depart: " + Counties[i].getName());

                //System.out.print("    Arrival: " + Counties[i1].getName());

                distance = CartesianDistance(Coord1,Coord2) * Parameters.Kilometers_per_degree;
                /**
                 * Kilometers_per_degree is a constant
                 */
                DistanceBetweenCounties[i][i1] = distance;

            }
        }

        for (int i = 0; i < Counties.length; i++) {
            Code_Index.add(Counties[i].getID());
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
