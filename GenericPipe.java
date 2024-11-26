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

public class GenericPipe implements Pipe{
   int _stage;
   int _number;

   {
      _stage  = -1;
      _number = -1;
   };
   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericPipe(int stage, int number){
      this._stage  = stage;
      this._number = number;
   }

   //////////////////////Pipe Interface Implementation////////////////
   //
   //
   //
   public PipeData monitorPrelaunch(){  return null; }

   //
   //
   //
   public PipeData monitorIgnition(){ return null; }

   //
   //
   //
   public PipeData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
