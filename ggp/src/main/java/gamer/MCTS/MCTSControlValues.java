package gamer.MCTS;
import java.util.ArrayList;
import java.util.List;

public class MCTSControlValues{
    public long rave;                       // Rave K value to set linear rave weight decrease
    public long grave;                      // Threshold at which to stop using grave
    public long limit;                      // The simulation limit
    public double randErr;                  // The odds of randomly picking a move instead of picking the best
    public double treeDiscount;             // The goal discount inside of the tree
    public double chargeDiscount;           // The goal discount in a depth charge
    public double epsilon;                  // The odds of using MAST rather than picking a random move
    public long chargeDepth;                // At which depth should a charge be stopped
    public long horizon;                    // How deep should our tree be allowed to be
    public long explorationFactor;          // UCT C value to control MCTS exploration
    public ArrayList<Double> defensiveness; // Used to modulate goal weight of keeping pieces alive
    public ArrayList<Double> aggression;    // Used to modulate goal weight of killing pieces
    public ArrayList<Double> chargeDefaults;// The default values used if a charge is stopped
    public boolean changed;                 // A control value used to see if a value has changed

    public MCTSControlValues(){
        explorationFactor = 40;
        rave = 100;
        grave = 20;
        randErr = 0;
        epsilon = 0.9f;
        treeDiscount = 0.999f;
        chargeDiscount = 0.995f;
        limit = 0;
        defensiveness = new ArrayList<>();
        defensiveness.add(0.50);
        defensiveness.add(0.50);
        aggression = new ArrayList<>();
        aggression.add(0.50);
        aggression.add(0.50);
        chargeDefaults = new ArrayList<>();
        chargeDefaults.add(40.00);
        chargeDefaults.add(40.00);
        horizon = Integer.MAX_VALUE;
        chargeDepth = Integer.MAX_VALUE;
    }

    public MCTSControlValues(ArrayList<Double> values){
        setAll(values);
    }

    public void setRave(double rave){
        this.rave = Math.round(rave);
        changed = true;
    }

    public void setGrave(double grave){
        this.grave = Math.round(grave);
        changed = true;
    }

    public void setEpsilon(double epsilon){
        this.epsilon = epsilon;
    }

    public void setTreeDiscount(double treeDiscount){
        this.treeDiscount = treeDiscount;
    }

    public void setChargeDiscount(double chargeDiscount){
        this.chargeDiscount = chargeDiscount;
    }

    public void setLimit(double limit){
        this.limit = Math.round(limit);
    }

    public void setAggression(List<Double> aggression){
        this.aggression = new ArrayList<>(aggression);
    }

    public void setDefensiveness(List<Double> defensiveness){
        this.defensiveness = new ArrayList<>(defensiveness);
    }

    public void setChargeDefaults(List<Double> chargeDefaults){
        this.chargeDefaults = new ArrayList<>(chargeDefaults);
    }

    public void setChargeDepth(double depth){
        this.chargeDepth = Math.round(depth);
    }

    public void setExplorationFactor(double explorationFactor){
        this.explorationFactor = Math.round(explorationFactor);
        changed = true;
    }

    public void setHorizon(double horizon){
        this.horizon = Math.round(horizon);
    }

    public void setRandErr(double randErr){
        this.randErr = randErr;
    }

    public void setAll(ArrayList<Double> values){
        setEpsilon(values.get(0));
        setRave(values.get(1));
        setGrave(values.get(2));
        setChargeDiscount(values.get(3));
        setTreeDiscount(values.get(4));
        setLimit(values.get(5));
        if (values.size() > 6){
            setAggression(values.subList(6,8));
            setDefensiveness(values.subList(8,10));
            setChargeDepth(values.get(10));
            setHorizon(values.get(11));
            setRandErr(values.get(12));
            setChargeDefaults(values.subList(13, 15));
        }
    }
}
