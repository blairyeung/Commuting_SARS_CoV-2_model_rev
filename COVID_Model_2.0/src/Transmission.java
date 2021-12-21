import java.util.ArrayList;
import java.util.Random;

public class Transmission{

    private boolean unlock = false;
    private int unlocked = 0;

    public static Data RunModel(CountyDataArray SeriesData,Data CountyData, int Imported, int Exported, int ResponseTier, int County){
        double[][][] StratifiedPack = CountyData.getDataPackByAge();

        ArrayList<Patient>[][] PatientArray = CountyData.getPatientArray();

        int Population = (int) CountyData.getDataPack()[1];
        int Infected = (int) CountyData.getDataPack()[2];
        int Exposed = (int) CountyData.getDataPack()[3];
        int ActiveCases = (int) CountyData.getDataPack()[4];
        int DailyIncidence = (int) CountyData.getDataPack()[5];
        int Resolved = (int) CountyData.getDataPack()[6];
        int CriticalCases = (int) CountyData.getDataPack()[7];
        int Deaths = (int) CountyData.getDataPack()[8];
        int DailyResolved = (int) CountyData.getDataPack()[9];
        int DailyCritical = (int) CountyData.getDataPack()[10];
        int DailyDeaths = (int) CountyData.getDataPack()[11];
        int Vaccinated = (int) CountyData.getDataPack()[12];
        int ClinicalCases = (int) CountyData.getDataPack()[13];
        int SubClinicalCases = (int) CountyData.getDataPack()[14];
        int IncidenceRate = (int) CountyData.getDataPack()[15];
        double CFR = CountyData.getDataPack()[16];
        int Immune = (int) CountyData.getDataPack()[17];
        int Tier = (int) CountyData.getDataPack()[18];
        int FlightConstant = (int) CountyData.getDataPack()[19];
        int ImportedCases = (int) CountyData.getDataPack()[20];
        int ExportedCases = (int) CountyData.getDataPack()[21];

        DailyIncidence = 0;
        DailyResolved = 0;
        DailyDeaths = 0;
        DailyCritical = 0;
        ClinicalCases = 0;
        SubClinicalCases = 0;


        /**
         * Calculate the OverallSubclinicalRate
         */

        double OverallSubclinicalRatio = 0;

        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
            double Clinicalcases_within_an_ageband = StratifiedPack[0][3][Age_Band] + StratifiedPack[1][3][Age_Band];
            OverallSubclinicalRatio += (Clinicalcases_within_an_ageband/(double) Exposed) * (1-Parameters.SubClinical_Ratio_By_Age[Age_Band]);
        }

        System.out.println("OverallSubclinicalRatio: " + OverallSubclinicalRatio);

        int LockLevel = 0;

