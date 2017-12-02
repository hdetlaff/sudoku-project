import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ashley and hannah on 12/1/17.
 */
public class Sudoku {

    //TODO: Test setCandidates and helper methods
    //TODO: Fix convertToArray file reading problem and type of return (initialize SingleBoxes)
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

    //this now needs to convert to a grid of single boxes objects
    public SingleBox[][] convertToArray(String filename){
        int cur;
        int[][] array = new int[SIZE][SIZE];
        int countX = 0;
        int countY = 0;
        try{
            Reader reader = new BufferedReader(new FileReader(filename));
            while((cur = reader.read()) != -1) {
                array[countX][countY] = cur;
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

    //TODO: add loops!
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


}
