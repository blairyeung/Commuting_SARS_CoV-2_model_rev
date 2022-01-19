public class County extends Thread{

    private String County_Name;
    private int County_Index;
    private int Population;
    private int Tier;

    private CountyDataArray Past_series;
    private CountyDataArray Series;
    private Data County_Data;

    public County(){
        County_Data = new Data();
        Series = new CountyDataArray();
    }

    /**
     * @param Population
     * @param Code
     */

    public County(int Population, int Code){
        System.out.println("Population = " + Population);
        System.out.println("Code = " + Code);
        this.Population = Population;
        this.County_Index = Code;
        County_Data = new Data();
        Series = new CountyDataArray(CountyDataIO.counties[Code].getName(),Parameters.Observation_Range);
        Series.setDataWithinSeries(Main.Day, County_Data);
    }

    /**
     * @param Population
     * @param Code
     * @param Past_series
     */

    public County(int Population, int Code, CountyDataArray Past_series){
        this.Population = Population;
        this.County_Index = Code;
        this.Past_series = Past_series;
        County_Data = new Data();
        Series = new CountyDataArray(CountyDataIO.counties[Code].getName(), Parameters.Observation_Range);
        Series.setDataWithinSeries(Main.Day, County_Data);
    }

    public void Run(){
    }


    public County_Return_Datapack Run_Model_with_flux(int Export, int Import, int Local_worker, int Vaccine_allocated, double[][] Age_specific_vaccine_distribution){

        if (Main.Day != 0) {
            County_Data = Series.getTimeSeries()[Main.Day];
        }

        else{
            County_Data = new Data();
            Series = new CountyDataArray(CountyDataIO.counties[County_Index].getName(), Parameters.Observation_Range);
            Series.setDataWithinSeries(Main.Day, County_Data);
        }

        double Incidence_rate = 0;
        int Incidence = 0;

        if(Main.Day>8){
            for (int i = 0; i < 7; i++) {
                Incidence += Series.getTimeSeries()[Main.Day-1-i].getEpidemiological_data_total()[18];
            }
        }

        Incidence_rate = ((double)Incidence)/((double) Population);


        this.Tier = FindResponseLevel.getResponseLevel(Incidence);

        Data NewData = SEIRModel.RunModel(Past_series,Series,County_Data, Import, Export, Tier, County_Index, Local_worker, Vaccine_allocated, Age_specific_vaccine_distribution);

        Series.setDataWithinSeries(Main.Day+1, NewData);

        System.out.print(CountyDataIO.counties[County_Index].getName() + "    ");
        if(NewData.getEpidemiological_data_total()[2]!=50){
            System.out.print("POP:  "+NewData.getEpidemiological_data_total()[1] + "    ");
            System.out.println(NewData.getEpidemiological_data_total()[2]);
            //ConsolePrint.PrintCountyInformation(Series.getTimeSeries()[Main.Day]);
        }
        //ConsolePrint.PrintInformation_By_Age(Series.getTimeSeries()[Main.Day]);

        return (new County_Return_Datapack(Incidence_rate, Incidence, (int) NewData.getEpidemiological_data_total()[16]));

    }

    public void Run_Model(int Export, int Import, int Local_worker,int VaccineAllocated, double[][] Age_specific_vaccine_distribution){

        if (Main.Day != 0) {
            County_Data = Series.getTimeSeries()[Main.Day];
        }

        else{
            County_Data = new Data();
        }

        double Incidence = 0;

        if(Main.Day>8){
            for (int i = 0; i < 7; i++) {
                Incidence += Series.getTimeSeries()[Main.Day-1-i].getEpidemiological_data_total()[18];
            }
            Incidence /= Population;
        }

        this.Tier = FindResponseLevel.getResponseLevel(Incidence);

        Data NewData = SEIRModel.RunModel(Past_series, Series, County_Data, Import, Export, Tier, County_Index, Local_worker, VaccineAllocated, Age_specific_vaccine_distribution);

        Series.setDataWithinSeries(Main.Day+1, NewData);

        //ConsolePrint.PrintCountyInformation(Series.getTimeSeries()[Main.Day]);
    }

    public void Run_Model_Resident(int VaccineAllocated, double[][] Age_specific_vaccine_distribution){

        County_Data = Series.getTimeSeries()[Main.Day+1];


        double Incidence = 0;

            if(Main.Day>8){
                for (int i = 0; i < 7; i++) {
                    Incidence += Series.getTimeSeries()[Main.Day-1-i].getEpidemiological_data_total()[4];
                }
                Incidence /= Population;
            }

            int Imported = 0;
            int Exported = 0;

            this.Tier = FindResponseLevel.getResponseLevel(Incidence);


            //Data NewData = SEIRModel.NightModel(Past_series, Series, County_Data, Imported, Exported, Tier, County_Index, VaccineAllocated, Age_specific_vaccine_distribution);

            //Series.setDataWithinSeries(Main.Day+1, NewData);

        //ConsolePrint.PrintCountyInformation(Series.getTimeSeries()[Main.Day]);

    }

    public void setImporting(int Imported_cases){

        if(Imported_cases==0){
            return;
        }

        double Calculated_total = 0;

        if(Imported_cases<=100){
            for (int Age_band = 0; Age_band < 16; Age_band++) {
                double Expected_cases = Parameters.Workforce_Age_Dist[Age_band] * Imported_cases;
                int this_age_band = (int) Math.round(Expected_cases + 0.5 * Math.random());
                Calculated_total += this_age_band;
            }
        }
        else {
            for (int Age_band = 0; Age_band < 16; Age_band++) {
                double Expected_cases = Parameters.Workforce_Age_Dist[Age_band] * Imported_cases;
                int this_age_band = (int) Math.round(Expected_cases);
                Calculated_total += this_age_band;
                int Variant = 0;
                Series.getTimeSeries()[Main.Day+1].setValueDataPackByAge(Variant, Age_band, 15, this_age_band);
            }
        }

        double ratio = Calculated_total/ ((double)Imported_cases);

        //System.out.println("ratio + " + ratio);

        Series.getTimeSeries()[Main.Day+1].setValueDataPack(15 , Imported_cases);

    }

    public void PrintFile(Trail TrailData){
        System.out.println(County_Index);
        FilePrint.Print_all(Series, TrailData, County_Index);
        FilePrint.Print_all(Series, TrailData, County_Index);
    }


    public void PrintPastFile(Trail TrailData){
        System.out.println(County_Index);
        FilePrint.Print_all(Past_series, TrailData, County_Index);
        FilePrint.Print_all(Past_series, TrailData, County_Index);
    }

    public CountyDataArray getSeries() {
        return Series;
    }

    public CountyDataArray getPast_series() {
        return Past_series;
    }

    public int getCounty_Index() {
        return County_Index;
    }
}