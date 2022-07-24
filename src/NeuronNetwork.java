import java.util.*;

public class NeuronNetwork {
    ArrayList<Double[]> Train_dataset = new ArrayList<Double[]>();
    ArrayList<Double> Train_desired = new ArrayList<Double>();
    public NeuronNetwork(ArrayList data, ArrayList desired){
        Train_dataset = data;
        Train_desired = desired;
    }
}
