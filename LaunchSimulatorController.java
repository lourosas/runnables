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
//FOR THE TIME BEING, A LIVING FILE!
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class LaunchSimulatorController implements ActionListener,
KeyListener,ItemListener,WindowListener{
   private LaunchSimulator    _simulator;
   private Subscriber         _subscriber;
   private ClockSubscriber    _clockSubscriber;
   
   {
      _simulator       = null;
      _subscriber      = null;
      _clockSubscriber = null;
   };

   /////////////////////////Contstructors/////////////////////////////
   /**/
   public LaunchSimulatorController(){}
   /**/
   public LaunchSimulatorController(Subscriber sub){
      this.addSubscriber(sub);
   }
   /**/
   public LaunchSimulatorController
   (
      Subscriber      sub,
      ClockSubscriber csub
   ){
      this.addSubscriber(sub);
      this.addClockSubscriber(csub);
   }
   /**/
   public LaunchSimulatorController
   (
      LaunchSimulator sim,
      Subscriber      sub,
      ClockSubscriber csub
   ){
      this.addClockSubscriber(csub);
      this.addSubscriber(sub);
      this.addLaunchSimulator(sim);
   }

   ////////////////////////////Public Methods/////////////////////////
   /**/
   public void addClockSubscriber(ClockSubscriber cs){
      this._clockSubscriber = cs;
   }
   /**/
   public void addLaunchSimulator(LaunchSimulator ls){
      this._simulator = ls;
   }
   /**/
   public void addSubscriber(Subscriber s){
      this._subscriber = s;
   }
   ///////////////////////Interface Implementations///////////////////
   /////////////////////////////Action Listener///////////////////////
   /**/
   public void actionPerformed(ActionEvent e){
      try{
         JButton b = (JButton)e.getSource();
         if(b.getActionCommand().toUpperCase().equals("PRELAUNCH")){
            //This is currently just TEST
            //I will need a better solution than this!  But this is
            //good as an example!!!
            //Create something much more Simple...but similar
            //Just need a frame with three text fields and some
            //buttons!
            /*
            CountdownTimerController ctc =
                                       new CountdownTimerController();
            CountdownTimerView ctv = new CountdownTimerView("",ctc);
            this._countdownTimer = new CountdownTimer(new LClock());
            ctc.addModel(this._countdownTimer);
            ctc.addView(ctv);
            this._countdownTimer.addSubscriber(this._clockSubscriber);
            */
            this.prelaunchTime();
         }
      }
      catch(ClassCastException ice){}
   }
   ///////////////////////////////Key Listener////////////////////////
   /**/
   public void keyPressed(KeyEvent ke){}
   /**/
   public void keyReleased(KeyEvent ke){}
   /**/
   public void keyTyped(KeyEvent ke){}
   /////////////////////////////Item Listener/////////////////////////
   /**/
   public void itemStateChanged(ItemEvent ie){}
   ///////////////////////////Window Listener/////////////////////////
   /**/
   public void windowActivated(WindowEvent we){}
   /**/
   public void windowClosed(WindowEvent we){}
   /**/
   public void windowClosing(WindowEvent we){}
   /**/
   public void windowDeactivated(WindowEvent we){}
   /**/
   public void windowDeiconified(WindowEvent we){}
   /**/
   public void windowIconified(WindowEvent we){}
   /**/
   public void windowOpened(WindowEvent we){}

   ////////////////////////////Private Methods////////////////////////
   /**/
   private void prelaunchTime(){
      try{
         this._subscriber.update(null,"Set:  Prelaunch");
      }
      catch(NullPointerException npe){
         JOptionPane.showMessageDialog(null,
               "NO GUI to set Prelaunch!",
               "GUI-less!",
               JOptionPane.ERROR_MESSAGE);
      }
   }
}
//////////////////////////////////////////////////////////////////////
