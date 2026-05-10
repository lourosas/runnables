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
import rosas.lou.runnables.*;

public class GenericPayloadData implements PayloadData{
   private int             _crew;
   private double          _currentWeight; //Derived (Measured)
   private double          _dryWeight;
   private double          _emptyMass;
   private String          _error;
   private boolean         _isError;
   private boolean         _isOccupied;
   private double          _loadedMass;
   private double          _maxWeight;
   private String          _model;
   private double          _o2Percent;    //Derived (Measured)
   private double          _temperature;  //Derived (Measured)
   private double          _tolerance;
   private String          _type;

   {
      _crew           = -1;
      _currentWeight  = Double.NaN;
      _dryWeight      = Double.NaN;
      _emptyMass      = Double.NaN;
      _error          = null;
      _isError        = false;
      _isOccupied     = false;
      _loadedMass     = Double.NaN;
      _maxWeight      = Double.NaN;
      _model          = null;
      _o2Percent      = Double.NaN;
      _temperature    = Double.NaN;
      _tolerance      = Double.NaN;
      _type           = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPayloadData
   (
      int     crew,
      double  currentWeight,
      double  dryWeight,
      double  emptyMass,
      String  error,
      boolean isError,
      boolean isOccupied,
      double  loadedMass,
      double  maxWeight,
      String  model,
      double  o2Percent,
      double  temperature,
      double  tolerance,
      String  type
   ){
      this.crew(crew);
      this.currentWeight(currentWeight);
      this.dryWeight(dryWeight);
      this.emptyMass(emptyMass);
      this.error(error);
      this.isError(isError);
      this.isOccupied(isOccupied);
      this.loadedMass(loadedMass);
      this.maxWeight(maxWeight);
      this.model(model);
      this.o2Percent(o2Percent);
      this.temperature(temperature);
      this.tolerance(tolerance);
      this.type(type);
   }

}
//////////////////////////////////////////////////////////////////////
