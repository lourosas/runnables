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

public class Ark{
   List<AnimalPair> pairs = null;
   private int numPairs = 0;

   /////////////////////////Constructor///////////////////////////////
   //
   //
   //
   public class Ark(){
      this.pairs = new LinkedList<AnimalPair>();
   }

   /////////////////////Public Methods////////////////////////////////
   //
   //
   //
   public int load(Animal animal){}
   
   //
   //
   //
   public int load(Collection<Animal> candidates){
      SortedSet<Animal> animals = null;
      Animal          candidate = null;
      animals                   = new TreeSet<Animal>();
      animals.addAll(candidate);
      Iterator<Animal> it = animals.iterator();
      while(it.hasNext()){
         Animal a = it.next();
         if(candidate == null ||  !candidate.isPotentialMate(a)){
            candidate = a;
         }
         else{
            this.load(new AnimalPair(candidate, a));
            ++this.numPairs;
            candidate = null;
         }
      }
      return this.numPairs;
   }

   //
   //
   //
   public void load(AnimalPair pair){
      try{
         this.pairs.add(pair);
      }
      catch(NullPointerException npe){
         this.pairs = new LinkedList<AnimalPair>();
         this.pairs.add(pair);
      } 
   }

   //
   //
   //
   public void  unload(){}
}
//////////////////////////////////////////////////////////////////////
