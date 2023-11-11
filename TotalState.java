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

public class TotalState{
   private ContainerState _carafeState;
   private MakerState     _makerState;
   private ContainerState _reservoirState;

   {
      _carafeState    = null;
      _makerState     = null;
      _reservoirState = null;
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public TotalState
   (
      MakerState makerState,
      ContainerState carafeState,
      ContainerState reservoirState
   ){
      this.makerState(makerState);
      this.carafeState(carafeState);
      this.reservoirState(reservoirState);
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public ContainerState carafeState(){
      return this._carafeState;
   }

   //
   //
   //
   public MakerState makerState(){
      return this._makerState;
   }

   //
   //
   //
   public ContainerState reservoirState(){
      return this._reservoirState;
   }

   //
   //
   //
   public String toString(){
      return new String(this._makerState+"\n"+this._carafeState+"\n"+
                        this._reservoirState);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void carafeState(ContainerState carafeState){
      this._carafeState = null;
      if(carafeState != null){
         this._carafeState = carafeState;
      }
   }

   //
   //
   //
   private void makerState(MakerState makerState){
      this._makerState = null;
      if(makerState != null){
         this._makerState = makerState;
      }
   }

   //
   //
   //
   private void reservoirState(ContainerState reservoirState){
      this._reservoirState = null;
      if(reservoirState != null){
         this._reservoirState = reservoirState;
      }
   }
}
//////////////////////////////////////////////////////////////////////
