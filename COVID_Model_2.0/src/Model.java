import javax.swing.*;
import java.util.Random;

public class Model extends Thread{

    private int Mode;
    private County county;

    int Export;
    int Import;
    int Local_Worker;

    int Vaccine_Allocated;
    double[][] Age_specific_vaccine_distribution;

    County_Return_Datapack pack = null;

    /**
     * @param Mode
     * @param county
     * @param Age_specific_vaccine_distribution
     * @param Vaccine_Allocated
     */

    public Model(int Mode, County county, double[][] Age_specific_vaccine_distribution, int Vaccine_Allocated){
        /**
         * Night Model
         */
        this.Mode = Mode;
        this.county = county;

        this.Vaccine_Allocated = Vaccine_Allocated;
        this.Age_specific_vaccine_distribution = Age_specific_vaccine_distribution;
    }

    /**
     * @param Mode
     * @param county
     * @param Export
     * @param Import
     * @param Local_Worker
     * @param Age_specific_vaccine_distribution
     * @param Vaccine_Allocated
     */

    public Model(int Mode, County county, int Export, int Import, int Local_Worker,double[][] Age_specific_vaccine_distribution, int Vaccine_Allocated){
        /**
         * Day Model
         */
        this.Mode = Mode;
        this.county = county;

        this.Export = Export;
        this.Import = Import;
        this.Local_Worker = Local_Worker;

        this.Vaccine_Allocated = Vaccine_Allocated;
        this.Age_specific_vaccine_distribution = Age_specific_vaccine_distribution;
    }


    public void run(){
        switch (Mode){
            case 0:
                pack = county.Run_Model_with_flux(Export, Import, Local_Worker, Vaccine_Allocated, Age_specific_vaccine_distribution);
            case 1:
                county.Run_Model_Resident(Vaccine_Allocated, Age_specific_vaccine_distribution);
            default: return;
        }

    }

    public County_Return_Datapack getPack() {
        return pack;
    }
}
