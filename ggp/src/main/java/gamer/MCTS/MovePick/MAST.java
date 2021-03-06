package gamer.MCTS.MovePick;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import org.ggp.base.util.statemachine.Move;

/**
 * Implements the Move action sampling technique for use in MCTS playouts.
 *
 * Abstracts the game down to Move, Value pairs for each role that can be used instead of
 * completely random picks in an MCTS playout phase
 */
public class MAST extends MovePick{
    private ArrayList<HashMap<Move, double[]>> mast;
    private Random rand;

    //public MAST(String gameName){{
    /**
     * @param gameName the name of the game being played, used for saving/loading
     */
    public MAST(String gameName){
        super(gameName);
        mast = new ArrayList<>();
        mast.add(new HashMap<>());
        mast.add(new HashMap<>());
        rand = new Random();

    }//}}

    //public List<Move> pickMove(List<List<Move>> list){{
    /**
     * Uses the saved MAST values to pick a joint move
     *
     * @param list The joint moves to choose from.
     */
    @Override
    public List<Move> pickMove(List<List<Move>> list){
        Move[] bestMoves = new Move[list.get(0).size()];
        double[] bestValue = new double[list.get(0).size()];
        for (List<Move> moves : list){
            for (int i = 0; i < moves.size(); ++i){
                double mastValue;
                Move m = moves.get(i);
                if (mast.get(i).containsKey(m)){
                    mastValue = mast.get(i).get(m)[0];
                    mastValue += rand.nextFloat() * 1;
                } else {
                    mastValue = rand.nextInt(30000);  //Arbitrary random value
                }
                if((bestMoves[i] == null) || (bestValue[i] > mastValue)){
                    bestMoves[i] = m;
                    bestValue[i] = mastValue;
                }
            }
        }
        return new ArrayList<>(Arrays.asList(bestMoves));
    }//}}

    //public void update(List<Move> moves, List<Double> newValue){{
    /**
     * Updates the mast values of the given moves with the new value
     *
     * @param moves The moves to be updated
     * @param newValue The value to update the moves with
     */
    @Override
    public void update(List<Move> moves, List<Double> newValue){
        for(int i = 0; i < newValue.size(); i++){
            Move move = moves.get(i);
            double[] newList;
            if(mast.get(i).containsKey(move)){
                newList = mast.get(i).get(move);
                int oldCount = (int)Math.round(newList[1]);
                int newCount = oldCount + 1;
                newList[0] = ((newList[0] * oldCount) + newValue.get(i))/newCount;
                newList[1] = (double)newCount;
            } else {
                newList = new double[2];
                newList[0] = newValue.get(i);
                newList[1] = 1.0d;
                mast.get(i).put(move, newList);
            }
        }
    }//}}

    //public int size(int which){{
    /**
     * @param which which table do you want.
     * @return The size of the requested mast table
     */
    @Override
    public int size(int which){
        return mast.get(which).size();
    }//}}

    //public void loadData(){{
    @SuppressWarnings("unchecked")
    @Override
    public void loadData(){
        File file = new File("data/mast/" + gameName);
        if(!file.isFile()){
            return;
        }
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mast = (ArrayList) ois.readObject();
            fis.close();
            ois.close();
        } catch (Exception e){
            System.out.println("EXCEPTION: " + e.toString());
            e.printStackTrace();
        }
    }//}}

    //public void saveData(){{
    @Override
    public void saveData(){
        File file = new File("data/mast/" + gameName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mast);
            fos.close();
            oos.close();
        } catch (Exception e){
            System.out.println("EXCEPTION: " + e.toString());
            e.printStackTrace();
        }
    }//}}
}
