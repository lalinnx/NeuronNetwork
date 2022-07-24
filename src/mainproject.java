import java.io.*;
import java.util.*;

public class mainproject {
    public static void main(String[] args) {

        ArrayList<Double[]> Train_dataset = new ArrayList<Double[]>();
        ArrayList<Double> Train_desired = new ArrayList<Double>();
        ArrayList<Double[]> Test_dataset = new ArrayList<Double[]>();
        ArrayList<Double> Test_desired = new ArrayList<Double>();

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
                    for (String a : dataline) {

                        Double[] arraydata = new Double[8];
                        if (i == 8){
                            Test_desired.add(Double.parseDouble(a));
                            Test_dataset.add(arraydata);
                        }
                        else arraydata[i] = Double.parseDouble(a) ;

                        i++;
                    }
                }else for (String a : dataline) {

                    Double[] arraydata = new Double[8];

                    if(i==8){
                        Train_desired.add(Double.parseDouble(a));
                        Train_dataset.add(arraydata);
                    }
                    else arraydata[i] = Double.parseDouble(a) ;

                    i++;
                }

                line++;

            }
            myReader.close();

            NeuronNetwork nn = new NeuronNetwork(Train_dataset,Train_desired);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



    }
}
