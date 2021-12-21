import javax.naming.Name;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Commute_IO {

    public static int[][] Static_Commuting_Matrix = null;
    public static int[][] Static_Commuting_Matrix_Male = null;
    public static int[][] Static_Commuting_Matrix_Female = null;

    public static int[][] Commuting_matrix = null;
    public static int[][] Reverse_Commuting_matrix = null;

    public static int[] Number_of_commuters_departing = null;
    public static int[] Number_of_commuters_arriving = null;

    public static int[] Local_worker_array = null;

    public static void main(String[] args) {
        CountyDataIO.CountyData_IO_Input();
        Commute_IO.Commute_IO_Input();
    }

    public static void Commute_IO_Input(){
        String Path = Parameters.Model_PATH + "IO Commute.csv";



        Static_Commuting_Matrix = new int[CountyDataIO.Counties.length][CountyDataIO.Counties.length];
        Static_Commuting_Matrix_Male = new int[CountyDataIO.Counties.length][CountyDataIO.Counties.length];
        Static_Commuting_Matrix_Female = new int[CountyDataIO.Counties.length][CountyDataIO.Counties.length];

        Commuting_matrix = new int[CountyDataIO.Counties.length][CountyDataIO.Counties.length];
        Reverse_Commuting_matrix = new int[CountyDataIO.Counties.length][CountyDataIO.Counties.length];

        Number_of_commuters_departing = new int[CountyDataIO.Counties.length];
        Number_of_commuters_arriving = new int[CountyDataIO.Counties.length];
        Local_worker_array = new int[CountyDataIO.Counties.length];

        ArrayList<Commute_info> Commuting_List = new ArrayList<>();
        ArrayList<String> lines = Function.Buffered_IO(Path,true);

        for (int line = 0; line < lines.size(); line++) {
            String str = lines.get(line);
            String[] Stratified = Function.Stratification(str);

            int ID_Resident = Integer.parseInt(Stratified[0]);
            String Name_Resident = Stratified[1];
            int ID_Work = Integer.parseInt(Stratified[2]);
            String Name_Work = Stratified[3];
            int Total_Commuter = Integer.parseInt(Stratified[4]);
            int Male_Commuter = Integer.parseInt(Stratified[5]);
            int Female_Comuter = Integer.parseInt(Stratified[6]);

            System.out.print("County: " + ID_Resident);
            System.out.print("    Name : " + Name_Resident);
            System.out.print("    To: " + ID_Work);
            System.out.print("    Name: " + Name_Work);
            System.out.print("    Total: " + Total_Commuter);
            System.out.print("    Male: " + Male_Commuter);
            System.out.println("    Female: " + Female_Comuter);

            Commute_info info = new Commute_info(ID_Resident, Name_Resident, ID_Work, Name_Work, Total_Commuter, Male_Commuter, Female_Comuter);
            Commuting_List.add(info);
        }


        for (int Info = 0; Info < Commuting_List.size(); Info++) {

            int Work_Code = Commuting_List.get(Info).getWork_County_Code();
            int Resident_Code = Commuting_List.get(Info).getResident_County_Code();
            int Total_Commuter = Commuting_List.get(Info).getTotal_Commuter();
            int Male_Commuter = Commuting_List.get(Info).getMale_Commuter();
            int Female_Commuter = Commuting_List.get(Info).getFemale_Commuter();
            int Work_Index = CountyDataIO.Code_Index.indexOf(Work_Code);
            int Resident_Index = CountyDataIO.Code_Index.indexOf(Resident_Code);

            if(Work_Index==Resident_Index){
                Local_worker_array[Work_Index] = Total_Commuter;
                continue;
            }

            Static_Commuting_Matrix[Resident_Index][Work_Index] = Total_Commuter;
            Static_Commuting_Matrix_Male[Resident_Index][Work_Index] = Male_Commuter;
            Static_Commuting_Matrix_Female[Resident_Index][Work_Index] = Female_Commuter;

            Number_of_commuters_departing[Resident_Index] += Total_Commuter;
            Number_of_commuters_arriving[Work_Index] += Total_Commuter;

            double Distance = CountyDataIO.DistanceBetweenCounties[Resident_Index][Work_Index];

            Commuting_List.get(Info).setCommute_Distance(Distance);
        }

        Commuting_matrix = Static_Commuting_Matrix;
        Reverse_Commuting_matrix = Reverse_Mat(Commuting_matrix);


    }

    public static void generateWeeklyMatrix(int[] Tiers){

    }

    public void findDistanceFromFile(){

    }

    public static int[][] Reverse_Mat(int[][] Original){
        int[][] Reverse = new int[Original.length][Original[0].length];
        for (int row = 0; row < Original.length; row++) {
            for (int column = 0; column < Original[0].length; column++) {
                Reverse[column][row] = Original[row][column];
            }
        }
        return Reverse;
    }

    /**
     * Export, break down into Districts
     */

    public static int[] getNumber_of_commuters_arriving() {
        return Number_of_commuters_arriving;
    }

    public static int[] getNumber_of_commuters_departing() {
        return Number_of_commuters_departing;
    }

    public static int[][] getCommuting_matrix() {
        return Commuting_matrix;
    }

    public static int[][] getReverse_Commuting_matrix() {
        return Reverse_Commuting_matrix;
    }

    public static int[][] getStatic_Commuting_Matrix() {
        return Static_Commuting_Matrix;
    }

    public static int[] getLocal_worker_array() {
        return Local_worker_array;
    }
}
