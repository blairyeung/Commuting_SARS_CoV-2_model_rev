import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MatrixSynthsis {
    private static String[] Types = {"work","school","home","others"};
    public static void main(String[] args) {
        double[][][] Matrices = Input();

        for (int x = 0; x < 5; x++) {
            double[] P = Parameters.Scenarios[x];
            double[][] R = Synthesis(Matrices,P);
            System.out.print("Type,");
            for (int i = 0; i < 16; i++) {
                System.out.print(Parameters.AgeBand[i]+",");
            }
            System.out.println();
            for (int i = 0; i < 16; i++) {
                System.out.print(Parameters.AgeBand[i]+",");
                for (int i1 = 0; i1 < 15; i1++) {
                    System.out.print(R[i][i1]+",");
                }
                System.out.println(R[i][15]);
            }
            for (int i = 0; i < 5; i++) {
                Print(R,x);
            }
        }

    }

    public static double[][] Synthesis(double[][][] Matrices, double[] Parameters){
        double[][] Synthesized = new double[16][16];
        for (int i = 0; i < Matrices.length; i++) {
            for (int i1 = 0; i1 < 16; i1++) {
                for (int i2 = 0; i2 < 16; i2++) {
                    //Synthesized[i1][i2] += Parameters[i] * Matrices[i][i1][i2];
                    Synthesized[i1][i2] += Parameters[i] * Matrices[i][i1][i2];
                }
            }
        }

        return Synthesized;
    }

    public static double[][] Synthesis_from_preset(double[][][] Matrices, double[] Parameter, int Mode){

        double[] Preset = Parameters.TimeMatrix[Mode];

        /**
         * For residential transmission (night)
         * Preset = Night (Work+School+Other), Day (Residential)
         * Paramters =
         */

        double[][] Synthesized = new double[16][16];

        for (int i = 0; i < Matrices.length; i++) {
            for (int i1 = 0; i1 < 16; i1++) {
                for (int i2 = 0; i2 < 16; i2++) {
                    Synthesized[i1][i2] += Parameter[i] * Preset[i] * Matrices[i][i1][i2] * Parameters.TimeMatrix[Mode][i];
                }
            }
        }

        return Synthesized;
    }

    public static double[][][] Input(){
        String Pre = "E:\\Global Model\\SocialMixing\\";
        double[][][] Mat = new double[4][16][16];
        for (int i = 0; i < Types.length; i++) {
            String type = Types[i];
            String Sur = " SocialMixing.csv";
            String Path = Pre + type + "\\" + "CA " + type +Sur;
            System.out.println(Path);
            File file = new File(Path);
            Scanner Scan = null;
            try {
                Scan = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Scan.nextLine();
            for (int i1 = 0; i1 < 16; i1++) {
                String L = Scan.nextLine();
                L = L.substring(L.indexOf(",")+1);
                for (int i2 = 0; i2 < 16; i2++) {
                    String Value = L.substring(0,L.indexOf(","));
                    L = L.substring(L.indexOf(",")+1);
                    double d = Double.parseDouble(Value);
                    Mat[i][i1][i2] = d;
                }
                //double d = Double.parseDouble(L);
                //Mat[i][i1][15] = d;
            }
        }
        return Mat;
    }

    public static void Print(double[][] Matrix, int ind){
        File O = new File("E:\\Global Model\\SocialMixing\\Canada\\Scenario " + Parameters.LockdownLevels[ind] +"1.csv");
        for (int i = 0; i < 16; i++) {
            for (int i1 = 0; i1 < 16; i1++) {
                Matrix[i][i1] *= Parameters.LockdownResPro[ind];
            }
        }
        PrintWriter printer = null;
        try {
            printer = new PrintWriter(O);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        printer.print("Type,");
        for (int i = 0; i < 15; i++) {
            printer.print(Parameters.AgeBand[i]+",");
        }
        printer.println(Parameters.AgeBand[15]);
        for (int i = 0; i < 16; i++) {
            printer.print(Parameters.AgeBand[i]+",");
            for (int i1 = 0; i1 < 15; i1++) {
                printer.print(Matrix[i][i1]+",");
            }
            printer.println(Matrix[i][15]);
        }
        printer.close();
    }
}
