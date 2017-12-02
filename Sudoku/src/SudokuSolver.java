/**
 * Created by ashley on 12/1/17.
 */
public class SudokuSolver {

    public static void main(String[] args){
        Sudoku trial = new Sudoku("MacintoshHD/Users/ashley/IdeaProjects/Algorithms/Sudoku/rec/sudokuTest.txt");
        int[][] array = trial.convertToArray("MacintoshHD/Users/ashley/IdeaProjects/Algorithms/Sudoku/rec/sudokuTest.txt");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(array[i][j]+", ");
            }
        }
    }
}
