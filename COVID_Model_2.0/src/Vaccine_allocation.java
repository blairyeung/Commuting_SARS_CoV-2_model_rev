public class Vaccine_allocation {
    private int Allocation_by_county[];
    private int Allocation_by_district[];

    public double[] Age_specific_vaccine_dist_common = null;
    public double[] Age_specific_vaccine_dist_urban = null;
    public double[] Age_specific_vaccine_dist_rural  = null;
    public double[][] Age_specific_vaccine_dist_pack  = null;

    private double Total_vaccine;


    public Vaccine_allocation(){

    }

    public Vaccine_allocation(int Preset){
        switch (Preset){
            case 0:
                /**
                 * Uniform distribution across all ages and counties
                 */
                Total_vaccine = 100000.0;
                Vaccine_allocation_case_0();
                break;
            case 1:
                /**
                 * Prioritize Young people
                 */
                Total_vaccine = 100000.0;
                Vaccine_allocation_case_1();
                break;
        }
    }

    public void Vaccine_allocation_case_0(){
        int Number_of_counties = CountyDataIO.Counties.length;
        int Number_of_districts = CountyDataIO.DistrictCodes.length;
        Allocation_by_county = new int[Number_of_counties];  
        double Total_population = 13447229;

        for (int County_code = 0; County_code < Number_of_counties; County_code++) {
            double Sub_population = CountyDataIO.Counties[County_code].getPopulation();
            double Ratio = Sub_population/Total_population;
            Allocation_by_county[County_code] = (int) Math.round(Ratio * Total_vaccine);
        }
        Age_specific_vaccine_dist_common = new double[16];
        Age_specific_vaccine_dist_urban = new double[16];
        Age_specific_vaccine_dist_rural  = new double[16];
        Age_specific_vaccine_dist_pack  = new double[2][16];
        for (int Age_band = 0; Age_band < 16; Age_band++) {
            Age_specific_vaccine_dist_common[Age_band] = 1.0/16.0;
            Age_specific_vaccine_dist_urban[Age_band] = 1.0/16.0;
            Age_specific_vaccine_dist_rural[Age_band]  = 1.0/16.0;
            Age_specific_vaccine_dist_pack[0][Age_band]  = Age_specific_vaccine_dist_rural[Age_band];
            Age_specific_vaccine_dist_pack[1][Age_band]  = Age_specific_vaccine_dist_urban[Age_band];
        }
    }

    public void Vaccine_allocation_case_1(){

    }

    public double getTotal_vaccine() {
        return Total_vaccine;
    }

    public int[] getAllocation_by_county() {
        return Allocation_by_county;
    }

    public int[] getAllocation_by_district() {
        return Allocation_by_district;
    }

    public double[] getAge_specific_vaccine_dist_common() {
        return Age_specific_vaccine_dist_common;
    }

    public double[] getAge_specific_vaccine_dist_rural() {
        return Age_specific_vaccine_dist_rural;
    }

    public double[] getAge_specific_vaccine_dist_urban() {
        return Age_specific_vaccine_dist_urban;
    }

    public double[][] getAge_specific_vaccine_dist_pack() {
        return Age_specific_vaccine_dist_pack;
    }
}
