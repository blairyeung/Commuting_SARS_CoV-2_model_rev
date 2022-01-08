//import jdk.swing.interop.SwingInterOpUtils;

public class Presets {

    private int PresetCode;

    private int[] Initial_Cases_by_County;
    private int[] Initial_Immunity_by_County;
    private double[] Initial_Cases_Age_Band;
    private double[] Initial_Immunity_Age_Band;
    private int[][] Initial_Cases_Age_Band_By_County;
    private int[][] Initial_Immunity_Age_Band_By_County;

    public Initial_Parameters getPreset(int Preset_Code){

        Initial_Cases_by_County = new int[CountyDataIO.counties.length];
        Initial_Immunity_by_County = new int[CountyDataIO.counties.length];
        Initial_Cases_Age_Band = new double[16];
        Initial_Immunity_Age_Band = new double[16];
        Initial_Cases_Age_Band_By_County = new int[CountyDataIO.counties.length][16];
        Initial_Immunity_Age_Band_By_County = new int[CountyDataIO.counties.length][16];

        Initial_Parameters Initial = null;

        switch (Preset_Code){
            case 0:
                Initial_Cases_Age_Band = Parameters.Workforce_Age_Dist;
                for (int i = 0; i < 16; i++) {
                    Initial_Immunity_Age_Band[i] = 0.2;
                }
                /**
                 * Covid-19 cases distributed evenly across Ontario
                 * 1% IncidenceRate
                 * Current Immunity_Matrix estimated from the number of deaths using three models
                 *
                 */

                /**
                 * Incidence_Rate: 1%
                 * Immunized_Proportion: 20%
                 * Numbers estimated from models by Davies et al. and Yang et al.
                 */

                int Num_Counties = CountyDataIO.counties.length;

                double Incidence_constant = 0.01;
                double Immunity_level_constant = 0.2;
                for (int County_Code = 0; County_Code < Num_Counties; County_Code++) {

                    int Population = CountyDataIO.counties[County_Code].getPopulation();


                    for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                        Initial_Cases_Age_Band_By_County[County_Code][Age_Band] = (int) Math.round(Initial_Cases_Age_Band[Age_Band] * Incidence_constant * ((double) Population));
                        Initial_Immunity_Age_Band_By_County[County_Code][Age_Band] = (int) Math.round(Initial_Immunity_Age_Band[Age_Band] * Immunity_level_constant * ((double) Population));

                     }
                }

                Initial = new Initial_Parameters(Initial_Cases_Age_Band_By_County, Initial_Immunity_Age_Band_By_County);

            case 1:
            default:
        }

        return Initial;
    }

    public Presets(int Preset_Code){
        this.PresetCode = Preset_Code;
    }

}
