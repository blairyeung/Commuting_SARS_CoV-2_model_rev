public class Parameters {

    /**
     * Key parameters of the model
     */

    final static public String Version = "Ver 1.5";

    public static double Kilometers_per_degree = 113.321;//float
    static public String Model_path = "Model IO/";
    static public String Read_path = "Model IO/";
    static public String WritePath = "Model Output/";
    static public double Transmissbility_by_variant[] = {0.025,0.025*1.66,0.025*1.66*1.6};
    static public int DataPackSize = 26;

    static public int Total_number_of_variants = 2;

    final static public double MedianDistance_of_Travel_for_Commute = 12.9;

    final static public double[] Travel_distance_distribution = {0.328238124,0.21618932,0.1320374,0.093315445,0.062606791,0.041898814,0.125714106};
    final static public String[] Travel_distance_tiers = {"Less_than_5km","5_to_9.9_km","10_to_14.9_km","15_to_19.9_km","20_to_24.9_km","25_to_29.9_km","Over_30_km"};

    //final static public double[] Travel_distance_distribution_full = {0,0,0.436656505,0.188477378,0.126452419,0.084626704,0.055267357,0.036678205,0.024341506,0.016154251,0.010720776,0.007114847,0.004721773,0.003133606,0.00207962,0.001380139,0.000915931,0.000607857,0.000403404,0.000267721,0,0,0,0};
    //final static public double[] Travel_distance_distribution_full = {0.086127195,0.13747567,0.178312546,0.181314874,0.147416786,0.10354102,0.068670674,0.045225001,0.029912401,0.019836524,0.013163033,0.008735593,0.0057977,0.003847532,0.00255335,0.001694422,0.001123086,7.39E-04,4.71E-04,2.76E-04,1.37E-04,5.49E-05,1.74E-05,4.52E-06};
    final static public double[] Travel_distance_distribution_full = {0,0,0.179743972,0.173122692,0.157183793,0.133361937,0.105706717,0.078874412,0.056115834,0.038621917,0.026053331,0.017394236,0.01156216,0.007673686,0.005089446,0.003371631,0.002227281,0.001462523,0.000950523,0.000609217,0.000385025,0.000241301,0.000151603,9.67636E-05};
    final static public String[] Travel_distance_tiers_full = {"Less_than_5km","5_to_9.9_km","10_to_14.9_km","15_to_19.9_km","20_to_24.9_km","25_to_29.9_km","30_to_34.9_km",
            "35_to_39.9_km","40_to_44.9_km","45_to_49.9_km","50_to_54.9_km","55_to_59.9_km","60_to_64.9_km","65_to_69.9_km","70_to_74.9_km","75_to_79.9_km","80_to_84.9_km",
            "85_to_89.9_km","90_to_94.9_km","95_to_99.9_km","100_to_104.9_km","105_to_109.9_km","110_to_114.9_km","115_to_119.9_km"};

    static public String ColumnName[] = {"Day","Population","Infected","Exposed","Active_cases","Critical_cases","Resolved","Deaths","Vaccinated",
            "Clinical_Cases","Subclinical_cases","CFR","Immune","Tier","Constant", "Imported_cases","Exported_cases",
            "Daily_Infected","Daily_Exposed","Daily_cases","Daily_critical","Daily_resolved","Daily_deaths","Daily_vaccinated","Daily_vaccinated_second_dose"};

    static public double[] CFR = {0.00004,0.00003,0.00002,0.00016,0.0002,0.00024,0.0003,0.0004,0.0006,0.0009,0.0014,0.0042,0.008,0.028,0.04,0.18};
    static public double[] CriticalRate = {0.00004,0.00003,0.00002,0.00016,0.0002,0.00024,0.0003,0.0004,0.0006,0.0009,0.0014,0.0042,0.008,0.028,0.04,0.18};/**Research crtical rate, substitude*/
    static public double SubClinical_Ratio_By_Age[] = {0.750,0.712,0.683,0.650,0.616,0.583,0.550,0.517,0.483,0.450,0.417,0.383,0.350,0.317,0.283,0.250};
    static public double Population_By_Age[] = {0.0491,0.0518,0.0538,0.0579,0.0706,0.0731,0.0707,0.0674,0.0625,0.0633,0.0657,0.0729,0.0652,0.0546,0.0455,0.0759};
    static public double Susceptibility[] = {0.375,0.3875,0.6,0.7,0.8,0.825,0.85,0.845,0.84,0.85,0.85,0.84,0.3,0.81,0.72,0.9};
    static public String[] AgeBand = {"0 to 4","5 to 9","10 to 14","15 to 19","20 to 24","25 to 29","30 to 34","35 to 39","40 to 44","45 to 49","50 to 54","55 to 59","60 to 64","65 to 69","70 to 74","75+"};
    static public double[] Ontario_population_by_age = {723016, 762654, 792947, 852405, 1039661, 1077433, 1041952, 992844, 921378, 932058, 968546, 1073519, 961243, 803962, 673546, 1116850};


    static public double Immunity_level_half_life = 224;

    static public double Efficacy_Against_Death = 0.95;
    static public double Efficacy_Against_Infection = 0.3;

    static public int Observation_Range = 200;

    //static public double Workforce_Age_Dist[] = {0.010693183,0.032079549, 0.083009492,0.106146399,0.106351741,0.105920522,0.103210008,0.098220198,0.10624907,0.098138062,0.065560558,0.042213176,0.025934691,0.016273351,0};

    static public double Workforce_Age_Dist[] = {0,0,0.010693183,0.032079549, 0.083009492,0.106146399,0.106351741,0.105920522,0.103210008,0.098220198,0.10624907,0.098138062,0.065560558,0.042213176,0.025934691,0.016273351,0};
    static public double Workforce_Employrate_Age_Dist[] = {0,0,0.010693183,0.032079549, 0.083009492,0.106146399,0.106351741,0.105920522,0.103210008,0.098220198,0.10624907,0.098138062,0.065560558,0.042213176,0.025934691,0.016273351,0};
    /**
     * Change it above
     */

    final static public double Work_force_ratio = 0.74;/**This value has to be changed after*/
    final static public double Commuter_ratio = 0.328002722;

    final static public double Urban_commuter_ratio = 0.3949 * 0.455572555943435;
    //final static public double Urban_commuter_ratio_Near_Toronto = 0.47388 * 0.455572555943435;
    final static public double Rural_commuter_ratio = 0.3207 * 0.455572555943435;
    //final static public double Rural_commuter_ratio_Near_Toronto = 0.38484;

    final static public double[] Commuter_ratio_by_type = {Urban_commuter_ratio,Rural_commuter_ratio};

    final static public double Larger_CMA_commuting_ratio = 0.36; /**Larger CMA, population > 500,000*/
    final static public double Smaller_CMA_commuting_ratio = 0.32; /**Larger CMA, population > 100,000*/
    final static public double CA_commuting_ratio = 0.3; /**Larger CA, population > 10,000*/
    final static public double Strong_MIZ_ratio = 0.48; /**Rural, Urban population near it over 1,000,000*/
    final static public double Moderate_MIZ_ratio = 0.35; /**Rural, Urban population near it over 500,000*/
    final static public double Weak_MIZ_ratio = 0.25; /**Rural, Urban population near it over 100,000*/
    final static public double None_MIZ_ratio = 0.18; /**Rural, Urban population near it less than 100,000*/

    final static public double Commuter_ratio_by_category[] = {Larger_CMA_commuting_ratio,Smaller_CMA_commuting_ratio,CA_commuting_ratio,Strong_MIZ_ratio,Moderate_MIZ_ratio,Weak_MIZ_ratio,None_MIZ_ratio};

    final static public double MIZ_Top10[] = {4.0,1.9,1.8,1.7,1.6,1.5,1.4,1.3,1.2,1.1};

    final static public double[] Larger_CMA_commuting_category = {0.02,0.98}; /**Larger CMA, population > 500,000*/
    final static public double[] Smaller_CMA_commuting_category = {0.05,0.95}; /**Larger CMA, population > 100,000*/
    final static public double[] CA_commuting_category = {0.22,0.78}; /**Larger CA, population > 10,000*/
    final static public double[] Strong_MIZ_category = {0.2,0.8}; /**Rural, Urban population near it over 1,000,000*/
    final static public double[] Moderate_MIZ_category = {0.58,0.42}; /**Rural, Urban population near it over 500,000*/
    final static public double[] Weak_MIZ_category = {0.92,0.08}; /**Rural, Urban population near it over 100,000*/
    final static public double[] None_MIZ_category = {0.9,0.1}; /**Rural, Urban population near it less than 10,000*/

    final static public double Commuter_ratio_by_destination[][] = {Larger_CMA_commuting_category,Smaller_CMA_commuting_category,CA_commuting_category,Strong_MIZ_category,Moderate_MIZ_category,Weak_MIZ_category,None_MIZ_category};

    final static public double Urban_to_Urban_Ratio = 0.95826972;
    final static public double Urban_to_Rural_Ratio = 0.04173028;
    final static public double Rural_to_Urban_Ratio = 0.498316498;
    final static public double Rural_to_Rural_Ratio = 0.501683502;

    /**
     * Time Parameters
     */

    static public double dE = 3.5;
    static public double dI = 5;
    static public double dD = 22;

    /** Work, School, Home, Other*/

    static public double[] SchoolClosure = {0.5,0,1.15,0.5};
    static public double[] LockdownScenario = {0.3,0.2,1.15,0.5};
    static public double[] Control = {0.4,0.3,1.12,0.6};
    static public double[] Restrict = {0.625,0.4,1.1,0.65};
    static public double[] Protect = {0.65,0.5,1.06,0.7};
    static public double[] Prevent = {0.7,0.5,1.03,0.75};
    //static public double[] Prevent = {1,1,1,1};
    static public double Scenarios[][] = {Prevent,Protect,Restrict,Control,LockdownScenario};
    static public double[] LockdownResPro = {0.9,0.85,0.8,0.75,0.7};
    static public double[] LockDownTiers = {0,0.0001,0.0004,0.001,0.005};
    static public String[] LockdownLevels = {"Prevent","Protect","Restrict","Control","Lockdown"};

    static public double[] Day_Time = {0.9, 1.0, 0.2, 0.8};
    static public double[] Night_Time = {1.0-Day_Time[0], 1.0-Day_Time[1], 1.0-Day_Time[2], 1.0-Day_Time[3]};
    static public double TimeMatrix[][] = {Day_Time,Night_Time};
    final static public int Steps = 1;

    static public double[] WorkPlaceReductionbyLevel = {0.7,0.65,0.625,0.4,0.3};

    //static public double[] Infectiousness_By_Variant = {0.025,0.0403};
    //static public double[] Infectiousness_By_Variant = {0.075,0.0403};
    //static public double[] Infectiousness_By_Variant = {0.25,0.23};

    static public double[] Infectiousness_By_Variant = {0.0275,0.0275*1.66,0.0275*1.66*1.5,0.0275*1.66*1.5*1.5};
    static public double[] CFR_By_Variant = {1,1.66,0.9};
    static public String[] Name_of_Variant = {"Wild_type","Alpha","Delta","Omicron"};

    /**
     * Adjustable bias
     */

    /** Work, School, Home, Other*/



    public final static boolean StartVaccination = false;

}
