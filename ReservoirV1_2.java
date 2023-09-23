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

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public class ReservoirV1_2 extends Reservoir{
   public static final double EMPTY = 0.25;

   private enum State{STARTUP,EMPTY,FILLED,WASFILLED};
   
   private final double CAPACITY = 32.;

   private double _emptyRate;
   private double _quantity;
   private State  _state;
}
//////////////////////////////////////////////////////////////////////
