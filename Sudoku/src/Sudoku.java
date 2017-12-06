import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ashley and hannah on 12/1/17.
 */
public class Sudoku {

    //TODO: Test setCandidates and helper methods
    //TODO: Fix convertToArray number/byte conversion problem
    //TODO: Make method updateCandidates that deletes the new final number from every affected row, col and box
    //TODO: implement solve method with rules
    //TODO: Add comments and javadocs
    //TODO: Add graphics if time

    private SingleBox[][] grid;
    private String file;
    public static final int SIZE = 9;
    public static final int BOX_SIZE = 3;

    public Sudoku(String filename){
        file = filename;
        grid = convertToArray(filename);
    }

    public SingleBox[][] convertToArray(String filename){
        byte cur;
        int input;
        SingleBox box;
        SingleBox[][] array = new SingleBox[SIZE][SIZE];
        int countX = 0;
        int countY = 0;
        try{
            Reader reader = new BufferedReader(new FileReader(filename));
            while(countX<9) {
                cur = (byte)reader.read();
                System.out.println("Cur before convert" + cur);
                input = cur & 0xFF;
                System.out.println("Cur after convert" + cur);
                box = new SingleBox(input);
                //System.out.println("cur= "+ cur);
                System.out.println("CountX= "+ countX);
                System.out.println("CountY= "+ countY);
                array[countX][countY] = box;
                if (countY == SIZE - 1) {
                    countY = 0;
                    countX++;
                } else {
                    countY++;
                }

            }
        }
        catch(FileNotFoundException ex){
            System.out.println("This sudoku file does not exist. You done messed up ;) ");
        }
        catch(IOException ex){
            System.out.println("Error reading file.");
        }
        return array;
    }

    private void setCandidates(){
        for(int i=0; i<SIZE; i++){
            setRoworCol(i, true);
            setRoworCol(i, false);
        }
        for(int i=0; i<SIZE; i+=3){
            for(int j=0; j<SIZE; j+=3){
                setBox(i, j);
            }
        }

    }

    private void setRoworCol(int loc, boolean isRow){
        List<Integer> finals = new LinkedList<>(); //saves slight space
        List<Integer> emptyIndices = new LinkedList<>();

        for(int i=0; i<SIZE; i++){
            SingleBox cur;
            if(isRow)
                cur = grid[loc][i];
            else
                cur = grid[i][loc];
            if(cur.isFinal()){
                finals.add(cur.getNumber());
            }
            else {
                emptyIndices.add(i);
            }
        }
        for(int index: emptyIndices){
            if(isRow)
                grid[loc][index].removeCandidates(finals);
            else
                grid[index][loc].removeCandidates(finals);
        }
    }

    //row, col are the indices of the upper left hand corner of the box
    private void setBox(int row, int col){
        List<Integer> finals = new LinkedList<>(); //saves slight space
        List<int[]> emptyIndices = new LinkedList<>();

        for(int i=0; i<BOX_SIZE; i++){
            for(int j=0; j<BOX_SIZE; j++){
                SingleBox cur = grid[i+row][j+col];
                if(cur.isFinal()){
                    finals.add(cur.getNumber());
                }
                else{
                    int[] index = {i+row, j+col};
                    emptyIndices.add(index);
                }
            }
        }
        for(int[] index: emptyIndices){
            grid[index[0]][index[1]].removeCandidates(finals);
        }
    }

    /** when a new final number is created this method crosses off that number from the candidate lists
     * of SingleBoxes in the same row column and box
     * @param num
     * @param row
     * @param col
     */
    private void updatecandidates(Integer num, int row, int col){
        for(int i=0; i<SIZE; i++){
            List<Integer>candidateList1=grid[row][i].getCandidates();
            List<Integer>candidateList2=grid[i][col].getCandidates();

            if (!grid[row][i].isFinal() && candidateList1.contains(num)){
                candidateList1.remove(num);
            }
            if(!grid[i][col].isFinal() && candidateList2.contains(num)){
                candidateList2.remove(num);
            }
        }
        //box
        int startRow= row/BOX_SIZE;
        int startCol= col/BOX_SIZE;
        for(int i=startRow; i<startRow+BOX_SIZE; i++){
            for(int j=startCol; j<startCol+BOX_SIZE; j++){
                List<Integer>candidateList3=grid[i][j].getCandidates();
                if(!grid[i][j].isFinal() && candidateList3.contains(num)){
                    candidateList3.remove(num);
                }
            }
        }
    }
    private void nakedSingle(int row, int col){
        SingleBox curr = grid[row][col];
        if(!curr.isFinal() && curr.getCandidates().size()==1){
            curr.setNumber(curr.getCandidates().get(0));
            curr.setFinal(true);
        }
    }

    //possible that this should work more similarly to the naked single method
    private void hiddenSingleRow(int row){
        List<Integer> posHiddenSingles= new LinkedList<>();
        for(int i=0; i<SIZE; i++){
            if(!grid[row][i].isFinal()){
                for(Integer candidate: grid[row][i].getCandidates()){
                    if(posHiddenSingles.contains(candidate)){
                        posHiddenSingles.remove(candidate);
                    }else{
                        posHiddenSingles.add(candidate);
                    }

                }
            }
        }
        if(posHiddenSingles.size()==1){
            //TODO: finish this
            //there is one
            //find the one with the value stored in posHiddenSingles
            //set it's final to that value
            //updateCandidates
        }
    }
    //TODO: make these work for columns and boxes

    private void nakedPair(int row){
        for(int i=0; i<SIZE; i++){
            SingleBox curr = grid[row][i];
            if(!curr.isFinal() && curr.getCandidates().size()==2){
                //check if there is another SingleBox with the same Candidate list
                for(int j=i+1; j<SIZE;j++){
                    SingleBox check = grid[row][j];
                    if(!check.isFinal() && check.getCandidates().equals(curr.getCandidates())){//found it
                        for(int k=0; k<SIZE; k++){//now remove other instances of these candidates
                            if(k!=i || k!=j){//ensures we don't remove from the nakedPair
                                grid[row][k].removeCandidates(curr.getCandidates());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * finds a hiddenPair if there is one and removes all other candidates from the pair's lists
     * @param row
     */
    private void hiddenPairRow(int row){

    }
    //this is unfinished I simply started thinking about how this loop might look
    public void solve(){
        setCandidates();
        boolean done = false;
        while(!done){
            for(int i=0; i<SIZE; i++){
                for(int j=0; j<SIZE; j++){
                    nakedSingle(i,j);
                }
            }
        }
    }



}
