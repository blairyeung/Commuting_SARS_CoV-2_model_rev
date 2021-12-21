public class MatrixSynthesisByCategory {
    public static double[][] Synthesis(double[][][] Matrices, double[] Parameter){
        double[][] Synthesized = new double[16][16];
        for (int i = 0; i < Matrices.length; i++) {

            int Synthesis_Mode = Main.Mode;

            switch (Synthesis_Mode){
                case 0:
                    /**
                     * Work (Day Time)
                     */
                    for (int i1 = 0; i1 < 16; i1++) {
                        for (int i2 = 0; i2 < 16; i2++) {
                            Synthesized[i1][i2] += Parameter[i] * Matrices[i][i1][i2] * (Parameters.Day_Time[i]);
                        }
                    }
                case 1:
                    /**
                     * Resident (Night Time)
                     */
                    for (int i1 = 0; i1 < 16; i1++) {
                        for (int i2 = 0; i2 < 16; i2++) {
                            Synthesized[i1][i2] += Parameter[i] * Matrices[i][i1][i2] * (Parameters.Night_Time[i]);
                        }
                    }
            }
        }
        return Synthesized;
    }
}
