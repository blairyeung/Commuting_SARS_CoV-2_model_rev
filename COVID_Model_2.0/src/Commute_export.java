public class Commute_export {
    public static void main(String[] args) {
        CountyDataIO.CountyData_IO_Input();
        Commute_IO.Commute_IO_Input();

        int Arriving[] = new int[CountyDataIO.district_code_array.length];

        for (int District = 0; District < CountyDataIO.district_code_array.length; District++) {
            for (int county = 0; county < CountyDataIO.county_by_district[District].size(); county++) {
                //System.out.println(CountyDataIO.Code_Index.get(county));
                Arriving[District] += Commute_IO.getNumber_of_commuters_arriving()[CountyDataIO.code_index.indexOf(CountyDataIO.county_by_district[District].get(county).getID())];
            }
            System.out.println(CountyDataIO.district_code_list.get(District) + "," +Arriving[District]);
        }
        
        for (int i = 0; i < Commute_IO.getNumber_of_commuters_arriving().length; i++) {
            //System.out.println(CountyDataIO.Counties[i].getName() + "," + Commute_IO.getNumber_of_commuters_arriving()[i]);
        }

        System.out.println();

        for (int i = 0; i < Commute_IO.getNumber_of_commuters_arriving().length; i++) {
           // System.out.println(CountyDataIO.Counties[i].getName() + ": " + Commute_IO.getNumber_of_commuters_departing()[i]);
        }
    }
}
