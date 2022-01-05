public class County_Return_Datapack {
    private double incidence_rate;
    private int incidence;
    private int exported_cases;

    public County_Return_Datapack(){

    }

    public County_Return_Datapack(double incidence_rate, int incidence, int exported_cases){
        this.incidence_rate = incidence_rate;
        this.incidence = incidence;
        this.exported_cases = exported_cases;
    }

    public void setExported_cases(int exported_cases) {
        exported_cases = exported_cases;
    }

    public void setIncidence(int incidence) {
        this.incidence = incidence;
    }

    public void setIncidence_rate(double incidence_rate) {
        this.incidence_rate = incidence_rate;
    }

    public double getIncidence_rate() {
        return incidence_rate;
    }

    public int getExported_cases() {
        return exported_cases;
    }

    public int getIncidence() {
        return incidence;
    }
}
