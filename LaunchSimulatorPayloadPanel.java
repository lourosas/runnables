//////////////////////////////////////////////////////////////////////
/*
Copyright 2024 Lou Rosas

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
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.border.*;
import java.time.*;
import java.text.*;
import myclasses.*;
import rosas.lou.clock.*;

public class LaunchSimulatorPayloadPanel extends JPanel{
   //Again, continue to use Anonymous Inner Classes
   private JFrame      _parent;
   private PayloadData _currentPD;

   {
      _parent    = null;
      _currentPD = null;
   };
   
   //////////////////////Constructor//////////////////////////////////
   //
   //
   //
   public LaunchSimulatorPayloadPanel(JFrame frame){
      super();
      this.setUpGUI();
      //For location if the Capsule Frame is requested...
      this._parent = frame;
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void initialize(PayloadData pd){
      //Keep around for payload updates...
      this._currentPD = pd;
      //First, deactivate all buttons
      this.deactivateButtonPanel();
      this.initializeCenterPanel();
      this.initializeButtonPanel();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(String action){
      this.deactivateButtonPanel();
      JPanel bp = (JPanel)this.getComponent(1);
      if(action.toUpperCase().equals("ERROR")){
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("ERROR")){
                  //Activate the Error JButton
                  b.setEnabled(true);
               }
               else if(b.getText().toUpperCase().equals("STATUS")){
                  b.setEnabled(true);
               }
            }
            catch(ClassCastException cce){
               //If "we" get here, "we" are in trouble!...
            }
         }
      }
      else if(action.toUpperCase().equals("INITIALIZE")){
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("STATUS")){
                  b.setEnabled(true);
               }
            }
            catch(ClassCastException cce){}
         }
      }
   }

   //
   //
   //
   private void deactivateButtonPanel(){
      JPanel bp = (JPanel)this.getComponent(1);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         try{
            JButton b = (JButton)bp.getComponent(i);
            b.setEnabled(false);
         }
         catch(ClassCastException cce){
            //If this is caught, that is bad...
            cce.printStackTrace();
         }
      }
   }

   //
   //
   //
   private void initializeButtonPanel(){
      if(this._currentPD.isError()){
         //Activate the button panel for Error
         this.activateButtonPanel("Error");
      }
      else{
         this.activateButtonPanel("Initialize");
      }
   }

   //
   //
   //
   private void initializeCenterPanel(){
      try{
         String n            = new String();
         NumberFormat format = NumberFormat.getInstance(Locale.US);
         JPanel panel        = (JPanel)this.getComponent(0);

         //Payload Type
         JPanel pdp = (JPanel)panel.getComponent(0);
         JLabel l=new JLabel("Payload Type: ",SwingConstants.RIGHT);
         pdp.add(l);
         l = new JLabel(this._currentPD.type());
         pdp.add(l);
         //Model
         pdp = (JPanel)panel.getComponent(1);
         l = new JLabel("Model: ",SwingConstants.RIGHT);
         pdp.add(l);
         l = new JLabel(this._currentPD.model());
         pdp.add(l);
         //Crew
         pdp = (JPanel)panel.getComponent(2);
         l = new JLabel("Crew: ", SwingConstants.RIGHT);
         pdp.add(l);
         if(this._currentPD.crew() > 0){
            n = "" + this._currentPD.crew();
         }
         else{
            n = "N/A";
         }
         l = new JLabel(n);
         pdp.add(l);
         //Weight
         pdp = (JPanel)panel.getComponent(3);
         l = new JLabel("Current Weight: ",SwingConstants.RIGHT);
         pdp.add(l);
         n = format.format(this._currentPD.currentWeight())+"N";
         l = new JLabel(n);
         pdp.add(l);
         //Error
         pdp = (JPanel)panel.getComponent(4);
         if(this._currentPD.isError()){
            l = new JLabel("Error: ",SwingConstants.RIGHT);
            l.setForeground(Color.RED);
            pdp.add(l);
            n = "" + this._currentPD.isError();
            l = new JLabel(n);
            l.setForeground(Color.RED);
            pdp.add(l);
            this.activateButtonPanel("ERROR");
         }
         else{
            l = new JLabel("");
            pdp.add(l);
            pdp.add(l);
         }
      }
      catch(ClassCastException cce){ cce. printStackTrace(); }
      catch(Exception e){ e.printStackTrace(); }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel      = new JPanel();
      JButton status = new JButton("Status");
      status.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      JButton error     = new JButton("Error");
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      panel.add(status);
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpDataPanel(){
      JPanel panel = new JPanel();
      //panel.setLayout(new GridLayout(0,2));
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel()); //Payload Panel
      panel.add(this.setUpPanel()); //Model Panel
      panel.add(this.setUpPanel()); //Crew Panel
      panel.add(this.setUpPanel()); //Weight Panel
      panel.add(this.setUpPanel()); //Error Panel
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setBorder(BorderFactory.createEtchedBorder());
      this.add(this.setUpDataPanel(),  BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),BorderLayout.SOUTH);
      this.deactivateButtonPanel();
   }

   //
   //
   //
   private JPanel setUpPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,2));
      return panel;
   }
}

//////////////////////////////////////////////////////////////////////
