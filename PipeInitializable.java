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

public class PipeInitializable implements Initializable{
   private int      _stage;
   private int      _tankNumber;
   private int      _number; //current number of the pipe connected
   private PipeData _pipeData; 

   {
      _stage       = -1;
      _tankNumber  = -1;
      _number      = -1;
      _pipeData    = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //Stage, tank number, pipe number
   //
   //
   public PipeInitializable(int stage, int tank, int num){}

   ///////////////Initializable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{}

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._pipeData;
   }
}
//////////////////////////////////////////////////////////////////////
