//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rosas.lou.runnables.*;

/**/
public class CoffeeMakerController
implements ActionListener, KeyListener, ItemListener, WindowListener{
   private Subscriber _subscriber;

   {
      _subscriber = null;
   };

   ///////////////////////////Constructors////////////////////////////
   /**/
   public CoffeeMakerController(){}

   /**/
   public CoffeeMakerController(Subscriber sub){
      this.addSubscriber(sub);
   }


   ///////////////////////////Public Methods//////////////////////////
   /**/
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
      Maker.instance().addSubscriber(subscriber);
      Carafe.instance().addSubscriber(subscriber);
   }

   /////////////////////////Protected Methods/////////////////////////
   ///////////////////////////Private Methods/////////////////////////
   /**/
   private void handleButtonCommand(String command){
      if(command.toUpperCase().equals("RESERVOIR FILL")){
         this.reservoirFill();
      }
      else if(command.toUpperCase().equals("BREW")){
         Maker.instance().brew();
      }
      else if(command.toUpperCase().equals("GET")){
         try{
            Carafe.instance().pull();
         }
         //Do not do anything, at the moment...
         catch(NotHomeException nhe){}
      }
      else if(command.toUpperCase().equals("RETURN")){
         //this._maker.returnCarafe();
         Carafe.instance().putback();
      }
      else if(command.toUpperCase().equals("POUR")){
         Mug mug = this.setUpMug();
         Carafe.instance().pour(mug);
      }
      else if(command.toUpperCase().equals("STOPPOURING")){
         Carafe.instance().stopPour();
      }
   }

   /**/
   private void reservoirFill(){
      Double amount  = null;
      String s       = null;
      boolean toLoop = true;
      do{
         try{
            s = JOptionPane.showInputDialog(
                   (JFrame)this._subscriber,
                   "Fill Amount",
                   "Filling the Reservoir for Brewing",
                   JOptionPane.QUESTION_MESSAGE);
            Maker.instance().fillReservoir(Double.parseDouble(s));
            toLoop = false;
         }
         catch(HeadlessException he){
            he.printStackTrace();
         }
         catch(NullPointerException npe){
            toLoop = false;
         }
         catch(NumberFormatException nfe){
            //alert the user to input a number
            //Show an error dialog
            if(s.length() > 0){
               JOptionPane.showMessageDialog(
                  (JFrame)this._subscriber,
                  "Please Enter a Number",
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
            }
            else{
               toLoop = false;
            }
         }
      }while(toLoop);
   }

   /**/
   private Mug setUpMug(){
      Mug mug        = null;
      String s       = null;
      boolean toLoop = true;
      do{
         try{
            s = JOptionPane.showInputDialog(
                   (JFrame)this._subscriber,
                   "Mug Size",
                   "Enter The Mug Size",
                   JOptionPane.QUESTION_MESSAGE);
            double amount = Double.parseDouble(s);
            mug = new Mug((int)amount,this);
            toLoop = false;
         }
         catch(HeadlessException he){
            he.printStackTrace();
         }
         catch(NullPointerException npe){
            toLoop = false;
         }
         catch(NumberFormatException nfe){
            //alert the User to input a number
            //show the error dialog
            if(s.length() > 0){
               JOptionPane.showMessageDialog(
                  (JFrame)this._subscriber,
                  "Please Enter a Number",
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
            } 
            else{
               toLoop = false;
            }
         }
      }while(toLoop);
      return mug;
   }

   /////////////////////Interface Implementation//////////////////////
   //////////////////////////Action Listener//////////////////////////
   /**/
   public void actionPerformed(ActionEvent e){
      try{
         JButton b = (JButton)e.getSource();
         this.handleButtonCommand(b.getActionCommand());
      }
      catch(ClassCastException cce){}
   }

   ///////////////////////////Key Listener////////////////////////////
   /**/
   public void keyPressed(KeyEvent ke){
      if(ke.getKeyCode() == KeyEvent.VK_ENTER){
         try{
            JButton b = (JButton)ke.getSource();
            b.doClick(130);
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   public void keyReleased(KeyEvent ke){}
   /**/
   public void keyTyped(KeyEvent ke){}

   //////////////////////////Item Listener////////////////////////////
   /**/
   public void itemStateChanged(ItemEvent ie){
      try{
         JRadioButton jb = (JRadioButton)ie.getSource();
         if(ie.getStateChange() == ItemEvent.SELECTED){
            String command = jb.getActionCommand().toUpperCase();
            if(command.equals("OFF")){
               Maker.instance().power(false);
            }
            else if(command.equals("POWER")){
               Maker.instance().power(true);
            }
         }
      }
      catch(ClassCastException cce){}
   }

   ////////////////////////Window Listener////////////////////////////
   /**/
   public void windowActivated(WindowEvent e){}

   /**/
   public void windowClosed(WindowEvent e){}

   /**/
   public void windowClosing(WindowEvent e){
      Carafe.instance().stopPour();
   }

   /**/
   public void windowDeactivated(WindowEvent e){}

   /**/
   public void windowDeiconified(WindowEvent e){}

   /**/
   public void windowIconified(WindowEvent e){}

   /**/
   public void windowOpened(WindowEvent e){}
}
//////////////////////////////////////////////////////////////////////
