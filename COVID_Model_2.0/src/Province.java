public class Province {
    public Trail trail_data = null;
    public int[] vaccine_distribution = null;
    public County[] counties = null;
    public int[] case_migration = null;/**Case migrated from*/
    public int[] case_migrated = null;/**Case migrated from*/

    public int[] county_response_tier = null;
    public double[] weekly_incidence_rate_by_county = null;

    public int[][] worker_outflux_matrix = null;
    public int[] worker_outflux_total = null;
    public int[][] worker_influx_matrix = null;
    public int[] worker_influx_total = null;

    public int[] total_influx_cases = null;
    public int[] total_outflux_cases = null;

    public double[] Age_specific_vaccine_dist_common = null;
    public double[] Age_specific_vaccine_dist_urban = null;
    public double[] Age_specific_vaccine_dist_rural  = null;
    public double[][] Age_specific_vaccine_dist_pack  = null;

    public Vaccine_allocation allocation;

    public Province(){
        Commute.getStaticCommuteMatrix();

        int Number_of_Counties = CountyDataIO.counties.length;

        vaccine_distribution = new int[Number_of_Counties];
        county_response_tier = new int[Number_of_Counties];
        weekly_incidence_rate_by_county = new double[Number_of_Counties];
        case_migration = new int[Number_of_Counties];
        case_migrated = new int[Number_of_Counties];
        counties = new County[Number_of_Counties];
        worker_outflux_total = new int[Number_of_Counties];
        worker_influx_total = new int[Number_of_Counties];
        total_influx_cases = new int[Number_of_Counties];
        total_outflux_cases = new int[Number_of_Counties];

        for (int i = 0; i < Number_of_Counties; i++) {
            vaccine_distribution[i] = 0;
        }

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            int Population = CountyDataIO.counties[County_Code].getPopulation();
            counties[County_Code] = new County(Population, County_Code, Ontario_past_data_IO.Ontario_past_data_array[County_Code]);
        }
    }

    public Province(Trail TrailSingle){

        Commute.getStaticCommuteMatrix();

        trail_data = TrailSingle;

        int Number_of_Counties = CountyDataIO.counties.length;

        vaccine_distribution = new int[Number_of_Counties];
        county_response_tier = new int[Number_of_Counties];
        weekly_incidence_rate_by_county = new double[Number_of_Counties];
        case_migration = new int[Number_of_Counties];
        case_migrated = new int[Number_of_Counties];
        counties = new County[Number_of_Counties];
        worker_outflux_total = new int[Number_of_Counties];
        worker_influx_total = new int[Number_of_Counties];
        total_influx_cases = new int[Number_of_Counties];
        total_outflux_cases = new int[Number_of_Counties];

        for (int i = 0; i < Number_of_Counties; i++) {
            vaccine_distribution[i] = 0;
        }

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            System.out.println("County: " + CountyDataIO.counties[County_Code].getName());
            System.out.println("Population: " + CountyDataIO.counties[County_Code].getPopulation());
            int Population = CountyDataIO.counties[County_Code].getPopulation();
            counties[County_Code] = new County(Population, County_Code, Ontario_past_data_IO.Ontario_past_data_array[County_Code]);
        }

        allocation = new Vaccine_allocation(0);
        vaccine_distribution = allocation.getAllocation_by_county();
        Age_specific_vaccine_dist_common = allocation.getAge_specific_vaccine_dist_common();
        Age_specific_vaccine_dist_rural = allocation.getAge_specific_vaccine_dist_rural();
        Age_specific_vaccine_dist_urban = allocation.getAge_specific_vaccine_dist_urban();
        Age_specific_vaccine_dist_pack = allocation.getAge_specific_vaccine_dist_pack();
    }

    public void ModelIterator(){

        int Number_of_Counties = CountyDataIO.counties.length;

        total_influx_cases = new int[Number_of_Counties];
        total_outflux_cases = new int[Number_of_Counties];

        case_migration = Commute_IO.getNumber_of_commuters_departing();
        case_migrated = Commute_IO.getNumber_of_commuters_arriving();

        worker_outflux_matrix = Commute_IO.getCommuting_matrix();
        worker_influx_matrix = Commute_IO.getReverse_Commuting_matrix();

        /**
         * Calculate total number of workers
         * Then calculate the proportion of new infections that are commuters
         * Output
         */

        Model multi_thread_models[] = new Model[Number_of_Counties];

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            int Export = case_migration[County_Code];
            int Import = case_migrated[County_Code];
            int Local_worker = Commute_IO.local_worker_matrix[County_Code];
            multi_thread_models[County_Code] = new Model(0, counties[County_Code],Export,Import, Local_worker, Age_specific_vaccine_dist_pack, vaccine_distribution[County_Code]);
            multi_thread_models[County_Code].start();
        }

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            County_Return_Datapack returnedpack = null;
            while(multi_thread_models[County_Code].pack==null){
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            returnedpack = multi_thread_models[County_Code].pack;
            int Exported_from_county = returnedpack.getExported_cases();
            weekly_incidence_rate_by_county[County_Code] = returnedpack.getIncidence_rate();

            worker_outflux_total[County_Code] = Math.max(Exported_from_county, 0);
        }

        Migration_Assign();
        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            if(total_influx_cases[County_Code]!=0){
                counties[County_Code].setImporting(total_influx_cases[County_Code]);
            }
        }

        multi_thread_models = new Model[Number_of_Counties];

        for (int County_code = 0; County_code < Number_of_Counties; County_code++) {
            //counties[County_code].Run_Model_Resident( 0, Age_specific_vaccine_dist_pack);
            multi_thread_models[County_code] = new Model(1, counties[County_code], Age_specific_vaccine_dist_pack, vaccine_distribution[County_code]);
            multi_thread_models[County_code].start();
        }
    }

    public void Debug(){
        System.out.println("County_Name,County_Exportation,County_Importation");
        for (int County_Code = 0; County_Code < CountyDataIO.counties.length; County_Code++) {
            System.out.println(CountyDataIO.counties[County_Code].getName() + "," +  Commute.getExportations()[County_Code] + "," + Commute.getImportations()[County_Code] + "," + CountyDataIO.counties[County_Code].getPopulation());
        }
    }

    public void printToFile(){

        //Directory_Creator.create_dir(Parameters.WritePath + trail_data.getModel_Iteratons()+"/"+trail_data.getTrail_Code() +"/");

        for (int County = 0; County < CountyDataIO.counties.length; County++) {
                counties[County].PrintFile(trail_data);
        }
        /*
        for (int District = 0; District < CountyDataIO.Counties_By_District.length; District++) {
            CountyDataArray[] District_data = new CountyDataArray[CountyDataIO.Counties_By_District[District].size()];
            for (int County_within_district = 0; County_within_district < CountyDataIO.Counties_By_District[District].size(); County_within_district++) {
                int County_index = CountyDataIO.Code_Index.indexOf(CountyDataIO.Counties_By_District[District].get(County_within_district).getID());
                District_data[County_within_district] = counties[County_index].getSeries();
            }
                FilePrint.Print_by_district_to_one_file(District_data, CountyDataIO.DistrictCodes[District], trail_data);
                FilePrint.Print_by_district_by_age(District_data, CountyDataIO.DistrictCodes[District], trail_data);
        }*/

        CountyDataArray[] Province_Data = new CountyDataArray[CountyDataIO.counties.length];

        for (int County = 0; County < CountyDataIO.counties.length; County++) {
            counties[County].PrintFile(trail_data);
            Province_Data[County] = counties[County].getSeries();
        }
        FilePrint.Print_ALL(Province_Data, trail_data);
        FilePrint.Print_ALL_by_age(Province_Data, trail_data);

    }

    public void printToConsole(){
        for (int County = 0; County < CountyDataIO.counties.length; County++) {
            System.out.println("County_Name: " + CountyDataIO.counties[County].getName());
            for (int Date = 0; Date < Parameters.Observation_Range; Date++) {
                ConsolePrint.PrintCountyInformation(counties[County].getSeries().getTimeSeries()[Date]);
            }
        }
    }

    public void Migration_Assign(){
        /**
         * Generate Commute flux matrix
         */
        int Number_of_counties = CountyDataIO.counties.length;
        for (int Depart = 0; Depart < Number_of_counties; Depart++) {
            int total_cases=worker_outflux_total[Depart];/** Number of cases leaving this county*/

            int total_going_back_to_resident = Commute_IO.getNumber_of_commuters_arriving()[Depart];

            double Iterated = 0;

            if(total_cases==0){
                continue;
            }
            else {
                //System.out.println("Total: " + total_cases);
            }

            int[] Cases_Migrated = Function.RandomAssign(Commute_IO.getCommuting_matrix()[Depart] ,worker_outflux_total[Depart]);

            for (int i = 0; i < Cases_Migrated.length; i++) {
                total_influx_cases[i] += Cases_Migrated[i];
            }
        }
    }

    public void clear(){
        trail_data = null;
        vaccine_distribution = null;
        counties = null;
        case_migration = null;/**Case migrated from*/
        case_migrated = null;/**Case migrated from*/
        county_response_tier = null;
        weekly_incidence_rate_by_county = null;
        worker_outflux_matrix = null;
        worker_outflux_total = null;
        worker_influx_matrix = null;
        worker_influx_total = null;
        total_influx_cases = null;
        total_outflux_cases = null;
    }

    public int[] getCase_migration() {
        return case_migration;
    }

    public int[] getCase_migrated() {
        return case_migrated;
    }

    public int[] getCounty_response_tier() {
        return county_response_tier;
    }

    public int[] getVaccine_distribution() {
        return vaccine_distribution;
    }

    public void setCase_migration(int[] case_migration) {
        this.case_migration = case_migration;
    }

    public void setCase_migrated(int[] case_migrated) {
        case_migrated = case_migrated;
    }

    public void setCounty_response_tier(int[] county_response_tier) {
        county_response_tier = county_response_tier;
    }

    public void setVaccine_distribution(int[] vaccine_distribution) {
        this.vaccine_distribution = vaccine_distribution;
    }

    public void setCase_migration_Value(int[] case_migration) {
        this.case_migration = case_migration;
    }

    public void setCase_migrated_Value(int[] case_migrated) {
        case_migrated = case_migrated;
    }

    public int[] getCase_flux_matrix_Total() {
        return worker_outflux_total;
    }

    public int[] getReverse_Case_flux_matrix_Total() {
        return worker_influx_total;
    }

    public int[][] getCase_flux_matrix() {
        return worker_outflux_matrix;
    }

    public int[][] getReverse_Case_flux_matrix() {
        return worker_influx_matrix;
    }
}
