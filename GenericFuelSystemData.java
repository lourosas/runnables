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
import rosas.lou.runnables.*;

public class GenericFuelSystemData implements FuelSystemData{
   private int            _numPipes;
   private int            _numPumps;
   private int            _numTanks;
   private List<PipeData> _pipes;
   private List<PumpData> _pumps;
   private List<TankData> _tanks;

   {
      _numPipes    = -1;
      _numPumps    = -1;
      _numTanks    = -1;
      _pipes       = null;
      _pumps       = null;
      _tanks       = null;
   };

   //////////////FuelSystemData Interface Implementation//////////////
   //
   //
   //
   public List<PipeData> pipeData(){ return this._pipes; }

   //
   //
   //
   public List<PumpData> pumpData(){ return this._pumps; }

   //
   //
   //
   public List<TankData> tankData(){ return this._tanks; }
}
//////////////////////////////////////////////////////////////////////
