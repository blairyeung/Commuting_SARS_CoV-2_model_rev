public class CountyDataArray {

    /**
     * Object that contains time-series data of a single county
     */

    private Data time_series[];
    private String county_name;
    private int length;

    public CountyDataArray(String Name, int Size){
        this.county_name = Name;
        time_series = new Data[Size];
        for (int Date = 0; Date < Size; Date++) {
            time_series[Date] = new Data();
        }
    }

    public CountyDataArray(int Size){
        time_series = new Data[Size];
        for (int date = 0; date < Size; date++) {
            time_series[date] = new Data();
        }
    }


    public CountyDataArray() {
        time_series = new Data[Parameters.Observation_Range];
        for (int Date = 0; Date < Parameters.Observation_Range; Date++) {
            time_series[Date] = new Data();
        }
    }

    public void bindTimeSeries(Data CountyData){
        time_series[length] = CountyData;
        length++;
    }

    public int getLength(){
        return time_series.length;
    }

    public Data[] getTimeSeries(){
        return time_series;
    }
    public void setTimeSeries(Data[] Series){
        this.time_series = Series;
    }
    public void setDataWithinSeries(int day,Data Data){
        this.time_series[day] = Data;
    }

    public String getCounty_name() {
        return county_name;
    }
}
