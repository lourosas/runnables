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

public class GenericTankData extends TankData{
   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericTankData
   (
      double   capacity,
      double   density,
      double   dryWeight,
      double   emptyRate,
      String   error,
      String   fuel,
      boolean  isError,
      double   massLossRate,
      long     model,
      int      number,
      int      stage,
      double   temperature,
      double   tolerance,
      double   weight
   ){
      this.capacity(capacity);
      this.density(density);
      this.dryWeight(dryWeight);
      this.emptyRate(emptyRate);
      this.error(error);
      this.fuel(fuel);
      this.isError(isError);
      this.massLossRate(massLossRate);
      this.model(model);
      this.number(number);
      this.stage(stage);
      this.temperature(temperature);
      this.tolerance(tolerance);
      this.weight(weight);
   }
}
//////////////////////////////////////////////////////////////////////
