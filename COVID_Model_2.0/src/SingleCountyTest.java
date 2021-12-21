public class SingleCountyTest {
    public static void main(String[] args) {
        Main.Day = 0;
        County C = new County(100000,0);

        String path = "K:\\Ontario Model\\Single_County_Test\\";

        Trail trailData = new Trail(0,1);

        path += "Iteration " + trailData.getModel_Iteratons() + "\\" + "T rail " + trailData.getTrail_Code() + "\\";

        String By_Age = path + "File_by_age\\";
        String By_Category = path + "File_by_category\\";

        Directory_Creator.create_dir(path);
        Directory_Creator.create_dir(By_Age);
        Directory_Creator.create_dir(By_Category);

        Province Ontario = new Province(trailData);
        Ontario.Debug();

        /*for (int i = 0; i < Parameters.Observation_Range-1; i++) {
            C.Run_Model(0,0);
            C.Run_Modle_Resident();
            Main.Day++;
        }*/

        C.PrintFile(Ontario.TrailData);

        System.out.println(path);
    }
}
