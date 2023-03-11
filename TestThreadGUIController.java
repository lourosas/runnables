//////////////////////////////////////////////////////////////////////
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import myclasses.*;
import rosas.lou.runnables.*;

public class TestThreadGUIController implements ActionListener,
KeyListener,ItemListener{
   private TestThreadModel model_  = null;

   public TestThreadGUIController(TestThreadModel model){
      this.model_ = model;
   }

   ///////////////////////Interface Implementation////////////////////
   public void actionPerformed(ActionEvent ae){
      System.out.print("Action Event, ");
      System.out.println("Ctrl: "+Thread.currentThread().getName());
      try{
         JButton button = (JButton)ae.getSource();
         if(button.getActionCommand().toUpperCase().equals("START")){
            this.model_.start();
         }
      }
      catch(ClassCastException cce){}
   }
   public void keyPressed(KeyEvent ke){
      System.out.print("Key Event, ");
      System.out.println("Ctrl: "+Thread.currentThread().getName());
   }
   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke){}
   public void itemStateChanged(ItemEvent ie){}
}
//////////////////////////////////////////////////////////////////////
