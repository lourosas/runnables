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
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.*;

public class AnimalPair{
   private Amimal male;
   private Animal female;

   ////////////////////////Constructor////////////////////////////////
   //
   //
   //
   public AnimalPair(Animal one, Animal two){
      if(one.sex() == 0){
         this.female = two;
         this.male   = one;
      }
      else{
         this.female = one;
         this.male   = two
      }
   }

   /////////////////////Public Methods////////////////////////////////
   //
   //
   //
   public Animal female(){
      return this.female;
   }

   //
   //
   //
   public Animal male(){
      return this.male;
   }
}

//////////////////////////////////////////////////////////////////////
