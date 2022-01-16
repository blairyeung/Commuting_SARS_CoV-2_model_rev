import java.util.ArrayList;

public class Data {

    final static int date_index = 0;
    final static int population_index = 1;
    final static int incidence_index = 2;
    final static int exposed_index = 3;
    final static int active_index = 4;
    final static int critical_index = 5;
    final static int resolved_index = 6;
    final static int deaths_index = 7;
    final static int tot_vaccination_index = 8;
    final static int clinical_index = 9;
    final static int sub_clinical_index = 10;
    final static int CFR_index = 11;
    final static int immunity_index = 12;
    final static int tier_index = 13;
    final static int commute_index = 14;
    final static int imported_index = 15;
    final static int exported_index = 16;
    final static int daily_incidence_index = 17;
    final static int daily_exposed_index = 18;
    final static int daily_cases_index = 19;
    final static int daily_critical_index = 20;
    final static int daily_resolved_index = 21;
    final static int daily_deaths_index = 22;
    final static int daily_one_dose_index = 23;
    final static int daily_two_dose_index = 24;
    final static int daily_three_dose_index = 25;

    private double epidemiological_data_total[];
    private double epidemiological_data_by_variant[][];
    private double epidemiological_data_by_age[][][] ;

    private double InitialInfected = 50;

    //Store the epidemiological information of each county of each day

    public Data(){

        epidemiological_data_total = new double[Parameters.DataPackSize];
        epidemiological_data_by_variant = new double[Parameters.Total_number_of_variants][Parameters.DataPackSize];
        epidemiological_data_by_age = new double[Parameters.Total_number_of_variants][Parameters.AgeBand.length][Parameters.DataPackSize];
    }

    public void reCalculate(){
        for (int index = 0; index < Parameters.DataPackSize; index++) {
            double subtotal = 0;
            for (int variant = 0; variant < Parameters.Total_number_of_variants; variant++) {
                for (int Age_band = 0; Age_band < Parameters.AgeBand.length; Age_band++) {
                    subtotal += epidemiological_data_by_age[variant][Age_band][index];
                }
            }
            epidemiological_data_total[index] = subtotal;
        }
    }

    public void setEpidemiological_data_total(double[] epidemiological_data_total, double[][][] epidemiological_data_by_age){
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
        epidemiological_data_by_age[Variant_index][AgeBand_index][Index] = Value;
    }

    public void addValueDataPackByAge(int Variant_index,int AgeBand_index, int Index, double Delta_Value){
        epidemiological_data_by_age[Variant_index][AgeBand_index][Index] += Delta_Value;
    }

    public double getPopulation(){
        return epidemiological_data_total[population_index];
    }

    public double getIncidence(){
        return epidemiological_data_total[incidence_index];
    }

    public double getExposed(){
        return epidemiological_data_total[exposed_index];
    }

    public double getActive(){
        return epidemiological_data_total[active_index];
    }

    public double getCritical(){
        return epidemiological_data_total[critical_index];
    }

    public double getResolved(){return epidemiological_data_total[resolved_index];}

    public double getDeaths(){return epidemiological_data_total[deaths_index];}

    public double getVaccinated(){return epidemiological_data_total[tot_vaccination_index];}

    public double getClinical(){return epidemiological_data_total[clinical_index];}

    public double getSub_clinical(){return epidemiological_data_total[sub_clinical_index];}

    public double getCFR(){return epidemiological_data_total[CFR_index];}

    public double getImmunity(){return epidemiological_data_total[immunity_index];}

    public double getTier(){return epidemiological_data_total[tier_index];}

    public double getCommute_coeff(){return epidemiological_data_total[commute_index];}

    public double getImported(){return epidemiological_data_total[imported_index];}

    public double getExported(){return epidemiological_data_total[exported_index];}

    public double getDaily_incidence(){return epidemiological_data_total[daily_incidence_index];}

    public double getDaily_exposed(){return epidemiological_data_total[daily_exposed_index];}

    public double getDaily_cases(){return epidemiological_data_total[daily_cases_index];}

    public double getDaily_critical(){return epidemiological_data_total[daily_critical_index];}

    public double getDaily_resolved(){return epidemiological_data_total[daily_resolved_index];}

    public double getDaily_deaths(){return epidemiological_data_total[daily_resolved_index];}

    public double getDaily_one_dose(){return epidemiological_data_total[daily_one_dose_index];}

    public double getDaily_two_dose(){return epidemiological_data_total[daily_two_dose_index];}

    public double getDaily_three_dose(){return epidemiological_data_total[daily_three_dose_index];}

    public double getPopulation(int variant, int age){ return epidemiological_data_by_age[variant][age][population_index];}

    public double getIncidence(int variant, int age){ return epidemiological_data_by_age[variant][age][incidence_index];}

    public double getExposed(int variant, int age){ return epidemiological_data_by_age[variant][age][exposed_index];}

    public double getActive(int variant, int age){ return epidemiological_data_by_age[variant][age][active_index];}

