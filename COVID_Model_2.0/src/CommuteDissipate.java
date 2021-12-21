public class CommuteDissipate {
    public double[][] getCommuteArray(){
        double[][] CommuteArray = new double[2][CountyDataIO.Counties.length];

        for (int i = 0; i < CountyDataIO.Counties.length; i++) {
            int Resident_County_Population = CountyDataIO.Counties[i].getPopulation();
            int Work_County_Population = CountyDataIO.Counties[i].getPopulation();
        }
        return CommuteArray;
    }

    public int[] FindCoutiesWithinRange(double[] StartCoord){

        int Exported_Population = 0;

        /**
         * Draw a circle with a diameter of 200km
         */

        for (int i = 0; i < CountyDataIO.Counties.length; i++) {
            double[] Coords = CountyDataIO.Counties[i].getCoordinate();

        }

        return new int[1];
    }
}
