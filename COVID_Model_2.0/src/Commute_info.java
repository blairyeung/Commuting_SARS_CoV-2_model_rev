public class Commute_info {
    private int Resident_County_Code;
    private String Resident_County_Name;
    private int Work_County_Code;
    private String Work_County_Name;
    private int Total_Commuter;
    private int Male_Commuter;
    private int Female_Commuter;
    private double Commute_Distance;

    public Commute_info(){

    }
    public Commute_info(int Resident_Code, String Resident_Name, int Work_Code, String Work_Name, int Total, int Male, int Female){
        this.Resident_County_Code = Resident_Code;
        this.Resident_County_Name = Resident_Name;
        this.Work_County_Code = Work_Code;
        this.Work_County_Name = Work_Name;
        this.Total_Commuter = Total;
        this.Male_Commuter = Male;
        this.Female_Commuter = Female;
    }

  public Commute_info(int Resident_Code, String Resident_Name, int Work_Code, String Work_Name, int Total, int Male, int Female, double Distance){
        this.Resident_County_Code = Resident_Code;
        this.Resident_County_Name = Resident_Name;
        this.Work_County_Code = Work_Code;
        this.Work_County_Name = Work_Name;
        this.Total_Commuter = Total;
        this.Male_Commuter = Male;
        this.Female_Commuter = Female;
        this.Commute_Distance = Distance;
    }

    public void setCommute_Distance(double commute_Distance) {
        Commute_Distance = commute_Distance;
    }

    public int getResident_County_Code() {
        return Resident_County_Code;
    }

    public String getResident_County_Name() {
        return Resident_County_Name;
    }

    public int getWork_County_Code() {
        return Work_County_Code;
    }

    public String getWork_County_Name() {
        return Work_County_Name;
    }

    public int getTotal_Commuter() {
        return Total_Commuter;
    }

    public int getMale_Commuter() {
        return Male_Commuter;
    }

    public int getFemale_Commuter() {
        return Female_Commuter;
    }

    public double getCommute_Distance() {
        return Commute_Distance;
    }
}
