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

public interface TankData{
   public double   capacity();
   public double   density();
   public double   dryWeight();
   public double   emptyRate();
   public String   error();
   public String   fuel(); //Fuel Type...
   public boolean  isError();
   public double   massLossRate(); //Derived...
   public long     model();
   public int      number();
   public int      stage();
   public double   temperature();
   public double   tolerance();
   public double   weight(); //Measured
   public String   toString();
}
//////////////////////////////////////////////////////////////////////
