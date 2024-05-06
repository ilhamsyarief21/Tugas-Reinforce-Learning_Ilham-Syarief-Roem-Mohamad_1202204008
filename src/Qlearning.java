import java.text.DecimalFormat;
import java.util.Random;

/**
 * Q-Learning Java Code
 * @Author Ilham Syarief
 */
public class Qlearning {
    final DecimalFormat df = new DecimalFormat("#.##");

    // path finding
    final double alpha = 0.1;
    final double gamma = 0.9;

    // States A, B, C, D, E, F
    // e.g. from A we can go to B or D
    // C is goal state, reward 100 when B->C or F->C

    //_______
    //|A|B|C| 
    //_______
    //|D|E|F|
    //_______

    final int stateA = 0;
    final int stateB = 1;   
    final int stateC = 2;
    final int stateD = 3;
    final int stateE = 4;
    final int stateF = 5;

    final int statesCount = 6;
    final int[] states = new int[]{stateA, stateB, stateC, stateD, stateE, stateF};

    //Q(s,a)= Q(s,a) + alpha * (R(s,a) + gamma * Max(next state, all actions) - Q(s,a))

    int[][] R = new int[statesCount][statesCount]; // reward lookup
    double[][] Q = new double[statesCount][statesCount]; // Q learning

    int[] actionsFromA = new int[] { stateB, stateD };
    int[] actionsFromB = new int[] { stateA, stateC, stateE };
    int[] actionsFromC = new int[] { stateC };
    int[] actionsFromD = new int[] { stateA, stateE };
    int[] actionsFromE = new int[] { stateB, stateD, stateF };
    int[] actionsFromF = new int[] { stateC, stateE };
    int[][] actions = new int[][] { actionsFromA, actionsFromB, actionsFromC, actionsFromD, actionsFromE, actionsFromF };

    String[] stateName = new String[] { "A", "B", "C", "D", "E", "F" };

    public Qlearning(){
        init();
    }

    public void init(){
        R[stateB][stateC] = 100; // from b to c
        R[stateF][stateC] = 100; // from f to c
    }

    public static void main(String[] args) {
        long BeginTime = System.currentTimeMillis();

        Qlearning obj = new Qlearning();
        obj.run();
        obj.printResult();
        obj.showPolicy();

        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BeginTime) / 1000.0 + "sec.");
    }

    void run() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) { // train episodes
            int state = rand.nextInt(statesCount);
            while (state != stateC) { // goal state
                int[] actionsFromState = actions[state];
                int index = rand.nextInt(actionsFromState.length);
                int action = actionsFromState[index];
                int nextState = action;
                double q = Q(state, action);
                double maxQ = maxQ(nextState);
                int r = R(state, action);

                double value = q + alpha * (r + gamma * maxQ - q);
                setQ(state, action, value);
                state = nextState;
            }
        }
    }

    double maxQ(int s){
        int[] actionsFromState = actions[s];
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < actionsFromState.length; i++) {
            int nextState = actionsFromState[i];
            double value = Q[s][nextState];
            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    int policy(int state){
        int[] actionsFromState = actions[state];
        double maxValue = Double.MIN_VALUE;
        int policyGotoState = state; // default goto self if not found
        for (int i = 0; i < actionsFromState.length; i++) {
            int nextState = actionsFromState[i];
            double value = Q[state][nextState];
            if (value > maxValue){
                maxValue = value;
                policyGotoState = nextState;
            }
        }
        return policyGotoState;
    }

    double Q(int s, int a){
        return Q[s][a];
    }

    void setQ(int s, int a, double value){
        Q[s][a] = value;
    }

    int R(int s, int a){
        return R[s][a];
    }

    void printResult(){
        System.out.println("Print result");
        for (int i = 0; i < Q.length; i++) {
            System.out.print("out from " + stateName[i] + ":  ");
            for (int j = 0; j < Q[i].length; j++) {
                System.out.print(df.format(Q[i][j]) + " ");
            }
            System.out.println();
        }
    }

    void showPolicy(){
        System.out.println("\nshowPolicy");
        for (int i = 0; i < states.length; i++) {
            int from = states[i];
            int to = policy(from);
            System.out.println("from " + stateName[from] + " goto " + stateName[to]);
        }
    }
}
