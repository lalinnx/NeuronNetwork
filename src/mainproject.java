import java.io.*;
import java.util.*;

public class mainproject {
    public static void main(String[] args) {

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

                //devide train and test
                if(line %10 == 0) {
                    Double[] arraydata = new Double[8];
                    Double[] arraydesired = new Double[1];
                    for (String a : dataline) {
                        if (i == 8){
                            arraydesired[0] = Double.parseDouble(a)/500;
                            Test_desired.add(arraydesired);
                            Test_dataset.add(arraydata);
                        }
                        else arraydata[i] = Double.parseDouble(a)/500;

                        i++;
                    }
                }else{
                    Double[] arraydata = new Double[8];
                    Double[] arraydesired = new Double[1];
                    for (String a : dataline) {
                        if(i==8){
                            arraydesired[0] = Double.parseDouble(a)/800;
                            Train_desired.add(arraydesired);
                            Train_dataset.add(arraydata);
                        }
                        else arraydata[i] = Double.parseDouble(a)/800 ;

                        i++;
                    }
                }

                line++;

            }
            myReader.close();

            NeuralNetwork nn = new NeuralNetwork(Train_dataset,Train_desired);
            nn.train();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



    }
}
