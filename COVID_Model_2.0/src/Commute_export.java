public class Commute_export {
    public static void main(String[] args) {
        CountyDataIO.CountyData_IO_Input();
        Commute_IO.Commute_IO_Input();

        int Arriving[] = new int[CountyDataIO.DistrictCodes.length];

        for (int District = 0; District < CountyDataIO.DistrictCodes.length; District++) {
            for (int county = 0; county < CountyDataIO.Counties_By_District[District].size(); county++) {
                //System.out.println(CountyDataIO.Code_Index.get(county));
                Arriving[District] += Commute_IO.getNumber_of_commuters_arriving()[CountyDataIO.Code_Index.indexOf(CountyDataIO.Counties_By_District[District].get(county).getID())];
            }
            System.out.println(CountyDataIO.DistrictCode.get(District) + "," +Arriving[District]);
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
