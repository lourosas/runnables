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

public abstract class SudokuGroup{
   protected static final int TOTAL = 9;

   protected SudokuBlock blocks[];
   protected int         indices[];
   
   //////////////////////////Constructor//////////////////////////////
   //
   //
   //
   public SudokuGroup(){
      this.blocks  = new SudokuBlock[TOTAL];
      this.indices = new int[TOTAL];
   }

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void values(Block[] block){}

   //
}
//////////////////////////////////////////////////////////////////////