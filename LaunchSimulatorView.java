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
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.border.*;
import java.time.*;
import myclasses.*;
import rosas.lou.clock.*;

public class LaunchSimulatorView extends GenericJFrame
implements Subscriber, ClockSubscriber, CountdownTimerInterface{
   private LaunchSimulatorStateSubstate.State             PREL = null;
   private LaunchSimulatorStateSubstate.State             INIT = null;
   private LaunchSimulatorStateSubstate.State             LAUN = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate SET  = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate CONT = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate HOLD = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  IGN  = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  BUP  = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  REL  = null;
   private LaunchSimulatorStateSubstate.LaunchSubstate    ASC  = null;
   private LaunchSimulatorStateSubstate.LaunchSubstate    STAG = null;
   private LaunchSimulatorStateSubstate.LaunchSubstate    IGNE = null;

   private LaunchSimulatorController     _controller;
   private LaunchSimulatorStateSubstate  _lsss;
   private LaunchingMechanismJFrame      _launchMechFrame;
   private ButtonGroup                   _menuButtonGroup;

   {
      PREL = LaunchSimulatorStateSubstate.State.PRELAUNCH;
      INIT = LaunchSimulatorStateSubstate.State.INITIATELAUNCH;
      LAUN = LaunchSimulatorStateSubstate.State.LAUNCH;
      SET  = LaunchSimulatorStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE;
      HOLD = LaunchSimulatorStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchSimulatorStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchSimulatorStateSubstate.IgnitionSubstate.BUILDUP;
      REL  = LaunchSimulatorStateSubstate.IgnitionSubstate.RELEASED;
      ASC  = LaunchSimulatorStateSubstate.LaunchSubstate.ASCENT;
      STAG = LaunchSimulatorStateSubstate.LaunchSubstate.STAGING;
      IGNE =LaunchSimulatorStateSubstate.LaunchSubstate.IGNITEENGINES;

      _controller      = null;
      _lsss            = null;
      _launchMechFrame = null;
      _menuButtonGroup = null;
   };

   //////////////////////////Constructors/////////////////////////////
   /**/
   public LaunchSimulatorView(){
      this("",null);
   }

   /**/
   public LaunchSimulatorView
   (
      String title,
      LaunchSimulatorController controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }

   //////////////////////////Public Methods///////////////////////////
   ////////////////////////Interface Methods//////////////////////////
   /////////////////////ClockSubscriber Methods///////////////////////
   /**/
   public void error(String type, String message){}
   /**/
   public void update(String time){
      //This will fucking need to change BIGTIME!!!
      try{
         LaunchSimulatorStateSubstate.State state=this._lsss.state();
         if(state==LaunchSimulatorStateSubstate.State.PRELAUNCH){
            //Start with the Countdown Panel first, amoung other
            //things that will need to happen
            LaunchSimulatorCountdownPanel p=this.getCountdownPanel();
            p.setCurrentCountdownTime(time);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }
   /**/
   public void update(State state){}
   /**/
   public void update(java.time.Instant instant){}
   /**/
   public void update(Duration duration){}
   /**/
   public void update(Duration duration, boolean isRunning){}
   /**/
   public void update(Duration duration, State state){}
   /**/
   public void update(Duration duration, State state, String type){}
   /**/
   public void update(boolean isRunning){}
   /**/
   public void update(String time, String type){}
   /**/
   public void update(java.util.List<?> list){
      for(int i = 0; i < list.size(); ++i){
         String s = ((String)list.get(i)).toUpperCase();
         if(s.contains(":")){
            //This way we know it is a time value
            this.update(s);
         }
         else if(s.contains("RESET")){}
         else if(s.contains("RUN")){
            System.out.println(s);
         }
         else if(s.contains("STOP")){
            System.out.println(s);
         }
      }
   }
   /**/
   public void updateElapsed(Duration duration){}
   /**/
   public void updateLap(Duration duration){}
   /**/
   public void updateLaps(java.util.List<?> list){}
   /**/
   public void updateRun(){}
   /**/
   public void updateStop(){}
   /**/
   public void updateReset(){}
   //////////////////CountdownTimerInterface Methods//////////////////
   /**/
   public java.util.List<Integer> requestTimes(){
      java.util.List<Integer> list = new LinkedList<Integer>();
      //Put this in reverse order for exception hadling the "most
      //logical" gets filled first
      list.add(this.requestSecs());
      list.add(this.requestMins());
      list.add(this.requestHours());
      return list;
   }

   /**/
   public Integer requestHours(){
      Integer hours = null;
      LaunchSimulatorCountdownPanel panel = this.getCountdownPanel();
      hours = panel.getHours();
      return hours;
   }

   /**/
   public Integer requestMins(){
      Integer mins = 0;
      LaunchSimulatorCountdownPanel panel = this.getCountdownPanel();
      mins = panel.getMins();
      return mins;
   }

   /**/
   public Integer requestSecs(){
      Integer secs = 0;
      LaunchSimulatorCountdownPanel panel = this.getCountdownPanel();
      secs = panel.getSecs();
      return secs;
   }
   ////////////////////////Subscriber Methods/////////////////////////
   /**/
   public void update(Object o){
      try{
         RocketData rd = (RocketData)o;
         this.handleRocketData(null, rd);
      }
      catch(ClassCastException cce){}
      try{
         LaunchingMechanismData lm = (LaunchingMechanismData)o;
         this.handleLaunchingMechanismData(null,lm);
      }
      catch(ClassCastException cce){}
   }

   /**/
   public void update(Object o, String s){
      if(o != null){
         try{
            JTextField jtf = (JTextField)o;
            this.handleJTextFieldEntry(jtf,s);
         }
         catch(ClassCastException cce){}
         try{
            LaunchSimulatorStateSubstate lss = null;
            lss = (LaunchSimulatorStateSubstate)o;
            this.handleStateSubstate(lss, s);
         }
         catch(ClassCastException cce){}
         try{
            LaunchingMechanismData lm = (LaunchingMechanismData)o;
            this.handleLaunchingMechanismData(s.toUpperCase(),lm);
         }
         catch(ClassCastException cce){}
         try{
            RocketData rd = (RocketData)o;
            this.handleRocketData(s.toUpperCase(),rd);
         }
         catch(ClassCastException cce){}      
      }
      else{
         this.getMessage(s);
      }
   }

   /**/
   public void error(RuntimeException re){
      this.handleRuntimeException(re);
   }
   /**/
   public void error(RuntimeException re, Object o){}
   /**/
   public void error(String error){
      //Fucking something else to go in here!
   }

   /////////////////////////Private Methods///////////////////////////
   /**/
   private void abortPrelaunch(){
      this.setPrelaunchTime();
      //Set the front label to indicate "Bounced Out" of States
      //re-set up the North Panel--probably should set up its
      //own method
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      JLabel label = (JLabel)panel.getComponent(0);
      label.setText("(Current State)");
      
   }

   /**/
   private JMenuBar addJMenuBar(){
      this._menuButtonGroup = new ButtonGroup();
      JMenuBar menuBar      = new JMenuBar();
      menuBar.add(this.setUpFileMenu());
      menuBar.add(this.setUpHelpMenu());

      return menuBar;
   }

   /**/
   private void displayLaunchingMechanismError(String error){
      //Test Prints for the time being...put together a (one time)
      //Error Dialog box...
      System.out.println(error);
   }

   /**/
   private void displayMechanismSupportError(String error){
      //Test Prints for the time being...
      System.out.println(error);
   }

   /**/
   private void displayState(LaunchSimulatorStateSubstate.State s){
      JPanel panel=(JPanel)this.getContentPane().getComponent(0);
      JLabel label=(JLabel)panel.getComponent(0);
      label.setText("" + s);
   }

   /**/
   private LaunchSimulatorCountdownPanel getCountdownPanel(){
      JPanel panel=(JPanel)this.getContentPane().getComponent(1);
      JPanel nwPanel=(JPanel)panel.getComponent(0);
      LaunchSimulatorCountdownPanel p = null;
      p = (LaunchSimulatorCountdownPanel)nwPanel.getComponent(0);
      return p;
   }
   
   /**/
   MechanismSupportsPanel getMechanismSupportsPanel(){
      JPanel panel   = (JPanel)this.getContentPane().getComponent(1);
      JPanel swPanel = (JPanel)panel.getComponent(2);
      MechanismSupportsPanel p = null;
      p = (MechanismSupportsPanel)swPanel.getComponent(0);
      return p;
   }

   /*
    *It is going to be some sort of message, so go ahead and get it
    first
    * */
   private void getMessage(String m){
      String message = m.toUpperCase();
      if(message.contains("SET")){
         if(message.contains("PRELAUNCH")){
            this.setPrelaunchTime();
         }
      }
      else if(message.contains("ABORT")){
         if(message.contains("PRELAUNCH")){
            this.abortPrelaunch();
         }
      }
   }

   /**/
   private void handleJTextFieldEntry(JTextField jtf, String s){
      String test = s.toUpperCase();
      if(test.equals("SETHOURS") ||
         test.equals("SETMINS")  ||
         test.equals("SETSECS")){
         LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
         p.requestNextFocus(jtf, s);
      }
   }

   /**/
   private void handleIgnitionSubstate
   (
      LaunchSimulatorStateSubstate.IgnitionSubstate sub
   ){
   }

   /**/
   private void handleLaunchingMechanismData
   (
      String                 state,
      LaunchingMechanismData lmd
   ){
      //LaunchingMechanismDataPanel p = null;
      //p = this.getLaunchingMechanismPanel();
      //MechanismSupportsPanel mp     = null;
      //mp = this.getMechanismSupportsPanel();
      java.util.List<MechanismSupportData> list = lmd.supportData();
      //Will also need to add the Mechanism Support Data,
      //and, set up another series of panels!!!
      if(state != null){
         //p.setUpData(state, lmd);
         if(state.toUpperCase().contains("INITIALIZE")){
            this._launchMechFrame = new LaunchingMechanismJFrame();
            //mp.initialize(list);
            this._launchMechFrame.initialize(lmd);
         }
      }
      else{
         //p.setUpData(lmd);
         //Will need to add the MechanismSupportsPanel here, as well
         //mp.setUpData(list);
         this._launchMechFrame.setData(lmd);
      }
      //this._launchMechFrame.add(p);
      //this._launchMechFrame.add(mp);
      //this._launchMechFrame.setVisible(true);
      if(lmd.isError()){
         this.displayLaunchingMechanismError(lmd.error());
      }
   }

   /**/
   private void handleLaunchSubstate
   (
      LaunchSimulatorStateSubstate.LaunchSubstate sub
   ){
   }

   /**/
   private void handlePrelaunchSubstate
   (
      LaunchSimulatorStateSubstate.PreLaunchSubstate sub
   ){
      try{
         if(sub == SET){
            this.setForCountdownStart();
         }
         else if(sub== CONT){
            //Set the Countdown Start
            this.setStartResumeCountdown();
         }
         else if(sub == HOLD){
            //Hold the Countdown
            this.setHoldCountdown();
         }
      }
      catch(NullPointerException npe){}
   }

   /**/
   private void handleRocketData(String state, RocketData rd){
      System.out.println(rd);
      if(state != null){}
      else{}
   }

   /**/
   private void handleRuntimeException(RuntimeException re){
      String message = re.getMessage().toUpperCase();
      if(message.contains("TIME ENTRY")){
         JOptionPane.showMessageDialog(this,
                                       re.getMessage(),
                                       "Time Entry Error",
                                       JOptionPane.ERROR_MESSAGE);
         (this.getCountdownPanel()).wrongHourEntry();
      }
      else if(message.contains("CANNOT FIND THE FILE")){
         String strings[] = re.getMessage().split(" ");
         String error = new String("The File:  "+strings[0]);
         error += "\nNot found!  Please enter another file.";
         JOptionPane.showMessageDialog(this,
                                       error,
                                       "File Not Found",
                                       JOptionPane.ERROR_MESSAGE);
      }
      else if(message.contains("NO SUCH FILE OR DIRECTORY")){
         String strings[] = re.getMessage().split(" ");
         String error = new String("The File:  "+strings[0]);
         error += "\nNot found!  Please enter another file.";
         JOptionPane.showMessageDialog(this,
                                       error,
                                       "File Not Found",
                                       JOptionPane.ERROR_MESSAGE);
      }
   }

   /**/
   private void handleStateSubstate
   (
      LaunchSimulatorStateSubstate lsss,
      String                       message
   ){
      this._lsss = lsss;
      this.displayState(lsss.state());
      this.handlePrelaunchSubstate(lsss.prelaunchSubstate());
      this.handleIgnitionSubstate(lsss.ignitionSubstate());
      this.handleLaunchSubstate(lsss.launchSubstate());
   }

   /**/
   private void setForCountdownStart(){
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      p.activateSetSubState();
   }

   /**/
   private void setHoldCountdown(){
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      p.activateHoldSubState();
   }

   /**/
   private void setPrelaunchTime(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel nwPanel=(JPanel)panel.getComponent(0);
      JPanel btnPanel=(JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < btnPanel.getComponentCount(); ++i){
         JButton b = (JButton)btnPanel.getComponent(i);
         b.setEnabled(false);
         if(b.getActionCommand().toUpperCase().contains("ABORT")){
            b.setEnabled(true);
         }
      }
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      p.activatePrelaunchTime();
   }

   /**/
   private void setStartResumeCountdown(){
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      p.activateContinueSubState();
   }

   /**/
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(2,2,1,1));
      panel.add(this.setUpNorthWestPanel());
      panel.add(new JPanel());
      panel.add(new JPanel());
      panel.add(new JPanel());
      return panel;
   }

   /**/
   private JPanel setUpCountdownPanel(){
      LaunchSimulatorController contr = this._controller;
      return new LaunchSimulatorCountdownPanel(contr);
   }

   /**/
   private JMenu setUpFileMenu(){
      int ctrl     = InputEvent.CTRL_DOWN_MASK;
      KeyStroke ks = null;
      JMenu file   = new JMenu("File");
      file.setMnemonic(KeyEvent.VK_F);

      JMenuItem open = new JMenuItem("Open", 'O');
      open.setActionCommand("OpenINIFile");
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_O, ctrl);
      open.setAccelerator(ks);
      this._menuButtonGroup.add(open);
      open.addActionListener(this._controller);
      file.add(open);

      file.addSeparator();
      return file;
   }

   /**/
   private void setUpGUI(){
      //int WIDTH  = 1080;
      int WIDTH  = 850;//Initial Values
      int HEIGHT = 700;//Initial Values
      //int HEIGHT = 1080;//Max Height

      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);

      JPanel northPanel  = this.setUpNorthPanel();
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(northPanel,   BorderLayout.NORTH);
      this.getContentPane().add(centerPanel,  BorderLayout.CENTER);
      this.getContentPane().add(southPanel,   BorderLayout.SOUTH);
      this.setJMenuBar(this.addJMenuBar());

      this.setVisible(true);
   }

   /**/
   private JMenu setUpHelpMenu(){
      KeyStroke ks = null;

      JMenu help = new JMenu("Help");
      help.setMnemonic(KeyEvent.VK_H);

      return help;
   }

   /**/
   private JPanel setUpNorthWestPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpCountdownPanel());
      panel.add(new JPanel());
      return panel;
   }

   /**/
   private JPanel setUpNorthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());

      JLabel stateLabel = new JLabel("(Current State)");
      panel.add(stateLabel);

      return panel;
   }

   /**/
   private JPanel setUpSouthPanel(){
      JPanel panel     = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      
      JButton preLaunch = new JButton("Pre-Launch");
      preLaunch.setActionCommand("PRELAUNCH");
      preLaunch.setMnemonic(KeyEvent.VK_P);
      preLaunch.addActionListener(this._controller);
      preLaunch.addKeyListener(this._controller);
      panel.add(preLaunch);

      JButton ignition = new JButton("Ignition");
      ignition.setActionCommand("IGNITION");
      ignition.setMnemonic(KeyEvent.VK_I);
      ignition.addActionListener(this._controller);
      ignition.addKeyListener(this._controller);
      ignition.setEnabled(false);
      panel.add(ignition);

      JButton launch = new JButton("Launch");
      launch.setActionCommand("LAUNCH");
      launch.setMnemonic(KeyEvent.VK_L);
      launch.addActionListener(this._controller);
      launch.addKeyListener(this._controller);
      launch.setEnabled(false);
      panel.add(launch);

      JButton abort = new JButton("Abort");
      abort.setActionCommand("ABORT");
      abort.setMnemonic(KeyEvent.VK_A);
      abort.addActionListener(this._controller);
      abort.addKeyListener(this._controller);
      abort.setEnabled(false);
      panel.add(abort);

      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
