//////////////////////////////////////////////////////////////////////
package rosas.lou.runnables;

import java.lang.*;
import java.text.ParseException;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.time.temporal.*;
import java.time.Instant;
import java.time.Duration;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import myclasses.*;
import rosas.lou.runnables.*;

public class TestThreadGUI extends GenericJFrame{ 
   private final static short  WIDTH  = 750;
   private final static short  HEIGHT = 750;

   private TestThreadGUIController controller = null;


   public TestThreadGUI(TestThreadGUIController control){
      super("Test Thread GUI");
      System.out.println("GUI 1: "+Thread.currentThread().getName());
      this.controller = control;
      this.setUpGUI();
      System.out.println("GUI 2: "+Thread.currentThread().getName());
   }

   private JMenuBar addJMenuBar(){
      JMenuBar menuBar = new JMenuBar();

      JMenu file = new JMenu("File");
      file.setMnemonic(KeyEvent.VK_F);

      JMenuItem save = new JMenuItem("Save");
      save.setActionCommand("MenuItemSave");
      save.addActionListener(this.controller);
      file.add(save);

      file.addSeparator();

      JMenuItem quit = new JMenuItem("Quit");
      quit.setActionCommand("MenuItemQuit");
      quit.addActionListener(this.controller);
      file.add(quit);

      menuBar.add(file);

      return menuBar;
   }

   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JLabel label = new JLabel("This is a Test Panel");
      panel.add(label);
      return panel;
   }

   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      this.getContentPane().add(southPanel, BorderLayout.SOUTH);
      this.setJMenuBar(this.addJMenuBar());
      this.setVisible(true);
   }

   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();

      JButton start = new JButton("Start");
      start.setActionCommand("Start");
      start.setMnemonic(KeyEvent.VK_S);
      start.addActionListener(this.controller);
      start.addKeyListener(this.controller);
      panel.add(start);

      JButton stop = new JButton("Stop");
      stop.setActionCommand("Stop");
      stop.setMnemonic(KeyEvent.VK_T);
      stop.addActionListener(this.controller);
      stop.addKeyListener(this.controller);
      stop.setEnabled(false);
      panel.add(stop);

      JButton lap = new JButton("Lap");
      lap.setActionCommand("Lap");
      lap.setMnemonic(KeyEvent.VK_L);
      lap.addActionListener(this.controller);
      lap.addKeyListener(this.controller);
      lap.setEnabled(false);
      panel.add(lap);

      JButton reset = new JButton("Reset");
      reset.setActionCommand("Reset");
      reset.setMnemonic(KeyEvent.VK_R);
      reset.addActionListener(this.controller);
      reset.addKeyListener(this.controller);
      panel.add(reset);

      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
