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
import myclasses.*;
import rosas.lou.clock.*;

public class LaunchingMechanismJFrame extends GenericJFrame{
   ///////////////////////////Constructor/////////////////////////////
   public LaunchingMechanismJFrame(){
      this.setUpGUI();
   }

   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public void initialize(LaunchingMechanismData lmd){
      this.setUpLchngMechanismData(lmd);
      this.setUpMechSupportsData(lmd);
      this.setVisual();
   }

   //
   //
   //
   public void setData(LaunchingMechanismData lmd){}


   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void setUpLchngMechanismData(LaunchingMechanismData lmd){
      LaunchingMechanismDataPanel p = null;
      p = new LaunchingMechanismDataPanel();
      p.setUpData(lmd);
      this.add(p);
   }

   //
   //
   //
   private void setUpMechSupportsData(LaunchingMechanismData lmd){
      MechanismSupportDataPanel mp = null;
      java.util.List<MechanismSupportData> list = lmd.supportData();
      int currentCount = 0;
      int idx          = 0;
      while(currentCount < list.size()){
         MechanismSupportData data = list.get(idx);
         ++idx;
         if(data.id() == currentCount){
            mp = new MechanismSupportDataPanel();
            mp.setUpData(data);
            this.add(mp);
            ++currentCount;
            idx = 0;
         }
      }
   }

   //
   //
   //
   private void setUpGUI(){
      //int WIDTH  = 425;
      //int HEIGHT = 700;

      this.setLayout(new GridLayout(0,1));
      this.setResizable(false);
   }

   //
   //
   //
   private void setVisual(){
      int WIDTH  = 425;
      int HEIGHT = 140*this.getContentPane().getComponentCount();
      this.setVisible(false);
      this.setSize(WIDTH,HEIGHT);
      this.setVisible(true);
   }
}
//////////////////////////////////////////////////////////////////////
