import java.io.*;
import java.nio.charset.StandardCharsets;

public class FilePrint {
    public static void main(String[] args) {

    }

    public static void Print_all(CountyDataArray DataSeries, Trail trail,int County_Code){

        Print_to_a_single_file(DataSeries, trail, County_Code);
        Print_by_Age(DataSeries, trail, County_Code);
        Print_by_Age_Full(DataSeries, trail, County_Code);
        //Print_by_Variant(DataSeries, trail, County_Code);
        /*File County_file_by_age = new File(File_by_age_path);
        File County_file_by_category = new File(File_by_category_path);*/
    }

    public static void Print_to_a_single_file(CountyDataArray DataSeries, Trail trail, int County_Code){
        String Common_Path = Parameters.WritePath;
        Common_Path += "Iteration " + trail.getModel_Iteratons() + "\\" + "Trail " + trail.getTrail_Code() + "\\";
        String Full_file_path = Common_Path + CountyDataIO.Counties[County_Code].getName() +".csv";
        String Folder_name = Common_Path + CountyDataIO.Counties[County_Code].getName() + "\\";
        Directory_Creator.create_dir(Common_Path);
        //System.out.println(Full_file_path);
        File County_file = new File(Full_file_path);

        BufferedWriter Print_to_file = null;
        try {
            Print_to_file = new BufferedWriter(new FileWriter(County_file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int Index = 0; Index < Parameters.DataPackSize; Index++) {

            try {
                Print_to_file.write((Parameters.ColumnName[Index] + ","));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Print_to_file.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int Date = 0; Date < DataSeries.getLength(); Date++) {

            String A_line = "";
            for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                A_line += DataSeries.getTimeSeries()[Date].getDataPack()[Index] + ",";
            }
            A_line = A_line.substring(0,A_line.length()-1);
            try {
                Print_to_file.write(A_line);
                Print_to_file.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Print_to_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Print_by_Age(CountyDataArray DataSeries, Trail trail,int County_Code){
        String Common_Path = Parameters.WritePath;
        Common_Path += "Iteration " + trail.getModel_Iteratons() + "\\" + "Trail " + trail.getTrail_Code() + "\\Stratified\\By_County\\";
        Common_Path += CountyDataIO.Counties[County_Code].getName() + "\\By_Age\\";

        Directory_Creator.create_dir(Common_Path);

        String Full_file_path = Common_Path + CountyDataIO.Counties[County_Code].getName() +".csv";
        File County_file_by_age[] = new File[16];
        //PrintWriter Print_by_Age[] = new PrintWriter[16];
        BufferedWriter Print_by_Age[] = new BufferedWriter[16];



        /**
         * use BufferedoutputStream
         */
        //BufferedWriter Print = new BufferedWriter(County_file_by_age[1]);
        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {

            County_file_by_age[Age_Band] = new File(Common_Path + Parameters.AgeBand[Age_Band] + ".csv");

            try {
                Print_by_Age[Age_Band] = new BufferedWriter(new FileWriter(County_file_by_age[Age_Band]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                try {
                    Print_by_Age[Age_Band].write(Parameters.ColumnName[Index] + ",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Print_by_Age[Age_Band].newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int Date = 0; Date < DataSeries.getLength(); Date++) {
                //System.out.println(Date);
                String A_line = "";
                for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                    A_line += (DataSeries.getTimeSeries()[Date].getDataPackByAge()[0][Index][Age_Band] + DataSeries.getTimeSeries()[Date].getDataPackByAge()[1][Index][Age_Band]) + ",";
                }
                A_line = A_line.substring(0,A_line.length()-1);
                try {
                    Print_by_Age[Age_Band].write(A_line);
                    Print_by_Age[Age_Band].newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Print_by_Age[Age_Band].close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void Print_by_Age_Full(CountyDataArray DataSeries, Trail trail,int County_Code){
        String Common_Path = Parameters.WritePath;
        Common_Path += "Iteration " + trail.getModel_Iteratons() + "\\" + "Trail " + trail.getTrail_Code() + "\\";
        String Full_file_path = Common_Path + CountyDataIO.Counties[County_Code].getName() +"_Full.csv";
        String Folder_name = Common_Path + CountyDataIO.Counties[County_Code].getName() + "\\";
        Directory_Creator.create_dir(Common_Path);
        //System.out.println(Full_file_path);
        File County_file = new File(Full_file_path);

        BufferedWriter Print_to_file = null;
        try {
            Print_to_file = new BufferedWriter(new FileWriter(County_file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * use BufferedoutputStream
         */
        //BufferedWriter Print = new BufferedWriter(County_file_by_age[1]);

        for (int Index = 0; Index < Parameters.DataPackSize; Index++){
            for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                try {
                    Print_to_file.write(Parameters.AgeBand[Age_Band] + "_" + Parameters.ColumnName[Index] + ",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Print_to_file.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int Date = 0; Date < DataSeries.getLength(); Date++) {
            for (int Index = 0; Index < Parameters.DataPackSize; Index++){
                for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                    try {
                        Print_to_file.write((DataSeries.getTimeSeries()[Date].getDataPackByAge()[0][Index][Age_Band] + DataSeries.getTimeSeries()[Date].getDataPackByAge()[1][Index][Age_Band]) + ",");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Print_to_file.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public static void Print_by_district_to_one_file(CountyDataArray[] District_data, int District, Trail trail){
        String Common_Path = Parameters.WritePath;
        Common_Path += "Iteration " + trail.getModel_Iteratons() + "\\" + "Trail " + trail.getTrail_Code() + "\\Stratified\\By_District\\";
        String FolderPath = Common_Path + District + "\\By_Age\\";
        //Directory_Creator.create_dir(FolderPath);
        String File_Path = Common_Path + District + ".csv";
        System.out.println(File_Path);
        
        CountyDataArray bufferpack = new CountyDataArray();

        for (int Date = 0; Date < District_data[0].getLength(); Date++) {
            for (int county_within = 0; county_within < District_data.length; county_within++) {
                for (int index = 1; index < Parameters.DataPackSize; index++) {/**DO NOT ADD THE DATE*/
                    bufferpack.getTimeSeries()[Date].addValueDataPack(index, District_data[county_within].getTimeSeries()[Date].getDataPack()[index]);
                    for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
                        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                            bufferpack.getTimeSeries()[Date].addValueDataPackByAge(Variant,Age_Band,index, District_data[county_within].getTimeSeries()[Date].getDataPack()[index]);
                        }
                    }
                }
            }
        }

        File County_file = new File(File_Path);

        BufferedWriter Print_to_file = null;
        try {
            Print_to_file = new BufferedWriter(new FileWriter(County_file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int Index = 0; Index < Parameters.DataPackSize; Index++) {

            try {
                Print_to_file.write((Parameters.ColumnName[Index] + ","));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Print_to_file.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int Date = 0; Date < District_data[0].getLength(); Date++) {

            String A_line = "";
            for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                A_line += bufferpack.getTimeSeries()[Date].getDataPack()[Index] + ",";
            }
            A_line = A_line.substring(0,A_line.length()-1);
            try {
                Print_to_file.write(A_line);
                Print_to_file.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Print_to_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Print_ALL(CountyDataArray[] District_data, Trail trail){
        String Common_Path = Parameters.WritePath;
        Common_Path += "Iteration " + trail.getModel_Iteratons() + "\\" + "Trail " + trail.getTrail_Code() + "\\Stratified\\By_District\\";
        String FolderPath = Common_Path + "Ontario" + "\\By_Age\\";
        Directory_Creator.create_dir(FolderPath);
        String File_Path = Common_Path + "Ontario" + ".csv";
        System.out.println(File_Path);

        CountyDataArray bufferpack = new CountyDataArray();

        for (int Date = 0; Date < District_data[0].getLength(); Date++) {
            for (int county_within = 0; county_within < District_data.length; county_within++) {
                for (int index = 1; index < Parameters.DataPackSize; index++) {/**DO NOT ADD THE DATE AND POPULATION*/
                    bufferpack.getTimeSeries()[Date].addValueDataPack(index, District_data[county_within].getTimeSeries()[Date].getDataPack()[index]);
                    for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
                        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                            bufferpack.getTimeSeries()[Date].addValueDataPackByAge(Variant,Age_Band,index, District_data[county_within].getTimeSeries()[Date].getDataPack()[index]);
                        }
                    }
                }
            }
        }

        File County_file = new File(File_Path);

        BufferedWriter Print_to_file = null;
        try {
            Print_to_file = new BufferedWriter(new FileWriter(County_file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int Index = 0; Index < Parameters.DataPackSize; Index++) {

            try {
                Print_to_file.write((Parameters.ColumnName[Index] + ","));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Print_to_file.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int Date = 0; Date < District_data[0].getLength(); Date++) {

            String A_line = "";
            for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                A_line += bufferpack.getTimeSeries()[Date].getDataPack()[Index] + ",";
            }
            A_line = A_line.substring(0,A_line.length()-1);
            try {
                Print_to_file.write(A_line);
                Print_to_file.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Print_to_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Print_ALL_by_age(CountyDataArray[] District_data, Trail trail){
        String Common_Path = Parameters.WritePath;
        Common_Path += "Iteration " + trail.getModel_Iteratons() + "\\" + "Trail " + trail.getTrail_Code() + "\\Stratified\\By_District\\Stratified\\";
        String FolderPath = Common_Path + "Ontario" + "\\By_Age\\";
        Directory_Creator.create_dir(FolderPath);
        String File_Path = Common_Path + "Ontario" + ".csv";
        System.out.println(File_Path);

        CountyDataArray bufferpack = new CountyDataArray();

        for (int Date = 0; Date < District_data[0].getLength(); Date++) {
            for (int county_within = 0; county_within < District_data.length; county_within++) {
                for (int index = 1; index < Parameters.DataPackSize; index++) {
                    bufferpack.getTimeSeries()[Date].addValueDataPack(index, District_data[county_within].getTimeSeries()[Date].getDataPack()[index]);
                    for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
                        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                            bufferpack.getTimeSeries()[Date].addValueDataPackByAge(Variant, Age_Band, index, District_data[county_within].getTimeSeries()[Date].getDataPackByAge()[Variant][index][Age_Band]);
                        }
                    }
                }
            }
        }

        File County_file_by_age[] = new File[16];
        BufferedWriter Print_by_Age[] = new BufferedWriter[16];

        /**
         * use BufferedoutputStream
         */

        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {

            County_file_by_age[Age_Band] = new File(FolderPath + Parameters.AgeBand[Age_Band] + ".csv");

            try {
                Print_by_Age[Age_Band] = new BufferedWriter(new FileWriter(County_file_by_age[Age_Band]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                try {
                    Print_by_Age[Age_Band].write(Parameters.ColumnName[Index] + ",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Print_by_Age[Age_Band].newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int Date = 0; Date < District_data[0].getLength(); Date++) {
                String A_line = "";
                for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                    A_line += (bufferpack.getTimeSeries()[Date].getDataPackByAge()[0][Index][Age_Band] + bufferpack.getTimeSeries()[Date].getDataPackByAge()[1][Index][Age_Band]) + ",";
                }
                A_line = A_line.substring(0,A_line.length()-1);
                try {
                    Print_by_Age[Age_Band].write(A_line);
                    Print_by_Age[Age_Band].newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Print_by_Age[Age_Band].close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void Print_by_district_by_age(CountyDataArray[] District_data, int District, Trail trail) {
        String Common_Path = Parameters.WritePath;
        Common_Path += "Iteration " + trail.getModel_Iteratons() + "\\" + "Trail " + trail.getTrail_Code() + "\\Stratified\\By_District\\Stratified\\";
        String FolderPath = Common_Path + District + "\\By_Age\\";
        Directory_Creator.create_dir(FolderPath);
        String File_Path = Common_Path + District + ".csv";
        System.out.println(File_Path);

        CountyDataArray bufferpack = new CountyDataArray();

        for (int Date = 0; Date < District_data[0].getLength(); Date++) {
            for (int county_within = 0; county_within < District_data.length; county_within++) {
                for (int index = 2; index < Parameters.DataPackSize; index++) {
                    bufferpack.getTimeSeries()[Date].addValueDataPack(index, District_data[county_within].getTimeSeries()[Date].getDataPack()[index]);
                    for (int Variant = 0; Variant < Parameters.Total_number_of_variants; Variant++) {
                        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {
                            bufferpack.getTimeSeries()[Date].addValueDataPackByAge(Variant, Age_Band, index, District_data[county_within].getTimeSeries()[Date].getDataPackByAge()[Variant][index][Age_Band]);
                        }
                    }
                }
            }
        }

        File County_file_by_age[] = new File[16];
        BufferedWriter Print_by_Age[] = new BufferedWriter[16];

        /**
         * use BufferedoutputStream
         */

        //BufferedWriter Print = new BufferedWriter(County_file_by_age[1]);
        for (int Age_Band = 0; Age_Band < 16; Age_Band++) {

            County_file_by_age[Age_Band] = new File(FolderPath + Parameters.AgeBand[Age_Band] + ".csv");

            try {
                Print_by_Age[Age_Band] = new BufferedWriter(new FileWriter(County_file_by_age[Age_Band]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                try {
                    Print_by_Age[Age_Band].write(Parameters.ColumnName[Index] + ",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Print_by_Age[Age_Band].newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int Date = 0; Date < District_data[0].getLength(); Date++) {
                String A_line = "";
                for (int Index = 0; Index < Parameters.DataPackSize; Index++) {
                    A_line += (bufferpack.getTimeSeries()[Date].getDataPackByAge()[0][Index][Age_Band] + bufferpack.getTimeSeries()[Date].getDataPackByAge()[1][Index][Age_Band]) + ",";
                }
                A_line = A_line.substring(0,A_line.length()-1);
                try {
                    Print_by_Age[Age_Band].write(A_line);
                    Print_by_Age[Age_Band].newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Print_by_Age[Age_Band].close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
