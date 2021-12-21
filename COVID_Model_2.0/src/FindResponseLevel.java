public class FindResponseLevel {
    public static int getResponseLevel(double IncidenceRate){
        int level = 0;
        for (int Level = 0; Level < Parameters.LockDownTiers.length; Level++) {
            if(Parameters.LockDownTiers[level]<=IncidenceRate){
                level = Level;
            }
        }
        return level;
    }
}
