import java.util.ArrayList;

public class Data {

    private double epidemiological_data_total[];
    private double epidemiological_data_by_variant[][];
    private double epidemiological_data_by_age[][][] ;

    private double InitialInfected = 50;

    /**Store the epidemiological information of each county of each day*/

    public Data(){
        epidemiological_data_total = new double[Parameters.DataPackSize];
        epidemiological_data_by_variant = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize];
        epidemiological_data_by_age = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize][Parameters.AgeBand.length];
    }

    public void reCalculate(){
        for (int index = 0; index < Parameters.DataPackSize; index++) {
            double subtotal = 0;
            for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
                for (int Age_band = 0; Age_band < Parameters.AgeBand.length; Age_band++) {
                    subtotal += epidemiological_data_by_age[variant][index][Age_band];
                }
            }
            epidemiological_data_total[index] = subtotal;
        }
    }

    public void setDataPack(double[] epidemiological_data_total, double[][][] epidemiological_data_by_age){
        this.epidemiological_data_total = epidemiological_data_total;
        this.epidemiological_data_by_age = epidemiological_data_by_age;
    }

    public double[] getEpidemiological_data_total(){
        return epidemiological_data_total;
    }

    public double[][] getDataPackByVariant(){
        return epidemiological_data_by_variant;
    }


    public double[][][] getDataPackByAge(){return epidemiological_data_by_age;}

    public void setDataPackByAge(double[][][] Package){epidemiological_data_by_age = Package;}

    public void setValueDataPack(int Index, double Value){epidemiological_data_total[Index] = Value;}

    public void addValueDataPack(int Index, double Delta_Value){epidemiological_data_total[Index] += Delta_Value;}

    public void setValueDataPackByAge(int Variant_index,int AgeBand_index, int Index, double Value){
        epidemiological_data_by_age[Variant_index][Index][AgeBand_index] = Value;
    }

    public void addValueDataPackByAge(int Variant_index,int AgeBand_index, int Index, double Delta_Value){
        epidemiological_data_by_age[Variant_index][Index][AgeBand_index] += Delta_Value;
    }
}
