//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import java.awt.event.*;
import rosas.lou.runnables.*;

public class TestSudoku2{
   public static void main(String [] args){
      new TestSudoku2();
   }

   public TestSudoku2(){
      System.out.println("Hello World");

      SudokuController05 controller = new SudokuController05();

      ActionListener a      = (ActionListener)controller;
      KeyListener    k      = (KeyListener)controller;
      SudokuView05   view   = new SudokuView05("Sudoku2",a,k);
      SudokuInterface model = new Sudoku2();
      controller.addFrame(view);
      controller.addModel(model);
      controller.addSubscriber(view);
   }
}
//////////////////////////////////////////////////////////////////////