    public double getCritical(int variant, int age){ return epidemiological_data_by_age[variant][age][critical_index];}

    public double getResolved(int variant, int age){return epidemiological_data_by_age[variant][age][resolved_index];}

    public double getDeaths(int variant, int age){return epidemiological_data_by_age[variant][age][deaths_index];}

    public double getVaccinated(int variant, int age){return epidemiological_data_by_age[variant][age][tot_vaccination_index];}

    public double getClinical(int variant, int age){return epidemiological_data_by_age[variant][age][clinical_index];}

    public double getSub_clinical(int variant, int age){return epidemiological_data_by_age[variant][age][sub_clinical_index];}

    public double getCFR(int variant, int age){return epidemiological_data_by_age[variant][age][CFR_index];}

    public double getImmunity(int variant, int age){return epidemiological_data_by_age[variant][age][immunity_index];}

    public double getTier(int variant, int age){return epidemiological_data_by_age[variant][age][tier_index];}

    public double getCommute_coeff(int variant, int age){return epidemiological_data_by_age[variant][age][commute_index];}

    public double getImported(int variant, int age){return epidemiological_data_by_age[variant][age][imported_index];}

    public double getExported(int variant, int age){return epidemiological_data_by_age[variant][age][exported_index];}

    public double getDaily_incidence(int variant, int age){return epidemiological_data_by_age[variant][age][daily_incidence_index];}

    public double getDaily_exposed(int variant, int age){return epidemiological_data_by_age[variant][age][daily_exposed_index];}

    public double getDaily_cases(int variant, int age){return epidemiological_data_by_age[variant][age][daily_cases_index];}

    public double getDaily_critical(int variant, int age){return epidemiological_data_by_age[variant][age][daily_critical_index];}

    public double getDaily_resolved(int variant, int age){return epidemiological_data_by_age[variant][age][daily_resolved_index];}

    public double getDaily_deaths(int variant, int age){return epidemiological_data_by_age[variant][age][daily_resolved_index];}

    public double getDaily_one_dose(int variant, int age){return epidemiological_data_by_age[variant][age][daily_one_dose_index];}

    public double getDaily_two_dose(int variant, int age){return epidemiological_data_by_age[variant][age][daily_two_dose_index];}

    public double getDaily_three_dose(int variant, int age){return epidemiological_data_by_age[variant][age][daily_three_dose_index];}


    public void setPopulation(int population){
        epidemiological_data_total[population_index] = population;
    }

    public void setIncidence(int incidence){
        epidemiological_data_total[incidence_index] = incidence;
    }

    public void setExposed(int exposed){
        epidemiological_data_total[exposed_index] = exposed;
    }

    public void setActive(int active){
        epidemiological_data_total[active_index] = active;
    }

    public void setCritical(int critical){
        epidemiological_data_total[critical_index] = critical;
    }

    public void setResolved(int resolved){epidemiological_data_total[resolved_index] = resolved;}

    public void setDeaths(int deaths){epidemiological_data_total[deaths_index] = deaths;}

    public void setVaccinated(int vaccinated){epidemiological_data_total[tot_vaccination_index] = vaccinated;}

    public void setClinical(int clinical){epidemiological_data_total[clinical_index] = clinical;}

    public void setSub_clinical(int sub_clinical){epidemiological_data_total[sub_clinical_index] = sub_clinical;}

    public void setCFR(double CFR){epidemiological_data_total[CFR_index] = CFR;}

    public void setImmunity(double immunity){epidemiological_data_total[immunity_index] = immunity;}

    public void setTier(int tier){epidemiological_data_total[tier_index] = tier;}

    public void setCommute_coeff(double commute_coeff){epidemiological_data_total[commute_index] = commute_coeff;}

    public void setImported(int imported){epidemiological_data_total[imported_index] = imported;}

    public void setExported(int exported){epidemiological_data_total[exported_index] = exported;}

    public void setDaily_incidence(int daily_incidence){epidemiological_data_total[daily_incidence_index] = daily_incidence;}

    public void setDaily_exposed(int daily_exposed){epidemiological_data_total[daily_exposed_index] = daily_exposed;}

    public void setDaily_cases(int daily_cases){epidemiological_data_total[daily_cases_index] = daily_cases;}

    public void setDaily_critical(int daily_critical){epidemiological_data_total[daily_critical_index] = daily_critical;}

    public void setDaily_resolved(int daily_resolved){epidemiological_data_total[daily_resolved_index] = daily_resolved;}

    public void setDaily_deaths(int daily_deaths){epidemiological_data_total[daily_deaths_index] = daily_deaths;}

    public void setDaily_one_dose(int daily_one_dose){epidemiological_data_total[daily_one_dose_index] = daily_one_dose;}

    public void setDaily_two_dose(int daily_two_dose){epidemiological_data_total[daily_two_dose_index] = daily_two_dose;}

    public void setDaily_three_dose(int daily_three_dose){epidemiological_data_total[daily_three_dose_index] = daily_three_dose;}
}
