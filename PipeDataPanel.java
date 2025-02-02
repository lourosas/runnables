//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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

public class PipeDataPanel extends JPanel{
   //Use Anonymous Inner Classes
   
   private int _stage;
   private int _tank;
   private int _pipe;

   {
      _stage = 0;
      _pipe  = 0;
      _pipe  = 0; //PipeData.number()
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PipeDataPanel(int stage, int tank, int number){
      super();
      if(stage > 0 && tank > 0 && number > 0){
         this._stage = stage;
         this._tank  = tank;
         this._pipe  = number;
         this.setUpGUI();
      }
   }

   //////////////////////////Public Methods///////////////////////////
   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new BorderLayout());
   }
}
//////////////////////////////////////////////////////////////////////
