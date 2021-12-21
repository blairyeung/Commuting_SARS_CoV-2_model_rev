import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Matrix_Stratification {

    public static String[] Category_type = {"rural","urban"};
    public static String[] Mat_type = {"work","school","home","others","all"};

    public static void main(String[] args) throws Exception{
        //ISO3166_3toISO3166_2();
        //Stratify_by_county_type();
        Stratify_by_country();
    }

    public static void ISO3166_3toISO3166_2() throws Exception{
        String Folder_Path = Paths.IO_Path + "Matrix\\Category\\synthetic_contacts_ISO3166_3.csv";
        System.out.println(Folder_Path);
        File ISO3166_3_File = new File(Folder_Path);
        FileInputStream Stream = null;
        try {
            Stream = new FileInputStream(ISO3166_3_File);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader reader = new InputStreamReader(Stream);
        BufferedReader IO_read = new BufferedReader(reader);

        String str = "";
    }

    public static void Stratify_by_county_type() throws Exception{
        String Folder_Path = Paths.IO_Path + "Matrix_IO\\Matrix\\Category\\synthetic_contacts_ISO3166_2.csv";
        System.out.println(Folder_Path);
        //File ISO3166_2_File = new File(Folder_Path);


        //FileInputStream Stream = null;
        BufferedReader reader = null;
        try {
             reader = new BufferedReader(new FileReader(Folder_Path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




        String str = "";
        int Columns = 6;

        ArrayList<String> BufferedLines = new ArrayList<>();
        ArrayList<String[]> BufferedPack = new ArrayList<>();

        ArrayList<String> County_Type = new ArrayList<>();
        ArrayList<String> Matrix_Type = new ArrayList<>();

        while((str = reader.readLine())!=null){
            System.out.println(str);
            BufferedLines.add(str);
        }



        for (int line = 0; line < BufferedLines.size(); line++) {
            String buffered = BufferedLines.get(line);

            String bufferpack[] = new String[Columns];
            String excerpt;
            for (int i = 0; i < Columns-1; i++) {
                excerpt = buffered.substring(0,buffered.indexOf(","));
                bufferpack[i] = excerpt;
                buffered = buffered.substring(buffered.indexOf(",")+1);
            }
            excerpt = buffered;
            bufferpack[Columns-1] = excerpt;

            String county_type = bufferpack[1];
            String matrix_type = bufferpack[2];

            if (!County_Type.contains(county_type)){
                County_Type.add(county_type);
                System.out.println(county_type);
            }

            if (!Matrix_Type.contains(matrix_type)) {
                Matrix_Type.add(matrix_type);
                System.out.println(matrix_type);
            }

            BufferedPack.add(bufferpack);
        }

        ArrayList[][] Stratified = new ArrayList[County_Type.size()][Matrix_Type.size()];

        for (int County_type = 0; County_type < Stratified.length; County_type++) {
            for (int Matrix_type = 0; Matrix_type < Stratified[County_type].length; Matrix_type++) {
                Stratified[County_type][Matrix_type] = new ArrayList<String>();
            }
        }

        for (int line = 0; line < BufferedPack.size(); line++) {
            String Pack[] = BufferedPack.get(line);
            String county = Pack[1];
            String matrix = Pack[2];


            int county_index = County_Type.indexOf(county);
            int matrix_index = Matrix_Type.indexOf(matrix);

            Stratified[county_index][matrix_index].add(BufferedLines.get(line));
        }


        for (int County_type = 0; County_type < Stratified.length; County_type++) {
            for (int Matrix_type = 0; Matrix_type < Stratified[County_type].length; Matrix_type++) {
                Directory_Creator.create_dir(Paths.IO_Path + "Matrix_IO\\Matrix\\Category\\" + County_Type.get(County_type) + "\\");
                String path =  Paths.IO_Path + "Matrix_IO\\Matrix\\Category\\" + County_Type.get(County_type) + "\\" + Matrix_Type.get(Matrix_type) + "  ISO3166_2.csv";
                PrintStream out = new PrintStream(path);
                //BufferedOutputStream stream = new BufferedOutputStream(out);
                for (int i = 0; i < Stratified[County_type][Matrix_type].size(); i++) {
                    out.println(Stratified[County_type][Matrix_type].get(i));
                }
                out.close();
            }
        }
    }

    public static void Stratify_by_country() throws Exception{
        for (int i = 0; i < Category_type.length; i++) {
            Directory_Creator.create_dir(Paths.IO_Path + "Matrix_IO\\Matrix_by_Category\\" + Category_type[i] + "\\");
            for (int i1 = 0; i1 < Mat_type.length; i1++) {
                Directory_Creator.create_dir(Paths.IO_Path + "Matrix_IO\\Matrix_by_Category\\" + Category_type[i] + "\\" + Mat_type[i1] + "\\");
            }
        }

        ArrayList<String> Countryname = new ArrayList<>();
        ArrayList<String> Countrycode = new ArrayList<>();

        BufferedReader Codereader = null;
        try {
            Codereader = new BufferedReader(new FileReader(Parameters.ReadPath+"Country_Files\\Country_Age_Distribution.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String Lineread = "";

        while (true) {
            try {
                if (!((Lineread = Codereader.readLine())!=null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            int comma = Lineread.indexOf(",");
            String sub1 = Lineread.substring(comma+1);
            String sub2 = Lineread.substring(0,comma);
            String c = sub1.substring(0,sub1.indexOf(","));
            sub1 = sub1.substring(sub1.indexOf(",")+1);
            String r = sub1.substring(0,sub1.indexOf(","));
            sub1 = sub1.substring(sub1.indexOf(",")+1);
            double[] ages = new double[16];
            for (int i = 0; i < 16; i++) {
                String b;
                if(i==16){
                    b = sub1;
                }
                else {
                    b = sub1.substring(0,sub1.indexOf(","));
                }
                sub1 = sub1.substring(sub1.indexOf(",")+1);
                double d = Double.parseDouble(b);
                ages[i] = d;
            }
            Countryname.add(sub2);
            Countrycode.add(c);
        }



        for (int Category = 0; Category < Category_type.length; Category++) {
            for (int Mat = 0; Mat < Mat_type.length; Mat++) {
                String ReadFilePath = Paths.IO_Path + "Matrix_IO\\Matrix\\Category\\";
                ReadFilePath = ReadFilePath + Category_type[Category] + "\\" + Mat_type[Mat] + "  ISO3166_2.csv";
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(ReadFilePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                String str = "";

                ArrayList<String> Lines = new ArrayList<>();
                ArrayList<String[]> DataPack = new ArrayList<>();
                ArrayList<String> Countries = new ArrayList<>();

                int Columns = 6;

                while((str = reader.readLine())!=null){
                    //System.out.println(str);
                    Lines.add(str);
                    String bufferpack[] = new String[Columns];
                    String excerpt;
                    for (int i = 0; i < Columns-1; i++) {
                        excerpt = str.substring(0,str.indexOf(","));
                        bufferpack[i] = excerpt;
                        str = str.substring(str.indexOf(",")+1);
                    }
                    excerpt = str;
                    bufferpack[Columns-1] = excerpt;
                    DataPack.add(bufferpack);
                }

                for (int line = 0; line < Lines.size(); line++) {
                    String buffer = Lines.get(line);
                    String country = buffer.substring(0,2);
                    if(!Countries.contains(country)){
                        Countries.add(country);
                    }
                }

                ArrayList[] By_Countries = new ArrayList[Countries.size()];

                for (int country = 0; country < Countries.size(); country++) {
                    By_Countries[country] = new ArrayList<String[]>();
                }


                for (int line = 0; line < Lines.size(); line++) {
                    String buffer = Lines.get(line);
                    String country = buffer.substring(0,2);
                    String[] Buffpack = DataPack.get(line);
                    By_Countries[Countries.indexOf(country)].add(Buffpack);
                }


                for (int country = 0; country < Countrycode.size(); country++) {
                    String Output_path = Paths.IO_Path + "Matrix_IO\\Matrix_by_Category\\" + Category_type[Category] + "\\" + Mat_type[Mat] + "\\";
                    Output_path = Output_path + Countrycode.get(country) + ".csv";
                    PrintStream printStream = new PrintStream(new FileOutputStream(new File(Output_path)));
                    int ind = 0;

                    String FirstLine = "Age_Band_i/Age_band_j,";

                    int Use_China = Countries.indexOf("CN");
                    int Relative_index = Countries.indexOf(Countrycode.get(country));

                    if(Relative_index==-1){
                        Relative_index = Use_China;
                    }

                    for (int i = 0; i < 16; i++) {
                        FirstLine += (Parameters.AgeBand[i] + ",");
                    }

                    FirstLine = FirstLine.substring(0,FirstLine.length()-1);

                    printStream.println(FirstLine);

                    for (int Row = 0; Row < 16; Row++) {
                        String line = Parameters.AgeBand[Row] + ",";
                        for (int Column = 0; Column < 16; Column++) {
                            String[] data = (String[]) By_Countries[Relative_index].get(ind);
                            line += (data[5] + ",");
                            ind++;
                        }
                        line = line.substring(0,line.length()-1);
                        printStream.println(line);
                    }
                    printStream.close();
                }

                /*for (int country = 0; country < Countries.size(); country++) {
                    String Output_path = Paths.IO_Path + "Matrix_IO\\Matrix_by_Category\\" + Category_type[Category] + "\\" + Mat_type[Mat] + "\\";
                    Output_path = Output_path + Countries.get(country) + ".csv";
                    PrintStream printStream = new PrintStream(new FileOutputStream(new File(Output_path)));

                    int ind = 0;


                    String FirstLine = "Age_Band_i/Age_band_j,";

                    for (int i = 0; i < 16; i++) {
                        FirstLine += (Parameters.AgeBand[i] + ",");
                    }

                    FirstLine = FirstLine.substring(0,FirstLine.length()-1);

                    printStream.println(FirstLine);

                    for (int Row = 0; Row < 16; Row++) {
                        String line = Parameters.AgeBand[Row] + ",";
                        for (int Column = 0; Column < 16; Column++) {
                            String[] data = (String[]) By_Countries[country].get(ind);
                            line += (data[5] + ",");
                            ind++;
                        }
                        line = line.substring(0,line.length()-1);
                        printStream.println(line);
                    }
                    printStream.close();
                }*/
            }
        }
    }
}
