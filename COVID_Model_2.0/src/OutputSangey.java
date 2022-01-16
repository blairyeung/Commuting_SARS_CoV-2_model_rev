import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OutputSangey {
    public OutputSangey() {

    }

    public static void main(String[] args) throws FileNotFoundException {

        CountyDataIO C = new CountyDataIO();
        Commute_IO Com = new Commute_IO();

        int Tier_Preset[] = new int[CountyDataIO.counties.length];

        //Tier_Preset[0] = 4;

        /*for (int i = 0; i < Tier_Preset.length; i++) {
            Tier_Preset[i] = 4;
        }*/

        Commute.getStaticCommuteMatrix();
        Commute.GenerateWeeklyCommuteMatrixfromMutiplicatoin(Tier_Preset);

        int[][] Mat = Commute_IO.static_commuting_matrix;
        int[][] MaleMat = Commute_IO.static_commuting_matrix_male;
        int[][] FemaleMat = Commute_IO.static_commuting_matrix_female;

        /**
         * Baseline
         */

        for (int i = 0; i < CountyDataIO.counties.length; i++) {
            for (int i1 = 0; i1 < CountyDataIO.counties.length; i1++) {
                System.out.println(Mat[i][i1]);
            }
        }
        //int[][] City = Commute.getTiered_Total_Population_of_potential_city_within_range();

        System.out.println("Resident,Work,ID_work,ID_resident,Value");


        File file = new File("K:\\Ontario Model\\Sankey.csv");
        File Fullfile = new File("K:\\Ontario Model\\FullSankey.csv");

        PrintWriter Printer = new PrintWriter(file);
        PrintWriter FillPrinter = new PrintWriter(Fullfile);

        File[] files = new File[CountyDataIO.counties.length];
        PrintWriter[] Printers = new PrintWriter[CountyDataIO.counties.length];

        Printer.println("Resident,Work,Value,Male,Female,Distance,Type");
        FillPrinter.println("Resident,Work,Value,Male,Female,Distance,Type,Resident_district,Work_district");

        for (int County_Code = 0; County_Code < CountyDataIO.counties.length; County_Code++) {

            files[County_Code] = new File("K:\\Ontario Model\\Sankey_fig2\\" + County_Code +".csv");
            Printers[County_Code] = new PrintWriter(files[County_Code]);

            Printers[County_Code].println("Resident,Work,Value,Male,Female,Distance,Type");

            for (int County_Destination = 0; County_Destination < CountyDataIO.counties.length; County_Destination++) {

                if(County_Destination==0&&County_Code==5){
                    System.out.println();
                    System.out.println(County_Code +"    " + County_Destination + "    " + Mat[County_Code][County_Destination]);
                    System.out.println(false);
                }

                if(Mat[County_Code][County_Destination]>=10){
                    System.out.print(CountyDataIO.counties[County_Code].getName()+",");
                    System.out.print(CountyDataIO.counties[County_Destination].getName()+",");
                    System.out.print(County_Code+",");
                    System.out.print(County_Destination + ",");
                    System.out.print(Mat[County_Code][County_Destination]);

                    if(CountyDataIO.counties[County_Code].getDistrict()==3525||((CountyDataIO.counties[County_Code].getDistrict()>3517&&CountyDataIO.counties[County_Code].getDistrict()<3522))&&CountyDataIO.counties[County_Code].getPopulation()>100000){
                        if(CountyDataIO.counties[County_Destination].getDistrict()==3525||((CountyDataIO.counties[County_Destination].getDistrict()>3517&&CountyDataIO.counties[County_Destination].getDistrict()<3522))&&CountyDataIO.counties[County_Destination].getPopulation()>100000){
                            Printer.print(CountyDataIO.counties[County_Code].getName()+",");
                            Printer.print(CountyDataIO.counties[County_Destination].getName()+",");
                            Printer.print(Mat[County_Code][County_Destination]+",");
                            Printer.print(MaleMat[County_Code][County_Destination]+",");
                            Printer.print(FemaleMat[County_Code][County_Destination]+",");
                            Printer.println(CountyDataIO.distance_between_county[County_Code][County_Destination] + ",");
                        }
                    }

                    /**Printer.print(CountyDataIO.counties[County_Code].getName()+",");
                    Printer.print(CountyDataIO.counties[County_Destination].getName()+",");
                    Printer.print(Mat[County_Code][County_Destination]+",");
                    Printer.print(CountyDataIO.distance_between_county[County_Code][County_Destination] + ",");*/

                    Printers[County_Code].print(CountyDataIO.counties[County_Code].getName()+",");
                    Printers[County_Code].print(CountyDataIO.counties[County_Destination].getName()+",");
                    //Printers[County_Code].print(County_Code + ",");
                    //Printers[County_Code].print(County_Destination + ",");
                    Printers[County_Code].print(Mat[County_Code][County_Destination]+",");
                    Printers[County_Code].print(MaleMat[County_Code][County_Destination]+",");
                    Printers[County_Code].print(FemaleMat[County_Code][County_Destination]+",");
                    Printers[County_Code].print(CountyDataIO.distance_between_county[County_Code][County_Destination]+",");

                    FillPrinter.print(CountyDataIO.counties[County_Code].getName()+",");
                    FillPrinter.print(CountyDataIO.counties[County_Destination].getName()+",");
                    FillPrinter.print(Mat[County_Code][County_Destination]+",");
                    FillPrinter.print(MaleMat[County_Code][County_Destination]+",");
                    FillPrinter.print(FemaleMat[County_Code][County_Destination]+",");
                    FillPrinter.print(CountyDataIO.distance_between_county[County_Code][County_Destination]+",");

                    switch (CountyDataIO.counties[County_Code].getCounty_Type()) {
                        case 0:
                            switch (CountyDataIO.counties[County_Destination].getCounty_Type()) {
                                case 0:
                                    Printers[County_Code].println("Rural_to_Rural");
                                    FillPrinter.print("Rural_to_Rural,");
                                    //Printer.println("Rural_to_Rural");
                                    break;
                                case 1:
                                    Printers[County_Code].println("Rural_to_Urban");
                                    FillPrinter.print("Rural_to_Urban,");
                                    //Printer.println("Rural_to_Urban");
                                    break;
                            }
                            break;
                        case 1:
                            switch (CountyDataIO.counties[County_Destination].getCounty_Type()) {
                                case 0:
                                    Printers[County_Code].println("Urban_to_Rural");
                                    FillPrinter.print("Urban_to_Rural,");
                                    //Printer.println("Urban_to_Rural");
                                    break;
                                case 1:
                                    Printers[County_Code].println("Urban_to_Urban");
                                    FillPrinter.print("Urban_to_Urban,");
                                    //Printer.println("Urban_to_Urban");
                                    break;
                            }
                            break;
                    }

                    FillPrinter.println(CountyDataIO.counties[County_Code].getDistrict() + "," + CountyDataIO.counties[County_Destination].getDistrict());

                }

            }
            Printers[County_Code].close();
        }

        Printer.close();
        FillPrinter.close();


        File IndexFile = new File("K:\\Ontario Model\\Sankey_Index.csv");
        PrintWriter IndexFilePrint = new PrintWriter(IndexFile);

        IndexFilePrint.println("name");
        for (int i = 0; i < CountyDataIO.counties.length; i++) {
            IndexFilePrint.println(CountyDataIO.counties[i].getName());
        }
        IndexFilePrint.close();

        File filebycounty[] = new File[CountyDataIO.district_code_array.length];
        PrintWriter[] CountyPrinters = new PrintWriter[CountyDataIO.district_code_array.length];

        for (int i = 0; i < CountyDataIO.district_code_array.length; i++) {
            filebycounty[i] = new File("K:\\Ontario Model\\" + i + ".csv");
            CountyPrinters[i] = new PrintWriter (filebycounty[i]);
            CountyPrinters[i].println("Resident,Work,Value,Male,Female,Distance,Type");
            int Code = CountyDataIO.district_code_array[i];
            for (int i1 = 0; i1 < CountyDataIO.counties.length; i1++) {
                if(CountyDataIO.counties[i1].getDistrict()==Code){
                    for (int i2 = 0; i2 < CountyDataIO.counties.length; i2++) {
                        if(CountyDataIO.counties[i2].getDistrict()==Code){
                            if(Mat[i1][i2]>=10) {
                                CountyPrinters[i].print(CountyDataIO.counties[i1].getName()+",");
                                CountyPrinters[i].print(CountyDataIO.counties[i2].getName()+",");
                                CountyPrinters[i].print(Mat[i1][i2]+",");
                                CountyPrinters[i].print(MaleMat[i1][i2]+",");
                                CountyPrinters[i].print(FemaleMat[i1][i2]+",");
                                CountyPrinters[i].print(CountyDataIO.distance_between_county[i1][i2] + ",");
                                switch (CountyDataIO.counties[i1].getCounty_Type()) {
                                    case 0:
                                        switch (CountyDataIO.counties[i2].getCounty_Type()) {
                                            case 0:
                                                CountyPrinters[i].println("Rural_to_Rural");
                                                break;
                                            case 1:
                                                CountyPrinters[i].println("Rural_to_Urban");
                                                break;
                                        }
                                        break;
                                    case 1:
                                        switch (CountyDataIO.counties[i2].getCounty_Type()) {
                                            case 0:
                                                CountyPrinters[i].println("Urban_to_Rural");
                                                break;
                                            case 1:
                                                CountyPrinters[i].println("Urban_to_Urban");
                                                break;
                                        }
                                        break;
                                }
                            }

                        }
                    }
                }
            }
            CountyPrinters[i].close();
        }
    }


}
