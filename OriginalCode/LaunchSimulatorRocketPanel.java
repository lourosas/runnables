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

public class LaunchSimulatorRocketPanel extends JPanel{
   //Going to "do" anonymous inner classes
   private JFrame       _parent;
   private StagesJFrame _stagesF;
   private RocketData   _currentRD;

   {
      _currentRD = null;
      _parent    = null;
      _stagesF   = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchSimulatorRocketPanel(){
      this(null);
   }

   //
   //
   //
   public LaunchSimulatorRocketPanel(JFrame frame){
      super();
      this.setUpGUI();
      this._parent = frame;
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void initialize(RocketData rd){
      this._currentRD = rd;
      this.deactivateButtonPanel();
      this.activateButtonPanel("INITIALIZE");
      this.initializeCenterPanel();
      this.updateStageJFrame();
   }

   //Blank everything out...basically, remove everything...
   //
   //
   public void resetCompletely(){}
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
               if(b.getText().toUpperCase().equals("STAGES")){
                  //Enable the Stages JButton
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
               if(b.getText().toUpperCase().equals("STAGES")){
                  b.setEnabled(true);
               }
               else if(b.getText().toUpperCase().equals("ERROR")){
                  b.setEnabled(true);
               }
            }
            catch(ClassCastException cce){}
         }
      }
      else if(action.toUpperCase().equals("STAGES PRESSED")){
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("STAGES")){
                  b.setEnabled(false);
               }
            }
            catch(ClassCastException cce){}
         }
      }
      else if(action.toUpperCase().equals("STAGES ACTIVATE")){
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("STAGES")){
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
            //If this is triggered "we" are in trouble!!
            cce.printStackTrace();
         }
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
         //Get the Model Panel
         JPanel rdp = (JPanel)panel.getComponent(0);
         JLabel l=new JLabel("Rocket Model: ",SwingConstants.RIGHT);
         rdp.add(l);
         l = new JLabel("" + this._currentRD.model());
         rdp.add(l);
         rdp.repaint();
         rdp.revalidate();
         //Caluclated Weight Panel
         rdp = (JPanel)panel.getComponent(1);
         l   = new JLabel("Current Weight: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = format.format(this._currentRD.calculatedWeight())+"N";
         l = new JLabel(n);
         rdp.add(l);
         //Current Stage Panel
         rdp = (JPanel)panel.getComponent(2);
         l = new JLabel("Current Stage: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = "" + this._currentRD.currentStage();
         l = new JLabel(n);
         rdp.add(l);
         //Total Stages Panel
         rdp = (JPanel)panel.getComponent(3);
         l = new JLabel("Total Stages: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = "" + this._currentRD.numberOfStages();
         l = new JLabel(n);
         rdp.add(l);
         //Error Panel
         rdp = (JPanel)panel.getComponent(4);
         if(this._currentRD.isError()){
            l = new JLabel("Error: ",SwingConstants.RIGHT);
            l.setForeground(Color.RED);
            rdp.add(l);
            n = "" + this._currentRD.isError();
            l = new JLabel(n);
            l.setForeground(Color.RED);
            rdp.add(l);
            this.activateButtonPanel("ERROR"); //TBD
         }
         else{
            l = new JLabel("");
            rdp.add(l);
            rdp.add(l);
         }
      }
      catch(ClassCastException cce){ cce.printStackTrace(); }
      catch(Exception e){ e.printStackTrace(); }
   }
   
   //
   //
   //
   private void requestStageJFrameDisplay(){
      if(this._stagesF != null){
         this._stagesF.requestDisplay();
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      //Add more Buttons as needed
      JButton stages = new JButton("Stages");
      JButton error  = new JButton("Error");
      stages.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Prints for the time being
            System.out.println(e);
            setUpStageJFrame();
            updateStageJFrame();
            requestStageJFrameDisplay();
            activateButtonPanel("Stages Pressed");
         }
      });
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Prints for the time being
            //TODO-complete this (as with all of them)
            System.out.println(e);
         }
      });
      panel.add(stages);
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpDataPanel(){
      JPanel panel = new JPanel();
      //panel.setBorder(BorderFactory.createEtchedBorder());
      //panel.setLayout(new GridLayout(0,2));
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel()); //Model Panel
      panel.add(this.setUpPanel()); //Weight Panel
      panel.add(this.setUpPanel()); //Current Stage panel
      panel.add(this.setUpPanel()); //Total Stages panel
      panel.add(this.setUpPanel()); //Error Panel
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      //The Layout may need to change...to a BorderLayout...
      this.setLayout(new BorderLayout());
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

   //
   //
   //
   private void setUpStageJFrame(){
      if(this._stagesF == null){
         if(this._parent != null){
            this._stagesF = new StagesJFrame(this._parent);
         }
         else{
            this._stagesF = new StagesJFrame();
         }
         this._stagesF.addWindowListener(new WindowAdapter(){
            //GenericJInteractionFrame already tankes care of visible
            public void windowClosing(WindowEvent w){
               activateButtonPanel("Stages Activate");
            }
         });
      }
      //this._stagesF.requestDisplay();
   }

   //This is the data associated with the Rocket...not associated
   //With the Stage...that needs to be addressed when the Actor
   //Presses the "Stage" Button...
   //NEEDS TO BE REDONE!!!!
   private void updateCenterPanel(RocketData rd){
      try{
         NumberFormat format = NumberFormat.getInstance(Locale.US);
         JPanel rdp = (JPanel)this.getComponent(0);
         String n = new String();
         JLabel l=new JLabel("Rocket Model: ",SwingConstants.RIGHT);
         rdp.add(l);
         l = new JLabel(rd.model());
         rdp.add(l);

         l = new JLabel("Current Weight: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = format.format(rd.calculatedWeight())+"N";
         l = new JLabel(n);
         rdp.add(l);

         l = new JLabel("Current Stage: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = "" + rd.currentStage();
         l = new JLabel(n);
         rdp.add(l);

         l = new JLabel("Total Stages: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = "" + rd.numberOfStages();
         l = new JLabel(n);
         rdp.add(l);
         
         l = new JLabel("Error: ",SwingConstants.RIGHT);
         if(rd.isError()){ l.setForeground(Color.RED); }
         else{ l.setForeground(Color.BLUE); }
         rdp.add(l);
         n = "" + rd.isError();
         l = new JLabel(n);
         if(rd.isError()){ l.setForeground(Color.RED); }
         else{ l.setForeground(Color.BLUE); }
         rdp.add(l);
         
         rdp.repaint();
         rdp.revalidate();
      }
      catch(ClassCastException cce){cce.printStackTrace(); }
      catch(Exception e){ e.printStackTrace(); }
   }

   //
   //
   //
   private void updateStageJFrame(){
      if(this._stagesF != null){
         this._stagesF.update(this._currentRD);
      }
   }
}
//////////////////////////////////////////////////////////////////////
