import java.util.*;

public class NeuralNetwork {
    ArrayList<Double[]> Train_dataset = new ArrayList<>();
    ArrayList<Double[]> Train_desired = new ArrayList<>();
    ArrayList<Double[]> Test_dataset = new ArrayList<>();
    ArrayList<Double[]> Test_desired = new ArrayList<>();
    int Layer[];
    int Output[];
    int TOutput[];
    Double Desired[];
    Double Node[][]; //[layer][node]
    Double LocalGradient[][];
    Double Error[];
    Double SError[];
    Double TError[];
    Double AVGError = 10000.0;
    Double MinError = 0.00001;
    Double MaxEpoch;
    Double Biases;
    Double N;
    Double M;
    Matrix[] LayerWeight;
    Matrix[] WeightChange;
    boolean find;

    public NeuralNetwork(ArrayList data, ArrayList desired, ArrayList test, ArrayList testd, int l[], double n, double b,double me, double m, boolean f) {
        Train_dataset = data;
        Train_desired = desired;
        Test_desired = testd;
        Test_dataset = test;
        Layer = l;
        N = n;
        Biases = b;
        MaxEpoch = me;
        M = m;
        find = f;

        create();
    }

    public void create(){
        Desired = new Double[Layer[Layer.length-1]];
        Node = new Double[Layer.length][]; //[layer][node]
        LocalGradient = new Double[Layer.length][];
        Error = new Double[Layer[Layer.length-1]];
        SError = new Double[Train_dataset.size()];
        Output = new int[Train_dataset.size()];
        TError = new Double[Test_dataset.size()];
        TOutput = new int[Test_dataset.size()];
        LayerWeight = new Matrix[Layer.length - 1];
        WeightChange = new Matrix[Layer.length - 1];


        //create node
        for (int i = 0; i < Layer.length; i++) {
            Node[i] = new Double[Layer[i]];
            LocalGradient[i] = new Double[Layer[i]];
        }

        //randwom weight
        for (int layer = 0; layer < LayerWeight.length; layer++) {
            LayerWeight[layer] = new Matrix(Layer[layer + 1], Layer[layer], true);
            WeightChange[layer] = new Matrix(Layer[layer + 1], Layer[layer], false);
        }
    }

    public void train() {
        int n = 0;
        while (n < MaxEpoch && AVGError > MinError) {
            Double[] d = new Double[2];
            Double[] o = new Double[2];
            double tp = 0.0;
            double tn = 0.0;
            double fp = 0.0;
            double fn = 0.0;
            double precision = 0.0;
            double recall = 0.0;
            double accuracy = 0.0;

            //train by size of data set
            for (int loop = 0; loop < Train_dataset.size(); loop++) {
                int Ran = (int) (Math.random() * Train_dataset.size());

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

                //update error
                if(find){
                    d[0] = Desired[0];
                    d[1] = Desired[1];

                    if(Node[Layer.length-1][0] > Node[Layer.length-1][1]){
                        o[0] = 1.0;
                        o[1] = 0.0;
                    }else{
                        o[0] = 0.0;
                        o[1] = 1.0;
                    }

                    if(o[0].equals(d[0]) && o[1].equals(d[1]) && o[0].equals(1.0)) tp++;

                    if(o[0].equals(d[0]) && o[1].equals(d[1]) && o[1].equals(1.0)) tn++;

                    if(!o[0].equals(d[0]) && !o[1].equals(d[1]) && o[0].equals(1.0)) fp++;

                    if(!o[0].equals(d[0]) && !o[1].equals(d[1]) && o[1].equals(1.0)) fn++;

                }else SError[loop] = Error[0];
            }

            if(find && n == MaxEpoch-1){
                precision = tp/(tp+fp);
                recall = tp/(tp+fn);
                accuracy = (tp+tn)/(tp+tn+fp+fn);

                System.out.println("------ Train ------");
                System.out.println("tp : " + tp);
                System.out.println("tn : " + tn);
                System.out.println("fp : " + fp);
                System.out.println("fn : " + fn);
                System.out.println("Precision : " + precision);
                System.out.println("Recall : " + recall);
                System.out.println("Accuracy : " + accuracy);
            }else if(!find){
                double sum = 0;
                for (int i = 0; i < Train_dataset.size(); i++) {
                    sum += Math.pow(SError[i], 2);
                }
                sum = sum / 2;
                AVGError = sum / Train_desired.size();
            }
            n++;
        }

        test();

    }

