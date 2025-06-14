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
   //String for the current error
   private ErrorEvent              _errorEvent;

   {
      _currentLMD   = null;
      _parent       = null;
      _mechanismsF  = null;
      _errorEvent   = null;
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
   public void error(RuntimeException re, ErrorEvent e){
      this._errorEvent = e;
      String err = this._errorEvent.getEvent();
      if(err.toUpperCase().contains("MEASURED WEIGHT")){
         this.deactivateButtonPanel();
         this.errorButtonPanel(re,e);
         this.errorCenterPanel(re,e);
      }
      //Arm measurement errors
      else{
         this.errorMechanismSupportsJFrame(re,this._errorEvent);
      }
   }

   //
   //
   //
   public void update(LaunchingMechanismData lmd){
      //Clear any errors if there were any...
      this._errorEvent = null;
      this._currentLMD = lmd;
      this.deactivateButtonPanel();
      this.updateButtonPanel();
      this.updateCenterPanel();
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
   private void displayErrorData(){
      String error = this._errorEvent.getEvent();
      //Put the Time in the Title
      String state = null;
      String title = this._errorEvent.getTime();
      String[] errors = error.split("\n");
      error = new String(errors[0]);
      for(int i = 0; i < errors.length; ++i){
         if(errors[i].toUpperCase().contains("STATE")){
            String[] states = errors[i].split(":");
            state = states[1].trim();
         }
      }
      if(state.toUpperCase().contains("INITIALIZE")){
         error += "\nExpected: "+errors[3].split(":")[1].trim();
      }
      error += "\n"+errors[2];
      JOptionPane.showMessageDialog(this._parent,
                                    error,
                                    title,
                                    JOptionPane.ERROR_MESSAGE);
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
      this._mechanismsF.setVisible(true);
   }

   //
   //
   //
   private void errorButtonPanel(RuntimeException re, ErrorEvent e){
      this.activateButtonPanel("Error");
      if(this._mechanismsF != null){
         if(this._mechanismsF.isVisible()){
            this.activateButtonPanel("Holds Pressed");
         }
         else{
            this.activateButtonPanel("Holds Activate");
         }
      }
      else{
         this.activateButtonPanel("Holds Activate");
      }
   }

   //
   //
   //
   private void errorCenterPanel(RuntimeException re, ErrorEvent e){
      //Still NEED TO HANDLE EXCEPTIONS When An error occures in
      //FIRST UPDATE--Frame is not even set up...!!!
      String error = e.getEvent();
      String [] errors = error.split("\n");
      String weight = null;
      for(int i = 0; i < errors.length; ++i){
         if(errors[i].toUpperCase().contains("MEASURED:")){
            String [] weightErrors = errors[i].split(":");
            weight = weightErrors[1].trim();
         }
      }
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel data  = (JPanel)panel.getComponent(2);
      JLabel l     = (JLabel)data.getComponent(0);
      l.setForeground(Color.RED);
      l = (JLabel)data.getComponent(1);
      l.setForeground(Color.RED);
      l.setText(weight+"N");

      data = (JPanel)panel.getComponent(3);
      data.removeAll();
      l = new JLabel("Error: ", SwingConstants.RIGHT);
      l.setForeground(Color.RED);
      data.add(l);
      l = new JLabel(errors[0]);
      l.setForeground(Color.RED);
      data.add(l);
      //See if this works...
      panel.repaint();
      panel.revalidate();
      
   }

   //
   //
   //
   private void errorMechanismSupportsJFrame
   (
      RuntimeException re,
      ErrorEvent       e
   ){
      try{
         MechanismSupport support = (MechanismSupport)e.getSource();
         if(this._mechanismsF != null){
            this._mechanismsF.updateErrorData(re,e);
         }
      }
      catch(ClassCastException cce){}
      catch(NullPointerException npe){}
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
            displayErrorData();
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
   private void updateButtonPanel(){
      //This will need to change somewhat based on errors...since
      //errors are now relayed in a different mechanism--more real
      //time
      if(this._currentLMD.isError()){
         this.activateButtonPanel("Error");
      }
      else if(this._mechanismsF != null){
         //If the Launching Mechanisms Frame is active, do not
         //actviate the Holds button
         if(this._mechanismsF.isVisible()){
            //If the Launching Mechanisms Frame is visible, indicate
            //that
            this.activateButtonPanel("Holds Pressed");
         }
         else{
            //If the Launching Mechanism Frame is not visible,
            //indicate Holds button should be active
            this.activateButtonPanel("Holds Activate");
         }
      }
      else{
         //If Launching Mechanism Frame is null and there is no error,
         //activate the Holds button--a Typical Update
         this.activateButtonPanel("Holds Activate");
      }
   }

   //
   //
   //
   private void updateCenterPanel(){
      try{
         String n       = new String();
         NumberFormat f = NumberFormat.getNumberInstance(Locale.US);
         JPanel panel   = (JPanel)this.getComponent(0);

         JPanel data    = (JPanel)panel.getComponent(0);
         if(data.getComponentCount() > 0){
            //Get rid of everything and start over...
            data.removeAll();
         }
         JLabel l=new JLabel("Platform Model: ",SwingConstants.RIGHT);
         data.add(l);
         l = new JLabel(""+this._currentLMD.model());
         data.add(l);

         data = (JPanel)panel.getComponent(1);
         if(data.getComponentCount() > 0){
            data.removeAll();
         }
         l = new JLabel("No. of Holds: ",SwingConstants.RIGHT);
         data.add(l);
         l = new JLabel("" + this._currentLMD.holds());
         data.add(l);

         data = (JPanel)panel.getComponent(2);
         if(data.getComponentCount() > 0){
            data.removeAll();
         }
         l = new JLabel("Measured Weight: ", SwingConstants.RIGHT);
         data.add(l);
         n = f.format(this._currentLMD.measuredWeight())+"N";
         l = new JLabel(n);
         data.add(l);

         //This will need to change accoringly for errors as
         //appropriate
         data = (JPanel)panel.getComponent(3);
         if(data.getComponentCount() > 0){
            data.removeAll();
         }
         //This WILL NEED TO CHANGE, since errors updated differently
         if(this._currentLMD.isError()){
            l = new JLabel("Error: ", SwingConstants.RIGHT);
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
            data.add(l);
         }
         panel.repaint();
         panel.revalidate();
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
      }
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