        double Ratio = 0;
        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
            Ratio += StratifiedPack[1][4][Age_Band];
        }

        System.out.println("Variant_of_concern: " + Ratio/ActiveCases);

        System.out.println(ActiveCases + "cases");
        System.out.println(Ratio + "NVOC cases");

        if(ActiveCases==0){
            Ratio = 0;
        }

        else {
            Ratio /= ActiveCases;
        }

        /**
         * Variant of concern seperate it from the file
         */


        for (int Variant = 0; Variant < 2; Variant++) {
            for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                if(Infected==1&&PatientArray[Variant][Age_Band].size()<=1){
                    PatientArray[Variant][Age_Band].add(new Patient(Age_Band));
                }
                StratifiedPack[Variant][0][Age_Band] = Main.Day;
            }
        }


        double[] ImmunityMatrix = new double[16];
        for (int Variant = 0; Variant < 2; Variant++) {
            for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                int Stratified_Daily_Exposed = 0;
                int Stratified_Daily_Active = 0;
                int Stratified_Daily_Resolved = 0;
                int Stratified_Daily_Critcical = 0;
                int Stratified_Daily_Deaths = 0;
                int Stratified_Daily_Infection = 0;
                int Stratified_Removed_Critical = 0;

                for (int Patient_Code = 0; Patient_Code < StratifiedPack[Variant][2][Age_Band]; Patient_Code++){
                    Patient COVID_19_Patient = PatientArray[Variant][Age_Band].get(Patient_Code);

                    /**Non critical, and critical; By default, non-critical*/
                    COVID_19_Patient.addOnsetDay();  //days after being infected
                    int AfterOnset = COVID_19_Patient.getAfter_onset();
                    int Property = COVID_19_Patient.getProperty();

                    switch (AfterOnset){
                        case 1:
                            COVID_19_Patient.setStatus(0);
                        case 3:
                            if(Property==-1){
                                Stratified_Daily_Exposed++;
                                COVID_19_Patient.setProperty(0);
                                continue;
                            }
                        case 5:
                            if(Property==0){
                                Stratified_Daily_Active++;
                                COVID_19_Patient.setProperty(1);
                                continue;
                            }
                        case 7:
                            if(Property==1){
                                if (Math.random() <  (1 - Parameters.CriticalRate[Age_Band]) * (1 - Parameters.SubClinical_Ratio_By_Age[Age_Band])) {
                                    Stratified_Daily_Critcical++;
                                    COVID_19_Patient.setStatus(1);
                                    continue;
                                }
                            }
                        case 26:
                            if (Property == 1) {
                                if (Math.random() <  (1 - Parameters.SubClinical_Ratio_By_Age[Age_Band]) * Parameters.CFR[Age_Band]) {
                                    if(Math.random()<Parameters.Efficacy_Against_Death*(1-(StratifiedPack[Variant][11][Age_Band]/StratifiedPack[Variant][1][Age_Band]))){
                                        Stratified_Daily_Deaths++;
                                        COVID_19_Patient.setProperty(4);
                                        if(COVID_19_Patient.getStatus()==1){
                                            Stratified_Removed_Critical++;
                                        }
                                        continue;
                                    }
                                }
                                else {
                                    Stratified_Daily_Resolved++;
                                    COVID_19_Patient.setProperty(3);
                                    if(COVID_19_Patient.getStatus()==1){
                                        Stratified_Removed_Critical++;
                                    }
                                    continue;
                                }
                            }
                        default: continue;
                    }
                }
                ImmunityMatrix = new double[16];

                for (int Age_Band_of_Infection = 0; Age_Band_of_Infection < 16; Age_Band_of_Infection++) {
                    double ImmunizedRatio = StratifiedPack[0][2][Age_Band_of_Infection] + Parameters.Efficacy_Against_Infection*StratifiedPack[0][12][Age_Band_of_Infection];
                    ImmunizedRatio += StratifiedPack[1][2][Age_Band_of_Infection] + Parameters.Efficacy_Against_Infection*StratifiedPack[1][12][Age_Band_of_Infection];
                    ImmunityMatrix[Age_Band_of_Infection] = 1.0 - ((ImmunizedRatio)/StratifiedPack[0][1][Age_Band_of_Infection]);
                    if(ImmunityMatrix[Age_Band_of_Infection]==0.0||Infected<10){
                        ImmunityMatrix[Age_Band_of_Infection] = 1.0;
                    }
                }

                double[][] Work = (double[][]) IO.MatricesByCategory[0][0].get(IO.Countrycode.indexOf("CA"));
                double[][] School = (double[][]) IO.MatricesByCategory[0][1].get(IO.Countrycode.indexOf("CA"));
                double[][] Home = (double[][]) IO.MatricesByCategory[0][2].get(IO.Countrycode.indexOf("CA"));
                double[][] Other = (double[][]) IO.MatricesByCategory[0][3].get(IO.Countrycode.indexOf("CA"));

                double[][][] Mat = {Work,School,Home,Other};

                int Effective_Cases = (int) Math.round(0.4 + 0.5 * StratifiedPack[Variant][3][Age_Band] + 0.5 * SubClinicalCases + ClinicalCases);

                Effective_Cases = 30;

                //System.out.println("Band: " + Parameters.AgeBand[Age_Band] + "Effective_Number" + Effective_Cases);

                double[] Matrix = AgeDistribution.GetPatientArray(Age_Band,Effective_Cases,ImmunityMatrix, 0,Variant ,(int)StratifiedPack[Variant][2][Age_Band],true,Mat,LockLevel);
                //double Matrix[] = new double[16];

                for (int SubBand = 0; SubBand < 16; SubBand++) {
                    int ThisAgeBand = (int) Math.round(Matrix[SubBand]);
                    for (int i2 = 0; i2 < ThisAgeBand; i2++) {
                        PatientArray[Variant][Age_Band].add(new Patient(SubBand));
                        Stratified_Daily_Infection++;
                    }
                }

                Exposed += Stratified_Daily_Exposed;
                StratifiedPack[Variant][3][Age_Band] += Stratified_Daily_Exposed;

                ActiveCases += Stratified_Daily_Active;
                DailyIncidence += Stratified_Daily_Active;
                StratifiedPack[Variant][4][Age_Band] += Stratified_Daily_Active;

                CriticalCases += Stratified_Daily_Critcical;
                DailyCritical += Stratified_Daily_Critcical;

                Deaths += Stratified_Daily_Deaths;
                DailyDeaths += Stratified_Daily_Deaths;
                StratifiedPack[Variant][6][Age_Band] += Stratified_Daily_Deaths;

                Resolved += Stratified_Daily_Resolved;
                DailyResolved += Stratified_Daily_Resolved;
                StratifiedPack[Variant][6][Age_Band] += Stratified_Daily_Resolved;

                Exposed -= Stratified_Daily_Active;
                StratifiedPack[Variant][3][Age_Band]  -= Stratified_Daily_Active;

                ActiveCases -= Stratified_Daily_Resolved;
                ActiveCases -= Stratified_Daily_Deaths;

                StratifiedPack[Variant][4][Age_Band] -= Stratified_Daily_Resolved;
                StratifiedPack[Variant][4][Age_Band] -= Stratified_Daily_Deaths;

                Infected += Stratified_Daily_Infection;
                StratifiedPack[Variant][2][Age_Band] += Stratified_Daily_Infection;

                CriticalCases -= Stratified_Removed_Critical;


            }
        }


        /**
         * Vaccination
         */

        /*if(Parameters.StartVaccination){
            double Vaccination[] = VaccineDistribution.GetVaccineArr(Parameters.VaccinationPioritization,StratifiedPack);//Vaccinated Per Day
            for (int i = 0; i < 16; i++) {
                VaccineDoses.DailyVaccineDoses(unlocked);
                double NewlyVaccinated = Parameters.VaccineSupply * Vaccination[i];
                double NCovRatio = StratifiedPack[2][i][0]/(StratifiedPack[2][i][0] + StratifiedPack[11][i][1]);
                StratifiedPack[11][i][0] += Parameters.VaccineSupply * Vaccination[i] * NCovRatio;
                StratifiedPack[11][i][1] += Parameters.VaccineSupply * Vaccination[i] * (1-NCovRatio);
            }
        }*/


        for (int Variant = 0; Variant < 2; Variant++) {
            if(Variant==0){
                System.out.println("Non-nVoC");
            }
            else {
                System.out.println("nVoC");
            }

            for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                StratifiedPack[Variant][13][Age_Band] = StratifiedPack[Variant][4][Age_Band] * (1-Parameters.SubClinical_Ratio_By_Age[Age_Band]);
                StratifiedPack[Variant][14][Age_Band] = StratifiedPack[Variant][4][Age_Band] - StratifiedPack[Variant][13][Age_Band];
                ClinicalCases += StratifiedPack[Variant][13][Age_Band];
                SubClinicalCases += StratifiedPack[Variant][14][Age_Band];
                System.out.print(Parameters.AgeBand[Age_Band]+" ,"+ImmunityMatrix[Age_Band]);
                double Pop = StratifiedPack[Variant][1][Age_Band];
                double Infected_ByVariant = StratifiedPack[Variant][2][Age_Band];
                double Total_Infected = StratifiedPack[Variant][2][Age_Band] + StratifiedPack[Variant][2][Age_Band];
                double Total_Vaccinated = StratifiedPack[0][12][Age_Band] + StratifiedPack[1][12][Age_Band];
                double Total_Deaths = StratifiedPack[0][8][Age_Band] + StratifiedPack[1][8][Age_Band];
                System.out.print("  Pop:"+Pop);
                System.out.print("  Infected:"+Infected_ByVariant);
                System.out.print("  Vaccinated:"+Total_Vaccinated);
                System.out.print("  Deaths Fraction:" + 100.0*(Total_Deaths/Deaths)+"%");
                System.out.println("  Imm:"+(100*((Total_Infected + Parameters.Efficacy_Against_Infection*Total_Vaccinated)/Pop))+"%");
            }

        }

        /**System.out.println("Clinical Cases: "+ ClinicalCases);
        System.out.print("County: " + County +"    Day: " + Main.Day +"    Population: " + Population +"    Infected: " + Infected + "    cases: " + ActiveCases +"    Exposed:"+ Exposed +"    Recovered:" +Resolved +"    Death:" + Deaths);
        System.out.println("    CFR: " + 100*((double)Deaths/ClinicalCases) +"%");
        System.out.println("    Weekly Incidnece:" + 100*IncidenceRate +"%");*/

       CountyData.setValueDataPack(0,Main.Day);
       CountyData.setValueDataPack(2,Infected);
       CountyData.setValueDataPack(3,Exposed);
       CountyData.setValueDataPack(4,ActiveCases);
       CountyData.setValueDataPack(5,DailyIncidence);
       CountyData.setValueDataPack(6,Resolved);
       CountyData.setValueDataPack(7,CriticalCases);
       CountyData.setValueDataPack(8,Deaths);
       CountyData.setValueDataPack(9,DailyResolved);
       CountyData.setValueDataPack(10,DailyCritical);
       CountyData.setValueDataPack(11,DailyDeaths);
       CountyData.setValueDataPack(12,Vaccinated);
       CountyData.setValueDataPack(13,ClinicalCases);
       CountyData.setValueDataPack(14,SubClinicalCases);
       CountyData.setValueDataPack(15,IncidenceRate);
       CountyData.setValueDataPack(16,CFR);
       CountyData.setValueDataPack(17,Immune);
       CountyData.setValueDataPack(18,Tier);
       CountyData.setValueDataPack(19,FlightConstant);
       CountyData.setValueDataPack(20,ImportedCases);
       CountyData.setValueDataPack(21,ExportedCases);
       CountyData.setPatientArray(PatientArray);
       CountyData.setDataPackByAge(StratifiedPack);

        return CountyData;
    }

    public static int Contact(double lockdown, String name, int Age, double[] AgeMatrix) {
        double[][] Matrix = IO.Matrices.get(IO.Countrycode.indexOf("CA"))[0];
        double[] bias = IO.Biases.get(IO.Countrycode.indexOf("CA"));
        double poss;
        poss = ProbabilityOutput.GetRandom(GammaDist.getGammaFunction((int) bias[Age]));
        int contact = (int) (lockdown * poss);
        for (int i = 0; i < AgeMatrix.length; i++) {
            double s = Matrix[i][Age];
            AgeMatrix[i] = s;
        }
        return contact;
    }

}
