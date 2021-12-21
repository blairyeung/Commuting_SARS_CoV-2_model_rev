public class Initial_Parameters {

    private int Initial_Cases_Parameter_Array[][];
    private int Initial_Immunity_Parameter_Array[][];

    public Initial_Parameters(){

    }

    public Initial_Parameters(int[][] Initial_Cases, int[][] Initial_Immunity){
         this.Initial_Cases_Parameter_Array = Initial_Cases;
         this.Initial_Immunity_Parameter_Array = Initial_Immunity;
    }

    public int[][] getInitial_Cases_Parameter_Array(){
        return Initial_Cases_Parameter_Array;
    }

    public int[][] getInitial_Immunity_Parameter_Array() {
        return Initial_Immunity_Parameter_Array;
    }

    public int[] getCases_by_County(int County_Code){
        return Initial_Cases_Parameter_Array[County_Code];
    }

    public int[] getImmunity_Level_by_County(int County_Code){
        return Initial_Immunity_Parameter_Array[County_Code];
    }

}
