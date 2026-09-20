//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

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
import java.io.*;
import rosas.lou.runnables.*;

public class StageMonitorable implements Monitorable{
   private StageData   _stageData;

   {
      _stageData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public StageMonitorable(){}

   //////////////////////////Private Methods//////////////////////////

   ////////////////Monitorable Interface Implementation///////////////
   //
   //
   //
   public void add(Object data){}

   //
   //
   //
   public void addData(String type, Object data){}

   //
   //
   //
   public void addError(String error){}

   //
   //
   //
   public Object monitor(){
      return this._rocketData;
   }
}
//////////////////////////////////////////////////////////////////////
