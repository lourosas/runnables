//////////////////////////////////////////////////////////////////////
/*
Copyright 2023 Lou Rosas

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rosas.lou.runnables.*;

public class CoffeeMakerControllerV1_3 implements ActionListener,
KeyListener, ItemListener, WindowListener{
   private Subscriber _subscriber;

   {
      _subscriber = null;
   };

   ///////////////////////Constructors////////////////////////////////
   //
   //
   //
   public CoffeeMakerControllerV1_3(){
      //MakerV1_3.instance().power(false);
   }

   //
   //
   //
   public CoffeeMakerControllerV1_3(Subscriber sub){
      this._subscriber = sub;
      MakerV1_3.instance().addSubscriber(sub);
      //MakerV1_3.instance().power(false);
   }
   
   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
      MakerV1_3.instance().addSubscriber(this._subscriber);
      CarafeV1_3.instance().addSubscriber(this._subscriber);
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private Mug setUpMug(){
      Mug     mug    = null;
      String  s      = null;
      boolean toLoop = true;
      do{
         try{
            s = JOptionPane.showInputDialog(
                   (JFrame)this._subscriber,
                   "Mug Size",
                   "Enter the Mug Size",
                   JOptionPane.QUESTION_MESSAGE);
            double amount = Double.parseDouble(s);
            mug           = new Mug((int)amount, this);
            toLoop        = false;
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

   //
   //
   //
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
            MakerV1_3.instance().fillReservoir(Double.parseDouble(s));
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

   //////////////////Interface Implementation/////////////////////////
   ///////////////////////Action Listener/////////////////////////////
   //
   //
   //
   public void actionPerformed(ActionEvent e){
      try{
         JButton button = (JButton)e.getSource();
         String command = button.getActionCommand().toUpperCase();
         if(command.equals("BREW")){
            MakerV1_3.instance().brew();
         }
         else if(command.equals("RESERVOIR FILL")){
            //System.out.println(command);
            this.reservoirFill();
         }
         else if(command.equals("GET")){}
         else if(command.equals("RETURN")){}
         else if(command.equals("POUR")){
            Mug mug = this.setUpMug();
         }
         else{ System.out.println(command); }
      }
      catch(ClassCastException cce){}
   }

   //////////////////////////Key Listener/////////////////////////////
   //
   //
   //
   public void keyPressed(KeyEvent ke){
      if(ke.getKeyCode() == KeyEvent.VK_ENTER){
         try{
            JButton b = (JButton)ke.getSource();
            b.doClick(130);
         }
         catch(ClassCastException cce){}
      }
   }

   //
   //
   //
   public void keyReleased(KeyEvent ke){}

   //
   //
   //
   public void keyTyped(KeyEvent ke){}

   ////////////////////////////Item Listener//////////////////////////
   //
   //
   //
   public void itemStateChanged(ItemEvent ie){
      try{
         JRadioButton jrb = (JRadioButton)ie.getSource();
         //if(ie.getStateChange() == ItemEvent.SELECTED){
         //   System.out.println(radioButton.getActionCommand());
         //}
         if(jrb.isSelected()){
            String command = jrb.getActionCommand().toUpperCase();
            if(command.equals("POWER")){
               MakerV1_3.instance().power(true);
            }
            else if(command.equals("OFF")){
               MakerV1_3.instance().power(false);
            }
         }
      }
      catch(ClassCastException cce){}
   }

   /////////////////////////Window Listener///////////////////////////
   //
   //
   //
   public void windowActivated(WindowEvent e){}

   //
   //
   //
   public void windowClosed(WindowEvent e){}

   //
   //
   //
   public void windowClosing(WindowEvent e){
      //TBD--something like below...
      //CarafeV1_3.instance().stopPour();
   }

   //
   //
   //
   public void windowDeactivated(WindowEvent e){}

   //
   //
   //
   public void windowDeiconified(WindowEvent e){}

   //
   //
   //
   public void windowIconified(WindowEvent e){}

   //
   //
   //
   public void windowOpened(WindowEvent e){}
}

//////////////////////////////////////////////////////////////////////
