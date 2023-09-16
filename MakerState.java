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
import rosas.lou.runnables.*;

//
//
//
//
public class MakerState{
   public static enum MASK{NONE              = 0x0,
                           CARAFECAPACITY    = 0x1,
                           CARAFEQUANTITY    = 0X2,
                           CARAFESTATE       = 0X4,
                           MAKERSTATE        = 0X8,
                           MAKERPOWERSTATE   = 0X10,
                           RESERVOIRCAPACITY = 0X20,
                           RESERVOIRQUANTITY = 0X40,
                           RESERVOIRSTATE    = 0X80,
                           ALL               = 0XFF};
   private double  _carafeCapacity;
   private double  _carafeQuantity;
   private String  _carafeState;
   private double  _reservoirCapacity;
   private double  _reservoirQuantity;
   private String  _reservoirState;
   private String  _coffeeMakerState;
   private String  _coffeeMakerPowerState;

   {
      _carafeCapacity        = -1.;
      _carafeQuantity        = -1.;
      _carafeState           = null;
      _reservoirCapacity     = -1.;
      _reservoirQuantity     = -1.;
      _reservoirState        = null;
      _coffeeMakerState      = null;
      _coffeeMakerPowerState = null;
   };
}
//////////////////////////////////////////////////////////////////////
