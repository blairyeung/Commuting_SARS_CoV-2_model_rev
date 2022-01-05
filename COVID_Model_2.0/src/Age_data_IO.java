    import java.io.*;
    import java.util.ArrayList;

    public class Age_data_IO {

        /**
         * Not an IO class
         */

        public static ArrayList[] county_by_phu = null;
        public static int[] population_by_phu = null;

        public static void main(String[] args){
            CountyDataIO.CountyData_IO_Input();
            PHU.PHU_IO_Input();
            county_by_phu = new ArrayList[PHU.number_of_phu];
            population_by_phu = new int[PHU.number_of_phu];
            for (int unit = 0; unit < PHU.number_of_phu; unit++) {
                county_by_phu[unit] = new ArrayList<County>();
            }
            Stratification_by_subdivision();
            Find_incidence_and_death_rate();
        }

        public static void Stratification_by_subdivision(){
            for (int county = 0; county < CountyDataIO.counties.length; county++) {
                int Code = CountyDataIO.counties[county].getDistrict();
                int Index = CountyDataIO.district_code_list.indexOf(Code);
                county_by_phu[PHU.phu_by_division[Index]].add(CountyDataIO.counties[county]);
            }

            for (int unit = 0; unit < PHU.number_of_phu; unit++) {
                int Population_PHU = 0;
                for (int county_within = 0; county_within < county_by_phu[unit].size(); county_within++) {
                    Population_PHU += ((CountyData) county_by_phu[unit].get(county_within)).getPopulation();
                }
                System.out.print(PHU.phu_list.get(unit)+",");
                System.out.println(Population_PHU);
                population_by_phu[unit] = Population_PHU;
            }
        }

        public static void Find_incidence_and_death_rate(){
            double per_pop = 100000.0;

            ArrayList<String> Lines = new ArrayList<>();

            String Path = Parameters.Read_path;
            FileReader read = null;
            try {
                read = new FileReader(Parameters.Model_path + "cases_by_status_and_phu.csv");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader buffread = new BufferedReader(read);
            String line = null;
            String firstline = null;
            try {
                firstline = buffread.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    if (!((line = buffread.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Lines.add(line);
                System.out.println(line);
            }

            FileWriter writer = null;
            try {
                writer = new FileWriter(Parameters.Model_path + "cases_by_status_and_phu_population.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            try {
                bufferedWriter.write(firstline);
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < Lines.size(); i++) {
                String l = Lines.get(i);
                l = l.substring(l.indexOf(",")+1);
                String unit = l.substring(0, l.indexOf(","));
                l = l.substring(l.indexOf(",")+1);
                double Cases = Integer.parseInt(l.substring(0, l.indexOf(",")));
                double Death = Integer.parseInt(l.substring(l.indexOf(",")+1));
                System.out.println(unit);
                int index = PHU.phu_list.indexOf(unit);
                System.out.println(index);
                double incideath_rate = 110 * per_pop * Death/population_by_phu[index];
                double incidence_rate = 1.92 *  per_pop * Cases/population_by_phu[index];
                System.out.println(population_by_phu[index]);
                System.out.println(incideath_rate);
                System.out.println(incidence_rate);
                String LINE = Lines.get(i).substring(0, Lines.get(i).indexOf(",")) +"," + unit + "," + incideath_rate + "," + incidence_rate;
                try {
                    bufferedWriter.write(LINE);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