    public void test() {
        int c=1;
        Double[] d = new Double[2];
        Double[] o = new Double[2];
        double tp = 0.0;
        double tn = 0.0;
        double fp = 0.0;
        double fn = 0.0;
        double precision = 0.0;
        double recall = 0.0;
        double accuracy = 0.0;
        for (int loop = 0; loop < Test_dataset.size(); loop++) {
            for(int i = 0; i< Layer[Layer.length-1]; i++) {
                    Desired[i] = Test_desired.get(loop)[i];
            }
            for (int i = 0; i < Layer[0]; i++) {
                    Node[0][i] = Test_dataset.get(loop)[i];
            }

            forward_pass();
            find_error();

            if(find){
                d[0] = Desired[0];
                d[1] = Desired[1];

                if(Node[Layer.length-1][0] > Node[Layer.length-1][1]){
                    o[0] = 1.0;
                    o[1] = 0.0;
                }else{
                    o[0] = 0.0;
                    o[1] = 1.0;
                }

                if(o[0].equals(d[0]) && o[1].equals(d[1]) && o[0].equals(1.0)) tp++;

                if(o[0].equals(d[0]) && o[1].equals(d[1]) && o[1].equals(1.0)) tn++;

                if(!o[0].equals(d[0]) && !o[1].equals(d[1]) && o[0].equals(1.0)) fp++;

                if(!o[0].equals(d[0]) && !o[1].equals(d[1]) && o[1].equals(1.0)) fn++;

            }else TError[loop] = Error[0];

            System.out.print(c);
            if(find) System.out.println("  Desired : " + Desired[0] + " " + Desired[1] + "    Output : " + activation_fn(Node[Layer.length-1][0]) + " " + activation_fn(Node[Layer.length-1][1]) );
            else System.out.println("  Desired : " + Desired[0]*700 + "    Output : "  + activation_fn(Node[Layer.length-1][0])*700 + "    Error : " + Error[0]*700);

            c++;

        }

        if(find){
            precision = tp/(tp+fp);
            recall = tp/(tp+fn);
            accuracy = (tp+tn)/(tp+tn+fp+fn);
            System.out.println("\n------ Test ------");
            System.out.println("tp : " + tp);
            System.out.println("tn : " + tn);
            System.out.println("fp : " + fp);
            System.out.println("fn : " + fn);
            System.out.println("Precision : " + precision);
            System.out.println("Recall : " + recall);
            System.out.println("Accuracy : " + accuracy);
        }else{
            double sum = 0;
            for (int i = 0; i < Test_dataset.size(); i++) {
                sum += Math.pow(TError[i], 2);
            }
            sum = sum / 2;
            AVGError = sum / Test_dataset.size();
            System.out.println("AVGError : " + AVGError);
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
                double wc = (WeightChange[hidden].data[o][h]*M) + N * Error[o] * activation_fn(Node[hidden][h]);
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
                    double wc = (WeightChange[i].data[h][j]*M) + N * LocalGradient[i+1][h] * activation_fn(Node[i][j]);
                    WeightChange[i].set(h, j,wc);
                }
            }
        }

    }

    public void changeweight() {
        for(int l = 0; l < LayerWeight.length; l++) { // loop layer
            for(int i = 0; i <Layer[l]; i++){ // loop node
                for(int j = 0; j<Layer[l+1]; j++){ // loop next node
                    double wc = (WeightChange[l].data[j][i]*M) + LayerWeight[l].data[j][i] + WeightChange[l].data[j][i];
                    LayerWeight[l].set(j,i,wc);
                }
            }
        }
    }

    public Double activation_fn(Double v){
        if(v>0) return v;
        else return 0.01;
    }

    public Double activation_fn_diff(Double v){
        if (v<=0.0) return 0.01;
        else return 1.0;
    }

}