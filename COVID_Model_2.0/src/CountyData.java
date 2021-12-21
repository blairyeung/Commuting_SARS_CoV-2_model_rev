public class CountyData {
    private int ID;
    private String Name;
    private int District;
    private double[] Coordinate;
    private int Population;
    private int County_Type;
    private int County_Category;/**Larger CMA, CA, or MIZ*/
    private CountyDataArray Past_data_series;
    private CountyDataArray Predicted_data_series;

    /**
     * @param Name
     * @param District
     * @param Coordinate
     * @param Population
     * @param County_Type
     */

    /**Store the demographic information of each county*/

    public CountyData(String Name, int District, double[] Coordinate, int Population, int County_Type){
        this.Name = Name;/**Name_of_the_county*/
        this.District = District;/**Code_of_the_distrizct*/
        this.Coordinate = Coordinate;/**Geographical_coordinate*/
        this.Population = Population;/**Total_population*/
        this.County_Type = County_Type;/**County_of_the_county: urban/rural*/
    }
    
    /**
     * @param County_ID
     * @param Name
     * @param District
     * @param Coordinate
     * @param Population
     * @param County_Type
     */

    public CountyData(int County_ID,String Name, int District, double[] Coordinate, int Population, int County_Type){
        this.ID = County_ID;
        this.Name = Name;/**Name_of_the_county*/
        this.District = District;/**Code_of_the_distrizct*/
        this.Coordinate = Coordinate;/**Geographical_coordinate*/
        this.Population = Population;/**Total_population*/
        this.County_Type = County_Type;/**County_of_the_county: urban/rural*/
    }

    /**
     * @param County_ID
     * @param Name
     * @param District
     * @param Coordinate
     * @param Population
     * @param County_Type
     * @param County_Category
     */

    public CountyData(int County_ID, String Name, int District, double[] Coordinate, int Population, int County_Type, int County_Category){
        this.ID = County_ID;
        this.Name = Name;/**Name_of_the_county*/
        this.District = District;/**Code_of_the_district*/
        this.Coordinate = Coordinate;/**Geographical_coordinate*/
        this.Population = Population;/**Total_population*/
        this.County_Type = County_Type;/**County_of_the_county: urban/rural*/
        this.County_Category = County_Category;/**Larger CMA, CA, or MIZ*/
    }

    /**|
     * @param County_ID
     * @param Name
     * @param District
     * @param Coordinate
     * @param Population
     * @param County_Type
     * @param County_Category
     * @param Past_data_series
     */

    public CountyData(int County_ID, String Name, int District, double[] Coordinate, int Population, int County_Type, int County_Category, CountyDataArray Past_data_series){
        this.ID = County_ID;
        this.Name = Name;/**Name_of_the_county*/
        this.District = District;/**Code_of_the_district*/
        this.Coordinate = Coordinate;/**Geographical_coordinate*/
        this.Population = Population;/**Total_population*/
        this.County_Type = County_Type;/**County_of_the_county: urban/rural*/
        this.County_Category = County_Category;/**Larger CMA, CA, or MIZ*/
        this.Past_data_series = Past_data_series;/**Past data of this county*/
    }

    /**
     * @param County_ID
     * @param Name
     * @param District
     * @param Coordinate
     * @param Population
     * @param County_Type
     * @param County_Category
     * @param Past_data_series
     * @param Predicted_data_series
     */

    public CountyData(int County_ID, String Name, int District, double[] Coordinate, int Population, int County_Type, int County_Category, CountyDataArray Past_data_series, CountyDataArray Predicted_data_series){
        this.ID = County_ID;
        this.Name = Name;/**Name_of_the_county*/
        this.District = District;/**Code_of_the_district*/
        this.Coordinate = Coordinate;/**Geographical_coordinate*/
        this.Population = Population;/**Total_population*/
        this.County_Type = County_Type;/**County_of_the_county: urban/rural*/
        this.County_Category = County_Category;/**Larger CMA, CA, or MIZ*/
        this.Past_data_series = Past_data_series;/**Past data of this county*/
        this.Predicted_data_series = Predicted_data_series;/**Future prediction of this county*/
    }

    public void setCounty_Category(int county_Category) {
        County_Category = county_Category;
    }

    public int getID() {
        return ID;
    }

    public String getName(){return Name;}
    public int getDistrict(){return District;}
    public double[] getCoordinate(){return Coordinate;}
    public int getPopulation(){return Population;}
    public int getCounty_Type(){return County_Type;}
    public int getCounty_Category() {
        return County_Category;
    }

    public void setPast_data_series(Data past_data, int Date) {
        Past_data_series.setDataWithinSeries(Date, past_data);
    }

    public void setPredicted_data_series(Data predicted_data, int Date) {
        Predicted_data_series.setDataWithinSeries(Date, predicted_data);
    }

    public void setPredicted_data_series(CountyDataArray predicted_data_series, int Date) {
        Predicted_data_series = predicted_data_series;
    }

    public CountyDataArray getPast_data_series() {
        return Past_data_series;
    }

    public CountyDataArray getPredicted_data_series() {
        return Predicted_data_series;
    }
}
