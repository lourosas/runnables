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
   private Suscriber _subscriber;

   {
      _suscriber = null;
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
      MakerV1_2.instance().addSuscriber(sub);
      //MakerV1_2.instance().power(false);
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
   public voide keyTyped(KeyEvent ke){}


}

//////////////////////////////////////////////////////////////////////
