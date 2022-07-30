import java.util.*;

public class NeuralNetwork {
    ArrayList<Double[]> Train_dataset = new ArrayList<>();
    ArrayList<Double> Train_desired = new ArrayList<>();
    int Neural[] = {8, 4, 1};
    Double Node[][] = new Double[Neural.length][]; //[layer][node]
    Double Local_gradient_node[][] = new Double[Neural.length][];
    Double AVG_error;
    Double Min_error;
    int Max_Epoch;
    Double biases;
    Matrix[] Layerweight;
    Matrix[] Weightchange;


    public NeuralNetwork(ArrayList data, ArrayList desired) {
        Train_dataset = data;
        Train_desired = desired;

        for (int i = 0; i < Neural.length; i++) {
            Node[i] = new Double[Neural[i]];
            Local_gradient_node[i] = new Double[Neural[i]];
        }

        Layerweight = new Matrix[Neural.length - 1];
        Weightchange = new Matrix[Neural.length - 1];
        for (int layer = 0; layer < Layerweight.length; layer++) {
            Layerweight[layer] = new Matrix(Neural[layer + 1], Neural[layer], true);
            Weightchange[layer] = new Matrix(Neural[layer + 1], Neural[layer], false);
        }

    }

    public void train() {
        int N = 0;
        while (N < Max_Epoch && AVG_error > Min_error) {
            for (int loop = 0; loop < Train_dataset.size(); loop++) {
                int Ran = (int) (Math.random() * Train_dataset.size());

                for (int i = 0; i < Neural[0]; i++) {
                    Node[0][i] = Train_dataset.get(Ran)[i];
                }

                forward_pass();
                find_error();
                backward_pass();

            }

            N++;
        }

    }

    public void forward_pass() {
        for (int layer = 0; layer < Neural.length - 1; layer++) {

            if (Layerweight[layer].cols != Node[layer].length) {
                System.out.println("invalid");
                return;
            }

            Double sum_input;
            Double[] sum_inputnode = new Double[Neural[layer + 1]];


            for (int j = 0; j < Neural[layer + 1]; j++) {
                double sum = 0;

                for (int k = 0; k < Node[layer].length; k++) sum += Layerweight[layer].data[j][k]*activation_fn(Node[layer][k]);

                sum_input = sum + biases;
                sum_inputnode[j] = sum_input;
            }

            Node[layer + 1] = sum_inputnode;
        }

    }

    public void find_error() {

    }

    public void backward_pass() {

    }

    public Double activation_fn(Double v){

        return 0.0;
    }
}