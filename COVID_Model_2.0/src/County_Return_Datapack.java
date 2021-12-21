public class County_Return_Datapack {
    private double Incidence_rate;
    private int Incidence;
    private int Exported_cases;

    public County_Return_Datapack(){

    }

    public County_Return_Datapack(double incidence_rate, int incidence, int exported_cases){
        this.Incidence_rate = incidence_rate;
        this.Incidence = incidence;
        this.Exported_cases = exported_cases;
    }

    public void setExported_cases(int exported_cases) {
        Exported_cases = exported_cases;
    }

    public void setIncidence(int incidence) {
        Incidence = incidence;
    }

    public void setIncidence_rate(double incidence_rate) {
        Incidence_rate = incidence_rate;
    }

    public double getIncidence_rate() {
        return Incidence_rate;
    }

    public int getExported_cases() {
        return Exported_cases;
    }

    public int getIncidence() {
        return Incidence;
    }
}
