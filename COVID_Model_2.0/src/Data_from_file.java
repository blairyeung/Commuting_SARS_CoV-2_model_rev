import java.util.Arrays;
import java.util.Date;

public class Data_from_file {

    final static int PHU_count = 34;
    final public static String[] Age_band_name = {"12-17yrs","18-29yrs","30-39yrs","40-49yrs","50-59yrs", "60-69yrs", "70-79yrs", "80+"};
    final static int Age_band_count = Age_band_name.length;

    /**
     * Data of a specified date, including:
     * 1. Date
     * 2. No. cases by PHU, an int array
     * 3. No. deaths by PHU, an int array
     * 4. No. new cases by PHU, an int array
     * 5. No. vaccinated, one dose, by age, an double array
     * 6. No. vaccinated, two dose, by age, an double array
     */

    private Date date;

    /**
     * Raw_data
     */

    private int[] Unadjusted_cases_by_PHU;
    private int[] Unadjusted_deaths_by_PHU;
    private int[] Unadjusted_resolved_by_PHU;
    private double[] Percentage_vaccinated_one_dose;
    private double[] Percentage_vaccinated_two_dose;

    private double[] Adjusted_percentage_vaccinated_one_dose;
    private double[] Adjusted_percentage_vaccinated_two_dose;

    public int[] Adjusted_cases_by_PHU;
    public int[] Adjusted_deaths_by_PHU;
    public int[] Adjusted_resolved_by_PHU;

    public static void main(String[] args) {

    }

    public Data_from_file(){

    }

    public Data_from_file(Date date){
        this.date = date;
        Percentage_vaccinated_one_dose = new double[this.Age_band_count];
        Percentage_vaccinated_two_dose = new double[this.Age_band_count];

        Unadjusted_cases_by_PHU = new int[this.PHU_count];
        Unadjusted_deaths_by_PHU = new int[this.PHU_count];
        Unadjusted_resolved_by_PHU = new int[this.PHU_count];
        Adjusted_cases_by_PHU = new int[this.PHU_count];
        Adjusted_deaths_by_PHU = new int[this.PHU_count];
        Adjusted_resolved_by_PHU = new int[this.PHU_count];
        Adjusted_percentage_vaccinated_one_dose = new double[Parameters.AgeBand.length];
        Adjusted_percentage_vaccinated_two_dose = new double[Parameters.AgeBand.length];
    }

    public Data_from_file(int PHU_count, int Age_band_count){
        Unadjusted_cases_by_PHU = new int[PHU_count];
        Unadjusted_deaths_by_PHU = new int[PHU_count];
        Adjusted_cases_by_PHU = new int[PHU_count];
        Adjusted_deaths_by_PHU = new int[PHU_count];
        Percentage_vaccinated_one_dose = new double[Age_band_count];
        Percentage_vaccinated_two_dose = new double[Age_band_count];
    }

    /**
     * @param unadjusted_cases_by_PHU
     * @param PHU_index
     */

    public void setUnadjusted_cases_by_PHU(int unadjusted_cases_by_PHU,int PHU_index) {
        Unadjusted_cases_by_PHU[PHU_index] = unadjusted_cases_by_PHU;
    }

    public void setUnadjusted_deaths_by_PHU(int unadjusted_deaths_by_PHU,int PHU_index) {
        Unadjusted_deaths_by_PHU[PHU_index] = unadjusted_deaths_by_PHU;
    }

    public void setUnadjusted_resolved_by_PHU(int unadjusted_resolved_by_PHU,int PHU_index) {
        Unadjusted_resolved_by_PHU[PHU_index] = unadjusted_resolved_by_PHU;
    }

    public void setAdjusted_percentage_vaccinated_one_dose(double[] adjusted_percentage_vaccinated_one_dose) {
        Adjusted_percentage_vaccinated_one_dose = adjusted_percentage_vaccinated_one_dose;
    }

    public void setAdjusted_percentage_vaccinated_two_dose(double[] adjusted_percentage_vaccinated_two_dose) {
        Adjusted_percentage_vaccinated_two_dose = adjusted_percentage_vaccinated_two_dose;
    }

    /**
     * @param adjusted_cases_by_PHU
     * @param PHU_index
     */

    private void setAdjusted_cases_by_PHU(int adjusted_cases_by_PHU,int PHU_index) {
        Adjusted_cases_by_PHU[PHU_index] = adjusted_cases_by_PHU;
    }

    private void setAdjusted_deaths_by_PHU(int adjusted_deaths_by_PHU,int PHU_index) {
        Adjusted_deaths_by_PHU[PHU_index] = adjusted_deaths_by_PHU;
    }

    private void setAdjusted_resolved_by_PHU(int adjusted_resolved_by_PHU,int PHU_index) {
        Adjusted_resolved_by_PHU[PHU_index] = adjusted_resolved_by_PHU;
    }

    public void setPercentage_vaccinated_one_dose_by_age(double[] percentage_vaccinated_one_dose) {
        Percentage_vaccinated_one_dose = percentage_vaccinated_one_dose;
    }

    public void setPercentage_vaccinated_two_dose_by_age(double[] percentage_vaccinated_two_dose) {
        Percentage_vaccinated_two_dose = percentage_vaccinated_two_dose;
    }

    /**
     * @param percentage_vaccinated_one_dose
     * @param Age_Band
     */

    public void setPercentage_vaccinated_one_dose_by_age(double percentage_vaccinated_one_dose, int Age_Band) {
        Percentage_vaccinated_one_dose[Age_Band] = percentage_vaccinated_one_dose;
    }

