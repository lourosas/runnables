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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import rosas.lou.runnables.*;
import myclasses.*;
import rosas.lou.lgraphics.*;

public class CoffeeMakerViewV1_2 extends GenericJFrame
implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;

   private CoffeeMakerControllerV1_2  _controller;
   private ButtonGroup                _powerGroup;

   {
      _controller = null;
      _powerGroup = null;
   };

   ///////////////////////Constructors////////////////////////////////
   //
   //
   //
   public CoffeeMakerViewV1_2(CoffeeMakerControllerV1_2 controller){
      this("",controller);
   }

   //
   //
   //
   public CoffeeMakerViewV1_2
   (
      String                    title,
      CoffeeMakerControllerV1_2 controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(1,2,10,10));
      //set up the carafe panel
      //set up the reservoir panel
      return panel;
   }
   
   
   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel northPanel  = this.setUpNorthPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(northPanel,  BorderLayout.NORTH);
      this.getContentPane().add(centerPanel, BorderLayout.CENTER);
      this.getContentPane().add(southPanel,  BorderLayout.SOUTH);
      this.setVisible(true);
   }

   //
   //
   //
   private JPanel setUpNorthPanel(){
      JPanel panel     = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      this._powerGroup = new ButtonGroup();

      JRadioButton power = new JRadioButton("Power", true);
      power.setActionCommand("POWER");
      this._powerGroup.add(power);
      power.addItemListener(this._controller);
      panel.add(power);

      JRadioButton off = new JRadioButton("Power Off");
      off.setActionCommand("OFF");
      this._powerGroup.add(off);
      off.addItemListener(this._controller);
      panel.add(off);

      JLabel ready = new JLabel("Ready");
      ready.setForeground(Color.blue);
      panel.add(ready);

      JLabel brewing = new JLabel("Brewing");
      brewing.setForeground(Color.red);
      brewing.setEnabled(false);
      panel.add(brewing);

      return panel;
   }

   //
   //
   //
   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());

      JButton brew = new JButton("Brew");
      brew.setActionCommand("BREW");
      brew.setMnemonic(KeyEvent.VK_B);
      //brew.setEnabled(false); //May not be needed
      brew.addActionListener(this._controller);
      brew.addKeyListener(this._controller);
      panel.add(brew);

      JButton carafe = new JButton("Get Carafe");
      carafe.setActionCommand("GET");
      carafe.setMnemonic(KeyEvent.VK_G);
      //carafe.setEnabled(false);
      carafe.addActionListener(this._controller);
      carafe.addKeyListener(this._controller);
      panel.add(carafe);

      JButton returnCarafe = new JButton("Return Carafe");
      returnCarafe.setActionCommand("RETURN");
      returnCarafe.setMnemonic(KeyEvent.VK_R);
      returnCarafe.setEnabled(false);
      returnCarafe.addActionListener(this._controller);
      returnCarafe.addKeyListener(this._controller);
      panel.add(returnCarafe);

      JButton fill = new JButton("Fill Reservoir");
      fill.setActionCommand("RESERVOIR FILL");
      fill.setMnemonic(KeyEvent.VK_F);
      //fill.setEnabled(false);
      fill.addActionListener(this._controller);
      fill.addKeyListener(this._controller);
      panel.add(fill);
      return panel;
   }

   ////////////////////Interface Methods//////////////////////////////
   //
   //
   //
   public void update(Object o){
      try{
         TotalState ts = (TotalState)o;
         System.out.println(ts);
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void update(Object o, String s){
   }

   //
   //
   //
   public void error(RuntimeException re){}

   //
   //
   //
   public void error(RuntimeException re, Object o){}

   //
   //
   //
   public void error(String error){
   }
}
//////////////////////////////////////////////////////////////////////
