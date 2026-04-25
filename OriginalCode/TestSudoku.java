//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestSudoku{
   public static void main(String[] args){
      new TestSudoku();
   }

   public TestSudoku(){
      System.out.println("Hello World");
      SudokuController controller = new SudokuController();
      SudokuView view = new SudokuView("Sudoku", controller);
      SudokuInterface model = new Sudoku2();
      controller.addSubscriber(view);
      controller.addModel(model);
      //model.addSubscriber(view);

   }
}
//////////////////////////////////////////////////////////////////////
