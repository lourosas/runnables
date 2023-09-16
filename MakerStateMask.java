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

public class MakerStateMask{
   public static final int NONE           = 0x0;
   public static final int CARAFECAPACITY = 0x1;
   public static final int CARAFEQUANTITY = 0X2;
   public static final int CARAFESTATE    = 0X4;
   public static final int MAKERSTATE     = 0X8;
   public static final int MAKERPOWERSTATE = 0X10;
   public static final int RESERVOIRCAPACITY = 0X20;
   public static final int RESERVOIRQUANTITY = 0X40;
   public static final int RESERVOIRSTATE = 0X80;
   public static final int ALL = 0XFF;
}

//////////////////////////////////////////////////////////////////////
