import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class main2 {
    public static void main(String[] args) {
        for (int r=1; r<30; r+=3) {
            ArrayList<Double[]> Train_dataset = new ArrayList<>();
            ArrayList<Double[]> Train_desired = new ArrayList<>();
            ArrayList<Double[]> Test_dataset = new ArrayList<>();
            ArrayList<Double[]> Test_desired = new ArrayList<>();
            //read file
            try {
                File myObj = new File("src/cross.txt");
                Scanner myReader = new Scanner(myObj);

                int line = 0;
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    Double[] arraydata = new Double[2];
                    Double[] arraydesired = new Double[2];
                    int i = 0;

                    if (line % 3 == 1) {
                        String[] dataline = data.split("( )+");
                        for (String a : dataline) {
                            arraydata[i] = Double.parseDouble(a);
                            i++;
                        }
                        if (line % (30) == r) {
                            Test_dataset.add(arraydata);
                        } else Train_dataset.add(arraydata);
                    } else if (line % 3 == 2) {
                        String[] dataline = data.split("( )+");
                        for (String a : dataline) {
                            arraydata[i] = Double.parseDouble(a);
                            i++;
                        }
                        if ((line - 1) % (30) == r) {
                            Test_desired.add(arraydata);
                        } else Train_desired.add(arraydata);
                    }
                    line++;

                }

                myReader.close();

                int[] Layer = {2, 7, 6, 2};


                NeuralNetwork nn = new NeuralNetwork(Train_dataset, Train_desired, Test_dataset, Test_desired, Layer, 0.01, 1.0, 1000, 0.01, true);
                System.out.println("------ Set " + (r/3+1) + " Test result ------");
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

