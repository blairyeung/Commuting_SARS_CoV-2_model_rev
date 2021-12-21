public class Trail {
    private int Model_Iteratons;
    private int Trail_Code;
    private int GlobalParameter;
    public Trail(int Iteration, int Code){
        this.Model_Iteratons = Iteration;
        this.Trail_Code = Code;
    }
    public Trail(int Iteration, int Code, int Global){
        this.Model_Iteratons = Iteration;
        this.Trail_Code = Code;
        this.GlobalParameter = Global;
    }

    public int getGlobalParameter() {
        return GlobalParameter;
    }

    public int getModel_Iteratons() {
        return Model_Iteratons;
    }

    public int getTrail_Code() {
        return Trail_Code;
    }
}
