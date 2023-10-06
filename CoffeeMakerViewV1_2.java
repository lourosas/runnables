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

public class CoffeeMakerViewV1_2 extends GenericJFrame,
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
   }

   ////////////////////Interface Methods//////////////////////////////
   //
   //
   //
   public void update(Object o){
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
