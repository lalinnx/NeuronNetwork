import java.util.*;

public class NeuralNetwork {
    ArrayList<Double[]> Train_dataset = new ArrayList<>();
    ArrayList<Double[]> Train_desired = new ArrayList<>();
    int Layer[] = {8, 7, 1};
    Double Desired[] = new Double[Layer[Layer.length-1]];
    Double Node[][] = new Double[Layer.length][]; //[layer][node]
    Double LocalGradient[][] = new Double[Layer.length][];
    Double Error[] = new Double[Layer[Layer.length-1]];
    Double AVGError = 1000.0;
    Double MinError = 0.00001;
    Double MaxEpoch = 10000.0;
    Double Biases = 1.0;
    Double n =0.01;
    Matrix[] LayerWeight = new Matrix[Layer.length - 1];
    Matrix[] WeightChange = new Matrix[Layer.length - 1];
    int Random[] = new int[300];


    public NeuralNetwork(ArrayList data, ArrayList desired) {
        Train_dataset = data;
        Train_desired = desired;

         //node
        for (int i = 0; i < Layer.length; i++) { //0 1 2
            Node[i] = new Double[Layer[i]];
            LocalGradient[i] = new Double[Layer[i]];
        }

        //weight
        for (int layer = 0; layer < LayerWeight.length; layer++) { // 0 1
            LayerWeight[layer] = new Matrix(Layer[layer + 1], Layer[layer], true);
            WeightChange[layer] = new Matrix(Layer[layer + 1], Layer[layer], false);
        }

    }

    public void train() {
        int N = 0;
        while (N < MaxEpoch && AVGError > MinError) {
            for (int loop = 0; loop < Train_dataset.size(); loop++) {
                int Ran = (int) (Math.random() * Train_dataset.size());
                boolean check = true;
                while (check) {
                    for (int i = 0; i < 300; i++) {
                        if (Ran == Random[i]) {
                            Ran = (int) (Math.random() * Train_dataset.size());
                            break;
                        }
                        if (i==299){
                            check = false;
                        }
                    }
                }

                for(int i = 0; i< Layer[Layer.length-1]; i++) {
                    Desired[i] = Train_desired.get(Ran)[i];
                }

                for (int i = 0; i < Layer[0]; i++) {
                    Node[0][i] = Train_dataset.get(Ran)[i];
                }

                forward_pass();
                find_error();
                backward_pass();
                changeweight();

                System.out.println(Desired[0]*800 + "  " + activation_fn(Node[2][0])*800 + "  " + Error[0]);
            }

            N++;
        }

    }

    public void forward_pass() {
        for(int l = 0; l < Layer.length-1; l++){  //layer 0 1
            for(int n = 0; n < Node[l+1].length; n++){ //next node 1 2
                double sum = 0.0;
                for(int w = 0; w < Node[l].length; w++){ //node
                    sum += (LayerWeight[l].data[n][w]*activation_fn(Node[l][w]));
                }
                Node[l+1][n] = sum+Biases;
            }
        }
    }

    public void find_error() {
        for(int i=0; i < Layer[Layer.length-1]; i++){
             Error[i] = Desired[i] - Node[Layer.length-1][i];
             LocalGradient[Layer.length-1][i] = Error[i];
        }
    }

    public void backward_pass() {
        //output layer
        int hidden = Layer.length - 2;
        int out = Layer.length - 1;
        for(int o = 0; o <Layer[out]; o++) { // loop node of output layer
            for (int h = 0; h < Layer[hidden]; h++) { // loop node of last hidden layer
                double wc = n * Error[o] * activation_fn(Node[hidden][h]);
                WeightChange[hidden].set(o, h, wc);
            }
        }

        //hidden layer
        for(int i = hidden; i>0; i--){ // loop hidden layer
            for (int h = 0; h <Layer[i]; h++){ //  loop node in each hidden
                double sum =0.0;
                for(int n = 0; n<Layer[i+1]; n++){ // loop next layer
                    sum += LayerWeight[i].data[n][h]*LocalGradient[i+1][n];
                }
                LocalGradient[i][h] = activation_fn_diff(Node[i][h])*sum;
            }
        }

        //inputt layer
        for(int i = 0; i<Layer.length-2; i++){ // loop layer from input
            for(int j = 0; j <Layer[i]; j++){ // loop node of each layer
                for(int h = 0; h<Layer[i+1]; h++){ // loop node of next layer
                    double wc = n * LocalGradient[i+1][h] * activation_fn(Node[i][j]);
                    WeightChange[i].set(h, j,wc);
                }
            }
        }

    }

    public void changeweight() {
        for(int l = 0; l < LayerWeight.length; l++) { // loop layer
            for(int i = 0; i <Layer[l]; i++){ // loop node
                for(int j = 0; j<Layer[l+1]; j++){ // loop next node
                    double wc = LayerWeight[l].data[j][i] + WeightChange[l].data[j][i];
                    LayerWeight[l].set(j,i,wc);
                }
            }
        }
    }

    public Double activation_fn(Double v){

        return Math.max(0.01,v);
    }

    public Double activation_fn_diff(Double v){
        if (v<=0) return 0.01;
        else return 1.0;
    }

}