public class Province {
    public Trail TrailData = null;
    public int[] VaccinationDistribution = null;
    public County[] Counties = null;
    public int[] Case_Migrating = null;/**Case migrated from*/
    public int[] Case_Migrated = null;/**Case migrated from*/

    public int[] County_response_tier = null;
    public double[] Weekly_Incidence_Rate_By_County = null;

    public int[][] Case_flux_matrix = null;
    public int[] Case_flux_matrix_Total = null;
    public int[][] Reverse_Case_flux_matrix = null;
    public int[] Reverse_Case_flux_matrix_Total = null;

    public int[] Importing_Case_flux_matrix_Total = null;
    public int[] Exporting_Case_flux_matrix_Total = null;

    public double[] Age_specific_vaccine_dist_common = null;
    public double[] Age_specific_vaccine_dist_urban = null;
    public double[] Age_specific_vaccine_dist_rural  = null;
    public double[][] Age_specific_vaccine_dist_pack  = null;

    public Vaccine_allocation allocation;

    public Province(){
        Commute.getStaticCommuteMatrix();

        int Number_of_Counties = CountyDataIO.Counties.length;

        VaccinationDistribution = new int[Number_of_Counties];
        County_response_tier = new int[Number_of_Counties];
        Weekly_Incidence_Rate_By_County = new double[Number_of_Counties];
        Case_Migrating = new int[Number_of_Counties];
        Case_Migrated = new int[Number_of_Counties];
        Counties = new County[Number_of_Counties];
        Case_flux_matrix_Total = new int[Number_of_Counties];
        Reverse_Case_flux_matrix_Total = new int[Number_of_Counties];
        Importing_Case_flux_matrix_Total = new int[Number_of_Counties];
        Exporting_Case_flux_matrix_Total = new int[Number_of_Counties];

        for (int i = 0; i < Number_of_Counties; i++) {
            VaccinationDistribution[i] = 0;
        }

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            int Population = CountyDataIO.Counties[County_Code].getPopulation();
            Counties[County_Code] = new County(Population, County_Code, Ontario_past_data_IO.Ontario_past_data_array[County_Code]);
        }
    }

    public Province(Trail TrailSingle){

        Commute.getStaticCommuteMatrix();

        TrailData = TrailSingle;

        int Number_of_Counties = CountyDataIO.Counties.length;

        VaccinationDistribution = new int[Number_of_Counties];
        County_response_tier = new int[Number_of_Counties];
        Weekly_Incidence_Rate_By_County = new double[Number_of_Counties];
        Case_Migrating = new int[Number_of_Counties];
        Case_Migrated = new int[Number_of_Counties];
        Counties = new County[Number_of_Counties];
        Case_flux_matrix_Total = new int[Number_of_Counties];
        Reverse_Case_flux_matrix_Total = new int[Number_of_Counties];
        Importing_Case_flux_matrix_Total = new int[Number_of_Counties];
        Exporting_Case_flux_matrix_Total = new int[Number_of_Counties];

        for (int i = 0; i < Number_of_Counties; i++) {
            VaccinationDistribution[i] = 0;
        }

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            System.out.println("County: " + CountyDataIO.Counties[County_Code].getName());
            System.out.println("Population: " + CountyDataIO.Counties[County_Code].getPopulation());
            int Population = CountyDataIO.Counties[County_Code].getPopulation();
            Counties[County_Code] = new County(Population, County_Code, Ontario_past_data_IO.Ontario_past_data_array[County_Code]);
        }

        allocation = new Vaccine_allocation(0);
        VaccinationDistribution = allocation.getAllocation_by_county();
        Age_specific_vaccine_dist_common = allocation.getAge_specific_vaccine_dist_common();
        Age_specific_vaccine_dist_rural = allocation.getAge_specific_vaccine_dist_rural();
        Age_specific_vaccine_dist_urban = allocation.getAge_specific_vaccine_dist_urban();
        Age_specific_vaccine_dist_pack = allocation.getAge_specific_vaccine_dist_pack();
    }

    public void ModelIterator(){

        int Number_of_Counties = CountyDataIO.Counties.length;

        Importing_Case_flux_matrix_Total = new int[Number_of_Counties];
        Exporting_Case_flux_matrix_Total = new int[Number_of_Counties];

        Case_Migrating = Commute_IO.getNumber_of_commuters_departing();
        Case_Migrated = Commute_IO.getNumber_of_commuters_arriving();

        Case_flux_matrix = Commute_IO.getCommuting_matrix();
        Reverse_Case_flux_matrix = Commute_IO.getReverse_Commuting_matrix();


        if(Main.Day%7==1){

            /**
             * Response tier will update weekly
             */

            for (int County_Code = 0; County_Code < Counties.length; County_Code++) {
                County_response_tier[County_Code] = FindResponseLevel.getResponseLevel(Weekly_Incidence_Rate_By_County[County_Code]);
            }
            Commute_IO.generateWeeklyMatrix(County_response_tier);
            VaccinationDistribution = allocation.getAllocation_by_county();
        }


        /**
         * Calculate total number of workers
         * Then calculate the proportion of new infections that are commuters
         * Output
         */

        Model multi_thread_models[] = new Model[Number_of_Counties];

        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {

            int Export = Case_Migrating[County_Code];
            int Import = Case_Migrated[County_Code];
            int Local_worker = Commute_IO.Local_worker_array[County_Code];
            multi_thread_models[County_Code] = new Model(0, Counties[County_Code],Export,Import, Local_worker, Age_specific_vaccine_dist_pack, VaccinationDistribution[County_Code]);
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
            Weekly_Incidence_Rate_By_County[County_Code] = returnedpack.getIncidence_rate();

            Case_flux_matrix_Total[County_Code] = Math.max(Exported_from_county, 0);
        }

        Migration_Assign();
        for (int County_Code = 0; County_Code < Number_of_Counties; County_Code++) {
            if(Importing_Case_flux_matrix_Total[County_Code]!=0){
                Counties[County_Code].setImporting(Importing_Case_flux_matrix_Total[County_Code]);
            }
        }

        multi_thread_models = new Model[Number_of_Counties];

        for (int County_code = 0; County_code < Number_of_Counties; County_code++) {
            //Counties[County_code].Run_Model_Resident( 0, Age_specific_vaccine_dist_pack);
            multi_thread_models[County_code] = new Model(1, Counties[County_code], Age_specific_vaccine_dist_pack, VaccinationDistribution[County_code]);
            multi_thread_models[County_code].start();
        }
    }

    public void Debug(){
        System.out.println("County_Name,County_Exportation,County_Importation");
        for (int County_Code = 0; County_Code < CountyDataIO.Counties.length; County_Code++) {
            System.out.println(CountyDataIO.Counties[County_Code].getName() + "," +  Commute.getExportations()[County_Code] + "," + Commute.getImportations()[County_Code] + "," + CountyDataIO.Counties[County_Code].getPopulation());
        }
    }

    public void printToFile(){

        //Directory_Creator.create_dir(Parameters.WritePath + TrailData.getModel_Iteratons()+"/"+TrailData.getTrail_Code() +"/");

        for (int County = 0; County < CountyDataIO.Counties.length; County++) {
                Counties[County].PrintFile(TrailData);
        }
        /*
        for (int District = 0; District < CountyDataIO.Counties_By_District.length; District++) {
            CountyDataArray[] District_data = new CountyDataArray[CountyDataIO.Counties_By_District[District].size()];
            for (int County_within_district = 0; County_within_district < CountyDataIO.Counties_By_District[District].size(); County_within_district++) {
                int County_index = CountyDataIO.Code_Index.indexOf(CountyDataIO.Counties_By_District[District].get(County_within_district).getID());
                District_data[County_within_district] = Counties[County_index].getSeries();
            }
                FilePrint.Print_by_district_to_one_file(District_data, CountyDataIO.DistrictCodes[District], TrailData);
                FilePrint.Print_by_district_by_age(District_data, CountyDataIO.DistrictCodes[District], TrailData);
        }*/

        CountyDataArray[] Province_Data = new CountyDataArray[CountyDataIO.Counties.length];

        for (int County = 0; County < CountyDataIO.Counties.length; County++) {
            Counties[County].PrintFile(TrailData);
            Province_Data[County] = Counties[County].getSeries();
        }
        FilePrint.Print_ALL(Province_Data, TrailData);
        FilePrint.Print_ALL_by_age(Province_Data, TrailData);

    }

    public void printToConsole(){
        for (int County = 0; County < CountyDataIO.Counties.length; County++) {
            System.out.println("County_Name: " + CountyDataIO.Counties[County].getName());
            for (int Date = 0; Date < Parameters.Observation_Range; Date++) {
                ConsolePrint.PrintCountyInformation(Counties[County].getSeries().getTimeSeries()[Date]);
            }
        }
    }

    public void Migration_Assign(){
        /**
         * Generate Commute flux matrix
         */
        int Number_of_counties = CountyDataIO.Counties.length;
        for (int Depart = 0; Depart < Number_of_counties; Depart++) {
            int total_cases=Case_flux_matrix_Total[Depart];/** Number of cases leaving this county*/

            int total_going_back_to_resident = Commute_IO.getNumber_of_commuters_arriving()[Depart];

            double Iterated = 0;

            if(total_cases==0){
                continue;
            }
            else {
                //System.out.println("Total: " + total_cases);
            }

            int[] Cases_Migrated = Function.RandomAssign(Commute_IO.getCommuting_matrix()[Depart] ,Case_flux_matrix_Total[Depart]);

            for (int i = 0; i < Cases_Migrated.length; i++) {
                Importing_Case_flux_matrix_Total[i] += Cases_Migrated[i];
            }
        }
    }

    public void clear(){
        TrailData = null;
        VaccinationDistribution = null;
        Counties = null;
        Case_Migrating = null;/**Case migrated from*/
        Case_Migrated = null;/**Case migrated from*/
        County_response_tier = null;
        Weekly_Incidence_Rate_By_County = null;
        Case_flux_matrix = null;
        Case_flux_matrix_Total = null;
        Reverse_Case_flux_matrix = null;
        Reverse_Case_flux_matrix_Total = null;
        Importing_Case_flux_matrix_Total = null;
        Exporting_Case_flux_matrix_Total = null;
    }

    public int[] getCase_Migrating() {
        return Case_Migrating;
    }

    public int[] getCase_Migrated() {
        return Case_Migrated;
    }

    public int[] getCounty_response_tier() {
        return County_response_tier;
    }

    public int[] getVaccinationDistribution() {
        return VaccinationDistribution;
    }

    public void setCase_Migrating(int[] Case_Migrating) {
        Case_Migrating = Case_Migrating;
    }

    public void setCase_Migrated(int[] case_Migrated) {
        Case_Migrated = case_Migrated;
    }

    public void setCounty_response_tier(int[] county_response_tier) {
        County_response_tier = county_response_tier;
    }

    public void setVaccinationDistribution(int[] vaccinationDistribution) {
        VaccinationDistribution = vaccinationDistribution;
    }

    public void setCase_Migrating_Value(int[] Case_Migrating) {
        Case_Migrating = Case_Migrating;
    }

    public void setCase_Migrated_Value(int[] Case_Migrated) {
        Case_Migrated = Case_Migrated;
    }

    public int[] getCase_flux_matrix_Total() {
        return Case_flux_matrix_Total;
    }

    public int[] getReverse_Case_flux_matrix_Total() {
        return Reverse_Case_flux_matrix_Total;
    }

    public int[][] getCase_flux_matrix() {
        return Case_flux_matrix;
    }

    public int[][] getReverse_Case_flux_matrix() {
        return Reverse_Case_flux_matrix;
    }
}
