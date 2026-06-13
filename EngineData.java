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

public abstract class EngineData{
   private int     _engine;  //Current Engine Number
   private String  _error;
   private double  _exhaustFlowRate;
   private boolean _isError;
   private boolean _isIgnited;
   private double  _fuelFlowRate;
   private long    _model;
   private int     _stage; //Stage the Engine is part
   private double  _temperature;
   private int     _total; //Total Number of engines:model&stage

   {
      _engine          = -1;
      _error           = null;
      _exhaustFlowRate = Double.NaN;
      _isError         = false;
      _isIgnited       = false;
      _fuelFlowRate    = Double.NaN;
   };
   public String  error();
   public double  exhaustFlowRate();
   public long    model();
   public int     index();
   public boolean isError();
   public boolean isIgnited();
   public double  fuelFlowRate();
   public int     stage();
   public double  temperature();
   public double  tolerance();
   public int     total();
   public String  toString();
}
//////////////////////////////////////////////////////////////////////
