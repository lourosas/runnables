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
import myclasses.*;
import javax.swing.border.*;
import rosas.lou.runnables.*;

public class MechanismSupportsPanel extends JPanel{
   /////////////////////////Construtors///////////////////////////////
   //
   //
   //
   public MechanismSupportsPanel(){
      super();
      this.setUpGUI();
   }

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void initialize(java.util.List<MechanismSupportData> list){
      int currentCount = 0;
      int idx          = 0;
      while(currentCount < list.size()){
         MechanismSupportData data = list.get(idx);
         idx++;
         if(data.id() == currentCount){
            this.addMechanismSupportDataPanel(data);
            ++currentCount;
            idx = 0;
         }
      }
   }

   //
   //
   //
   public void setUpData(java.util.List<MechanismSupportData> list){}

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void addMechanismSupportDataPanel
   (
      MechanismSupportData data
   ){
      MechanismSupportDataPanel p = new MechanismSupportDataPanel();
      p.setUpData(data);
      this.add(p);
   }
   
   //
   //
   //
   private void setUpGUI(){
      //THIS HAS GOT TO BE CHANGED!! Not all Fields SHOWING UP!!!!
      this.setLayout(new GridLayout(0,2));
      //this.setBorder(BorderFactory.createEtchedBorder());
   }
}
//////////////////////////////////////////////////////////////////////
