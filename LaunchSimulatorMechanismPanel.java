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

public class LaunchSimulatorMechanismPanel extends JPanel{
   //Going to use annonymous inner classes
   private LaunchingMechanismData  _currentLMD;//May not need
   private JFrame                  _parent;
   private MechanismSupportsJFrame _mechanismsF;

   {
      _currentLMD  = null;
      _parent      = null;
      _mechanismsF = null;
   };
   
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchSimulatorMechanismPanel(){
      this(null);
   }

   //
   //
   //
   public LaunchSimulatorMechanismPanel(JFrame frame){
      super();
      this.setUpGUI();
      this._parent = frame;
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void initialize(LaunchingMechanismData lmd){
      this._currentLMD = lmd;
      this.deactivateButtonPanel();
      this.initiateButtonPanel();
      this.initiateCenterPanel();
      this.updateMechanismSupportsJFrame();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(String action){
      JPanel bp = (JPanel)this.getComponent(1);
      if(action.toUpperCase().equals("INITIALIZE")){
         this.deactivateButtonPanel();
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("HOLDS")){
                  //Enable the Holds JButton
                  b.setEnabled(true);
               }
            }
            catch(ClassCastException cce){}
         }
      }
      else if(action.toUpperCase().equals("ERROR")){
         this.deactivateButtonPanel();
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("HOLDS")){
                  b.setEnabled(true);
               }
               else if(b.getText().toUpperCase().equals("ERROR")){
                  b.setEnabled(true);
               }
            }
            catch(ClassCastException cce){}
         }
      }
      else if(action.toUpperCase().equals("HOLDS PRESSED")){
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("HOLDS")){
                  b.setEnabled(false);
               }
            }
            catch(ClassCastException cce){}
         }
      }
      else if(action.toUpperCase().equals("HOLDS ACTIVATE")){
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("HOLDS")){
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
            //If this is "triggered", "we" are in trouble!
            cce.printStackTrace();
         }
      }
   }

   //
   //
   //
   private void displayMechanismsSupportsJFrame(){
      if(this._mechanismsF == null){
         MechanismSupportsJFrame f = null;
         if(this._parent != null){
            f = new MechanismSupportsJFrame(this._parent);
         }
         else{
            f = new MechanismSupportsJFrame();
         }
         this._mechanismsF = f;
         this._mechanismsF.addWindowListener(new WindowAdapter(){
            //GenericJInteractionFrame already takes care of visible
            public void windowClosing(WindowEvent w){
               activateButtonPanel("Holds Activate");
            }
         });
      }
      else{
         this._mechanismsF.setVisible(true);
      }
   }

   //
   //
   //
   private void initiateButtonPanel(){
      if(this._currentLMD.isError()){
         //Activate the button panel for error
         this.activateButtonPanel("Error");
      }
      else{
         this.activateButtonPanel("Initialize");
      }
   }

   //
   //
   //
   private void initiateCenterPanel(){
      try{
         String n = new String();
         NumberFormat format=NumberFormat.getNumberInstance(Locale.US);
         JPanel panel = (JPanel)this.getComponent(0);

         JPanel data  = (JPanel)panel.getComponent(0);
         JLabel l=new JLabel("Platform Model: ",SwingConstants.RIGHT);
         data.add(l);
         l = new JLabel("" + this._currentLMD.model());
         data.add(l);

         data = (JPanel)panel.getComponent(1);
         l = new JLabel("No. of Holds: ",SwingConstants.RIGHT);
         data.add(l);
         l = new JLabel("" + this._currentLMD.holds());
         data.add(l);

         data = (JPanel)panel.getComponent(2);
         l = new JLabel("Measured Weight: ",SwingConstants.RIGHT);
         data.add(l);
         n = format.format(this._currentLMD.measuredWeight())+"N";
         l = new JLabel(n);
         data.add(l);

         data = (JPanel)panel.getComponent(3);
         if(this._currentLMD.isError()){
            l = new JLabel("Error: ",SwingConstants.RIGHT);
            l.setForeground(Color.RED);
            data.add(l);
            n = "" + this._currentLMD.isError();
            l = new JLabel(n);
            l.setForeground(Color.RED);
            data.add(l);
            this.activateButtonPanel("ERROR");
         }
         else{
            l = new JLabel("");
            data.add(l);
            data.add(l); //Add the Label twice
         }
      }
      catch(ClassCastException cce){ cce.printStackTrace(); }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();

      JButton holds = new JButton("Holds");
      JButton error = new JButton("Error");
      holds.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            displayMechanismsSupportsJFrame();
            updateMechanismSupportsJFrame();
            activateButtonPanel("Holds Pressed");
         }
      });
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //TODO--figure out how to handle errors
            System.out.println(e);
         }
      });
      panel.add(holds);
      panel.add(error);
      return panel;
   }

   //
   //
   //
   //private LaunchingMechanismDataPanel setUpDataPanel(){
   private JPanel setUpDataPanel(){
      JPanel panel = new JPanel();
      //panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpModelPanel());
      panel.add(this.setUpHoldsPanel());
      panel.add(this.setUpWeightPanel());
      panel.add(this.setUpErrorPanel());
      return panel;
      //return new LaunchingMechanismDataPanel();
   }

   //
   //
   //
   private JPanel setUpErrorPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,2));
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new BorderLayout());
      this.add(this.setUpDataPanel(),  BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),BorderLayout.SOUTH);
      this.deactivateButtonPanel();
   }

   //
   //
   //
   private JPanel setUpHoldsPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,2));
      return panel;
   }

   //
   //
   //
   private JPanel setUpModelPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,2));
      return panel;
   }

   //
   //
   //
   private JPanel setUpWeightPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,2));
      return panel;
   }

   //
   //
   //
   private void updateMechanismSupportsJFrame(){
      if(this._mechanismsF != null){
         this._mechanismsF.updateSupportsData(this._currentLMD);
      }
   }
}
//////////////////////////////////////////////////////////////////////
