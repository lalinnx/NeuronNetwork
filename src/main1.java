import java.io.*;
import java.util.*;

public class main1 {
    public static void main(String[] args) {

        for (int r=0; r<10; r++) {
            ArrayList<Double[]> Train_dataset = new ArrayList<>();
            ArrayList<Double[]> Train_desired = new ArrayList<>();
            ArrayList<Double[]> Test_dataset = new ArrayList<>();
            ArrayList<Double[]> Test_desired = new ArrayList<>();

            //read file
            try {
                File myObj = new File("src/Flood_dataset.txt");
                Scanner myReader = new Scanner(myObj);
                int line = 0;
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] dataline = data.split("\t", 9);
                    int i = 0;
                    Double[] arraydata = new Double[8];
                    Double[] arraydesired = new Double[1];

                    //test
                    if (line % 10 == r) {
                        for (String a : dataline) {
                            if (i == 8) {
                                arraydesired[0] = Double.parseDouble(a) / 700;
                                Test_desired.add(arraydesired);
                                Test_dataset.add(arraydata);
                            } else arraydata[i] = Double.parseDouble(a) / 700;

                            i++;
                        }
                    }
                    //train
                    else {
                        for (String a : dataline) {
                            if (i == 8) {
                                arraydesired[0] = Double.parseDouble(a) / 700;
                                Train_desired.add(arraydesired);
                                Train_dataset.add(arraydata);
                            } else arraydata[i] = Double.parseDouble(a) / 700;

                            i++;
                        }
                    }

                    line++;

                }
                myReader.close();

                int[] Layer = {8, 10, 6, 1};

                NeuralNetwork nn = new NeuralNetwork(Train_dataset, Train_desired, Test_dataset, Test_desired, Layer, 0.01, 1.0, 2000.0, 0.01, false);
                System.out.println("------ Set " + (r+1) + " Test result ------");
                System.out.println(" ");
                nn.train();
                System.out.println(" ");

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

    }
}
