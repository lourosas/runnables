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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rosas.lou.runnables.*;

public class CoffeeMakerControllerV1_2 implements ActionListener,
KeyListener, ItemListener, WindowListener{
   private Subscriber _subscriber;

   {
      _subscriber = null;
   };

   ///////////////////////Constructors////////////////////////////////
   //
   //
   //
   public CoffeeMakerControllerV1_2(){
      //MakerV1_2.instance().power(false);
   }

   //
   //
   //
   public CoffeeMakerControllerV1_2(Subscriber sub){
      this._subscriber = sub;
      MakerV1_2.instance().addSubscriber(sub);
      //MakerV1_2.instance().power(false);
   }
   
   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
      MakerV1_2.instance().addSubscriber(this._subscriber);
   }

   //////////////////Interface Implementation/////////////////////////
   ///////////////////////Action Listener/////////////////////////////
   //
   //
   //
   public void actionPerformed(ActionEvent e){}

   //////////////////////////Key Listener/////////////////////////////
   //
   //
   //
   public void keyPressed(KeyEvent ke){}

   //
   //
   //
   public void keyReleased(KeyEvent ke){}

   //
   //
   //
   public void keyTyped(KeyEvent ke){}

   ////////////////////////////Item Listener//////////////////////////
   //
   //
   //
   public void itemStateChanged(ItemEvent ie){}

   /////////////////////////Window Listener///////////////////////////
   //
   //
   //
   public void windowActivated(WindowEvent e){}

   //
   //
   //
   public void windowClosed(WindowEvent e){}

   //
   //
   //
   public void windowClosing(WindowEvent e){
      //Alert the Model to stop pouring the Carafe if such a thing is
      //occuring
   }

   //
   //
   //
   public void windowDeactivated(WindowEvent e){}

   //
   //
   //
   public void windowDeiconified(WindowEvent e){}

   //
   //
   //
   public void windowIconified(WindowEvent e){}

   //
   //
   //
   public void windowOpened(WindowEvent e){}
}

//////////////////////////////////////////////////////////////////////
