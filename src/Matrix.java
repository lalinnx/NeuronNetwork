import java.util.Random;

public class Matrix {
    double[][] data;
    int rows, cols;

    public Matrix(int rows, int cols, boolean random) {
        data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
        Random generator = new Random(10);

        if (random) {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    double ran = 0;
                    while (ran == 0) {
                        ran = generator.nextDouble() * 2 - 1;
                        data[j][i] = ran;
                    }
                }
            }
        }
    }

    public void set(int row, int col, double value) {
        this.data[row][col] = value;
    }

}