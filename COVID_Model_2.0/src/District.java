public class District {
    private int District_Code;  /**Index*/
    private int County_Code[];  /**Indices of all counties within this district*/
    private int District_Vaccines;
    private int County_Vaccines[];

    public District(){

    }

    public District(int Code){
        this.District_Code = Code;
    }

    public District(int Code, int Codes[]){
        this.District_Code = Code;
        this.County_Code = Codes;
    }
}
