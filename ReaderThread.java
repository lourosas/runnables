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
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.*;

public class ReaderThread implements Runnable{
   private boolean _ready    = false;
   private int     _number   =     0;

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public ReaderThread(){}

   /////////////////////////////Public Methods////////////////////////
   //
   //
   //
   public void ready(){
      this._ready = true;
      System.out.println("ready(): "+this._ready);
   }
   /////////////////////////Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      while(!this._ready){
         Thread.yield();
      }
      System.out.println("number = "+this._number);
   }
}
//////////////////////////////////////////////////////////////////////