    public void setPercentage_vaccinated_two_dose_by_age(double percentage_vaccinated_two_dose, int Age_Band) {
        Percentage_vaccinated_two_dose[Age_Band] = percentage_vaccinated_two_dose;
    }
    public Date getDate() {
        return date;
    }

    public void adjust_data(){
        double Underacertainment_case = 0.3;
        double Underacertainment_death = 0.1;

        double Adjust_ratio_case = 1 + Underacertainment_case;
        double Adjust_ratio_death = 1 + Underacertainment_death;

        for (int PHU_code = 0; PHU_code < Adjusted_cases_by_PHU.length; PHU_code++) {
            Adjusted_cases_by_PHU[PHU_code] = (int) ((double) Unadjusted_cases_by_PHU[PHU_code] * Adjust_ratio_case);
            Adjusted_deaths_by_PHU[PHU_code] = (int) ((double)Unadjusted_deaths_by_PHU[PHU_code] * Adjust_ratio_death);
            Adjusted_resolved_by_PHU[PHU_code] = (int) ((double)Unadjusted_resolved_by_PHU[PHU_code] * Adjust_ratio_case);
        }
    }

    public double[] getPercentage_vaccinated_one_dose() {
        return Percentage_vaccinated_one_dose;
    }

    public double[] getPercentage_vaccinated_two_dose() {
        return Percentage_vaccinated_two_dose;
    }

    public double[] getAdjusted_percentage_vaccinated_one_dose() {
        return Adjusted_percentage_vaccinated_one_dose;
    }

    public double[] getAdjusted_percentage_vaccinated_two_dose() {
        return Adjusted_percentage_vaccinated_two_dose;
    }

    /**
     * @param Age_band
     * @return vaccinated
     */

    public double getPercentage_vaccinated_one_dose(int Age_band) {
        return Percentage_vaccinated_one_dose[Age_band];
    }

    public double getPercentage_vaccinated_two_dose(int Age_band) {
        return Percentage_vaccinated_two_dose[Age_band];
    }

    /**
     * @param PHU_index
     * @return
     */

    public int getAdjusted_cases_by_PHU(int PHU_index) {
        return Adjusted_cases_by_PHU[PHU_index];
    }

    public int getAdjusted_deaths_by_PHU(int PHU_index) {
        return Adjusted_deaths_by_PHU[PHU_index];
    }

    public int getAdjusted_resolved_by_PHU(int PHU_index) {
        return Adjusted_resolved_by_PHU[PHU_index];
    }

    public int getUnadjusted_cases_by_PHU(int PHU_index) {
        return Unadjusted_cases_by_PHU[PHU_index];
    }

    public int getUnadjusted_deaths_by_PHU(int PHU_index) {
        return Unadjusted_deaths_by_PHU[PHU_index];
    }

    public int getUnadjusted_resolved_by_PHU(int PHU_index) {
        return Unadjusted_resolved_by_PHU[PHU_index];
    }

    /**
     * @param PHU_name
     * @return
     */

    public int getAdjusted_cases_by_PHU(String PHU_name) {
        return Adjusted_cases_by_PHU[PHU.PHUs_list.indexOf(PHU_name)];
    }

    public int getAdjusted_deaths_by_PHU(String PHU_name) {
        return Adjusted_deaths_by_PHU[PHU.PHUs_list.indexOf(PHU_name)];
    }

    public int getAdjusted_resolved_by_PHU(String PHU_name) {
        return Adjusted_resolved_by_PHU[PHU.PHUs_list.indexOf(PHU_name)];
    }

    public int getUnadjusted_cases_by_PHU(String PHU_name) {
        return Unadjusted_cases_by_PHU[PHU.PHUs_list.indexOf(PHU_name)];
    }

    public int getUnadjusted_deaths_by_PHU(String PHU_name) {
        return Unadjusted_deaths_by_PHU[PHU.PHUs_list.indexOf(PHU_name)];
    }

    public int getUnadjusted_resolved_by_PHU(String PHU_name) {
        return Unadjusted_resolved_by_PHU[PHU.PHUs_list.indexOf(PHU_name)];
    }

    @Override
    public String toString() {
        return "Data_from_file{" +
                "date=" + date +
                ", Unadjusted_cases_by_PHU=" + Arrays.toString(Unadjusted_cases_by_PHU) +
                ", Unadjusted_deaths_by_PHU=" + Arrays.toString(Unadjusted_deaths_by_PHU) +
                ", Unadjusted_resolved_by_PHU=" + Arrays.toString(Unadjusted_resolved_by_PHU) +
                ", Percentage_vaccinated_one_dose=" + Arrays.toString(Percentage_vaccinated_one_dose) +
                ", Percentage_vaccinated_two_dose=" + Arrays.toString(Percentage_vaccinated_two_dose) +
                ", Adjusted_percentage_vaccinated_one_dose=" + Arrays.toString(Adjusted_percentage_vaccinated_one_dose) +
                ", Adjusted_percentage_vaccinated_two_dose=" + Arrays.toString(Adjusted_percentage_vaccinated_two_dose) +
                ", Adjusted_cases_by_PHU=" + Arrays.toString(Adjusted_cases_by_PHU) +
                ", Adjusted_deaths_by_PHU=" + Arrays.toString(Adjusted_deaths_by_PHU) +
                ", Adjusted_resolved_by_PHU=" + Arrays.toString(Adjusted_resolved_by_PHU) +
                '}';
    }
}
