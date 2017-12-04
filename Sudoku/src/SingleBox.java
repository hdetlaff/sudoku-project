import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a single box in the Sudoku grid.
 * The box contains its candidate numbers or its final number, and it knows which of those states it is in.
 *
 * Created by ashley and hannah on 12/2/17.
 */
public class SingleBox {
    private List<Integer> candidates;
    private boolean isFinal;
    private int number;

    public SingleBox(int num){
        number = num;
        candidates = new LinkedList<>();
        for(int i=1; i<=9; i++){ //this for loop is constant time effectively because it only loops 9 times always
            candidates.add(i);
        }
        if(num!=0)
            isFinal = true;
        else
            isFinal = false;
    }

    public void setCandidates(List<Integer> nums){
        candidates = nums;
    }
    public void removeCandidates(List<Integer> nums){
        for(Integer i: nums){
            candidates.remove(i); //this might be problematic if it removes at the index instead of removing the candidate object
        }
    }
    public List<Integer> getCandidates(){
        return candidates;
    }
    public boolean isFinal(){
        return isFinal;
    }
    public int getNumber(){
        return number;
    }

    @Override
    public String toString(){
        return number + "";
    }
}
