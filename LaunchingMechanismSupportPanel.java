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

public class LaunchingMechanismSupportPanel extends JPanel{
   private LaunchSimulatorController _controller = null;

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public LaunchingMechanismSupportPanel(LaunchSimulatorController c){
      super();
      this._controller = c;
      this.setUpGUI();
   }

   /////////////////////////Private Methdods//////////////////////////
   //
   //
   //
   private void setUpGUI(){
      int CENTER = SwingConstants.CENTER;
      JLabel platformLabel = new JLabel("Platform", CENTER);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new GridLayout(0,1));
      this.add(platformLabel);
      this.add(setUpMechanismData());
      this.add(setUpMechanismSupportData());
   }
}
//////////////////////////////////////////////////////////////////////
