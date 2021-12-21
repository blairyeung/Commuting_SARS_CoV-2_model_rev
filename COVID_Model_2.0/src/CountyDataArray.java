public class CountyDataArray {

    /**
     * The place where all the data were stored
     */

    private Data TimeSeries[];
    private String CountyName;
    private int Length;

    public CountyDataArray(String Name, int Size){
        this.CountyName = Name;
        TimeSeries = new Data[Size];
        for (int Date = 0; Date < Size; Date++) {
            TimeSeries[Date] = new Data();
        }
    }

    public CountyDataArray(int Size){
        TimeSeries = new Data[Size];
        for (int Date = 0; Date < Size; Date++) {
            TimeSeries[Date] = new Data();
        }
    }


    public CountyDataArray() {
        TimeSeries = new Data[Parameters.Observation_Range];
        for (int Date = 0; Date < Parameters.Observation_Range; Date++) {
            TimeSeries[Date] = new Data();
        }
    }

    public void bindTimeSeries(Data CountyData){
        //System.out.println("Going to bind");
        //ConsolePrint.PrintCountyInformation(CountyData);
        //TimeSeries[Main.Day] = CountyData;
        TimeSeries[Length] = CountyData;
        Length++;
    }

    public int getLength(){
        return TimeSeries.length;
    }

    public Data[] getTimeSeries(){
        return TimeSeries;
    }
    public void setTimeSeries(Data[] Series){
        this.TimeSeries = Series;
    }
    public void setDataWithinSeries(int day,Data Data){
        this.TimeSeries[day] = Data;
    }
}
