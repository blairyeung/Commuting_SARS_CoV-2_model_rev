public class Patient {

    private int Status;
    private int After_onset;
    private int Property;
    private int Age;
    private int Direct_infection;

    public Patient(int AgeBand){
        Status = 0;
        After_onset = 0;
        Property = -1;
        Direct_infection = 0;
        Age = AgeBand;
    }

    public Patient(int Patient_status, int Days_after_onset, int Patient_property, int Patient_age, int Next_generation_matrix){

        /**
         * Status: the Status (step) of the patient
         * Status = 1:
         * Status = 2:
         */

        this.Status = Patient_status;
        this.After_onset = Days_after_onset;
        this.Property = Patient_property;
        this.Age = Patient_age;
        this.Direct_infection = Next_generation_matrix;
    }

    public void setStatus(int Patient_Status){
        this.Status = Patient_Status;
    }

    public void setAfter_onset(int Days_after_onset){
        this.After_onset = Days_after_onset;
    }

    public void setProperty(int PatientProperty){
        this.Property = PatientProperty;
    }

    public void setAge(int Patient_Age){
        this.Age = Patient_Age;
    }

    public void setDirect_infection(int Next_generation_Matrix){
        this.Direct_infection = Next_generation_Matrix;
    }

    public int getStatus(){
        return Status;
    }

    public int getAfter_onset(){
        return After_onset;
    }

    public int getProperty(){
        return Property;
    }

    public int getAge(){
        return Age;
    }

    public int getDirect_infection(){
        return Direct_infection;
    }

    public void addOnsetDay(){
        this.After_onset+=1;
    }
}
