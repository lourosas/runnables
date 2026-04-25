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

/**/
public class TickTockView extends GenericJFrame
implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;

   private ButtonGroup _buttonGroup;
   //Controller added later
   
   {
      _buttonGroup = null;
   };

   ////////////////////////Constructors///////////////////////////////
   //
   //More to be filled in once I know what I want for a Controller
   //
   public TickTockView(){
      this("");
   }

   //
   //
   //
   public TickTockView(String title){
      super(title);
      //set up the controller eventually
      this.setUpGui();
   }

   ///////////////////////Public Methods//////////////////////////////
   /////////////////////Protected Methods/////////////////////////////
   //////////////////////Private Methods//////////////////////////////
   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(1,2,10,10));
      panel.add(this.setUpLeftPanel());
      panel.add(this.setUpRightPanel());
      return panel;
   }

   //
   //
   //
   private void setUpGui(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel northPanel  = this.setUpNorthPanel();
      JPanel southPanel  = this.setUpSouthPanel();
   }

   //
   //
   //
   private JPanel setUpLeftPanel(){
      JPanel panel = new JPanel();

      return panel;
   }

   //
   //
   //
   private JPanel setUpRightPanel(){
      JPanel panel = new JPanel();

      return panel;
   }

   //////////////////////Interface Methods////////////////////////////
   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){}

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
   public void error(String error){}
}
//////////////////////////////////////////////////////////////////////
