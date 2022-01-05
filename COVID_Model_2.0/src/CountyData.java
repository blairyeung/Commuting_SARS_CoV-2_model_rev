public class CountyData {
    private int id;
    private String name;
    private int district;
    private double[] coordinate;
    private int population;
    private int county_type;
    private int county_Category;/**Larger CMA, CA, or MIZ*/
    private CountyDataArray  past_data_series;
    private CountyDataArray predicted_data_series;

    /**
     * @param Name
     * @param District
     * @param Coordinate
     * @param Population
     * @param County_Type
     */

    /**Store the demographic information of each county*/

    public CountyData(String Name, int District, double[] Coordinate, int Population, int County_Type){
        this.name = Name;/**Name_of_the_county*/
        this.district = District;/**Code_of_the_distrizct*/
        this.coordinate = Coordinate;/**Geographical_coordinate*/
        this.population = Population;/**Total_population*/
        this.county_type = County_Type;/**County_of_the_county: urban/rural*/
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
        this.id = County_ID;
        this.name = Name;/**Name_of_the_county*/
        this.district = District;/**Code_of_the_distrizct*/
        this.coordinate = Coordinate;/**Geographical_coordinate*/
        this.population = Population;/**Total_population*/
        this.county_type = County_Type;/**County_of_the_county: urban/rural*/
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
        this.id = County_ID;
        this.name = name;/**Name_of_the_county*/
        this.district = District;/**Code_of_the_distrizct*/
        this.coordinate = Coordinate;/**Geographical_coordinate*/
        this.population = Population;/**Total_population*/
        this.county_type = County_Type;/**County_of_the_county: urban/rural*/
        this.county_Category = County_Category;/**Larger CMA, CA, or MIZ*/
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
        this.id = County_ID;
        this.name = name;/**Name_of_the_county*/
        this.district = District;/**Code_of_the_distrizct*/
        this.coordinate = Coordinate;/**Geographical_coordinate*/
        this.population = Population;/**Total_population*/
        this.county_type = County_Type;/**County_of_the_county: urban/rural*/
        this.county_Category = County_Category;/**Larger CMA, CA, or MIZ*/
        this.past_data_series = Past_data_series;/**Past data of this county*/
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
        this.id = County_ID;
        this.name = name;/**Name_of_the_county*/
        this.district = District;/**Code_of_the_distrizct*/
        this.coordinate = Coordinate;/**Geographical_coordinate*/
        this.population = Population;/**Total_population*/
        this.county_type = County_Type;/**County_of_the_county: urban/rural*/
        this.county_Category = County_Category;/**Larger CMA, CA, or MIZ*/
        this.past_data_series = Past_data_series;/**Past data of this county*/
        this.predicted_data_series = Predicted_data_series;/**Future prediction of this county*/
    }

    public void setCounty_Category(int county_Category) {
        this.county_Category = county_Category;
    }

    public int getID() {
        return id;
    }

    public String getName(){return name;}
    public int getDistrict(){return district;}
    public double[] getCoordinate(){return coordinate;}
    public int getPopulation(){return population;}
    public int getCounty_Type(){return county_type;}
    public int getCounty_Category() {
        return county_Category;
    }

    public void setPast_data_series(Data past_data, int Date) {past_data_series.setDataWithinSeries(Date, past_data);
    }

    public void setPredicted_data_series(Data predicted_data, int Date) {
        predicted_data_series.setDataWithinSeries(Date, predicted_data);
    }

    public void setPredicted_data_series(CountyDataArray predicted_data_series, int Date) {
        predicted_data_series = predicted_data_series;
    }

    public CountyDataArray getPast_data_series() {
        return past_data_series;
    }

    public CountyDataArray getPredicted_data_series() {
        return predicted_data_series;
    }
}
