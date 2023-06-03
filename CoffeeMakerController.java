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
implements ActionListener, KeyListener, ItemListener{
   private Subscriber _subscriber;
   private Maker      _maker;
   
   {
      _subscriber = null;
      _maker      = null;
   };

   ///////////////////////////Constructors////////////////////////////
   /**/
   public CoffeeMakerController(){}

   /**/
   public CoffeeMakerController(Subscriber sub){}

   /**/
   public CoffeeMakerController(Subscriber sub, Maker maker){
      this.addSubscriber(sub);
      this.addModel(maker);
   }

   ///////////////////////////Public Methods//////////////////////////
   /**/
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
   }

   /**/
   public void addModel(Maker maker){
      this._maker = maker;
   }

   /////////////////////////Protected Methods/////////////////////////
   ///////////////////////////Private Methods/////////////////////////
   /**/
   private void handleButtonCommand(String command){
      if(command.toUpperCase().equals("RESERVOIR FILL")){
         this.reservoirFill();
      }
      else if(command.toUpperCase().equals("BREW")){
         this._maker.brew();
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
                   "Fillint the Reservoir for Brewing",
                   JOptionPane.QUESTION_MESSAGE);
            this._maker.fillReservoir(Double.parseDouble(s));
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
   public void itemStateChanged(ItemEvent ie){}
}
//////////////////////////////////////////////////////////////////////
